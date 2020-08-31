<script>

    //初始化刷卡
    initSwipeCard({
        id:'getCustomer',
    });

    function buildFormData(){
    	$('#saveForm').find('select').each(function(){
			$(this).removeAttr("disabled"); 
		})
        // let formData = new FormData($('#saveForm')[0]);
        let formData = $("input:not(table input),textarea,select").serializeObject();
        let departmentName = $('#departmentId').find("option:selected").text();
    	formData.departmentName = departmentName;
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
        return formData;
    }
    function strIsEmpty(str){
    	return str==null||str==""||str==undefined
    }
    // 提交保存
    function saveOrUpdateHandler(){
    	let validator = $('#saveForm').validate({ignore:''})
        if (!validator.form()) {
            return;
        }
    	let labor = buildFormData();
    	
    	let url = "";
    	if(strIsEmpty(labor.code)){
    		url = "${contextPath}/fee/message/insert.action"
    	}else{
    		url = "${contextPath}/fee/message/update.action"
    	}
        bui.loading.show('努力提交中，请稍候。。。');
        // let _formData = new FormData($('#saveForm')[0]);
        $.ajax({
            type: "POST",
            url: url,
            data: JSON.stringify(labor),
    		dataType: "json",
    		contentType: "application/json",
            success: function (ret) {
                if(!ret.success){
                    bs4pop.alert(ret.message, {type: 'error'});
                }else{
                	parent.closeDialog(parent.dia);
    				parent.$('#grid').bootstrapTable('refresh');	
                }
                bui.loading.hide();
            },
            error: function (error) {
                bui.loading.hide();
                bs4pop.alert('远程访问失败', {type: 'error'});
            }
        });
    }

$(document).on('change', '.chargeItem', function() {
	count();
});
function count(){
	let total = 0;
	$('#saveForm').find('.chargeItem').each(function(){
		total = parseInt(total) + parseInt($(this).val());
	})
	$('#amount').val(total);
	if(total > 0 ){
		$('#payAmount').val(parseInt(total)-parseInt($("#transactionAmount").val()))
	}
}
$(document).on('change', '#transactionAmount', function() {
	count();
});

//计算应收取费用
$(document).on('change',".get-cost", function() {
	getCost();
});

function getCost(){
	let detail = {};
	detail.laborType=$("[name=laborType]").find("option:selected").val();
	detail.models=$("[name=models]").find("option:selected").val();
	detail.businessChargeType=$("[name=businessChargeType]").val();
	// 时间
	detail.startDate=$('#startDate').val();
	detail.endDate=$('#endDate').val();
	if(strIsEmpty(detail.laborType) || strIsEmpty(detail.models) || strIsEmpty(detail.startDate) || strIsEmpty(detail.endDate)){
		return;
	}
	// 动态收费项
	let itemBusinessChargeDtos = []
	$('.chargeItem').each(function(){
		let itemBusinessCharge = {};
		itemBusinessCharge.chargeItemId=$(this).attr("name").split("_")[1];
		itemBusinessChargeDtos.push(itemBusinessCharge);
	})
	detail.businessChargeItems=itemBusinessChargeDtos;
	$.ajax({
		type: "POST",
		url: "${contextPath}/fee/message/getCost.action",
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
					let obj = $("[name=chargeItem_"+item.chargeItem+"]");
					obj.val(item.totalFee);
					count();
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
}

</script>