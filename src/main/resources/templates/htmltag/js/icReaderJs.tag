<script type="text/javascript">
    /**
     * 初始化刷卡 身份证
     * @param option {id:'',onLoadSuccess:function(customer){}}
     */
    function initSwipeCard(option){
        $('#' + option.id).click(function () {
            var ic = readerIc()
            if(ic.card) {
                $(this).val(ic.card);
                icCheck();
            } else {
                bs4pop.alert(ic.message, {type : "error"});
            }
        });
    }



    function icCheck(onLoadSuccess) {
        var ic = $("#_cardNo").val();
        console.info(ic+"this ic check");
        $.ajax({
            type: 'get',
            url: '/account/icCheck?ic='+ic,
            dataType: 'json',
            async: false,
            success: function (ret) {
                if (ret.code == "success") {
                    var aInfo = ret.data;
                    onLoadSuccess(aInfo);
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