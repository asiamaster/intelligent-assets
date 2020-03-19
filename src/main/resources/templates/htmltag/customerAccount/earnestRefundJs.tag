<script>

    $(function () {
        $('[data-refund-way="bank"]').hide();
    });

    // 退款方式
    $('#refundType').on('change', function () {
        debugger
        if($(this).val() == '3') {
            $('[data-refund-way="bank"]').show();
        } else {
            $('[data-refund-way="bank"]').hide();
        }
    })

    function buildFormData(){
        let formData = $("input:not(table input),textarea,select").serializeObject();
        bui.util.yuanToCentForMoneyEl(formData);
        return formData;
    }

    // 定金退款保存
    $('#formSubmit').on('click', function (e) {
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
                async : false,
                success: function (ret) {
                    bui.loading.hide();
                    if(!ret.success){
                        bs4pop.alert(ret.message, {type: 'error'},function () {
                            parent.closeDialog(parent.dia);
                        });
                    }else{
                        parent.closeDialog(parent.dia);
                    }
                },
                error: function (error) {
                    bui.loading.hide();
                    bs4pop.alert('远程访问失败', {type: 'error'});
                }
            });
        }
    });


</script>


