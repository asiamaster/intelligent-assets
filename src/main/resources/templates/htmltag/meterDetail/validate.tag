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

</script>