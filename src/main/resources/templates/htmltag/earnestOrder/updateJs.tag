<script>
    /**
     *
     * @Date 2019-11-06 17:30:00
     * @author jiangchengyong
     *
     ***/

        //行索引计数器
    let itemIndex = 0;

    //获取table Index
    function getIndex(str) {
        return str.split('_')[1];
    }

    $('#firstDistrictId').on('change', function(){
        $("#boothTable tbody").find("tr").each(function(){
            $(this).find("input").each(function(t,el){
                $(this).val('');
            });
        });
    })

    $('#secondDistrictId').on('change', function(){
        $("#boothTable tbody").find("tr").each(function(){
            $(this).find("input").each(function(t,el){
                $(this).val('');
            });
        });
    })

    let assetsType = $('[name="assetsType"]').val();
    let firstDistrictId = $('[name="firstDistrictId"]').val();
    let secondDistrictId = $('[name="secondDistrictId"]').val();
    var boothAutoCompleteOption = {
        paramName: 'keyword',
        displayFieldName: 'name',
        serviceUrl: '/assets/searchAssets.action',
        onSearchStart: function (params) {
            params['assetsType'] = $('[name="assetsType"]').val();
            params['firstDistrictId'] = $('[name="firstDistrictId"]').val();
            params['secondDistrictId'] = $('[name="secondDistrictId"]').val();
            return params;
        },
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
    /**
     * 摊位选择事件Handler
     * */
      /******************************驱动执行区 begin***************************/
    $(function () {
        //初始化刷身份证
        initSwipeIdCard({
            id:'getCustomer',
        });

        //初始化刷园区卡
        initSwipeParkCard({
            id:'icReader',
            onLoadSuccess:function(customer){
                $('#customerName').val(customer.name);
                $('#customerId').val(customer.customerId);
                $('#certificateNumber,#_certificateNumber').val(customer.customerCertificateNumber);
                $('#customerCellphone').val(customer.customerContactsPhone);
            }
        });

        <% if(isNotEmpty(earnestOrderDetails)){ %>
        let earnestOrderDetails = JSON.parse('${earnestOrderDetails}');
        for (let earnestOrderDetail of earnestOrderDetails){
            boothItem($.extend(earnestOrderDetail,{index: ++itemIndex}));
        }
        <% }else{%>
        while (itemIndex<1) {
            boothItem({index: ++itemIndex});
        }
        <% }%>

        registerMsg();

    });

    /**
     * 添加摊位
     * @param leaseOrderItem
     */
    function boothItem(earnestOrderDetail){
        $('#boothTable tbody').append(bui.util.HTMLDecode(template('boothItem',earnestOrderDetail)))
    }

    /**
     * 添加摊位
     * @param leaseOrderItem
     */
    function addBoothItem(){
        $('#boothTable tbody').append(bui.util.HTMLDecode(template('boothItem', {index: ++itemIndex})))
    }

    // 添加摊位
    $('#addBooth').on('click', function () {
        if ($('#boothTable tr').length < 11) {

            addBoothItem();
        } else {
            bs4pop.notice('最多10个摊位', {position: 'leftcenter', type: 'warning'})
        }
    })

    //删除行事件 （删除摊位行）
    $(document).on('click', '.item-del', function () {
        if ($('#boothTable tr').length > 1) {
            $(this).closest('tr').remove();
        }
    });

    function buildFormData(){
        // let formData = new FormData($('#saveForm')[0]);
        let formData = $("input:not(table input),textarea,select").serializeObject();
        let earnestOrderdetails = [];
        bui.util.yuanToCentForMoneyEl(formData);
        $("#boothTable tbody").find("tr").each(function(){
            let earnestOrderdetail = {};
            $(this).find("input").each(function(t,el){
                let fieldName = $(this).attr("name").split('_')[0];
                if ($(this).val() != ""){
                    earnestOrderdetail[fieldName] = $(this).hasClass('money')? Number($(this).val()).mul(100) : $(this).val();
                }
            });
            if (Object.keys(earnestOrderdetail).length) {
                earnestOrderdetails.push(earnestOrderdetail);
            }
        });

        $.extend(formData,{earnestOrderdetails,logContent:Log.buildUpdateContent()});
        return JSON.stringify(formData);
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
    // 提交保存
    function doUpdateEarnestHandler(){
        let validator = $('#updateForm').validate({ignore:''})
        if (!validator.form()) {
            $('.breadcrumb [data-toggle="collapse"]').html('收起 <i class="fa fa-angle-double-up" aria-hidden="true"></i>');
            $('.collapse:not(.show)').addClass('show');
            return false;
        }
        let boothIds = $("table input[name^='assetsId']").filter(function () {
            return this.value
        }).map(function(){
            return $('#assetsId_'+getIndex(this.id)).val();
        }).get();

        if(arrRepeatCheck(boothIds)){
            bs4pop.alert('存在重复摊位，请检查！');
            return false;
        }
        bui.loading.show('努力提交中，请稍候。。。');
        // let _formData = new FormData($('#updateForm')[0]);
        $.ajax({
            type: "POST",
            url: "${contextPath}/earnestOrder/doUpdate.action",
            data: buildFormData(),
            contentType: "application/json; charset=utf-8",
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

    //防抖
    $('#formSubmit').on('click', bui.util.debounce(doUpdateEarnestHandler,1000,true));
</script>