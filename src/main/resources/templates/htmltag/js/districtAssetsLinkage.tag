<script>

    $(()=>{
        if($('[name="id"]').val()){
            $('#assetsId, #assetsName, #assetsNameInput').hide();
        } else {
            $('#assetsNameInput').hide();
        }
    })

    // 获取当前区域下资产的编号数据
    var boothAutoCompleteOption = {
        paramName: 'keyword',
        displayFieldName: 'name',
        serviceUrl: '/assets/searchAssets.action',
        onSearchStart: function (params) {
            params['assetsType'] = $('#assetsType').val();
            params['firstDistrictId'] = $('#firstDistrictId').val();
            params['secondDistrictId'] = $('#secondDistrictId').val();
            return params;
        },
        transformResult: function (result) {
            if(result.success){
                let data = result.data;
                return {
                    suggestions: $.map(data, function (dataItem) {
                        return $.extend(dataItem, {
                                value: dataItem.name + '(' + (dataItem.secondAreaName? dataItem.areaName + '->' + dataItem.secondAreaName : dataItem.areaName) + ')'
                            }
                        );
                    })
                }
            }else{
                bs4pop.alert(result.message, {type: 'error'});
                return;
            }
        },
        selectFn: function (suggestion) {
            $('#assetsName').val(suggestion.name);
            $('#assetsId').val(suggestion.id);
        }
    }

    $('#assetsType').on('change', function(){
        $('#assetsName-error').remove();
        $('#assetsId, #assetsName, #assetsNameInput').val('');
        $('#assetsName, #assetsNameInput').attr('name', '').hide();
        if($(this).val() != 100 ) {
            $('#assetsName').attr('name', 'assetsName').show();
        } else {
            $('#assetsNameInput').attr('name', 'assetsName').show();
        }
    })

    $('#chargeItemId').on('change',  function () {
        $('#chargeItemName').val($(this.data('chargeItemName')))
    })

    $('#firstDistrictId, #secondDistrictId').change(function () {
        let id  = $(this).attr('id');
        $('#assetsType').trigger("change");
        valueDistrictName($('#'+id));
    })

</script>