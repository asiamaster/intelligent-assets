<script>
    /**
     *
     * @Date 2019-11-06 17:30:00
     * @author jiangchengyong
     *
     ***/

    /*********************变量定义区 begin*************/
        //行索引计数器
        let itemIndex = 0;
        var customerNameAutoCompleteOption = {
            width : 350,
            serviceUrl: '/customer/list.action',
            paramName : 'likeName',
            displayFieldName : 'name',
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
                $('#payeeCertificateNumber').val(suggestion.certificateNumber);
            }
        };

        var customerNameTableAutoCompleteOption = $.extend({},customerNameAutoCompleteOption,{
            selectFn: function (suggestion,element) {
                let index = getIndex($(element).attr('id'));
                $('#certificateNumber_'+index).val(suggestion.certificateNumber);
            }
        })

    /*********************变量定义区 end***************/


    /******************************驱动执行区 begin***************************/
    $(function () {
        while ( itemIndex < 1){
            addTransferItem();
        }
    });
    /******************************驱动执行区 end****************************/

    /*****************************************函数区 begin************************************/

    //获取table Index
    function getIndex(str) {
        return str.split('_')[1];
    }

    /**
     * 添加摊位
     */
    function addTransferItem(){
        $('#transferTable tbody').append(bui.util.HTMLDecode(template('transferItemTpl',{index:++itemIndex})))
    }


    /*****************************************函数区 end**************************************/

    /*****************************************自定义事件区 begin************************************/
    $('#formSubmit').on('click', function (e) {
        if (!$('#refundApplyForm').valid()) {
            return false;
        }

        let transferIds = $("table input[name^='transferId']").filter(function () {
            return this.value
        }).map(function(){
            return $('#transferId_'+getIndex(this.id)).val();
        }).get();
        if(arrRepeatCheck(transferIds)){
            bs4pop.alert('存在重复摊位，请检查！')
            return false;
        }

        bui.loading.show();
        $.ajax({
            type: "POST",
            url: "/leaseOrder/saveLeaseOrder.action",
            data: buildFormData(),
            dataType: "json",
            async : false,
            success: function (ret) {
                bui.loading.hide();
                if(!ret.success){
                    bs4pop.alert(ret.message, {type: 'error'});
                }
            },
            error: function (a, b, c) {
                bs4pop.alert('远程访问失败', {type: 'error'});
            }
        });
        parent.closeDialog(parent.dia);
    });

    //摊位新增事件
    $('#addTransfer').on('click', function(){
        addTransferItem({index: ++itemIndex});
    });

    //摊位删除事件
    $(document).on('click', '.item-del', function () {
        if ($('#transferTable tr').length > 1) {
            $(this).closest('tr').remove();
        }
    });

    /*****************************************自定义事件区 end**************************************/
</script>