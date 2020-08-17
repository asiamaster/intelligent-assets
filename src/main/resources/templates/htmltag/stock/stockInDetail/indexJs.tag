<script>

/*********************变量定义区 begin*************/
//行索引计数器
//如 let itemIndex = 0;
let _grid = $('#grid');
let _form = $('#_form');
let _modal = $('#_modal');
var dia;
let duration = 3;
let timeUnit = "day"
$(".laystart").attr("value", moment().subtract(duration, timeUnit).startOf('day').format('YYYY-MM-DD HH:mm:ss'));
$(".layend").attr("value", moment().endOf('day').format('YYYY-MM-DD HH:mm:ss'));
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
		url: '/stock/stockInDetail/listPage.action'
	});
}


/**
 * table参数组装
 * 可修改queryParams向服务器发送其余的参数
 * 前置 table初始化时 queryParams方法拿不到option对象，需要在页面加载时初始化option
 * @param params
 */
function strIsNotEmpty(str){
	return str!=null&&str!=""&&str!=undefined
}
function queryParams(params) {
	let temp = {
		rows: params.limit, //页面大小
		page: ((params.offset / params.limit) + 1) || 1, //页码
		sort: params.sort,
		order: params.order
	}
	/*if(strIsNotEmpty($("#expireDate").val())){
		  let day1 = new Date();
		  day1.setDate(day1.getDate() + $("#expireDate").val());
		  console.log(day1.getFullYear()+'-'+(day1.getMonth()+1)+'-'+day1.getDate());
		temp = $.extend(temp,{expireDate:'2020-07-12'});
	}*/
	return $.extend(temp, bui.util.bindGridMeta2Form('grid', 'queryForm'));
}


function codeFormatter(value,row,index) {
    return '<a href="javascript:openViewHandler(\''+row.code+'\')">'+value+'</a>';
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
       content: "${contextPath}/stock/stockInDetail/view.html?code=" + code,
       isIframe : true,
       closeBtn: true,
       backdrop : 'static',
       width: '95%',
       height : '95%',
       btns: []
   });
}



function doExport(){
	bui.util.doExport("grid", "queryForm");
}



/*****************************************函数区 end**************************************/

/*****************************************自定义事件区 begin************************************/





/*****************************************自定义事件区 end**************************************/
</script>
