<script>
    /**
     *
     * @Date 2019-11-06 17:30:00
     * @author jiangchengyong
     *
     ***/
    $('#refundType').on('change', function(){
        debugger;
        var refundType = $('#refundType').val();
        if (refundType == ${@com.dili.settlement.enums.SettleWayEnum.CASH.getCode()}){
            // 现金
            $('#bankInfo').css('display','none');
            $('#accountInfo').css('display','none');
        }else if(refundType == ${@com.dili.settlement.enums.SettleWayEnum.BANK.getCode()}){
            // 银行卡
            $('#bankInfo').css('display','flex');
            $('#accountInfo').css('display','none');
        }else if(refundType == ${@com.dili.settlement.enums.SettleWayEnum.CARD.getCode()}){
            // 园区卡
            $('#accountInfo').css('display','flex');
            $('#bankInfo').css('display','none');
            debugger
            var customerId = 226;
            $.ajax({
                type: 'get',
                url: '/account/getAccountListByCustomerId.action?customerId='+customerId,
                dataType: 'json',
                async: false,
                success: function (ret) {
                    debugger
                    if (ret.success) {
                        var aInfoList = ret.data;
                        for(var i=0;i<aInfoList.length;i++){
                            $('#accountInfo').append( '<option value="'+aInfoList[i].cardNo+'">'+aInfoList[i].cardNo+'</option>' ); //添加option
                        }
                    } else {
                        bs4pop.alert('此卡无效，不能交易！', {type : "warning"});
                        return false;
                    }
                }
            });


        }
    })

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
        bui.util.yuanToCentForMoneyEl(formData);
        $("#transferTable tbody").find("tr").each(function(){
            let transferDeductionItem = {};
            $(this).find("input").each(function(t,el){
                if(!this.value){
                    return false;
                }
                let fieldName = $(this).attr("name").split('_')[0];
                transferDeductionItem[fieldName] =  $(this).hasClass('money')? Number($(this).val()).mul(100) : $(this).val();
            });
            if (Object.keys(transferDeductionItem).length > 0) {
                transferDeductionItems.push(transferDeductionItem);
            }
        });
        return $.extend(formData, {
            transferDeductionItems,
            logContent: $('#id').val() ? Log.buildUpdateContent() : ''
        });
    }
    /**
     * 验证实际退款金额是否小于
     * @returns {boolean}
     */
    function validateActualRefundAmount(){
        let payeeAmount = Number($('#payeeAmount').val());
        let totalRefundAmount = Number($('#totalRefundAmount').val());
        let transferAmount = 0;
        $("table input[name^='payeeAmount']").filter(function () {
            return this.value
        }).each(function (i) {
            transferAmount = Number(this.value).add(transferAmount);
        });

        if (totalRefundAmount.mul(100) != (payeeAmount.mul(100) + transferAmount.mul(100))) {
            return false;
        }
        return true;
    }

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
        if(arrRepeatCheck(payeeIds)){
            bs4pop.alert('存在重复转低收款人，请检查！');
            return;
        }

        if(!validateActualRefundAmount()){
            bs4pop.alert('退款金额分配错误，请重新修改再保存');
            return;
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

    /*****************************************自定义事件区 begin************************************/
    //摊位新增事件
    $('#addTransfer').on('click', function(){
        addTransferItem();
    });

    //摊位删除事件
    $(document).on('click', '.item-del', function () {
        if ($('#transferTable tr').length > 1) {
            $(this).closest('tr').remove();
        }
    });

    /*****************************************自定义事件区 end**************************************/
    $('#save').on('click', bui.util.debounce(saveFormHandler,1000,true));
</script>