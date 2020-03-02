<script>
    /**
     *
     * @Date 2019-11-06 17:30:00
     * @author jiangchengyong
     *
     ***/

        //行索引计数器
    let itemIndex = 0;

    //对应摊位
    $(function () {
        addStallItem();
    });


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


    function buildFormData(){
        // let formData = new FormData($('#saveForm')[0]);
        let formData = $("input:not(table input),textarea,select").serializeObject();
        let earnestOrderdetails = [];
        bui.util.yuanToCentForMoneyEl(formData);
        $("#boothTable tbody").find("tr").each(function(){
            let earnestOrderdetail = {};
            $(this).find("input").each(function(t,el){
                let fieldName = $(this).attr("name").split('_')[0];
                earnestOrderdetail[fieldName] = $(this).hasClass('money')? Number($(this).val()).mul(100) : $(this).val();
            });
            earnestOrderdetails.push(earnestOrderdetail);
        });

        $.extend(formData,{earnestOrderdetails});
        console.log(formData);
        return formData;
    }

    // 提交保存
    $('#formSubmit').on('click', function (e) {
        if (!$('#saveForm').valid()) {
            return false;
        } else {
            bui.loading.show('努力提交中，请稍候。。。');
            $.ajax({
                type: "POST",
                url: "${contextPath}/earnestOrder/doAdd.action",
                data: buildFormData(),
                processData: false,
                contentType: false,
                async: true,
                success: function (data) {
                    bui.loading.hide();
                    if(data.success){
                        bs4pop.alert('注册成功', {type: 'success'}, function () {
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