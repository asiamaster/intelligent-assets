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


    // 定金转移保存
    $('#formSubmit').on('click', function (e) {
        if (!$('#saveForm').valid()) {
            return false;
        } else {
            bui.loading.show('努力提交中，请稍候。。。');
            let _formData = new FormData($('#saveForm')[0]);
            $.ajax({
                type: "POST",
                url: "${contextPath}/customerAccount/doEarnestTransfer.action",
                data: _formData,
                processData: false,
                contentType: false,
                async: true,
                success: function (data) {
                    bui.loading.hide();
                    if(data.success){
                        bs4pop.alert('转移成功', {type: 'success'}, function () {
                            /* 应该要带条件刷新 */
                            window.location.reload();
                        });
                    } else {
                        bui.loading.hide();
                        bs4pop.alert(data.message, {type: 'error'});
                    }
                },
                error: function (error) {
                    bui.loading.hide();
                    bs4pop.alert(error.result, {type: 'error'});
                }
            });
        }
    });

</script>


