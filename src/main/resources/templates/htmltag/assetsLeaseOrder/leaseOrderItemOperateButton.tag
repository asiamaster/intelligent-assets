<!-- 摊位操作button -->
<#resource code="refundApplyBoothLeaseOrder" checkMenu="true">
<button id="btn_refund_apply{{index}}" type="button" class="btn btn-primary" onclick="openRefundApplyHandler('{{index}}')"><i
        class="fa fa-reply"></i> 退款申请
</button>
</#resource>
<#resource code="stopRentBoothLeaseOrder" checkMenu="true">
<button id="btn_stop_rent{{index}}" type="button" class="btn btn-primary" onclick="openStopRentHandler('{{index}}')"><i
        class="fa fa-stop"></i> 停租
</button>
</#resource>

<!-- 冷库操作button -->
<#resource code="refundApplyLocationLeaseOrder" checkMenu="true">
<button id="btn_refund_apply{{index}}" type="button" class="btn btn-primary" onclick="openRefundApplyHandler('{{index}}')"><i
        class="fa fa-reply"></i> 退款申请
</button>
</#resource>
<#resource code="stopRentLocationLeaseOrder" checkMenu="true">
<button id="btn_stop_rent{{index}}" type="button" class="btn btn-primary" onclick="openStopRentHandler('{{index}}')"><i
        class="fa fa-stop"></i> 停租
</button>
</#resource>

<!-- 公寓操作button -->
<#resource code="refundApplyLodgingLeaseOrder" checkMenu="true">
<button id="btn_refund_apply{{index}}" type="button" class="btn btn-primary" onclick="openRefundApplyHandler('{{index}}')"><i
        class="fa fa-reply"></i> 退款申请
</button>
</#resource>
<#resource code="stopRentLodgingLeaseOrder" checkMenu="true">
<button id="btn_stop_rent{{index}}" type="button" class="btn btn-primary" onclick="openStopRentHandler('{{index}}')"><i
        class="fa fa-stop"></i> 停租
</button>
</#resource>