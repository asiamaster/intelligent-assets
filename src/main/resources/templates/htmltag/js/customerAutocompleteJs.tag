<script>
    /**
     *
     * @Date 2019-03-17 09:00:00
     * @author chenliangfang
     *
     ***/

    /************  start **************/
    /************  end ***************/


    window.domain = 'diligrp.com';
    var registerDia;


    /************* 刷卡获取客户信息  start *****************/
// 客户名称
    var customerNameAutoCompleteOption = {
        serviceUrl: '/customer/listNormal.action',
        paramName: 'likeName',
        displayFieldName: 'name',
        showNoSuggestionNotice: true,
        noSuggestionNotice: '<a href="javascript:;" id="goCustomerRegister">无此客户，点击注册</a>',
        transformResult: function (result) {
            if(result.success){
                let data = result.data;
                return {
                    suggestions: $.map(data, function (dataItem) {
                        return $.extend(dataItem, {
                                value: dataItem.name + '（' + dataItem.certificateNumber + '）'
                            }
                        );
                    })
                }
            }else{
                bs4pop.alert(result.message, {type: 'error'});
                return false;
            }
        },
        selectFn: function (suggestion) {
            $('#certificateNumber, #_certificateNumber').val(suggestion.certificateNumber);
            $('#customerCellphone').val(suggestion.contactsPhone);
            $('#certificateNumber, #_certificateNumber, #customerCellphone').valid();
        }
    };
    // 证件号码
    var certificateNumberAutoCompleteOption = {
        minChars: 6,
        serviceUrl: '/customer/listNormal.action',
        paramName: 'certificateNumberMatch',
        displayFieldName: 'certificateNumber',
        showNoSuggestionNotice: true,
        noSuggestionNotice: '<a href="javascript:;" id="goCustomerRegister">无此客户，点击注册</a>',
        transformResult: function (result) {
            if(result.success){
                let data = result.data;
                return {
                    suggestions: $.map(data, function (dataItem) {
                        return $.extend(dataItem, {
                                value: dataItem.name + '（' + dataItem.certificateNumber + '）'
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
            $('#customerName').val(suggestion.name);
            $('#customerId').val(suggestion.id);
            $('#customerCellphone').val(suggestion.contactsPhone);
            $('#customerName, #customerCellphone').valid();
        }
    };

    /**
     * 初始化刷卡 身份证
     * @param option {id:'',onLoadSuccess:function(customer){}}
     */
    function initSwipeCard(option){
        $('#'+option.id).on('click', function (e) {
            e.stopPropagation();
            let user = reader();
            $.ajax({
                type: "POST",
                url: "/customer/listNormal.action",
                data: {certificateNumber : user.IDCardNo},
                dataType: "json",
                success: function (result) {
                    if(result.success){
                        let data = result.data;
                        if(data && data.length > 0){
                            let customer = data[0];
                            $('#customerName').val(customer.name);
                            $('#customerId').val(customer.id);
                            $('#certificateNumber,#_certificateNumber').val(customer.certificateNumber);
                            $('#customerCellphone').val(customer.contactsPhone);
                            option.onLoadSuccess && option.onLoadSuccess(customer);
                        }else{
                            bs4pop.alert('无此客户，请注册');
                        }
                    }else{
                        bs4pop.alert('接口查询异常');
                    }
                },
                error: function (a, b, c) {
                    bs4pop.alert('远程访问失败', {type: 'error'});
                }
            });
        });
    }



    /**
     * 读身份证卡
     * @return {IDCardNo:'5116021989...'}
     * */
    function reader() {
        if (!window.callbackObj) return;
        return eval('(' + callbackObj.readIDCard() + ')');
    }
    /************* 刷卡获取客户信息  end *****************/


    /************ postMessage 监听消息 start **************/
    function initMsg(fn){
        window.addEventListener('message', function (e) {
            fn(e.data);
        }, false);
    }
    /************  end ***************/


    /**
     无此客户点击注册
     */


    function openCustomerRegister(sourceChannel) {
        let http = '<#config name="customer.origin"/>';
        let sourceSystem = '<#config name="ia.source.system"/>';

        // let url = 'http://customer.diligrp.com:8382/customer/register.action?sourceSystem=INTELLIGENT_ASSETS&sourceChannel=bg_create';
        let url = http + '/customer/register.action?sourceSystem=' + sourceSystem + '&sourceChannel=' + sourceChannel;
        registerDia = bs4pop.dialog({
            title: '新增客户',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '600',
            height: '700',
        });
    }

    initMsg(function (data) {
        if (JSON.parse(data)["isClose"]) {
            registerDia.hide();
        }
    });

    // 无此客户点击注册
    $(document).on('click','#goCustomerRegister', function(){
        $(this).parent().remove();
        openCustomerRegister($('#_sourceChannel').val());
    });


    //监听客户注册，回显客户信息
    function registerMsg(fn, isBackShow = true, other){
        window.addEventListener('message', function (e) {
            // 是否回显客户信息
            debugger
            if (isBackShow) {
                let msgData = JSON.parse(e.data);
                if(msgData.topic == 'customerRegister'){
                    let customer = msgData.content;
                    $('#certificateNumber, #_certificateNumber').val(customer.certificateNumber);
                    $('#customerName').val(customer.name);
                    $('#customerId').val(customer.customerId);
                    $('#customerCellphone').val(customer.contactsPhone);
                }
            }

            if(typeof fn === "function"){
                fn(e.data, other);
            }
        }, false);
    }



</script>