<script type="text/javascript">
    /**
     * 初始化刷卡 身份证
     * @param option {id:'',onLoadSuccess:function(customer){}}
     */
    function initSwipeParkCard(option){
        $('#' + option.id).click(function () {
            var ic = readerIc()
            if(ic.card) {
                icCheck(ic.card, option);
            } else {
                bs4pop.alert(ic.message, {type : "error"});
            }
        });
    }

    function icCheck(cardNo, option) {
        console.info(cardNo+"this ic check");
        $.ajax({
            type: 'get',
            url: '/account/icCheck.action?ic=' + cardNo,
            dataType: 'json',
            async: false,
            success: function (ret) {
                if (ret.success) {
                    var aInfo = ret.data;
                    option.onLoadSuccess && option.onLoadSuccess(aInfo);
                } else {
                    bs4pop.alert('此卡无效，不能交易！', {type : "warning"});
                    return false;
                }
            }
        });
    }

    // 读园区卡
    function readerIc() {
        if (typeof(callbackObj) == "undefined") return false;
        var json = JSON.parse(callbackObj.readCardNumber());
        var result = {
            message: json.message,
            card: ''
        }
        if(json.code == 0){
            result['card'] = json.data;
        }
        return result;
    }
</script>