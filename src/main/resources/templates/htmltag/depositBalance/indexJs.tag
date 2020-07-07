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
        _grid.bootstrapTable('refreshOptions', {pageNumber: 1, url: '${contextPath}/depositBalance/listPage.action', pageSize: parseInt(size)});
        calcTotalBalance();
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
        calcTotalBalance();
    }

    /**
     * 关闭弹窗
     */
    function closeDialog(dialog){
        dialog.hide();
        queryDataHandler();
    }

    function calcTotalBalance() {
        let formData = $("input:not(table input),textarea,select").serializeObject();
        $.ajax({
            type: "GET",
            url: "${contextPath}/depositBalance/getTotalBalance.action",
            data: formData,
            processData:true,
            dataType: "json",
            success : function(ret) {
                if(ret.success){
                    $('#totalBalance').text('余额：' + ret.data);
                }else{
                    bs4pop.alert(ret.message, {type: 'error'});
                }
            },
            error : function() {
                bs4pop.alert('远程访问失败', {type: 'error'});
            }
        });
    }
</script>

