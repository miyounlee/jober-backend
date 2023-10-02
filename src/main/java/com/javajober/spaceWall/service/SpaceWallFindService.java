package com.javajober.spaceWall.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javajober.backgroundSetting.dto.response.BackgroundSettingResponse;
import com.javajober.blockSetting.dto.response.BlockSettingResponse;
import com.javajober.core.util.CommonResponse;
import com.javajober.fileBlock.domain.FileBlock;
import com.javajober.fileBlock.dto.response.FileBlockResponse;
import com.javajober.fileBlock.repository.FileBlockRepository;
import com.javajober.freeBlock.domain.FreeBlock;
import com.javajober.freeBlock.dto.response.FreeBlockResponse;
import com.javajober.freeBlock.repository.FreeBlockRepository;
import com.javajober.listBlock.domain.ListBlock;
import com.javajober.listBlock.dto.response.ListBlockResponse;
import com.javajober.listBlock.repository.ListBlockRepository;
import com.javajober.snsBlock.domain.SNSBlock;
import com.javajober.snsBlock.dto.response.SNSBlockResponse;
import com.javajober.snsBlock.repository.SNSBlockRepository;
import com.javajober.spaceWall.domain.BlockType;
import com.javajober.spaceWall.domain.SpaceWall;
import com.javajober.spaceWall.dto.response.BlockResponse;
import com.javajober.spaceWall.dto.response.SpaceWallResponse;
import com.javajober.spaceWall.repository.SpaceWallRepository;
import com.javajober.styleSetting.domain.StyleSetting;
import com.javajober.styleSetting.dto.response.StyleSettingResponse;
import com.javajober.styleSetting.repository.StyleSettingRepository;
import com.javajober.templateBlock.domain.TemplateBlock;
import com.javajober.templateBlock.dto.response.TemplateBlockResponse;
import com.javajober.templateBlock.repository.TemplateBlockRepository;
import com.javajober.themeSetting.dto.response.ThemeSettingResponse;
import com.javajober.wallInfoBlock.domain.WallInfoBlock;
import com.javajober.wallInfoBlock.dto.response.WallInfoBlockResponse;
import com.javajober.wallInfoBlock.repository.WallInfoBlockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class SpaceWallFindService {

    private final SpaceWallRepository spaceWallRepository;
    private final SNSBlockRepository snsBlockRepository;
    private final FreeBlockRepository freeBlockRepository;
    private final TemplateBlockRepository templateBlockRepository;
    private final WallInfoBlockRepository wallInfoBlockRepository;
    private final FileBlockRepository fileBlockRepository;
    private final ListBlockRepository listBlockRepository;
    private final StyleSettingRepository styleSettingRepository;

    public SpaceWallFindService(
            final SpaceWallRepository spaceWallRepository, final SNSBlockRepository snsBlockRepository,
            final FreeBlockRepository freeBlockRepository, final TemplateBlockRepository templateBlockRepository,
            final WallInfoBlockRepository wallInfoBlockRepository, final FileBlockRepository fileBlockRepository,
            final ListBlockRepository listBlockRepository, final StyleSettingRepository styleSettingRepository) {

        this.spaceWallRepository = spaceWallRepository;
        this.snsBlockRepository = snsBlockRepository;
        this.freeBlockRepository = freeBlockRepository;
        this.templateBlockRepository = templateBlockRepository;
        this.wallInfoBlockRepository = wallInfoBlockRepository;
        this.fileBlockRepository = fileBlockRepository;
        this.listBlockRepository = listBlockRepository;
        this.styleSettingRepository = styleSettingRepository;
    }

    /**
     * Lazy 로딩이 사용된 여러 엔티티를 가져오고 있어서 LazyInitializationException을 예방하고자 @Transaction 사용
     * -> 이 메서드 내에서 수행되는 모든 db 연산이 하나의 트랜잭션으로 처리 되기 때문에
     * -> 연관 엔티티에 안전하게 접근 가능
     */
    @Transactional
    public SpaceWallResponse find(@PathVariable Long memberId, @PathVariable Long addSpaceId,
                                  @PathVariable Long spaceWallId) throws JsonProcessingException {

        // 1. 요청에 맞는 blocks 컬럼 조회
        SpaceWall spaceWall = spaceWallRepository.findSpaceWall(spaceWallId, addSpaceId, memberId);
        String blocksPS = spaceWall.getBlocks();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(blocksPS);
        List<BlockResponse<CommonResponse>> blocks = new ArrayList<>();
        WallInfoBlockResponse wallInfoBlockResponse = new WallInfoBlockResponse();
        StyleSettingResponse styleSettingResponse = new StyleSettingResponse();

        // 2. position 기준으로 오름차순 정렬 후 position이 같은 객체들 그룹화
        Map<Long, List<JsonNode>> groupedNodesByPosition = StreamSupport.stream(rootNode.spliterator(), false)
                .sorted((a, b) -> a.get("position").asInt() - b.get("position").asInt())
                .collect(Collectors.groupingBy(node -> (long) node.get("position").asInt()));

        Long maxPosition = groupedNodesByPosition.keySet().stream()
                .max(Long::compareTo)
                .orElse(null);

        // 3. blocks 리스트 형태로 저장
        for (Map.Entry<Long, List<JsonNode>> entry : groupedNodesByPosition.entrySet()) {
            // position 이 1(wallInfo)이거나 마지막 값(styleSetting)일 경우 continue
            Long currentPosition = entry.getKey();
            if (currentPosition.equals(1L)) {
                wallInfoBlockResponse = getWallInfoBlockDTO(entry);
                continue;
            }
            if (currentPosition.equals(maxPosition)) {
                styleSettingResponse = getStyleSettingDTO(entry);
                continue;
            }

            String blockUUID = "";
            String blockTypeString = "";
            List<JsonNode> nodesWithSamePosition = entry.getValue();
            List<CommonResponse> subData = new ArrayList<>();

            // 4. subData 로 저장 - 같은 포지션 별 블록 entity 조회
            for (JsonNode node : nodesWithSamePosition) {

                Long blockId = node.get("blockId").asLong();
                blockUUID = node.get("blockUUID").asText();

                blockTypeString = node.get("blockType").asText();
                BlockType blockType = BlockType.findBlockTypeByString(blockTypeString);

                // entity 조회 후 dto로 변환
                subData.add(getBlockDTO(blockType, blockId));
            }
            BlockResponse<CommonResponse> blockResponse = BlockResponse.builder()
                    .blockUUID(blockUUID)
                    .blockType(blockTypeString)
                    .subData(subData)
                    .build();

            blocks.add(blockResponse);
        } // blocks 다 담김

        return SpaceWallResponse.from(spaceWall.getSpaceWallCategoryType().getEngTitle(), spaceWall.getMember().getId(),
                spaceWall.getAddSpace().getId(), spaceWall.getShareURL(), wallInfoBlockResponse, blocks, styleSettingResponse);

    }

    private CommonResponse getBlockDTO(BlockType blockType, Long blockId) {
        switch (blockType) {
            case FREE_BLOCK:
                FreeBlock freeBlock = freeBlockRepository.getById(blockId);
                return FreeBlockResponse.from(freeBlock);
            case SNS_BLOCK:
                SNSBlock snsBlock = snsBlockRepository.findSNSBlock(blockId);
                return SNSBlockResponse.from(snsBlock);
            case FILE_BLOCK:
                FileBlock fileBlock = fileBlockRepository.getById(blockId);
                return FileBlockResponse.from(fileBlock);
            case LIST_BLOCK:
                ListBlock listBlock = listBlockRepository.getById(blockId);
                return ListBlockResponse.from(listBlock);
            case TEMPLATE_BLOCK:
                TemplateBlock templateBlock = templateBlockRepository.getById(blockId);
                return TemplateBlockResponse.from(templateBlock, new ArrayList<>(), new ArrayList<>()); // TODO : 권한 빈 리스트로 출력
        }
        return null;
    }

    private WallInfoBlockResponse getWallInfoBlockDTO(Map.Entry<Long, List<JsonNode>> entry) {
        Long blockId = entry.getValue().get(0).get("blockId").asLong();
        WallInfoBlock wallInfoBlock = wallInfoBlockRepository.getById(blockId);
        return WallInfoBlockResponse.from(wallInfoBlock);
    }

    private StyleSettingResponse getStyleSettingDTO(Map.Entry<Long, List<JsonNode>> entry) {
        Long styleSettingId = entry.getValue().get(0).get("blockId").asLong();
        StyleSetting styleSetting = styleSettingRepository.getById(styleSettingId);

        BackgroundSettingResponse backgroundSettingResponse = BackgroundSettingResponse.from(styleSetting.getBackgroundSetting());
        BlockSettingResponse blockSettingResponse = BlockSettingResponse.from(styleSetting.getBlockSetting());
        ThemeSettingResponse themeSettingResponse = ThemeSettingResponse.from(styleSetting.getThemeSetting());

        return StyleSettingResponse.from(backgroundSettingResponse, blockSettingResponse, themeSettingResponse);
    }
}
