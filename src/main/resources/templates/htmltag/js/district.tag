<div class="input-group">
    <select class="form-control" id="firstDistrict" name="firstDistrict" >
        <option value="${firstDistrictID!}"></option>
    </select>
    <select class="form-control" id="secondDistrict" name="secondDistrict">
        <option value="${secondDistrictID!}">-- 全部 --</option>
    </select>
</div>
<script>

    $(function () {
        // 初始化一级区域
         initFirstDistrict();
         initSecondDistrict()
        // initFirstDistrict(8);
        // initSecondDistrict(8, 43)
    });

    // 初始化一级区域
    function initFirstDistrict(){
        debugger
        let firstId = $('#firstDistrict').val()
        $.ajax({
            type: "get",
            url: "/assets/searchDistrict.action",
            success: function (res) {
                let str = '<option value="">-- 全部 --</option>'
                $.each(res.data, function(index, item){
                    str +=  '<option value="' + item.id + '">' + item.name + '</option>'
                })
                $('#firstDistrict').html(str)
                if(firstId) {
                    $('#firstDistrict').val(firstId)
                }
            }
        })
    }
    function initSecondDistrict(){
        let firstId = $('#firstDistrict').val()
        let secondId = $('#secondDistrict').val()
        if(!firstId) {return false;}
        $('#secondDistrict').html('<option value="">-- 全部 --</option>');
        $.ajax({
            type: "get",
            url: "/assets/searchDistrict.action",
            data: {parentId: parseInt(firstId)},
            success: function (res) {
                let str = '<option value="">-- 全部 --</option>'
                $.each(res.data, function(index, item){
                    str +=  '<option value="' + item.id + '">' + item.name + '</option>'
                })
                $('#secondDistrict').html(str)
                if(secondId) {
                    $('#secondDistrict').val(secondId)
                }
            }
        })
    }

    //一级区域变动事件
    $(document).on('change', '#firstDistrict', function() {
        initSecondDistrict()
    });

</script>
