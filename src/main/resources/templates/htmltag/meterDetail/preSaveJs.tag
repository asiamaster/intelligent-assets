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
            $('[name="customerId"]').val(suggestion.customerId);
            $('[name="price"]').val(suggestion.price);
            $('[name="meterId"]').val(suggestion.meterId);
            $.ajax({
                type: "post",
                url: '/meterDetail/getLastAmount.action',
                datatype: 'json',
                data: {'meterId': suggestion.meterId},
                success: function(res){
                    if(res.success === true) {
                        $('[name="lastAmount"]').val(res.data);
                    } else {
                        bs4pop.alert("上期指数获取失败!", {type: 'error'});
                    }
                },
                error: function(error){
                    bs4pop.alert("上期指数获取失败!", {type: 'error'});
                }
            })
        }
    };

    // 提交保存
    function saveOrUpdateHandler(){
        Logger
        let validator = $('#saveForm').validate({ignore:''})
        if (!validator.form()) {
            $('.breadcrumb [data-toggle="collapse"]').html('收起 <i class="fa fa-angle-double-up" aria-hidden="true"></i>');
            $('.collapse:not(.show)').addClass('show');
            return false;
        }

        bui.loading.show('努力提交中，请稍候。。。');
        let _formData = $('#saveForm').serializeObject();
        let _url = null;

        // 动态收费项
        let businessChargeDtos = []
        $('#saveForm').find('.chargeItem').each(function(){
            let businessCharge = {};
            businessCharge.chargeItemId=$(this).attr("name").split("_")[1];
            businessCharge.chargeItemName=$(this).attr("chargeItem");
            businessCharge.amount=parseInt($(this).val())*100;
            if (businessCharge != {}) {
                businessChargeDtos.push(businessCharge);
            }
        })
        _formData.businessChargeItems = businessChargeDtos;

        //没有id就新增
        if (_formData.id == null || _formData.id == "") {
            _url = "${contextPath}/meterDetail/add.action";
        } else {//有id就修改
            _url = "${contextPath}/meterDetail/update.action";
        }
        $.ajax({
            type: "POST",
            url: _url,
            data: JSON.stringify(_formData),
            dataType: "json",
            contentType: "application/json",
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