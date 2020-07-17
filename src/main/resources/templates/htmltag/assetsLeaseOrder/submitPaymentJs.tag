<script >
    
    $(function () {
        batchQueryDepositOrder({businessId:$('#leaseOrderId').val()});
        calcPayAmount();
    });
    
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
            async : false,
            success: function (ret) {
                if (ret.success) {
                    let depositOrders = ret.data;
                    if (depositOrders.length > 0) {
                        for (let depositOrder of depositOrders) {
                            let index = getIndex($("table input.assets[value='" + depositOrder.assetsId + "']").attr('id'));
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

    /**
     * 计算合计金额
     * @returns {number}
     */
    function calcApportionAmount() {
        let totalAmount = 0;
        $("table input.money").filter(function () {
            return this.value;
        }).each(function (i) {
            totalAmount = Number(this.value).add(totalAmount);
        });
        $('#apportionAmountTotal').val(totalAmount.toFixed(2));
        return totalAmount;
    }

    /**
     * 计算收费项分摊合计
     * @returns {number}
     */
    function calcChargeAmount() {
        let totalAmount = 0;
        $("table input[isCharge]").filter(function () {
            return this.value;
        }).each(function (i) {
            totalAmount = Number(this.value).add(totalAmount);
        });
        return totalAmount;
    }

    /**
     * 计算支付金额
     */
    function calcPayAmount() {
        let deductionAmount = Number($('#deductionAmount').val());
        $('#payAmount').val((calcApportionAmount().mul(100)-Number($('#deductionAmount').val()).mul(100)).centToYuan());
        $('#leasePayAmount').val((calcChargeAmount().mul(100) - deductionAmount.mul(100)).centToYuan());
    }

    function saveFormHandler() {
        let validator = $('#saveForm').validate({ignore: ''})
        if (!validator.form()) {
            $('.breadcrumb [data-toggle="collapse"]').html('收起 <i class="fa fa-angle-double-up" aria-hidden="true"></i>');
            $('.collapse:not(.show)').addClass('show');
            return false;
        }

        console.log(buildFormData());
        bui.loading.show('努力提交中，请稍候。。。');
        $.ajax({
            type: "POST",
            url: "/assetsLeaseOrder/submitPayment.action",
            data: JSON.stringify(buildFormData()),
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            success: function (ret) {
                if(!ret.success){
                    bs4pop.alert(ret.message, {type: 'error'});
                }else{
                    parent.closeDialog(parent.dia);
                }
                bui.loading.hide();
            },
            error: function (a, b, c) {
                bui.loading.hide();
                bs4pop.alert('远程访问失败', {type: 'error'});
            }
        });
    }

    /**
     * 构建表单数据对象
     * @returns {jQuery|any|{}}
     */
    function buildFormData() {
        let formData = $("input:not(table input),textarea,select").serializeObject();
        bui.util.yuanToCentForMoneyEl(formData);

        let businessChargeItems = [];
        let depositAmountMap = {};
        //构建收费项分摊集合对象
        $("table input[isCharge]").filter(function () {
            return this.value;
        }).each(function (i) {
            let index = this.dataset.index;
            let chargeItemId = this.dataset.chargeItemId;
            let leaseItemId = $('#leaseItemId_'+index).val();
            let businessChargeItemId = $('#businessChargeItemId_' + chargeItemId + '_' + index).val();
            let businessChargeItem = {};
            businessChargeItem.id = businessChargeItemId;
            businessChargeItem.businessId = leaseItemId;
            businessChargeItem.paymentAmount = Number($(this).val()).mul(100);
            businessChargeItems.push(businessChargeItem);
        });
        //构建保证金
        $("table input[isDeposit]").filter(function () {
            return this.value;
        }).each(function (i) {
            let index = getIndex($(this).attr("name"));
            let assetsId = $('#assetsId_'+index).val();
            depositAmountMap[assetsId] = Number($(this).val()).mul(100);
        });

        return $.extend(formData,{businessChargeItems,depositAmountMap})
    }
</script>