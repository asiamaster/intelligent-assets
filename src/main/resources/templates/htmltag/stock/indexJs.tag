<script>

/*********************变量定义区 begin*************/
//行索引计数器
//如 let itemIndex = 0;
let _grid = $('#grid');
let _form = $('#_form');
let _modal = $('#_modal');
var dia;
let duration = 2;
let timeUnit = "day"
$("#createdStart").attr("value", moment().subtract(duration, timeUnit).startOf('day').format('YYYY-MM-DD HH:mm:ss'));
$("#createdEnd").attr("value", moment().endOf('day').format('YYYY-MM-DD HH:mm:ss'));
/*********************变量定义区 end***************/


/******************************驱动执行区 begin***************************/
$(function () {
	$(window).resize(function () {
		_grid.bootstrapTable('resetView')
	});
	queryDataHandler();
});
/******************************驱动执行区 end****************************/

/*****************************************函数区 begin************************************/

/**
 * 查询处理
 */
function queryDataHandler() {
	_grid.bootstrapTable('refreshOptions', {pageNumber: 1, url: '/stock/stockIn/listPage.action'});
}

function doExport(){
	bui.util.doExport("grid", "queryForm");
}

/**
 * 业务编号formatter
 * @param value
 * @param row
 * @param index
 * @returns {string}
 */
function codeFormatter(value,row,index) {
    return '<a href="javascript:openViewHandler(\''+row.code+'\')">'+value+'</a>';
}

/**
 * table参数组装
 * 可修改queryParams向服务器发送其余的参数
 * 前置 table初始化时 queryParams方法拿不到option对象，需要在页面加载时初始化option
 * @param params
 */
function queryParams(params) {
	let temp = {
			rows: params.limit,   //页面大小
			page: ((params.offset / params.limit) + 1) || 1, //页码
			sort: params.sort,
			order: params.order
	}
	return $.extend(temp, bui.util.bindGridMeta2Form('grid', 'queryForm'));
}


/**
 * 关闭弹窗
 */
function closeDialog(dialog){
    dialog.hide();
    queryDataHandler();
}

/**
 * 打开新增窗口
 */
function openAddHandler(type,title) {
   dia = bs4pop.dialog({
        title: title,
        content: '${contextPath}/stock/stockIn/add.html?type=' +type,
        isIframe : true,
        closeBtn: true,
        backdrop : 'static',
        width: '95%',
        height : '95%',
        btns: [{label: '取消',className: 'btn-secondary',onClick(e, $iframe){

        }
	    }, {label: '确定',className: 'btn-primary',onClick(e, $iframe){
	            let diaWindow = $iframe[0].contentWindow;
	            bui.util.debounce(diaWindow.doAddStockInHandler,1000,true)()
	            return false;
	        }
	    }]
    });
}

/**
     打开查看窗口
 */
function openViewHandler(code) {
	if(!code){
        //获取选中行的数据
        let rows = _grid.bootstrapTable('getSelections');
        if (null == rows || rows.length == 0) {
            bs4pop.alert('请选中一条数据');
            return;
        }
        code = rows[0].code;
    }
	   dia = bs4pop.dialog({
	        title: "查看",
	        content: "${contextPath}/stock/stockIn/view.html?code="+code,
	        isIframe : true,
	        closeBtn: true,
	        backdrop : 'static',
	        width: '95%',
	        height : '95%',
	        btns: []
	    });
	}


function openUpdateHandler() {
	let rows = _grid.bootstrapTable('getSelections');
	if (null == rows || rows.length == 0) {
		bs4pop.alert('请选中一条数据');
		return false;
	}
	   dia = bs4pop.dialog({
	        title: "修改",
	        content: "${contextPath}/stock/stockIn/update.html?code="+rows[0].code,
	        isIframe : true,
	        closeBtn: true,
	        backdrop : 'static',
	        width: '95%',
	        height : '95%',
	        btns: [{label: '取消',className: 'btn-secondary',onClick(e, $iframe){

	        }
		    }, {label: '确定',className: 'btn-primary',onClick(e, $iframe){
		            let diaWindow = $iframe[0].contentWindow;
		            bui.util.debounce(diaWindow.doUpdateStockInHandler,1000,true)()			
		            return false;
		        }
		    }]
	    });
	}


