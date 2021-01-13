<div class="input-group" data-option-text="${_optionText!}">
    <select class="form-control" id="firstDistrictId" name="${_firstDistrictName!}" <%if(isNotEmpty(_isRequiredFirst) && _isRequiredFirst == "true"){%>  required <%}%> data-grade="first">
        <option value="${_firstDistrictIdValue!}"  data-name="${_firstDistrictTextValue!}" ></option>
    </select>
    <input type="hidden" id="firstDistrictText" name="${_firstDistrictTextName!}" value="${_firstDistrictTextValue!}">
    <select class="form-control" id="secondDistrictId" name="${_secondDistrictName!}" <%if(isNotEmpty(_isRequiredSecond) && _isRequiredSecond == "true"){%> required <%}%> data-grade="second" >
        <option value="${_secondDistrictIdValue!}" data-name="${_secondDistrictTextValue!}"></option>
    </select>
    <input  type="hidden" id="secondDistrictText" name="${_secondDistrictTextName!}" value="${_secondDistrictTextValue!}">
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
        let optionText = $('.input-group').data('option-text') || '-- 请选择 --';
        $.ajax({
            type: "get",
            url: "/assets/searchDistrict.action",
            success: function (res) {
                let str = '<option value="">'+ optionText + '</option>';
                $.each(res.data, function(index, item){
                    str +=  '<option value="' + item.id + '" data-name="' + item.name + '">' + item.name + '</option>';
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
        let optionText = $('.input-group').data('option-text') || '-- 请选择 --';
        $('#secondDistrictId').html('<option value="">'+ optionText + '</option>');

        if(!firstId) {return false;}
        $.ajax({
            type: "get",
            url: "/assets/searchDistrict.action",
            data: {parentId: parseInt(firstId)},
            success: function (res) {
                let str = '<option value="">'+ optionText + '</option>';
                let currentIds = res.data.map(function(item, index){ return item.id});
                $.each(res.data, function(index, item){
                    str +=  '<option value="' + item.id + '" data-name="' + item.name + '">' + item.name + '</option>';
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

    // 选中区域时也要保存name值
    function valueDistrictName(el){
        let grade = el.data('grade');
        let name = $('#'+ grade +'DistrictId :selected').data('name');
        $('#' + grade + 'DistrictText').val(name);
        if(grade == 'first'){
            $('#secondDistrictText').val('');
        }
    }


    //一级区域变动事件
    $(document).on('change', '#firstDistrictId', function() {
        initSecondDistrict()
    });

</script>
