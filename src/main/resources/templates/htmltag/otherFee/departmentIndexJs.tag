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
        _grid.bootstrapTable('refreshOptions', {url: '${contextPath}/departmentChargeItem/listPage.action', pageSize: parseInt(size)});
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
    }

    /**
     * 关闭弹窗
     */
    function closeDialog(dialog){
        dialog.hide();
        queryDataHandler();
    }

    /**
     打开绑定部门与收费项
     */
    function openAddDepartmentHandler() {
        dia = bs4pop.dialog({
            title: '设置绑定',//对话框title
            content: '${contextPath}/departmentChargeItem/addDepartment.html', //对话框内容，可以是 string、element，$object
            width: '900px',//宽度
            height: '680px',//高度
            isIframe: true,//默认是页面层，非iframe
        });
    }

    // 提交保存绑定部门与收费项
    function doAddDepartmentHandler(){
        let validator = $('#saveForm').validate({ignore:''})
        if (!validator.form()) {
            return false;
        }
        bui.loading.show('努力提交中，请稍候。。。');
        let department  = []
        $.each($('[name="department"]:checked'), function (index, element) {
            department.push({departmentId: $(element).val(), departmentName: $(element).siblings('.custom-control-label').text()})
        })
        let data =  $.extend({}, $('#chargeItemId').serializeObject(), {departmentList: department})

        $.ajax({
            type: "POST",
            url: "${contextPath}/departmentChargeItem/doAddDepartment.action",
            data:  JSON.stringify(data),
            dataType: "json",
            contentType: "application/json",
            success: function (ret) {
                bui.loading.hide();
                if(!ret.success){
                    bs4pop.alert(ret.message, {type: 'error'});
                }else{
                    parent.closeDialog(parent.dia);
                }
            },
            error: function (error) {
                bui.loading.hide();
                bs4pop.alert('远程访问失败', {type: 'error'});
            }
        });
    }


    function buildFormData(){
        // let formData = new FormData($('#saveForm')[0]);
        let formData = $("input:not(table input),textarea,select").serializeObject();
        let typeName = $('#typeCode').find("option:selected").text();
        bui.util.yuanToCentForMoneyEl(formData);
        $.extend(formData,{typeName});
        return formData;
    }

    /**
     * 业务编号formatter
     * @param value
     * @param row
     * @param index
     * @returns {string}
     */
    function codeFormatter(value,row,index) {
        return '<a href="javascript:openViewHandler('+row.id+')">'+value+'</a>';
    }

    //选中行事件
    _grid.on('uncheck.bs.table', function (e, row, $element) {
        currentSelectRowIndex = undefined;
    });

    $("#checkAll").click(function(){
        $('#department-group :checkbox').not(this).prop('checked', this.checked);
    });
    $(document).on('change', '#department-group :checkbox', function(){
        $("#checkAll").prop('checked', '');
    });

    $('#save').on('click', bui.util.debounce(doAddDepartmentHandler,1000,true));


</script>
