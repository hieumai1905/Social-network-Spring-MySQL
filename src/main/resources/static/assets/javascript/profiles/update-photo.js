let photoModal = null, btnUpdatePhoto = null, previewPhoto = null, defaultPhoto = '',
photoFileInput = null, currentCoverPhoto = null;
function registerUpdatePhotoEvents() {
    photoModal = $("#photoModal");
    btnUpdatePhoto = $("#btnUpdatePhoto");
    previewPhoto = $("#previewPhoto");
    photoFileInput = $('#photo-file');
    currentCoverPhoto = $("#currentUserCoverPhoto");
    $("#update-avatar").on("click", function(){
        defaultPhoto = $(this).attr("data-avatar");
        photoModal.find("#photoModalLabel").text("Update avatar");
        btnUpdatePhoto.attr("type", "CHANGE_AVATAR");
        previewPhoto.attr("src", defaultPhoto);
        photoModal.modal("show");
    });
    currentCoverPhoto.on("click", function(){
        defaultPhoto = $(this).attr("data-cover-photo");
        photoModal.find("#photoModalLabel").text("Update cover photo");
        btnUpdatePhoto.attr("type", "CHANGE_COVER");
        previewPhoto.attr("src", defaultPhoto);
        photoModal.modal("show");
    });
    photoFileInput.on('change', function() {
        const fileInput = $(this)[0];

        if (fileInput.files && fileInput.files[0]) {
            const reader = new FileReader();

            reader.onload = function(e) {
                previewPhoto.attr('src', e.target.result);
            }

            reader.readAsDataURL(fileInput.files[0]);
        } else {
            previewPhoto.attr('src', defaultPhoto);
        }
    });
    btnUpdatePhoto.on("click", function() {
        let type = $(this).attr("type");
        updatePhoto(type);
    });
}

function updatePhoto(type) {
    const fileInput = photoFileInput[0];
    if (!fileInput.files || !fileInput.files[0]) {
        return;
    }
    let formData = new FormData();
    formData.append('access', $("#postAccess").val());
    formData.append('content', "");
    formData.append('postType', type);
    formData.append('files', fileInput.files[0]);
    $.ajax({
        url: '/api/posts',
        type: 'POST',
        data: formData,
        processData: false,
        contentType: false,
        success: function(response) {
            console.log('Save successfully:', response);
            if(response.code === 201) {
                savePostToUI(response.data, true, false);
                if(type === "CHANGE_AVATAR") {
                    $(".current-user-avatar").attr("src", response.data.medias[0]);
                    $("#update-avatar").attr("data-avatar", response.data.medias[0]);
                }
                else if(type === "CHANGE_COVER") {
                    currentCoverPhoto.attr("src", response.data.medias[0]);
                    currentCoverPhoto.attr("data-cover-photo", response.data.medias[0]);
                }
                photoModal.modal('hide');
            }
        },
        error: function(xhr, status, error) {
            console.error('Error save post:', error);
        }
    });
}
