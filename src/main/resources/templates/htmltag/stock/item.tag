
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
		
			<div class="form-group col-4 itmedetail-code">
			<label for="">子单号:</label> <input readonly type="text" class="form-control"  value="{{stockDetail.code}}" />
			</div>

					<div class="form-group col-4">
						<input type="hidden"  id="code_{{index}}" name="code" value="{{stockDetail.code}}" required  />
						<label for="">接车单号:</label> <input type="text" class="form-control" id="pickupNumber_{{index}}" name="pickupNumber" value="{{stockDetail.pickupNumber}}" maxlength="50" />
					</div>
					<div class="form-group col-4">
						<label for="">车型:<i class="red">*</i></label>
						<#bautoCompleteProvider _hiddenDomainId="carTypePublicCode_{{index}}" _hiddenDomainName="carTypePublicCode" _displayDomainId="carTypePublicName_{{index}}"
							 _displayDomainName="carTypePublicName" _placeholder="" _escape="true" _validatorMethod="isSelected" _optionVariable="carTypeAutoCompleteOption"
							 _required="true" _value="{{stockDetail.carTypePublicCode}}" _text="{{stockDetail.carTypePublicName}}" />
					</div>
					<div class="form-group col-4">
						<label for="">车牌号:</label> <input type="text" class="form-control" id="carPlate_{{index}}" name="carPlate" value="{{stockDetail.carPlate}}" maxlength="20" />
					</div>
					<div class="form-group col-4">
						
						<label for="">区域:<i class="red">*</i></label>
		                <div class="input-group">
		                    <select class="form-control districtId districtId_one" id="districtId_one_{{index}}" name="districtId_one" required>

		                    </select>
		                    <select class="form-control districtId" id="districtId_two_{{index}}" name="districtId_two">
		                        <option value="">-- 请选择上级区域 --</option>
		                    </select>
		                </div>
					</div>
					<div class="form-group col-4">
						<label for="">冷库编号:<i class="red">*</i></label>
						<select id="assetsId_{{index}}" name="assetsId" class="form-control assetsId"  required> 
						<option value="" selected="">-- 请选择区域 --</option>
						</select>
						<input type="hidden" id="mchid_{{index}}" >
						
					</div>
					<div class="form-group col-4">
						<label for="quantity" class="">入库件数:<i class="red">*</i></label> <input id="quantity_{{index}}" type="number" value="{{stockDetail.quantity}}" class="form-control number_change"
						 name="quantity" range="1 99999999" required />
					</div>
					<div class="form-group col-4">
						<label for="weight" class="">货物净重(公斤):<i class="red">*</i></label> <input id="weight_{{index}}" type="number" value="{{stockDetail.weight}}" class="form-control number_change get-cost"
						 name="weight" range="1 999999999" required />
					</div>
					<div class="form-group col-4">
						<label for="weight" class="">入库金额:<i class="red">*</i></label> <input id="amount_{{index}}" type="number" value="{{stockDetail.amount}}" class="form-control number_change money"
						 name="amount"  required readonly />
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
					    <textarea id="notes_{{index}}" class="form-control" name="notes" rows="3" maxlength="200" value="{{stockDetail.notes}}">{{stockDetail.notes}}</textarea>
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
	<div class="form-group col-4 itmedetail-code">
	<label for="">子单号:</label> <input readonly type="text" class="form-control"  value="{{stockDetail.code}}" />
	</div>
					<div class="form-group col-4">
					
					<input type="hidden"  id="code_{{index}}" name="code" value="{{stockDetail.code}}" required  />

					<label for="">接车单号:</label> <input type="text" class="form-control" id="pickupNumber_{{index}}" name="pickupNumber" value="{{stockDetail.pickupNumber}}"  maxlength="50"/>
					</div>
					<div class="form-group col-4">
					<label for="">车型:<i class="red">*</i></label>
					<#bautoCompleteProvider _hiddenDomainId="carTypePublicCode_{{index}}" _hiddenDomainName="carTypePublicCode" _displayDomainId="carTypePublicName_{{index}}"
						 _displayDomainName="carTypePublicName" _placeholder="" _escape="true" _validatorMethod="isSelected" _optionVariable="carTypeAutoCompleteOption"
						 _required="true" _value="{{stockDetail.carTypePublicCode}}" _text="{{stockDetail.carTypePublicName}}" />
							 </div>
					<div class="form-group col-4">
						<label for="">车牌号:</label> <input type="text" class="form-control" id="carPlate_{{index}}" name="carPlate" value="{{stockDetail.carPlate}}" maxlength="20" />
					</div>
					
					<div class="form-group col-4">
					<label for="">区域:<i class="red">*</i></label>
	                <div class="input-group">
	                    <select class="form-control districtId districtId_one" id="districtId_one_{{index}}" name="districtId_one" required>

	                    </select>
	                    <select class="form-control districtId" id="districtId_two_{{index}}" name="districtId_two">
	                        <option value="">-- 请选择上级区域 --</option>
	                    </select>
	                </div>
					</div>
					<div class="form-group col-4">
						<label for="">冷库编号:<i class="red">*</i></label>
						<select id="assetsId_{{index}}" name="assetsId" class="form-control assetsId" required> 
						<option value="" selected="">-- 请选择区域 --</option>
						</select>
						<input type="hidden" id="mchid_{{index}}" >

					</div>
					<div class="form-group col-4">
						<label for="" class="">入库件数:<i class="red">*</i></label> <input id="quantity_{{index}}" type="number" class="form-control number_change"
						 name="quantity" value="{{stockDetail.quantity}}" range="0 99999999" required />
					</div>
					<div class="form-group col-4">
					<label for="" class="">货物净重(公斤):<i class="red">*</i></label> 
						<div class="input-group">
							<input id="weight_{{index}}" type="number" class="form-control number_change get-cost"
							 name="weight" value="{{stockDetail.weight}}" range="0 999999999" required readonly/>
							 <button type="button" class="btn btn-secondary px-5 weight" onclick = "openWeightUpdateHandler({{index}})">连接地磅</button>

						</div>
					</div>
					<div class="form-group col-4">
						<label for="" class="">入库金额:<i class="red">*</i></label> <input id="amount_{{index}}" type="number" class="form-control number_change money"
						 name="amount" value="{{stockDetail.amount}}"  required readonly/>
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
					<div class="form-group col-4">
						<label for="checkOperatorId">查件员:<i class="red">*</i></label>
						<select id="checkOperatorId_{{index}}" name="checkOperatorId" class="form-control" required></select>
						<#bcomboProvider _id="checkOperatorId_{{index}}" _provider="userDepProvider" _value="{{stockDetail.checkOperatorId}}" _queryParams='{dd_code:"${userTicket.departmentId}", required:false}' _escape="true" />
					</div>
					<div class="form-group col-8">
					    <label for="notes">备注:</label>
					    <textarea id="notes_{{index}}" class="form-control" name="notes" rows="3" value="{{stockDetail.notes}}" maxlength="200">{{stockDetail.notes}}</textarea>
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
					<div class="form-group col-4 itmedetail-code">
					<label for="">子单号:</label> <input readonly type="text" class="form-control"  value="{{stockDetail.code}}" />
					</div>
					<div class="form-group col-4">
					<label for="">区域:<i class="red">*</i></label>
	                <div class="input-group">
	                    <select class="form-control districtId districtId_one" id="districtId_one_{{index}}" name="districtId_one" required>

	                    </select>
	                    <select class="form-control districtId" id="districtId_two_{{index}}" name="districtId_two" >
	                        <option value="">-- 请选择上级区域 --</option>
	                    </select>
	                </div>
					</div>
					<div class="form-group col-4">
						<label for="">冷库编号:<i class="red">*</i></label>
						<select id="assetsId_{{index}}" name="assetsId" class="form-control assetsId" required> 
						<option value="" selected="">-- 请选择区域 --</option>
						</select>
						<input type="hidden" id="mchid_{{index}}" >

					</div>
					<div class="form-group col-4">
						<label for="quantity" class="">入库件数:<i class="red">*</i></label> <input id="quantity_{{index}}" type="number" class="form-control number_change get-cost count-weight"
						 name="quantity" range="1 99999999" value="{{stockDetail.quantity}}" required />
					</div>
					<div class="form-group col-4">
						<label for="quantity" class="">单件重(公斤):<i class="red">*</i></label> <input id="unitWeight_{{index}}" type="number" class="form-control number_change count-weight"
						 name="unitWeight" range="0 99999" value="{{stockDetail.unitWeight}}" required />
					</div>
					<div class="form-group col-4">
						<label for="weight" class="">货物净重(公斤):<i class="red">*</i></label> <input id="weight_{{index}}" type="number" class="form-control number_change"
						 name="weight"  value="{{stockDetail.weight}}" required readonly />
					</div>
					<div class="form-group col-4">
						<label for="weight" class="">入库金额:<i class="red">*</i></label> <input id="amount_{{index}}" type="number" class="form-control number_change money"
						 name="amount"  value="{{stockDetail.amount}}" required readonly/>
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
					    <textarea id="notes_{{index}}" class="form-control" name="notes" rows="3" maxlength="200" >{{stockDetail.notes}}</textarea>
					</div>
					<div class="form-group col-4">
					    <button type="button" class="btn btn-secondary px-5 item-del">删除</button>
					</div>

				</div>
	</form>			
