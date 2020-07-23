package com.dili.ia.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.dili.ia.domain.CategoryStorageCycle;
import com.dili.ss.base.MyMapper;

public interface CategoryStorageCycleMapper extends MyMapper<CategoryStorageCycle> {
	
	/**
	 * 
	 * @Title insertOrUpdate
	 * @Description 更新或修改
	 * @param map
	 * @throws
	 */
	void insertOrUpdate(Map<String, Object> map);
	
	/**
	 * 
	 * @Title selectCycleById
	 * @Description 获取品类周期
	 * @param ids
	 * @return
	 * @throws
	 */
	List<CategoryStorageCycle> selectCycleByIds(Map<String, Object> map);
	
}