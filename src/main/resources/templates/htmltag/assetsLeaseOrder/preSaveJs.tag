<script>
    /**
     *
     * @Date 2019-11-06 17:30:00
     * @author jiangchengyong
     *
     ***/

    /*********************变量定义区 begin*************/
    let itemIndex = 0;//行索引计数器
    $.extend(customerNameAutoCompleteOption,{
        selectFn: function (suggestion) {
            $('#certificateNumber').val(suggestion.certificateNumber);
            $('#_certificateNumber').val(suggestion.certificateNumber);
            $('#customerCellphone').val(suggestion.contactsPhone);
            $("#_certificateNumber,#customerCellphone").valid();
            calcTotalAmount(true);
        }
    });
    $.extend(certificateNumberAutoCompleteOption,{
        selectFn: function (suggestion) {
            $('#customerName').val(suggestion.name);
            $('#customerId').val(suggestion.customerId);
            $('#customerCellphone').val(suggestion.contactsPhone);
            $("#customerName,#customerCellphone").valid();

            calcTotalAmount(true);
        }
    });

    //资产搜索自动完成
    var assetAutoCompleteOption = {
        width: '100%',
        language: 'zh-CN',
        minimumInputLength: 1,
        maximumSelectionLength: 10,
        ajax: {
            type:'get',
            url : function(params){
                let bizType = $('#bizType').val();
                let mchId = $('#mchId').val();
                let batchId = $('#batchId').val();
                let index = getIndex($(this).attr('id'));
                let leaseOrderItems = buildLeaseOrderItems();
                if (leaseOrderItems.length == 0 || (leaseOrderItems.length == 1 && leaseOrderItems[0].itemIndex == index)) {
                    return '/assets/searchAssets.action';
                } else if(!batchId) {
                    if (bizType == ${@com.dili.ia.glossary.BizTypeEnum.BOOTH_LEASE.getCode()}) {
                        params.isExcludeRental = true;
                    }
                    params.mchId = mchId;
                    return '/assets/searchAssets.action';
                } else {
                    params.mchId = mchId;
                    params.batchId = batchId;
                    return '/assetsRental/listRentalsByRentalDtoAndKeyWord.action';
                }
            },
            data: function (params) {
                return {
                    keyword: params.term,
                    assetsType: $('#assetsType').val(),
                    ...params
                }
            },
            processResults: function (result) {
                if(result.success){
                    let data = result.data;
                    return {
                        results: $.map(data, function (dataItem) {
                            return $.extend(dataItem, {
                                    id: dataItem.assetsId || dataItem.id,//预设池则取资产ID 基础信息则取ID
                                    name: dataItem.assetsName || dataItem.name,
                                    text: (dataItem.assetsName || dataItem.name) + '(' + ((dataItem.secondAreaName || dataItem.secondDistrictName) ? (dataItem.areaName || dataItem.firstDistrictName) + '->' + (dataItem.secondAreaName || dataItem.secondDistrictName) : (dataItem.areaName || dataItem.firstDistrictName)) + ')'
                                }
                            );
                        })
                    };
                }else{
                    bs4pop.alert(result.message, {type: 'error'});
                    return;
                }
            }
        }
    }

    //资产组件事件
    var assetEvent = {
        eventName: 'select2:selecting',
        eventHandler: function (e) {
            let bizType = $('#bizType').val();
            let suggestion = e.params.args.data;
            let leaseOrderItems = buildLeaseOrderItems();
            let index = getIndex($(this).attr('id'));
            if (!suggestion.marketId) {
                bs4pop.notice('未指定业务入账组织，不能办理业务', {position: 'bottomleft', type: 'danger'});
                return false;
            }

            clearAssetsInputData(index);
            //摊位预设信息设置
            if (leaseOrderItems.length == 0 || (leaseOrderItems.length == 1 && leaseOrderItems[0].itemIndex == index)) {
                $('#mchId').val(suggestion.marketId);
                if (bizType == ${@com.dili.ia.glossary.BizTypeEnum.BOOTH_LEASE.getCode()}) {
                    let rental = getRentalByAssetsId(suggestion.id);
                    if (rental) {
                        $('#batchId_' + index).val(rental.batchId);
                        $('#batchId').val(rental.batchId);
                        rental.engageCode && $('#engageCode').val(rental.engageCode);
                        rental.leaseTermCode && $('#leaseTermCode').val(rental.leaseTermCode);
                        rental.leaseDays && $('#days').val(rental.leaseDays);
                        rental.startTime && $('#startTime').val(moment(rental.startTime).format("YYYY-MM-DD"));
                        rental.endTime && $('#endTime').val(moment(rental.endTime).format("YYYY-MM-DD"));

                        if (rental.categoryId) {
                            $('#categorys').html('');
                            let categoryIds = rental.categoryId.split(',');
                            let categoryNames = rental.categoryName.split(',');
                            categoryIds.forEach((categoryId, i, arr) => {
                                var option = new Option(categoryNames[i], categoryId, true, true);
                                $('#categorys').append(option).trigger('change');
                            });
                        }
                    } else {
                        $('#batchId_' + index).val('');
                        $('#batchId').val('');
                    }
                }

            }

            if (bizType == ${@com.dili.ia.glossary.BizTypeEnum.LOCATION_LEASE.getCode()}) {
                let rentBalance = getRentBalance(suggestion.id);
                $('#leasesNum_' + index).val(rentBalance);
                $('#leasesNum_' + index).attr('max', rentBalance);
            }

            $('#mchId_' + index).val(suggestion.marketId);
            $('#number_' + index).val(suggestion.number);
            if (bizType != ${@com.dili.ia.glossary.BizTypeEnum.LOCATION_LEASE.getCode()}) {
                $('#leasesNum_' + index).val(suggestion.number);
            }
            $('#assetsName_' + index).val(suggestion.name);
            $('#unitCode_' + index).val(suggestion.unit);
            $('#unitName_' + index).val(suggestion.unitName);
            $('#sku_' + index).val(suggestion.number + suggestion.unitName);
            $('#isCorner_' + index).val(suggestion.cornerName);
            $('#firstDistrictId_' + index).val(suggestion.area);
            $('#firstDistrictName_' + index).val(suggestion.areaName);
            $('#secondDistrictId_' + index).val(suggestion.secondArea);
            $('#secondDistrictName_' + index).val(suggestion.secondAreaName);
            batchQueryDepositBalance($('#assetsType').val(),$('#customerId').val(),[suggestion.id]);
            $('#id').val() && batchQueryDepositOrder({
                businessId: $('#id').val(),
                bizType: bizType,
                assetsId: suggestion.id
            });
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

        let assetsIds = $("table select[name^='assetsId']").filter(function () {
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
     * 清除资产项填充数据
     * @param index
     */
    function clearAssetsInputData(index) {
        $('#unitPrice_' + index).val('');
        $('#paymentMonth_' + index).val('');
        $('#discountAmount_' + index).val('');
        $('#depositMakeUpAmount_' + index).val('');
        $("tr[data-index='" + index + "']").find("input[isCharge]").val('').attr('readonly', true);
    }

    /**
     * 查询预设
     * @param assetsId
     * @returns {*}
     */
    function getRentalByAssetsId(assetsId) {
        let rental;
        $.ajax({
            type: "get",
            url: "/assetsRental/getRentalByAssetsId.action",
            data: {assetsId: assetsId},
            async: false,
            dataType: "json",
            success: function (ret) {
                if (ret.success) {
                    if (ret.data) {
                        rental = ret.data;
                    }
                } else {
                    bs4pop.notice(ret.message, {position: 'bottomleft', type: 'danger'});
                }
            },
            error: function (a, b, c) {
                bs4pop.alert('远程访问失败', {type: 'error'});
            }
        });
        return rental;
    }


    /**
     * 冷库可租数量查询
     * @returns number
     */
    function getRentBalance(assetsId) {
        let rentBalance;
        $.ajax({
            type: "get",
            url: "/assets/getRentBalance.action",
            data: {assetsId: assetsId, start: $('#startTime').val(), end: $('#endTime').val()},
            async: false,
            dataType: "json",
            success: function (ret) {
                if (ret.success) {
                    if (ret.data) {
                        rentBalance = ret.data;
                    }
                } else {
                    bs4pop.notice(ret.message, {position: 'bottomleft', type: 'danger'});
                }
            },
            error: function (a, b, c) {
                bs4pop.alert('远程访问失败', {type: 'error'});
            }
        });
        return rentBalance;
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
                                $('#depositMakeUpAmount_' + index).val(Number(depositOrder.amount).centToYuan()).attr('readonly', true);
                            }else{
                                $('#depositMakeUpAmount_' + index).val(Number(depositOrder.amount).centToYuan());
                            }
                        }
                    }
                    calcTotalAmountAndDeposit();
                }
            },
            error: function (a, b, c) {
                bs4pop.alert('远程访问失败', {type: 'error'});
            }
        });
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

        isCascadeCalc && calcTotalAmountAndDeposit();
    }

    /**
     * 计算合计金额+补交保证金
     */
    function calcTotalAmountAndDeposit() {
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
    }

    /**
     * 构建摊位项数据
     * @param isYuanToCent是否元转分
     * @returns {[]}
     */
    function buildLeaseOrderItems(isYuanToCent = true) {
        let leaseOrderItems = [];
        $("table select[name^='assetsId']").filter(function () {
            return this.value
        }).parents("tr").each(function () {
            let leaseOrderItem = {};
            leaseOrderItem.businessChargeItems = [];
            $(this).find("input:not('.select2-search__field'),select").each(function (t, el) {
                let nameArr = $(this).attr("name").split('_');
                let fieldName = nameArr[0];
                if (fieldName.includes("chargeItem")) {
                    let businessChargeItem = {};
                    businessChargeItem.chargeItemName = this.dataset.chargeItemName;
                    businessChargeItem.chargeItemId = this.dataset.chargeItemId;
                    businessChargeItem.ruleId = this.dataset.ruleId;
                    businessChargeItem.ruleName = this.dataset.ruleName;
                    businessChargeItem.ruleAmount = isYuanToCent && this.dataset.ruleAmount;
                    businessChargeItem.amount = isYuanToCent && $(this).hasClass('money')? Number($(this).val()).mul(100) : $(this).val();
                    leaseOrderItem.businessChargeItems.push(businessChargeItem);
                } else {
                    leaseOrderItem[fieldName] = isYuanToCent && $(this).hasClass('money') ? Number($(this).val()).mul(100) : $(this).val();
                }
            });
            leaseOrderItem.assetsType = $('#assetsType').val();
            leaseOrderItem.bizType = $('#bizType').val();
            leaseOrderItems.push(leaseOrderItem);
        });
        return leaseOrderItems;
    }

    /**
     * 构建资产租赁表单提交数据
     * @param isYuanToCent是否元转分
     * @returns {{}|jQuery}
     */
    function buildFormData(isYuanToCent = true) {
        let formData = $("input:not(table input),textarea,select").serializeObject();
        let leaseTermName = $('#leaseTermCode').find("option:selected").text();
        let engageName = $('#engageCode').find("option:selected").text();
        let departmentName = $('#departmentId').find("option:selected").text();

        isYuanToCent && bui.util.yuanToCentForMoneyEl(formData);

        $.extend(formData, {
            leaseOrderItems: buildLeaseOrderItems(),
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
     * 构建费用项计算参数
     * @returns {boolean|[]}
     */
    function buildChargeItemCalcParam() {
        let bizType = $('#bizType').val();
        let marketId = $('#marketId').val();
        let startTime = $('#startTime').val();
        let endTime = $('#endTime').val();
        let wholeDate;//时间数计算
        if (startTime && endTime) {
            wholeDate = wholeDateCalc(startTime, endTime);
        }

        let formData = buildFormData(false);
        // let categoryIds =  $('#categorys').val();
        let queryFeeInputs = [];
        for (let leaseOrderItem of formData.leaseOrderItems) {
            let conditionParams = {...formData, ...leaseOrderItem};
            if (wholeDate) {
                conditionParams = {
                    ...conditionParams,
                    years: wholeDate.wholeYear,
                    months: wholeDate.wholeMonth,
                    scatteredDays: wholeDate.WholeDay
                }
            }
            delete conditionParams.leaseOrderItems;
            delete conditionParams.businessChargeItems;
            let lotQueryFeeInputs = leaseOrderItem.businessChargeItems.map(function (o) {
                return {
                    requestDataId: o.chargeItemId + '_' + leaseOrderItem.itemIndex ,
                    chargeItem: o.chargeItemId,
                    businessType: bizType,
                    marketId,
                    conditionParams,
                    calcParams: conditionParams
                }
            });
            queryFeeInputs = queryFeeInputs.concat(lotQueryFeeInputs);
        }

        console.log(queryFeeInputs);
        return queryFeeInputs;
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
     * 检查表单数据
     * @returns {boolean}
     */
    function checkFormData() {
        let validator = $('#saveForm').validate({ignore: ''})
        if (!validator.form()) {
            $('.breadcrumb [data-toggle="collapse"]').html('收起 <i class="fa fa-angle-double-up" aria-hidden="true"></i>');
            $('.collapse:not(.show)').addClass('show');
            return false;
        }

        return checkAssets();
    }

    /**
     * 检查资产
     * @returns {boolean}
     */
    function checkAssets() {
        let assetsIds = $("table select[name^='assetsId']").filter(function () {
            return this.value
        }).map(function () {
            return $('#assetsId_' + getIndex(this.id)).val();
        }).get();

        if (assetsIds.length == 0) {
            bs4pop.notice('请添加资产！', {position: 'bottomleft', type: 'danger'});
            return false;
        }

        if (arrRepeatCheck(assetsIds)) {
            bs4pop.notice('存在重复资产，请检查！', {position: 'bottomleft', type: 'danger'});
            return false;
        }

        if (assetsIds.length > 10) {
            bs4pop.notice('最多10个资产', {position: 'bottomleft', type: 'danger'});
            return false;
        }
        return true;
    }

    /**
     * 费用计算
     */
    function calcChargeItemHandler() {
        if (!checkAssets())
            return false;
        bui.loading.show('努力计算中，请稍候。。。');
        $.ajax({
            type: "POST",
            url: "/leaseOrder/batchQueryFeeWithoutShortcut.action",
            data: JSON.stringify(buildChargeItemCalcParam()),
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            success: function (ret) {
                $("table select[name^='assetsId']").filter(function () {
                    return this.value
                }).parents("tr").find("input[isCharge]").attr('readonly', false);
                if(!ret.success){
                    bs4pop.alert(ret.message, {type: 'error'});
                }else{
                    let calcResult = ret.data;
                    for (let chargeItemResult of calcResult) {
                        let chargeItemDataset = $('#chargeItem_'+chargeItemResult.requestDataId)[0].dataset;
                        if (chargeItemResult.success) {
                            let ruleAmount = Math.round(Number(chargeItemResult.totalFee).mul(100)).centToYuan();
                            $('#chargeItem_'+chargeItemResult.requestDataId).val(ruleAmount);
                            chargeItemDataset['ruleId'] = chargeItemResult.ruleId;
                            chargeItemDataset['ruleName'] = chargeItemResult.ruleName;
                            chargeItemDataset['ruleAmount'] = ruleAmount;
                            calcTotalAmount(true);
                        } else {
                            bs4pop.notice(chargeItemDataset['chargeItemName'] + ' ' + chargeItemResult.message, {position: 'bottomleft', type: 'danger'});
                        }
                    }
                }
                bui.loading.hide();
            },
            error: function (a, b, c) {
                bui.loading.hide();
                bs4pop.alert('远程访问失败', {type: 'error'});
            }
        });
    }

    /**
     * 表单baocun
     * @returns {boolean}
     */
    function saveFormHandler(){
        if (!checkFormData())
            return false;
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
            bs4pop.notice('最多10个资产', {position: 'bottomleft', type: 'danger'})
        }
    });

    //资产删除事件
    $(document).on('click', '.item-del', function () {
        if ($('#assetTable tr').length > 1) {
            $(this).closest('tr').remove();
            calcTotalAmount(true);
        }

        if ($('#assetTable tr').length == 1) {
            $('#batchId').val('');
            $('#mchId').val('');
        }
    });

    $('#save').on('click', bui.util.debounce(saveFormHandler,1000,true));
    $('#chargeItemCalc').on('click', bui.util.debounce(calcChargeItemHandler,1000,true));

    $('#managerId').on('select2:select', function (e) {
        var data = e.params.data;
        $('#manager').val(data.text);
    });

    /*****************************************自定义事件区 end**************************************/
</script>