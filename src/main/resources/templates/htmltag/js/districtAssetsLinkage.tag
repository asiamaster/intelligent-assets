<script>

    $(()=>{
        // $('#assetsType').trigger('change')
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

    // 资产类型改变，资产地址输入方式更改。
    $('#assetsType').on('change', function(){
        $('#assetsName-error').remove();
        $('#assetsId, #assetsName, #assetsNameInput').val('');
        $('#assetsName, #assetsNameInput').attr('name', '').removeClass('d-none');
        if($(this).val() == 100 ) {
            // 为'其他'时
            $('#assetsName').addClass('d-none');
            $('#assetsNameInput').attr('name', 'assetsName');
        } else {
            $('#assetsName').attr('name', 'assetsName');
            $('#assetsId').attr('name', 'assetsId');
            $('#assetsNameInput').addClass('d-none');
        }
    })

    // 区域改变，
    $('#firstDistrictId, #secondDistrictId').change(function () {
        let id  = $(this).attr('id');
        $('#assetsType').trigger("change");
        valueDistrictName($('#'+id));
    })

</script>