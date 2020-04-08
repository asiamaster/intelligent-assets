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
                $('#certificateNumber_'+index).val(suggestion.certificateNumber);
            }
        });

    /*********************变量定义区 end***************/


    /******************************驱动执行区 begin***************************/
    $(function () {
        while ( itemIndex < 1){
            addTransferItem();
        }
    <% if(isNotEmpty(leaseOrderItem)){ %>
        <% if(leaseOrderItem.depositAmountFlag == @com.dili.ia.glossary.DepositAmountFlagEnum.FROZEN.getCode()){ %>
            bs4pop.notice('保证金已被冻结，不能进行退款。', {position: 'bottomleft',autoClose: false});
            $('#formSubmit').attr('disabled', true);
        <% }else if(leaseOrderItem.depositAmountFlag == @com.dili.ia.glossary.DepositAmountFlagEnum.DEDUCTION.getCode()){ %>
            $('#depositRefundAmount').val(0).attr('readonly', true);
            $('#depositRefundAmount').val(0).attr('readonly', true);
        <% } %>
    <% } else {%>
            let refundMaxAmount = Number(${leaseOrder.paidAmount + leaseOrder.depositDeduction + leaseOrder.earnestDeduction + leaseOrder.transferDeduction}).centToYuan();
            $('#totalRefundAmount').attr('max', refundMaxAmount).attr('placeHolder','可退'+refundMaxAmount);
    <% } %>


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
                transferDeductionItem[fieldName] = $(this).hasClass('money')? Number($(this).val()).mul(100) : $(this).val();
            });
            if (Object.keys(transferDeductionItem).length > 0) {
                transferDeductionItems.push(transferDeductionItem);
            }
        });
        return $.extend(formData, {transferDeductionItems, totalRefundAmountFormatStr: $('#totalRefundAmount').val()});
    }

    /**
    * 计算退款总金额
    */
    function calcTotalRefundAmount(){
        let rentRefundAmount = Number($('#rentRefundAmount').val());
        let manageRefundAmount = Number($('#manageRefundAmount').val());
        let depositRefundAmount = Number($('#depositRefundAmount').val());
        $('#totalRefundAmount').val((rentRefundAmount.mul(100) + manageRefundAmount.mul(100) + depositRefundAmount.mul(100)).centToYuan());
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

    /*****************************************函数区 end**************************************/

    /*****************************************自定义事件区 begin************************************/
    $('#formSubmit').on('click', function (e) {
        let validator = $('#refundApplyForm').validate({ignore:''})
        if (!validator.form()) {
            $('.breadcrumb [data-toggle="collapse"]').html('收起 <i class="fa fa-angle-double-up" aria-hidden="true"></i>');
            $('.collapse:not(.show)').addClass('show');
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

        bui.loading.show();
        $.ajax({
            type: "POST",
            url: "/leaseOrder/createRefundOrder.action",
            data: buildFormData(),
            dataType: "json",
            async : false,
            success: function (ret) {
                bui.loading.hide();
                if(!ret.success){
                    bs4pop.alert(ret.message, {type: 'error'});
                }else{
                    parent.closeDialog(parent.dia);
                }
            },
            error: function (a, b, c) {
                bs4pop.alert('远程访问失败', {type: 'error'});
            }
        });

    });

    //摊位新增事件
    $('#addTransfer').on('click', function(){
        addTransferItem({index: ++itemIndex});
    });

    //摊位删除事件
    $(document).on('click', '.item-del', function () {
        if ($('#transferTable tr').length > 1) {
            $(this).closest('tr').remove();
        }
    });

    /*****************************************自定义事件区 end**************************************/
</script>