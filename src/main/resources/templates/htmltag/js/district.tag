<div class="input-group">
    <select class="form-control" id="firstDistrictId" name="firstDistrictId" required>
        <option value="${firstDistrictId!}"></option>
    </select>
    <select class="form-control" id="secondDistrictId" name="secondDistrictId">
        <option value="${secondDistrictId!}"></option>
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
        let firstId = $('#firstDistrictId').val();
        $.ajax({
            type: "get",
            url: "/assets/searchDistrict.action",
            success: function (res) {
                let str = '<option value="">-- 请选择 --</option>';
                $.each(res.data, function(index, item){
                    str +=  '<option value="' + item.id + '">' + item.name + '</option>';
                })
                $('#firstDistrictId').html(str);
                if(firstId) {
                    $('#firstDistrictId').val(firstId);
                }
            }
        })
    }
    function initSecondDistrict(){
        let firstId = $('#firstDistrictId').val();
        let secondId = parseInt($('#secondDistrictId').val());
        $('#secondDistrictId').html('<option value="">-- 请选择 --</option>');

        if(!firstId) {return false;}
        $.ajax({
            type: "get",
            url: "/assets/searchDistrict.action",
            data: {parentId: parseInt(firstId)},
            success: function (res) {
                let str = '<option value="">-- 请选择 --</option>';
                let currentIds = res.data.map(function(item, index){ return item.id});
                $.each(res.data, function(index, item){
                    str +=  '<option value="' + item.id + '">' + item.name + '</option>';
                })
                $('#secondDistrictId').html(str);
                if(secondId && currentIds.includes(secondId)) {
                    $('#secondDistrictId').val(secondId);
                } else {
                    $('#secondDistrictId option:first-child').prop('selected', true);
                }
            }
        })
    }

    //一级区域变动事件
    $(document).on('change', '#firstDistrictId', function() {
        initSecondDistrict()
    });

</script>
