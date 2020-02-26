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
    /******************************驱动执行区 end****************************/


    /**
     * 查询处理
     */
    function queryDataHandler() {
        // currentSelectRowIndex = undefined;
        // $('#toolbar button').attr('disabled', false);
        _grid.bootstrapTable('refreshOptions', {url: '/customerAccount/listPage.action'});
    }

    /**
     打开退款窗口
     */
    function openEarnestRefundHandler() {
        dia = bs4pop.dialog({
            title: '退款',//对话框title
            content: '/customerAccount/earnestRefund.html', //对话框内容，可以是 string、element，$object
            width: 900,//宽度
            height: 450,//高度
            isIframe : true,//默认是页面层，非iframe
        });

    }
    /**
     打开定金转移窗口
     */
    function openEarnestTransferHandler() {
        dia = bs4pop.dialog({
            title: '定金转移',//对话框title
            content: '/customerAccount/earnestTransfer.html', //对话框内容，可以是 string、element，$object
            width: 900,//宽度
            height: 580,//高度
            isIframe : true,//默认是页面层，非iframe
        });
    }



</script>

<!--
http://ia.diligrp.com:8381/customerAccount/earnestRefund.html 定金退款
http://ia.diligrp.com:8381/customerAccount/earnestTransfer.html 定金转移
-->
