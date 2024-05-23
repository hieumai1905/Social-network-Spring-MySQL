function registerModalEvent() {
    let $createPostModal = $('#createPostModal');
    let $tagUsersModal = $('#tag-friends');

    $createPostModal.on('show.bs.modal', function () {
        let tagUsersModalInstance = bootstrap.Modal.getInstance($tagUsersModal[0]);
        if (tagUsersModalInstance) {
            tagUsersModalInstance.hide();
        }
    });

    $tagUsersModal.on('show.bs.modal', function () {
        let createPostModalInstance = bootstrap.Modal.getInstance($createPostModal[0]);
        if (createPostModalInstance) {
            createPostModalInstance.hide();
        }
    });

    $tagUsersModal.on('hidden.bs.modal', function () {
        let createPostModalInstance = bootstrap.Modal.getInstance($createPostModal[0]);
        createPostModalInstance.show();
    });

    $('#btnConfirmModal').on('click', function(e){
        e.preventDefault();
        let postId = $(this).attr('data-postId');
        let action = $(this).attr('data-action');
        if(action === 'delete-post'){
            deletePost(postId);
        }
    });
    $('.save-post').on('click', function() {
        let postId = $(this).attr('data-postId');
        updatePostInteract('saved', postId);
    });
    $('.hide-post').on('click', function() {
        let postId = $(this).attr('data-postId');
        updatePostInteract('hidden', postId);
    });
}

function updatePostInteract(type, postId){
    $.ajax({
        url: `/api/posts/${postId}/interact/${type}`,
        type: 'POST',
        success: function(response) {
            console.log('Success:', response);
            if(response.code === 200){
                let btnDeletePost = $('#btnConfirmModal');
                let modalDeletePost = $('#confirmModal');
                modalDeletePost.find('.modal-title').text("Notification");
                modalDeletePost.find('.modal-body').text(response.message);
                modalDeletePost.find('#btnCancelModal').hide();
                modalDeletePost.modal('show');
                btnDeletePost.text('Ok');
                if(type === 'hidden')
                    $('#post-' + postId).remove();
            }
        },
        error: function(xhr, status, error) {
            console.error('Error:', error);
            if(xhr.responseJSON && xhr.responseJSON.message) {
                console.log('Error: ' + xhr.responseJSON.message);
            } else {
                console.log('An error occurred: ' + error);
            }
        }
    });
}
