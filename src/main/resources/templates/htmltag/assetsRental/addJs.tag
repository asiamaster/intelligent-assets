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
        serviceUrl: '/stock/categoryCycle/search.action',
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


    function viewAssetsByNoTable(){
        $.ajax({
            type: "POST",
            url: "/assetsRentalItem/viewAssetsByNoTable.action",
            data: JSON.stringify($("#searchForm").serializeObject()),
            dataType: "json",
            async: false,
            contentType: "application/json",
            success: function (res) {
                if(res.success){
                    $.each(res.data, (index, item)=>{
                        $('.booth-data-origin').append('<div class="custom-control custom-checkbox"><input type="checkbox" class="custom-control-input" id="booth_'+ item.id +'" data-id="'+ item.id +'"  data-area="'+ item.area +'" data-area-name="'+ item.areaName +'" data-secondarea="'+ item.secondArea +'" ><label class="custom-control-label" for="booth_'+ item.id +'">' + item.name + ' 【' + item.areaName + '】' + item.id  + '</label></div>')

                    })
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
    var boothCheckedStr = '';
    var boothCheckedData  = [];
    var MerchantsId = '';
    $(document).on('change', '.booth-data-origin .custom-control-input', function () {
        if($(this).is(':checked')){
            let id = $(this).attr('data-id');
            let name = $(this).attr('data-name');
            let firstDistrictId = $(this).data('area') ;
            let secondDistrictId = $(this).data('secondarea') ;
            let areaName = $(this).attr('data-area-name');
            let that = $(this);
            if(firstDistrictId == 'undefined'){ firstDistrictId = ''};
            if(secondDistrictId == 'undefined'){ secondDistrictId = ''};
            $.ajax({
                type: "POST",
                url: "/assetsRentalItem/getMchIdByDistrictId.action",
                // data: JSON.stringify({ assetsId: id}),
                data: JSON.stringify({ firstDistrictId, secondDistrictId }),
                dataType: "json",
                async: false,
                contentType: "application/json",
                success: function (res) {
                    if(res.success){
                        debugger
                        // 传的值挺多的，id 换做 assets_id，name 换做 assets_name ，type换做 assets_type ，number，unit corner 6个字段
                        if(!boothCheckedData.length){
                            MerchantsId = res.data;
                        }
                        if( MerchantsId == res.data){
                            boothCheckedData.push(id);
                            $('.booth-checked').append(that.parents('.custom-checkbox')[0].outerHTML)
                            that.parents('.custom-control').remove();
                        } else {
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
            // $('.booth-checked').append('<div class="custom-control custom-checkbox"><input type="checkbox" checked class="custom-control-input" id="booth_'+ id +'" data-id="'+ id +'"><label class="custom-control-label" for="booth_'+ id +'">' + name + ' 【' + areaName + '】' + id  + '</label></div>')
        }
    })

    $(document).on('change', '.booth-checked .custom-control-input', function () {
        if($(this).is(':checked')) {
            let id = $(this).attr('data-id');
            let name = $(this).attr('data-name');
            let firstDistrictId = $(this).data('area');
            let secondDistrictId = $(this).data('secondarea');
            let areaName = $(this).attr('data-area-name');
            let that = $(this);
            $('.booth-data-origin').append(that.parents('.custom-checkbox')[0].outerHTML)
            that.parents('.custom-control').remove();
            boothCheckedData.splice(boothCheckedData.indexOf(id), 1)
        }
    })

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

    // 提交保存
    function doUpdateAssetsRentalHandler(){
        let validator = $('#saveForm').validate({ignore:''})
        if (!validator.form()) {
            return false;
        }
        let buildData = JSON.stringify({}, $.extend($("#saveForm").serializeObject()), {assetsRentalItemList: boothCheckedData})
        bui.loading.show('努力提交中，请稍候。。。');
        $.ajax({
            type: "POST",
            url: "/assetsRental/update.action",
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

    // 提交保存
    function doSaveAssetsRentalHandler(){
        let validator = $('#saveForm').validate({ignore:''})
        if (!validator.form()) {
            return false;
        }
        $("#nameHidden").val($("#name").val());
        let buildData = JSON.stringify($.extend({}, $('#saveForm').serializeObject(), {assetsRentalItemList: boothCheckedData}))
        bui.loading.show('努力提交中，请稍候。。。');
        $.ajax({
            type: "POST",
            url: "/assetsRental/add.action",
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
    $('#update').on('click', bui.util.debounce(doUpdateAssetsRentalHandler,1000,true));

</script>

