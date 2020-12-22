package com.dili.ia.service;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;

import com.dili.ia.domain.Attachment;
import com.dili.ss.base.BaseService;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @Description TODO(用一句话描述该文件做什么)
 * @author yangfan
 * @date 2020年12月21日
 */
public interface AttachmentService extends BaseService<Attachment, Long> {

	/**
	 * 添加附件
	 * @param attachments
	 * @param bizCode
	 * @throws
	 */
	void add(List<Attachment> attachments,String bizCode);
	
	/**
	 * 
	 * @Title 获取关联文件列表
	 */
	List<Attachment> list(String bizCode);
}
