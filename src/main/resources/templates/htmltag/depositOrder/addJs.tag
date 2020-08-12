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
        registerMsg();
        $('#assetsNameInput').hide();
    });

    //初始化刷卡
    initSwipeCard({
        id:'getCustomer',
    });

    $('#assetsType').on('change', function(){
        $('#assetsId, #assetsName, #assetsNameInput').val('').hide();
        $('#assetsNameInput').attr('name', '');
        if($(this).val() == 1 ) {
            $('#assetsName').show();
        } else {
            $('#assetsNameInput').attr('name', 'assetsName').show();
        }
    })

    let assetsType = $('[name="assetsType"]').val();
    var boothAutoCompleteOption = {
        paramName: 'keyword',
        displayFieldName: 'name',
        serviceUrl: '/booth/search.action',
        onSearchStart: function (params) {
            params['assetsType'] = $('[name="assetsType"]').val();
            return params;
        },
        transformResult: function (result) {
            if(result.success){
                let data = result.data;
                return {
                    suggestions: $.map(data, function (dataItem) {
                        return $.extend(dataItem, {
                                value: dataItem.name + '(' + (dataItem.secondAreaName? dataItem.areaName + '->' + dataItem.secondAreaName : dataItem.areaName) + ')'
                            }
                        );
                    })
                }
            }else{
                bs4pop.alert(result.message, {type: 'error'});
                return;
            }
        },
        selectFn: function (suggestion) {
            $('#assetsName').val(suggestion.name);
            $('#assetsId').val(suggestion.id);
        }
    }

    function buildFormData(){
        // let formData = new FormData($('#saveForm')[0]);
        let formData = $("input:not(table input),textarea,select").serializeObject();
        debugger
        let typeName = $('#typeCode').find("option:selected").text();
        bui.util.yuanToCentForMoneyEl(formData);
        $.extend(formData,{typeName});
        return formData;
    }

    // 提交保存
    function doAddDepositHandler(){
        let validator = $('#saveForm').validate({ignore:''})
        if (!validator.form()) {
            /*$(this).find('.collapse').each(function (index, element) {
                $(element).trigger('show.bs.collapse');
            });*/
            $('.breadcrumb [data-toggle="collapse"]').html('收起 <i class="fa fa-angle-double-up" aria-hidden="true"></i>');
            $('.collapse:not(.show)').addClass('show');
            return false;
        }
        bui.loading.show('努力提交中，请稍候。。。');
        // let _formData = new FormData($('#saveForm')[0]);
        $.ajax({
            type: "POST",
            url: "${contextPath}/depositOrder/doAdd.action",
            data: buildFormData(),
            dataType: "json",
            success: function (ret) {
                bui.loading.hide();
                if(!ret.success){
                    bs4pop.alert(ret.message, {type: 'error'});
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
</script>