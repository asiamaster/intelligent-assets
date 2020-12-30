<!--车辆信息添加表格-->
<script id="boothCheckItem" type="text/html">
    {{each booth as data}}
        <div class="custom-control custom-checkbox">
            <input type="checkbox" class="custom-control-input" id="{{data.id}}" data-mch-id="{{data.marketId}}" data-id="{{data.id}}" data-name="{{data.name}}" data-first-area="{{data.area}}" data-first-area-name="{{data.areaName}}" data-second-area="{{data.secondArea}}" data-second-area-name="{{data.secondAreaName}}" data-type="{{data.type}}" data-number="{{data.number}}" data-unit="{{data.unit}}" data-corner="{{data.corner}}">
            <label class="custom-control-label" for="{{data.id}}">{{data.name}}【{{data.areaName}}】</label>
        </div>
    {{/each}}
</script>

