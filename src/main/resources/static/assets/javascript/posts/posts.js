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
}
