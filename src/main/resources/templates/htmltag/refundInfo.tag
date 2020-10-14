<input type="hidden" name="oldTradeCardNo" value="${refundOrder.tradeCardNo!}"/>

<div class="form-group col">
    <label for="payee">收款人<i class="red">*</i></label>
    <%if(@com.dili.uap.sdk.session.SessionContext.getSessionContext().getUserTicket().getFirmCode() =='hzsc'){%>
    <input type="text" class="form-control" name="payee" value="${customerName!}" readonly/>
    <input type="hidden" class="form-control" name="payeeId" value="${customerId!}"/>
    <%}else{%>
    <#bautoCompleteProvider _log="收款人" _hiddenDomainId="payeeId" _hiddenDomainName="payeeId" _displayDomainId="payee" _displayDomainName="payee" _validatorMethod="isSelected" _required="true" _value="${refundOrder.payeeId!}" _text="${refundOrder.payee!}" _optionVariable="customerNameAutoCompleteOption"/>
    <%}%>
</div>
<div class="form-group col">
    <label for="payeeCertificateNumber" class="">证件号<i class="red">*</i></label>
    <%if(@com.dili.uap.sdk.session.SessionContext.getSessionContext().getUserTicket().getFirmCode()=='hzsc'){%>
    <input type="text" class="form-control" name="payeeCertificateNumber" value="${certificateNumber!}" readonly/>
    <%}else{%>
    <input type="text" class="form-control" id="payeeCertificateNumber" name="payeeCertificateNumber" value="${refundOrder.payeeCertificateNumber!}" readonly/>
    <%}%>
</div>
<div class="form-group col">
    <label  class="" _log>金额<i class="red">*</i></label>
    <input type="number" class="form-control floatReserve money" id="payeeAmount" name="payeeAmount" value="<#centToYuan value='${refundOrder.payeeAmount!0}'/>"  min="0" required/>
</div>
<div class="form-group col">
    <label for="refundType" class="">退款方式<i class="red">*</i></label>
    <select id="refundType" name="refundType" class="form-control" ></select>
    <#bcomboProvider _log="退款方式" _id="refundType" _provider="refundTypeProvider" _queryParams='{required:true}' _value="${refundOrder.refundType!}" _option="async:false" />
</div>
</div>
<div id="bankInfo"  class="row row-cols-4"  <%if(isNotEmpty(refundOrder.refundType) && refundOrder.refundType == @com.dili.settlement.enums.SettleWayEnum.BANK.getCode()){ %> style="display: flex" <% } else {%>  style="display: none" <%}%> >
    <div class="form-group col">
        <label for="bank" class="" _log>开户行<i class="red">*</i></label>
        <input type="text" class="form-control" id="bank"  name="bank" value="${refundOrder.bank!}" maxlength="50" required/>
    </div>
    <div class="form-group col">
        <label for="bankCardNo" class="" _log>银行卡号<i class="red">*</i></label>
        <input type="text" class="form-control" id="bankCardNo" name="bankCardNo" value="${refundOrder.bankCardNo!}" maxlength="30" required/>
    </div>
</div>

<div id="accountInfo"  class="row row-cols-4"  <%if(isNotEmpty(refundOrder.refundType) && refundOrder.refundType == @com.dili.settlement.enums.SettleWayEnum.CARD.getCode()){ %> style="display: flex" <% } else {%>  style="display: none" <%}%> >
    <div class="form-group col">
        <label for="tradeCardNo" class="" _log>园区卡号<i class="red">*</i></label>
        <select class="form-control" id="tradeCardNo"  name="tradeCardNo" required>
        </select>
    </div>
</div>

<script type="text/javascript">
    $('#refundType').on('change', function(){
        var refundType = $('#refundType').val();
        if (refundType == ${@com.dili.settlement.enums.SettleWayEnum.CASH.getCode()}){
            // 现金
            $('#bankInfo').css('display','none');
            $('#accountInfo').css('display','none');
        }else if(refundType == ${@com.dili.settlement.enums.SettleWayEnum.BANK.getCode()}){
            // 银行卡
            $('#bankInfo').css('display','flex');
            $('#accountInfo').css('display','none');
        }else if(refundType == ${@com.dili.settlement.enums.SettleWayEnum.CARD.getCode()}){
            // 园区卡
            $('#accountInfo').css('display','flex');
            $('#bankInfo').css('display','none');
            var customerId = $('input[name="payeeId"]').val();
            var oldTradeCardNo = $('input[name="oldTradeCardNo"]').val();
            //清空值
            $("#tradeCardNo").empty();
            $.ajax({
                type: 'get',
                url: '/account/getAccountListByCustomerId.action?customerId='+customerId,
                dataType: 'json',
                async: false,
                success: function (ret) {
                    if (ret.success) {
                        var aInfoList = ret.data;
                        for(var i=0;i<aInfoList.length;i++){
                            $('#tradeCardNo').append( '<option value="'+aInfoList[i].cardNo+'">'+aInfoList[i].cardNo+'</option>'); //添加option
                        }
                        //修改回显
                        if(oldTradeCardNo){
                            $('#tradeCardNo').val(oldTradeCardNo);
                        }
                    } else {
                        bs4pop.alert('此卡无效，不能交易！', {type : "warning"});
                        return false;
                    }
                }
            });


        }
    });


    $(function () {
        $('#refundType').trigger('change');
    });
</script>