/**
 *
 * @Date 2019-02-22 21:32:00
 * @author chenliangfang
 *
 ***/

// 刷卡获取客户信息
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
