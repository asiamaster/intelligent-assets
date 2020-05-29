<script>
    /**
     *
     * @Date 2019-11-06 17:30:00
     * @author jiangchengyong
     *
     ***/

        //行索引计数器
    let itemIndex = 0;

    //对应摊位
    $(function () {
        registerMsg();
    });

    //初始化刷卡
    initSwipeCard({
        id:'getCustomer',
    });

    function buildFormData(){
        // let formData = new FormData($('#saveForm')[0]);
        let formData = $("input:not(table input),textarea,select").serializeObject();
        let depositOrderdetails = [];
        bui.util.yuanToCentForMoneyEl(formData);
        $("#boothTable tbody").find("tr").each(function(){
            let depositOrderdetail = {};
            $(this).find("input").each(function(t,el){
                let fieldName = $(this).attr("name").split('_')[0];
                if ($(this).val() != ""){
                    depositOrderdetail[fieldName] = $(this).val();
                }
            });
            if (depositOrderdetail != {}){
                depositOrderdetails.push(depositOrderdetail);
            }
        });

        $.extend(formData,{depositOrderdetails});
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
</script>