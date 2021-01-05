
<style>
.detailInfo {
    border-bottom-style: groove;
	margin-bottom: 15px;
}
</style>

<!-- 整车入库 -->
<script id="detailInfo1" type="text/html">
	<form id="saveForm_{{index}}" role="form" class="detailInfo" novalidate>
	<div class="row row-cols-12 detail" id="detailInfo_{{index}}">
					<div class="form-group col-4">
						<input type="hidden"  id="code_{{index}}" name="code" value="{{stockDetail.code}}" required  />
						<label for="">接车单号:</label> <input type="text" class="form-control" id="pickupNumber_{{index}}" name="pickupNumber" value="{{stockDetail.pickupNumber}}" />
					</div>
					<div class="form-group col-4">
						<label for="">车型:<i class="red">*</i></label>
						<#bautoCompleteProvider _hiddenDomainId="carTypePublicCode_{{index}}" _hiddenDomainName="carTypePublicCode" _displayDomainId="carTypePublicName_{{index}}"
							 _displayDomainName="carTypePublicName" _placeholder="" _escape="true" _validatorMethod="isSelected" _optionVariable="carTypeAutoCompleteOption"
							 _required="true" _value="{{stockDetail.carTypePublicCode}}" _text="{{stockDetail.carTypePublicName}}" />
					</div>
					<div class="form-group col-4">
						<label for="">车牌号:</label> <input type="text" class="form-control" id="carPlate_{{index}}" name="carPlate" value="{{stockDetail.carPlate}}"  />
					</div>
					<div class="form-group col-4">
						
						<label for="">区域:</label>
		                <div class="input-group">
		                    <select class="form-control districtId" id="districtId_one_{{index}}" name="districtId_one" required>

		                    </select>
		                    <select class="form-control districtId" id="districtId_two_{{index}}" name="districtId_two">
		                        <option value="">-- 全部 --</option>
		                    </select>
		                </div>
					</div>
					<div class="form-group col-4">
						<label for="">冷库编号:<i class="red">*</i></label>
						<select id="assetsId_{{index}}" name="assetsId" class="form-control assetsId"  required> 
						<option value="" selected="">-- 请选择区域 --</option>
						</select>
						
					</div>
					<div class="form-group col-4">
						<label for="quantity" class="">入库件数:<i class="red">*</i></label> <input id="quantity_{{index}}" type="number" value="{{stockDetail.quantity}}" class="form-control number_change"
						 name="quantity" range="1 9999999" required />
					</div>
					<div class="form-group col-4">
						<label for="weight" class="">货物净重(公斤):<i class="red">*</i></label> <input id="weight_{{index}}" type="number" value="{{stockDetail.weight}}" class="form-control number_change get-cost"
						 name="weight" range="0.01 9999999" required />
					</div>
					<div class="form-group col-4">
						<label for="weight" class="">入库金额:<i class="red">*</i></label> <input id="amount_{{index}}" type="number" value="{{stockDetail.amount}}" class="form-control number_change money"
						 name="amount" range="0.01 9999999" required readonly />
					</div>
					<chargeItems class="chargeItems">
						<!--用于标签定位-->
					</chargeItems>
					<!--
					/*<%if(isNotEmpty(chargeItems) && chargeItems.~size>0){
				        for(item in chargeItems){
				        %>
				        
				        <div class="form-group col-4">
							<label for="amount" class="">${item.chargeItem!}<i class="red">*</i></label> <input id="chargeItem_${item.id!}_{{index}}" type="number" class="form-control amount-item number_change chargeItem money"
							 name="chargeItem_${item.id!}" chargeItem="${item.chargeItem!}" range="0 9999999.99" required  />
						</div>
				    <% } }%>*/
				    -->
					<div class="form-group col-8">
					    <label for="notes">备注:</label>
					    <textarea id="notes_{{index}}" class="form-control" name="notes" rows="1" maxlength="200" value="{{stockDetail.notes}}">{{stockDetail.notes}}</textarea>
					</div>
					<div class="form-group col-4">
					    <button type="button" class="btn btn-secondary px-5 item-del">删除</button>
					</div>
				
				</div>
	</form>			
