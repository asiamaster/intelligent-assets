package com.dili.ia.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dili.assets.sdk.dto.CategoryDTO;
import com.dili.assets.sdk.rpc.AssetsRpc;
import com.dili.commons.glossary.EnabledStateEnum;
import com.dili.ia.domain.CategoryStorageCycle;
import com.dili.ia.domain.dto.CategoryQuery;
import com.dili.ia.domain.dto.CategoryStorageCycleDto;
import com.dili.ia.mapper.CategoryStorageCycleMapper;
import com.dili.ia.service.CategoryStorageCycleService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import com.github.pagehelper.Page;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollectionUtil;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-07-16 15:35:44.
 */
@Service
public class CategoryStorageCycleServiceImpl extends BaseServiceImpl<CategoryStorageCycle, Long> implements CategoryStorageCycleService {
	
	@Autowired
    private AssetsRpc assetsRpc;
	
    public CategoryStorageCycleMapper getActualDao() {
        return (CategoryStorageCycleMapper)getDao();
    }

	@Override
	public void saveCycle(CategoryStorageCycleDto dto) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        List<CategoryStorageCycle> insert = new ArrayList<>();
		CategoryStorageCycle categoryStorageCycle = new CategoryStorageCycle(userTicket);
    	BeanUtil.copyProperties(dto, categoryStorageCycle);    
    	insert.add(categoryStorageCycle);
    	if(dto.getAllChildren() != null && dto.getAllChildren()) {
    		CategoryQuery input = new CategoryQuery();
        	input.setMarketId(userTicket.getFirmId());
        	//input.setParent(dto.getId());
        	input.setQueryPath(dto.getPath());
        	//input.setState(1);
        	List<CategoryDTO> list = assetsRpc.list(input).getData();
        	list.forEach(item -> {
        		CategoryStorageCycle child = new CategoryStorageCycle(userTicket);
            	BeanUtil.copyProperties(item, child, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));    	
            	child.setNotes(dto.getNotes());
            	child.setCycle(dto.getCycle());
            	child.setModuleLabel(dto.getModuleLabel());
            	child.setState(dto.getState());
            	insert.add(child);
        	});
    	}
    	if(!CollectionUtils.isEmpty(insert)) {
    		Map<String, Object> map = new HashMap<String, Object>();
    		map.put("categoryStorageCycles", insert);
    		map.put("notes", dto.getNotes());
    		map.put("cycle", dto.getCycle());
    		map.put("state", dto.getState());
    		map.put("moduleLabel", dto.getModuleLabel());
    		getActualDao().insertOrUpdate(map);
    	}
	}
	
	@Override
	public Page<JSONObject> list(CategoryDTO input) {
        List<CategoryDTO> results = new ArrayList<>();
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        input.setMarketId(userTicket.getFirmId());
        //获取品类基础信息
        if (input.getParent() != null && input.getParent() != 0) {
            CategoryDTO c = assetsRpc.get(input.getParent()).getData();
            if (c.getState() != 3) {
                results.add(c);
            }
        }
        List<CategoryDTO> list = assetsRpc.list(input).getData();
        results.addAll(list);
        //获取品类id
        List<Long> ids = new ArrayList<>();
        Map<Long, CategoryDTO> mapCategory = new HashMap<>();
        results.stream().forEach(item -> {
        	ids.add(item.getId());
        	mapCategory.put(item.getId(), item);
        });
        //根据品类id获取存储周期
        Map<String, Object> queryMap = new HashMap();
        queryMap.put("ids", ids);
        queryMap.put("state", input.getState());
        List<CategoryStorageCycle> categoryStorageCycles = getActualDao().selectCycleByIds(queryMap);
        Map<Long, CategoryStorageCycle> map = new HashMap<>();
        categoryStorageCycles.forEach(item ->{
        	map.put(item.getId(), item);
        });
        
        //组装基础信息和存储周期
        Page<JSONObject> resultPage = new Page<>();
        resultPage.setTotal(list.size());
        Integer page = input.getPage();
        Integer row = input.getRows();
        List<JSONObject> array = new ArrayList<>();
        if(input.getState() != null) {
        	CollectionUtil.sub(categoryStorageCycles, (page-1)*row, page*row).stream().forEach(item -> {
            	JSONObject obj = (JSONObject) JSON.toJSON(item);
            	CategoryDTO category = mapCategory.get(item.getId());
            	if(category != null) {
            		obj.put("name", category.getName());
                	obj.put("pingying", category.getPingying());
                	obj.put("pyInitials", category.getPyInitials());
                	obj.put("parent", category.getParent());
                	obj.put("cycleState", item.getState());
            	}
            	array.add(obj);
            });
        }else {
        	CollectionUtil.sub(results, (page-1)*row, page*row).stream().forEach(item -> {
            	JSONObject obj = (JSONObject) JSON.toJSON(item);
            	CategoryStorageCycle category = map.get(item.getId());
            	if(category != null) {
            		obj.put("cycle", category.getCycle());
                	obj.put("notes", category.getNotes());
                	obj.put("moduleLabel", category.getModuleLabel());
                	obj.put("cycleState", category.getState());
            	}
            	array.add(obj);
            });
        }
        
        resultPage.addAll(array);
		return resultPage;
    }

	@Override
	public LocalDate getCategoryStorageCycle(LocalDate stockInDate,Long categoryId) {
		CategoryStorageCycle ca = new CategoryStorageCycle();
		ca.setId(categoryId);
		List<CategoryStorageCycle> li = this.listByExample(ca);
		LocalDate date = LocalDate.now();
		if(CollectionUtil.isNotEmpty(li)) {
			 date = stockInDate.plusDays(li.get(0).getCycle());
		}
		//TODO 未获取到商品周期,是否采用默认周期??
		date = stockInDate.plusDays(7);
		return date;
	}

	@Override
	public List<JSONObject> searchCategory(String keyword) {
		CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setMarketId(SessionContext.getSessionContext().getUserTicket().getFirmId());
        categoryDTO.setState(EnabledStateEnum.ENABLED.getCode());
        if (null == keyword) {
            categoryDTO.setParent(0L);
        } else {
            categoryDTO.setKeyword(keyword);
        }
        //根据关键词查询品类
        List<CategoryDTO> list = assetsRpc.list(categoryDTO).getData();
        List<Long> ids = new ArrayList<>();
        Map<Long, CategoryDTO> mapCategory = new HashMap<>();
        list.stream().forEach(item -> {
        	ids.add(item.getId());
        	mapCategory.put(item.getId(), item);
        });
        //根据品类id获取存储周期
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("ids", ids);
        queryMap.put("state", 1);
        List<CategoryStorageCycle> categoryStorageCycles = getActualDao().selectCycleByIds(queryMap);
        List<JSONObject> result = new ArrayList<JSONObject>();
        if(CollectionUtil.isNotEmpty(categoryStorageCycles)) {
        	categoryStorageCycles.stream().forEach(item -> {
        		JSONObject obj = (JSONObject) JSON.toJSON(item);
            	CategoryDTO category = mapCategory.get(item.getId());
            	if(category != null) {
            		obj.put("name", category.getName());
            		result.add(obj);
            	}
        	});
        }
		return result;
	}
}