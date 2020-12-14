<input type="hidden" name="oldTradeCardNo" value="${refundOrder.tradeCardNo!}"/>
<div class="row row-cols-4">
    <div class="form-group col">
        <label for="payee">收款人<i class="red">*</i></label>
        <%if(@com.dili.uap.sdk.session.SessionContext.getSessionContext().getUserTicket().getFirmCode() =='sg'){%>
        <input type="text" class="form-control" name="payee" value="${customerName!}" readonly/>
        <input type="hidden" class="form-control" name="payeeId" value="${customerId!}"/>
        <%}else{%>
        <#bautoCompleteProvider _log="收款人" _hiddenDomainId="payeeId" _hiddenDomainName="payeeId" _displayDomainId="payee" _displayDomainName="payee" _validatorMethod="isSelected" _required="true" _value="${refundOrder.payeeId!}" _text="${refundOrder.payee!}" _optionVariable="customerNameAutoCompleteOption"/>
        <%}%>
    </div>
    <div class="form-group col">
        <label for="payeeCertificateNumber" class="">证件号<i class="red">*</i></label>
        <%if(@com.dili.uap.sdk.session.SessionContext.getSessionContext().getUserTicket().getFirmCode()=='sg'){%>
        <input type="text" class="form-control" name="payeeCertificateNumber" value="${certificateNumber!}" readonly/>
        <%}else{%>
        <input type="text" class="form-control" id="payeeCertificateNumber" name="payeeCertificateNumber" value="${refundOrder.payeeCertificateNumber!}" readonly/>
        <%}%>
    </div>
    <div class="form-group col">
        <label  class="" _log>金额<i class="red">*</i></label>
        <input type="number" class="form-control floatReserve money" id="payeeAmount" name="payeeAmount" value="<#centToYuan value='${refundOrder.payeeAmount!0}'/>" min="0" readonly/>
    </div>
</div>
