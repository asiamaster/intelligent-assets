<script >
    
    $(function () {
        batchQueryDepositOrder({businessId:$('#leaseOrderId').val()});
    });
    
    function saveFormHandler() {
        let validator = $('#saveForm').validate({ignore: ''})
        if (!validator.form()) {
            $('.breadcrumb [data-toggle="collapse"]').html('收起 <i class="fa fa-angle-double-up" aria-hidden="true"></i>');
            $('.collapse:not(.show)').addClass('show');
            return false;
        }
    }
    
    /**
     * 批量订单项保证金（补交）查询
     * @param {businessId: leaseOrderId,assetsId:123}
     */
    function batchQueryDepositOrder(depositOrderQuery) {
        $.ajax({
            type: "post",
            url: "/assetsLeaseOrder/batchQueryDepositOrder.action",
            data: depositOrderQuery,
            dataType: "json",
            success: function (ret) {
                if (ret.success) {
                    let depositOrders = ret.data;
                    if (depositOrders.length > 0) {
                        for (let depositOrder of depositOrders) {
                            let index = getIndex($("table input[id='assetsId_" + depositOrder.assetsId + "']").attr('id'));
                            $('#depositAmount_' + index).val(Number(depositOrder.waitAmount).centToYuan());
                            if(depositOrder.state == ${@com.dili.ia.glossary.DepositOrderStateEnum.CANCELD.getCode()}
                                || depositOrder.payState == ${@com.dili.ia.glossary.DepositPayStateEnum.PAID.getCode()}
                                    || depositOrder.refundState != ${@com.dili.ia.glossary.DepositRefundStateEnum.NO_REFUNDED.getCode()}){
                                $('#depositAmount_' + index).attr('readonly', true);
                            }
                        }
                    }
                }
            },
            error: function (a, b, c) {
                bs4pop.alert('远程访问失败', {type: 'error'});
            }
        });
    }

</script>