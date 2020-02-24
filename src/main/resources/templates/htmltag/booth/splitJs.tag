<script>
    /**
     *
     * @Date 2019-11-06 17:30:00
     * @author jiangchengyong
     *
     ***/

    /*********************变量定义区 begin*************/
    //行索引计数器
    let itemIndex = 0;

    /*********************变量定义区 end***************/




    /******************************驱动执行区 begin***************************/

    $(function () {
        addBoothItem();
    });

    /**
     * 添加摊位
     * */
    function addBoothItem(){
        $('#boothTable tbody').append(HTMLDecode(template('boothItem',{index:++itemIndex})))
    }


    /******************************驱动执行区 end****************************/




    /*****************************************函数区 begin************************************/

    //HTML反转义
    function HTMLDecode(str)
    {
        var s = "";
        if (str.length == 0) return "";
        s = str.replace(/&amp;/g, "&");
        s = s.replace(/&lt;/g, "<");
        s = s.replace(/&gt;/g, ">");
        s = s.replace(/&nbsp;/g, " ");
        s = s.replace(/&#39;/g, "\'");
        s = s.replace(/&quot;/g, "\"");
        s = s.replace(/<br\/>/g, "\n");
        return s;
    }

    //获取table Index
    function getIndex(str) {
        return str.split('_')[1];
    }

    /*****************************************函数区 end**************************************/




    /*****************************************自定义事件区 begin************************************/

    $('#addBooth').on('click', function(){
        addBoothItem();
    })

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
                url: "${contextPath}/booth/save.action",
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