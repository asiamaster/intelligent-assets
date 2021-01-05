<script>
//行索引计数器
let itemIndex = 0;
//子单数量
let itemCount = 0;
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
initSwipeIdCard({
    id:'getCustomer',
});

//品类搜索
//品类搜索自动完成
var categoryAutoCompleteOption = {
		serviceUrl: '/stock/categoryCycle/search.action',
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
			$("#cycle").val(suggestion.cycle);
			getCycle($("#stockInDate").val(),suggestion.cycle)
		}}




//初始化入库单信息
$(function() {
	//逆向计算存储天数
	let st = moment('${stockIn.stockInDate!}');
	let et = moment('${stockIn.expireDate!}');
	$("#cycle").val(et.diff(st,'day'));
	let uom = '${stockIn.uom!}';
	$("#uom").val(uom);
	/*if(uom == "1"){
		$("#unitPrice").val(parseFloat($("#amount").val())/parseFloat($("#weight").val()));
	}else{
		$("#unitPrice").val(parseFloat($("#amount").val())/parseFloat($("#quantity").val()));
	}*/
	let stockInDetails = JSON.parse('${stockIn.jsonStockInDetailDtos!}');
	for (let stockDetail of stockInDetails) {
		stockDetail.departmentId=${stockIn.departmentId!};
		initDetailItem(stockDetail);
	}
});


/**
 * 添加子单
 * */
function adddetailItem() {
	let stockDetail = {};
	$('#details').append(bui.util.HTMLDecode(template('detailInfo' + type, {
		index: ++itemIndex,stockDetail
	})))
	changeDistrict(itemIndex,0,null,'one');
	let validate = $("#saveForm_"+itemIndex).validate(saveFormDetail);
	itemCount++;
	canDel();
}

/**
 * 子单数据回填
 * */
function initDetailItem(stockDetail) {
	stockDetail.amount = (stockDetail.amount/100).toFixed(2) + '';
	$('#details').append(bui.util.HTMLDecode(template('detailInfo' + type, {
		stockDetail,index: ++itemIndex
	})))
	/*for (let chargeItem of stockDetail.businessChargeItem) {
		chargeItem.amount = (chargeItem.amount/100).toFixed(2) + '';
		$("#saveForm_"+itemIndex).find(".chargeItems").after(bui.util.HTMLDecode(template('businessChargeItem', {
			chargeItem
		})))
	}*/
	for (let chargeItem of stockDetail.businessChargeItem) {
		chargeItem.amount = (chargeItem.amount/100).toFixed(2) + '';
		$("#chargeItem_"+chargeItem.chargeItemId+"_"+itemIndex).val(chargeItem.amount);
		$("#chargeItem_"+chargeItem.chargeItemId+"_"+itemIndex).attr("item-id",chargeItem.id);
		$("#chargeItem_"+chargeItem.chargeItemId+"_"+itemIndex).attr("chargeItem-version",chargeItem.version);
	}
	weightItems.set(itemIndex+"",stockDetail.stockWeighmanRecord);
	changeDistrict(itemIndex,0,stockDetail.parentDistrictId,'one');
	changeDistrict(itemIndex,stockDetail.parentDistrictId,stockDetail.districtId,'two');
	changeAssets(itemIndex,stockDetail.districtId,stockDetail.assetsId);
	let validate = $("#saveForm_"+itemIndex).validate(saveFormDetail);
	itemCount++;
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
		bui.util.yuanToCentForMoneyEl(detail);
		removeDetails.push(detail);
	}
	$(this).closest('.detailInfo').remove();
	countNumber("quantity");
	countNumber("weight");
	countNumber("amount");
	itemCount--;
	canDel();
});

function canDel(){
	if(itemCount==1){
		$('.item-del').attr("disabled","disabled");
	}else{
		$('.item-del').removeAttr("disabled");
	}
}

//计算金额,重量,数量
/*$(document).on('change', '.number_change', function() {
	let fieldName = $(this).attr("name");
	countNumber(fieldName);
	countItemNumber($(this));

});

function countNumber(name){
	let total = 0;
	$("#details").find("form").find("[name="+name+"]").each(function() {
		total = parseFloat(total) + parseFloat($(this).val());
		$("#saveForm").find("[name="+name+"]").val(total);
	});
}
function countItemNumber(obj){
	let total = 0;
	obj.closest("form").find(".amount-item").each(function(){
		total = parseFloat(total) + parseFloat($(this).val()==""?0:$(this).val());
	})
	obj.closest("form").find("[name=amount]").val(total);
	countNumber("amount")
}*/

function buildFormData() {
	// let formData = new FormData($('#saveForm')[0]);
	let departmentName = $('#departmentId').find("option:selected").text();
	//let categoryName = $('#categoryId').find("option:selected").text();
	let formData = $('#saveForm').serializeObject();
	formData.departmentName = departmentName;
	//formData.categoryName = categoryName;
	formData.origin = city.value.join(",");
    formData.originPath = city.originName;
    
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
			businessCharge.version=$(this).attr("chargeItem-version");
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
		// 只填入一级区域
		if(isNull(detail.districtId)){
			districtName = $(this).find("[name=parentDistrictId]").find("option:selected").text();
			detail.districtId = detail.parentDistrictId;
		}
		let assetsName = $(this).find("[name=assetsId]").find("option:selected").text();
		detail.districtName = districtName;
		detail.assetsCode = assetsName;
		detail.categoryId = formData.categoryId;
		detail.categoryName = formData.categoryName;
		if(type == 3){
			let index = $(this).attr("id").split("_")[1];
			let weightItem = weightItems.get(index);
			weightItem.images = JSON.stringify(weightItem.images);
			detail.stockWeighmanRecordDto = weightItem;
		}
		detail.mchId=mchId;
		// 动态收费项
		let itemBusinessChargeDtos = []
		$(this).find('.chargeItem').each(function(){
			let itemBusinessCharge = {};
			itemBusinessCharge.chargeItemId=$(this).attr("name").split("_")[1];
			itemBusinessCharge.chargeItemName=$(this).attr("chargeItem");
			itemBusinessCharge.amount=parseInt($(this).val())*100;
			if($(this).attr("item-id") != ""){
				itemBusinessCharge.id=$(this).attr("item-id");
				itemBusinessCharge.version=$(this).attr("chargeItem-version");
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
	formData.mchId=mchId;
	formData.stockInDetailDtos = stockDetails;
	return JSON.stringify(formData);
}

// 提交保存
function doUpdateStockInHandler() {
	if(!validateForm()){
		return;
	}
	$.ajax({
		type: "POST",
		url: "${contextPath}/stock/stockIn/update.action",
		data: buildFormData(),
		dataType: "json",
		contentType: "application/json",
		success: function(ret) {
            if(!ret.success){
                bs4pop.alert(ret.message, {type: 'error'});
            }else{
            	parent.closeDialog(parent.dia);
				parent.$('#grid').bootstrapTable('refresh');	
            }
            bui.loading.hide();
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
