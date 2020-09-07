<script>
    /**
     *
     * @Date 2019-11-06 17:30:00
     * @author jiangchengyong
     *
     ***/

        // 表类型
    let meterType = '';

    // 对应摊位
    $(function () {
        registerMsg();
    });

    //初始化刷卡
    initSwipeCard({
        id:'getCustomer',
    });

    $('[name="type"]').on('change', function(){
        // $('[name="number"]').autocomplete('setOptions', {serviceUrl: '/meter/listUnbindMetersByType.action'});
        meterType = $(this).val();
        $('[name="number"]').autocomplete('setOptions', {params: {'type': meterType }});
    })

    var numberAutoCompleteOption = {
        serviceUrl: '/meter/listUnbindMetersByType.action',
        paramName: 'keyword',
        displayFieldName: 'name',
        transformResult: function (response) {
            if(response.success){
                return {
                    suggestions: $.map(response.data, function(item) {
                        return  $.extend(item, {
                            value: item.number + ''
                        });
                    })
                };
            }else{
                bs4pop.alert(result.message, {type: 'error'});
                return false;
            }
        },
        selectFn: function (suggestion) {
            $('[name="number"]').val(suggestion.value);
            $('[name="assetsId"]').val(suggestion.assetsId);
            $('[name="assetsName"]').val(suggestion.assetsName);
            $('[name="number"], [name="assetsId"], [name="assetsName"]').valid();
        }
    };




    // 提交保存
    function doAddEarnestHandler(){
        let validator = $('#saveForm').validate({ignore:''});
        if (!validator.form()) {
            $('.breadcrumb [data-toggle="collapse"]').html('收起 <i class="fa fa-angle-double-up" aria-hidden="true"></i>');
            $('.collapse:not(.show)').addClass('show');
            return false;
        }
               bui.loading.show('努力提交中，请稍候。。。');
        // let _formData = new FormData($('#saveForm')[0]);
        $.ajax({
            type: "POST",
            url: "${contextPath}/customerMeter/add.action",
            data: $("input:not(table input),textarea,select").serializeObject(),
            dataType: "json",
            success: function (ret) {
                bui.loading.hide();
                if(!ret.success){
                    bs4pop.alert(ret.message, {type: 'error'});
                }else{
                    parent.dia.hide();
                }
            },
            error: function (error) {
                bui.loading.hide();
                bs4pop.alert('远程访问失败', {type: 'error'});
            }
        });
    }


    $('#save').on('click', bui.util.debounce(doAddEarnestHandler,1000,true));

</script>