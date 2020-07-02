<script>

/*********************变量定义区 begin*************/
//行索引计数器
//如 let itemIndex = 0;
let _grid = $('#grid');
let _form = $('#_form');
let _modal = $('#_modal');
var dia;
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

//品类搜索
//品类搜索自动完成
var categoryAutoCompleteOption = {
		width: '100%',
		language: 'zh-CN',
		maximumSelectionLength: 10,
		ajax: {
			type: 'post',
			url: '/category/search.action',
			data: function(params) {
				return {
					keyword: params.term,
				}
			},
			processResults: function(result) {
				if (result.success) {
					let data = result.data;
					return {
						results: $.map(data, function(dataItem) {
							dataItem.text = dataItem.name + (dataItem.cusName ? '(' + dataItem.cusName + ')' : '');
							return dataItem;
						})
					};
				} else {
					bs4pop.alert(result.message, {
						type: 'error'
					});
					return;
				}
			}
		}
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
     打开新增窗口
 */
function openAddHandler(type) {
	window.location.href = "${contextPath}/stock/stockIn/add.html?type="+type;

}
/**
     打开查看窗口
 */
function openViewHandler(type) {
	//获取选中行的数据
	let rows = _grid.bootstrapTable('getSelections');
	if (null == rows || rows.length == 0) {
		bs4pop.alert('请选中一条数据');
		return false;
	}
	window.location.href = "${contextPath}/stock/stockIn/view.html?code="+rows[0].code;

}
/**
 * 修改
 */
function openUpdateHandler() {
	//获取选中行的数据
	let rows = _grid.bootstrapTable('getSelections');
	if (null == rows || rows.length == 0) {
		bs4pop.alert('请选中一条数据');
		return false;
	}
	window.location.href = "${contextPath}/stock/stockIn/update.html?code="+rows[0].code;
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


/*****************************************函数区 end**************************************/

/*****************************************自定义事件区 begin************************************/





/*****************************************自定义事件区 end**************************************/
</script>