package com.dili.ia.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dili.assets.sdk.dto.CategoryDTO;
import com.dili.ia.domain.CategoryStorageCycle;
import com.dili.ia.domain.dto.CategoryStorageCycleDto;
import com.dili.ia.mapper.CategoryStorageCycleMapper;
import com.dili.ia.rpc.AssetsRpc;
import com.dili.ia.service.CategoryStorageCycleService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.exception.BusinessException;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import com.github.pagehelper.Page;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.ModelAndView;

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
    		CategoryDTO input = new CategoryDTO();
        	input.setMarketId(userTicket.getFirmId());
        	input.setParent(dto.getId());
        	List<CategoryDTO> list = assetsRpc.list(input).getData();
        	list.forEach(item -> {
        		CategoryStorageCycle child = new CategoryStorageCycle(userTicket);
            	BeanUtil.copyProperties(item, child);    	
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
        results.stream().forEach(item -> {
        	ids.add(item.getId());
        });
        //根据品类id获取存储周期
        List<CategoryStorageCycle> categoryStorageCycles = getActualDao().selectCycleByIds(ids);
        Map<Long, CategoryStorageCycle> map = new HashMap<>();
        categoryStorageCycles.forEach(item ->{
        	map.put(item.getId(), item);
        });
        
        //组装基础信息和存储周期
        Page<JSONObject> resultPage = new Page<>();
        resultPage.setTotal(list.size());
        Integer page = input.getPage();;
        Integer row = input.getRows();;
        List<JSONObject> array = new ArrayList<>();
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
        resultPage.addAll(array);
		return resultPage;
    }

	@Override
	public CategoryStorageCycle getCategoryStorageCycle(CategoryStorageCycleDto dto) {
		CategoryStorageCycle ca = new CategoryStorageCycle();
		ca.setId(dto.getId());
		List<CategoryStorageCycle> li = this.listByExample(ca);
		if(CollectionUtil.isNotEmpty(li)) {
			return li.get(0);
		}
		//TODO 未获取到商品周期,是否采用默认周期??
		ca.setCycle(7);
		return ca;
	}
}