package com.dili.ia.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.ModelAttribute;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dili.assets.sdk.dto.CategoryDTO;
import com.dili.ia.domain.CategoryStorageCycle;
import com.dili.ia.domain.dto.CategoryStorageCycleDto;
import com.dili.ss.base.BaseService;
import com.github.pagehelper.Page;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-07-16 15:35:44.
 */
public interface CategoryStorageCycleService extends BaseService<CategoryStorageCycle, Long> {
	
	/**
	 * 
	 * @Title saveCycle
	 * @Description 保存存储周期
	 * @param dto
	 * @throws
	 */
	void saveCycle(CategoryStorageCycleDto dto); 
	
	/**
	 * 
	 * @Title list
	 * @Description 获取品类信息以及存储周期列表
	 * @param input
	 * @return
	 * @throws
	 */
	Page<JSONObject> list(CategoryDTO input);
	
	/**
	 * 
	 * @Title getCategoryStorageCycle
	 * @Description 获取品类存储周期
	 * @param dto
	 * @return
	 * @throws
	 */
	LocalDate getCategoryStorageCycle(LocalDate stockInDate,Long categoryId);
}