<script>

    $(function () {
        $('[data-refund-way="bank"]').hide();
    });

    function buildFormData(){
        let formData = $("input:not(table input),textarea,select").serializeObject();
        bui.util.yuanToCentForMoneyEl(formData);
        return formData;
    }

    //初始化刷卡
    initSwipeCard({
        id:'getCustomer',
    });

    // 定金转移保存
    $('#formSubmit').on('click', function (e) {
        if (!$('#saveForm').valid()) {
            return false;
        } else {
            bui.loading.show('努力提交中，请稍候。。。');
            // let _formData = new FormData($('#saveForm')[0]);
            $.ajax({
                type: "POST",
                url: "${contextPath}/customerAccount/doEarnestTransfer.action",
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


