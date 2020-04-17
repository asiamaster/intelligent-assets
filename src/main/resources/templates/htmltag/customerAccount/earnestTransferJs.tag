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
    function doEarnestTransferHandler(){
        console.log(123)
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


