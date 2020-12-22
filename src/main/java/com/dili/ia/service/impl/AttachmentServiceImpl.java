package com.dili.ia.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dili.ia.domain.Attachment;
import com.dili.ia.mapper.AttachmentMapper;
import com.dili.ia.service.AttachmentService;
import com.dili.ss.base.BaseServiceImpl;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-07-15 16:59:23.
 */
@Service
public class AttachmentServiceImpl extends BaseServiceImpl<Attachment, Long> implements AttachmentService {

    public AttachmentMapper getActualDao() {
        return (AttachmentMapper)getDao();
    }

	@Override
	public void add(List<Attachment> attachments, String bizCode) {
		// 删除原文件关联
		Attachment ex = new Attachment();
		ex.setBusinessCode(bizCode);
		this.deleteByExample(ex);
		// 新增文件关联
		attachments.forEach(i -> {
			i.setBusinessCode(bizCode);
		});
		this.batchInsert(attachments);
	}

	@Override
	public List<Attachment> list(String bizCode) {
		Attachment condtion = new Attachment();
		condtion.setBusinessCode(bizCode);
		List<Attachment> attachments = this.list(condtion);
		return attachments;
	}
}