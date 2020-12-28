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
		url: '/stock/stockOut/listPage.action'
	});
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


function codeFormatter(value,row,index) {
    return '<a href="javascript:openViewHandler(\''+row.code+'\')">'+value+'</a>';
}

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
	       content: "${contextPath}/stock/stockOut/view.html?code=" + code,
	       isIframe : true,
	       closeBtn: true,
	       backdrop : 'static',
	       width: '95%',
	       height : '95%',
	       btns: []
	   });
	}

/** 票据打印处理器 */
function printHandler() {
	 let rows = _grid.bootstrapTable('getSelections');
     if (null == rows || rows.length == 0) {
         bs4pop.alert('请选中一条数据');
         return;
     }
    let code = rows[0].code;
    window.printFinish = function() {}
    $.ajax({
        type:"POST",
        url:"/api/stockIn/queryPrintData/stock_out?orderCode="+code,
        dataType:"json",
      
        success:function(result) {
            if (result.code === '200') {
                callbackObj.boothPrintPreview(JSON.stringify(result.data.item), result.data.name, 0);
            }
        },
        error:function() {

        }
    });
}

/*****************************************函数区 end**************************************/

/*****************************************自定义事件区 begin************************************/





/*****************************************自定义事件区 end**************************************/
</script>
