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
            $('[name="customerCellphone"]').val(suggestion.customerCellphone);
            $('[name="customerName"]').val(suggestion.customerName);
            $('[name="customerId"]').val(suggestion.customerId);
            $('[name="price"]').val(suggestion.price/100);
            $('[name="meterId"]').val(suggestion.meterId);
            $.ajax({
                type: "post",
                url: '/meterDetail/getLastAmount.action',
                datatype: 'json',
                data: {'meterId': suggestion.meterId},
                success: function(res){
                    if(res.success === true) {
                        $('[name="lastAmount"]').val(res.data);
                        calcReceivable();
                        calcAmount();
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

    $('#thisAmount, #lastAmount').on('change', function () {
        calcReceivable();
        calcAmount();
    })
    $('.chargeItem').on('change', function () {
        calcAmount();
    })
    // 计算费用
    function calcReceivable() {
        let lastAmount = $('#lastAmount').val();
        let thisAmount = $('#thisAmount').val();
        let price = $('#price').val();
        if (lastAmount == '' || thisAmount == '' || price == '') {
            return false;
        }
        if (parseFloat(lastAmount) > parseFloat(thisAmount)) {
            $('#thisAmount, #usageAmount').val('');
            return false;
        }
        let usageAmount = parseFloat(thisAmount) - parseFloat(lastAmount);
        let receivable = usageAmount * price;
        $('#usageAmount').val(usageAmount);
        $('#receivable').val(receivable);
    }
    // 计算实收金额
    function calcAmount() {
        let chargeItem = $('.chargeItem').val() || 0;
        let receivable = $('#receivable').val();
        if (receivable == '') {
            return false;
        }
        $('#amount').val(parseFloat(chargeItem) + parseFloat(receivable));
    }

    // 提交保存
    function saveOrUpdateHandler(){
        let validator = $('#saveForm').validate({ignore:''})
        if (!validator.form()) {
            $('.breadcrumb [data-toggle="collapse"]').html('收起 <i class="fa fa-angle-double-up" aria-hidden="true"></i>');
            $('.collapse:not(.show)').addClass('show');
            return false;
        }

        bui.loading.show('努力提交中，请稍候。。。');
        let _formData = $('#saveForm').serializeObject();
        let _url = null;

        //没有id就新增
        if (_formData.id == null || _formData.id == "") {
            _url = "${contextPath}/meterDetail/add.action";
        } else {//有id就修改
            _url = "${contextPath}/meterDetail/update.action";
        }
        $.ajax({
            type: "POST",
            url: _url,
            data: buildFormData(),
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


    //数据组装
    function buildFormData() {
        let _formData = $('#saveForm').serializeObject();
        // 月份补全
        _formData.usageTime = _formData.usageTime + "-01 00:00:00";
        // 部门名称
        let departmentName = $('#departmentId').find("option:selected").text();
        _formData.departmentName = departmentName;
        // 动态收费项
        let businessChargeDtos = []
        $('#saveForm').find('.chargeItem').each(function(){
            let businessCharge = {};
            businessCharge.chargeItemId=$(this).attr("name").split("_")[1];
            businessCharge.chargeItemName=$(this).attr("chargeItem");
            businessCharge.amount=parseInt($(this).val())*100;
            businessCharge.id=$(this).attr("item-id");
            if (businessCharge != {}) {
                businessChargeDtos.push(businessCharge);
            }
        })
        _formData.businessChargeItems = businessChargeDtos;

        // 金钱乘以100
        bui.util.yuanToCentForMoneyEl(_formData);

        return JSON.stringify(_formData)
    }
</script>