<div class="input-group">
    <select class="form-control" id="firstDistrict" name="firstDistrict" required>
    </select>
    <select class="form-control" id="secondDistrict" name="secondDistrict">
        <option value="">-- 全部 --</option>
    </select>
</div>
<script>
    // 初始化一级区域
    function initFirstDistrict(firstId){
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
    function initSecondDistrict(firstId, secondId){
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
                debugger
                if(secondId) {
                    $('#secondDistrict').val(secondId)
                }
            }
        })
    }

    //一级区域变动事件
    $(document).on('change', '#firstDistrict', function() {
        let firstId = $('#firstDistrict').val();
        if (!firstId) {
            return false;
        }
        $('#secondDistrict').html('<option value="">-- 全部 --</option>');
        initSecondDistrict(firstId)
    });

</script>
