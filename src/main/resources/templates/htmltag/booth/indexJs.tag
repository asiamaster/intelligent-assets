<script>

    /*********************变量定义区 begin*************/
        //行索引计数器
        //如 let itemIndex = 0;
    let _grid = $('#grid');
    let _form = $('#_form');
    let currentSelectRowIndex;
    var editBoothDia, viewBoothDia, splitBoothDia;

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

    /**
     时间范围
     */
    lay('.laydatetime').each(function () {
        laydate.render({
            elem: this
            , trigger: 'click'
        });
    });


    /**
     * 查询处理
     */
    function queryDataHandler() {
        // currentSelectRowIndex = undefined;
        // $('#toolbar button').attr('disabled', false);
        _grid.bootstrapTable('refreshOptions', {url: '/booth/listPage.action'});
    }
    /**
     打开新增窗口
     */
    function openInsertHandler() {
        editBoothDia = bs4pop.dialog({
            title: '新增客户',
            content: '/booth/add.html',
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '400',
            height: '700',
            btns: []
        });
    }
    /**
     打开修改窗口
     */
    function openInsertHandler() {
        viewBoothDia = bs4pop.dialog({
            title: '资产详情',//对话框title
            content: '/booth/view', //对话框内容，可以是 string、element，$object
            width: 900,//宽度
            height: 700,//高度
            isIframe : true,//默认是页面层，非iframe
        });
    }

    /**
     打开拆分窗口
     */
    function openInsertHandler() {
        splitBoothDia = bs4pop.dialog({
            title: '资产详情',//对话框title
            content: '/booth/split', //对话框内容，可以是 string、element，$object
            width: 900,//宽度
            height: 700,//高度
            isIframe : true,//默认是页面层，非iframe
        });
    }

</script>

