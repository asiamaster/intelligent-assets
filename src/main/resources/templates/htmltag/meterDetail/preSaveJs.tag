<script>

    let meterType = $('#metertype').val();
    lay('.laymonth').each(function () {
        laydate.render({
            elem: this,
            trigger: 'click',
            type: 'month',
            theme: '#007bff',
            done: function () {
            }
        });
    });

    var numberAutoCompleteOption = {
        serviceUrl: '/customerMeter/listCustomerMeterByLikeName.action',
        paramName: 'keyword',
        displayFieldName: 'name',
        params: {'type': meterType},
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
            $('[name="assetsType"]').val(suggestion.assetsType);
            $('[name="assetsName"]').val(suggestion.assetsName);
            $('[name="departmentId"]').val(suggestion.departmentId);
            $('[name="customerCellphone"]').val(suggestion.customerCellphone);
            $('[name="customerName"]').val(suggestion.customerName);
            $('[name="lastAmount"]').val(suggestion.lastAmount);
            $('[name="price"]').val(suggestion.price);
            $('#saveForm').validate().form();
        }
    };

    var lastAmountAutoCompleteOption = {
        serviceUrl: '/customerMeter/getBindInfoByMeterId.action',
        paramName: 'keyword',
        displayFieldName: 'name',
        params: {'meterId': meterId},
        transformResult: function (response) {
            if(response.success){
                return {
                    suggestions: $.map(response.data, function(item) {
                        return  $.extend(item, {
                            value: item.lastAmount + ''
                        });
                    })
                };
            }else{
                bs4pop.alert(result.message, {type: 'error'});
                return false;
            }
        },
        selectFn: function (suggestion) {
            $('[name="lastAmount"]').val(suggestion.value);
            $('#saveForm').validate().form();
        }
    };


    // 提交保存
    function saveOrUpdateHandler(){
        let validator = $('#saveForm').validate({ignore:''})
        if (!validator.form()) {
            $('.breadcrumb [data-toggle="collapse"]').html('收起 <i class="fa fa-angle-double-up" aria-hidden="true"></i>');
            $('.collapse:not(.show)').addClass('show');
            return false;
        }

        bui.loading.show('努力提交中，请稍候。。。');
        let _formData = bui.util.removeKeyStartWith(_form.serializeObject(), "_");
        let _url = null;

        //没有id就新增
        if (_formData.id == null || _formData.id == "") {
            _url = "${contextPath}/meterDetail/insert.action";
        } else {//有id就修改
            _url = "${contextPath}/meterDetail/update.action";
        }
        $.ajax({
            type: "POST",
            url: _url,
            data: _formData,
            dataType: "json",
            success: function (ret) {
                bui.loading.hide();
                if(!ret.success){
                    bs4pop.alert(ret.message, {type: 'error'});
                }else{
                    parent.dia.hide()
                }
            },
            error: function (error) {
                bui.loading.hide();
                bs4pop.alert('远程访问失败', {type: 'error'});
            }
        });
    }

</script>