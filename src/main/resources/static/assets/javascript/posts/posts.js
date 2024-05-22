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
}
