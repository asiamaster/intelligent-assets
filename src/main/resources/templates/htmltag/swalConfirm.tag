    swal({
        title: '${swalTitle}',
        type: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'ȷ��',
        cancelButtonText: 'ȡ��',
        confirmButtonClass: 'btn btn-success',
        cancelButtonClass: 'btn btn-danger'
    }).then(function (flag) {
        if (flag.dismiss == "cancel" || flag.dismiss == "overlay" || flag.dismiss == "esc" || flag.dismiss == "close") {
            return;
        }
        ${tag.body}
    });