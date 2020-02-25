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

    $(function () {
        $(window).resize(function () {
            _grid.bootstrapTable('resetView')
        });
        queryDataHandler();
    });
    /******************************驱动执行区 end****************************/

    /************ 表格是否选中一条数据 start **************/
    function isSelectRow() {
        let rows = _grid.bootstrapTable('getSelections');
        let isSelectFlag = true;
        if (null == rows || rows.length == 0) {
            bs4pop.alert('请选中一条数据');
            isSelectFlag = false;
        }
        return isSelectFlag
    }
    /************ 表格是否选中一条数据 end **************/

    /**
     * 查询处理
     */
    function queryDataHandler() {
        // currentSelectRowIndex = undefined;
        // $('#toolbar button').attr('disabled', false);
        _grid.bootstrapTable('refreshOptions', {url: '/earnestOrder/listPage.action'});
    }

    /**
     打开新增窗口
     */
    function openInsertHandler() {
        dia = bs4pop.dialog({
            id: 'addModal',
            title: '新增定金',//对话框title
            content: '/earnestOrder/add.html', //对话框内容，可以是 string、element，$object
            width: 900,//宽度
            height: 700,//高度
            isIframe: true,//默认是页面层，非iframe
        });
    }

    /**
     打开查看窗口
     */
    function openViewHandler() {
        dia = bs4pop.dialog({
            id: 'addModal',
            title: '定金详情',//对话框title
            content: '/earnestOrder/view.html', //对话框内容，可以是 string、element，$object
            width: 900,//宽度
            height: 700,//高度
            isIframe: true,//默认是页面层，非iframe
        });
    }

    function openSubmitHandler() {
        if(isSelectRow()){
            bs4pop.confirm('提交后该信息不可更改，并且可进行缴费，确认提交？', {}, function () {
                bs4pop.alert('提交成功', {type: 'success '});
                bs4pop.alert('提交失败', {type: 'error '});
            });
        }
    }



</script>

<!--
http://ia.diligrp.com:8381/earnestOrder/view.html 查看
http://ia.diligrp.com:8381/earnestOrder/add.html 新增
http://ia.diligrp.com:8381/earnestOrder/update.html 修改
-->
