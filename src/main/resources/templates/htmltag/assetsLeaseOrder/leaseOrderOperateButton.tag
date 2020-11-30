<button id="btn_view" type="button" class="btn btn-primary" onclick="openViewHandler()"><i class="fa fa-eye"></i> 查看</button>
<button id="btn_showProgress" type="button" class="btn btn-primary" onclick="showProgress()"><i class="fa fa-eye"></i> 查看流程</button>
<!-- 摊位操作button -->
<#resource code="addBoothLeaseOrder" checkMenu="true">
<button id="btn_add" type="button" class="btn btn-primary" onclick="openInsertHandler()"><i class="fa fa-plus"></i> 新增</button>
</#resource>
<#resource code="updateBoothLeaseOrder" checkMenu="true">
<button id="btn_edit" type="button" class="btn btn-primary updateEvent" onclick="openUpdateHandler()"><i class="fa fa-pencil-square-o"></i> 修改</button>
</#resource>
<#resource code="cancelBoothLeaseOrder" checkMenu="true">
<button id="btn_cancel" type="button" class="btn btn-primary cancelEvent" onclick="openCancelHandler()"><i class="fa fa-ban"></i> 取消</button>
</#resource>
<#resource code="invalidBoothLeaseOrder" checkMenu="true">
<button id="btn_invalid" type="button" class="btn btn-primary obsoleteEvent" onclick="openInvalidHandler()"><i class="fa fa-window-close"></i> 作废</button>
</#resource>
<#resource code="submitBoothLeaseOrderApplication" checkMenu="true">
<button id="btn_approval" type="button" class="btn btn-primary submitApprovalEvent" onclick="submitForApproval()"><i class="fa fa-paper-plane"></i> 提交审批</button>
</#resource>
<#resource code="submitPaymentBoothLeaseOrder" checkMenu="true">
<button id="btn_submit" type="button" class="btn btn-primary submitEvent" onclick="openSubmitPaymentHandler()"><i class="fa fa-paper-plane"></i> 提交付款</button>
</#resource>
<#resource code="withdrawBoothLeaseOrder" checkMenu="true">
<button id="btn_withdraw" type="button" class="btn btn-primary withdrawEvent" onclick="openWithdrawHandler()"><i class="fa fa-undo"></i> 撤回</button>
</#resource>
<#resource code="supplementBoothLeaseOrder" checkMenu="true">
<button id="btn_supplement" type="button" class="btn btn-primary supplementEvent" onclick="openSupplementHandler()"><i class="fa fa-paint-brush"></i> 补录</button>
</#resource>
<#resource code="renewBoothLeaseOrder" checkMenu="true">
<button id="btn_renew" type="button" class="btn btn-primary reletEvent" onclick="openRenewHandler()"><i class="fa fa-plug"></i> 续租</button>
</#resource>
<#resource code="invoiceBoothLeaseOrder" checkMenu="true">
<button id="btn_invoice" type="button" class="btn btn-primary invoiceEvent" onclick="openInvoiceHandler()"><i class="fa fa-reply"></i>开票</button>
</#resource>
<#resource code="boothLeasePrint" checkMenu="true">
<button id="btn_print" type="button" class="btn btn-primary" onclick="openPrintHandler()"><i class="fa fa-reply"></i>业务打印</button>
</#resource>

