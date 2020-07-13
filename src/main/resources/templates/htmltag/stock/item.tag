
<!-- 入库明细 -->
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
						<input id="districtId_{{index}}" type="text" class="form-control"
						 name="districtId"  required />
					</div>	
					<div class="form-group col-4">
						<label for="">冷库编号：<i class="red">*</i></label>
						<input id="assetsId_{{index}}" type="text" class="form-control"
						 name="assetsId" required />
						
					</div>
					<div class="form-group col-4">
						<label for="quantity" class="">入库件数：<i class="red">*</i></label> <input id="quantity_{{index}}" type="number" class="form-control number_change get-cost"
						 name="quantity" range="0 9999999" required />
					</div>
					<div class="form-group col-4">
						<label for="weight" class="">货物净重(公斤)：<i class="red">*</i></label> <input id="weight_{{index}}" type="number" class="form-control number_change"
						 name="weight" range="0 9999999" required />
					</div>
					<div class="form-group col-4">
						<label for="weight" class="">总金额：<i class="red">*</i></label> <input id="amount_{{index}}" type="number" class="form-control number_change money"
						 name="amount" range="0 9999999" required readonly />
					</div>
					<%if(isNotEmpty(chargeItems) && chargeItems.~size>0){
				        for(item in chargeItems){
				        %>
				        <div class="form-group col-4">
							<label for="amount" class="">${item.chargeItem!}<i class="red">*</i></label> <input id="chargeItem_${item.id!}_{{index}}" type="number" class="form-control amount-item number_change chargeItem money"
							 name="chargeItem_${item.id!}" chargeItem="${item.chargeItem!}" range="0 9999999.99" required  />
						</div>
				        <% } }%>
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
						<label for="" class="">入库件数：<i class="red">*</i></label> <input id="quantity_{{index}}" type="number" class="form-control number_change get-cost"
						 name="quantity" range="0 9999999" required />
					</div>
					<div class="form-group col-4">
						<label for="" class="">货物净重(公斤)：<i class="red">*</i></label> <input id="weight_{{index}}" type="number" class="form-control number_change"
						 name="weight" range="0 9999999" required  readonly/>
						 <button type="button" class="btn btn-secondary px-5" onclick="openWeightHandler({{index}})">连接地磅</button>
					</div>
					<div class="form-group col-4">
						<label for="" class="">总金额：<i class="red">*</i></label> <input id="amount_{{index}}" type="number" class="form-control number_change money"
						 name="amount" range="0 9999999" required readonly/>
					</div>
					<%if(isNotEmpty(chargeItems) && chargeItems.~size>0){
				        for(item in chargeItems){
				        %>
				        <div class="form-group col-4">
						<label for="amount" class="">${item.chargeItem!}<i class="red">*</i></label> <input id="chargeItem_${item.id!}_{{index}}" type="number" class="form-control amount-item number_change chargeItem money"
						 name="chargeItem_${item.id!}" chargeItem="${item.chargeItem!}" range="0 9999999.99" required  />
					</div>
				        <% } }%>
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

						 <input id="districtId_{{index}}" type="text" class="form-control"
							 name="districtId" required />
					</div>
					<div class="form-group col-4">
						<label for="">冷库编号：<i class="red">*</i></label>
						<input id="assetsId_{{index}}" type="text" class="form-control"
						 name="assetsId" required />
						
					</div>
					<div class="form-group col-4">
						<label for="quantity" class="">入库件数：<i class="red">*</i></label> <input id="quantity_{{index}}" type="number" class="form-control number_change get-cost"
						 name="quantity" range="0 9999999" required />
					</div>
					<div class="form-group col-4">
						<label for="weight" class="">货物净重(公斤)：<i class="red">*</i></label> <input id="weight_{{index}}" type="number" class="form-control number_change"
						 name="weight" range="0 9999999" required />
					</div>
					<div class="form-group col-4">
						<label for="weight" class="">总金额：<i class="red">*</i></label> <input id="amount_{{index}}" type="number" class="form-control number_change money"
						 name="amount" range="0 9999999" required readonly/>
					</div>
					<%if(isNotEmpty(chargeItems) && chargeItems.~size>0){
				        for(item in chargeItems){
				        %>
				        <div class="form-group col-4">
						<label for="amount" class="">${item.chargeItem!}<i class="red">*</i></label> <input id="chargeItem_${item.id!}_{{index}}" type="number" class="form-control amount-item number_change chargeItem money"
						 name="chargeItem_${item.id!}" chargeItem="${item.chargeItem!}" range="0 9999999.99" required  />
					</div>
				        <% } }%>
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


