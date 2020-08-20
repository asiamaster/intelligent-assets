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
        _grid.bootstrapTable('refreshOptions', {pageNumber: 1, url: '${contextPath}/depositOrder/listPage.action', pageSize: parseInt(size)});
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
        _grid.bootstrapTable('refreshOptions', {pageNumber: 1});
    }

    /**
     * 关闭弹窗
     */
    function closeDialog(dialog){
        dialog.hide();
        queryDataHandler();
    }

    /**
     打开新增窗口
     */
    function openInsertHandler() {
        dia = bs4pop.dialog({
            title: '新增保证金',//对话框title
            content: '${contextPath}/depositOrder/add.html', //对话框内容，可以是 string、element，$object
            width: '800px',//宽度
            height: '680px',//高度
            isIframe: true,//默认是页面层，非iframe
        });
    }

    /**
     打开新增窗口
     */
    function openUpdateHandler() {
        //获取选中行的数据
        let rows = _grid.bootstrapTable('getSelections');
        if (null == rows || rows.length == 0) {
            bs4pop.alert('请选中一条数据');
            return;
        }

        dia = bs4pop.dialog({
            title: '修改保证金',//对话框title
            content: '${contextPath}/depositOrder/update.html?id='+rows[0].id, //对话框内容，可以是 string、element，$object
            width: '800px',//宽度
            height: '680px',//高度
            isIframe: true,//默认是页面层，非iframe
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
            title: '保证金详情',
            content: '/depositOrder/view.action?id='+id,
            isIframe : true,
            closeBtn: true,
            backdrop : 'static',
            width: '800px',
            height : '600px',
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
     * 打开提交付款Handler
     */
    function openSubmitPaymentHandler() {
        //获取选中行的数据
        let rows = _grid.bootstrapTable('getSelections');
        if (null == rows || rows.length == 0) {
            bs4pop.alert('请选中一条数据');
            return;
        }

        dia = bs4pop.dialog({
            title: '提交付款',
            content: template('submitPaymentTpl', {
                waitAmount: rows[0].waitAmount,
                minAmount: rows[0].$_waitAmount == 0 ? 0 : 0.01
            }),
            closeBtn: true,
            backdrop : 'static',
            width: '550px',
            btns: [
                {
                    label: '确定', className: 'btn-primary', onClick(e) {
                        bui.util.debounce(function () {
                            if (!$('#submitPaymentForm').valid()) {
                                return false;
                            }
                            bui.loading.show('努力提交中，请稍候。。。');
                            $.ajax({
                                type: "POST",
                                url: "${contextPath}/depositOrder/submit.action",
                                data: {
                                    id: rows[0].id,
                                    waitAmount: rows[0].$_waitAmount,
                                    amount: Number($('#amount').val()).mul(100)
                                },
                                dataType: "json",
                                success : function(data) {
                                    closeDialog(dia)
                                    bui.loading.hide();
                                    queryDataHandler();
                                    if(!data.success){
                                        bs4pop.alert(data.result, {type: 'error'});
                                    }
                                },
                                error : function() {
                                    closeDialog(dia)
                                    bui.loading.hide();
                                    bs4pop.alert('远程访问失败', {type: 'error'});
                                }
                            });
                        },1000,true)();
                        return false;
                    }
                },
                {label: '取消', className: 'btn-default', onClick(e) {}}
            ]
        });
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
                        url: "${contextPath}/depositOrder/withdraw.action",
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
                        url: "${contextPath}/depositOrder/cancel.action",
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
     打开退款窗口
     */
    function openRefundApplyHandler() {
        //获取选中行的数据
        let rows = _grid.bootstrapTable('getSelections');
        if (null == rows || rows.length == 0) {
            bs4pop.alert('请选中一条数据');
            return;
        }

        dia = bs4pop.dialog({
            title: '退款',//对话框title
            content: '${contextPath}/depositOrder/refundApply.html?depositOrderId='+rows[0].id, //对话框内容，可以是 string、element，$object
            width: '95%',//宽度
            height: 750,//高度
            isIframe : true,//默认是页面层，非iframe
        });

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
        $('#btn_view').attr('disabled', false);
        if (state == ${@com.dili.ia.glossary.DepositOrderStateEnum.CREATED.getCode()} && isRelated == ${@com.dili.commons.glossary.YesOrNoEnum.NO.getCode()}) {
            $('#btn_update').attr('disabled', false);
            $('#btn_cancel').attr('disabled', false);
            $('#btn_submit').attr('disabled', false);
        }  else if (state == ${@com.dili.ia.glossary.DepositOrderStateEnum.SUBMITTED.getCode()} && isRelated == ${@com.dili.commons.glossary.YesOrNoEnum.NO.getCode()}) {
            $('#btn_withdraw').attr('disabled', false);
            $('#btn_submit').attr('disabled', false);
        } else if (state == ${@com.dili.ia.glossary.DepositOrderStateEnum.PAID.getCode()}) {
            $('#btn_refund_apply').attr('disabled', false);
            if (payState == ${@com.dili.ia.glossary.DepositPayStateEnum.NOT_PAID.getCode()} && isRelated == ${@com.dili.commons.glossary.YesOrNoEnum.NO.getCode()}){
                $('#btn_submit').attr('disabled', false);
            }
        } else if (state == ${@com.dili.ia.glossary.DepositOrderStateEnum.REFUND.getCode()} && refundState == ${@com.dili.ia.glossary.DepositRefundStateEnum.PART_REFUND.getCode()}) {
            $('#btn_refund_apply').attr('disabled', false);
        }
    });

</script>

<!--
http://ia.diligrp.com:8381/depositOrder/view.html 查看
http://ia.diligrp.com:8381/depositOrder/add.html 新增
http://ia.diligrp.com:8381/depositOrder/update.html 修改
-->
