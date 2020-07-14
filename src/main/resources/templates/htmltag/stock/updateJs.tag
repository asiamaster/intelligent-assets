<script>
//行索引计数器
let itemIndex = 0;
//入库类型
let type = ${stockIn.type!};
//司磅入库相关消息
let weightItems = new Map();
//记录删除子单
let removeDetails = [];
//获取table Index
function getIndex(str) {
	return str.split('_')[1];
}

//初始化刷卡
initSwipeCard({
	id: 'getCustomer',
});

//品类搜索
//品类搜索自动完成
var categoryAutoCompleteOption = {
		serviceUrl: '/category/search.action',
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
							value: dataItem.name + '（' + dataItem.code + '）'
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
		}}

var districtAutoCompleteOption = {
	width: '100%',
	language: 'zh-CN',
	maximumSelectionLength: 10,
	ajax: {
		type: 'post',
		url: '/district/search.action',
		data: function(params) {
			return {
				departmentId: 54,
			}
		},
		processResults: function(result) {
			if (result.success) {
				let data = result.data;
				return {
					results: $.map(data, function(dataItem) {
						dataItem.text = dataItem.name + (dataItem.cusName ? '(' + dataItem.cusName + ')' : '');
						return dataItem;
					})
				};
			} else {
				bs4pop.alert(result.message, {
					type: 'error'
				});
				return;
			}
		}
	}
}

//初始化入库单信息
$(function() {
	let stockInDetails = JSON.parse('${stockIn.jsonStockInDetailDtos!}');
	for (let stockDetail of stockInDetails) {
		initDetailItem(stockDetail);
		console.log(stockDetail);
	}
});


/**
 * 添加子单
 * */
function adddetailItem() {
	$('#details').append(bui.util.HTMLDecode(template('detailInfo' + type, {
		index: ++itemIndex
	})))
	let validate = $("#saveForm_"+itemIndex).validate(saveFormDetail);
}

/**
 * 子单数据回填
 * */
function initDetailItem(stockDetail) {
	stockDetail.amount = (stockDetail.amount/100).toFixed(2) + '';
	$('#details').append(bui.util.HTMLDecode(template('initDetailInfo' + type, {
		stockDetail,index: itemIndex
	})))
	for (let chargeItem of stockDetail.businessChargeItem) {
		chargeItem.amount = (chargeItem.amount/100).toFixed(2) + '';
		$("#saveForm_"+itemIndex).find(".chargeItems").after(bui.util.HTMLDecode(template('businessChargeItem', {
			chargeItem
		})))
	}
	/*for (let chargeItem of stockDetail.businessChargeItem) {
		$("#chargeItem_"+chargeItem.chargeItemId+"_"+itemIndex).val(chargeItem.amount);
		$("#chargeItem_"+chargeItem.chargeItemId+"_"+itemIndex).attr("item-id",chargeItem.id);
	}*/
	
	let validate = $("#saveForm_"+itemIndex).validate(saveFormDetail);
	itemIndex++;
}

// 添加子单
$('#adddetailItem').on('click', function() {
	if (itemIndex < 11) {
		adddetailItem();
	} else {
		bs4pop.notice('最多10个子单', {
			position: 'leftcenter',
			type: 'warning'
		})
	}
	//buildFormData()
})

//删除行事件 （删除子单）
$(document).on('click', '.item-del', function() {
	let detail = $(this).closest('form').serializeObject();
	if(detail.code != ""){
		detail.categoryId = $('#categoryId').val();
		detail.delete = true;
		removeDetails.push(detail);
	}
	$(this).closest('form').remove();
	countNumber("quantity");
	countNumber("weight");
	countNumber("amount");
});



//计算金额,重量,数量
$(document).on('change', '.number_change', function() {
	let fieldName = $(this).attr("name");
	countNumber(fieldName);
	countItemNumber($(this));

});

