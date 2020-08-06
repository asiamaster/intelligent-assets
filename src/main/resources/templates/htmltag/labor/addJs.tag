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
    function strIsNotEmpty(str){
    	return str!=null&&str!=""&&str!=undefined
    }
    // 提交保存
    function saveOrUpdateHandler(){
    	let validator = $('#saveForm').validate({ignore:''})
        if (!validator.form()) {
            $('.breadcrumb [data-toggle="collapse"]').html('收起 <i class="fa fa-angle-double-up" aria-hidden="true"></i>');
            $('.collapse:not(.show)').addClass('show');
            return false;
        }
    	let labor = buildFormData();
    	
    	let url = "";
    	if(strIsNotEmpty(labor.code)){
    		url = "${contextPath}/labor/vest/update.action"
    	}else{
    		url = "${contextPath}/labor/vest/insert.action"
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
                bui.loading.hide();
                if(!ret.success){
                    bs4pop.alert(ret.message, {type: 'error'});
                }else{
                	bs4pop.alert(ret.message, {type: 'success'});
    				parent.$('#grid').bootstrapTable('refresh');
                }
            },
            error: function (error) {
                bui.loading.hide();
                bs4pop.alert('远程访问失败', {type: 'error'});
            }
        });
    }



$(document).on('change', '#interval', function() {
	changeEndDay($("#interval").val(),$('#startDate').val());
});

$(document).on('change', '.chargeItem', function() {
	let total = 0;
	$('#saveForm').find('.chargeItem').each(function(){
		total = parseInt(total) + parseInt($(this).val());
	})
	$('#amount').val(total);
});


function changeEndDay(interval,date){
	if(interval == 0){
		$('#endDate').val(null);
		return;
	}
	$('#endDate').val(moment(date).add('month', interval).format('YYYY-MM-DD'));
	
}

laydate.render({
	elem: '#startDate',
	theme: '#007bff',
	trigger: 'click',
	done: function(value, date){
		//监听日期被切换
		changeEndDay($("#interval").val(),value);
	}
});

$(function () {
	let type = '${type!}';
	
	if(type == "add"){
		$('#startDate').val(moment().format("YYYY-MM-DD"));
		$('#endDate').val(moment().add('month', 1).format('YYYY-MM-DD'));
	}
	if(type == "renew"){
		//续费  默认老单结束时间为新单开始时间
		let startDate = moment("${labor.endDate!}").add('day', 1)
		$('#startDate').val(startDate.format("YYYY-MM-DD"));
		$('#endDate').val(startDate.add('month', 1).format('YYYY-MM-DD'));
		$(".chargeItem").removeAttr("readonly");

	}
	if(type != "update" && type != "add"){
		//根据操作类型判断可修改数据
		$('#code').attr("name",type+"Code");
		$('#actionType').val(type);
		$('#saveForm').find('input').each(function(){
			if(!$(this).hasClass(type)){
				$(this).attr("readonly","readonly");
				//$(this).attr("disabled","disabled");
			}
		})
		$('#saveForm').find('select').each(function(){
			if(!$(this).hasClass(type)){
				$(this).attr("disabled","disabled");
			}
		})
		if(type == "rename"){
			$('#customerName').removeAttr("readonly");
		}
		$(".chargeItem").removeAttr("readonly");
	}
});


</script>