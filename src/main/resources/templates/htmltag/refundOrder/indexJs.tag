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
        _grid.bootstrapTable('refreshOptions', {pageNumber: 1, url: '/refundOrder/listPage.action',pageSize: parseInt(size)});
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
            title: '退款详情',//对话框title
            content: '${contextPath}/refundOrder/view.action?id='+id, //对话框内容，可以是 string、element，$object
            width: '80%',//宽度
            height: '95%',//高度
            isIframe: true,//默认是页面层，非iframe
            btns: [{label: '关闭', className: 'btn-secondary', onClick(e) {}}]
        });
    }

    /**
     * 打开查看
     * @param id
     */
    function openUpdateHandler(id) {
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
            title: '修改退款单',//对话框title
            content: '${contextPath}/refundOrder/update.html?id='+id, //对话框内容，可以是 string、element，$object
            width: '80%',//宽度
            height: '95%',//高度
            isIframe: true,//默认是页面层，非iframe
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
            bs4pop.confirm('提交之后信息不可更改，并可在结算中心办理退款，确定提交？', {}, function (sure) {
                if(sure){
                    bui.util.debounce(function () {
                        bui.loading.show('努力提交中，请稍候。。。');
                        //获取选中行的数据
                        let rows = _grid.bootstrapTable('getSelections');
                        let selectedRow = rows[0];

                        $.ajax({
                            type: "POST",
                            url: "${contextPath}/refundOrder/submit.action",
                            data: {id: selectedRow.id},
                            processData:true,
                            dataType: "json",
                            success : function(data) {
                                bui.loading.hide();
                                if(data.success){
                                    _grid.bootstrapTable('refresh');
                                }else{
                                    bs4pop.alert(data.message, {type: 'error'});
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
            bs4pop.confirm('撤回之后该业务单可继续修改，但不能退款，如需继续退款可以再次提交。确定撤回？', {}, function (sure) {
                if(sure){
                    bui.loading.show('努力提交中，请稍候。。。');
                    //获取选中行的数据
                    let rows = _grid.bootstrapTable('getSelections');
                    let selectedRow = rows[0];

                    $.ajax({
                        type: "POST",
                        url: "${contextPath}/refundOrder/withdraw.action",
                        data: {id: selectedRow.id},
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
    }
    /**
     取消
     */
    function openCancelHandler() {
        if(isSelectRow()){
            bs4pop.confirm('取消后该单据不可用，确定取消？', {}, function (sure) {
                if(sure){
                    bui.loading.show('努力提交中，请稍候。。。');
                    //获取选中行的数据
                    let rows = _grid.bootstrapTable('getSelections');
                    let selectedRow = rows[0];

                    $.ajax({
                        type: "POST",
                        url: "${contextPath}/refundOrder/cancel.action",
                        data: {id: selectedRow.id},
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
    }

    /**
     *  显示流程图
     */
    function showProgress() {
        //获取选中行的数据
        let rows = _grid.bootstrapTable('getSelections');
        if (null == rows || rows.length == 0) {
            bs4pop.alert('请选中一条数据');
            return;
        }
        let selected = rows[0];
        //var href = '<#config name="bpmc.server.address"/>/api/runtime/progress?processInstanceId='+selected.processInstanceId+'&processDefinitionId='+selected.processDefinitionId+"&"+Math.random();
        //$("#processInstanceImg").attr("src", href);
        let url = '<#config name="bpmc.server.address"/>/api/runtime/progress?processInstanceId='+selected.processInstanceId+'&processDefinitionId='+selected.processDefinitionId+"&"+Math.random();
        dia = bs4pop.dialog({
            title: '流程图',
            content: url,
            isIframe : true,
            closeBtn: true,
            backdrop : 'static',
            width: '95%',
            height : '95%',
            btns: []
        });
    }

    /**
     *  提交审批
     */
    function submitRefundOrderApplication(){
        //获取选中行的数据
        let rows = _grid.bootstrapTable('getSelections');
        if (null == rows || rows.length == 0) {
            bs4pop.alert('请选中一条数据');
            return;
        }
        let selectedRow = rows[0];
        bs4pop.confirm('确定提交审批？', undefined, function (sure) {
            if(sure){
                bui.loading.show('努力提交中，请稍候。。。');
                $.ajax({
                    type: "POST",
                    url: "${contextPath}/refundOrder/submitForApproval.action",
                    data: {id: selectedRow.id},
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

    //选中行事件
    _grid.on('uncheck.bs.table', function (e, row, $element) {
        currentSelectRowIndex = undefined;
    });

    //选中行事件 -- 可操作按钮控制
    _grid.on('check.bs.table', function (e, row, $element) {
        let state = row.$_state;
        //审批状态
        let approvalState = row.$_approvalState;
        if (state == ${@com.dili.ia.glossary.RefundOrderStateEnum.CREATED.getCode()}) {
            $('#toolbar button').attr('disabled', true);
            $('#btn_view').attr('disabled', false);
            $('#btn_add').attr('disabled', false);
            $('#btn_edit').attr('disabled', false);

            <%//摊位租赁退款单需要提交审批
            if(bizType=="1"){%>
            //没有审批状态可以 提交审批，修改和取消
            if(!approvalState){
                $('#btn_approval').attr('disabled', false);
                $('#btn_edit').attr('disabled', false);
                $('#btn_cancel').attr('disabled', false);
                return;
            }
            //待审批时可以 提交审批，修改和取消
            if(approvalState == ${@com.dili.ia.glossary.ApprovalStateEnum.WAIT_SUBMIT_APPROVAL.getCode()}){
                $('#btn_approval').attr('disabled', false);
                $('#btn_edit').attr('disabled', false);
                $('#btn_cancel').attr('disabled', false);
            }
            //审批中不允许修改、取消和提交付款
            //审批通过后直接提交退款
            //审批拒绝后，可以再次提交审批，修改和取消
            else if(approvalState == ${@com.dili.ia.glossary.ApprovalStateEnum.APPROVAL_DENIED.getCode()}){
                $('#btn_approval').attr('disabled', false);
                $('#btn_edit').attr('disabled', false);
                $('#btn_cancel').attr('disabled', false);
            }
            <%}else{%>
                $('#btn_edit').attr('disabled', false);
                $('#btn_cancel').attr('disabled', false);
            <%}%>
        } else if (state == ${@com.dili.ia.glossary.RefundOrderStateEnum.CANCELD.getCode()}) {
            $('#toolbar button').attr('disabled', true);
            $('#btn_view').attr('disabled', false);
            $('#btn_add').attr('disabled', false);
        } else if (state == ${@com.dili.ia.glossary.RefundOrderStateEnum.SUBMITTED.getCode()}) {
            $('#toolbar button').attr('disabled', true);
            $('#btn_view').attr('disabled', false);
            $('#btn_add').attr('disabled', false);
            $('#btn_withdraw').attr('disabled', false);
        } else if (state == ${@com.dili.ia.glossary.RefundOrderStateEnum.REFUNDED.getCode()}) {
            $('#toolbar button').attr('disabled', true);
            $('#btn_view').attr('disabled', false);
            $('#btn_add').attr('disabled', false);
        }
        //只能有流程实例id就可以查看流程图
        if(row.processInstanceId) {
            $("#btn_showProgress").attr('disabled', false);
        }
    });
</script>

<!--
http://ia.diligrp.com:8381/refundOrder/index.html 列表
http://ia.diligrp.com:8381/refundOrder/view.html 查看
http://ia.diligrp.com:8381/refundOrder/update.html 修改
-->
