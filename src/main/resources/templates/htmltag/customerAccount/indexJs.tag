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
        queryDataHandler();
    });

    _grid.ready( function () {

        let size = ($(window).height() - $('#queryForm').height() - 210) / 40;
        size = size > 10 ? size : 10;
        _grid.bootstrapTable('refreshOptions', {pageSize: parseInt(size)});
    })
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
        // $('#toolbar button').attr('disabled', false);
        _grid.bootstrapTable('refreshOptions', {url: '${contextPath}/customerAccount/listPage.action'});
    }

    /**
     打开退款窗口
     */
    function openEarnestRefundHandler() {
        //获取选中行的数据
        let rows = _grid.bootstrapTable('getSelections');
        if (null == rows || rows.length == 0) {
            bs4pop.alert('请选中一条数据');
            return;
        }

        dia = bs4pop.dialog({
            title: '退款',//对话框title
            content: '${contextPath}/customerAccount/earnestRefund.html?id='+rows[0].id, //对话框内容，可以是 string、element，$object
            width: '80%',//宽度
            height: 450,//高度
            isIframe : true,//默认是页面层，非iframe
        });

    }
    /**
     打开定金转移窗口
     */
    function openEarnestTransferHandler() {
        //获取选中行的数据
        let rows = _grid.bootstrapTable('getSelections');
        if (null == rows || rows.length == 0) {
            bs4pop.alert('请选中一条数据');
            return;
        }

        dia = bs4pop.dialog({
            title: '定金转移',//对话框title
            content: '${contextPath}/customerAccount/earnestTransfer.html?id='+rows[0].id, //对话框内容，可以是 string、element，$object
            width: '80%',//宽度
            height: 580,//高度
            isIframe : true,//默认是页面层，非iframe
        });
    }



</script>

<!--
http://ia.diligrp.com:8381/customerAccount/earnestRefund.html 定金退款
http://ia.diligrp.com:8381/customerAccount/earnestTransfer.html 定金转移
-->