<!-- 冷库操作button -->
<#resource code="addLocationLeaseOrder" checkMenu="true">
<button id="btn_add" type="button" class="btn btn-primary" onclick="openInsertHandler()"><i class="fa fa-plus"></i> 新增</button>
</#resource>
<#resource code="updateLocationLeaseOrder" checkMenu="true">
<button id="btn_edit" type="button" class="btn btn-primary updateEvent" onclick="openUpdateHandler()"><i class="fa fa-pencil-square-o"></i> 修改</button>
</#resource>
<#resource code="cancelLocationLeaseOrder" checkMenu="true">
<button id="btn_cancel" type="button" class="btn btn-primary cancelEvent" onclick="openCancelHandler()"><i class="fa fa-ban"></i> 取消</button>
</#resource>
<#resource code="invalidLocationLeaseOrder" checkMenu="true">
<button id="btn_invalid" type="button" class="btn btn-primary obsoleteEvent" onclick="openInvalidHandler()"><i class="fa fa-window-close"></i> 作废</button>
</#resource>
<#resource code="submitLocationLeaseOrderApplication" checkMenu="true">
<button id="btn_approval" type="button" class="btn btn-primary submitApprovalEvent" onclick="submitForApproval()"><i class="fa fa-paper-plane"></i> 提交审批</button>
</#resource>
<#resource code="submitPaymentLocationLeaseOrder" checkMenu="true">
<button id="btn_submit" type="button" class="btn btn-primary submitEvent" onclick="openSubmitPaymentHandler()"><i class="fa fa-paper-plane"></i> 提交付款</button>
</#resource>
<#resource code="withdrawLocationLeaseOrder" checkMenu="true">
<button id="btn_withdraw" type="button" class="btn btn-primary withdrawEvent" onclick="openWithdrawHandler()"><i class="fa fa-undo"></i> 撤回</button>
</#resource>
<#resource code="supplementLocationLeaseOrder" checkMenu="true">
<button id="btn_supplement" type="button" class="btn btn-primary supplementEvent" onclick="openSupplementHandler()"><i class="fa fa-paint-brush"></i> 补录</button>
</#resource>
<#resource code="renewLocationLeaseOrder" checkMenu="true">
<button id="btn_renew" type="button" class="btn btn-primary reletEvent" onclick="openRenewHandler()"><i class="fa fa-plug"></i> 续租</button>
</#resource>
<#resource code="invoiceLocationLeaseOrder" checkMenu="true">
<button id="btn_invoice" type="button" class="btn btn-primary invoiceEvent" onclick="openInvoiceHandler()"><i class="fa fa-reply"></i>开票</button>
</#resource>
<#resource code="locationLeasePrint" checkMenu="true">
<button id="btn_print" type="button" class="btn btn-primary" onclick="openPrintHandler()"><i class="fa fa-reply"></i>业务打印</button>
</#resource>

<!-- 公寓操作button -->
<#resource code="addLodgingLeaseOrder" checkMenu="true">
<button id="btn_add" type="button" class="btn btn-primary" onclick="openInsertHandler()"><i class="fa fa-plus"></i> 新增</button>
</#resource>
<#resource code="updateLodgingLeaseOrder" checkMenu="true">
<button id="btn_edit" type="button" class="btn btn-primary updateEvent" onclick="openUpdateHandler()"><i class="fa fa-pencil-square-o"></i> 修改</button>
</#resource>
<#resource code="cancelLodgingLeaseOrder" checkMenu="true">
<button id="btn_cancel" type="button" class="btn btn-primary cancelEvent" onclick="openCancelHandler()"><i class="fa fa-ban"></i> 取消</button>
</#resource>
<#resource code="invalidLodgingLeaseOrder" checkMenu="true">
<button id="btn_invalid" type="button" class="btn btn-primary obsoleteEvent" onclick="openInvalidHandler()"><i class="fa fa-window-close"></i> 作废</button>
</#resource>
<#resource code="submitLodgingLeaseOrderApplication" checkMenu="true">
<button id="btn_approval" type="button" class="btn btn-primary submitApprovalEvent" onclick="submitForApproval()"><i class="fa fa-paper-plane"></i> 提交审批</button>
</#resource>

<#resource code="submitPaymentLodgingLeaseOrder" checkMenu="true">
<button id="btn_submit" type="button" class="btn btn-primary submitEvent" onclick="openSubmitPaymentHandler()"><i class="fa fa-paper-plane"></i> 提交付款</button>
</#resource>
<#resource code="withdrawLodgingLeaseOrder" checkMenu="true">
<button id="btn_withdraw" type="button" class="btn btn-primary withdrawEvent" onclick="openWithdrawHandler()"><i class="fa fa-undo"></i> 撤回</button>
</#resource>
<#resource code="supplementLodgingLeaseOrder" checkMenu="true">
<button id="btn_supplement" type="button" class="btn btn-primary supplementEvent" onclick="openSupplementHandler()"><i class="fa fa-paint-brush"></i> 补录</button>
</#resource>
<#resource code="renewLodgingLeaseOrder" checkMenu="true">
<button id="btn_renew" type="button" class="btn btn-primary reletEvent" onclick="openRenewHandler()"><i class="fa fa-plug"></i> 续租</button>
</#resource>
<#resource code="invoiceLodgingLeaseOrder" checkMenu="true">
<button id="btn_invoice" type="button" class="btn btn-primary invoiceEvent" onclick="openInvoiceHandler()"><i class="fa fa-reply"></i>开票</button>
</#resource>
<#resource code="lodgingLeasePrint" checkMenu="true">
<button id="btn_print" type="button" class="btn btn-primary" onclick="openPrintHandler()"><i class="fa fa-reply"></i>业务打印</button>
</#resource>