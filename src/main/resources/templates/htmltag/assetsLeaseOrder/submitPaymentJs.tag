<script >

    $(function () {
        batchQueryDepositOrder({
            businessId: $('#leaseOrderId').val(),
            isRelated: ${@com.dili.commons.glossary.YesOrNoEnum.YES.getCode()}
        });
        loadDefaultAmount();
    });

    /**
     * 批量订单项保证金（补交）查询
     * @param {businessId: leaseOrderId,assetsId:123}
     */
    function batchQueryDepositOrder(depositOrderQuery) {
        $.ajax({
            type: "post",
            url: "/leaseOrder/batchQueryDepositOrder.action",
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
                            if (depositOrder.state == ${@com.dili.ia.glossary.DepositOrderStateEnum.CANCELD.getCode()}
                                || depositOrder.payState == ${@com.dili.ia.glossary.DepositPayStateEnum.PAID.getCode()}
                                    || depositOrder.refundState != ${@com.dili.ia.glossary.DepositRefundStateEnum.NO_REFUNDED.getCode()}){
                                $('#depositAmount_' + index).attr('readonly', true);
                            } else {
                                $('#depositAmount_' + index).attr('readonly', false);
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
     * 本次付款改动联动计算分摊总金额
     * @returns {number}
     */
    function calcWaitApportionAmount() {
        let payAmount = Number($('#payAmount').val());
        let apportionedAmount = calcApportionedAmount();
        $('#waitApportionAmount').val(payAmount.sub(apportionedAmount));
        apportionedLinkageCalc();
    }

    /**
     * 计算分摊明细金额之和
     * @returns {number}
     */
    function calcApportionedAmount() {
        let totalAmount = 0;
        $("table input.money").each(function (i) {
            totalAmount = Number(this.value).add(totalAmount);
        });
        return totalAmount;
    }

    /**
     * 分摊金额改动联动
     */
    function apportionedLinkageCalc() {
        let payAmount = Number($('#payAmount').val());
        let apportionedAmount = calcApportionedAmount();
        let chargeAmount = calcChargeAmount();

        $('#waitApportionAmount').val(payAmount.sub(apportionedAmount));
        $('#leasePayAmount').val(chargeAmount);
    }

    /**
     * 计算收费项分摊合计
     * @returns {number}
     */
    function calcChargeAmount() {
        let totalAmount = 0;
        $("table input[isCharge]").each(function (i) {
            totalAmount = Number(this.value).add(totalAmount);
        });
        return totalAmount;
    }

    /**
     * 计算加载默认金额
     */
    function loadDefaultAmount() {
        let initApportionedAmount = calcApportionedAmount();
        let chargeAmount = calcChargeAmount();

        //反推本次付款金额
        $('#payAmount').attr('max',initApportionedAmount).val(initApportionedAmount);
        //反推租赁支付金额
        $('#leasePayAmount').val(chargeAmount);
    }

    /**
     * 提交前校验
     * @returns {boolean}
     */
    function checkSubmit() {
        let waitApportionAmount = Number($('#waitApportionAmount').val());
        if (waitApportionAmount !== 0) {
            bs4pop.notice('【本次付款金额】必须等于【分摊明细之和】', {type: 'danger',position: 'bottomleft'});
            return false;
        }
        return true;
    }

    function saveFormHandler() {
        let validator = $('#saveForm').validate({ignore: ''})
        if (!validator.form() || !checkSubmit()) {
            $('.breadcrumb [data-toggle="collapse"]').html('收起 <i class="fa fa-angle-double-up" aria-hidden="true"></i>');
            $('.collapse:not(.show)').addClass('show');
            return false;
        }

        bui.loading.show('努力提交中，请稍候。。。');
        $.ajax({
            type: "POST",
            url: "/leaseOrder/submitPayment.action",
            data: JSON.stringify(buildFormData()),
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            success: function (ret) {
                if(!ret.success){
                    bs4pop.alert(ret.message, {type: 'error'},function () {
                        parent.closeDialog(parent.dia);
                    });
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
            return this.value > 0;
        }).each(function (i) {
            let index = this.dataset.index;
            let chargeItemId = this.dataset.chargeItemId;
            let chargeItemName = this.dataset.chargeItemName;
            let leaseItemId = $('#leaseItemId_'+index).val();
            let businessChargeItemId = $('#businessChargeItemId_' + chargeItemId + '_' + index).val();
            let businessChargeItem = {};
            businessChargeItem.id = businessChargeItemId;
            businessChargeItem.chargeItemId = chargeItemId;
            businessChargeItem.chargeItemName = chargeItemName;
            businessChargeItem.businessId = leaseItemId;
            businessChargeItem.paymentAmount = Number($(this).val()).mul(100);
            businessChargeItems.push(businessChargeItem);
        });
        //构建保证金
        $("table input[isDeposit]").filter(function () {
            return this.value > 0;
        }).each(function (i) {
            let index = getIndex($(this).attr("name"));
            let assetsId = $('#assetsId_'+index).val();
            depositAmountMap[assetsId] = Number($(this).val()).mul(100);
        });

        return $.extend(formData,{businessChargeItems,depositAmountMap})
    }

/*****************************************自定义事件区 begin************************************/
    //保存事件
    $('#save').on('click', bui.util.debounce(saveFormHandler,1000,true));

/*****************************************自定义事件区 end**************************************/
</script>