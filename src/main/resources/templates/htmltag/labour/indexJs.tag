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
        _grid.bootstrapTable('refreshOptions', {url: '${contextPath}/earnestOrder/listPage.action', pageSize: parseInt(size)});
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
            title: '新增马甲办理',//对话框title
            content: '${contextPath}/earnestOrder/add.html', //对话框内容，可以是 string、element，$object
            width: '80%',//宽度
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
     打开更新窗口
     */
    function openUpdateHandler() {
        //获取选中行的数据
        let rows = _grid.bootstrapTable('getSelections');
        if (null == rows || rows.length == 0) {
            bs4pop.alert('请选中一条数据');
            return;
        }

        dia = bs4pop.dialog({
            title: '修改马甲办理',//对话框title
            content: '${contextPath}/labour/update.html?id='+rows[0].id, //对话框内容，可以是 string、element，$object
            width: '80%',//宽度
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
     * 打开查看
     * @param id
     */
    function openViewHandler(id) {
        if(!id){
            //获取选中行的数据
            let rows = _grid.bootstrapTable('getSelections');
            if (null == rows || rows.length == 0) {
                bs4pop.alert('请选中一条数据');
                return;
            }
            id = rows[0].id;
        }
        dia = bs4pop.dialog({
            title: '马甲办理详情',
            content: '/labour/view.action?id='+id,
            isIframe : true,
            closeBtn: true,
            backdrop : 'static',
            width: '80%',
            height : '95%',
            btns: [{label: '关闭', className: 'btn-secondary', onClick(e) {}}]
        });
    }

    /**
     * 业务编号formatter
     * @param value
     * @param row
     * @param index
     * @returns {string}
     */
    function codeFormatter(value,row,index) {
        return '<a href="javascript:openViewHandler('+row.id+')">'+value+'</a>';
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
                            url: "${contextPath}/labour/submit.action",
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
                        url: "${contextPath}/labour/withdraw.action",
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
                        url: "${contextPath}/labour/cancel.action",
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
        if (state == ${@com.dili.ia.glossary.EarnestOrderStateEnum.CREATED.getCode()}) {
            $('#toolbar button').attr('disabled', true);
            $('#btn_view').attr('disabled', false);
            $('#btn_add').attr('disabled', false);
            $('#btn_update').attr('disabled', false);
            $('#btn_cancel').attr('disabled', false);
            $('#btn_submit').attr('disabled', false);
        } else if (state == ${@com.dili.ia.glossary.EarnestOrderStateEnum.CANCELD.getCode()}) {
            $('#toolbar button').attr('disabled', true);
            $('#btn_view').attr('disabled', false);
            $('#btn_add').attr('disabled', false);
        } else if (state == ${@com.dili.ia.glossary.EarnestOrderStateEnum.SUBMITTED.getCode()}) {
            $('#toolbar button').attr('disabled', true);
            $('#btn_view').attr('disabled', false);
            $('#btn_add').attr('disabled', false);
            $('#btn_withdraw').attr('disabled', false);
        } else if (state == ${@com.dili.ia.glossary.EarnestOrderStateEnum.PAID.getCode()}) {
            $('#toolbar button').attr('disabled', true);
            $('#btn_view').attr('disabled', false);
            $('#btn_add').attr('disabled', false);
        }
    });

</script>

<!--
http://ia.diligrp.com:8381/earnestOrder/view.html 查看
http://ia.diligrp.com:8381/earnestOrder/add.html 新增
http://ia.diligrp.com:8381/earnestOrder/update.html 修改
-->
