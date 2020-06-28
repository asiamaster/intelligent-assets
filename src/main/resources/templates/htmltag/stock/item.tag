<!-- 整车入库 -->
<script id="detailInfo1" type="text/html">
	<form id="saveForm_{{index}}" role="form" novalidate>
	<div class="row row-cols-12 detail" id="detailInfo_{{index}}">
					<div class="form-group col-4">
						<label for="">接车单号</label> <input type="text" class="form-control" id="pickupNumber_{{index}}" name="pickupNumber" required />
					</div>
					<div class="form-group col-4">
						<label for="">车型</label> <input type="text" class="form-control" id="carTypePublicCode_{{index}}" name="carTypePublicCode"
						 required />
					</div>
					<div class="form-group col-4">
						<label for="">车牌号</label> <input type="text" class="form-control" id="carPlate_{{index}}" name="carPlate" required />
					</div>
					<div class="form-group col-4">
						<label for="">冷库区域：<i class="red">*</i></label>
						<input id="districtId_{{index}}" type="text" class="form-control floatReserve"
						 name="districtId" range="0 9999999" required />
						
					</div>	
					<div class="form-group col-4">
						<label for="">冷库编号：<i class="red">*</i></label>
						<input id="assetsId_{{index}}" type="text" class="form-control"
						 name="assetsId" required />
						
					</div>
					<div class="form-group col-4">
						<label for="quantity" class="">入库件数：<i class="red">*</i></label> <input id="quantity_{{index}}" type="number" class="form-control floatReserve"
						 name="quantity" range="0 9999999" required />
					</div>
					<div class="form-group col-4">
						<label for="weight" class="">货物净重(公斤)：<i class="red">*</i></label> <input id="weight_{{index}}" type="number" class="form-control floatReserve"
						 name="weight" range="0 9999999" required />
					</div>
					<div class="form-group col-4">
						<label for="weight" class="">入库金额：<i class="red">*</i></label> <input id="amount_{{index}}" type="number" class="form-control floatReserve money"
						 name="amount" range="0 9999999" required />
					</div>
					<div class="form-group col-8">
					    <label for="notes">备注</label>
					    <textarea id="notes" class="form-control" name="notes" rows="1" maxlength="100"></textarea>
					</div>
					<div class="form-group col-4">
					    <button type="button" class="btn btn-secondary px-5 item-del">删除</button>
					</div>
					
					
				</div>
	</form>			
</script>
<!-- 司磅入库 -->
<script id="detailInfo3" type="text/html">
	<form id="saveForm_{{index}}" role="form" novalidate>
	<div class="row row-cols-12 detail" id="detailInfo_{{index}}">
					<div class="form-group col-4">
						<label for="">接车单号</label> <input type="text" class="form-control" id="pickupNumber_{{index}}" name="pickupNumber" required />
					</div>
					<div class="form-group col-4">
						<label for="">车型</label> <input type="text" class="form-control" id="carTypePublicCode_{{index}}" name="carTypePublicCode"
						 required />
					</div>
					<div class="form-group col-4">
						<label for="">车牌号</label> <input type="text" class="form-control" id="carPlate_{{index}}" name="carPlate" required />
					</div>
					<div class="form-group col-4">
						<label for="">冷库区域：<i class="red">*</i></label>
						<select id="districtId_{{index}}" name="districtId" class="form-control"
						 required></select>
						
					</div>
					<div class="form-group col-4">
						<label for="">冷库编号：<i class="red">*</i></label>
						<input id="assetsId_{{index}}" type="text" class="form-control"
						 name="assetsId" required />
						
					</div>
					<div class="form-group col-4">
						<label for="" class="">入库件数：<i class="red">*</i></label> <input id="quantity_{{index}}" type="number" class="form-control floatReserve"
						 name="quantity" range="0 9999999" required />
					</div>
					<div class="form-group col-4">
						<label for="" class="">货物净重(公斤)：<i class="red">*</i></label> <input id="weight_{{index}}" type="number" class="form-control floatReserve"
						 name="weight" range="0 9999999" required />
						 <button type="button" class="btn btn-secondary px-5" onclick="openWeightHandler()">连接地磅</button>
					</div>
					<div class="form-group col-4">
						<label for="" class="">入库金额：<i class="red">*</i></label> <input id="amount_{{index}}" type="number" class="form-control floatReserve money"
						 name="amount" range="0 9999999" required />
					</div>
					<div class="form-group col-8">
					    <label for="">备注</label>
					    <textarea id="notes_{{index}}" class="form-control" name="notes" rows="1" maxlength="100"></textarea>
					</div>
					<div class="form-group col-4">
					    <button type="button" class="btn btn-secondary px-5 item-del">删除</button>
					</div>
					<#bcomboProvider _id="districtId_{{index}}" _provider="districtProvider" _queryParams="{departmentId:54}" />					
				</div>
	</form>			
