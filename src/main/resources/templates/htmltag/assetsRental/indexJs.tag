<script>

    /*********************变量定义区 begin*************/
        //行索引计数器
        //如 let itemIndex = 0;
    let _grid = $('#grid');
    let _form = $('#_form');
    let currentSelectRowIndex;
    var dia;

    /*********************变量定义区 end***************/

    /**
     打开新增窗口
     */
    function openInsertHandler() {
        dia = bs4pop.dialog({
            title: '新增摊位出租预设',//对话框title
            content: '/assetsRental/add.html', //对话框内容，可以是 string、element，$object
            width: '800px',//宽度
            height: '680px',//高度
            isIframe: true,//默认是页面层，非iframe
        });
    }




    //选中行事件
    _grid.on('uncheck.bs.table', function (e, row, $element) {
        currentSelectRowIndex = undefined;
    });

    //选中行事件 -- 可操作按钮控制
    _grid.on('check.bs.table', function (e, row, $element) {
        let state = row.$_state;
        let payState = row.$_payState;
        let refundState = row.$_refundState;
        let isRelated = row.isRelated;
        $('#toolbar button').attr('disabled', true);
        $('#btn_add').attr('disabled', false);
        $('#btn_view').attr('disabled', false);
        if (state == ${@com.dili.ia.glossary.DepositOrderStateEnum.CREATED.getCode()} && isRelated == ${@com.dili.commons.glossary.YesOrNoEnum.NO.getCode()}) {
            $('#btn_update').attr('disabled', false);
            $('#btn_cancel').attr('disabled', false);
            $('#btn_submit').attr('disabled', false);
        }  else if (state == ${@com.dili.ia.glossary.DepositOrderStateEnum.SUBMITTED.getCode()} && isRelated == ${@com.dili.commons.glossary.YesOrNoEnum.NO.getCode()}) {
            $('#btn_withdraw').attr('disabled', false);
            $('#btn_submit').attr('disabled', false);
        } else if (state == ${@com.dili.ia.glossary.DepositOrderStateEnum.PAID.getCode()}) {
            $('#btn_refund_apply').attr('disabled', false);
            $('#btn_invalid').attr('disabled', false);
            if (payState == ${@com.dili.ia.glossary.DepositPayStateEnum.NOT_PAID.getCode()} && isRelated == ${@com.dili.commons.glossary.YesOrNoEnum.NO.getCode()}){
                $('#btn_submit').attr('disabled', false);
            }
        } else if (state == ${@com.dili.ia.glossary.DepositOrderStateEnum.REFUND.getCode()} && refundState == ${@com.dili.ia.glossary.DepositRefundStateEnum.PART_REFUND.getCode()}) {
            $('#btn_refund_apply').attr('disabled', false);
        }
    });

</script>
