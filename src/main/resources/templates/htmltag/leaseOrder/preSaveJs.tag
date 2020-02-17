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
            paramName : 'name',
            displayFieldName : 'name',
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
        var certificateNumberAutoCompleteOption = {
            width : 350,
            serviceUrl: '/customer/list.action',
            paramName : 'certificateNumber',
            displayFieldName : 'certificateNumber',
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

    /*********************变量定义区 end***************/


    /******************************驱动执行区 begin***************************/
    $(function () {
        addStallItem();
    });
    /******************************驱动执行区 end****************************/

    /*****************************************函数区 begin************************************/

    //HTML反转义
    function HTMLDecode(str)
    {
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

    //获取table Index
    function getIndex(str) {
        return str.split('_')[1];
    }

    /**
     * 读身份证卡
     * @return {IDCardNo:'5116021989...'}
     * */
    function reader(){
        if(!window.callbackObj)return ;
        return eval('(' + callbackObj.readIDCard() + ')');
    }

    /**
     * 添加摊位
     * */
    function addStallItem(){
        $('#stallTable tbody').append(HTMLDecode(template('stallItem',{index:++itemIndex})))
    }

    /*****************************************函数区 end**************************************/

    /*****************************************自定义事件区 begin************************************/
    $('#formSubmit').on('click', function (e) {
        if (!$('#addForm').valid()) {
            return false;
        }
        parent.dia.hide();
    });

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

    $('#addStall').on('click', function(){
        addStallItem();
    })

    //删除行事件
    $(document).on('click', '.item-del', function () {
        if ($('#stallTable tr').length > 2) {
            $(this).closest('tr').remove();
        }
    });
    /*****************************************自定义事件区 end**************************************/
</script>