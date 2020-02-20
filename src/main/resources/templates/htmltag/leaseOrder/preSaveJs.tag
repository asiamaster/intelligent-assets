<script>
    /**
     *
     * @Date 2019-11-06 17:30:00
     * @author jiangchengyong
     *
     ***/

    /*********************变量定义区 begin*************/
        //行索引计数器
        let itemIndex = 0;
        var customerNameAutoCompleteOption = {
            width : 350,
            serviceUrl: '/customer/list.action',
            paramName : 'name',
            displayFieldName : 'name',
            transformResult: function (result) {
                return {
                    suggestions: $.map(result, function (dataItem) {
                        return $.extend(dataItem, {
                                value: dataItem.name + ' ' + dataItem.certificateNumber + ' ' + dataItem.cellphone
                            }
                        );
                    })
                }
            },
            selectFn: function (suggestion) {
                $('#certificateNumber').val(suggestion.certificateNumber);
                $('#_certificateNumber').val(suggestion.certificateNumber);
                $('#customerCellphone').val(suggestion.cellphone);

                //获取保证金抵扣余额
                getCustomerDepositAmount();
            }
        };
        var certificateNumberAutoCompleteOption = {
            width : 350,
            serviceUrl: '/customer/list.action',
            paramName : 'certificateNumber',
            displayFieldName : 'certificateNumber',
            transformResult: function (result) {
                return {
                    suggestions: $.map(result, function (dataItem) {
                        return $.extend(dataItem, {
                                value: dataItem.name + ' ' + dataItem.certificateNumber + ' ' + dataItem.cellphone
                            }
                        );
                    })
                }
            },
            selectFn: function (suggestion) {
                $('#customerName').val(suggestion.name);
                $('#customerId').val(suggestion.id);
                $('#customerCellphone').val(suggestion.cellphone);

                //获取保证金抵扣余额
                getCustomerDepositAmount();
            }
        };

    /*********************变量定义区 end***************/


    /******************************驱动执行区 begin***************************/
    $(function () {
        laydate.render({
            elem: '#startTime',
            type: 'date',
            theme: '#007bff',
            done: function(value, date){
                startTimeChangeHandler();
                $("#saveForm").validate().element($("#startTime"));
                $("#saveForm").validate().element($("#days"));
            }
        });
        laydate.render({
            elem: '#endTime',
            type: 'date',
            theme: '#007bff',
            done: function(value, date){
                endTimeChangeHandler();
                $("#saveForm").validate().element($("#endTime"));
                $("#saveForm").validate().element($("#days"));
            }
        });

        addBoothItem();
    });
    /******************************驱动执行区 end****************************/

    /*****************************************函数区 begin************************************/

    //HTML反转义
    function HTMLDecode(str)
    {
        var s = "";
        if (str.length == 0) return "";
        s = str.replace(/&amp;/g, "&");
        s = s.replace(/&lt;/g, "<");
        s = s.replace(/&gt;/g, ">");
        s = s.replace(/&nbsp;/g, " ");
        s = s.replace(/&#39;/g, "\'");
        s = s.replace(/&quot;/g, "\"");
        s = s.replace(/<br\/>/g, "\n");
        return s;
    }

    //获取table Index
    function getIndex(str) {
        return str.split('_')[1];
    }

    /**
     * 读身份证卡
     * @return {IDCardNo:'5116021989...'}
     * */
    function reader(){
        if(!window.callbackObj)return ;
        return eval('(' + callbackObj.readIDCard() + ')');
    }

    /**
     * 添加摊位
     * */
    function addBoothItem(){
        $('#boothTable tbody').append(HTMLDecode(template('boothItem',{index:++itemIndex})))
    }

    /**
     * 天数改变处理Handler
     * 三者相互联动；三者都有值情况，修改天数，变结束；修改开始或者结束，都变天数
     * */
    function daysChangeHandler(){
        let days = $('#days').val();
        let startTime = $('#startTime').val();
        let endTime = $('#endTime').val();
        if(days){
            //天数变更优先计算结束日期
            if(startTime){
                $('#endTime').val(moment(startTime).add(days-1,"days").format("YYYY-MM-DD"));
                $("#saveForm").validate().element($("#endTime"));
                return;
            }

            if(endTime){
                $('#startTime').val(moment(endTime).subtract(days-1,"days").format("YYYY-MM-DD"));
                $("#saveForm").validate().element($("#startTime"));
                return;
            }
        }
    }

    /**
     * 开始日期值改变处理Handler
     * 三者相互联动；三者都有值情况，修改天数，变结束；修改开始或者结束，都变天数
     * */
    function startTimeChangeHandler(){
        let days = $('#days').val();
        let startTime = $('#startTime').val();
        let endTime = $('#endTime').val();
        if(startTime && !moment(startTime,"YYYY-MM-DD",true).isValid()){
            $('#days').val('');
            return;
        }
        if(startTime){
            //开始结束日期变更优先计算天数
            if(endTime){
                $('#days').val(moment(endTime).diff(moment(startTime),'days') + 1);
                $("#saveForm").validate().element($("#days"));
                return;
            }

            if(days){
                $('#endTime').val(moment(startTime).add(days-1,"days").format("YYYY-MM-DD"));
                return;
            }
        }else{
            if(endTime && days){
                $('#startTime').val(moment(endTime).subtract(days-1,"days").format("YYYY-MM-DD"));
            }
        }
    }

    /**
     * 结束日期值改变处理Handler
     * 三者相互联动；三者都有值情况，修改天数，变结束；修改开始或者结束，都变天数
     * */
    function endTimeChangeHandler(){
        let days = $('#days').val();
        let startTime = $('#startTime').val();
        let endTime = $('#endTime').val();
        if(endTime && !moment(endTime,"YYYY-MM-DD",true).isValid()){
            $('#days').val('');
            return;
        }
        if(endTime){
            //开始结束日期变更优先计算天数
            if(startTime){
                $('#days').val(moment(endTime).diff(moment(startTime),'days') + 1);
                return;
            }
            if(days){
                $('#startTime').val(moment(endTime).subtract(days-1,"days").format("YYYY-MM-DD"));
                return;
            }
        }else{
            if(startTime && days){
                $('#endTime').val(moment(startTime).add(days-1,"days").format("YYYY-MM-DD"));
            }
        }
    }

    /**
     * 摊位选择事件Handler
     * */
    function boothSelectHandler() {
        getCustomerDepositAmount();
    }


    /**
     * 保证金可抵扣金额计算
     * */
    function getCustomerDepositAmount(){
        let customerId = $('#customerId').val();
        let boothIds = $("table input[name='boothId']").filter(function () {
            return this.value
        }).map(function(){
            return $('#boothId_'+getIndex(this.id)).val();
        }).get();
        if(customerId && boothIds && boothIds.length > 0){
            $.ajax({
                type: "POST",
                url: '/leaseOrderItem/list.action',
                data: JSON.stringify({customerId,boothIds}),
                contentType: "application/json; charset=utf-8",
                dataType: "json"
            }).done(function (data) {
                if (data.code == "200") {
                    let depositAmount = 0;
                    $.each(data.data,function (index,item) {
                        depositAmount += item.depositAmount;
                    });
                    $('#depositDeduction').val(Number(depositAmount).div(100))
                } else {
                    bs4pop.alert(data.result, {type: 'error'});
                }
            }).fail(function () {
                bs4pop.alert('远程访问失败', {type: 'error'});
            }).done(function () {
                let depositDeduction = Number($('#depositDeduction').val());
                let earnestDeduction = Number($('#earnestDeduction').val());
                let transferDeduction = Number($('#transferDeduction').val());
                let totalAmount = Number($('#totalAmount').val());
                if(Number.isFinite(earnestDeduction) && Number.isFinite(transferDeduction)){
                    $('#payAmount').val((totalAmount.mul(100)- depositDeduction.mul(100) - earnestDeduction.mul(100) - transferDeduction.mul(100)).div(100));
                }
            });
        }

    }

    /*****************************************函数区 end**************************************/

    /*****************************************自定义事件区 begin************************************/
    $('#formSubmit').on('click', function (e) {
        if (!$('#saveForm').valid()) {
            return false;
        }
        parent.dia.hide();
    });

    $('#getCustomer').on('click', function (e) {
        e.stopPropagation();
        let user = reader();
        $.ajax({
            type: "POST",
            url: "/customer/list.action",
            // data: {certificateNumber : user.IDCardNo},
            dataType: "json",
            success: function (data) {
                $('#customerName').val(data[0].name);
                $('#customerId').val(data[0].id);
                $('#certificateNumber').val(data[0].certificateNumber);
                $('#_certificateNumber').val(data[0].certificateNumber);
                $('#customerCellphone').val(data[0].cellphone);
            },
            error: function (a, b, c) {
                bs4pop.alert('远程访问失败', {type: 'error'});
            }
        });

    })

    $('#addBooth').on('click', function(){
        addBoothItem();
    })

    //删除行事件
    $(document).on('click', '.item-del', function () {
        if ($('#boothTable tr').length > 2) {
            $(this).closest('tr').remove();
        }
    });

    /*****************************************自定义事件区 end**************************************/
</script>