<script>
    /**
     *
     * @Date 2019-11-06 17:30:00
     * @author jiangchengyong
     *
     ***/
    //行索引计数器
    let itemIndex = 0;

    //品类搜索
    //品类搜索自动完成
    var testAutoCompleteOption = {
        serviceUrl: '/stock/categoryCycle/searchV2.action',
        paramName : 'keyword',
        displayFieldName : 'name',
        showNoSuggestionNotice: true,
        noSuggestionNotice: '无匹配结果',
        transformResult: function (result) {
            if(result.success){
                let data = result.data;
                return {
                    suggestions: $.map(data, function (dataItem) {
                        return $.extend(dataItem, {
                                value: dataItem.name + '（' + dataItem.code + '）'
                            }
                        );
                    })
                }
            }else{
                bs4pop.alert(result.message, {type: 'error'});
                return false;
            }
        },
        selectFn: function (suggestion) {
            $("#cycle").val(suggestion.cycle);
        }
    }
    $.fn.serializeObject = function()
    {
        var o = {};
        var a = this.serializeArray();
        $.each(a, function() {
            if (o[this.name]) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
        });
        return o;
    };

    /**
     *---------------天数/开始/结束日期联动 start ---------------
     **/
    laydate.render({
        elem: '#startTime',
        type: 'date',
        theme: '#007bff',
        trigger:'click',
        done: function(value, date){
            startTimeChangeHandler();
            $("#saveForm").validate().element($("#startTime"));
            $("#saveForm").validate().element($("#days"));
        }
    });
    laydate.render({
        elem: '#endTime',
        type: 'date',
        theme: '#007bff',
        trigger:'click',
        done: function(value, date){
            endTimeChangeHandler();
            $("#saveForm").validate().element($("#endTime"));
            $("#saveForm").validate().element($("#days"));
        }
    });

    /**
     * 开始日期值改变处理Handler
     * 三者相互联动；三者都有值情况，修改天数，变结束；修改开始或者结束，都变天数
     * */
    function startTimeChangeHandler(){
        let days = $('#days').val();
        let startTime = $('#startTime').val();
        let endTime = $('#endTime').val();
        if(startTime && !moment(startTime,"YYYY-MM-DD",true).isValid()){
            $('#days').val('');
            return false;
        }
        if(startTime){
            //开始结束日期变更优先计算天数
            if(endTime){
                $('#days').val(moment(endTime).diff(moment(startTime),'days') + 1);
                $("#saveForm").validate().element($("#days"));
                return false;
            }

            if(days){
                $('#endTime').val(moment(startTime).add(days-1,"days").format("YYYY-MM-DD"));
                return false;
            }
        }
    }

    /**
     * 结束日期值改变处理Handler
     * 三者相互联动；三者都有值情况，修改天数，变结束；修改开始或者结束，都变天数
     * */
    function endTimeChangeHandler(){
        let days = $('#days').val();
        let startTime = $('#startTime').val();
        let endTime = $('#endTime').val();
        if(endTime && !moment(endTime,"YYYY-MM-DD",true).isValid()){
            $('#days').val('');
            return;
        }
        if(endTime){
            //开始结束日期变更优先计算天数
            if(startTime){
                $('#days').val(moment(endTime).diff(moment(startTime),'days') + 1);
                return;
            }
            if(days){
                $('#startTime').val(moment(endTime).subtract(days-1,"days").format("YYYY-MM-DD"));
                return;
            }
        }
    }

    /**
     * ---------------天数/开始/结束日期联动 end ---------------
     * */

    // 通过区域等信息查询摊位
    function viewAssetsByNoTable(){
        $('.booth-data-origin').html('');
        $.ajax({
            type: "POST",
            url: "/assetsRentalItem/viewAssetsByNoTable.action",
            data: JSON.stringify($("#searchForm").serializeObject()),
            dataType: "json",
            async: false,
            contentType: "application/json",
            success: function (res) {
                if(res.success){
                    $('.booth-data-origin').append(bui.util.HTMLDecode(template('boothCheckItem', {booth: res.data})));
                } else {
                    bs4pop.alert(res.message, {type: 'error'});
                }
            },
            error: function (error) {
                bs4pop.alert('远程访问失败', {type: 'error'});
            }
        });
    }

    // 我去整个了商户2，还是区域区分为 三产和广告位，属于不同的2个子商户

    // 勾选待选摊位时，需同属于一个商户下。
    var MerchantsId = '';
    $(document).on('change', '.booth-data-origin .custom-control-input', function () {
        if($(this).is(':checked')){
            let id = $(this).attr('data-id');
            let firstDistrictId = $(this).data('first-area') ;
            let secondDistrictId = $(this).data('second-area') ;
            let that = $(this);

            if(firstDistrictId == 'undefined'){ firstDistrictId = ''};
            if(secondDistrictId == 'undefined'){ secondDistrictId = ''};
            $.ajax({
                type: "POST",
                url: "/assetsRentalItem/getMchIdByDistrictId.action",
                data: JSON.stringify({ firstDistrictId, secondDistrictId }),
                dataType: "json",
                async: false,
                contentType: "application/json",
                success: function (res) {
                    if(res.success){
                        if(MerchantsId == ''){
                            MerchantsId = res.data;
                        } else if( MerchantsId == res.data){
                            // $('.booth-checked').append(that.parents('.custom-checkbox')[0].outerHTML)
                            // that.parents('.custom-control').remove();

                        } else if( MerchantsId != res.data){
                            that.prop('checked', false)
                            bs4pop.notice('不属于同一个商户', {position: 'topcenter', type: 'danger'});
                        }
                    } else {
                        bs4pop.alert(res.message, {type: 'error'});
                    }
                },
                error: function (error) {
                    bs4pop.alert('远程访问失败', {type: 'error'});
                }
            });
        } else if (!$('.booth-data-origin .custom-control-input:checked').length){
            MerchantsId = '';
        }
    })

    // 移动摊位
    function moveBooth( origin , target){
        let str = '';
        $.each(origin.find('.custom-control-input:checked'), function (index, el) {
            str += $(el).parents('.custom-control')[0].outerHTML;
            $(el).parents('.custom-control').remove();
        })
        target.append(str);
    }

    // 选定勾选的待选摊位
    $('#checkedBoothBtn').on('click', function () {
        moveBooth( $('.booth-data-origin') , $('.booth-checked'));
    })

    // 移出勾选的已选摊位
    $('#uncheckedBoothBtn').on('click', function () {
        moveBooth( $('.booth-checked') , $('.booth-data-origin'));
    })


    // 待选摊位全选
    $(document).on('change', '#checkAll', function () {
        if($(this).is(':checked')) {
            //未完成-----------------------等杨刚接口------------
            let id = $('.booth-data-origin .custom-control:first-child').data('id');
            let mchId = $('.booth-data-origin .custom-control:first-child').data('mch-id')
        }
    })

    // 已选摊位全选
    $(document).on('change', '#uncheckAll', function () {
        if($(this).is(':checked')) {
            $('.booth-checked .custom-control-input').prop('checked', true);
        } else {
            $('.booth-checked .custom-control-input').prop('checked', false);
        }
    })

    // 提交保存
    function doSaveAssetsRentalHandler(){
        let validator = $('#saveForm').validate({ignore:''})
        let url = '';
        let boothCheckedData = [];
        if (!validator.form()) {
            return false;
        }
        $("#nameHidden").val($("#name").val());

        // 构建已选摊位数据
        $.each($('.booth-checked .custom-control .custom-control-input'), function (index, item ) {
            
            let assets_id = $(item).data('id');
            let assets_name = $(item).data('name');
            let area = $(item).data('first-area');
            let areaName = $(item).data('first-area-name');
            let secondArea = $(item).data('second-area');
            let secondAreaName = $(item).data('second-area-name');
            let type = $(item).data('type');
            let number = $(item).data('number');
            let unit = $(item).data('unit');
            let corner = $(item).data('corner');
            boothCheckedData.push({ assets_id, assets_name, area, areaName, secondArea, secondAreaName, type, number, unit, corner});
        })

        let buildData = JSON.stringify($.extend({}, $('#saveForm').serializeObject(), {assetsRentalItemList: boothCheckedData, mchId: MerchantsId}));
        bui.loading.show('努力提交中，请稍候。。。');

        if($('#id').val()){
            url = "/assetsRental/update.action";
        } else {
            url = "/assetsRental/add.action";
        }
        $.ajax({
            type: "POST",
            url: url,
            data: buildData,
            dataType: "json",
            contentType: "application/json",
            success: function (ret) {
                bui.loading.hide();
                if(!ret.success){
                    bs4pop.alert(ret.message, {type: 'error'});
                }else{
                    parent.closeDialog(parent.dia);
                }
            },
            error: function (error) {
                bui.loading.hide();
                bs4pop.alert('远程访问失败', {type: 'error'});
            }
        });
    }

    $('#save').on('click', bui.util.debounce(doSaveAssetsRentalHandler,1000,true));

</script>

