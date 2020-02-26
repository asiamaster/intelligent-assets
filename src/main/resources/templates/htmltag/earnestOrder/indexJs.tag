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
        _grid.bootstrapTable('refreshOptions', {url: '${contextPath}/earnestOrder/listPage.action'});
    }

    /**
     打开新增窗口
     */
    function openInsertHandler() {
        dia = bs4pop.dialog({
            id: 'addModal',
            title: '新增定金',//对话框title
            content: '${contextPath}/earnestOrder/add.html', //对话框内容，可以是 string、element，$object
            width: 900,//宽度
            height: 700,//高度
            isIframe: true,//默认是页面层，非iframe
        });
    }

    /**
     打开新增窗口
     */
    function openUpdateHandler() {
        dia = bs4pop.dialog({
            id: 'addModal',
            title: '修改定金',//对话框title
            content: '${contextPath}/earnestOrder/update.html', //对话框内容，可以是 string、element，$object
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
            content: '${contextPath}/earnestOrder/view.html', //对话框内容，可以是 string、element，$object
            width: 900,//宽度
            height: 700,//高度
            isIframe: true,//默认是页面层，非iframe
        });
    }
    /**
     提交处理
     */
    function openSubmitHandler() {
        if(isSelectRow()){
            bs4pop.confirm('提交后该信息不可更改，并且可进行缴费，确认提交？', {}, function (sure) {
                if(sure){
                    bui.loading.show('努力提交中，请稍候。。。');
                    //获取选中行的数据
                    let rows = _grid.bootstrapTable('getSelections');
                    let selectedRow = rows[0];

                    $.ajax({
                        type: "POST",
                        url: "${contextPath}/earnestOrder/submit.action",
                        data: {id: selectedRow.id},
                        processData:true,
                        dataType: "json",
                        async : true,
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
                        url: "${contextPath}/earnestOrder/withdraw.action",
                        data: {id: selectedRow.id},
                        processData:true,
                        dataType: "json",
                        async : true,
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
            bs4pop.confirm('确定取消该业务单？', {}, function (sure) {
                if(sure){
                    bui.loading.show('努力提交中，请稍候。。。');
                    //获取选中行的数据
                    let rows = _grid.bootstrapTable('getSelections');
                    let selectedRow = rows[0];

                    $.ajax({
                        type: "POST",
                        url: "${contextPath}/earnestOrder/cancel.action",
                        data: {id: selectedRow.id},
                        processData:true,
                        dataType: "json",
                        async : true,
                        success : function(data) {
                            bui.loading.hide();
                            console.log(data);
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

</script>

<!--
http://ia.diligrp.com:8381/earnestOrder/view.html 查看
http://ia.diligrp.com:8381/earnestOrder/add.html 新增
http://ia.diligrp.com:8381/earnestOrder/update.html 修改
-->
