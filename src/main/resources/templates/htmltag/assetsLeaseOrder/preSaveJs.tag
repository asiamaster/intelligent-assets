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

            //账户余额查询
            queryCustomerAccount();
            calcTotalAmount(true);
        }
    });
    $.extend(certificateNumberAutoCompleteOption,{
        selectFn: function (suggestion) {
            $('#customerName').val(suggestion.name);
            $('#customerId').val(suggestion.customerId);
            $('#customerCellphone').val(suggestion.contactsPhone);
            $("#customerName,#customerCellphone").valid();

            //账户余额查询
            queryCustomerAccount();
            calcTotalAmount(true);
        }
    });

    //资产搜索自动完成
    var assetAutoCompleteOption = {
        paramName: 'keyword',
        displayFieldName: 'name',
        serviceUrl: '/assets/searchAssets.action',
        selectFn: assetSelectHandler,
        transformResult: function (result) {
            if(result.success){
                let data = result.data;
                return {
                    suggestions: $.map(data, function (dataItem) {
                        return $.extend(dataItem, {
                            value: dataItem.name + '(' + (dataItem.secondAreaName ? dataItem.areaName + '->' + dataItem.secondAreaName : dataItem.areaName) + ')'
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

    //品类搜索自动完成
    var categoryAutoCompleteOption = {
        width: '100%',
        language: 'zh-CN',
        maximumSelectionLength: 10,
        ajax: {
            type:'get',
            url: '/assets/searchCategory.action',
            data: function (params) {
                return {
                    keyword: params.term,
                }
            },
            processResults: function (result) {
                if(result.success){
                    let data = result.data;
                    return {
                        results: $.map(data, function (dataItem) {
                            dataItem.text = dataItem.name + (dataItem.cusName ? '(' + dataItem.cusName + ')' : '');
                            return dataItem;
                        })
                    };
                }else{
                    bs4pop.alert(result.message, {type: 'error'});
                    return;
                }
            }
        }
    }

    //品类搜索自动完成
    var userAutoCompleteOption = {
        width: '100%',
        language: 'zh-CN',
        minimumInputLength: 1,
        //格式化结果
        templateResult: function (dataItem) {
            return dataItem.text + (dataItem.cellphone ? '(' + dataItem.cellphone + ')' : '')
        },
        ajax: {
            type:'get',
            url: '/leaseOrder/queryUsers.action',
            data: function (params) {
                return {
                    keyword: params.term,
                }
            },
            processResults: function (result) {
                if(result.success){
                    let data = result.data;
                    return {
                        results: $.map(data, function (dataItem) {
                            dataItem.text = dataItem.realName;
                            return dataItem;
                        })
                    };
                }else{
                    bs4pop.alert(result.message, {type: 'error'});
                    return;
                }
            }
        }
    }
    /*********************变量定义区 end***************/


    /******************************驱动执行区 begin***************************/
    $(function () {
        //初始化刷身份证
        initSwipeIdCard({
            id:'getCustomer',
            onLoadSuccess:function(customer){
                //账户余额查询
                queryCustomerAccount();
                calcTotalAmount(true);
            }
        });

        //初始化刷园区卡
        initSwipeParkCard({
            id:'icReader',
            onLoadSuccess:function(customer){
                $('#customerName').val(customer.name);
                $('#customerId').val(customer.customerId);
                $('#certificateNumber,#_certificateNumber').val(customer.customerCertificateNumber);
                $('#customerCellphone').val(customer.customerContactsPhone);
                //账户余额查询
                queryCustomerAccount();
                calcTotalAmount(true);
            }
        });

        //监听客户注册
        registerMsg();

        <% if(isNotEmpty(isRenew) && isRenew == 1){ %>
            let startTime = moment("${leaseOrder.endTime!,localDateTimeFormat='yyyy-MM-dd'}").add(1,"days").format("YYYY-MM-DD");
            //重新计算续租日期
            $('#endTime').val(moment(startTime).add($('#days').val()-1,"days").format("YYYY-MM-DD"));
        <% } %>

        laydate.render({
                elem: '#startTime',
                type: 'date',
                theme: '#007bff',
                trigger:'click',
            <% if(isNotEmpty(isRenew) && isRenew == 1){ %>
                value: startTime,
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
            trigger:'click',
            done: function(value, date){
                $('#endTime').val(value);
                endTimeChangeHandler();
                $("#saveForm").validate().element($("#endTime"));
                $("#saveForm").validate().element($("#days"));
            }
        });

        <% if(isNotEmpty(leaseOrderItems)){ %>
            itemIndex += ${leaseOrderItems.~size};
        <% }else{%>
            while (itemIndex<1) {
                addBoothItem({index: ++itemIndex});
            }
        <% }%>

        //账户余额查询
        queryCustomerAccount();


        let assetsIds = $("table input[name^='assetsId']").filter(function () {
            return this.value
        }).map(function () {
            return this.value
        }).get();
        if(assetsIds.length > 0){
            batchQueryDepositBalance($('#assetsType').val(), $('#customerId').val(), assetsIds);
            $('#id').val() && batchQueryDepositOrder({businessId: $('#id').val(), bizType: $('#bizType').val()});
        }
        calcTotalAmount(true);
    });
    /******************************驱动执行区 end****************************/

    /*****************************************函数区 begin************************************/

    //获取table Index
    function getIndex(str) {
        return str.split('_')[1];
    }

    /**
     * 添加资产
     * @param leaseOrderItem
     */
    function addBoothItem(leaseOrderItem){
        $('#assetTable tbody').append(bui.util.HTMLDecode(template('assetItem',leaseOrderItem)))
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
     * 资产选择事件Handler
     * */
    function assetSelectHandler(suggestion,element) {
        let index = getIndex($(element).attr('id'));
        $('#number_'+index).val(suggestion.number);
        $('#unitCode_'+index).val(suggestion.unit);
        $('#unitName_'+index).val(suggestion.unitName);
        $('#sku_'+index).val(suggestion.number+suggestion.unitName);
        $('#isCorner_'+index).val(suggestion.cornerName);
        $('#districtId_'+index).val(suggestion.secondArea?suggestion.secondArea : suggestion.area);
        $('#districtName_' + index).val(suggestion.secondAreaName ? suggestion.areaName + '->' + suggestion.secondAreaName : suggestion.areaName);
        batchQueryDepositBalance($('#assetsType').val(),$('#customerId').val(),[suggestion.id]);
        $('#id').val() && batchQueryDepositOrder({
            businessId: $('#id').val(),
            bizType: $('#bizType').val(),
            assetsId: suggestion.id
        });
    }

    /**
     * 保证金余额查询
     * @param assetsType
     * @param customerId
     * @param assetsIds
     */
    function batchQueryDepositBalance(assetsType,customerId, assetsIds) {
        $.ajax({
            type: "post",
            url: "/leaseOrder/batchQueryDepositBalance.action",
            data: JSON.stringify({assetsType,customerId,assetsIds}),
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            success: function (ret) {
                if(ret.success){
                    let depositBalances = ret.data;
                    if(depositBalances.length > 0){
                        for (let depositBalance of depositBalances){
                            let index = getIndex($("table input.assets[value='"+depositBalance.assetsId+"']").attr('id'));
                            $('#depositBalance_'+index).val(Number(depositBalance.balance).centToYuan());
                        }
                    }
                }
            },
            error: function (a, b, c) {
                bs4pop.alert('远程访问失败', {type: 'error'});
            }
        });
    }

    /**
     * 批量订单项保证金（补交）查询
     * @param {businessId: leaseOrderId,assetsId:123}
     */
    function batchQueryDepositOrder(depositOrderQuery) {
        $.ajax({
            type: "post",
            url: "/leaseOrder/batchQueryDepositOrder.action",
            data: depositOrderQuery,
            dataType: "json",
            async: false,
            success: function (ret) {
                if(ret.success){
                    let depositOrders = ret.data;
                    if(depositOrders.length > 0){
                        for (let depositOrder of depositOrders){
                            let index = getIndex($("table input.assets[value='"+depositOrder.assetsId+"']").attr('id'));
                            if(depositOrder.state != ${@com.dili.ia.glossary.DepositOrderStateEnum.CREATED.getCode()}){
                                $('#depositMakeUpAmount_'+index).val(Number(depositOrder.amount).centToYuan()).attr('readonly',true);
                            }else{
                                $('#depositMakeUpAmount_'+index).val(Number(depositOrder.amount).centToYuan());
                            }
                        }
                    }
                    calcTotalAmountAndDeposit(true);
                }
            },
            error: function (a, b, c) {
                bs4pop.alert('远程访问失败', {type: 'error'});
            }
        });
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
     * 计算实付金额
     * @param isCascadeCalc 是否级联计算加保证金后的实付金额
     * */
    function calcPayAmount(isCascadeCalc) {
        let earnestDeduction = Number($('#earnestDeduction').val());
        let transferDeduction = Number($('#transferDeduction').val());
        let totalAmount = Number($('#totalAmount').val());
        if(Number.isFinite(earnestDeduction) && Number.isFinite(transferDeduction)){
            $('#payAmount').val((totalAmount.mul(100) - earnestDeduction.mul(100) - transferDeduction.mul(100)).centToYuan());
        }

        isCascadeCalc && calcPayAmountAndDeposit();
    }

    /**
     * 计算实付金额+补交保证金
     * */
    function calcPayAmountAndDeposit() {
        let earnestDeduction = Number($('#earnestDeduction').val());
        let transferDeduction = Number($('#transferDeduction').val());
        let totalAmountAndDeposit = Number($('#totalAmountAndDeposit').val());
        if(Number.isFinite(earnestDeduction) && Number.isFinite(transferDeduction)){
            $('#payAmountAndDeposit').val((totalAmountAndDeposit.mul(100) - earnestDeduction.mul(100) - transferDeduction.mul(100)).centToYuan());
        }
    }

    /**
     * 计算合计金额
     * @param isCascadeCalc 是否级联计算加保证金后的合计金额
     */
    function calcTotalAmount(isCascadeCalc) {
        let totalAmount = 0;
        $("table input[isCharge]").filter(function () {
            return this.value;
        }).each(function (i) {
            totalAmount = Number(this.value).add(totalAmount);
        });
        $('#totalAmount').val(totalAmount.toFixed(2));

        isCascadeCalc && calcPayAmount();
        calcTotalAmountAndDeposit(true);
    }

    /**
     * 计算合计金额+补交保证金
     * @param isCascadeCalc 是否级联计算加保证金后的实付金额
     */
    function calcTotalAmountAndDeposit(isCascadeCalc) {
        let totalAmount = depositMakeUpAmount = 0;
        $("table input[isCharge]").filter(function () {
            return this.value;
        }).each(function (i) {
            totalAmount = Number(this.value).add(totalAmount);
        });
        $("table input[isDeposit]").filter(function () {
            return this.value;
        }).each(function (i) {
            depositMakeUpAmount = Number(this.value).add(depositMakeUpAmount);
        });
        $('#totalAmountAndDeposit').val(totalAmount.add(depositMakeUpAmount).toFixed(2));

        isCascadeCalc && calcPayAmountAndDeposit();
    }


    /**
     * 构建资产租赁表单提交数据
     * @returns {{}|jQuery}
     */
    function buildFormData(){
        let formData = $("input:not(table input),textarea,select").serializeObject();
        let leaseOrderItems = [];
        let leaseTermName = $('#leaseTermCode').find("option:selected").text();
        let engageName = $('#engageCode').find("option:selected").text();
        let departmentName = $('#departmentId').find("option:selected").text();

        bui.util.yuanToCentForMoneyEl(formData);
        $("#assetTable tbody").find("tr").each(function(){
            let leaseOrderItem = {};
            leaseOrderItem.businessChargeItems = [];
            $(this).find("input").each(function(t,el){
                let nameArr = $(this).attr("name").split('_');
                let fieldName = nameArr[0];
                if(fieldName.includes("chargeItem")){
                    let businessChargeItem = {};
                    businessChargeItem.chargeItemName = this.dataset.chargeItemName;
                    businessChargeItem.chargeItemId = this.dataset.chargeItemId;
                    businessChargeItem.amount = Number($(this).val()).mul(100);
                    leaseOrderItem.businessChargeItems.push(businessChargeItem);
                }else{
                    leaseOrderItem[fieldName] = $(this).hasClass('money')? Number($(this).val()).mul(100) : $(this).val();
                }
            });
            leaseOrderItem.assetsType = $('#assetsType').val();
            leaseOrderItem.bizType = $('#bizType').val();
            leaseOrderItems.push(leaseOrderItem);
        });

        $.extend(formData, {
            leaseOrderItems,
            leaseTermName,
            engageName,
            departmentName,
            categorys: $.map($('#categorys').select2('data'), function (dataItem) {
                return {id: dataItem.id, text: dataItem.text}
            }),
            logContent: $('#id').val() ? Log.buildUpdateContent() : ''
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

    /**
     * 表单baocun
     * @returns {boolean}
     */
    function saveFormHandler(){
        let validator = $('#saveForm').validate({ignore: ''})
        if (!validator.form()) {
            $('.breadcrumb [data-toggle="collapse"]').html('收起 <i class="fa fa-angle-double-up" aria-hidden="true"></i>');
            $('.collapse:not(.show)').addClass('show');
            return false;
        }

        let assetsIds = $("table input[name^='assetsId']").filter(function () {
            return this.value
        }).map(function () {
            return $('#assetsId_' + getIndex(this.id)).val();
        }).get();

        if (assetsIds.length == 0) {
            bs4pop.notice('请添加资产！', {position: 'leftcenter', type: 'danger'});
            return false;
        }

        if (arrRepeatCheck(assetsIds)) {
            bs4pop.notice('存在重复资产，请检查！', {position: 'leftcenter', type: 'danger'});
            return false;
        }

        if (assetsIds.length > 10) {
            bs4pop.notice('最多10个资产', {position: 'leftcenter', type: 'danger'});
            return false;
        }

        let earnestDeduction = Number($('#earnestDeduction').val());
        let transferDeduction = Number($('#transferDeduction').val());
        let totalAmount = Number($('#totalAmount').val());
        if (totalAmount < earnestDeduction.add(transferDeduction)) {
            bs4pop.notice('抵扣金额之和不能大于租赁费用之和', {position: 'leftcenter', type: 'danger'});
            return false;
        }

        bui.loading.show('努力提交中，请稍候。。。');
        $.ajax({
            type: "POST",
            url: "/leaseOrder/saveLeaseOrder.action",
            data: JSON.stringify(buildFormData()),
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            success: function (ret) {
                if(!ret.success){
                    bs4pop.alert(ret.message, {type: 'error'});
                }else{
                    parent.closeDialog(parent.dia);
                }
                bui.loading.hide();
            },
            error: function (a, b, c) {
                bui.loading.hide();
                bs4pop.alert('远程访问失败', {type: 'error'});
            }
        });
    }

    /*****************************************函数区 end**************************************/

    /*****************************************自定义事件区 begin************************************/
    //资产新增事件
    $('#addBooth').on('click', function(){
        if ($('#assetTable tr').length < 11) {
            addBoothItem({index: ++itemIndex});
        } else {
            bs4pop.notice('最多10个资产', {position: 'leftcenter', type: 'danger'})
        }
    });

    //资产删除事件
    $(document).on('click', '.item-del', function () {
        if ($('#assetTable tr').length > 1) {
            $(this).closest('tr').remove();
            calcTotalAmount(true);
        }
    });

    $('#save').on('click', bui.util.debounce(saveFormHandler,1000,true));

    $('#managerId').on('select2:select', function (e) {
        var data = e.params.data;
        $('#manager').val(data.text);
    });

    /*****************************************自定义事件区 end**************************************/
</script>