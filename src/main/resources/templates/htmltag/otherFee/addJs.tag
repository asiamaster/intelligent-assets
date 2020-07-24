<script>
        //行索引计数器
    let itemIndex = 0;

    $(function () {
        registerMsg();
    });
    //初始化刷卡
    initSwipeCard({
        id:'getCustomer',
    });

    // //品类搜索自动完成
    // var categoryAutoCompleteOption = {
    //     width: '100%',
    //     language: 'zh-CN',
    //     maximumSelectionLength: 10,
    //     ajax: {
    //         type:'post',
    //         url: '/category/search.action',
    //         data: function (params) {
    //             return {
    //                 keyword: params.term,
    //             }
    //         },
    //         processResults: function (result) {
    //             if(result.success){
    //                 let data = result.data;
    //                 return {
    //                     results: $.map(data, function (dataItem) {
    //                         dataItem.text = dataItem.name + (dataItem.cusName ? '(' + dataItem.cusName + ')' : '');
    //                         return dataItem;
    //                     })
    //                 };
    //             }else{
    //                 bs4pop.alert(result.message, {type: 'error'});
    //                 return;
    //             }
    //         }
    //     }
    // }

        //品类搜索
        //品类搜索自动完成
        var categoryAutoCompleteOption = {
            serviceUrl: '/stock/categoryCycle/search.action',
            paramName : 'keyword',
            displayFieldName : 'name',
            showNoSuggestionNotice: true,
            noSuggestionNotice: '无匹配结果',
            transformResult: function (result) {
                if(result.success){
                    let data = result.data;
                    return {
                        suggestions: $.map(data, function (dataItem) {
                            return $.extend(dataItem, {
                                    value: dataItem.name + '（' + dataItem.code + '）'
                                }
                            );
                        })
                    }
                }else{
                    bs4pop.alert(result.message, {type: 'error'});
                    return false;
                }
            },
            selectFn: function (suggestion) {
                $("#cycle").val(suggestion.cycle);
                getCycle($("#stockInDate").val(),suggestion.cycle)
            }}

    var boothAutoCompleteOption = {
        paramName: 'keyword',
        displayFieldName: 'name',
        serviceUrl: '/booth/search.action',
        transformResult: function (result) {
            debugger
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
        let typeName = $('#typeCode').find("option:selected").text();
        bui.util.yuanToCentForMoneyEl(formData);
        $.extend(formData,{typeName});
        return formData;
    }

    // 提交保存
    function doAddOtherFeeHandler(){
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
            url: "${contextPath}/otherFee/doAdd.action",
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