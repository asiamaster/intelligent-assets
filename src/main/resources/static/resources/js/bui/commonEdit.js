/**
 *
 * @Date 2019-02-22 21:32:00
 * @author chenliangfang
 *
 ***/


/************  start **************/
/************  end ***************/

/************* 刷卡获取客户信息  start *****************/
// 客户名称
var customerNameAutoCompleteOption = {
    width: 350,
    serviceUrl: '/customer/list.action',
    paramName: 'name',
    displayFieldName: 'name',
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
    }
};
// 证件号码
var certificateNumberAutoCompleteOption = {
    width: 350,
    serviceUrl: '/customer/list.action',
    paramName: 'certificateNumber',
    displayFieldName: 'certificateNumber',
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
            $('#customerCellphone').val(data[0].cellphone);
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



/************ 初始化日期/时间 start **************/
$(function () {
    lay('.laydate').each(function () {
        laydate.render({
            elem: this,
            trigger: 'click',
            type: 'date',
            theme: '#007bff',
            done: function (value, date) {
                isStartEndDatetime(value, this.elem);
            }
        });

    });
    lay('.laydatetime').each(function () {
        laydate.render({
            elem: this,
            trigger: 'click',
            type: 'datetime',
            theme: '#007bff',
            done: function (value, date) {
                isStartEndDatetime(value, this.elem);
            }
        });
    });
})
//始结束时间对比
function isStartEndDatetime(date, el){
    let start = new Date($('.laystart').val());
    let end = new Date($('.layend').val());
    if ($(el).attr('class').indexOf('laystart')>-1 && end) {
        if (moment(date).isSameOrAfter(end)) {
            bs4pop.alert('结束时间需大于开始时间',{} ,function () {$(el).val('')});
        }
    } else if (start && $(el).attr('class').indexOf('layend')>-1 ) {
        if (moment(start).isSameOrAfter(date)) {
            bs4pop.alert('结束时间需大于开始时间',{} ,function () {$(el).val('')});
        }
    }
}
/************ 初始化日期/时间 end **************/




/************ HTML反转义 start **************/
function HTMLDecode(str) {
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

/************ HTML反转义 end **************/
