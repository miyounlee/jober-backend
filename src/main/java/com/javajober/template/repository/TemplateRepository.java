package com.javajober.template.repository;


import java.util.List;

import org.springframework.data.repository.Repository;

import com.javajober.core.error.exception.Exception404;
import com.javajober.entity.Template;

public interface TemplateRepository extends Repository<Template, Long> {

	List<Template> findBySpaceWallCategoryId(Long templateCategoryId);

	default List<Template> getBySpaceWallCategoryId(final Long templateCategoryId){
		List<Template> templates = findBySpaceWallCategoryId(templateCategoryId);

		if(templates == null || templates.isEmpty()){
			throw new Exception404("추천 템플릿 그룹을 찾을 수 없습니다.");
		}

		return templates;
	}

	List<Template> findByTemplateTitleContaining(String keyword);

	default List<Template> getTemplateTitle(final String keyword){
		List<Template> templates = findByTemplateTitleContaining(keyword);

		if(templates == null || templates.isEmpty()){
			throw new Exception404( "'" + keyword + "'" + " 템플릿을 찾을 수 없습니다.");
		}

		return templates;
	}
}

