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
        _grid.bootstrapTable('refreshOptions', {url: '/refundOrder/listPage.action',pageSize: parseInt(size)});
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



    //选中行事件
    _grid.on('uncheck.bs.table', function (e, row, $element) {
        currentSelectRowIndex = undefined;
    });

    //选中行事件 -- 可操作按钮控制
    _grid.on('check.bs.table', function (e, row, $element) {
        let state = row.$_state;
        if (state == ${@com.dili.ia.glossary.RefundOrderStateEnum.CREATED.getCode()}) {
            $('#toolbar button').attr('disabled', true);
            $('#btn_view').attr('disabled', false);
            $('#btn_add').attr('disabled', false);
            $('#btn_update').attr('disabled', false);
            $('#btn_cancel').attr('disabled', false);
            $('#btn_submit').attr('disabled', false);
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
    });
</script>

<!--
http://ia.diligrp.com:8381/refundOrder/index.html 列表
http://ia.diligrp.com:8381/refundOrder/view.html 查看
http://ia.diligrp.com:8381/refundOrder/update.html 修改
-->