</script>
<!-- 零散入库 -->
<script id="detailInfo2" type="text/html">
	<form id="saveForm_{{index}}" role="form" novalidate>
	<div class="row row-cols-12 detail" id="detailInfo_{{index}}">
					<div class="form-group col-4">
						<label for="">冷库区域：<i class="red">*</i></label>
						<select id="districtId_{{index}}" name="districtId" class="form-control"
						 required></select>
						
					</div>
					<div class="form-group col-4">
						<label for="">冷库编号：<i class="red">*</i></label>
						<input id="assetsId_{{index}}" type="text" class="form-control"
						 name="assetsId" required />
						
					</div>
					<div class="form-group col-4">
						<label for="quantity" class="">入库件数：<i class="red">*</i></label> <input id="quantity_{{index}}" type="number" class="form-control floatReserve"
						 name="quantity" range="0 9999999" required />
					</div>
					<div class="form-group col-4">
						<label for="weight" class="">货物净重(公斤)：<i class="red">*</i></label> <input id="weight_{{index}}" type="number" class="form-control floatReserve"
						 name="weight" range="0 9999999" required />
					</div>
					<div class="form-group col-4">
						<label for="weight" class="">入库金额：<i class="red">*</i></label> <input id="amount_{{index}}" type="number" class="form-control floatReserve money"
						 name="amount" range="0 9999999" required />
					</div>
					<div class="form-group col-8">
					    <label for="notes">备注</label>
					    <textarea id="notes_{{index}}" class="form-control" name="notes" rows="1" maxlength="100"></textarea>
					</div>
					<div class="form-group col-4">
					    <button type="button" class="btn btn-secondary px-5 item-del">删除</button>
					</div>
				</div>
	</form>			
</script>

<script id="weighman" type="text/html">
	<form id="weighman" role="form" novalidate>
	<div class="row">
				<div class="form-group row col-8">
					<div class="form-group col-12">
						<label for="_certificateNumber">称台重量(公斤)：<i class="red">*</i></label>
						<input id="weight" type="number" class="form-control floatReserve"
						 name="weight" range="0 9999999" required readonly/>
						<button>写入重量</button>
					</div>
					<div class="form-group col-6">
						<label for="_certificateNumber">毛重(公斤)：<i class="red">*</i></label>
						<input id="grossWeight" type="number" class="form-control floatReserve"
						 name="grossWeight" range="0 9999999" required readonly/>
						
					</div>	
					<div class="form-group col-6">
						<label for="_certificateNumber">毛重时间：<i class="red">*</i></label>
						<input id="grossWeightDate" type="number" class="form-control floatReserve"
						 name="grossWeightDate" required readonly/>
						
					</div>
					<div class="form-group col-6">
						<label for="tareWeight" class="">皮重(公斤)：<i class="red">*</i></label> <input id="quantity" type="number" class="form-control floatReserve"
						 name="tareWeight" range="0 9999999" required readonly />
					</div>
					<div class="form-group col-6">
						<label for="tareWeightDate" class="">皮重时间：<i class="red">*</i></label> <input id="quantity" type="number" class="form-control floatReserve"
						 name="tareWeightDate" required readonly />
					</div>
				</div>
				<div class="form-group col-4">
					
					pic
						
				</div>
				</div>
	</form>			
</script>
