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
        addBoothItem();
    });


    //获取table Index
    function getIndex(str) {
        return str.split('_')[1];
    }

    //初始化刷卡
    initSwipeCard({
        id:'getCustomer',
    });

    var boothAutoCompleteOption = {
        width: '350',
        paramName: 'keyword',
        displayFieldName: 'name',
        serviceUrl: '/booth/search.action',
        selectFn: boothSelectHandler,
        transformResult: function (result) {
            return {
                suggestions: $.map(result, function (dataItem) {
                    return $.extend(dataItem, {
                            value: dataItem.name + '(' + (dataItem.secondAreaName?dataItem.secondAreaName : dataItem.areaName) + ')'
                        }
                    );
                })
            }
        }
    }

    /**
     * 摊位选择事件Handler
     * */
    function boothSelectHandler(suggestion,element) {
        return ;
    }

    /**
     * 添加摊位
     * */
    // function addBoothItem() {
    //     $('#boothTable tbody').append(HTMLDecode(template('stallItem', {index: ++itemIndex})))
    // }

    function addBoothItem(){
        $('#boothTable tbody').append(bui.util.HTMLDecode(template('boothItem', {index: ++itemIndex})))
    }

    // 添加摊位
    $('#addBooth').on('click', function () {
        addBoothItem();
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
        return formData;
    }

    // 提交保存
    $('#formSubmit').on('click', function (e) {
        if (!$('#saveForm').valid()) {
            return false;
        } else {
            bui.loading.show('努力提交中，请稍候。。。');
            // let _formData = new FormData($('#saveForm')[0]);
            $.ajax({
                type: "POST",
                url: "${contextPath}/earnestOrder/doAdd.action",
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