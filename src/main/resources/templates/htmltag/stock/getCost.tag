<script>
function isNull(value){
	 if (value == null || value == "" || value == undefined) {
        return true;
    }
    return false;
}

//计算金额,重量,数量
$(document).on('change', '.number_change', function() {
	let fieldName = $(this).attr("name");
	countNumber(fieldName);
	// countItemNumber($(this));

});

function countNumber(name){
	let total = 0;
	$("#details").find("form").find("[name="+name+"]").each(function() {
		total = parseFloat(total) + parseFloat($(this).val());
	});
	$("#saveForm").find("[name="+name+"]").val(total);
	getCost();
}
function countItemNumber(obj){
	let total = 0;
	obj.closest("form").find(".amount-item").each(function(){
		total = parseFloat(total) + parseFloat($(this).val()==""?0:$(this).val());
	})
	obj.closest("form").find("[name=amount]").val(total);
	//countNumber("amount")
}

//计算应收取费用
$(document).on('change',"#uom", function() {
	getCost();
});
//计算子单费用
function countItemAmount(){
	let uom = $("#uom").val();
	let unitPrice = parseFloat($("#unitPrice").val());
	$("#details").find("form").each(function() {
		if(uom == 1){
			$(this).find("[name=amount]").val(parseFloat($(this).find("[name=weight]").val())*unitPrice);
		}else{
			$(this).find("[name=amount]").val(parseFloat($(this).find("[name=quantity]").val())*unitPrice);
		}
	})
}
//获取费用
function getCost(){
	let detail = {};
	detail.categoryId=$('#categoryId').val();
	detail.quantity=$('#quantity').val();
	detail.weight=$('#weight').val();
	detail.uom=$('#uom').val();
	if(type != 2){
		detail.uom=1;
	}
	if(isNull(detail.categoryId) || (detail.uom == 1 && isNull(detail.weight)) || (detail.uom == 2 && isNull(detail.quantity))){
		return;
	}
	// 动态收费项
	let itemBusinessChargeDtos = []
	$("#saveForm").find('.chargeItem').each(function(){
		let itemBusinessCharge = {};
		itemBusinessCharge.chargeItemId=$(this).attr("name").split("_")[1];
		itemBusinessCharge.bizType=$(this).attr("businessType");
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
					let obj = $("#saveForm").find("[name=chargeItem_"+item.chargeItem+"]");
					obj.val(item.totalFee);
					//countNumber(obj.attr("name"));
					countItemNumber(obj);
				}
				//计算单价
				if(detail.uom == 1){
					$("#unitPrice").val(parseFloat($("#amount").val())/parseFloat($("#weight").val()));
					if($("#weight").val() == 0){
						$("#unitPrice").val(0);
					}
				}else{
					$("#unitPrice").val(parseFloat($("#amount").val())/parseFloat($("#quantity").val()));
					if($("#quantity").val() == 0){
						$("#unitPrice").val(0);
					}
				}
				
				countItemAmount();
			}
		},
		error: function(error) {
			bui.loading.hide();
			bs4pop.alert('远程访问失败', {
				type: 'error'
			});
		}
	});


}

</script>