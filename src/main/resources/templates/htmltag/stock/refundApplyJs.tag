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
$(function () {
	calcTotalRefundAmount();
});
/******************************驱动执行区 end****************************/

/*****************************************函数区 begin************************************/






/**
 * 构建退款申请表单提交数据
 * @returns {{}|jQuery}
 */
function buildFormData(){
	let formData = $("input:not(table input),textarea,select").serializeObject();
	let refundFeeItems = [];
	$("form").find(".chargeItem").each(function(){
		let businessCharge = {};
		businessCharge.chargeItemName=$(this).attr("chargeItem");
		businessCharge.amount= parseFloat(new Number($(this).val()).mul(100)).toFixed(0);
		businessCharge.chargeItemId=$(this).attr("name").split("_")[1];
		if (businessCharge != {}) {
			refundFeeItems.push(businessCharge);
		}
	})
	formData.refundFeeItems = refundFeeItems;
	bui.util.yuanToCentForMoneyEl(formData);
	return JSON.stringify(formData);
}

/**
 * 计算退款总金额
 */
function calcTotalRefundAmount(){
	let total = 0;
	$("form").find(".chargeItem").each(function() {
		total = parseFloat(total) + parseFloat($(this).val());
		$('#totalRefundAmount').val(total.toFixed(2));
	    $('[name="payeeAmount"]').val(total.toFixed(2));
	});
}

$(document).on('input', '.chargeItem', function(){
	calcTotalRefundAmount();
})

//费用联动
$(document).on('input', '[name="totalRefundAmount"]', function(){
    let totalRefundAmount = $('[name="totalRefundAmount"]').val();
    $('[name="payeeAmount"]').val(totalRefundAmount);
})




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
		url: "/stock/stockIn/refund.action",
		data: buildFormData(),
		dataType: "json",
		contentType: "application/json",
		success: function (ret) {
            if(!ret.success){
                bs4pop.alert(ret.message, {type: 'error'});
            }else{
            	parent.closeDialog(parent.dia);
				parent.$('#grid').bootstrapTable('refresh');	
            }
            bui.loading.hide();
        },
		error: function (error) {
			bui.loading.hide();
			bs4pop.alert('远程访问失败', {type: 'error'});
		}
	});

}

/*****************************************函数区 end**************************************/

/*****************************************自定义事件区 begin************************************/


/*****************************************自定义事件区 end**************************************/
</script>