/**
 取消
 */
function openCancelHandler() {
	if(isSelectRow()){
		bs4pop.confirm('确定取消该业务单？', {}, function (sure) {
			if(sure){
				bui.loading.show('努力提交中，请稍候。。。');
				//获取选中行的数据
				let rows = _grid.bootstrapTable('getSelections');
				let selectedRow = rows[0];
				$.ajax({
					type: "POST",
					url: "${contextPath}/stock/stockIn/cancel.action",
					data: {code: selectedRow.code},
					processData:true,
					dataType: "json",
					success : function(ret) {
						bui.loading.hide();
						if(ret.success){
							bs4pop.alert(ret.message, {type: 'success'});
							queryDataHandler();
						}else{
							bs4pop.alert(ret.message, {type: 'error'});
						}
					},
					error : function() {
						bui.loading.hide();
						bs4pop.alert('远程访问失败', {type: 'error'});
					}
				});
			}
		})
	}
}

/**
 提交处理
 */
function openSubmitHandler() {
	if(isSelectRow()){
		bs4pop.confirm('提交后该信息不可更改，并且可进行缴费，确认提交？', {}, function (sure) {
			if(sure){
				bui.util.debounce(function () {
					bui.loading.show('努力提交中，请稍候。。。');
					//获取选中行的数据
					let rows = _grid.bootstrapTable('getSelections');
					let selectedRow = rows[0];
					$.ajax({
						type: "POST",
						url: "${contextPath}/stock/stockIn/submit.action",
						data: {code: selectedRow.code},
						processData:true,
						dataType: "json",
						success : function(ret) {
							bui.loading.hide();
							if(ret.success){
								bs4pop.alert(ret.message, {type: 'success'});
								queryDataHandler();
							}else{
								bs4pop.alert(ret.message, {type: 'error'});
							}
						},
						error : function() {
							bui.loading.hide();
							bs4pop.alert('远程访问失败', {type: 'error'});
						}
					});
				},1000,true)();
			}
		})
	}
}

/**
 撤回处理
 */
function openWithdrawHandler() {
	if(isSelectRow()){
		bs4pop.confirm('撤回之后该业务单可继续修改，但不能交费，如需继续交费可以再次提交。确定撤回？', {}, function (sure) {
			if(sure){
				bui.util.debounce(function () {
					bui.loading.show('努力提交中，请稍候。。。');
					//获取选中行的数据
					let rows = _grid.bootstrapTable('getSelections');
					let selectedRow = rows[0];
					$.ajax({
						type: "POST",
						url: "${contextPath}/stock/stockIn/remove.action",
						data: {code: selectedRow.code},
						processData:true,
						dataType: "json",
						success : function(ret) {
							bui.loading.hide();
							if(ret.success){
								bs4pop.alert(ret.message, {type: 'success'});
								queryDataHandler();
							}else{
								bs4pop.alert(ret.message, {type: 'error'});
							}
						},
						error : function() {
							bui.loading.hide();
							bs4pop.alert('远程访问失败', {type: 'error'});
						}
					});
				},1000,true)();
			}
		})
	}
}


/**
 * 打开支付窗口
 */
function openPayHandler() {
	if(isSelectRow()){
		//获取选中行的数据
		let rows = _grid.bootstrapTable('getSelections');
		let selectedRow = rows[0];
		dia = bs4pop.dialog({
			title: '支付',//对话框title
			content: bui.util.HTMLDecode(template('pay', {selectedRow})), //对话框内容，可以是 string、element，$object
			width: '50%',//宽度
			height: '70%',//高度
			btns: [{label: '取消',className: 'btn btn-secondary',onClick(e){

			}
			}, {label: '确定',className: 'btn btn-primary',onClick(e){
				bui.loading.show('努力提交中，请稍候。。。');
				let formData = $('#payForm').serializeObject();
				$.ajax({
					type: "POST",
					url: "${contextPath}/stock/stockIn/pay.action",
					data: formData,
					processData:true,
					dataType: "json",
					success : function(ret) {
						bui.loading.hide();
						if(ret.success){
							queryDataHandler();
						}else{
							bs4pop.alert(ret.message, {type: 'error'});
						}
					},
					error : function() {
						bui.loading.hide();
						bs4pop.alert('远程访问失败', {type: 'error'});
					}
				});    
				/*bui.util.debounce(pay(formData),1000,true)()*/
			}
			}]
		});
	}
}


