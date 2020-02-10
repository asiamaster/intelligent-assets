<script>
    /**
     *
     * @Date 2019-11-06 17:30:00
     * @author jiangchengyong
     *
     ***/

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
        queryDataHandler();
    });
    /******************************驱动执行区 end****************************/

    /*****************************************函数区 begin************************************/
    /**
     * 打开新增窗口
     */
    function openInsertHandler() {
        _modal.modal('show');
        _modal.find('.modal-title').text('摊位新增');

    }

    /**
     * 打开修改窗口
     */
    function openUpdateHandler() {
        //获取选中行的数据
        let rows = _grid.bootstrapTable('getSelections');
        if (null == rows || rows.length == 0) {
            bs4pop.alert('请选中一条数据');
            return;
        }
        _modal.modal('show');
        _modal.find('.modal-title').text('摊位修改');

        let formData = $.extend({}, rows[0]);
        formData = bui.util.addKeyStartWith(bui.util.getOriginalData(formData), "_");
        bui.util.loadFormData(formData);
        $('#_account').prop('disabled',true);
    }

    /**
     * 禁启用操作
     * @param enable 是否启用:true-启用
     */
    function doEnableHandler(enable) {
        //获取选中行的数据
        let rows = _grid.bootstrapTable('getSelections');
        if (null == rows || rows.length == 0) {
            bs4pop.alert('请选中一条数据');
            return;
        }
        
        //table选择模式是单选时可用
        let selectedRow = rows[0];
        let msg = (enable || 'true' == enable) ? '确定要启用该摊位吗？' : '确定要禁用该摊位吗？';

        bs4pop.confirm(msg, undefined, function (sure) {
            if(sure){
                bui.loading.show('努力提交中，请稍候。。。');
                $.ajax({
                    type: "POST",
                    url: "${contextPath}/booth/doEnable.action",
                    data: {id: selectedRow.id, enable: enable},
                    processData:true,
                    dataType: "json",
                    async : true,
                    success : function(data) {
                        bui.loading.hide();
                        if(data.success){
                            _grid.bootstrapTable('refresh');
                            _modal.modal('hide');
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
     *  保存及更新表单数据
     */
    function saveOrUpdateHandler() {
        if (_form.validate().form() != true) {
            return false;
        }

        bui.loading.show('努力提交中，请稍候。。。');
        let _formData = bui.util.removeKeyStartWith(_form.serializeObject(), "_");
        let _url = null;
        //没有id就新增
        if (_formData.id == null || _formData.id == "") {
            _url = "${contextPath}/booth/insert.action";
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
        _grid.bootstrapTable('refreshOptions', {url: '/booth/listPage.action'});
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
    //表单弹框关闭事件
    _modal.on('hidden.bs.modal', function () {
        _form[0].reset();
        //重置表单验证到初始状态
        $(this).find('input,select,textarea').removeClass('is-invalid is-valid');
        $(this).find('input,select,textarea').removeAttr('disabled readonly');
        $(this).find('.invalid-feedback').css('display','none');
    });

    //行点击事件
    _grid.on('click-row.bs.table', function (e, row, $element, field) {
        var state = row.$_state;
        if (state == ${@com.dili.demo.glossary.EnabledStateEnum.DISABLED.getCode()}) {
            //当用户状态为 禁用，可操作 启用
            $('#btn_enable').attr('disabled', false);
            $('#btn_disabled').attr('disabled', true);
        } else if (state == ${@com.dili.demo.glossary.EnabledStateEnum.ENABLED.getCode()}) {
            //当用户状态为正常时，则只能操作 禁用
            $('#btn_enable').attr('disabled', true);
            $('#btn_disabled').attr('disabled', false);
        } else {
            //其它情况，按钮不可用
            $('#btn_enable').attr('disabled', true);
            $('#btn_disabled').attr('disabled', true);
        }
    });


    _grid.on('expand-row.bs.table', function (e,index, row, $detail){
        var cur_table = $detail.html(template('subTable',{})).find('table');
        $(cur_table).bootstrapTable();
        $(cur_table).bootstrapTable('refreshOptions', {url: '/booth/listPage.action'});
    });

    /*****************************************自定义事件区 end**************************************/
</script>