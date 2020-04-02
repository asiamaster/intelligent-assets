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
    let isInitCheckDeduction = ${isNotEmpty(leaseOrder) ? true : false};
    $.extend(customerNameAutoCompleteOption,{
        selectFn: function (suggestion) {
            $('#certificateNumber').val(suggestion.certificateNumber);
            $('#_certificateNumber').val(suggestion.certificateNumber);
            $('#customerCellphone').val(suggestion.contactsPhone);
            $("#_certificateNumber,#customerCellphone").valid();

            //获取保证金抵扣余额
            queryCustomerDepositDeduction(true);
            //账户余额查询
            queryCustomerAccount();
        }
    });
    $.extend(certificateNumberAutoCompleteOption,{
        selectFn: function (suggestion) {
            $('#customerName').val(suggestion.name);
            $('#customerId').val(suggestion.id);
            $('#customerCellphone').val(suggestion.contactsPhone);
            $("#customerName,#customerCellphone").valid();

            //获取保证金抵扣余额
            queryCustomerDepositDeduction(true);
            //账户余额查询
            queryCustomerAccount();
        }
    });

    //摊位搜索自动完成
    var boothAutoCompleteOption = {
        paramName: 'keyword',
        displayFieldName: 'name',
        serviceUrl: '/booth/search.action',
        selectFn: boothSelectHandler,
        transformResult: function (result) {
            if(result.success){
                let data = result.data;
                return {
                    suggestions: $.map(data, function (dataItem) {
                        return $.extend(dataItem, {
                                value: dataItem.name + '(' + (dataItem.secondAreaName?dataItem.secondAreaName : dataItem.areaName) + ')'
                            }
                        );
                    })
                }
            }else{
                bs4pop.alert(result.message, {type: 'error'});
                return;
            }
        }
    }
    /*********************变量定义区 end***************/


    /******************************驱动执行区 begin***************************/
    $(function () {
        //初始化刷卡
        initSwipeCard({
            id:'getCustomer',
            onLoadSuccess:function(customer){
                queryCustomerAccount();
                queryCustomerDepositDeduction(true);
            }
        });

        //监听客户注册
        initMsg(function(message){
            let msgData = JSON.parse(message);
            if(msgData.topic == 'customerRegister'){
                let customer = msgData.content;
                $('#certificateNumber').val(customer.certificateNumber);
                $('#_certificateNumber').val(customer.certificateNumber);
                $('#customerName').val(customer.name);
                $('#customerId').val(customer.customerId);
                $('#customerCellphone').val(customer.contactsPhone);
            }
        });

        laydate.render({
                elem: '#startTime',
                type: 'date',
                theme: '#007bff',
            <% if(isNotEmpty(isRenew) && isRenew == 1){ %>
                value: moment("${leaseOrder.endTime!,dateFormat='yyyy-MM-dd'}").add(1,"days").format("YYYY-MM-DD"),
            <% } else if(isEmpty(leaseOrder)){ %>
                value: new Date(),
            <% }%>
            done: function(value, date){
                $('#startTime').val(value);
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
                $('#endTime').val(value);
                endTimeChangeHandler();
                $("#saveForm").validate().element($("#endTime"));
                $("#saveForm").validate().element($("#days"));
            }
        });

        <% if(isNotEmpty(isRenew) && isRenew == 1){ %>
            //重新计算续租日期
            $('#endTime').val(moment($('#startTime').val()).add($('#days').val()-1,"days").format("YYYY-MM-DD"));
        <% } %>

        <% if(isNotEmpty(leaseOrderItems)){ %>
            let leaseOrderItems = JSON.parse('${leaseOrderItems}');
            for (let leaseOrderItem of leaseOrderItems){
                leaseOrderItem.rentAmount = leaseOrderItem.rentAmount.centToYuan();
                leaseOrderItem.manageAmount = leaseOrderItem.manageAmount.centToYuan();
                leaseOrderItem.depositAmount = leaseOrderItem.depositAmount.centToYuan();
                leaseOrderItem.unitPrice = leaseOrderItem.unitPrice.centToYuan();
                leaseOrderItem.discountAmount = leaseOrderItem.discountAmount.centToYuan();
                addBoothItem($.extend(leaseOrderItem,{index: ++itemIndex}));
            }
            queryCustomerAccount();
            queryCustomerDepositDeduction(true);
        <% }else{%>
            while (itemIndex<1) {
                addBoothItem({index: ++itemIndex});
            }
        <% }%>
    });
    /******************************驱动执行区 end****************************/

    /*****************************************函数区 begin************************************/

    //获取table Index
    function getIndex(str) {
        return str.split('_')[1];
    }

    /**
     * 添加摊位
     * @param leaseOrderItem
     */
    function addBoothItem(leaseOrderItem){
        $('#boothTable tbody').append(bui.util.HTMLDecode(template('boothItem',leaseOrderItem)))
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
        }
    }

    /**
     * 摊位选择事件Handler
     * */
    function boothSelectHandler(suggestion,element) {
        let index = getIndex($(element).attr('id'));
        $('#number_'+index).val(suggestion.number);
        $('#unitCode_'+index).val(suggestion.unit);
        $('#unitName_'+index).val(suggestion.unitName);
        $('#isCorner_'+index).val(suggestion.cornerName);
        $('#districtId_'+index).val(suggestion.secondArea?suggestion.secondArea : suggestion.area);
        $('#districtName_'+index).val(suggestion.secondAreaName?suggestion.secondAreaName : suggestion.areaName);

        queryCustomerDepositDeduction(true);
    }


    /**
     * 保证金可抵扣金额计算
     * @param (boolean) isCascadeCalc 是否级联计算所联动的金额
     * true：级联计算 false：不级联计算
     */
    function queryCustomerDepositDeduction(isCascadeCalc){
        let customerId = $('#customerId').val();
        let boothIds = $("table input[name^='boothId']").filter(function () {
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
                dataType: "json",
                async : false
            }).done(function (ret) {
                if (ret.success) {
                    let depositAmount = 0;
                    let sourceLeaseOrderItems = ret.data;
                    for(let item of sourceLeaseOrderItems){
                        depositAmount += item.depositAmount;
                    }
                    $("table input[name^='boothId']").each(function () {
                        let depositExist = false;
                        let trIndex = getIndex($(this).attr('id'));
                        for(let item of sourceLeaseOrderItems){
                            if(this.value == item.boothId){
                                $('#depositAmountSourceId_'+trIndex).val(item.id);
                                depositExist = true;
                                break;
                            }
                        }
                        if(!depositExist){
                            $('#depositAmountSourceId_'+trIndex).val('');
                        }

                    });
                    if(isInitCheckDeduction){
                        if(Number($('#depositDeduction').val()) != Number(depositAmount.centToYuan())){
                            bs4pop.notice('保证金可抵扣额发生变化,已为您调整至最新值！', {position: 'bottomleft',autoClose: false})
                        }
                        isInitCheckDeduction = false;
                    }
                    $('#depositDeduction').val(Number(depositAmount).centToYuan())
                } else {
                    bs4pop.alert(data.message, {type: 'error'});
                }
            }).fail(function () {
                bs4pop.alert('远程访问失败', {type: 'error'});
            }).done(function () {
                isCascadeCalc && calcPayAmount();
            });
        }else{
            $('#depositDeduction').val('0.00');
        }

    }

    /**
     * 账户余额查询
     * */
    function queryCustomerAccount(){
        let customerId = $('#customerId').val();
        if(!customerId) return;
        $.ajax({
            type: "get",
            url: "/customerAccount/getCustomerAccountByCustomerId.action",
            data: {customerId},
            dataType: "json",
            async : false,
            success: function (ret) {
                if(ret.success){
                    let earnestDeductionEl$ = $('#earnestDeduction');
                    let transferDeductionEl$ = $('#transferDeduction');
                    let earnestAvailableBalance = 0;
                    let transferAvailableBalance = 0;
                    if(ret.data){
                        let data = ret.data;
                        earnestAvailableBalance = Number(data.earnestAvailableBalance).centToYuan();
                        transferAvailableBalance = Number(data.transferAvailableBalance).centToYuan();
                        if(isInitCheckDeduction){
                            if(Number(earnestDeductionEl$.val()) > earnestAvailableBalance){
                                earnestDeductionEl$.val(earnestAvailableBalance);
                                bs4pop.notice('定金可抵扣额小于之前设置金额,已为您调整至最大抵扣额！', {position: 'bottomleft',autoClose: false})
                            }
                            if(Number(transferDeductionEl$.val()) > transferAvailableBalance){
                                transferDeductionEl$.val(transferAvailableBalance);
                                bs4pop.notice('转低可抵扣额小于之前设置金额,已为您调整至最大抵扣额！', {position: 'bottomleft',autoClose: false})
                            }
                        }else{
                            earnestDeductionEl$.val('');
                            $('#transferDeduction').val('');
                        }
                    }
                    earnestDeductionEl$.attr('max',earnestAvailableBalance);
                    $('#earnestAmount').text('余额'+earnestAvailableBalance);
                    transferDeductionEl$.attr('max',transferAvailableBalance);
                    $('#transferAmount').text('余额'+transferAvailableBalance);
                }
            },
            error: function (a, b, c) {
                bs4pop.alert('远程访问失败', {type: 'error'});
            }
        });
    }

    /**
     * 计算租金
     * @param (boolean) isCascadeCalc 是否级联计算所联动的金额
     * true：级联计算 false：不级联计算
     * */
    function calcRentAmount(isCascadeCalc){
        let rentAmount = 0;
        $("table input[name^='rentAmount_']").filter(function () {
            return this.value
        }).each(function (i) {
            rentAmount = Number(this.value).add(rentAmount);
        });
        $('#rentAmount').val(rentAmount.toFixed(2));

        isCascadeCalc && calcTotalAmount(isCascadeCalc);
    }

    /**
     * 计算物业管理费
     * @param (boolean) isCascadeCalc 是否级联计算所联动的金额
     * true：级联计算 false：不级联计算
     * */
    function calcManageAmount(isCascadeCalc){
        let manageAmount = 0;
        $("table input[name^='manageAmount_']").filter(function () {
            return this.value
        }).each(function (i) {
            manageAmount = Number(this.value).add(manageAmount);
        });
        $('#manageAmount').val(manageAmount.toFixed(2));

        isCascadeCalc && calcTotalAmount(isCascadeCalc);
    }

    /**
     * 计算保证金
     * @param (boolean) isCascadeCalc 是否级联计算所联动的金额
     * true：级联计算 false：不级联计算
     * */
    function calcDepositAmount(isCascadeCalc){
        let depositAmount = 0;
        $("table input[name^='depositAmount_']").filter(function () {
            return this.value;
        }).each(function (i) {
            depositAmount = Number(this.value).add(depositAmount);
        });
        $('#depositAmount').val(depositAmount.toFixed(2));

        isCascadeCalc && calcTotalAmount(isCascadeCalc);
    }

    /**
     * 计算合计金额
     * @param (boolean) isCascadeCalc 是否级联计算所联动的金额
     * true：级联计算 false：不级联计算
     *
     * */
    function calcTotalAmount(isCascadeCalc){
        let rentAmount = Number($('#rentAmount').val());
        let manageAmount = Number($('#manageAmount').val());
        let depositAmount = Number($('#depositAmount').val());
        $('#totalAmount').val((rentAmount.mul(100) + manageAmount.mul(100) + depositAmount.mul(100)).centToYuan());

        isCascadeCalc && calcPayAmount(isCascadeCalc);
    }

    /**
     * 计算实付金额
     * */
    function calcPayAmount() {
        let depositDeduction = Number($('#depositDeduction').val());
        let earnestDeduction = Number($('#earnestDeduction').val());
        let transferDeduction = Number($('#transferDeduction').val());
        let totalAmount = Number($('#totalAmount').val());
        if(Number.isFinite(earnestDeduction) && Number.isFinite(transferDeduction)){
            $('#payAmount').val((totalAmount.mul(100)- depositDeduction.mul(100) - earnestDeduction.mul(100) - transferDeduction.mul(100)).centToYuan());
        }
    }

    /**
     * 构建摊位租赁表单提交数据
     * @returns {{}|jQuery}
     */
    function buildFormData(){
        let formData = $("input:not(table input),textarea,select").serializeObject();
        let leaseOrderItems = [];
        let leaseTermName = $('#leaseTermCode').find("option:selected").text();
        let engageName = $('#engageCode').find("option:selected").text();
        let departmentName = $('#departmentId').find("option:selected").text();

        bui.util.yuanToCentForMoneyEl(formData);
        $("#boothTable tbody").find("tr").each(function(){
            let leaseOrderItem = {};
            $(this).find("input").each(function(t,el){
                let fieldName = $(this).attr("name").split('_')[0];
                leaseOrderItem[fieldName] = $(this).hasClass('money')? Number($(this).val()).mul(100) : $(this).val();
            });
            leaseOrderItems.push(leaseOrderItem);
        });

        $.extend(formData, {
            leaseOrderItems,
            leaseTermName,
            engageName,
            departmentName,
            logContent: $('#id').val() ? Log.buildUpdateContent() : '',
            operationType: $('#id').val() ? 'edit' : 'add'
        });
        return formData;
    }

    /**
     * 判断数组中的元素是否重复出现
     * 验证重复元素，有重复返回true；否则返回false
     * @param arr
     * @returns {boolean}
     */
    function arrRepeatCheck(arr) {
        var hash = {};
        for(var i in arr) {
            if(hash[arr[i]]) {
                return true;
            }
            // 不存在该元素，则赋值为true，可以赋任意值，相应的修改if判断条件即可
            hash[arr[i]] = true;
        }
        return false;
    }

    /*****************************************函数区 end**************************************/

    /*****************************************自定义事件区 begin************************************/
    $('#formSubmit').on('click', function (e) {
        if (!$('#saveForm').valid()) {
            return false;
        }

        let boothIds = $("table input[name^='boothId']").filter(function () {
            return this.value
        }).map(function(){
            return $('#boothId_'+getIndex(this.id)).val();
        }).get();

        if(!boothIds || boothIds.length == 0){
            bs4pop.alert('请添加摊位！')
            return false;
        }

        if(arrRepeatCheck(boothIds)){
            bs4pop.alert('存在重复摊位，请检查！')
            return false;
        }

        if(boothIds.length > 10){
            bs4pop.notice('最多10个摊位', {position: 'leftcenter', type: 'warning'});
        }

        bui.loading.show();
        $.ajax({
            type: "POST",
            url: "/leaseOrder/saveLeaseOrder.action",
            data: buildFormData(),
            dataType: "json",
            async : false,
            success: function (ret) {
                bui.loading.hide();
                if(!ret.success){
                    bs4pop.alert(ret.message, {type: 'error'});
                }else{
                    parent.closeDialog(parent.dia);
                }
            },
            error: function (a, b, c) {
                bs4pop.alert('远程访问失败', {type: 'error'});
            }
        });
    });

    //摊位新增事件
    $('#addBooth').on('click', function(){
        addBoothItem({index: ++itemIndex});
    });

    //摊位删除事件
    $(document).on('click', '.item-del', function () {
        if ($('#boothTable tr').length > 1) {
            $(this).closest('tr').remove();

            queryCustomerDepositDeduction();
            calcRentAmount();
            calcDepositAmount();
            calcManageAmount();
            calcTotalAmount(true);
        }
    });

    /*****************************************自定义事件区 end**************************************/
</script>