</script>
<!-- 司磅入库 -->
<script id="detailInfo3" type="text/html">
	<form id="saveForm_{{index}}" role="form" class="detailInfo" novalidate>
	<div class="row row-cols-12 detail" id="detailInfo_{{index}}">
					
					<div class="form-group col-4">
					<input type="hidden"  id="code_{{index}}" name="code" value="{{stockDetail.code}}" required  />

					<label for="">接车单号:</label> <input type="text" class="form-control" id="pickupNumber_{{index}}" name="pickupNumber" value="{{stockDetail.pickupNumber}}"  />
					</div>
					<div class="form-group col-4">
					<label for="">车型:<i class="red">*</i></label>
					<#bautoCompleteProvider _hiddenDomainId="carTypePublicCode_{{index}}" _hiddenDomainName="carTypePublicCode" _displayDomainId="carTypePublicName_{{index}}"
						 _displayDomainName="carTypePublicName" _placeholder="" _escape="true" _validatorMethod="isSelected" _optionVariable="carTypeAutoCompleteOption"
						 _required="true" _value="{{stockDetail.carTypePublicCode}}" _text="{{stockDetail.carTypePublicName}}" />
							 </div>
					<div class="form-group col-4">
						<label for="">车牌号:</label> <input type="text" class="form-control" id="carPlate_{{index}}" name="carPlate" value="{{stockDetail.carPlate}}"  />
					</div>
					
					<div class="form-group col-4">
					<label for="">区域:</label>
	                <div class="input-group">
	                    <select class="form-control districtId" id="districtId_one_{{index}}" name="districtId_one" required>

	                    </select>
	                    <select class="form-control districtId" id="districtId_two_{{index}}" name="districtId_two">
	                        <option value="">-- 全部 --</option>
	                    </select>
	                </div>
					</div>
					<div class="form-group col-4">
						<label for="">冷库编号:<i class="red">*</i></label>
						<select id="assetsId_{{index}}" name="assetsId" class="form-control" required> 
						<option value="" selected="">-- 请选择区域 --</option>
						</select>
					</div>
					<div class="form-group col-4">
						<label for="" class="">入库件数:<i class="red">*</i></label> <input id="quantity_{{index}}" type="number" class="form-control number_change"
						 name="quantity" value="{{stockDetail.quantity}}" range="1 9999999" required />
					</div>
					<div class="form-group col-4">
						<label for="" class="">货物净重(公斤):<i class="red">*</i></label> <input id="weight_{{index}}" type="number" class="form-control number_change get-cost"
						 name="weight" value="{{stockDetail.weight}}" range="0 9999999" required readonly/>
						 <button type="button" class="btn btn-secondary px-5" onclick = "openWeightUpdateHandler({{index}})">连接地磅</button>
					</div>
					<div class="form-group col-4">
						<label for="" class="">入库金额:<i class="red">*</i></label> <input id="amount_{{index}}" type="number" class="form-control number_change money"
						 name="amount" value="{{stockDetail.amount}}" range="0 9999999" required readonly/>
					</div>
					<chargeItems class="chargeItems">
						<!--用于标签定位-->
					</chargeItems>
					<!--
					/*<%if(isNotEmpty(chargeItems) && chargeItems.~size>0){
				        for(item in chargeItems){
				        %>
				        <div class="form-group col-4">
							<label for="amount" class="">${item.chargeItem!}<i class="red">*</i></label> <input id="chargeItem_${item.id!}_{{index}}" type="number" class="form-control amount-item number_change chargeItem money"
							 name="chargeItem_${item.id!}" chargeItem="${item.chargeItem!}" range="0 9999999.99" required  />
						</div>
				        <% } }%>*/
				        -->
					<div class="form-group col-8">
					    <label for="">备注:</label>
					    <textarea id="notes_{{index}}" class="form-control" name="notes" rows="1" value="{{stockDetail.notes}}" maxlength="200">{{stockDetail.notes}}</textarea>
					</div>
					<div class="form-group col-4">
					    <button type="button" class="btn btn-secondary px-5 item-del">删除</button>
					</div>
				</div>
	</form>			
