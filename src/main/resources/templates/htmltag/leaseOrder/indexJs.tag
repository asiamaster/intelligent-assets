<script>
    /**
     *
     * @Date 2019-11-06 17:30:00
     * @author jiangchengyong
     *
     ***/

    /*********************变量定义区 begin*************/
        //行索引计数器
        //如 let itemIndex = 0;
    let _grid = $('#grid');
    let _form = $('#_form');
    let _modal = $('#_modal');
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
        _grid.bootstrapTable('refreshOptions', {url: '/leaseOrder/listPage.action',pageSize: parseInt(size)});
    });


    /******************************驱动执行区 end****************************/

    /*****************************************函数区 begin************************************/
    /**
     * 打开新增窗口
     */
    function openInsertHandler() {
       dia = bs4pop.dialog({
            title: '新增摊位出租',
            content: '/leaseOrder/preSave.html',
            isIframe : true,
            closeBtn: true,
            backdrop : 'static',
            width: '95%',
            height : '95%',
            btns: []
        });
    }

    /**
     * 打开修改Handler
     */
    function openUpdateHandler() {
        //获取选中行的数据
        let rows = _grid.bootstrapTable('getSelections');
        if (null == rows || rows.length == 0) {
            bs4pop.alert('请选中一条数据');
            return;
        }

        dia = bs4pop.dialog({
            title: '修改摊位出租',
            content: '/leaseOrder/preSave.html?id='+rows[0].id,
            isIframe : true,
            closeBtn: true,
            backdrop : 'static',
            width: '95%',
            height : '95%',
            btns: []
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
            title: '摊位租赁详情',
            content: '/leaseOrder/view.action?id='+id,
            isIframe : true,
            closeBtn: true,
            backdrop : 'static',
            width: '95%',
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
     * 打开补录Handler
     */
    function openSupplementHandler() {
        //获取选中行的数据
        let rows = _grid.bootstrapTable('getSelections');
        if (null == rows || rows.length == 0) {
            bs4pop.alert('请选中一条数据');
            return;
        }

        let dia = bs4pop.dialog({
            title: '补录',
            content: template('supplementTpl',{}),
            closeBtn: true,
            backdrop : 'static',
            width: '40%',
            height : '200px',
            btns: [
                {
                    label: '确定', className: 'btn-primary', onClick(e) {
                        if (!$('#supplementForm').valid()) {
                            return false;
                        }
                        bui.loading.show();
                        $.ajax({
                            type: "POST",
                            url: "${contextPath}/leaseOrder/supplement.action",
                            data: {id: rows[0].id,contractNo : $('#contractNo').val()},
                            processData:true,
                            dataType: "json",
                            success : function(data) {
                                bui.loading.hide();
                                if(!data.success){
                                    bs4pop.alert(data.result, {type: 'error'});
                                }
                                dia.hide();
                            },
                            error : function() {
                                bui.loading.hide();
                                bs4pop.alert('远程访问失败', {type: 'error'});
                                dia.hide();
                            }
                        });
                        return false;
                    }
                },
                {label: '取消', className: 'btn-default', onClick(e) {}}
            ]
        });
    }

    /**
     * 打开取消Handler
     */
    function openCancelHandler() {
        //获取选中行的数据
        let rows = _grid.bootstrapTable('getSelections');
        if (null == rows || rows.length == 0) {
            bs4pop.alert('请选中一条数据');
            return;
        }

        let selectedRow = rows[0];
        let dia = bs4pop.confirm('确定取消该业务单？', undefined, function (sure) {
            if(sure){
                bui.loading.show('努力提交中，请稍候。。。');
                $.ajax({
                    type: "POST",
                    url: "${contextPath}/leaseOrder/cancelOrder.action",
                    data: {id: selectedRow.id},
                    dataType: "json",
                    success : function(ret) {
                        bui.loading.hide();
                        if(ret.success){
                            queryDataHandler();
                        }else{
                            bs4pop.alert(ret.message, {type: 'error'});
                        }
                        dia.hide();
                    },
                    error : function() {
                        bui.loading.hide();
                        bs4pop.alert('远程访问失败', {type: 'error'});
                        dia.hide();
                    }
                });
                return false;
            }

        })

    }

    /**
     * 打开撤回Handler
     */
    function openWithdrawHandler() {
        //获取选中行的数据
        let rows = _grid.bootstrapTable('getSelections');
        if (null == rows || rows.length == 0) {
            bs4pop.alert('请选中一条数据');
            return;
        }

        let selectedRow = rows[0];
        let dia = bs4pop.confirm('撤回之后该业务单可继续修改，但不能交费，如需继续交费可以再次提交。确定撤回？', undefined, function (sure) {
            if(sure){
                bui.loading.show('努力提交中，请稍候。。。');
                $.ajax({
                    type: "POST",
                    url: "${contextPath}/leaseOrder/withdrawOrder.action",
                    data: {id: selectedRow.id},
                    dataType: "json",
                    success : function(ret) {
                        bui.loading.hide();
                        if(ret.success){
                            queryDataHandler();
                        }else{
                            bs4pop.alert(ret.message, {type: 'error'});
                        }
                        dia.hide();
                    },
                    error : function() {
                        bui.loading.hide();
                        bs4pop.alert('远程访问失败', {type: 'error'});
                        dia.hide();
                    }
                });
                return false;
            }
        })

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

        let dia = bs4pop.dialog({
            title: '提交付款',
            content: template('submitPaymentTpl', {
                waitAmount: rows[0].waitAmount,
                minAmount: rows[0].$_waitAmount == 0 ? 0 : 0.01
            }),
            closeBtn: true,
            backdrop : 'static',
            width: '550px',
            height : '250px',
            btns: [
                {
                    label: '确定', className: 'btn-primary', onClick(e) {
                        bui.loading.show('努力提交中，请稍候。。。');
                        if (!$('#submitPaymentForm').valid()) {
                            bui.loading.hide();
                            return false;
                        }
                        $.ajax({
                            type: "POST",
                            url: "${contextPath}/leaseOrder/submitPayment.action",
                            data: {
                                id: rows[0].id,
                                waitAmount: rows[0].$_waitAmount,
                                amount: Number($('#amount').val()).mul(100),
                                amountFormatStr:$('#amount').val()
                            },
                            dataType: "json",
                            success : function(data) {
                                bui.loading.hide();
                                queryDataHandler();
                                if(!data.success){
                                    bs4pop.alert(data.result, {type: 'error'});
                                }
                                dia.hide();
                            },
                            error : function() {
                                bui.loading.hide();
                                bs4pop.alert('远程访问失败', {type: 'error'});
                                dia.hide();
                            }
                        });
                        return false;
                    }
                },
                {label: '取消', className: 'btn-default', onClick(e) {}}
            ]
        });
    }

    /**
     * 打开续租Handler
     */
    function openRenewHandler() {
        //获取选中行的数据
        let rows = _grid.bootstrapTable('getSelections');
        if (null == rows || rows.length == 0) {
            bs4pop.alert('请选中一条数据');
            return;
        }

        dia = bs4pop.dialog({
            title: '摊位续租',
            content: '/leaseOrder/renew.html?id='+rows[0].id,
            isIframe : true,
            closeBtn: true,
            backdrop : 'static',
            width: '95%',
            height : '95%',
            btns: []
        });
    }

    /**
     * 摊位订单停租
     * @returns {boolean}
     */
    function openStopRentHandler(subTableIndex) {
        //获取选中行的数据
        let rows = $('#subGrid'+subTableIndex).bootstrapTable('getSelections');
        if (null == rows || rows.length == 0) {
            bs4pop.alert('请选中一条数据');
            return;
        }

        let leaseOrder = _grid.bootstrapTable('getRowByUniqueId',rows[0].leaseOrderId);

        let dia = bs4pop.dialog({
            title: '停租',
            content: bui.util.HTMLDecode(template('stopRentTpl',$.extend({},rows[0],{endTime : leaseOrder.endTime}))),
            closeBtn: true,
            backdrop : 'static',
            width: '40%',
            height : '78%',
            onShowEnd(){
                let now = moment(new Date()).format("YYYY-MM-DD");
                let minDate = moment(now).isBefore(leaseOrder.startTime)?leaseOrder.startTime : now;
                laydate.render({
                        elem: '#stopTime',
                        type: 'date',
                        theme: '#007bff',
                        min : minDate,
                        done: function(value, date){
                            $("#stopRentForm").validate().element($("#stopTime"));
                        }
                });
                $('#stopWay').on('change',':radio',function () {
                    $('#stopDateSelect').toggle();
                });
                $('#stopRentForm').validate({
                    rules: {
                        stopTime: {
                            required: true,
                            minDate: minDate,
                            maxDate: leaseOrder.endTime
                        }
                    }
                });
            },
            btns: [
                {
                    label: '确定', className: 'btn-primary', onClick(e) {
                        if (!$('#stopRentForm').valid()) {
                            return false;
                        }
                        bui.loading.show();
                        $.ajax({
                            type: "POST",
                            url: "${contextPath}/leaseOrderItem/stopRent.action",
                            data: $('#stopRentForm').serializeObject(),
                            dataType: "json",
                            success : function(data) {
                                bui.loading.hide();
                                queryDataHandler();
                                if(!data.success){
                                    bs4pop.alert(data.result, {type: 'error'});
                                }
                                dia.hide();
                            },
                            error : function() {
                                bui.loading.hide();
                                bs4pop.alert('远程访问失败', {type: 'error'});
                                dia.hide();
                            }
                        });
                        return false;
                    }
                },
                {label: '取消', className: 'btn-default', onClick(e) {}}
            ]
        });
    }

    /**
     * 打开退款申请Handler
     * @param type 1：租赁单退款 2： 子单退款
     * @param subTableIndex
     */
    function openRefundApplyHandler(type,subTableIndex) {
        //获取选中行的数据
        let rows;
        if(type == 1){
            rows = _grid.bootstrapTable('getSelections');
        }else if(type == 2){
            rows = $('#subGrid' + subTableIndex).bootstrapTable('getSelections');
        }
        if (null == rows || rows.length == 0) {
            bs4pop.alert('请选中一条数据');
            return;
        }
        let url = '/leaseOrder/refundApply.html?id=' + rows[0].id + '&type=' + type;
        dia = bs4pop.dialog({
            title: '退款申请',
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
     * 查询处理
     */
    function queryDataHandler() {
        currentSelectRowIndex = undefined;
        $('#toolbar button').attr('disabled', false);
        _grid.bootstrapTable('refresh');
    }

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
     * table参数组装
     * 可修改queryParams向服务器发送其余的参数
     * @param params
     */
    function subQueryParams(params) {
        let temp = {
            rows: params.limit,   //页面大小
            page: ((params.offset / params.limit) + 1) || 1, //页码
            sort: params.sort,
            order: params.order
        }
        return $.extend(temp, bui.util.bindMetadata(this.id));
    }

    /**
     * 关闭弹窗
     */
    function closeDialog(dialog){
        dialog.hide();
        queryDataHandler();
    }

    /*****************************************函数区 end**************************************/

    /*****************************************自定义事件区 begin************************************/
    //表单弹框关闭事件
    // _modal.on('hidden.bs.modal', function () {
    //     _form[0].reset();
    //     //重置表单验证到初始状态
    //     $(this).find('input,select,textarea').removeClass('is-invalid is-valid');
    //     $(this).find('input,select,textarea').removeAttr('disabled readonly');
    //     $(this).find('.invalid-feedback').css('display','none');
    // });

    //展开事件
    _grid.on('expand-row.bs.table', function (e,index, row, $detail){
        //展开选中行
        // if(currentSelectRowIndex != index){
        //     _grid.bootstrapTable('collapseRow', currentSelectRowIndex);
        //     currentSelectRowIndex = index;
        //     _grid.bootstrapTable('check',index);
        // }


        var cur_table = $detail.html(template('subTable',{index})).find('table');
        $(cur_table).bootstrapTable();
        $(cur_table).bootstrapTable('refreshOptions', {url: '/leaseOrderItem/listPage.action?leaseOrderId='+row.id});
        //选中行事件
        $(cur_table).on('check.bs.table', function (e,row, $element){
            e.stopPropagation();
            let state = row.$_state;
            //（未生效 || 已生效）&& 已交清方可停租
            if ((state == ${@com.dili.ia.glossary.LeaseOrderStateEnum.NOT_ACTIVE.getCode()}
                || state == ${@com.dili.ia.glossary.LeaseOrderStateEnum.EFFECTIVE.getCode()})
                && row.payState == ${@com.dili.ia.glossary.PayStateEnum.PAID.getCode()}) {
                $('#toolbar'+index+' button').attr('disabled', true);
                $('#btn_stop_rent'+index).attr('disabled', false);
            } else if (state == ${@com.dili.ia.glossary.LeaseOrderStateEnum.RENTED_OUT.getCode()}
            || state == ${@com.dili.ia.glossary.LeaseOrderStateEnum.EXPIRED.getCode()}) {
                // 退款条件
                // 1.已到期或已停租 AND 2.未发起过退款 AND 3.保证金未被冻结 4.订单费用已交清
                $('#toolbar'+index+' button').attr('disabled', true);
                if(row.refundState == ${@com.dili.ia.glossary.RefundStateEnum.WAIT_APPLY.getCode()}
                    && row.depositAmountFlag != ${@com.dili.ia.glossary.DepositAmountFlagEnum.FROZEN.getCode()}
                    && row.payState == ${@com.dili.ia.glossary.PayStateEnum.PAID.getCode()}){
                    $('#btn_refund_apply'+index).attr('disabled', false);
                }
            } else{
                $('#toolbar'+index+' button').attr('disabled', true);
            }
        });
    });

    //选中行事件
    _grid.on('uncheck.bs.table', function (e, row, $element) {
        currentSelectRowIndex = undefined;
    });

    //选中行事件
    _grid.on('check.bs.table', function (e, row, $element) {
        // let newIndex = $element.parents('tr').data('index');
        // if (currentSelectRowIndex !== newIndex) {
        //     _grid.bootstrapTable('collapseRow', currentSelectRowIndex);
        //     _grid.bootstrapTable('expandRow', newIndex);
        //     currentSelectRowIndex = newIndex;
        // }

        let state = row.$_state;
        if (state == ${@com.dili.ia.glossary.LeaseOrderStateEnum.CREATED.getCode()}) {
            $('#toolbar button').attr('disabled', true);
            $('#btn_view').attr('disabled', false);
            $('#btn_add').attr('disabled', false);
            $('#btn_edit').attr('disabled', false);
            $('#btn_cancel').attr('disabled', false);
            $('#btn_submit').attr('disabled', false);
        } else if (state == ${@com.dili.ia.glossary.LeaseOrderStateEnum.CANCELD.getCode()}) {
            $('#toolbar button').attr('disabled', true);
            $('#btn_view').attr('disabled', false);
            $('#btn_add').attr('disabled', false);
        } else if (state == ${@com.dili.ia.glossary.LeaseOrderStateEnum.SUBMITTED.getCode()}) {
            $('#toolbar button').attr('disabled', true);
            $('#btn_view').attr('disabled', false);
            $('#btn_add').attr('disabled', false);
            if(row.$_payState == ${@com.dili.ia.glossary.PayStateEnum.NOT_PAID.getCode()}){
                $('#btn_submit').attr('disabled', false);
            }
            $('#btn_withdraw').attr('disabled', false);
        } else if (state == ${@com.dili.ia.glossary.LeaseOrderStateEnum.NOT_ACTIVE.getCode()}
            || state == ${@com.dili.ia.glossary.LeaseOrderStateEnum.EFFECTIVE.getCode()}) {
            $('#toolbar button').attr('disabled', true);
            $('#btn_view').attr('disabled', false);
            $('#btn_add').attr('disabled', false);
            $('#btn_supplement').attr('disabled', false);
            $('#btn_renew').attr('disabled', false);

            if(row.$_payState == ${@com.dili.ia.glossary.PayStateEnum.NOT_PAID.getCode()}){
                $('#btn_submit').attr('disabled', false);
            }
            //未交清且未发起过退款申请
            if (row.$_payState == ${@com.dili.ia.glossary.PayStateEnum.NOT_PAID.getCode()} && row.refundState == ${@com.dili.ia.glossary.RefundStateEnum.WAIT_APPLY.getCode()}) {
                $('#btn_refund_apply').attr('disabled', false);
            }
        } else if (state == ${@com.dili.ia.glossary.LeaseOrderStateEnum.RENTED_OUT.getCode()}) {
            $('#toolbar button').attr('disabled', true);
            $('#btn_view').attr('disabled', false);
            $('#btn_add').attr('disabled', false);
            $('#btn_supplement').attr('disabled', false);
        } else if (state == ${@com.dili.ia.glossary.LeaseOrderStateEnum.REFUNDED.getCode()}) {
            $('#toolbar button').attr('disabled', true);
            $('#btn_view').attr('disabled', false);
            $('#btn_add').attr('disabled', false);
            $('#btn_supplement').attr('disabled', false);
        } else if (state == ${@com.dili.ia.glossary.LeaseOrderStateEnum.EXPIRED.getCode()}) {
            $('#toolbar button').attr('disabled', true);
            $('#btn_view').attr('disabled', false);
            $('#btn_add').attr('disabled', false);
            $('#btn_renew').attr('disabled', false);
            $('#btn_supplement').attr('disabled', false);
            //未交清且未发起过退款申请
            if (row.$_payState == ${@com.dili.ia.glossary.PayStateEnum.NOT_PAID.getCode()} && row.refundState == ${@com.dili.ia.glossary.RefundStateEnum.WAIT_APPLY.getCode()}) {
                $('#btn_refund_apply').attr('disabled', false);
            }
        }
    });
    /*****************************************自定义事件区 end**************************************/
</script>