<script>

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
initSwipeCard({
	id: 'getCustomer',
});


//品类搜索
//品类搜索自动完成
var categoryAutoCompleteOption = {
	width: '100%',
	language: 'zh-CN',
	maximumSelectionLength: 10,
	ajax: {
		type: 'post',
		url: '/category/search.action',
		data: function(params) {
			return {
				keyword: params.term,
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



/**
 * 添加子单
 * */
function adddetailItem() {
	$('#details').append(bui.util.HTMLDecode(template('detailInfo'+type, {
		index: ++itemIndex
	})))
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
	$(this).closest('.detail').remove();
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

//数据组装
function buildFormData() {
	// let formData = new FormData($('#saveForm')[0]);
	let departmentName = $('#departmentId').find("option:selected").text();
	let categoryName = $('#categoryId').find("option:selected").text();
	let formData = $('#saveForm').serializeObject();
	formData.departmentName = departmentName;
	formData.categoryName = categoryName;
	formData.type = type;
	// 动态收费项
	let businessChargeDtos = []
	$('#saveForm').find('.chargeItem').each(function(){
		let businessCharge = {};
		businessCharge.chargeItemId=$(this).attr("name").split("_")[1];
		businessCharge.chargeItemName=$(this).attr("chargeItem");
		businessCharge.amount=parseInt($(this).val())*100;
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
		detail.districtName = districtName;
		detail.categoryId = formData.categoryId;
		detail.categoryName = formData.categoryName;
		detail.stockWeighmanRecordDto = weightItems.get(index);
		
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
		url: "${contextPath}/stock/stockIn/insert.action",
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




/**
 * 打开新增窗口:页面层
 */
/*
function openWeightHandler(index) {
	let weightItem = $("#saveForm_"+index)
	console.log(weightItem.attr("id"));
    dia = bs4pop.dialog({
        title: '获取地磅读数',//对话框title
        content: bui.util.HTMLDecode(template("weighman", {})), //对话框内容，可以是 string、element，$object
        width: '80%',//宽度
        height: '95%',//高度
        btns: [{label: '取消',className: 'btn-secondary',onClick(e){

            }
        }, {label: '确定',className: 'btn-primary',onClick(e){
        	let ob = $("#weighmanForm").serializeObject();
        	weightItems.set(index,ob);
        	weightItem.find("[name=weight]").val($("#grossWeight").val());
            }
        }]
    });
}*/





</script>
