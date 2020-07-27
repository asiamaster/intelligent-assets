<script>

    /*********************变量定义区 begin*************/
    //     //行索引计数器
    // let itemIndex = 0;
    // var customerNameAutoCompleteOption = {
    //     serviceUrl: '/customer/listNormal.action',
    //     paramName : 'keyword',
    //     displayFieldName : 'name',
    //     showNoSuggestionNotice: true,
    //     noSuggestionNotice: '无匹配结果',
    //     transformResult: function (result) {
    //         if(result.success){
    //             let data = result.data;
    //             return {
    //                 suggestions: $.map(data, function (dataItem) {
    //                     return $.extend(dataItem, {
    //                             value: dataItem.name + '（' + dataItem.certificateNumber + '）'
    //                         }
    //                     );
    //                 })
    //             }
    //         }else{
    //             bs4pop.alert(result.message, {type: 'error'});
    //             return false;
    //         }
    //     },
    //     selectFn: function (suggestion) {
    //         $('#payeeCertificateNumber').val(suggestion.certificateNumber);
    //     }
    // };
    //
    // var customerNameTableAutoCompleteOption = $.extend({},customerNameAutoCompleteOption,{
    //     selectFn: function (suggestion,element) {
    //         let index = getIndex($(element).attr('id'));
    //         $('#certificateNumber_'+index).val(suggestion.certificateNumber);
    //     }
    // });

    /*********************变量定义区 end***************/


    /*********************变量定义区 begin*************/
        //行索引计数器
    let itemIndex = 0;
    var customerNameAutoCompleteOption = {
        serviceUrl: '/customer/listNormal.action',
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
                                value: dataItem.name + '（' + dataItem.certificateNumber + '）'
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
            $('#payeeCertificateNumber').val(suggestion.certificateNumber);
        }
    };

    var customerNameTableAutoCompleteOption = $.extend({},customerNameAutoCompleteOption,{
        selectFn: function (suggestion,element) {
            let index = getIndex($(element).attr('id'));
            $('#payeeCertificateNumber_'+index).val(suggestion.certificateNumber);
        }
    });

    /*********************变量定义区 end***************/


    /******************************驱动执行区 begin***************************/
    $(function () {
        <% if(isNotEmpty(transferDeductionItems)){ %>
        itemIndex += ${transferDeductionItems.~size};
        <% }else{%>
        while ( itemIndex < 1){
            addTransferItem();
        }
        <% }%>

    });
    /******************************驱动执行区 end****************************/

    /*****************************************函数区 begin************************************/

    //获取table Index
    function getIndex(str) {
        return str.split('_')[1];
    }

    /**
     * 添加摊位
     */
    function addTransferItem(){
        $('#transferTable tbody').append(bui.util.HTMLDecode(template('transferItemTpl',{index:++itemIndex})))
    }

    /*****************************************自定义事件区 begin************************************/
    //新增事件
    $('#addTransfer').on('click', function(){
        addTransferItem();
    });

    //删除事件
    $(document).on('click', '.item-del', function () {
        if ($('#transferTable tr').length > 1) {
            $(this).closest('tr').remove();
        }
    });

    /*****************************************自定义事件区 end**************************************/

    function buildFormData(){
        let formData = $("input:not(table input),textarea,select").serializeObject();
        bui.util.yuanToCentForMoneyEl(formData);
        return formData;
    }

    // 定金退款保存
    function saveFormHandler(){
        if (!$('#refundApplyForm').valid()) {
            return false;
        } else {
            bui.loading.show('努力提交中，请稍候。。。');
            // let _formData = new FormData($('#saveForm')[0]);
            $.ajax({
                type: "POST",
                url: "${contextPath}/depositOrder/addRefundOrder.action",
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
    // 费用联动
    $(document).on('input', '[name="totalRefundAmount"]', function(){
        let totalRefundAmount = $('[name="totalRefundAmount"]').val();
        $('[name="payeeAmount"]').val(totalRefundAmount);
    })

</script>


