<script>
    /**
     *
     * @Date 2020-8-4
     * @author asiamaster
     *
     ***/

    /*********************变量定义区 begin*************/
        //行索引计数器
        //如 let itemIndex = 0;
    let _grid = $('#grid');

    //开票类型的select2控件参数
    let invoiceTypeOption = {
        //隐藏搜索框
        minimumResultsForSearch: -1
    };

    //开票主体的select2控件参数
    var customerNameAutoCompleteOption = {
        serviceUrl: '/customer/listNormal.action',
        paramName: 'likeName',
        displayFieldName: 'name',
        showNoSuggestionNotice: true,
        noSuggestionNotice: '<a href="javascript:;" id="goCustomerRegister">无此客户</a>',
        transformResult: function (result) {
            if(result.success){
                let data = result.data;
                return {
                    suggestions: $.map(data, function (dataItem) {
                        return $.extend(dataItem, {
                                value: dataItem.name + '（' + dataItem.certificateNumber + '）'
                            }
                        );
                    })
                }
            }else{
                bs4pop.alert(result.message, {type: 'error'});
                return false;
            }
        },
        selectFn: function (suggestion) {

        }
    };

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
     *  保存及更新表单数据
     */
    function saveOrUpdateHandler() {
        if ($('#_form').validate().form() != true) {
            return false;
        }
        bui.loading.show('努力提交中，请稍候。。。');
        let _formData = bui.util.removeKeyStartWith($('#_form').serializeObject(true), "_");
        let _url = null;
        //没有id就新增
        if (_formData.id == null || _formData.id == "") {
            _url = "${contextPath}/invoiceRecord/insert.action";
        } else {//有id就修改
            _url = "${contextPath}/invoiceRecord/update.action";
        }
        $.ajax({
            type: "POST",
            url: _url,
            data: _formData,
            dataType: "json",
            async: true,
            success: function (data) {
                bui.loading.hide();
                if (data.success) {
                    _grid.bootstrapTable('refresh');
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
     * 打开编辑窗口
     */
    function bs4popDialog(title, param, handler){
        bs4pop.dialog({
            title: title,
            content: bui.util.HTMLDecode(template('editTpl',param)),
            closeBtn: true,
            backdrop : 'static',
            width: '40%',
            onShowEnd: function(){
                laydate.render({
                    elem: "#_invoiceDate",
                    type: 'date',
                    theme: '#007bff',
                    trigger:'click'
                });
            },
            btns: [
                {
                    label: '确定', className: 'btn-primary', onClick(e) {
                        return handler();
                    }
                },
                {label: '取消', className: 'btn-default', onClick(e) {}}
            ]
        });

    }

    /**
     * 打开修改窗口
     */
    function openUpdateHandler(row) {
        if(!row){
            //获取选中行的数据
            let rows = _grid.bootstrapTable('getSelections');
            if (null == rows || rows.length != 1) {
                bs4pop.alert('请选中一条数据');
                return;
            }
            row = rows[0];
        }
        bs4popDialog('修改审批人分配', row, saveOrUpdateHandler);
    }

    /**
     * 删除操作
     */
    function doDeleteHandler() {
        //获取选中行的数据
        let rows = _grid.bootstrapTable('getSelections');
        if (null == rows || rows.length == 0) {
            bs4pop.alert('请选中一条数据');
            return;
        }
        //table选择模式是单选时可用
        let selectedRow = rows[0];
        let msg = '将删除对应的数据，确定要删除吗？';
        bs4pop.confirm(msg, undefined, function (sure) {
            if(sure){
                bui.loading.show('努力提交中，请稍候。。。');
                $.ajax({
                    type: "POST",
                    url: "${contextPath}/invoiceRecord/delete.action",
                    data: {id: selectedRow.id},
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

    /**
     * 查询处理
     */
    function queryDataHandler() {
        _grid.bootstrapTable('refreshOptions', {url: '/invoiceRecord/listPage.action'});
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

    /*****************************************函数区 end**************************************/

    /*****************************************自定义事件区 begin************************************/

    _grid.on('dbl-click-cell.bs.table', function ($element, field, value, row) {
        openUpdateHandler(row);
    });

    /*****************************************自定义事件区 end**************************************/
</script>