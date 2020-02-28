<script>
    /**
     *
     * @Date 2019-11-06 17:30:00
     * @author jiangchengyong
     *
     ***/

        //行索引计数器
    let itemIndex = 0;


    //获取table Index
    function getIndex(str) {
        return str.split('_')[1];
    }



    /**
     * 添加摊位
     * */
    function addStallItem() {
        $('#boothTable tbody').append(HTMLDecode(template('stallItem', {index: ++itemIndex})))
    }

    // 添加摊位
    $('#addStall').on('click', function () {
        addStallItem();
    })

    //删除行事件 （删除摊位行）
    $(document).on('click', '.item-del', function () {
        if ($('#boothTable tr').length > 2) {
            $(this).closest('tr').remove();
        }
    });

    // 提交保存
    $('#formSubmit').on('click', function (e) {
        if (!$('#updateorm').valid()) {
            return false;
        } else {
            bui.loading.show('努力提交中，请稍候。。。');
            let _formData = new FormData($('#updateForm')[0]);
            $.ajax({
                type: "POST",
                url: "${contextPath}/earnestOrder/doUpdate.action",
                data: _formData,
                processData: false,
                contentType: false,
                async: true,
                success: function (res) {
                    bui.loading.hide();
                    if (data.code == "200") {
                        bs4pop.alert('修改成功', {type: 'success '}, function () {
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

</script>