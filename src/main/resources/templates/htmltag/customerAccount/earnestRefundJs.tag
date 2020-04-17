<script>

    $(function () {
        $('[data-refund-way="bank"]').hide();
    });

    // 退款方式
    $('#refundType').on('change', function () {
        if($(this).val() == '3') {
            $('[data-refund-way="bank"]').show();
        } else {
            $('[data-refund-way="bank"]').hide();
            $('[data-refund-way="bank"]').find('input').val("");
        }
    })

    function buildFormData(){
        let formData = $("input:not(table input),textarea,select").serializeObject();
        bui.util.yuanToCentForMoneyEl(formData);
        return formData;
    }

    // 定金退款保存
    function doAddEarnestRefundHandler(){
        if (!$('#saveForm').valid()) {
            return false;
        } else {
            bui.loading.show('努力提交中，请稍候。。。');
            // let _formData = new FormData($('#saveForm')[0]);
            $.ajax({
                type: "POST",
                url: "${contextPath}/customerAccount/doAddEarnestRefund.action",
                data: buildFormData(),
                dataType: "json",
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
    }

</script>


