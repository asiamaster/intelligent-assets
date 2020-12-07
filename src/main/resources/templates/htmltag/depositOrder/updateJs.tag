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

    var boothAutoCompleteOption = {
        paramName: 'keyword',
        displayFieldName: 'name',
        serviceUrl: '/assets/searchAssets.action',
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

        registerMsg();
        $('#assetsId, #assetsName, #assetsNameInput').hide();

    });

    $('#firstDistrictId').on('change', function(){
        $('#assetsType').trigger("change");
    })

    $('#secondDistrictId').on('change', function(){
        $('#assetsType').trigger("change");
    })

    $('#assetsType').on('change', function(){
        $('#assetsId, #assetsName, #assetsNameInput').val('');
        $('#assetsName, #assetsNameInput').removeClass('d-block');
        $('#assetsName-error').remove();
        $('#assetsNameInput').attr('name', '');
        if($(this).val() == 1 ) {
            $('#assetsName').addClass('d-block');
        } else {
            $('#assetsNameInput').attr('name', 'assetsName').addClass('d-block');
        }
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
                                value: dataItem.name + '(' + (dataItem.secondAreaName? dataItem.areaName + '->' + dataItem.secondAreaName : dataItem.areaName) + ')'
                            }
                        );
                    })
                }
            }else{
                bs4pop.alert(result.message, {type: 'error'});
                return;
            }
        },
        selectFn: function (suggestion) {
            $('#assetsName').val(suggestion.name);
            $('#assetsId').val(suggestion.id);
        }
    }

    function buildFormData(){
        // let formData = new FormData($('#saveForm')[0]);
        let formData = $("input:not(table input),textarea,select").serializeObject();
        let typeName = $('#typeCode').find("option:selected").text();
        bui.util.yuanToCentForMoneyEl(formData);
        $.extend(formData,{typeName,logContent:Log.buildUpdateContent()});
        return formData;
    }

    // 提交保存
    function doUpdateDepostHandler(){
        let validator = $('#updateForm').validate({ignore:''})
        if (!validator.form()) {
            $('.breadcrumb [data-toggle="collapse"]').html('收起 <i class="fa fa-angle-double-up" aria-hidden="true"></i>');
            $('.collapse:not(.show)').addClass('show');
            return false;
        }
        bui.loading.show('努力提交中，请稍候。。。');
        // let _formData = new FormData($('#updateForm')[0]);
        $.ajax({
            type: "POST",
            url: "${contextPath}/depositOrder/doUpdate.action",
            data: buildFormData(),
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
    $('#formSubmit').on('click', bui.util.debounce(doUpdateDepostHandler,1000,true));
</script>