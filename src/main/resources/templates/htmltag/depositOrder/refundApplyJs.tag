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

    /*****************************************函数区 begin************************************/

    //获取table Index
    function getIndex(str) {
        return str.split('_')[1];
    }
    //定金退款总金额 = 定金退款金额
    $('input[name="totalRefundAmount"]').bind('input propertychange', function() {
        $('input[name="payeeAmount"]').val($('input[name="totalRefundAmount"]').val());
    });

    /**
     * 构建退款申请表单提交数据
     * @returns {{}|jQuery}
     */
    function buildFormData(){
        let formData = $("input:not(table input),textarea,select").serializeObject();
        bui.util.yuanToCentForMoneyEl(formData);
        return $.extend(formData, {logContent: $('#id').val() ? Log.buildUpdateContent() : ''});
    }
    /**
     * 表单baocun
     * @returns {boolean}
     */
    function saveFormHandler(){
        if (!$('#refundApplyForm').valid()) {
            return false;
        }
        bui.loading.show('努力提交中，请稍候。。。');
        $.ajax({
            type: "POST",
            url: "${contextPath}/depositOrder/saveOrUpdateRefundOrder.action",
            data: JSON.stringify(buildFormData()),
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            success: function (ret) {
                if(!ret.success){
                    bs4pop.alert(ret.message, {type: 'error'});
                }else{
                    parent.closeDialog(parent.dia);
                }
                bui.loading.hide();
            },
            error: function (a, b, c) {
                bui.loading.hide();
                bs4pop.alert('远程访问失败', {type: 'error'});
            }
        });

    }

    /*****************************************函数区 end**************************************/
    $('#save').on('click', bui.util.debounce(saveFormHandler,1000,true));
</script>