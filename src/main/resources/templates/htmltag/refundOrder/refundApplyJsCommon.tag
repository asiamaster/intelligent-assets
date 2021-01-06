<script>

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

    function buildFormData(){
        let formData = $("input:not(table input),textarea,select").serializeObject();
        let refundFeeItems = [];
    	$("form").find(".chargeItem").each(function(){
    		let businessCharge = {};
    		businessCharge.chargeItemName=$(this).attr("chargeItem");
    		businessCharge.id=$(this).attr("item-id");
    		businessCharge.amount=parseInt($(this).val())*100;
    		businessCharge.chargeItemId=$(this).attr("name").split("_")[1];
    		if (businessCharge != {}) {
    			refundFeeItems.push(businessCharge);
    		}
    	})
    	formData.refundFeeItems = refundFeeItems;
        bui.util.yuanToCentForMoneyEl(formData);
        $.extend(formData,{logContent:Log.buildUpdateContent()});
        return JSON.stringify(formData);
    }

    // 定金退款保存
    function updateFormHandler(){
        if (!$('#refundApplyForm').valid()) {
            return false;
        } else {
            bui.loading.show('努力提交中，请稍候。。。');
            // let _formData = new FormData($('#saveForm')[0]);
            $.ajax({
                type: "POST",
                url: "${contextPath}/refundOrder/doUpdateV1.action",
                data: buildFormData(),
                dataType: "json",
        		contentType: "application/json",
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
    
    $(function () {
    	calcTotalRefundAmount();
	});
    /**
     * 计算退款总金额
     */
    function calcTotalRefundAmount(){
    	let total = 0;
    	$("form").find(".chargeItem").each(function() {
    		total = parseInt(total) + parseInt($(this).val());
    		$('#totalRefundAmount').val((total.mul(100)).centToYuan());
    	    $('[name="payeeAmount"]').val((total.mul(100)).centToYuan());
    	});
    }

    $(document).on('input', '.chargeItem', function(){
    	calcTotalRefundAmount();
    })

</script>


