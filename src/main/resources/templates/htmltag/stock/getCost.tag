<script>

let unitPrice = 0;
let totalAmount = 0;

function setValue(obj,value){
	obj.val(value);
	obj.attr('value',value);
}

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
	setValue($("#saveForm").find("[name="+name+"]"),total)
	getCost();
}
function countItemNumber(obj){
	let total = 0;
	obj.closest("form").find(".amount-item").each(function(){
		total = parseFloat(total) + parseFloat($(this).val()==""?0:$(this).val());
	})
	setValue(obj.closest("form").find("[name=amount]"),total)

	//countNumber("amount")
}

//计算应收取费用
$(document).on('change',"#uom", function() {
	getCost();
});
//计算子单费用
function countItemAmount(){
	let uom = $("#uom").val();
	if(type != 2){
		uom=1;
	}
	// 小数误差 例如 10/3 =3.333333
	let detailsList = $("#details").find("form");
	let lastAmount = totalAmount;
	detailsList.each(function(i,item) {
		let itemAmount = 0;
		// 最后一条数据补齐四舍五入金额
		if(i == (detailsList.length-1)){
			itemAmount = lastAmount;
		}else{
			if(uom == 1){
				itemAmount = new Number(parseFloat($(this).find("[name=weight]").val())*unitPrice).toFixed(2)
			}else{
				itemAmount = new Number(parseFloat($(this).find("[name=quantity]").val())*unitPrice).toFixed(2)
			}
			lastAmount = new Number(parseFloat(lastAmount)-parseFloat(itemAmount)).toFixed(2);
		}
		setValue($(this).find("[name=amount]"),itemAmount)

	})
}


//获取费用
function getCost(){
	var reg= /^[0-9]*$/;
	let detail = {};
	detail.categoryId=$('#categoryId').val();
	detail.quantity=$('#quantity').val();
	detail.weight=$('#weight').val();
	if(!reg.test(detail.quantity) || !reg.test(detail.weight)){
		return;
	}
	detail.type=type;
	detail.uom=$('#uom').val();
	detail.day=$("#cycle").val();
	if(type != 2){
		detail.uom=1;
	}
	if(isNull(detail.categoryId) || (detail.uom == 1 && isNull(detail.weight)) || (detail.uom == 2 && isNull(detail.quantity))){
		return;
	}
	// 司磅入库特殊处理
	if(type == 3 && detail.weight == 0){
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
				totalAmount = 0;
				for (let item of ret.data) {
					let obj = $("#saveForm").find("[name=chargeItem_"+item.chargeItem+"]");
					setValue(obj,item.totalFee);

					totalAmount =  parseFloat(totalAmount) + parseFloat(item.totalFee);
					//countNumber(obj.attr("name"));
					countItemNumber(obj);
				}
				//计算单价
				if(detail.uom == 1){
					unitPrice = new Number(parseFloat($("#amount").val())/parseFloat($("#weight").val())).toFixed(2);
					setValue($("#unitPrice"),unitPrice);
					if($("#weight").val() == 0){
						$("#unitPrice").val(0);
					}
				}else{
					unitPrice = new Number(parseFloat($("#amount").val())/parseFloat($("#quantity").val())).toFixed(2);
					setValue($("#unitPrice"),unitPrice);
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