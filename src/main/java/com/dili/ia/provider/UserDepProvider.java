package com.dili.ia.provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.provider.BatchDisplayTextProviderAdaptor;
import com.dili.uap.sdk.domain.User;
import com.dili.uap.sdk.domain.dto.UserQuery;
import com.dili.uap.sdk.rpc.UserRpc;
import com.google.common.collect.Lists;

/**
 * 司磅入库获取查件员
 * 获取同部门人员
 */
@Component
@Scope("prototype")
public class UserDepProvider extends BatchDisplayTextProviderAdaptor {

	// 前台需要传入的参数
	protected static final String DD_CODE_KEY = "dd_code";
	@Autowired
	private UserRpc userRpc;

	@Override
	public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {
		Object queryParams = metaMap.get(QUERY_PARAMS_KEY);
		if (queryParams == null) {
			return Lists.newArrayList();
		}

		String ddCode = getDdCode(queryParams.toString());
		if(StringUtils.isAllEmpty(ddCode)) {
			return Lists.newArrayList();
		}
		UserQuery userQuery = DTOUtils.newDTO(UserQuery.class);
		userQuery.setDepartmentId(Long.parseLong(ddCode));
		BaseOutput<List<User>> userList = userRpc.listByExample(userQuery);
		if (!userList.isSuccess()) {
			return null;
		}
		List<ValuePair<?>> valuePairs = Lists.newArrayList();
		List<User> userValues = (List<User>) userList.getData();
		for (int i = 0; i < userValues.size(); i++) {
			User user = userValues.get(i);
			valuePairs.add(i, new ValuePairImpl(user.getRealName(), user.getId()));
		}
		return valuePairs;
	}

	@Override
	protected List getFkList(List<String> ddvIds, Map metaMap) {
		Object queryParams = metaMap.get(QUERY_PARAMS_KEY);
		if (queryParams == null) {
			return Lists.newArrayList();
		}
		String ddCode = getDdCode(queryParams.toString());
		UserQuery userQuery = DTOUtils.newDTO(UserQuery.class);
		userQuery.setSuperiorId(Long.parseLong(ddCode));
		BaseOutput<List<User>> userList = userRpc.listByExample(userQuery);
		return userList.isSuccess() ? (List<User>)userList.getData() : null;
	}

	@Override
	protected Map<String, String> getEscapeFileds(Map metaMap) {
		if (metaMap.get(ESCAPE_FILEDS_KEY) instanceof Map) {
			return (Map) metaMap.get(ESCAPE_FILEDS_KEY);
		} else {
			Map<String, String> map = new HashMap<>();
			map.put(metaMap.get(FIELD_KEY).toString(), "name");
			return map;
		}
	}

	/**
	 * 关联(数据库)表的主键的字段名 默认取id，子类可自行实现
	 * 
	 * @return
	 */
	@Override
	protected String getRelationTablePkField(Map metaMap) {
		return "code";
	}

	/**
	 * 获取数据字典编码
	 * 
	 * @return
	 */
	public String getDdCode(String queryParams) {
		// 清空缓存
		String ddCode = JSONObject.parseObject(queryParams).getString(DD_CODE_KEY);
		if (ddCode == null) {
			throw new RuntimeException("dd_code属性为空");
		}
		return ddCode;
	}

	@Override
	protected boolean ignoreCaseToRef(Map metaMap) {
		return true;
	}
}