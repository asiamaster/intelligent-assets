<div class="input-group">
    <select class="form-control" id="firstDistrict" name="firstDistrict" >
        <option value="${firstDistrictID!}"></option>
    </select>
    <select class="form-control" id="secondDistrict" name="secondDistrict">
        <option value="${secondDistrictID!}"></option>
    </select>
</div>
<script>

    $(function () {
        // 初始化一级区域
         initFirstDistrict();
         initSecondDistrict()
    });

    // 初始化一级区域:如果是修改初始化后赋值。
    function initFirstDistrict(){
        let firstId = $('#firstDistrict').val();
        $.ajax({
            type: "get",
            url: "/assets/searchDistrict.action",
            success: function (res) {
                let str = '<option value="">-- 全部 --</option>';
                $.each(res.data, function(index, item){
                    str +=  '<option value="' + item.id + '">' + item.name + '</option>';
                })
                $('#firstDistrict').html(str);
                if(firstId) {
                    $('#firstDistrict').val(firstId);
                }
            }
        })
    }
    function initSecondDistrict(){
        let firstId = $('#firstDistrict').val();
        let secondId = parseInt($('#secondDistrict').val());
        $('#secondDistrict').html('<option value="">-- 全部 --</option>');

        if(!firstId) {return false;}
        $.ajax({
            type: "get",
            url: "/assets/searchDistrict.action",
            data: {parentId: parseInt(firstId)},
            success: function (res) {
                let str = '<option value="">-- 全部 --</option>';
                let currentIds = res.data.map(function(item, index){ return item.id});
                $.each(res.data, function(index, item){
                    str +=  '<option value="' + item.id + '">' + item.name + '</option>';
                })
                $('#secondDistrict').html(str);
                if(secondId && currentIds.includes(secondId)) {
                    $('#secondDistrict').val(secondId);
                } else {
                    $('#secondDistrict option:first-child').prop('selected', true);
                }
            }
        })
    }

    //一级区域变动事件
    $(document).on('change', '#firstDistrict', function() {
        initSecondDistrict()
    });

</script>
