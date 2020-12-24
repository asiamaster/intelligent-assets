package com.dili.ia.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dili.assets.sdk.dto.CityDto;
import com.dili.assets.sdk.rpc.CityRpc;
import com.dili.ia.domain.dto.CityTreeDto;
import com.dili.ss.domain.BaseOutput;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @Description TODO(用一句话描述该文件做什么)
 * @author yangfan
 * @date 2020年12月16日
 */
@Controller
@RequestMapping("/city")
public class CityController {

	@Autowired
	private CityRpc cityRpc;
	
	/**
	 * 
	 * @Title cityTree
	 */
    @RequestMapping(value="/tree.action", method = RequestMethod.GET)
	public @ResponseBody BaseOutput<Object> cityTree(){
    	//CityTreeDto cacheTree = OcrmCache.getInstance().TREE;
		/*
		 * if(cacheTree != null) { return BaseOutput.successData(cacheTree); }
		 */
		List<CityDto> cityDtos = cityRpc.list(new CityDto()).getData();
		CityTreeDto tree = new CityTreeDto();
		tree.setId(100000L);
		child(cityDtos, tree);
		//OcrmCache.getInstance().TREE = tree;
		return BaseOutput.successData(tree);
	}
	
	/**
	 * 
	 * @Title child
	 */
	private void child(List<CityDto> cityDtos, CityTreeDto p) {
		List<CityTreeDto> childs = new ArrayList<>();
		Iterator<CityDto> it = cityDtos.iterator();
		while (it.hasNext()) {
			CityDto cityDto = it.next();
			CityTreeDto dto = new CityTreeDto();
			if (cityDto.getParentId().equals(p.getId())) {
				BeanUtil.copyProperties(cityDto, dto);
				childs.add(dto);
				//it.remove();
				child(cityDtos, dto);
			}
		}
		if(CollectionUtil.isNotEmpty(childs)) {
			p.setChilds(childs);
		}
		
	}
	
}
