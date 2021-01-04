<script>
    /**
     *
     * @Date 2019-11-06 17:30:00
     * @author jiangchengyong
     *
     ***/
    //行索引计数器
    let itemIndex = 0;
    var TheMerchantsId = $('.booth-checked .custom-control-input').data('mch-id');

    //品类搜索
    //品类搜索自动完成
    var categoryAutoCompleteOption = {
        width: '100%',
        language: 'zh-CN',
        maximumSelectionLength: 10,
        ajax: {
            type:'get',
            url: '/stock/categoryCycle/searchV2.action',
            data: function (params) {
                return {
                    keyword: params.term,
                }
            },
            processResults: function (result) {
                if(result.success){
                    let data = result.data;
                    return {
                        results: $.map(data, function (dataItem) {
                            dataItem.text = dataItem.name + (dataItem.cusName ? '(' + dataItem.cusName + ')' : '');
                            return dataItem;
                        })
                    };
                }else{
                    bs4pop.alert(result.message, {type: 'error'});
                    return;
                }
            }
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
    $(document).on('change', '.booth-data-origin .custom-control-input', function () {
        let checkingLen = $('.booth-data-origin .custom-control-input:checked').length;
        let checkedLen = $('.booth-checked .custom-control-input').length;

        console.log('商户ID：', TheMerchantsId)

        if(!checkedLen) {
            if (!checkingLen) {
                // 一个都没选中时，取消所有勾选时
                TheMerchantsId = '';
            } else if (checkingLen == 1) {
                // 首个被选中时，商户id设为这个的商户id
                TheMerchantsId = $(this).data('mch-id');
            } else if ($(this).data('mch-id') != TheMerchantsId){
                $(this).prop('checked', false)
                bs4pop.notice('不属于同一个商户', {position: 'topcenter', type: 'danger'});
            }
        } else {
            if ($(this).data('mch-id') != TheMerchantsId){
                $(this).prop('checked', false)
                bs4pop.notice('不属于同一个商户', {position: 'topcenter', type: 'danger'});
            }
        }
        console.log('商户ID：', TheMerchantsId)
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
        $('#checkAll').prop('checked', false);
    })

    // 移出勾选的已选摊位
    $('#uncheckedBoothBtn').on('click', function () {
        moveBooth( $('.booth-checked') , $('.booth-data-origin'));
        $('#uncheckAll').prop('checked', false);
    })


    // 待选摊位全选
    $(document).on('change', '#checkAll', function () {
        if($(this).is(':checked')) {
            //未完成-----------------------等杨刚接口------------
            let firstMchId = $('.booth-data-origin .custom-control:first-child .custom-control-input').data('mch-id')

            $.each($('.booth-data-origin .custom-control .custom-control-input'), function (index, item) {
                debugger
                if($(item).data('mch-id') == firstMchId) {
                    $(item).prop('checked', true);
                }
            })


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
        let categoryId = [];
        let categoryName = [];
        let ell = $('#detailInfo .form-control');
        let detailInfo = [$('#engageCode'), $('#leaseTermCode'), $('#days'), $('#startTime'), $('#startTime'), $('#categorys')].some(function(el, index){
            return ($(el).val() != '')
        });

        console.log('detailInfo', detailInfo)
        if (!validator.form() || !detailInfo) {
            return false;
        }

        $.map($('#categorys').select2('data'), function (item) {
            debugger
            categoryId.push(parseInt(item.id))
            categoryName.push(item.text)
        });



        $("#nameHidden").val($("#name").val());

        // 构建已选摊位数据
        $.each($('.booth-checked .custom-control .custom-control-input'), function (index, item ) {
            
            let assetsId = $(item).data('id');
            let assetsName = $(item).data('name');
            let firstDistrictId = $(item).data('first-area');
            let firstDistrictName = $(item).data('first-area-name');
            let secondDistrictId = $(item).data('second-area');
            let secondDistrictName = $(item).data('second-area-name');
            let assetsType = $(item).data('type');
            let number = $(item).data('number');
            let unit = $(item).data('unit');
            let corner = $(item).data('corner');
            boothCheckedData.push({ assetsId, assetsName, firstDistrictId, firstDistrictName, secondDistrictId, secondDistrictName, assetsType, number, unit, corner});
        })


        let buildData = JSON.stringify($.extend({}, $('#saveForm :not(#categorys)').serializeObject(), {categoryId: categoryId.join(), categoryName: categoryName.join()},  {assetsRentalItemList: boothCheckedData, mchId: TheMerchantsId}));
        bui.loading.show('努力提交中，请稍候。。。');
        console.log('buildData:',  buildData)

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