</script>
<!-- 零散入库 -->
<script id="detailInfo2" type="text/html">
	<form id="saveForm_{{index}}" role="form" class="detailInfo" novalidate>
	<input type="hidden"  id="code_{{index}}" name="code" value="{{stockDetail.code}}" required  />

					<div class="row row-cols-12 detail" id="detailInfo_{{index}}">
					<div class="form-group col-4">
					<label for="">区域:</label>
	                <div class="input-group">
	                    <select class="form-control districtId" id="districtId_one_{{index}}" name="districtId_one" required>

	                    </select>
	                    <select class="form-control districtId" id="districtId_two_{{index}}" name="districtId_two" >
	                        <option value="">-- 全部 --</option>
	                    </select>
	                </div>
					</div>
					<div class="form-group col-4">
						<label for="">冷库编号:<i class="red">*</i></label>
						<select id="assetsId_{{index}}" name="assetsId" class="form-control assetsId" required> 
						<option value="" selected="">-- 请选择区域 --</option>
						</select>
					</div>
					<div class="form-group col-4">
						<label for="quantity" class="">入库件数:<i class="red">*</i></label> <input id="quantity_{{index}}" type="number" class="form-control number_change get-cost count-weight"
						 name="quantity" range="1 9999999" value="{{stockDetail.quantity}}" required />
					</div>
					<div class="form-group col-4">
						<label for="quantity" class="">单件重(公斤):<i class="red">*</i></label> <input id="unitWeight_{{index}}" type="number" class="form-control number_change count-weight"
						 name="unitWeight" range="0.01 99999" value="{{stockDetail.unitWeight}}" required />
					</div>
					<div class="form-group col-4">
						<label for="weight" class="">货物净重(公斤):<i class="red">*</i></label> <input id="weight_{{index}}" type="number" class="form-control number_change"
						 name="weight" range="0.01 9999999" value="{{stockDetail.weight}}" required readonly />
					</div>
					<div class="form-group col-4">
						<label for="weight" class="">入库金额:<i class="red">*</i></label> <input id="amount_{{index}}" type="number" class="form-control number_change money"
						 name="amount" range="0.01 9999999" value="{{stockDetail.amount}}" required readonly/>
					</div>
					<chargeItems class="chargeItems">
						<!--用于标签定位-->
					</chargeItems>
					<!--
					/*<%if(isNotEmpty(chargeItems) && chargeItems.~size>0){
				        for(item in chargeItems){
				        %>
				        <div class="form-group col-4">
							<label for="amount" class="">${item.chargeItem!}<i class="red">*</i></label> <input id="chargeItem_${item.id!}_{{index}}" type="number" class="form-control amount-item number_change chargeItem money"
							 name="chargeItem_${item.id!}" chargeItem="${item.chargeItem!}" range="0 9999999.99" required  />
						</div>
				        <% } }%>*/
				        -->
					<div class="form-group col-8">
					    <label for="notes">备注:</label>
					    <textarea id="notes_{{index}}" class="form-control" name="notes" rows="1" maxlength="200" >{{stockDetail.notes}}</textarea>
					</div>
					<div class="form-group col-4">
					    <button type="button" class="btn btn-secondary px-5 item-del">删除</button>
					</div>

				</div>
	</form>			
</script>


<script>




// 部门变动 冷库区变更
// index 第几个子单  parent 父级区域   value 默认值    level  区域等级(one/two)
function changeDistrict(index,parent,value,level){
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
					$('#districtId_one_'+index).attr('name','districtId');
					$('#districtId_two_'+index).attr('name','districtId_two');

				} else {
					//当index大于0 标记为新增详情,只触发当前index的更新
					let htmlConent = '<option value="" selected="">-- 请选择--</option>';
					for (let item of array) {
						htmlConent+='<option merchantid="'+item.marketId+'" value="'+item.id+'" >'+item.name+'</option>'
					}
					$('#districtId_'+level+'_'+index).html(htmlConent);
					if(value != null){
						$('#districtId_'+level+'_'+index).val(value);
					}
					$('#districtId_two_'+index).attr('name','districtId');
					$('#districtId_one_'+index).attr('name','parentDistrictId');
					
				}
			}
		}
	});
}


