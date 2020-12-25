<script>

    /*********************变量定义区 begin*************/
        //行索引计数器
        //如 let itemIndex = 0;
    let _grid = $('#grid');
    let _form = $('#_form');
    let currentSelectRowIndex;
    var dia;

    /*********************变量定义区 end***************/

    /******************************驱动执行区 begin***************************/
    $(function () {
        $(window).resize(function () {
            _grid.bootstrapTable('resetView')
        });
        let size = ($(window).height() - $('#queryForm').height() - 210) / 40;
        size = size > 10 ? size : 10;
        _grid.bootstrapTable('refreshOptions', {url: '${contextPath}/assetsRental/listPage.action', pageSize: parseInt(size)});
    });

    /******************************驱动执行区 end****************************/

    /**
     * table参数组装
     * 可修改queryParams向服务器发送其余的参数
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
     * 查询处理
     */
    function queryDataHandler() {
        currentSelectRowIndex = undefined;
        $('#toolbar button').attr('disabled', false);
        _grid.bootstrapTable('refresh');
    }

    /**
     打开新增窗口
     */
    function openInsertHandler() {
        dia = bs4pop.dialog({
            title: '新增摊位出租预设',//对话框title
            content: '/assetsRental/add.html', //对话框内容，可以是 string、element，$object
            width: '800px',//宽度
            height: '680px',//高度
            isIframe: true,//默认是页面层，非iframe
        });
    }

    /**
     打开修改窗口
     */
    function openUpdateHandler() {
        //获取选中行的数据
        let rows = _grid.bootstrapTable('getSelections');
        if (null == rows || rows.length == 0) {
            bs4pop.alert('请选中一条数据');
            return;
        }

        dia = bs4pop.dialog({
            title: '修改摊位出租预设',//对话框title
            content: '${contextPath}/assetsRental/update.html?id='+rows[0].id, //对话框内容，可以是 string、element，$object
            width: '800px',//宽度
            height: '680px',//高度
            isIframe: true,//默认是页面层，非iframe
        });
    }

    /**
     启用
     */
    function openEnableHandler() {
        if(isSelectRow()){
            bs4pop.confirm('确定启用？', {}, function (sure) {
                if(sure){
                    bui.loading.show('努力提交中，请稍候。。。');
                    //获取选中行的数据
                    let rows = _grid.bootstrapTable('getSelections');
                    let selectedRow = rows[0];

                    $.ajax({
                        type: "POST",
                        url: "${contextPath}/assetsRental/enableOrDisable.action",
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
     启用或者禁用
     */
    function openDisableHandler() {
        if(isSelectRow()){
            bs4pop.confirm('确定禁用？', {}, function (sure) {
                if(sure){
                    bui.loading.show('努力提交中，请稍候。。。');
                    //获取选中行的数据
                    let rows = _grid.bootstrapTable('getSelections');
                    let selectedRow = rows[0];

                    $.ajax({
                        type: "POST",
                        url: "${contextPath}/assetsRental/enableOrDisable.action",
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


    //选中行事件
    _grid.on('uncheck.bs.table', function (e, row, $element) {
        currentSelectRowIndex = undefined;
    });

    //选中行事件 -- 可操作按钮控制
    _grid.on('check.bs.table', function (e, row, $element) {
        let state = row.$_state;
        let payState = row.$_payState;
        let refundState = row.$_refundState;
        let isRelated = row.isRelated;
        $('#toolbar button').attr('disabled', true);
        $('#btn_add').attr('disabled', false);
        $('#btn_update').attr('disabled', false);
        if (state == ${@com.dili.ia.glossary.AssetsRentalStateEnum.ENABLE.getCode()}){
            $('#btn_disable').attr('disabled', false);
        }  else if (state == ${@com.dili.ia.glossary.AssetsRentalStateEnum.DISABLE.getCode()}) {
            $('#btn_enable').attr('disabled', false);
        }
    });

</script>
