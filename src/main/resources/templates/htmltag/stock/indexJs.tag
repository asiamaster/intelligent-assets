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
    _grid.bootstrapTable('refreshOptions', {pageNumber: 1, url: '/stockIn/listPage.action'});
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
    	window.location.href = "${contextPath}/stockIn/add.html?type="+type;

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
    	window.location.href = "${contextPath}/stockIn/view.html?code="+rows[0].code;

    }
    /**
 打开更新窗口:iframe
 */
function openUpdateHandler() {
    //获取选中行的数据
    let rows = _grid.bootstrapTable('getSelections');
    if (null == rows || rows.length == 0) {
        bs4pop.alert('请选中一条数据');
        return false;
    }
    	window.location.href = "${contextPath}/stockIn/update.html?code="+rows[0].code;
}
    
/**
 * 打开新增窗口:页面层
 */
function openInsertHandler() {
    dia = bs4pop.dialog({
        title: '页面层新增',//对话框title
        content: bui.util.HTMLDecode(template('addForm', {})), //对话框内容，可以是 string、element，$object
        width: '80%',//宽度
        height: '95%',//高度
        btns: [{label: '取消',className: 'btn btn-secondary',onClick(e){

            }
        }, {label: '确定',className: 'btn btn-primary',onClick(e){
                bui.util.debounce(saveOrUpdateHandler,1000,true)()
                return false;
            }
        }]
    });
}

/**
 打开更新窗口:iframe
 */
function openInsertIframeHandler() {
    dia = bs4pop.dialog({
        title: 'iframe新增',//对话框title
        content: '${contextPath}/customer/preSave.html?', //对话框内容，可以是 string、element，$object
        width: '80%',//宽度
        height: '700px',//高度
        isIframe: true,//默认是页面层，非iframe
        //按钮放在父页面用此处的 btns 选项。也可以放在页面里直接在页面写div。
        /*btns: [{label: '取消',className: 'btn btn-secondary',onClick(e, $iframe){

            }
        }, {label: '确定',className: 'btn btn-primary',onClick(e, $iframe){
                let diaWindow = $iframe[0].contentWindow;
                bui.util.debounce(diaWindow.saveOrUpdateHandler,1000,true)()
                return false;
            }
        }]*/
    });
}





// 提交保存
function saveOrUpdateHandler(){
    let validator = $('#_form').validate({ignore:''})
    debugger
    if (!validator.form()) {
        $('.breadcrumb [data-toggle="collapse"]').html('收起 <i class="fa fa-angle-double-up" aria-hidden="true"></i>');
        $('.collapse:not(.show)').addClass('show');
        return false;
    }

    bui.loading.show('努力提交中，请稍候。。。');
    let _formData = bui.util.removeKeyStartWith(_form.serializeObject(), "_");
    let _url = null;

    //没有id就新增
    if (_formData.id == null || _formData.id == "") {
        _url = "${contextPath}/customer/insert.action";
    } else {//有id就修改
        _url = "${contextPath}/customer/update.action";
    }
    $.ajax({
        type: "POST",
        url: "${contextPath}/customer/update.action",
        data: _formData,
        dataType: "json",
        success: function (ret) {
            bui.loading.hide();
            if(!ret.success){
                bs4pop.alert(ret.message, {type: 'error'});
            }else{
                parent.closeDialog(parent.dia);
            }
        },
        error: function (error) {
            bui.loading.hide();
            bs4pop.alert('远程访问失败', {type: 'error'});
        }
    });
}



/**
 * 禁启用操作
 * @param enable 是否启用:true-启用
 */
function doEnableHandler(enable) {
    //获取选中行的数据
    let rows = _grid.bootstrapTable('getSelections');
    if (null == rows || rows.length == 0) {
        bs4pop.alert('请选中一条数据');
        return;
    }

    //table选择模式是单选时可用
    let selectedRow = rows[0];
    let msg = (enable || 'true' == enable) ? '确定要启用该货站吗？' : '确定要禁用该货站吗？';

    bs4pop.confirm(msg, undefined, function (sure) {
        if(sure){
            bui.loading.show('努力提交中，请稍候。。。');
            $.ajax({
                type: "POST",
                url: "${contextPath}/customer/doEnable.action",
                data: {id: selectedRow.id, enable: enable},
                processData:true,
                dataType: "json",
                success : function(data) {
                    bui.loading.hide();
                    if(data.success){
                        _grid.bootstrapTable('refresh');
                    }else{
                        bs4pop.alert(data.result, {type: 'error'});
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
                        url: "${contextPath}/customer/submit.action",
                        data: {id: selectedRow.id},
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
                },1000,true)();
            }
        })
    }
}

/**
 撤回
 */
function openWithdrawHandler() {
    if(isSelectRow()){
        bs4pop.confirm('撤回之后该业务单可继续修改，但不能交费，如需继续交费可以再次提交。确定撤回？', {}, function (sure) {
            if(sure){
                bui.loading.show('努力提交中，请稍候。。。');
                //获取选中行的数据
                let rows = _grid.bootstrapTable('getSelections');
                let selectedRow = rows[0];

                $.ajax({
                    type: "POST",
                    url: "${contextPath}/customer/withdraw.action",
                    data: {id: selectedRow.id},
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
            }
        })
    }
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
                    url: "${contextPath}/earnestOrder/cancel.action",
                    data: {id: selectedRow.id},
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
            }
        })
    }
}





/*****************************************函数区 end**************************************/

/*****************************************自定义事件区 begin************************************/





/*****************************************自定义事件区 end**************************************/
</script>