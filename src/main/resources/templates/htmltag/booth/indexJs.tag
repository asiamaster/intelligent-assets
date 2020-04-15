<script>
    /**
     *
     * @Date 2019-11-06 17:30:00
     * @author jiangchengyong
     *
     ***/


    //时间范围
    lay('.laydatetime').each(function () {
        laydate.render({
            elem: this
            , trigger: 'click'
            , range: false
        });
    });

    /*********************变量定义区 begin*************/
        //行索引计数器
        //如 let itemIndex = 0;
    let _grid = $('#grid');
    let _form = $('#_form');
    let _modal = $('#_modal');

    /*********************变量定义区 end***************/


    /******************************驱动执行区 begin***************************/
    $(function () {
        $(window).resize(function () {
            _grid.bootstrapTable('resetView')
        });
        let size = ($(window).height() - $('#queryForm').height() - 210) / 40;
        size = size > 10 ? size : 10;
        _grid.bootstrapTable('refreshOptions', {url: '/booth/listPage.action', pageSize: parseInt(size)});
    });

    /******************************驱动执行区 end****************************/

    /*****************************************函数区 begin************************************/
    /**
     * 打开新增窗口
     */
    function openInsertHandler() {
        $("#_modal").modal();

        $('#_modal .modal-body').load("/booth/add.html");
        _modal.find('.modal-title').text('摊位新增');

    }

    /**
     * 打开修改窗口
     */
    function openUpdateHandler(id) {
        let rows = _grid.bootstrapTable('getSelections');
        if (null == rows || rows.length == 0) {
            bs4pop.alert('请选中一条数据');
            return;
        }
        $("#_modal").modal("show");

        $('#_modal .modal-body').load("/booth/update.html?id=" + rows[0].id);
        _modal.find('.modal-title').text('摊位修改');
    }

    function openViewHandler(id) {
        let rows = _grid.bootstrapTable('getSelections');
        if (null == rows || rows.length == 0) {
            bs4pop.alert('请选中一条数据');
            return;
        }
        $("#_modal").modal("show");

        $('#_modal .modal-body').load("/booth/view.html?id=" + rows[0].id);
        _modal.find('.modal-title').text('摊位查看');
    }

    function openSplitHandler(id) {
        let rows = _grid.bootstrapTable('getSelections');
        if (null == rows || rows.length == 0) {
            bs4pop.alert('请选中一条数据');
            return;
        }
if(rows[0].parentId !=0){
    bs4pop.alert('只有父摊位才能拆分');
    return;
}
        $("#_modal").modal("show");

        $('#_modal .modal-body').load("/booth/split.html?id=" + rows[0].id);
        _modal.find('.modal-title').text('摊位拆分');
    }

    function openDeleteHandler(id) {
        let rows = _grid.bootstrapTable('getSelections');
        if (null == rows || rows.length == 0) {
            bs4pop.alert('请选中一条数据');
            return;
        }
        bs4pop.confirm("确定要删除吗", {title: "确认提示"}, function (sure) {
            if (sure) {
                $.ajax({
                    type: "POST",
                    dataType: "json",
                    url: '/booth/delete.action',
                    data: {id: rows[0].id},
                    success: function (data) {
                        bui.loading.hide();
                        if (data.code != '200') {
                            bs4pop.alert(data.message, {type: 'error'});
                            return;
                        }
                        // bs4pop.alert('成功', {type: 'success '}, function () {
                        //     window.location.reload();
                        // });
                        window.location.reload();
                    },
                    error: function () {
                        bui.loading.hide();
                        bs4pop.alert("区域信息删除失败!", {type: 'error'});
                    }
                });
            }
        });
    }

    /**
     * 禁启用操作
     * @param enable 是否启用:true-启用
     * @param id
     */
    function doEnableHandler(enable, id) {
        var opType ="";
        if(enable == 1){
            opType = "enable";
        }
        if(enable == 2){
            opType = "disable";
        }
        let rows = _grid.bootstrapTable('getSelections');
        if (null == rows || rows.length == 0) {
            bs4pop.alert('请选中一条数据');
            return;
        }
        //table选择模式是单选时可用
        let msg = enable == 1 ? '确定要启用该摊位吗？' : '确定要禁用该摊位吗？';

        bs4pop.confirm(msg, undefined, function (sure) {
            if (sure) {
                bui.loading.show('努力提交中，请稍候。。。');
                $.ajax({
                    type: "POST",
                    url: "${contextPath}/booth/update.action",
                    data: {id: rows[0].id, state: enable,opType:opType},
                    processData: true,
                    dataType: "json",
                    async: true,
                    success: function (data) {
                        bui.loading.hide();
                        if (data.success) {
                            _grid.bootstrapTable('refresh');
                            _modal.modal('hide');
                        } else {
                            bs4pop.alert(data.result, {type: 'error'});
                        }
                    },
                    error: function () {
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
        var form = $("#_form");
        if (form.validate().form() != true) {
            return false;
        }

        bui.loading.show('努力提交中，请稍候。。。');
        let _formData = bui.util.removeKeyStartWith(form.serializeObject(), "_");
        let _url = null;
        //没有id就新增
        if (_formData.id == null || _formData.id == "") {
            _url = "${contextPath}/booth/insert";
        } else {//有id就修改
            _url = "${contextPath}/booth/update.action";
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
        };
        return $.extend(temp, bui.util.bindGridMeta2Form('grid', 'queryForm'));
    }

    /*****************************************函数区 end**************************************/

    /*****************************************自定义事件区 begin************************************/
    //表单弹框关闭事件
    _modal.on('hidden.bs.modal', function () {
        _form[0].reset();
        //重置表单验证到初始状态
        $(this).find('input,select,textarea').removeClass('is-invalid is-valid');
        $(this).find('input,select,textarea').removeAttr('disabled readonly');
        $(this).find('.invalid-feedback').css('display', 'none');
    });

    $.ajax({
        type: "POST",
        url: "/district/search.action",
        data: {parentId: 0},
        success: function (data) {
            if (data.code == "200") {
                var array = [];
                array.push({text:"-- 全部 --",id:""});
                var data = $.map(data.data, function (obj) {
                    obj.text = obj.text || obj.name;
                    return obj;
                });
                for (var x in data) {
                    array.push(data[x]);
                }
                if (data.length == 0) {
                    $('#areaOneList').html("");
                    $('#areaTwoList').html("");
                } else {
                    $("#areaOneList").select2({
                        data: array,
                        language:'zh-CN',
                        width: "50%",
                        minimumResultsForSearch: Infinity
                    });
                }
            }
        }
    });
    $("#areaOneList").change(function () {
        if ($(this).val() != "") {
            $.ajax({
                type: "POST",
                url: "/district/search.action",
                data: {parentId: $(this).val()},
                success: function (data) {
                    if (data.code == "200") {
                        var array = [];
                        array.push({text:"-- 全部 --",id:""});
                        var data = $.map(data.data, function (obj) {
                            obj.text = obj.text || obj.name;
                            return obj;
                        });
                        for (var x in data) {
                            array.push(data[x]);
                        }
                        $('#areaTwoList').html("")
                        $("#areaTwoList").select2({
                            language: 'zh-CN',
                            data: array,
                            width: "50%",
                            minimumResultsForSearch: Infinity
                        })
                    }
                }
            });
        } else {
            $('#areaTwoList').html("");
            var array = [];
            array.push({text:"-- 全部 --",id:""});
            $("#areaTwoList").select2({
                language: 'zh-CN',
                data: array,
                width: "50%",
                minimumResultsForSearch: Infinity
            })
        }

    });

    _grid.on('post-body.bs.table', function (e,data){
        var columns = _grid.bootstrapTable('getOptions').columns;
        if (columns && columns[0][0].visible) {
            _grid.treegrid({
                treeColumn: 0,
                onChange: function() {
                    console.log($(this).treegrid('getDepth'))
                }
            })
        }
    });

    /*****************************************自定义事件区 end**************************************/
</script>