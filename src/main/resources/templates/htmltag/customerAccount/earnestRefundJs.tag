<script>
    /******************************驱动执行区 begin***************************/
    //定金退款总金额 = 定金退款金额
    $('input[name="totalRefundAmount"]').bind('input propertychange', function() {
        $('input[name="payeeAmount"]').val($('input[name="totalRefundAmount"]').val());
    });
    $('input[name="payeeAmount"]').bind('input propertychange', function() {
        $('input[name="totalRefundAmount"]').val($('input[name="payeeAmount"]').val());
    });

    function buildFormData(){
        let formData = $("input:not(table input),textarea,select").serializeObject();
        bui.util.yuanToCentForMoneyEl(formData);
        return $.extend(formData, {logContent: $('#id').val() ? Log.buildUpdateContent() : ''});
    }

    /**
     * 验证实际退款金额是否小于
     * @returns {boolean}
     */
    function validateActualRefundAmount(){
        let payeeAmount = Number($('#payeeAmount').val());
        let totalRefundAmount = Number($('#totalRefundAmount').val());
        if (totalRefundAmount.mul(100) != (payeeAmount.mul(100))) {
            return false;
        }
        return true;
    }


    // 定金退款保存
    function doAddEarnestRefundHandler(){
        if (!$('#saveForm').valid()) {
            return false;
        }
        if(!validateActualRefundAmount()){
            bs4pop.alert('退款金额分配错误，请重新修改再保存');
            return;
        }

        bui.loading.show('努力提交中，请稍候。。。');
        // let _formData = new FormData($('#saveForm')[0]);
        $.ajax({
            type: "POST",
            url: "${contextPath}/customerAccount/saveOrUpdateRefundOrder.action",
            data:JSON.stringify(buildFormData()),
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
            error: function (error) {
                bui.loading.hide();
                bs4pop.alert('远程访问失败', {type: 'error'});
            }
        });
    }

    $('#save').on('click', bui.util.debounce(doAddEarnestRefundHandler,1000,true));
</script>