//冷库区域变更  对应子单的冷库更新
$(document).on('change', '.districtId', function() {
	let id = $(this).attr('id');
	let index = id.split("_")[2];
	//判断是一级区域or二级区域
	if(id.split("_")[1] == "one"){
		//加载二级区域
		changeDistrict(index,$(this).val(),null,'two');
		changeAssets(index,$(this).val(),null,1);
	}else{
		changeAssets(index,$(this).val(),null,2);
	}
});

//商户id
let mchId = '${stockIn.mchId!}';
//冷库区域变更  对应子单的冷库更新
$(document).on('change', '.assetsId', function() {
	
	if(strIsNotEmpty($(this).val())){
		let id = $(this).attr('id');
		let index = id.split("_")[1];
		mchId = $('#mchid_'+index).val();
	}else{
		// 判断是否清除 mchId
		let count = 0;
		$('.assetsId').each(function(){
			if(strIsNotEmpty($(this).val())){
				count++;
			}
		})
		if(count == 0){
			mchId ="";
		}
		console.log("mchId:"+mchId);
	}
	
});

function changeAssets(index,districtId,value,level){
    if(districtId && districtId!==''){
    	let param = {};
    	if(level == 1){
    		param = {firstDistrictId: districtId,
                	assetsType:2,
                	mchId:mchId
                }
    	}else {
    		param = {secondDistrictId: districtId,
                	assetsType:2,
                	mchId:mchId
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
                    	$('#assetsId_'+index).html('<option value="" selected="">-- 请选择区域 --</option>');
                    } else {
                    	var htmlConent = '<option value="">-- 请选择 --</option>';
                    	let m; //获取商户id
                		for (let item of array) {
                			m = item.marketId;
                			if(item.id == value){
                    			htmlConent = htmlConent+'<option  value="'+item.id+'" selected>'+item.text+'</option>';
                			}else{
                    			htmlConent = htmlConent+'<option  value="'+item.id+'" >'+item.text+'</option>';
                			}
                		}
                		$('#assetsId_'+index).html(htmlConent);
                		$('#assetsId_'+index).after('<input type="hidden" id="mchid_'+index+'" value ="'+m+'">');
                    }
                }
            }
        });
    }else{
    	$('#assetsId_'+index).html('<option value="" selected="">-- 请选择区域 --</option>');
    }

}

//获取到期时间
//日期范围
laydate.render({
	elem: '#stockInDate',
	theme: '#007bff',
	trigger: 'click',
	type:'datetime',
	done: function(value, date){
		//监听日期被切换
		let days = $("#cycle").val();
		getCycle(value,days)
	}
});


function getCycle(stockInDate,days){
	if(!strIsNotEmpty(stockInDate)|| !strIsNotEmpty(days)){
		return;
	}
	$("#expireDate").val(moment(stockInDate).add(days,"days").format("YYYY-MM-DD HH:mm:ss"))
}

$(document).on('change', '.count-weight', function() {
	let ind = $(this).attr('id').split('_')[1];
	let quantity = $("#quantity_"+ind).val();
	let unitWeight = $("#unitWeight_"+ind).val();
	if(strIsNotEmpty(quantity) && strIsNotEmpty(unitWeight)){
		$("#weight_"+ind).val(quantity*unitWeight);
		countNumber("weight");
	}
});

var carTypeAutoCompleteOption = {
		serviceUrl: '/stock/stockIn/searchCarType.action',
		paramName : 'keyword',
		displayFieldName : 'carTypeName',
		hiddenDomainName : 'number',
		showNoSuggestionNotice: true,
		noSuggestionNotice: '无匹配结果',
		transformResult: function (result) {
			if(result.success){
				let data = result.data;
				return {
					suggestions: $.map(data, function (dataItem) {
						return $.extend(dataItem, {
							value: dataItem.carTypeName + '（' + dataItem.number + '）'
						}
						);
					})
				}
			}else{
				bs4pop.alert(result.message, {type: 'error'});
				return false;
			}
		},
		selectFn: function (suggestion ,self) {
            let hiddenDomain = $(self).siblings('input');
            hiddenDomain.val(suggestion.number);
		}}


function strIsNotEmpty(str){
	return str!=null&&str!=""&&str!=undefined
}
</script>

