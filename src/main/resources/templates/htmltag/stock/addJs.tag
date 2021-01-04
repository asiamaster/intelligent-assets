<script>

//设置默认入库时间为当天
$("#stockInDate").attr("value", moment().format('YYYY-MM-DD'));

//子单索引计数器
let itemIndex = 0;
//入库类型
let type = ${type};
//司磅入库相关消息
let weightItems = new Map();
//入库明细
$(function() {
	adddetailItem();
});


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


/**
 * 添加子单
 * */
function adddetailItem() {
	let departmentId = $('#departmentId').val();
	if(departmentId == null || departmentId == ""){
		departmentId =0
	}
	let stockDetail = {};
	$('#details').append(bui.util.HTMLDecode(template('detailInfo'+type, {
		index: ++itemIndex,departmentId,stockDetail
	})))
	changeDistrict(itemIndex,0,null,'one');
	let validate = $("#saveForm_"+itemIndex).validate(saveFormDetail);
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
	$(this).closest('.detailInfo').remove();
	countNumber("quantity");
	countNumber("weight");
	countNumber("amount");
});

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

//数据组装
function buildFormData() {
	// let formData = new FormData($('#saveForm')[0]);
	let departmentName = $('#departmentId').find("option:selected").text();
	//let categoryName = $('#categoryId').find("option:selected").text();
	let formData = $('#saveForm').serializeObject();
	formData.departmentName = departmentName;
	//formData.categoryName = categoryName;
	formData.type = type;
    formData.origin = city.value.join(",");
    formData.originPath = city.originName;
	// 动态收费项
	let businessChargeDtos = []
	$('#saveForm').find('.chargeItem').each(function(){
		let businessCharge = {};
		businessCharge.chargeItemId=$(this).attr("name").split("_")[1];
		businessCharge.chargeItemName=$(this).attr("chargeItem");
		if($(this).val()){
			businessCharge.amount=parseInt($(this).val())*100;
		}else{
			businessCharge.amount=0;
		}
		
		if (businessCharge != {}) {
			businessChargeDtos.push(businessCharge);
		}
	})
	bui.util.yuanToCentForMoneyEl(formData);
	formData.businessChargeItems=businessChargeDtos;
	// 子单
	let stockDetails = [];
	$("#details").find("form").each(function() {
		let index = $(this).attr("id").split("_")[1];
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
		// 组装司磅入库数据
		if(type == 3){
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
			if (itemBusinessCharge != {}) {
				itemBusinessChargeDtos.push(itemBusinessCharge);
			}
			
		})
		bui.util.yuanToCentForMoneyEl(detail);
		detail.businessChargeItems=itemBusinessChargeDtos;
		if (detail != {}) {
			stockDetails.push(detail);
		}
	});
	formData.mchId=mchId;
	formData.stockInDetailDtos = stockDetails;
	return JSON.stringify(formData);
}


// 提交保存
function doAddStockInHandler() {
	//司磅入库参数验证问题
	if(!validateForm()){
		return;
	}
	//let data = buildFormData();
	$.ajax({
		type: "POST",
		url: "${contextPath}/stock/stockIn/insert.action",
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
