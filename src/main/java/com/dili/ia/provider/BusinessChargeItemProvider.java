//package com.dili.ia.provider;
//
//import com.alibaba.fastjson.JSONObject;
//import com.dili.ia.service.BusinessChargeItemService;
//import com.dili.ss.dto.DTOUtils;
//import com.dili.ss.metadata.BatchProviderMeta;
//import com.dili.ss.metadata.FieldMeta;
//import com.dili.ss.metadata.ValuePair;
//import com.dili.ss.metadata.ValuePairImpl;
//import com.dili.ss.metadata.provider.BatchDisplayTextProviderSupport;
//import com.dili.uap.domain.Resource;
//import com.dili.uap.domain.dto.ResourceDto;
//import com.dili.uap.service.ResourceService;
//import com.google.common.collect.Maps;
//import org.apache.commons.collections.map.HashedMap;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
///**
// * 业务收费项提供者
// */
//@Component
//public class BusinessChargeItemProvider extends BatchDisplayTextProviderSupport {
//
//    @Autowired
//    private BusinessChargeItemService businessChargeItemService;
//
//    //    查询参数
//    private Map<String, Object> queryParams = new HashedMap();
//
//    // 不提供下拉数据
//    @Override
//    public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {
//        //清空缓存
//        Object queryParams = metaMap.get(QUERY_PARAMS_KEY);
//        if(queryParams != null) {
//            getQueryParams().clear();
//            setQueryParams(JSONObject.parseObject(queryParams.toString()));
//        }
//        Resource resource = DTOUtils.newInstance(Resource.class);
//        Object menuId = getQueryParams().get("menuId");
//        if(menuId == null || !menuId.toString().startsWith("menu_")){
//            return null;
//        }
//        resource.setMenuId(Long.parseLong(menuId.toString().substring(5)));
//        return resourceService.listByExample(resource).parallelStream().map(r ->{
//            return new ValuePairImpl<String>(r.getName(), r.getCode());
//        }).collect(Collectors.toList());
//    }
//
//
//    @Override
//    protected BatchProviderMeta getBatchProviderMeta(Map metaMap) {
//        BatchProviderMeta batchProviderMeta = DTOUtils.newInstance(BatchProviderMeta.class);
//        //设置主DTO和关联DTO需要转义的字段名，这里直接取resource表的name属性
//        Map<String, String> map = Maps.newHashMap();
//        map.put(metaMap.get(FIELD_KEY).toString(), "name");
//        map.put("resourceName","name");
//        batchProviderMeta.setEscapeFileds(map);
//        //忽略大小写关联
//        batchProviderMeta.setIgnoreCaseToRef(true);
//        //主DTO与关联DTO的关联(java bean)属性(外键), 这里取resource_link表的resourceId字段
//        batchProviderMeta.setFkField("resourceCode");
//        //关联(数据库)表的主键的字段名，默认取id，这里写出来用于示例用法
//        batchProviderMeta.setRelationTablePkField("code");
//        return batchProviderMeta;
//    }
//
//    @Override
//    protected List getFkList(List<String> relationIds, Map metaMap) {
//        ResourceDto resourceDto = DTOUtils.newInstance(ResourceDto.class);
//        resourceDto.setCodes(relationIds);
//        return resourceService.listByExample(resourceDto);
//    }
//
//
//    public Map<String, Object> getQueryParams(){
//        return queryParams;
//    }
//
//    public void setQueryParams(Map<String, Object> queryParams){
//        this.queryParams = queryParams;
//    }
//}