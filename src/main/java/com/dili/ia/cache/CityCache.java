package com.dili.ia.cache;

import com.dili.ia.domain.dto.CityTreeDto;

/**
 * Created by asiamaster on 2017/10/19 0019.
 */
public class CityCache {
	
	private static final CityCache INSTANCE = new CityCache();
	
	public static CityTreeDto TREE;
	public static CityCache getInstance() {
		return INSTANCE;
	}
	
}
