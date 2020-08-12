<script>
//主表数据验证
let saveForm = {
	    onkeyup: false,
	    rules: {
	    	customerName: {
	            required: true
	        },
	        customerCellphone: {
	        	required: true,
	            maxlength: 20
	        },
	        departmentId: {
	        	required: true,
	        },
	        categoryId: {
	        	required: true,
	        },
	        quantity: {
	        	required: true,
	        },
	        weight: {
	        	required: true,
	        },
	        amount: {
	        	required: true,
	        },
	        stockInDate: {
	        	required: true
	        },
	        expireDate: {
	        	required: true
	        },
	    },
	    messages: {
	    	customerName: {
	            required: "客户必填"
	        },
	        customerCellphone: {
	        	required:"客户联系方式必填",
	            maxlength: "不能超过{0}"
	        },
	        departmentId: {
	        	required: "部门必填",
	        },
	        categoryId: {
	        	required: "品类必填",
	        },
	        quantity: {
	        	required: "数量必填",
	        },
	        weight: {
	        	required: "重量必填",
	        },
	        amount: {
	        	required: "数量必填",
	        },
	        stockInDate: {
	        	required: "入库时间必填"
	        },
	        expireDate: {
	        	required: "过期时间必填"
	        }
	    },
	    focusCleanup: true
};
let validateSaveForm = $("#saveForm").validate(saveForm);
//子表数据验证
let saveFormDetail = {
	    onkeyup: false,
	    rules: {
	    	districtId: {
	            required: true
	        },
	        assetsId: {
	        	required: true,
	        },
	        quantity: {
	        	required: true,
	        	digits:true,
	        },
	        unitWeight: {
	        	required: true,
	        	digits:true,
	        },
	        weight: {
	        	required: true,
	        	digits:true,
	        },
	        amount: {
	        	required: true,
	        },
	        stockInDate: {
	        	required: true,
	        },
	        expireDate: {
	        	required: true
	        },
	        carTypePublicCode:{
	        	required: true
	        }
	    },
	    messages: {
	    	districtId: {
	            required: "区域必填"
	        },
	        assetsId: {
	        	required:"冷库必填",
	        },
	        quantity: {
	        	required: "数量必填",
	        	digits:"只能输入整数"
	        },
	        unitWeight: {
	        	required: "件重必填",
	        	digits:"只能输入整数"
	        },
	        weight: {
	        	required: "重量必填",
	        	digits:"只能输入整数"
	        },
	        amount: {
	        	required: "数量必填",
	        },
	        stockInDate: {
	        	required: "入库时间必填"
	        },
	        expireDate: {
	        	required: "过期时间必填"
	        },
	        carTypePublicCode:{
	        	required: "车型必填"
	        }
	    },
	    focusCleanup: true
	}

function validateForm(){
	let count = 0;
	if(!$("#saveForm").validate().form()){
		count++
	}
	for(let i=1;i<=itemIndex;i++){
		if($("#saveForm_"+i).length > 0){
			if(!$("#saveForm_"+i).validate().form()){
				count++
			}  
		}
	}
	return count==0?true:false;
}

</script>