package com.dili.ia.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dili.ia.domain.Attachment;
import com.dili.ia.service.AttachmentService;
import com.dili.ia.service.impl.AttachmentServiceImpl;
import com.dili.ss.domain.BaseOutput;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @Description 附件
 * @author yangfan
 * @date 2020年12月21日
 */
@Controller
@RequestMapping("/attachment")
public class AttachmentController {

	@Autowired
	private AttachmentService attachmentService;
	
	/**
	 * 
	 * @Title add
	 * @Description 
	 */
	@RequestMapping("/add.action")
	public @ResponseBody BaseOutput add(@RequestBody List<Attachment> attachments,String bizCode) {
		attachmentService.add(attachments, bizCode);
		return BaseOutput.success();
	}
	
	/**
	 * 
	 * @Title add
	 * @Description 
	 */
	@RequestMapping("/list.action")
	public @ResponseBody BaseOutput list(String bizCode) {
		
		return BaseOutput.successData(attachmentService.list(bizCode));
	}
	
}
