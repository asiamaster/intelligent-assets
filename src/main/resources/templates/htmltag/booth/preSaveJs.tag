<script>
    /**
     *
     * @Date 2019-11-06 17:30:00
     * @author jiangchengyong
     *
     ***/

    /*********************变量定义区 begin*************/


    /*********************变量定义区 end***************/




    /******************************驱动执行区 begin***************************/

    /******************************驱动执行区 end****************************/




    /*****************************************函数区 begin************************************/



    /*****************************************函数区 end**************************************/




    /*****************************************自定义事件区 begin************************************/


    //删除行事件 （删除摊位行）
    $(document).on('click', '.item-del', function () {
        if ($('#stallTable tr').length > 2) {
            $(this).closest('tr').remove();
        }
    });

    // 提交保存
    $('#formSubmit').on('click', function (e) {
        if (!$('#saveForm').valid()) {
            return false;
        } else {
            bui.loading.show('努力提交中，请稍候。。。');
            let _formData = new FormData($('#saveForm')[0]);
            $.ajax({
                type: "POST",
                url: "${contextPath}/earnestOrder/save.action",
                data: _formData,
                processData: false,
                contentType: false,
                async: true,
                success: function (res) {
                    bui.loading.hide();
                    if (data.code == "200") {
                        bs4pop.alert('成功', {type: 'success '}, function () {
                            /* 应该要带条件刷新 */
                            window.location.reload();
                        });
                    } else {
                        bui.loading.hide();
                        bs4pop.alert(res.result, {type: 'error'});
                    }
                },
                error: function (error) {
                    bui.loading.hide();
                    bs4pop.alert(error.result, {type: 'error'});
                }
            });
        }
    });

    /*****************************************自定义事件区 end**************************************/
</script>