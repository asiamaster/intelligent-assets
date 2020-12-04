<script>
    /**
     *
     * @Date 2019-11-06 17:30:00
     * @author jiangchengyong
     *
     ***/

    /*********************变量定义区 begin*************/
        var customerNameAutoCompleteOption = {
            serviceUrl: '/customer/listNormal.action',
            paramName : 'keyword',
            displayFieldName : 'name',
            showNoSuggestionNotice: true,
            noSuggestionNotice: '无匹配结果',
            transformResult: function (result) {
                if(result.success){
                    let data = result.data;
                    return {
                        suggestions: $.map(data, function (dataItem) {
                            return $.extend(dataItem, {
                                    value: dataItem.name + '（' + dataItem.certificateNumber + '）'
                                }
                            );
                        })
                    }
                }else{
                    bs4pop.alert(result.message, {type: 'error'});
                    return false;
                }
            },
            selectFn: function (suggestion) {
                $('#payeeCertificateNumber').val(suggestion.certificateNumber);
            }
        };

    /*********************变量定义区 end***************/


    /******************************驱动执行区 begin***************************/


    /******************************驱动执行区 end****************************/

    /*****************************************函数区 begin************************************/

    /**
     * 构建退款申请表单提交数据
     * @returns {{}|jQuery}
     */
    function buildFormData(){
        let formData = $("input:not(table input),textarea,select").serializeObject();
        bui.util.yuanToCentForMoneyEl(formData);
        let refundFeeItems = [];
        $("input[isRefundFeeItem]").each(function (i) {
            let refundFeeItem = {};
            refundFeeItem.chargeItemId = this.dataset.chargeItemId;
            refundFeeItem.chargeItemName = this.dataset.chargeItemName;
            refundFeeItem.amount = Number($(this).val()).mul(100);
            refundFeeItems.push(refundFeeItem);
        });
        return $.extend(formData, {
            refundFeeItems,
            logContent: $('#id').val() ? Log.buildUpdateContent() : ''
        });
    }

    /**
    * 计算退款总金额
    */
    function calcTotalRefundAmount(){
        let totalAmount = 0;
        $("input[isRefundFeeItem]").each(function (i) {
            totalAmount = Number(this.value).add(totalAmount);
        });
        $('#totalRefundAmount').val(totalAmount);
    }

    /**
     * 表单baocun
     * @returns {boolean}
     */
    function saveFormHandler(){
        if (!$('#refundApplyForm').valid()) {
            return false;
        }

        bui.loading.show('努力提交中，请稍候。。。');
        $.ajax({
            type: "POST",
            url: "/leaseOrder/createOrUpdateRefundOrder.action",
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

    /*****************************************函数区 end**************************************/

    /*****************************************自定义事件区 begin************************************/

    $('#save').on('click', bui.util.debounce(saveFormHandler,1000,true));

/*****************************************自定义事件区 end**************************************/
</script>