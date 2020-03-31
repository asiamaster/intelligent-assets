<script>
    /**
     *
     * @Date 2019-11-06 17:30:00
     * @author jiangchengyong
     *
     ***/

        //行索引计数器
    let itemIndex = 0;

    //对应摊位
    $(function () {
        addBoothItem();
    });


    //获取table Index
    function getIndex(str) {
        return str.split('_')[1];
    }

    //初始化刷卡
    initSwipeCard({
        id:'getCustomer',
    });

    var boothAutoCompleteOption = {
        width: '350',
        paramName: 'keyword',
        displayFieldName: 'name',
        serviceUrl: '/booth/search.action',
        selectFn: function (suggestion, that) {
            $(that).siblings('input').val(suggestion.id)
        },
        transformResult: function (result) {
            return {
                suggestions: $.map(result, function (dataItem) {
                    return $.extend(dataItem, {
                            value: dataItem.name + '(' + (dataItem.secondAreaName?dataItem.secondAreaName : dataItem.areaName) + ')'
                        }
                    );
                })
            }
        }
    }

    /**
     * 添加摊位
     * */
    function addBoothItem(){
        $('#boothTable tbody').append(bui.util.HTMLDecode(template('boothItem', {index: ++itemIndex})))
    }

    // 添加摊位
    $('#addBooth').on('click', function () {
        addBoothItem();
    })

    //删除行事件 （删除摊位行）
    $(document).on('click', '.item-del', function () {
        if ($('#boothTable tr').length > 2) {
            $(this).closest('tr').remove();
        }
    });

    /**
     * 判断数组中的元素是否重复出现
     * 验证重复元素，有重复返回true；否则返回false
     * @param arr
     * @returns {boolean}
     */
    function arrRepeatCheck(arr) {
        var hash = {};
        for(var i in arr) {
            if(hash[arr[i]]) {
                return true;
            }
            // 不存在该元素，则赋值为true，可以赋任意值，相应的修改if判断条件即可
            hash[arr[i]] = true;
        }
        return false;
    }

    function buildFormData(){
        // let formData = new FormData($('#saveForm')[0]);
        let formData = $("input:not(table input),textarea,select").serializeObject();
        let earnestOrderdetails = [];
        bui.util.yuanToCentForMoneyEl(formData);
        $("#boothTable tbody").find("tr").each(function(){
            let earnestOrderdetail = {};
            $(this).find("input").each(function(t,el){
                let fieldName = $(this).attr("name").split('_')[0];
                if ($(this).val() != ""){
                    earnestOrderdetail[fieldName] = $(this).val();
                }
            });
            if (earnestOrderdetail != {}){
                earnestOrderdetails.push(earnestOrderdetail);
            }
        });

        $.extend(formData,{earnestOrderdetails});
        return formData;
    }

    // 提交保存
    $('#formSubmit').on('click', function (e) {
        if (!$('#saveForm').valid()) {
            return false;
        }

        let boothIds = $("table input[name^='assetsId']").filter(function () {
            return this.value
        }).map(function(){
            return $('#assetsId_'+getIndex(this.id)).val();
        }).get();

        if(arrRepeatCheck(boothIds)){
            bs4pop.alert('存在重复摊位，请检查！');
            return false;
        }
        bui.loading.show('努力提交中，请稍候。。。');
        // let _formData = new FormData($('#saveForm')[0]);
        $.ajax({
            type: "POST",
            url: "${contextPath}/earnestOrder/doAdd.action",
            data: buildFormData(),
            dataType: "json",
            async : false,
            success: function (ret) {
                bui.loading.hide();
                if(!ret.success){
                    bs4pop.alert(ret.message, {type: 'error'},function () {
                        parent.closeDialog(parent.dia);
                    });
                }else{
                    parent.closeDialog(parent.dia);
                }
            },
            error: function (error) {
                bui.loading.hide();
                bs4pop.alert('远程访问失败', {type: 'error'});
            }
        });
    });

</script>