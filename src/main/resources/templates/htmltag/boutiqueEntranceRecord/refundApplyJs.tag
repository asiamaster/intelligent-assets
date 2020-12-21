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
                var customerCellPhone = suggestion.contactsPhone;
                $('#payeeCellphone').val(suggestion.contactsPhone);
            }
        };

        var customerNameTableAutoCompleteOption = $.extend({},customerNameAutoCompleteOption,{
            selectFn: function (suggestion,element) {
                let index = getIndex($(element).attr('id'));
                $('#certificateNumber_'+index).val(suggestion.certificateNumber);
            }
        });

    /*********************变量定义区 end***************/


    /******************************驱动执行区 begin***************************/
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

    /**
     * 判断数组中的元素是否重复出现
     * 验证重复元素，有重复返回true；否则返回false
     * @param arr
     * @returns {boolean}
     */
    function arrRepeatCheck(arr) {
        var hash = {};
        for(var i in arr) {
            if(hash[arr[i]]) {
                return true;
            }
            // 不存在该元素，则赋值为true，可以赋任意值，相应的修改if判断条件即可
            hash[arr[i]] = true;
        }
        return false;
    }

    /**
     * 构建退款申请表单提交数据
     * @returns {{}|jQuery}
     */
    function buildFormData(){
        let formData = $("input:not(table input),textarea,select").serializeObject();
        let transferDeductionItems = [];

        $("#transferTable tbody").find("tr").each(function(){
            let transferDeductionItem = {};
            $(this).find("input").each(function(t,el){
                if(!this.value){
                    return false;
                }
                let fieldName = $(this).attr("name").split('_')[0];
                transferDeductionItem[fieldName] = $(this).hasClass('money')? Number($(this).val()).mul(100) : $(this).val();
            });
            if (Object.keys(transferDeductionItem).length > 0) {
                transferDeductionItems.push(transferDeductionItem);
            }
        });

        // 构建退款参数
        formData.transferDeductionItems = transferDeductionItems;
        bui.util.yuanToCentForMoneyEl(formData);
        return JSON.stringify(formData);
    }

    //定金退款总金额 = 定金退款金额
    $('input[name="totalRefundAmount"]').bind('input propertychange', function() {
        $('input[name="payeeAmount"]').val($('input[name="totalRefundAmount"]').val());
    });

    /**
     * 表单baocun
     * @returns {boolean}
     */
    function saveFormHandler(){
        if (!$('#refundApplyForm').valid()) {
            return false;
        }

        let payeeIds = $("table input[name^='payeeId']").filter(function () {
            return this.value
        }).map(function(){
            return $('#payeeId_'+getIndex(this.id)).val();
        }).get();

        bui.loading.show('努力提交中，请稍候。。。');
        $.ajax({
            type: "POST",
            url: "/boutiqueFeeOrder/refund.action",
            data: buildFormData(),
            dataType: "json",
            contentType: "application/json",
            success: function (ret) {
                bui.loading.hide();
                if(!ret.success){
                    bs4pop.alert(ret.message, {type: 'error'});
                }else{
                    parent.dia.hide()
                }
            },
            error: function (error) {
                bui.loading.hide();
                bs4pop.alert('远程访问失败', {type: 'error'});
            }
        });

    }

    $('#save').on('click', bui.util.debounce(saveFormHandler,1000,true));

    /*****************************************自定义事件区 end**************************************/
</script>