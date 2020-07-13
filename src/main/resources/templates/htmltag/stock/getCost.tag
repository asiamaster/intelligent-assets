<script>
function isNull(value){
	 if (value == null || value == undefined) {
        return true;
    }
    return false;
}

//计算应收取费用
$(document).on('blur',".get-cost", function() {
	console.log(3123123);
	let index = $(this).attr("id").split("_")[1];
	let detail = $("#saveForm_"+index).serializeObject();
	detail.categoryId=$('#categoryId').find("option:selected").val();
	detail.assetsId=$("#saveForm_"+index).find("[name=assetsId]").find("option:selected").val();
	if(isNull(detail.categoryId)  || isNull(detail.quantity)){
		return;
	}
	// 动态收费项
	let itemBusinessChargeDtos = []
	$("#saveForm_"+index).find('.chargeItem').each(function(){
		let itemBusinessCharge = {};
		itemBusinessCharge.chargeItemId=$(this).attr("name").split("_")[1];
		itemBusinessChargeDtos.push(itemBusinessCharge);
	})
	detail.businessChargeItems=itemBusinessChargeDtos;
	$.ajax({
		type: "POST",
		url: "${contextPath}/stock/stockIn/getCost.action",
		data: JSON.stringify(detail),
		dataType: "json",
		contentType: "application/json",
		success: function(ret) {
			bui.loading.hide();
			if (!ret.success) {
				bs4pop.alert(ret.message, {
					type: 'error'
				});
			} else {
				for (let item of ret.data) {
					$("#saveForm_"+index).find("[name=chargeItem_"+item.chargeItem+"]").val(item.totalFee);
				}
				 
			}
		},
		error: function(error) {
			bui.loading.hide();
			bs4pop.alert('远程访问失败', {
				type: 'error'
			});
		}
	});

});

</script>