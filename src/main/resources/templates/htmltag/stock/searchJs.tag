<script>
//品类搜索
//品类搜索自动完成
function strIsEmpty(str){
	return str==null||str==""||str==undefined
}


/*$(document).on('change', '#clear',function(){
	$('#select2-categoryId-container').html('--全部--')

});*/
$("#clear").click(function(){
	$('#select2-categoryId-container').html('--全部--')
});

var categoryAutoCompleteOption = {
		width: '100%',
		language: 'zh-CN',
		maximumSelectionLength: 10,
		ajax: {
			type: 'post',
			url: '/stock/categoryCycle/searchV2.action',
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

//部门变动对应,冷库区域变动
//部门变动 冷库区域变更
$(document).on('change', '#departmentId', function() {
	$('#district-one').val("");
	$('#district-two').html('<option value="" selected="">--全部--</option>');
	$('#assetsId').val("");
	$('#assetsId').html('<option value="" selected="">--请选择区域--</option>');
	let data = [];
	// 根据部门获取区域
	$.ajax({
		type: "POST",
		url: "/stock/stockIn/searchDistrict.action",
		data: {
			parentId: 0,
			departmentId:$('#departmentId').val()},
		success: function (data) {
			if (data.code == "200") {
				var array = $.map(data.data, function (obj) {
					obj.text = obj.text || obj.name;
					return obj;
				});
				if (array.length == 0) {
					let htmlConent = '<option value="" selected="">--无--</option>';
					$('#district-one').html(htmlConent);
				}else{
					//当index大于0 标记为新增详情,只触发当前index的更新
					let htmlConent = '<option value="" selected="">--请选择--</option>';
					for (let item of array) {
						htmlConent+='<option  value="'+item.id+'" >'+item.name+'</option>'
					}
					$("#district-one").html(htmlConent);
				}
			
				
			}
		}
	});
	
});


$(function () {
	getDistrict($("#district-one"),"0");
	//getAssets($("#assetsId"),0);
});

//一级区域变动事件
$(document).on('change', '#district-one', function() {
	getDistrict($("#district-two"),$("#district-one").val());
	getAssets($("#assetsId"),$(this).val(),1);
});
$(document).on('change', '#district-two', function() {
	getAssets($("#assetsId"),$(this).val(),2);
});

//冷库区域变更  对应子单的冷库更新


//区域搜索
function getDistrict(obj,parent){
	if(strIsEmpty(parent)){
		obj.html('<option value="" selected="">-- 全部--</option>');
		return;
	}
	$.ajax({
		type: "POST",
		url: "/stock/stockIn/searchDistrict.action",
		data: {parentId: parent},
		success: function (data) {
			if (data.code == "200") {
				var array = $.map(data.data, function (obj) {
					obj.text = obj.text || obj.name;
					return obj;
				});
				if (array.length == 0) {
					obj.html('<option value="" selected="">-- 全部--</option>');
				} else {
					//部门变动 触发全部index更新
					obj.html('<option value="" selected="">-- 全部--</option>');
					var htmlConent = '<option value="" selected>-- 全部--</option>';
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
function getAssets(obj,districtId,level){
	if(strIsEmpty(districtId)){
		obj.html('<option value="" selected="">-- 全部--</option>');
		return;
	}
	let param = {};
	if(level == 1){
		param = {firstDistrictId: districtId,
            	assetsType:2
            	//mchId:mchId
            }
	}else {
		param = {secondDistrictId: districtId,
            	assetsType:2
            	//mchId:mchId
            }
	}
	$.ajax({
		type: "GET",
		url: "/assets/searchAssets.action",
        data: param,
		success: function (data) {
			if (data.code == "200") {
				var array = $.map(data.data, function (obj) {
					obj.text = obj.text || obj.name;
					return obj;
				});
				if (array.length == 0) {
					obj.html('<option value="" selected="">-- 全部--</option>');
				} else {
					//部门变动 触发全部index更新
					obj.html('<option value="" selected="">-- 全部--</option>');
					var htmlConent = '<option value="" selected>-- 全部--</option>';
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