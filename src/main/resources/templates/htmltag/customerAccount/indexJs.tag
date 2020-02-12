<script>

    //时间范围
    lay('.laydatetime').each(function() {
        laydate.render({
            elem : this
            ,trigger : 'click'
            ,range: true
        });
    });

    function openInsertHandler() {
        let dia = bs4pop.dialog({
            id: 'addModal',
            title: '新增定金',//对话框title
            content: '/earnestOrder/add.html', //对话框内容，可以是 string、element，$object
            width: 900,//宽度
            height: 700,//高度
            isIframe : true,//默认是页面层，非iframe
        });
        // dia.modal('show');
        $('#addModal').modal('handleUpdate')
    }

</script>

<!--
http://127.0.0.1/earnestOrder/view.html 查看
http://127.0.0.1/earnestOrder/add.html 新增
http://127.0.0.1/earnestOrder/update.html 修改
-->