function countNumber(name){
	let total = 0;
	$("#details").find("form").find("[name="+name+"]").each(function() {
		total = parseInt(total) + parseInt($(this).val());
		$("#saveForm").find("[name="+name+"]").val(total);
	});
}
function countItemNumber(obj){
	let total = 0;
	obj.closest("form").find(".amount-item").each(function(){
		total = parseInt(total) + parseInt($(this).val()==""?0:$(this).val());
	})
	obj.closest("form").find("[name=amount]").val(total);
	countNumber("amount")
}

function buildFormData() {
	// let formData = new FormData($('#saveForm')[0]);
	let departmentName = $('#departmentId').find("option:selected").text();
	let categoryName = $('#categoryId').find("option:selected").text();
	let formData = $('#saveForm').serializeObject();
	formData.departmentName = departmentName;
	formData.categoryName = categoryName;
	let stockDetails = [];
	// 动态收费项
	let businessChargeDtos = []
	$('#saveForm').find('.chargeItem').each(function(){
		let businessCharge = {};
		businessCharge.chargeItemId=$(this).attr("name").split("_")[1];
		businessCharge.chargeItemName=$(this).attr("chargeItem");
		businessCharge.amount=parseInt($(this).val())*100;
		if($(this).attr("item-id") != ""){
			businessCharge.id=$(this).attr("item-id");
		}
		if (businessCharge != {}) {
			businessChargeDtos.push(businessCharge);
		}
	})
	formData.businessChargeItems=businessChargeDtos;
	bui.util.yuanToCentForMoneyEl(formData);
	$("#details").find("form").each(function() {
		let detail = $(this).serializeObject();
		let districtName = $(this).find("[name=districtId]").find("option:selected").text();
		let assetsName = $(this).find("[name=assetsId]").find("option:selected").text();
		detail.districtName = districtName;
		detail.assetsName = assetsName;
		detail.categoryId = formData.categoryId;
		detail.categoryName = formData.categoryName;
		let index = $(this).attr("id").split("_")[1];
		console.log(weightItems.get(index));
		detail.stockWeighmanRecordDto = weightItems.get(index);
		// 动态收费项
		let itemBusinessChargeDtos = []
		$(this).find('.chargeItem').each(function(){
			let itemBusinessCharge = {};
			itemBusinessCharge.chargeItemId=$(this).attr("name").split("_")[1];
			itemBusinessCharge.chargeItemName=$(this).attr("chargeItem");
			itemBusinessCharge.amount=parseInt($(this).val())*100;
			if($(this).attr("item-id") != ""){
				itemBusinessCharge.id=$(this).attr("item-id");
			}
			if (itemBusinessCharge != {}) {
				itemBusinessChargeDtos.push(itemBusinessCharge);
			}
		})
		detail.businessChargeItems=itemBusinessChargeDtos;
		if (detail != {}) {
			bui.util.yuanToCentForMoneyEl(detail);
			stockDetails.push(detail);
		}
	});
	//stockDetails.concat(removeDetails);
	$.merge(stockDetails, removeDetails)
	formData.stockInDetailDtos = stockDetails;
	return JSON.stringify(formData);
}

// 提交保存
function doAddStockInHandler() {
	if(!$("#saveForm").validate().form()){
		return;
	}
	for(let i=1;i<=itemIndex;i++){
		if(document.getElementById("#saveForm_"+i)){
			if(!$("#saveForm_"+i).validate().form()){
				return;
			}  
		}
	}
	$.ajax({
		type: "POST",
		url: "${contextPath}/stock/stockIn/update.action",
		data: buildFormData(),
		dataType: "json",
		contentType: "application/json",
		success: function(ret) {
			bui.loading.hide();
			if (!ret.success) {
				bs4pop.alert(ret.message, {
					type: 'error'
				});
			} else {
				bs4pop.alert(ret.message, {type: 'success'});
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

function form2JsonString(formId) {
	var paramArray = $('#' + formId).serializeArray();
	/*请求参数转json对象*/
	var jsonObj = {};
	$(paramArray).each(function() {
		jsonObj[this.name] = this.value;

	});
	// json对象再转换成json字符串
	return JSON.stringify(jsonObj);
}



</script>
