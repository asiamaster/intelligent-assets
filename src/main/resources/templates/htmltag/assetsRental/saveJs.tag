<script>
    /**
     *
     * @Date 2019-11-06 17:30:00
     * @author jiangchengyong
     *
     ***/
    //行索引计数器
    let itemIndex = 0;

    var app = new Vue({
        el: '#app',
        data() {
            const generateData = _ => {
                const data = [{
                    key: 1,
                    label: '备选项1'
                },{
                    key: 2,
                    label: '备选项2'
                },{
                    key: 3,
                    label: '备选项3'
                },{
                    key: 4,
                    label: '备选项4'
                }];
                return data;
            };
            return {
                boothData: generateData(),
                boothChecked: [1, 4],
                renderFunc(h, option) {
                    // return <span>{ option.key } - { option.label }</span>;
                }
            };
        }
    })



    var categoryAutoCompleteOption = {
        width: '100%',
        language: 'zh-CN',
        minimumInputLength: 1,
        maximumSelectionLength: 10,
        ajax: {
            type:'post',
            contentType: 'application/json',
            url: '/commonData/listCusCategory.action',
            delay: 500,
            data: function (params) {
                return JSON.stringify({
                    keyword: params.term
                })
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
                    return false;
                }
            }
        }
    }

    function viewAssetsByNoTable(){
        let booth = [];
        $.ajax({
            type: "POST",
            url: "/assetsRentalItem/viewAssetsByNoTable.action",
            data: JSON.stringify($("#searchForm").serializeObject()),
            dataType: "json",
            contentType: "application/json",
            success: function (ret) {
                if(ret.success){
                    booth = res.data;
                } else {
                    bs4pop.alert(ret.message, {type: 'error'});
                }
            },
            error: function (error) {
                bs4pop.alert('远程访问失败', {type: 'error'});
            }
        });
        return booth;
    }

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
        console.log('ddd')
        let validator = $('#saveForm').validate({ignore:''})
        if (!validator.form()) {
            return false;
        }
        let buildData = $.extend($("#saveForm").serializeObject(), {booth: app.boothChecked})
        bui.loading.show('努力提交中，请稍候。。。');
        $.ajax({
            type: "POST",
            url: "/assetsRental/add.action",
            data: buildData,
            dataType: "json",
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

    $('#save').on('click', bui.util.debounce(viewAssetsByNoTable,1000,true));

</script>

