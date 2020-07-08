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
$(function() {
	$(window).resize(function() {
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
	_grid.bootstrapTable('refreshOptions', {
		pageNumber: 1,
		url: '/stock/stock/listPage.action'
	});
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
		rows: params.limit, //页面大小
		page: ((params.offset / params.limit) + 1) || 1, //页码
		sort: params.sort,
		order: params.order
	}
	return $.extend(temp, bui.util.bindGridMeta2Form('grid', 'queryForm'));
}


/**
查看入库详情
 */
/*function openStockInListHandler() {
	//获取选中行的数据
	let rows = _grid.bootstrapTable('getSelections');
	if (null == rows || rows.length == 0) {
		bs4pop.alert('请选中一条数据');
		return false;
	}
	window.location.href = "${contextPath}/stock/stock/inList.html?assetsId=" + rows[0].assetsId +"&customerId=" + rows[0].customerId +"&categoryId="+ rows[0].categoryId;
}*/
function openStockInListHandler() {
	let rows = _grid.bootstrapTable('getSelections');
	if (null == rows || rows.length == 0) {
		bs4pop.alert('请选中一条数据');
		return false;
	}
	  dia = bs4pop.dialog({
	       title: "查看",
	       content: "${contextPath}/stock/stock/inList.html?assetsId=" + rows[0].assetsId +"&customerId=" + rows[0].customerId +"&categoryId="+ rows[0].categoryId,
	       isIframe : true,
	       closeBtn: true,
	       backdrop : 'static',
	       width: '95%',
	       height : '95%',
	       btns: []
	   });
	}
/**
查看出库详情
 */
/*function openStockOutListHandler() {
	//获取选中行的数据
	let rows = _grid.bootstrapTable('getSelections');
	if (null == rows || rows.length == 0) {
		bs4pop.alert('请选中一条数据');
		return false;
	}
	window.location.href = "${contextPath}/stock/stock/outList.html?assetsId=" + rows[0].assetsId +"&customerId=" + rows[0].customerId +"&categoryId="+ rows[0].categoryId;
}*/
function openStockOutListHandler() {
	let rows = _grid.bootstrapTable('getSelections');
	if (null == rows || rows.length == 0) {
		bs4pop.alert('请选中一条数据');
		return false;
	}
	  dia = bs4pop.dialog({
	       title: "查看",
	       content: "${contextPath}/stock/stock/outList.html?assetsId=" + rows[0].assetsId +"&customerId=" + rows[0].customerId +"&categoryId="+ rows[0].categoryId,
	       isIframe : true,
	       closeBtn: true,
	       backdrop : 'static',
	       width: '95%',
	       height : '95%',
	       btns: []
	   });
	}

/**
 * 打开新增窗口:页面层
 */
function openStockOutHandler() {
	if (isSelectRow()) {
		let rows = _grid.bootstrapTable('getSelections');
		let selectedRow = rows[0];
		dia = bs4pop.dialog({
			title: '出库办理', //对话框title
			content: bui.util.HTMLDecode(template("stockOut", {
				selectedRow
			})), //对话框内容，可以是 string、element，$object
			width: '30%', //宽度
			height: '50%', //高度
			btns: [{
				label: '取消',
				className: 'btn btn-secondary',
				onClick(e) {

				}
			}, {
				label: '确定',
				className: 'btn btn-primary',
				onClick(e) {
					bui.util.debounce(stockOut, 1000, true)()
					//return false;
				}
			}]
		});

	}

}

/**
 提交处理
 */
function stockOut() {
	let formData = $('#saveForm').serializeObject();
	$.ajax({
		type: "POST",
		url: "${contextPath}/stock/stock/stockOut.action",
		data: formData,
		processData: true,
		dataType: "json",
		success: function(ret) {
			bui.loading.hide();
			if (ret.success) {
				bs4pop.alert(ret.message, {type: 'success'});
				queryDataHandler();
			} else {
				bs4pop.alert(ret.message, {
					type: 'error'
				});
			}
		},
		error: function() {
			bui.loading.hide();
			bs4pop.alert('远程访问失败', {
				type: 'error'
			});
		}
	});
}


/*****************************************函数区 end**************************************/

/*****************************************自定义事件区 begin************************************/





/*****************************************自定义事件区 end**************************************/
</script>
