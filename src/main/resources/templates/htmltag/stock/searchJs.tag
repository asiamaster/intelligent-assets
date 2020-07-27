<script>
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


$(function () {
	getDistrict($("#district-one"),0);
	//getAssets($("#assetsId"),0);
});

//一级区域变动事件
$(document).on('change', '#district-one', function() {
	getDistrict($("#district-two"),$("#district-one").val());
});
$(document).on('change', '#district-two', function() {
	getAssets($("#assetsId"),$(this).val());
});

//冷库区域变更  对应子单的冷库更新


//区域搜索
function getDistrict(obj,parent){
	$.ajax({
		type: "POST",
		url: "/district/search.action",
		data: {parentId: parent},
		success: function (data) {
			if (data.code == "200") {
				var array = $.map(data.data, function (obj) {
					obj.text = obj.text || obj.name;
					return obj;
				});
				if (array.length == 0) {
					obj.html('<option value="" selected="">-- 请选择 --</option>');
				} else {
					//部门变动 触发全部index更新
					obj.html('<option value="" selected="">-- 请选择--</option>');
					var htmlConent = '<option value="" selected>-- 请选择 --</option>';
					for (let item of array) {
						htmlConent = htmlConent+'<option value="'+item.id+'" >'+item.text+'</option>';
					}
					obj.html(htmlConent);
				}
			}
		}
	});
}

//区域搜索
function getAssets(obj,districtId){
	$.ajax({
		type: "POST",
		url: "/stock/stockIn/getColdStorage.action",
		data:{secondArea: districtId,
        	businessType:2
        },
		success: function (data) {
			if (data.code == "200") {
				var array = $.map(data.data, function (obj) {
					obj.text = obj.text || obj.name;
					return obj;
				});
				if (array.length == 0) {
					obj.html('<option value="" selected="">-- 请选择 --</option>');
				} else {
					//部门变动 触发全部index更新
					obj.html('<option value="" selected="">-- 请选择--</option>');
					var htmlConent = '<option value="" selected>-- 请选择 --</option>';
					for (let item of array) {
						htmlConent = htmlConent+'<option value="'+item.id+'" >'+item.text+'</option>';
					}
					obj.html(htmlConent);
				}
			}
		}
	});
}


</script>