/**
 * 打开退款窗口
 */
function openRefundHandler() {
	if(isSelectRow()){
		//获取选中行的数据
		let rows = _grid.bootstrapTable('getSelections');
		let selectedRow = rows[0];

		dia = bs4pop.dialog({
			title: '退款申请',//对话框title
			content: bui.util.HTMLDecode(template('refund', {selectedRow})), //对话框内容，可以是 string、element，$object
			width: '50%',//宽度
			height: '70%',//高度
			btns: [{label: '取消',className: 'btn btn-secondary',onClick(e){
			}
			}, {label: '确定',className: 'btn btn-primary',onClick(e){
				bui.loading.show('努力提交中，请稍候。。。');
				let formData = $('#refundForm').serializeObject();
				bui.util.yuanToCentForMoneyEl(formData);

				$.ajax({
					type: "POST",
					url: "${contextPath}/stock/stockIn/refund.action",
					data: formData,
					processData:true,
					dataType: "json",
					success : function(ret) {
						bui.loading.hide();
						if(ret.success){
							bs4pop.alert(ret.message, {type: 'success'});
							queryDataHandler();
						}else{
							bs4pop.alert(ret.message, {type: 'error'});
						}
					},
					error : function() {
						bui.loading.hide();
						bs4pop.alert('远程访问失败', {type: 'error'});
					}
				});    
			}
			}]
		});
	}
}

function openRefundHandler(){
	if(isSelectRow()){
		let rows = _grid.bootstrapTable('getSelections');
        let selectedRow = rows[0];
		dia = bs4pop.dialog({
            title: '退款申请',//对话框title
            content: '${contextPath}/stock/stockIn/refundApply.html?code='+selectedRow.code, //对话框内容，可以是 string、element，$object
            width: '80%',//宽度
            height: '95%',//高度
            isIframe: true,//默认是页面层，非iframe
            btns: [{label: '取消',className: 'btn-secondary',onClick(e, $iframe){

                }
            }, {label: '确定',className: 'btn-primary',onClick(e, $iframe){
                    let diaWindow = $iframe[0].contentWindow;
                    bui.util.debounce(diaWindow.saveFormHandler,1000,true)()
                    return false;
                }
            }]
        });
	} 
}



//选中行事件
_grid.on('uncheck.bs.table', function (e, row, $element) {
    currentSelectRowIndex = undefined;
});

//选中行事件 -- 可操作按钮控制
_grid.on('check.bs.table', function (e, row, $element) {
    let state = row.$_state;
    
    if (state == ${@com.dili.ia.glossary.StockInStateEnum.CREATED.getCode()}) {
        $('#toolbar button').attr('disabled', true);
        $('.btn_update').attr('disabled', false);
        $('.btn_cancel').attr('disabled', false);
        $('.btn_submit').attr('disabled', false);
       
    } else if (state == ${@com.dili.ia.glossary.StockInStateEnum.CANCELLED.getCode()}) {
        $('#toolbar button').attr('disabled', true);
    } else if (state == ${@com.dili.ia.glossary.StockInStateEnum.SUBMITTED_PAY.getCode()}) {
        $('#toolbar button').attr('disabled', true);
        $('.btn_withdraw').attr('disabled', false);
    } else if (state == ${@com.dili.ia.glossary.StockInStateEnum.PAID.getCode()}) {
        $('#toolbar button').attr('disabled', true);
        $('.btn_refund').attr('disabled', false);
    }  else if (state == ${@com.dili.ia.glossary.StockInStateEnum.SUBMITTED_REFUND.getCode()}) {
        $('#toolbar button').attr('disabled', true);
    }else if (state == ${@com.dili.ia.glossary.StockInStateEnum.REFUNDED.getCode()}) {
        $('#toolbar button').attr('disabled', true);
    }
    $('.btn_add').attr('disabled', false);
    $('.btn_view').attr('disabled', false);
    $('.btn_export').attr('disabled', false);
});

/*****************************************函数区 end**************************************/

/*****************************************自定义事件区 begin************************************/





/*****************************************自定义事件区 end**************************************/
</script>