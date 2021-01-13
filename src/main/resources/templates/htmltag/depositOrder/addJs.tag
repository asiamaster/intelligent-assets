<script>
    /**
     *
     * @Date 2019-11-06 17:30:00
     * @author jiangchengyong
     *
     ***/

    //对应摊位
    $(function () {
        //初始化刷身份证
        initSwipeIdCard({
            id:'getCustomer',
        });

        //初始化刷园区卡
        initSwipeParkCard({
            id:'icReader',
            onLoadSuccess:function(customer){
                $('#customerName').val(customer.name);
                $('#customerId').val(customer.customerId);
                $('#certificateNumber,#_certificateNumber').val(customer.customerCertificateNumber);
                $('#customerCellphone').val(customer.customerContactsPhone);
            }
        });

        registerMsg();
    });


    function buildFormData(){
        // let formData = new FormData($('#saveForm')[0]);
        let formData = $("input:not(table input),textarea,select").serializeObject();

        let typeName = $('#typeCode').find("option:selected").text();
        bui.util.yuanToCentForMoneyEl(formData);
        $.extend(formData,{typeName});
        return formData;
    }

    // 提交保存
    function doAddDepositHandler(){
        let validator = $('#saveForm').validate({ignore:''})
        if (!validator.form()) {
            /*$(this).find('.collapse').each(function (index, element) {
                $(element).trigger('show.bs.collapse');
            });*/
            $('.breadcrumb [data-toggle="collapse"]').html('收起 <i class="fa fa-angle-double-up" aria-hidden="true"></i>');
            $('.collapse:not(.show)').addClass('show');
            return false;
        }
        bui.loading.show('努力提交中，请稍候。。。');
        // let _formData = new FormData($('#saveForm')[0]);
        $.ajax({
            type: "POST",
            url: "${contextPath}/depositOrder/doAdd.action",
            data: buildFormData(),
            dataType: "json",
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

    $('#save').on('click', bui.util.debounce(doAddDepositHandler,1000,true));
</script>