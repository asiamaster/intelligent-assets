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
    width: 350,
    serviceUrl: '/customer/list.action',
    paramName: 'likeName',
    displayFieldName: 'name',
    showNoSuggestionNotice: true,
    noSuggestionNotice: '<a href="javascript:;" id="goCustomerRegister">无此客户，点击注册</a>',
    transformResult: function (result) {
        return {
            suggestions: $.map(result, function (dataItem) {
                return $.extend(dataItem, {
                        value: dataItem.name + ' ' + dataItem.certificateNumber
                    }
                );
            })
        }
    },
    selectFn: function (suggestion) {
        $('#certificateNumber').val(suggestion.certificateNumber);
        $('#_certificateNumber').val(suggestion.certificateNumber);
        $('#customerCellphone').val(suggestion.contactsPhone);
    }
};
// 证件号码
var certificateNumberAutoCompleteOption = {
    width: 350,
    minChars: 6,
    serviceUrl: '/customer/list.action',
    paramName: 'certificateNumberMatch',
    displayFieldName: 'certificateNumber',
    transformResult: function (result) {
        return {
            suggestions: $.map(result, function (dataItem) {
                return $.extend(dataItem, {
                        value: dataItem.name + ' ' + dataItem.certificateNumber
                    }
                );
            })
        }
    },
    selectFn: function (suggestion) {
        $('#customerName').val(suggestion.name);
        $('#customerId').val(suggestion.id);
        $('#customerCellphone').val(suggestion.contactsPhone);
    }
};
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
            $('#customerCellphone').val(data[0].contactsPhone);
        },
        error: function (a, b, c) {
            bs4pop.alert('远程访问失败', {type: 'error'});
        }
    });
});


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

function openCustomerRegister() {
    let url = 'http://customer.diligrp.com:8382/customer/register.action?sourceSystem=INTELLIGENT_ASSETS&sourceChannel=bg_create';
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

$('#goCustomerRegister').on('click', function(){
    openCustomerRegister();
});



