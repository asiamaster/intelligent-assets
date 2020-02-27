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
        queryDataHandler();
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
            width: '80%',
            height : '95%',
            btns: []
        });
    }

    function closeModal() {
        parent.dia.hide();
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
            width: '80%',
            height : '95%',
            btns: []
        });
    }

    /**
     * 打开查看Handler
     */
    function openViewHandler() {
        //获取选中行的数据
        let rows = _grid.bootstrapTable('getSelections');
        if (null == rows || rows.length == 0) {
            bs4pop.alert('请选中一条数据');
            return;
        }

        dia = bs4pop.dialog({
            title: '摊位租赁详情',
            content: '/leaseOrder/view.html?id='+rows[0].id,
            isIframe : true,
            closeBtn: true,
            backdrop : 'static',
            width: '80%',
            height : '95%',
            btns: []
        });
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

        bs4pop.dialog({
            title: '摊位租赁详情',
            content: template('supplementTpl',{}),
            closeBtn: true,
            backdrop : 'static',
            width: '40%',
            height : '60%',
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
                            async : false,
                            success : function(data) {
                                bui.loading.hide();
                                if(!data.success){
                                    bs4pop.alert(data.result, {type: 'error'});
                                }
                            },
                            error : function() {
                                bui.loading.hide();
                                bs4pop.alert('远程访问失败', {type: 'error'});
                            }
                        });
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
        bs4pop.confirm('确定取消该业务单？', undefined, function (sure) {
            if(sure){
                bui.loading.show('努力提交中，请稍候。。。');
                $.ajax({
                    type: "POST",
                    url: "${contextPath}/leaseOrder/cancelOrder.action",
                    data: {id: selectedRow.id},
                    dataType: "json",
                    async:false,
                    success : function(ret) {
                        bui.loading.hide();
                        if(ret.success){
                            _grid.bootstrapTable('refresh');
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
                    url: "${contextPath}/leaseOrder/doEnable.action",
                    data: {id: selectedRow.id, enable: enable},
                    processData:true,
                    dataType: "json",
                    async : true,
                    success : function(data) {
                        bui.loading.hide();
                        if(data.success){
                            _grid.bootstrapTable('refresh');
                            _modal.modal('hide');
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
     *  保存及更新表单数据
     */
    function saveOrUpdateHandler() {
        if (_form.validate().form() != true) {
            return;
        }

        bui.loading.show('努力提交中，请稍候。。。');
        let _formData = bui.util.removeKeyStartWith(_form.serializeObject(), "_");
        let _url = null;
        //没有id就新增
        if (_formData.id == null || _formData.id == "") {
            _url = "${contextPath}/leaseOrder/insert.action";
        } else {//有id就修改
            _url = "${contextPath}/leaseOrder/update.action";
        }
        $.ajax({
            type: "POST",
            url: _url,
            data: _formData,
            processData: true,
            dataType: "json",
            async: true,
            success: function (data) {
                bui.loading.hide();
                if (data.code == "200") {
                    _grid.bootstrapTable('refresh');
                    _modal.modal('hide');
                } else {
                    bs4pop.alert(data.result, {type: 'error'});
                }
            },
            error: function (a, b, c) {
                bui.loading.hide();
                bs4pop.alert('远程访问失败', {type: 'error'});
            }
        });
    }


    /**
     * 查询处理
     */
    function queryDataHandler() {
        currentSelectRowIndex = undefined;
        $('#toolbar button').attr('disabled', false);
        _grid.bootstrapTable('refreshOptions', {url: '/leaseOrder/listPage.action'});
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

    function moneyFormatter(val) {
        return
    }

    /**
     * 关闭弹窗
     */
    function closeDialog(dialog){
        dialog.hide();
        _grid.bootstrapTable('refresh');
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
            if (state == ${@com.dili.ia.glossary.LeaseOrderStateEnum.NOT_ACTIVE.getCode()}
                || state == ${@com.dili.ia.glossary.LeaseOrderStateEnum.EFFECTIVE.getCode()}) {
                $('#toolbar'+index+' button').attr('disabled', true);
                $('#btn_stop_rent'+index).attr('disabled', false);
            } else if (state == ${@com.dili.ia.glossary.LeaseOrderStateEnum.RENTED_OUT.getCode()}
            || state == ${@com.dili.ia.glossary.LeaseOrderStateEnum.EXPIRED.getCode()}) {
                $('#toolbar'+index+' button').attr('disabled', true);
                $('#btn_refund_apply'+index).attr('disabled', false);
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
            $('#toolbar button').attr('disabled', false);
            $('#btn_withdraw').attr('disabled', true);
            $('#btn_renew').attr('disabled', true);
            $('#btn_refund_apply').attr('disabled', true);
        } else if (state == ${@com.dili.ia.glossary.LeaseOrderStateEnum.CANCELD.getCode()}) {
            $('#toolbar button').attr('disabled', false);
            $('#btn_edit').attr('disabled', true);
            $('#btn_cancel').attr('disabled', true);
            $('#btn_submit').attr('disabled', true);
            $('#btn_withdraw').attr('disabled', true);
            $('#btn_supplement').attr('disabled', true);
            $('#btn_renew').attr('disabled', true);
            $('#btn_refund_apply').attr('disabled', true);
        } else if (state == ${@com.dili.ia.glossary.LeaseOrderStateEnum.SUBMITTED.getCode()}) {
            $('#toolbar button').attr('disabled', false);
            $('#btn_edit').attr('disabled', true);
            $('#btn_cancel').attr('disabled', true);
            $('#btn_renew').attr('disabled', true);
            $('#btn_refund_apply').attr('disabled', true);
        } else if (state == ${@com.dili.ia.glossary.LeaseOrderStateEnum.NOT_ACTIVE.getCode()}
            || state == ${@com.dili.ia.glossary.LeaseOrderStateEnum.EFFECTIVE.getCode()}) {
            $('#toolbar button').attr('disabled', false);
            $('#btn_edit').attr('disabled', true);
            $('#btn_cancel').attr('disabled', true);
            $('#btn_submit').attr('disabled', true);
            $('#btn_withdraw').attr('disabled', true);
            $('#btn_supplement').attr('disabled', true);
            if (row.waitAmount == 0) {
                $('#btn_refund_apply').attr('disabled', true);
            }
        } else if (state == ${@com.dili.ia.glossary.LeaseOrderStateEnum.RENTED_OUT.getCode()}) {
            $('#toolbar button').attr('disabled', false);
            $('#btn_edit').attr('disabled', true);
            $('#btn_cancel').attr('disabled', true);
            $('#btn_submit').attr('disabled', true);
            $('#btn_withdraw').attr('disabled', true);
            $('#btn_supplement').attr('disabled', true);
            $('#btn_refund_apply').attr('disabled', true);
        } else if (state == ${@com.dili.ia.glossary.LeaseOrderStateEnum.REFUNDED.getCode()}) {
            $('#toolbar button').attr('disabled', false);
            $('#btn_edit').attr('disabled', true);
            $('#btn_cancel').attr('disabled', true);
            $('#btn_submit').attr('disabled', true);
            $('#btn_withdraw').attr('disabled', true);
            $('#btn_supplement').attr('disabled', true);
            $('#btn_refund_apply').attr('disabled', true);
        } else if (state == ${@com.dili.ia.glossary.LeaseOrderStateEnum.EXPIRED.getCode()}) {
            $('#toolbar button').attr('disabled', false);
            $('#btn_edit').attr('disabled', true);
            $('#btn_cancel').attr('disabled', true);
            $('#btn_submit').attr('disabled', true);
            $('#btn_withdraw').attr('disabled', true);
            $('#btn_supplement').attr('disabled', true);
            $('#btn_refund_apply').attr('disabled', true);
        }
    });
    /*****************************************自定义事件区 end**************************************/
</script>