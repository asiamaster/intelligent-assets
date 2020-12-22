<script>

    /*********************变量定义区 begin*************/
        //行索引计数器
        //如 let itemIndex = 0;
    let _grid = $('#details');
    let _boutiqueListTable = $('#boutiqueListTable');

    let _form = $('#_form');
    let currentSelectRowIndex;
    var dia;
    let duration = 2;
    let timeUnit = "day"
    $("#enterTimeStart").attr("value", moment().subtract(duration, timeUnit).startOf('day').format('YYYY-MM-DD HH:mm:ss'));
    $("#enterTimeEnd").attr("value", moment().endOf('day').format('YYYY-MM-DD HH:mm:ss'));

    $("#confirmTimeStart").attr("value", moment().subtract(duration, timeUnit).startOf('day').format('YYYY-MM-DD HH:mm:ss'));
    $("#confirmTimeEnd").attr("value", moment().endOf('day').format('YYYY-MM-DD HH:mm:ss'));
    /*********************变量定义区 end***************/


    /******************************驱动执行区 begin***************************/
    $(function () {
        $(window).resize(function () {
            _grid.bootstrapTable('resetView')
        });
        let size = ($(window).height() - $('#queryForm').height() - 210) / 40;
        size = size > 10 ? size : 10;
        _grid.bootstrapTable('refreshOptions', {url: '${contextPath}/boutiqueEntranceRecord/listPage.action', pageSize: parseInt(size)});
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
     打开退款窗口
     */
    function openRefundHandler(id) {
        //获取选中行的数据
        if(!id){
            //获取选中行的数据
            // let rows = _grid.bootstrapTable('getSelections');
            let rows = _boutiqueListTable.bootstrapTable('getSelections');
            if (null == rows || rows.length == 0) {
                bs4pop.alert('请选中一条数据');
                return;
            }
            var id = rows[0]['0'];
        }
        let url = '/boutiqueFeeOrder/refundApply.html?id=' + id;
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
     取消
     */
    function openCancelHandler() {
        if(!id){
            //获取选中行的数据
            // let rows = _grid.bootstrapTable('getSelections');
            let rows = _boutiqueListTable.bootstrapTable('getSelections');
            if (null == rows || rows.length == 0) {
                bs4pop.alert('请选中一条数据');
                return;
            }
            var id = rows[0]['0'];
        }
            bs4pop.confirm('确定取消该业务单？', {}, function (sure) {
                let data = JSON.stringify({id: id});
                if(sure){
                    $.ajax({
                        type: "POST",
                        url: '${contextPath}/boutiqueFeeOrder/cancel.action?id=' + id,
                        data: data,
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


    //选中行事件
    _grid.on('uncheck.bs.table', function (e, row, $element) {
        currentSelectRowIndex = undefined;
    });

   //选中行事件 -- 可操作按钮控制
    _grid.on('check.bs.table', function (e, row, $element) {
        let state = row.state;
        $('#toolbar button').attr('disabled', true);
        if (state == '${@com.dili.ia.glossary.BoutiqueOrderStateEnum.SUBMITTED_PAY.getName()}') {  //已提交 才能取消
            $('#btn_cancel').attr('disabled', false);
        } else if (state == '${@com.dili.ia.glossary.BoutiqueOrderStateEnum.PAID.getName()}') { //已缴费 才能退款
            $('#btn_refund').attr('disabled', false);
        }
    });

</script>

<!--
http://ia.diligrp.com:8381/earnestOrder/view.html 查看
http://ia.diligrp.com:8381/earnestOrder/add.html 新增
http://ia.diligrp.com:8381/earnestOrder/update.html 修改
-->
