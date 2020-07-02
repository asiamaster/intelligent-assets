<script>

    /*********************变量定义区 begin*************/
        //行索引计数器
        //如 let itemIndex = 0;
    let _grid = $('#grid');
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
        _grid.bootstrapTable('refreshOptions', {url: '${contextPath}/meter/listPage.action', pageSize: parseInt(size)});
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
     * 查询处理
     */
    function queryDataHandler() {
        currentSelectRowIndex = undefined;
        $('#toolbar button').attr('disabled', false);
        _grid.bootstrapTable('refresh');
    }

    /**
     * 关闭弹窗
     */
    function closeDialog(dialog){
        dialog.hide();
        queryDataHandler();
    }

    // 提交保存
    function saveOrUpdateHandler(){
        let validator = $('#saveForm').validate({ignore:''})
        debugger
        if (!validator.form()) {
            $('.breadcrumb [data-toggle="collapse"]').html('收起 <i class="fa fa-angle-double-up" aria-hidden="true"></i>');
            $('.collapse:not(.show)').addClass('show');
            return false;
        }

        bui.loading.show('努力提交中，请稍候。。。');
        let _formData = $('#saveForm').serializeObject();
        let _url = null;

        //没有id就新增
        if (_formData.id == null || _formData.id == "") {
            _url = "${contextPath}/meter/insert.action";
        } else {//有id就修改,复制
            _url = "${contextPath}/meter/update.action";
        }
        $.ajax({
            type: "POST",
            url: "${contextPath}/meter/update.action",
            data: _formData,
            dataType: "json",
            success: function (ret) {
                bui.loading.hide();
                if(!ret.success){
                    bs4pop.alert(ret.message, {type: 'error'});
                }else{
                    parent.dia.hide()
                }
            },
            error: function (error) {
                bui.loading.hide();
                bs4pop.alert('远程访问失败', {type: 'error'});
            }
        });
    }



    /**
     打开新增窗口
     */
    function openInsertHandler() {
        dia = bs4pop.dialog({
            title: '新增表信息',//对话框title
            content: '${contextPath}/meter/edit.html', //对话框内容，可以是 string、element，$object
            width: '900',//宽度
            height: '500',//高度
            isIframe: true,//默认是页面层，非iframe
            btns: [{label: '取消',className: 'btn-secondary',onClick(e, $iframe){

                }
            }, {label: '确定',className: 'btn-primary',onClick(e, $iframe){
                    let diaWindow = $iframe[0].contentWindow;
                    bui.util.debounce(diaWindow.saveOrUpdateHandler,1000,true)()
                    return false;
                }
            }]
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
            title: '修改表信息',//对话框title
            content: '${contextPath}/meter/edit.html?id='+rows[0].id, //对话框内容，可以是 string、element，$object
            width: '900',//宽度
            height: '95%',//高度
            isIframe: true,//默认是页面层，非iframe
            btns: [{label: '取消',className: 'btn-secondary',onClick(e, $iframe){

                }
            }, {label: '确定',className: 'btn-primary',onClick(e, $iframe){
                    let diaWindow = $iframe[0].contentWindow;
                    bui.util.debounce(diaWindow.saveOrUpdateHandler,1000,true)()
                    return false;
                }
            }]
        });
    }


    /**
     打开复制窗口
     */
    function openCopyHandler() {
        //获取选中行的数据
        let rows = _grid.bootstrapTable('getSelections');
        if (null == rows || rows.length == 0) {
            bs4pop.alert('请选中一条数据');
            return;
        }
        dia = bs4pop.dialog({
            title: '复制表信息',//对话框title
            content: '${contextPath}/meter/edit.html?id='+rows[0].id, //对话框内容，可以是 string、element，$object
            width: '900',//宽度
            height: '95%',//高度
            isIframe: true,//默认是页面层，非iframe
            btns: [{label: '取消',className: 'btn-secondary',onClick(e, $iframe){

                }
            }, {label: '确定',className: 'btn-primary',onClick(e, $iframe){
                    let diaWindow = $iframe[0].contentWindow;
                    bui.util.debounce(diaWindow.saveOrUpdateHandler,1000,true)()
                    return false;
                }
            }]
        });
    }

    //选中行事件
    _grid.on('uncheck.bs.table', function (e, row, $element) {
        currentSelectRowIndex = undefined;
    });

    //选中行事件 -- 可操作按钮控制
    _grid.on('check.bs.table', function (e, row, $element) {
        let state = row.$_state;
        if (state == ${@com.dili.ia.glossary.EarnestOrderStateEnum.CREATED.getCode()}) {
            $('#toolbar button').attr('disabled', true);
            $('#btn-copy').attr('disabled', false);
            $('#btn_add').attr('disabled', false);
            $('#btn_update').attr('disabled', false);
        } else if (state == ${@com.dili.ia.glossary.EarnestOrderStateEnum.CANCELD.getCode()}) {
            $('#toolbar button').attr('disabled', true);
            $('#btn-copy').attr('disabled', false);
            $('#btn_add').attr('disabled', false);
        } else if (state == ${@com.dili.ia.glossary.EarnestOrderStateEnum.SUBMITTED.getCode()}) {
            $('#toolbar button').attr('disabled', true);
            $('#btn-copy').attr('disabled', false);
            $('#btn_add').attr('disabled', false);
            $('#btn_withdraw').attr('disabled', false);
        } else if (state == ${@com.dili.ia.glossary.EarnestOrderStateEnum.PAID.getCode()}) {
            $('#toolbar button').attr('disabled', true);
            $('#btn-copy').attr('disabled', false);
            $('#btn_add').attr('disabled', false);
        }
    });

</script>
