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


    $('#tollAmount').on('change', function () {
        tollAmount();
    })

    // 计算实收金额
    function tollAmount() {
        let tollAmount = $('#tollAmount').val();
        if (tollAmount == '') {
            return false;
        }
        $('#amount').val(parseFloat(tollAmount));
    }

    $('#validPeriod, #startTime').on('change', function () {
        startTime();
    })

    // 计算结束日期
    function startTime() {
        let startTime = $('#startTime').val();
        let validPeriod = $('#validPeriod').val();
        if (validPeriod == ${@com.dili.ia.glossary.PassportPeriodEnum.ONE_MONTH.getCode()}) {
            startTime.addMonths(startTime, 1);
        } else if ( validPeriod == ${@com.dili.ia.glossary.PassportPeriodEnum.TWO_MONTH.getCode()}) {
            startTime.addMonths(startTime, 2);
        } else if ( validPeriod == ${@com.dili.ia.glossary.PassportPeriodEnum.QUARTERLY.getCode()}) {
            startTime.addMonths(startTime, 3);
        } else if ( validPeriod == ${@com.dili.ia.glossary.PassportPeriodEnum.HALF_YEAR.getCode()}) {
            startTime.addMonths(startTime, 6);
        } else if ( validPeriod == ${@com.dili.ia.glossary.PassportPeriodEnum.ONE_YEAR.getCode()}) {
            startTime.addMonths(startTime, 12);
        }
        $('#endTime').val(parseFloat(startTime));
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
            _url = "${contextPath}/passport/add.action";
        } else {//有id就修改
            _url = "${contextPath}/passport/update.action";
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

        // 部门名称
        let departmentName = $('#departmentId').find("option:selected").text();
        _formData.departmentName = departmentName;

        // 金钱乘以100
        bui.util.yuanToCentForMoneyEl(_formData);

        return JSON.stringify(_formData)
    }
</script>