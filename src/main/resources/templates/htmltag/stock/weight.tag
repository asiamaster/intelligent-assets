
<script id="weighmanUpdate" type="text/html">
<form id="weighmanForm" role="form" novalidate>
<input type="hidden" value = "{{weightDetail.id}}" name="id" />
<input type="hidden" value = "{{weightDetail.version}}" name="version" />
<div class="row">
			<div class="form-group row col-8">
				<div class="form-group col-12">
					<label for="_certificateNumber">称台重量(公斤)：<i class="red">*</i></label>
					<input id="weight" type="number" class="form-control "
					 name="weight" range="0 9999999" required readonly/>
					<button type="button" class="btn btn-secondary px-5 readWeight" >写入重量</button>
				</div>
				<div class="form-group col-2">
				 	<input type="radio" name="weighman" class="" value="1">
				</div>
				<div class="form-group col-5">
					<label for="_certificateNumber">毛重(公斤)：<i class="red">*</i></label>
					<input id="grossWeight" type="number" class="form-control " value="{{weightDetail.grossWeight}}"
					 name="grossWeight" range="0 9999999" required readonly/>
				</div>	
				<div class="form-group col-5">
					<label for="_certificateNumber">毛重时间：<i class="red">*</i></label>
					<input id="grossWeightDate"  class="form-control "
					 name="grossWeightDate" value="{{weightDetail.grossWeightDate}}" required readonly/>
				</div>
				<div class="form-group col-2">
				 	<input type="radio" name="weighman" class="" value="2">
				</div>
				<div class="form-group col-5">
					<label for="tareWeight" class="">皮重(公斤)：<i class="red">*</i></label> <input id="tareWeight" type="number" class="form-control "
					 name="tareWeight" value="{{weightDetail.tareWeight}}" range="0 9999999" required readonly />
				</div>
				<div class="form-group col-5">
					<label for="tareWeightDate" class="">皮重时间：<i class="red">*</i></label> <input id="tareWeightDate"  class="form-control "
					 name="tareWeightDate" value="{{weightDetail.tareWeightDate}}" required readonly />
				</div>
			</div>
			<div class="form-group col-4" id = "weightPic">
				
				pic
					
			</div>
			</div>
</form>	

</script>

<script>

let curIndex = 0;
//修改子单打开司磅界面 回填数据
function openWeightUpdateHandler(index) {
	curIndex = index;
	let weightDetail = weightItems.get(index+"")
	if(weightDetail == null || weightDetail == ""){
		weightDetail = {};
		weightDetail.images= {
				
		};
	}
	let weightType = 1;
	if(!isNull(weightDetail.grossWeight)){
		weightType = 0;
	}
	// 重量类型 毛重1 皮重0
	let obj = {
			"weightType": weightType,
			"grossWeight": weightDetail.grossWeight,
			"grossDateTime": weightDetail.grossWeightDate,
			"tareWeight": weightDetail.tareWeight,
			"tareDateTime": weightDetail.tareWeightDate,
			"image1":weightDetail.images.beforegross,
			"image2":weightDetail.images.aftergross,
			"image3":weightDetail.images.befortare,
			"image4":weightDetail.images.aftertare
	}
	callbackObj.weighmanInput(JSON.stringify(obj));
	
	
}



$(function(){
	//获取司磅读数
	window.weightCallback=function(data){
		let weightItem = $("#saveForm_"+curIndex);
		let weightDetail = weightItems.get(curIndex+"")
		if(weightDetail == null || weightDetail == ""){
			weightDetail = {};
		}
		if(data.status == 0){
	        return;
	    }
		weightDetail.grossWeight = data.grossWeight;
		weightDetail.grossWeightDate = data.grossDateTime;
		weightDetail.tareWeight = data.tareWeight;
		weightDetail.tareWeightDate =  data.tareDateTime;
		let images = {
				"beforegross":  data.image1,
				"aftergross":  data.image2,
				"befortare": data.image3,
				"aftertare": data.image4
		};
		weightDetail.images = images;
		
		//通过map保存司磅记录,key为index,value为表单信息,方便前端页面组装数据
		weightItems.set(curIndex+"",weightDetail);
		if(strIsNotEmpty(data.grossWeight) && strIsNotEmpty(data.tareWeight)){
			//weightItem.find("[name=weight]").val(parseInt(weightDetail.grossWeight)-parseInt(weightDetail.tareWeight));
			setValue(weightItem.find("[name=weight]"),new Number(parseInt(weightDetail.grossWeight)-parseInt(weightDetail.tareWeight)))
		}else{
			weightItem.find("[name=weight]").val(0);
			$(".money").val(0);
		}
		if(strIsNotEmpty(data.grossWeight) || strIsNotEmpty(data.tareWeight)){
			weightItem.find('.weight').attr('disabled',"true");
			weightItem.find('.weight').text('已过磅');
		}
		//weightItem.find("[name=weight]").val($("#grossWeight").val());
		countNumber("weight");
	}
})

</script>