</script>


<script>




// 部门变动 冷库区域变更
$(document).on('change', '#departmentId', function() {
	$('.districtId').val("");
	$('.districtId').html('<option value="" selected="">--请选择上级区域--</option>');
	$('.assetsId').val("");
	$('.assetsId').html('<option value="" selected="">--请选择区域--</option>');
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
				$("#details").find(".districtId_one").each(function(){
					if (array.length == 0) {
						let htmlConent = '<option value="" selected="">--无--</option>';
						$('#districtId_one_'+index).html(htmlConent);
					}else{
						//当index大于0 标记为新增详情,只触发当前index的更新
						let index = $(this).attr('id').split("_")[2];
						let htmlConent = '<option value="" selected="">--请选择--</option>';
						for (let item of array) {
							htmlConent+='<option merchantid="'+item.marketId+'" value="'+item.id+'" >'+item.name+'</option>'
						}
						$('#districtId_one_'+index).html(htmlConent);
					}
				})
			}
		}
	});
	
});
// index 第几个子单  parent 父级区域   value 默认值    level  区域等级(one/two)
function changeDistrict(index,parent,value,level,departmentId){
	if(!strIsNotEmpty(parent)){
		$('#districtId_'+level+'_'+index).html('<option value="" selected="">-- 请选择上级区域--</option>');
		return;
	}
	if(!strIsNotEmpty(departmentId)){
		departmentId = $('#departmentId').val();
	}
	let query = {
			parentId: parent,
			departmentId:departmentId
	}
	$.ajax({
		type: "POST",
		url: "/stock/stockIn/searchDistrict.action",
		data: query,
		success: function (data) {
			if (data.code == "200") {
				var array = $.map(data.data, function (obj) {
					obj.text = obj.text || obj.name;
					return obj;
				});
				if (array.length == 0) {
					$('#districtId_one_'+index).attr('name','districtId');
					$('#districtId_two_'+index).attr('name','districtId_two');
					let htmlConent = '<option value="" selected="">--无--</option>';
					$('#districtId_'+level+'_'+index).html(htmlConent);
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
		// 二级区域值为空,则查询一级区域冷库
		if(!strIsNotEmpty($(this).val())){
			changeAssets(index,$(this).prev().val(),null,1);
		}else{
			changeAssets(index,$(this).val(),null,2);
		}
	}
});

//商户id
let mchId = '${stockIn.mchId!}';
//冷库区域变更  对应子单的冷库更新
$(document).on('change', '.assetsId', function() {
	// 判断是否清除 mchId 逻辑 判断选择了几个冷库,当全部冷库清空,商户id重置
	let count = 0;
	$('.assetsId').each(function(){
		if(strIsNotEmpty($(this).val())){
			count++;
		}
	})
	// 解决选择 数据清空问题
	if(!strIsNotEmpty($(this).val())){
		if(count == 0){
			mchId ="";
		}
	}else{
		if(count == 1){
			mchId ="";
		}
	}
	console.log(mchId);
	if(strIsNotEmpty($(this).val())){
		let id = $(this).attr('id');
		let index = id.split("_")[1];
		let currentM = $(this).find("option:selected").attr('mchid');
		if(!strIsNotEmpty(currentM)){
			bs4pop.alert("所选冷库未绑定商户!", {type: 'error'});
		}
		if(strIsNotEmpty(mchId) && currentM != mchId){
			bs4pop.alert("所选冷库不属于同一商户,请重新选择区域!", {type: 'error'});
			$(this).val("");
			return;
		}
		mchId = currentM;
		console.log(mchId);
	}
});

function changeAssets(index,districtId,value,level){
    if(districtId && districtId!==''){
    	let param = {};
    	if(level == 1){
    		param = {firstDistrictId: districtId,
                	assetsType:2
                	//isOnlyFirstArea:true
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
                    	$('#assetsId_'+index).html('<option value="" selected="">-- 请选择区域 --</option>');
                    } else {
                    	var htmlConent = '<option value="">-- 请选择 --</option>';
                    	
                		for (let item of array) {
                			if(item.id == value){
                    			htmlConent = htmlConent+'<option mchid="'+item.mchId+'" value="'+item.id+'" selected>'+item.text+'</option>';
                			}else{
                    			htmlConent = htmlConent+'<option mchid="'+item.mchId+'" value="'+item.id+'" >'+item.text+'</option>';
                			}
                		}
                		$('#assetsId_'+index).html(htmlConent);
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
	type:'date',
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
	$("#expireDate").val(moment(stockInDate).add(days-1,"days").format("YYYY-MM-DD"))
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

