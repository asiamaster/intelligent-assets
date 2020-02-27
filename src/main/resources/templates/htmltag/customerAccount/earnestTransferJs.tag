<script>
    $('[data-refund-way="bank"]').hide();
    $('[name="refundWay"]').on('change', function () {
        if($(this).val() === '3') {
            $('[data-refund-way="bank"]').show();
        } else {
            $('[data-refund-way="bank"]').hide();
        }

    })



</script>


