package com.javajober.spaceWall.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.javajober.blocks.styleSetting.backgroundSetting.domain.BackgroundSetting;
import com.javajober.blocks.styleSetting.backgroundSetting.filedto.BackgroundSettingSaveRequest;
import com.javajober.blocks.styleSetting.backgroundSetting.filedto.BackgroundSettingUpdateRequest;
import com.javajober.blocks.styleSetting.backgroundSetting.repository.BackgroundSettingRepository;
import com.javajober.blocks.styleSetting.blockSetting.domain.BlockSetting;
import com.javajober.blocks.styleSetting.blockSetting.dto.request.BlockSettingSaveRequest;
import com.javajober.blocks.styleSetting.blockSetting.dto.request.BlockSettingUpdateRequest;
import com.javajober.blocks.styleSetting.blockSetting.repository.BlockSettingRepository;
import com.javajober.blocks.styleSetting.themeSetting.dto.request.ThemeSettingSaveRequest;
import com.javajober.core.exception.ApiStatus;
import com.javajober.core.exception.ApplicationException;
import com.javajober.core.util.file.FileImageService;
import com.javajober.blocks.fileBlock.domain.FileBlock;
import com.javajober.blocks.fileBlock.filedto.FileBlockSaveRequest;
import com.javajober.blocks.fileBlock.filedto.FileBlockUpdateRequest;
import com.javajober.blocks.fileBlock.repository.FileBlockRepository;
import com.javajober.blocks.freeBlock.dto.request.FreeBlockUpdateRequest;
import com.javajober.blocks.freeBlock.repository.FreeBlockRepository;
import com.javajober.blocks.listBlock.domain.ListBlock;
import com.javajober.blocks.listBlock.dto.request.ListBlockSaveRequest;
import com.javajober.blocks.listBlock.dto.request.ListBlockUpdateRequest;
import com.javajober.blocks.listBlock.repository.ListBlockRepository;
import com.javajober.member.domain.Member;
import com.javajober.member.repository.MemberRepository;
import com.javajober.blocks.snsBlock.domain.SNSBlock;
import com.javajober.blocks.snsBlock.domain.SNSType;
import com.javajober.blocks.snsBlock.dto.request.SNSBlockSaveRequest;
import com.javajober.blocks.snsBlock.dto.request.SNSBlockUpdateRequest;
import com.javajober.blocks.snsBlock.repository.SNSBlockRepository;
import com.javajober.space.domain.AddSpace;
import com.javajober.space.repository.AddSpaceRepository;
import com.javajober.spaceWall.domain.BlockType;
import com.javajober.spaceWall.domain.FlagType;
import com.javajober.spaceWall.domain.SpaceWall;
import com.javajober.spaceWall.dto.request.BlockSaveRequest;
import com.javajober.spaceWall.filedto.DataSaveRequest;
import com.javajober.spaceWall.filedto.DataUpdateRequest;
import com.javajober.spaceWall.filedto.SpaceWallSaveRequest;
import com.javajober.spaceWall.dto.response.SpaceWallSaveResponse;
import com.javajober.spaceWall.filedto.SpaceWallUpdateRequest;
import com.javajober.spaceWall.repository.SpaceWallRepository;
import com.javajober.spaceWall.spaceWallCategory.domain.SpaceWallCategoryType;
import com.javajober.blocks.styleSetting.domain.StyleSetting;
import com.javajober.blocks.styleSetting.filedto.StyleSettingSaveRequest;
import com.javajober.blocks.styleSetting.filedto.StyleSettingUpdateRequest;
import com.javajober.blocks.styleSetting.repository.StyleSettingRepository;
import com.javajober.blocks.templateBlock.domain.TemplateBlock;
import com.javajober.blocks.templateBlock.dto.request.TemplateBlockSaveRequest;
import com.javajober.blocks.templateBlock.dto.request.TemplateBlockUpdateRequest;
import com.javajober.blocks.templateBlock.repository.TemplateBlockRepository;
import com.javajober.blocks.styleSetting.themeSetting.domain.ThemeSetting;
import com.javajober.blocks.styleSetting.themeSetting.dto.request.ThemeSettingUpdateRequest;
import com.javajober.blocks.styleSetting.themeSetting.repository.ThemeSettingRepository;
import com.javajober.blocks.wallInfoBlock.domain.WallInfoBlock;
import com.javajober.blocks.wallInfoBlock.filedto.WallInfoBlockSaveRequest;
import com.javajober.blocks.wallInfoBlock.filedto.WallInfoBlockUpdateRequest;
import com.javajober.blocks.wallInfoBlock.repository.WallInfoBlockRepository;
import com.javajober.spaceWall.strategy.BlockJsonProcessor;
import com.javajober.spaceWall.strategy.BlockStrategyFactory;
import com.javajober.spaceWall.strategy.BlockStrategyName;
import com.javajober.spaceWall.strategy.FixBlockStrategy;
import com.javajober.spaceWall.strategy.MoveBlockStrategy;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class FileUploadService {
    private static final Long INITIAL_POSITION = 1L;
    private final SpaceWallRepository spaceWallRepository;
    private final SNSBlockRepository snsBlockRepository;
    private final FreeBlockRepository freeBlockRepository;
    private final TemplateBlockRepository templateBlockRepository;
    private final WallInfoBlockRepository wallInfoBlockRepository;
    private final FileBlockRepository fileBlockRepository;
    private final ListBlockRepository listBlockRepository;
    private final StyleSettingRepository styleSettingRepository;
    private final BackgroundSettingRepository backgroundSettingRepository;
    private final BlockSettingRepository blockSettingRepository;
    private final ThemeSettingRepository themeSettingRepository;
    private final MemberRepository memberRepository;
    private final AddSpaceRepository addSpaceRepository;
    private final FileImageService fileImageService;
    private final BlockStrategyFactory blockStrategyFactory;
    private final BlockJsonProcessor blockJsonProcessor;

    public FileUploadService(final SpaceWallRepository spaceWallRepository, final SNSBlockRepository snsBlockRepository,
                             final FreeBlockRepository freeBlockRepository, final TemplateBlockRepository templateBlockRepository,
                             final WallInfoBlockRepository wallInfoBlockRepository, final FileBlockRepository fileBlockRepository,
                             final ListBlockRepository listBlockRepository, final FileImageService fileImageService,
                             final StyleSettingRepository styleSettingRepository, final BackgroundSettingRepository backgroundSettingRepository,
                             final BlockSettingRepository blockSettingRepository, final ThemeSettingRepository themeSettingRepository,
                             final MemberRepository memberRepository, final AddSpaceRepository addSpaceRepository,
        BlockStrategyFactory blockStrategyFactory, BlockJsonProcessor blockJsonProcessor) {

        this.spaceWallRepository = spaceWallRepository;
        this.snsBlockRepository = snsBlockRepository;
        this.freeBlockRepository = freeBlockRepository;
        this.templateBlockRepository = templateBlockRepository;
        this.wallInfoBlockRepository = wallInfoBlockRepository;
        this.fileBlockRepository = fileBlockRepository;
        this.listBlockRepository = listBlockRepository;
        this.styleSettingRepository = styleSettingRepository;
        this.backgroundSettingRepository = backgroundSettingRepository;
        this.blockSettingRepository = blockSettingRepository;
        this.themeSettingRepository = themeSettingRepository;
        this.memberRepository = memberRepository;
        this.addSpaceRepository = addSpaceRepository;
        this.fileImageService = fileImageService;
        this.blockStrategyFactory = blockStrategyFactory;
        this.blockJsonProcessor = blockJsonProcessor;
    }

    @Transactional
    public SpaceWallSaveResponse save(final Long memberId, final SpaceWallSaveRequest spaceWallRequest, FlagType flagType,
                                      final List<MultipartFile> files, final MultipartFile backgroundImgURL,
                                      final MultipartFile wallInfoImgURL, final MultipartFile styleImgURL) {

        Member member = memberRepository.findMember(memberId);

        DataSaveRequest data = spaceWallRequest.getData();

        AddSpace addSpace = addSpaceRepository.findAddSpace(data.getSpaceId());

        validateSpaceOwnership(member, addSpace);

        validateAddSpaceId(addSpace.getId());

        SpaceWallCategoryType spaceWallCategoryType = SpaceWallCategoryType.findSpaceWallCategoryTypeByString(data.getCategory());

        ArrayNode blockInfoArray = blockJsonProcessor.createArrayNode();

        AtomicLong blocksPositionCounter = new AtomicLong(INITIAL_POSITION);

        processWallInfoBlock(backgroundImgURL, wallInfoImgURL, data, blockInfoArray, blocksPositionCounter);

        List<BlockSaveRequest<?>> blocks = data.getBlocks();

        processBlocks(blocks, blockInfoArray, blocksPositionCounter, files);

        processStyleSettingBlock(styleImgURL, data, blockInfoArray, blocksPositionCounter);

        String shareURL = spaceWallRequest.getData().getShareURL();

        Long spaceWallId = saveSpaceWall(spaceWallCategoryType, member, addSpace, shareURL, flagType, blockInfoArray);

        return new SpaceWallSaveResponse(spaceWallId);
    }

    private void validateSpaceOwnership(final Member member, final AddSpace addSpace) {
        Long memberId = member.getId();
        Long spaceId = addSpace.getMember().getId();

        if (!memberId.equals(spaceId)) {
            throw new ApplicationException(ApiStatus.INVALID_DATA, "사용자의 스페이스를 찾을 수가 없습니다.");
        }
    }

    private void validateAddSpaceId (final Long spaceId) {
        boolean existsSpaceId = spaceWallRepository.existsByAddSpaceId(spaceId);
        if (existsSpaceId) {
            throw new ApplicationException(ApiStatus.INVALID_DATA, "스페이스 하나당 공유페이지 하나만 생성 가능합니다.");
        }
    }

    private void processWallInfoBlock(final MultipartFile backgroundImgURL, final MultipartFile wallInfoImgURL,
        final DataSaveRequest data, final ArrayNode blockInfoArray,
        final AtomicLong blocksPositionCounter) {

        String wallInfoBlockStrategyName = BlockType.WALL_INFO_BLOCK.getStrategyName();
        FixBlockStrategy wallInfoBlockStrategy = blockStrategyFactory.findFixBlockStrategy(wallInfoBlockStrategyName);

        wallInfoBlockStrategy.uploadTwoFiles(backgroundImgURL, wallInfoImgURL);

        Long wallInfoBlockPosition = blocksPositionCounter.getAndIncrement();
        wallInfoBlockStrategy.saveBlocks(data, blockInfoArray, wallInfoBlockPosition);
    }

    private void processBlocks(final List<BlockSaveRequest<?>> blocks, final ArrayNode blockInfoArray,
        final AtomicLong blocksPositionCounter, final List<MultipartFile> files) {
        try {
            AtomicInteger fileIndexCounter = new AtomicInteger();

            blocks.forEach(block -> {

                BlockType blockType = BlockType.findBlockTypeByString(block.getBlockType());
                Long position = blocksPositionCounter.getAndIncrement();

                String strategyName = blockType.getStrategyName();
                MoveBlockStrategy blockProcessingStrategy = blockStrategyFactory.findMoveBlockStrategy(strategyName);

                if (BlockStrategyName.FileBlockStrategy.name().equals(blockProcessingStrategy.getStrategyName())) {
                    blockProcessingStrategy.uploadFile(files.get(fileIndexCounter.getAndIncrement()));
                }

                blockProcessingStrategy.saveBlocks(block, blockInfoArray, position);
            });
        } catch (IndexOutOfBoundsException e) {
            throw new ApplicationException(ApiStatus.INVALID_DATA, "파일이 첨부되지 않은 파일블록이 있습니다.");
        }
    }

    private void processStyleSettingBlock(final MultipartFile styleImgURL, final DataSaveRequest data, final ArrayNode blockInfoArray,
        final AtomicLong blocksPositionCounter) {

        String styleSettingBlockStrategyName = BlockType.STYLE_SETTING.getStrategyName();
        FixBlockStrategy styleSettingBlockStrategy = blockStrategyFactory.findFixBlockStrategy(styleSettingBlockStrategyName);

        styleSettingBlockStrategy.uploadSingleFile(styleImgURL);

        Long styleSettingPosition = blocksPositionCounter.getAndIncrement();
        styleSettingBlockStrategy.saveBlocks(data, blockInfoArray, styleSettingPosition);
    }
    private Long saveSpaceWall(final SpaceWallCategoryType spaceWallCategoryType, final Member member, final AddSpace addSpace, final String shareURL, final FlagType flagType, final ArrayNode blockInfoArray) {

        String blockInfoArrayAsString = blockInfoArray.toString();
        SpaceWall spaceWall = SpaceWallSaveRequest.toEntity(spaceWallCategoryType, member, addSpace, shareURL, flagType, blockInfoArrayAsString);

        return spaceWallRepository.save(spaceWall).getId();
    }

    @Transactional
    public SpaceWallSaveResponse update(final Long memberId, final SpaceWallUpdateRequest spaceWallRequest, FlagType flagType,
                                        final List<MultipartFile> files, final MultipartFile backgroundImgURL,
                                        final MultipartFile wallInfoImgURL, final MultipartFile styleImgURL){

        DataUpdateRequest dataUpdateRequest = spaceWallRequest.getData();

        Long spaceWallId = dataUpdateRequest.getSpaceWallId();
        Long addSpaceId = dataUpdateRequest.getSpaceId();

        memberRepository.findMember(memberId);
        addSpaceRepository.findAddSpace(addSpaceId);

        SpaceWall spaceWall = spaceWallRepository.findSpaceWall(spaceWallId, addSpaceId, memberId, flagType);

        Long blocksPosition = 2L;
        AtomicLong blocksPositionCounter = new AtomicLong(blocksPosition);
        ObjectMapper jsonMapper = new ObjectMapper();
        ArrayNode blockInfoArray = jsonMapper.createArrayNode();
        AtomicInteger i = new AtomicInteger();

        WallInfoBlockUpdateRequest wallInfoBlockRequest = spaceWallRequest.getData().getWallInfoBlock();
        Long wallInfoBlock = updateWallInfoBlock(wallInfoBlockRequest, backgroundImgURL, wallInfoImgURL);
        String wallInfoBlockType  = BlockType.WALL_INFO_BLOCK.getEngTitle();
        Long blockStartPosition = 1L;
        addBlockToJsonArray(blockInfoArray, jsonMapper, blockStartPosition, wallInfoBlockType, wallInfoBlock);

        spaceWallRequest.getData().getBlocks().forEach(block -> {
            BlockType blockType = BlockType.findBlockTypeByString(block.getBlockType());
            Long position = blocksPositionCounter.getAndIncrement();
            switch (blockType) {
                case FREE_BLOCK:
                    List<FreeBlockUpdateRequest> freeBlockRequests = jsonMapper.convertValue(block.getSubData(), new TypeReference<List<FreeBlockUpdateRequest>>() {});
                    List<Long> updateFreeBlockIds = updateFreeBlocks(freeBlockRequests);
                    updateFreeBlockIds.forEach(freeBlockId -> addBlockInfoToArray(blockInfoArray, jsonMapper, blockType, position, freeBlockId, block));
                    break;
                case SNS_BLOCK:
                    List<SNSBlockUpdateRequest> snsBlockRequests = jsonMapper.convertValue(block.getSubData(), new TypeReference<List<SNSBlockUpdateRequest>>() {});
                    List<Long> updateSnsBlockIds = updateSnsBlocks(snsBlockRequests);
                    updateSnsBlockIds.forEach(snsBlockId -> addBlockInfoToArray(blockInfoArray, jsonMapper, blockType, position, snsBlockId, block));
                    break;
                case TEMPLATE_BLOCK:
                    List<TemplateBlockUpdateRequest> templateBlockRequests = jsonMapper.convertValue(block.getSubData(), new TypeReference<List<TemplateBlockUpdateRequest>>() {});
                    List<Long> updateTemplateBlockIds = updateTemplateBlocks(templateBlockRequests);
                    updateTemplateBlockIds.forEach(templateBlockId -> addBlockInfoToArray(blockInfoArray, jsonMapper, blockType, position, templateBlockId, block));
                    break;
                case FILE_BLOCK:
                    List<FileBlockUpdateRequest> fileBlockRequests = jsonMapper.convertValue(block.getSubData(), new TypeReference<List<FileBlockUpdateRequest>>() {});
                    List<Long> updateFileBlockIds = updateFileBlocks(fileBlockRequests, files.get(i.getAndIncrement()));
                    updateFileBlockIds.forEach(fileBlockId -> addBlockInfoToArray(blockInfoArray, jsonMapper, blockType, position, fileBlockId, block));
                    break;
                case LIST_BLOCK:
                    List<ListBlockUpdateRequest> listBlockRequests = jsonMapper.convertValue(block.getSubData(), new TypeReference<List<ListBlockUpdateRequest>>() {});
                    List<Long> updateListBlockIds = updateListBlock(listBlockRequests);
                    updateListBlockIds.forEach(listBlockId -> addBlockInfoToArray(blockInfoArray, jsonMapper, blockType, position, listBlockId, block));
            }
        });

        StyleSettingUpdateRequest styleSettingUpdateRequest = dataUpdateRequest.getStyleSetting();
        Long styleSetting = updateStyleSetting(styleSettingUpdateRequest, styleImgURL);
        String styleSettingString = "styleSetting";
        Long stylePosition = blocksPositionCounter.getAndIncrement();

        addBlockToJsonArray(blockInfoArray, jsonMapper, stylePosition, styleSettingString, styleSetting);
        String blocks = blockInfoArray.toString();

        spaceWall.fileUpdate(dataUpdateRequest, flagType, blocks);
        spaceWallId = spaceWallRepository.save(spaceWall).getId();

        return new SpaceWallSaveResponse(spaceWallId);
    }

    private Long saveWallInfoBlock(final WallInfoBlockSaveRequest wallInfoBlockSaveRequest, MultipartFile backgroundImgURL, MultipartFile wallInfoImgURL) {

        String backgroundImgName = fileImageService.uploadFile(backgroundImgURL);
        String wallInfoImgName = fileImageService.uploadFile(wallInfoImgURL);
        WallInfoBlock wallInfoBlock = WallInfoBlockSaveRequest.toEntity(wallInfoBlockSaveRequest, backgroundImgName, wallInfoImgName);

        return wallInfoBlockRepository.save(wallInfoBlock).getId();
    }

    private List<Long> saveFreeBlocks(final List<com.javajober.blocks.freeBlock.dto.request.FreeBlockSaveRequest> subData) {

        List<Long> freeBlockIds = new ArrayList<>();
        subData.forEach(block -> {
            com.javajober.blocks.freeBlock.domain.FreeBlock freeBlock = com.javajober.blocks.freeBlock.dto.request.FreeBlockSaveRequest.toEntity(block);
            freeBlockIds.add(freeBlockRepository.save(freeBlock).getId());
        });
        return freeBlockIds;
    }

    private List<Long> saveSnsBlocks(final List<SNSBlockSaveRequest> subData) {

        List<Long> snsBlockIds = new ArrayList<>();

        subData.forEach(block -> {
            SNSBlock snsBlock = SNSBlockSaveRequest.toEntity(block);
            snsBlockIds.add(snsBlockRepository.save(snsBlock).getId());
        });
        return snsBlockIds;
    }

    private List<Long> saveTemplateBlocks(final List<TemplateBlockSaveRequest> subData) {

        List<Long> templateBlockIds = new ArrayList<>();

        subData.forEach(block -> {
            TemplateBlock templateBlock = TemplateBlockSaveRequest.toEntity(block);
            templateBlockIds.add(templateBlockRepository.save(templateBlock).getId());
        });
        return templateBlockIds;
    }

    private List<Long> saveFileBlocks(final List<FileBlockSaveRequest> subData, MultipartFile file) {

        String fileName = fileImageService.uploadFile(file);
        List<Long> fileBlockIds = new ArrayList<>();
        subData.forEach(block -> {
            FileBlock fileBlock = FileBlockSaveRequest.toEntity(block, fileName);
            fileBlockIds.add(fileBlockRepository.save(fileBlock).getId());
        });
        return fileBlockIds;
    }

    private List<Long> saveListBlocks(final List<ListBlockSaveRequest> subData) {

        List<Long> listBlockIds = new ArrayList<>();
        subData.forEach(block -> {
            ListBlock listBlock = ListBlockSaveRequest.toEntity(block);
            listBlockIds.add(listBlockRepository.save(listBlock).getId());
        });
        return listBlockIds;
    }

    private Long saveStyleSetting(final StyleSettingSaveRequest request, MultipartFile styleImgURL){

        BackgroundSettingSaveRequest backgroundRequest = request.getBackgroundSetting();
        String styleImgName = fileImageService.uploadFile(styleImgURL);
        BackgroundSetting backgroundSetting = backgroundSettingRepository.save(BackgroundSettingSaveRequest.toEntity(backgroundRequest, styleImgName));

        BlockSettingSaveRequest blockSettingRequest = request.getBlockSetting();
        BlockSetting blockSetting = blockSettingRepository.save(BlockSettingSaveRequest.toEntity(blockSettingRequest));

        ThemeSettingSaveRequest themeSettingRequest = request.getThemeSetting();
        ThemeSetting themeSetting = themeSettingRepository.save(ThemeSettingSaveRequest.toEntity(themeSettingRequest));

        StyleSetting styleSetting = request.toEntity(backgroundSetting, blockSetting, themeSetting);

        return styleSettingRepository.save(styleSetting).getId();
    }

    private Long updateWallInfoBlock(final WallInfoBlockUpdateRequest wallInfoBlockRequest, MultipartFile backgroundImgURL, MultipartFile wallInfoImgURL) {

        WallInfoBlock wallInfoBlock = wallInfoBlockRepository.findWallInfoBlock(wallInfoBlockRequest.getWallInfoBlockId());
        String backgroundImgName = fileImageService.uploadFile(backgroundImgURL);
        String wallInfoImgName = fileImageService.uploadFile(wallInfoImgURL);
        wallInfoBlock.update(wallInfoBlockRequest,backgroundImgName, wallInfoImgName);

        return wallInfoBlockRepository.save(wallInfoBlock).getId();
    }

    private List<Long> updateFreeBlocks(final List<FreeBlockUpdateRequest> subData) {

        List<Long> updatedFreeBlockIds = new ArrayList<>();
        for (FreeBlockUpdateRequest updateRequest : subData) {
            if(updateRequest.getFreeBlockId() == null ){
                com.javajober.blocks.freeBlock.domain.FreeBlock freeBlock = new com.javajober.blocks.freeBlock.domain.FreeBlock(updateRequest.getFreeTitle(),updateRequest.getFreeContent());
                updatedFreeBlockIds.add(freeBlockRepository.save(freeBlock).getId());
            }else {
                com.javajober.blocks.freeBlock.domain.FreeBlock freeBlock = freeBlockRepository.findFreeBlock(updateRequest.getFreeBlockId());
                freeBlock.update(updateRequest);
                updatedFreeBlockIds.add(freeBlockRepository.save(freeBlock).getId());
            }
        }
        return updatedFreeBlockIds;
    }

    private List<Long> updateSnsBlocks(final List<SNSBlockUpdateRequest> subData){

        List<Long> updateSnsBlockIds = new ArrayList<>();
        subData.forEach(snsBlockRequest -> {
            if(snsBlockRequest.getSnsBlockId() ==null){
                SNSType snsType = SNSType.findSNSTypeByString(snsBlockRequest.getSnsType());
                SNSBlock snsBlock = new SNSBlock(snsBlockRequest.getSnsUUID(),snsType,snsBlockRequest.getSnsURL());
                updateSnsBlockIds.add(snsBlockRepository.save(snsBlock).getId());
            }else {
                SNSBlock snsBlock = snsBlockRepository.findSNSBlock(snsBlockRequest.getSnsBlockId());
                snsBlock.update(snsBlockRequest);
                updateSnsBlockIds.add(snsBlockRepository.save(snsBlock).getId());
            }
        });

        return updateSnsBlockIds;
    }

    private List<Long> updateTemplateBlocks(final List<TemplateBlockUpdateRequest> subData) {

        List<Long> updateTemplateBlockIds = new ArrayList<>();
        for(TemplateBlockUpdateRequest updateRequest : subData) {
            if (updateRequest.getTemplateBlockId() == null) {
                TemplateBlock templateBlock = new TemplateBlock(updateRequest.getTemplateUUID(),updateRequest.getTemplateTitle(),updateRequest.getTemplateDescription());
                updateTemplateBlockIds.add(templateBlockRepository.save(templateBlock).getId());
            } else {
                TemplateBlock templateBlock = templateBlockRepository.findTemplateBlock(updateRequest.getTemplateBlockId());
                templateBlock.update(updateRequest.getTemplateUUID(), updateRequest.getTemplateTitle(), updateRequest.getTemplateDescription());
                updateTemplateBlockIds.add(templateBlockRepository.save(templateBlock).getId());
            }
        }
        return updateTemplateBlockIds;
    }

    private List<Long> updateFileBlocks(final List<FileBlockUpdateRequest> subData, MultipartFile file) {

        String fileName = fileImageService.uploadFile(file);
        List<Long> updateFileBlockIds = new ArrayList<>();
        for (FileBlockUpdateRequest updateRequest : subData) {
            if(updateRequest.getFileBlockId() == null){
                FileBlock fileBlock = new FileBlock(updateRequest.getFileTitle(), updateRequest.getFileDescription(),fileName);
                updateFileBlockIds.add(fileBlockRepository.save(fileBlock).getId());
            }else{
                FileBlock fileBlock = fileBlockRepository.findFileBlock(updateRequest.getFileBlockId());
                fileBlock.fileUpdate(updateRequest, fileName);
                updateFileBlockIds.add(fileBlockRepository.save(fileBlock).getId());
            }
        }
        return updateFileBlockIds;
    }

    private List<Long> updateListBlock(final List<ListBlockUpdateRequest> subData){

        List<Long> updateListBlockIds = new ArrayList<>();
        for(ListBlockUpdateRequest updateRequest : subData){
            if(updateRequest.getListBlockId() == null){
                ListBlock listBlock = new ListBlock(updateRequest.getListUUID(),updateRequest.getListLabel(),updateRequest.getListTitle(),updateRequest.getListDescription(),updateRequest.getIsLink());
                updateListBlockIds.add(listBlockRepository.save(listBlock).getId());
            }else{
                ListBlock listBlock = listBlockRepository.findListBlock(updateRequest.getListBlockId());
                listBlock.update(updateRequest);
                updateListBlockIds.add(listBlockRepository.save(listBlock).getId());
            }
        }
        return updateListBlockIds;
    }

    private Long updateStyleSetting(final StyleSettingUpdateRequest updateRequest, MultipartFile styleImgURL){

        StyleSetting styleSetting = styleSettingRepository.findStyleBlock(updateRequest.getStyleSettingBlockId());
        updateBackgroundSetting(updateRequest.getBackgroundSetting(), styleImgURL);
        updateBlockSetting(updateRequest.getBlockSetting());
        updateThemeSetting(updateRequest.getThemeSetting());
        styleSetting.update(styleSetting);
        return styleSettingRepository.save(styleSetting).getId();
    }

    private Long updateBackgroundSetting(final BackgroundSettingUpdateRequest updateRequest, MultipartFile styleImgURL){

        String styleImgName = fileImageService.uploadFile(styleImgURL);
        BackgroundSetting backgroundSetting = backgroundSettingRepository.getById(updateRequest.getBackgroundSettingBlockId());
        backgroundSetting.update(updateRequest.getSolidColor(), updateRequest.getGradation(), styleImgName);
        return backgroundSettingRepository.save(backgroundSetting).getId();
    }

    private Long updateBlockSetting(final BlockSettingUpdateRequest updateRequest){

        BlockSetting blockSetting = blockSettingRepository.getById(updateRequest.getBlockSettingBlockId());
        blockSetting.update(updateRequest);
        return blockSettingRepository.save(blockSetting).getId();
    }

    private Long updateThemeSetting(final ThemeSettingUpdateRequest updateRequest){

        ThemeSetting themeSetting = themeSettingRepository.getById(updateRequest.getThemeSettingBlockId());
        themeSetting.update(updateRequest.getTheme());
        return themeSettingRepository.save(themeSetting).getId();
    }

    private void addBlockInfoToArray(final ArrayNode blockInfoArray, final ObjectMapper jsonMapper, final BlockType blockType, final Long position, final Long blockId, final BlockSaveRequest block) {

        String currentBlockTypeTitle = blockType.getEngTitle();
        String blockUUID = block.getBlockUUID();

        ObjectNode blockInfoObject = jsonMapper.createObjectNode();

        blockInfoObject.put("position", position);
        blockInfoObject.put("blockType", currentBlockTypeTitle);
        blockInfoObject.put("blockId", blockId);
        blockInfoObject.put("blockUUID", blockUUID);

        blockInfoArray.add(blockInfoObject);
    }

    private void addBlockToJsonArray(final ArrayNode jsonArray, final ObjectMapper mapper, final Long position, final String blockType, final Long blockId) {

        ObjectNode blockInfoObject = mapper.createObjectNode();

        blockInfoObject.put("position", position);
        blockInfoObject.put("blockType", blockType);
        blockInfoObject.put("blockId", blockId);
        blockInfoObject.put("blockUUID", (String) null);

        jsonArray.add(blockInfoObject);
    }
}