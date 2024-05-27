let btnDeletePost = null,  modalDeletePost = null;
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

    $("#showPostModal").on("click", function () {
        let postModal = $("#createPostModal");
        $("#content").val("");
        $("#file-list").empty();
        files = [];
        $("#post-id").val("");
        resetUsersTag();
        postModal.find(".modal-title").text("Create post");
        postModal.modal("show");
    });

    $("#createPost").on("click", function () {
        let postId = $("#post-id").val();
        savePost(postId);
    });

    btnDeletePost = $('#btnConfirmModal');
    modalDeletePost = $('#confirmModal');
    btnDeletePost.on('click', function(e){
        e.preventDefault();
        let postId = $(this).attr('data-postId');
        let action = $(this).attr('data-action');
        if(action === 'delete-post'){
            deletePost(postId);
        }
    });
    $('.save-post').on('click', function() {
        let postId = $(this).attr('data-postId');
        updatePostInteract('saved', postId, $(this));
    });
    $('.hide-post').on('click', function() {
        let postId = $(this).attr('data-postId');
        updatePostInteract('hidden', postId, $(this));
    });
    $('.un-save-post').on('click', function() {
        let postId = $(this).attr('data-postId');
        deletePostInteract('saved', postId, $(this));
    });
    $('.un-hide-post').on('click', function() {
        let postId = $(this).attr('data-postId');
        deletePostInteract('hidden', postId, $(this));
    });
    $(".update-post").on("click", function () {
        let postId = $(this).data("postid");
        findPostById(postId);
    });
    $(".btn-share-post").on("click", function () {
        let postId = $(this).data("postid");
        updatePostInteract('shared', postId, $(this));
    });
    $(".report-post").on("click", function () {
        let postId = $(this).data("postid");
        $("#report-post-id").val(postId);
        $("#reportModal").modal("show");
    });
    $("#btnReportPost").on("click", function () {
        let postId =   $("#report-post-id").val();
        updatePostInteract('report', postId, $(this));
    });
}

function updatePostInteract(type, postId, element){
    let content = '';
    if(type === 'shared')
        content = $("#share-content-" + postId).val();
    else if(type === 'report')
    {
        let selectedValues = [];
        $('.reason-report:checked').each(function() {
            selectedValues.push($(this).val());
        });
        content = selectedValues.join(', ');
        if(content === '') {
            $("#report-message").text("Please choose at least one reason to report!");
            return;
        }
    }
    $.ajax({
        url: `/api/posts/${postId}/interact/${type}`,
        type: 'POST',
        contentType: "application/json",
        data: JSON.stringify(content),
        success: function(response) {
            console.log('Success:', response);
            if(response.code === 200){
                modalDeletePost.find('.modal-title').text("Notification");
                modalDeletePost.find('.modal-body').text(response.message);
                modalDeletePost.find('#btnCancelModal').hide();
                modalDeletePost.modal('show');
                btnDeletePost.text('Ok');
                if(type === 'hidden')
                    $('#post-' + postId).remove();
                else if(type === 'saved')
                    switchDiv(element, false, postId);
                else if(type === 'shared'){
                    $("#share-content-" + postId).val("");
                }
                else if(type === 'report'){
                    $("#post-" + postId).remove();
                    $("#reportModal").modal('hide');
                    $("#report-message").text("");
                    $('.reason-report').prop('checked', false);
                }
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

function switchDiv(element, isSaved, postId){
    element.off();
    let newDiv = isSaved ?
        $('<div>', {
            'data-postId': postId,
            'class': `update-save-post-${postId} card-body p-2 dropdown-item rounded-xxxl d-flex`,
            'html': '<i class="feather-bookmark text-grey-500 me-2 fw-600 font-sm"></i><h4 class="fw-600 text-grey-900 font-xsss mt-1">Save Post</h4>'
        }) :
        $('<div>', {
            'data-postId': postId,
            'class': `update-un-save-post-${postId} card-body p-2 dropdown-item rounded-xxxl d-flex`,
            'html': '<i class="fa fa-window-close-o text-grey-500 me-2 fw-600 font-sm"></i><h4 class="fw-600 text-grey-900 font-xsss mt-1">UnSave Post</h4>'
        });
    element.replaceWith(newDiv);
    $(isSaved ? `.update-save-post-${postId}` : `.update-un-save-post-${postId}`).on("click", function () {
        let postId = $(this).attr('data-postId');
        if(isSaved)
            updatePostInteract('saved', postId, $(this));
        else
            deletePostInteract('saved', postId, $(this));
    });
}

function deletePostInteract(type, postId, element){
    $.ajax({
        url: `/api/posts/${postId}/interact/${type}`,
        type: 'DELETE',
        success: function(response) {
            console.log('Success:', response);
            if(response.code === 204){
                modalDeletePost.find('.modal-title').text("Notification");
                modalDeletePost.find('.modal-body').text(response.message);
                modalDeletePost.find('#btnCancelModal').hide();
                modalDeletePost.modal('show');
                btnDeletePost.text('Ok');
                let interactType = $('#post-interact-type').text();
                if(interactType.toLowerCase().includes(type))
                    $('#post-' + postId).remove();
                if(type === 'saved' && interactType === '')
                    switchDiv(element, true, postId);
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

function copyLink(element){
    let postId = element.getAttribute('data-post-id');
    let link = window.location.origin + '/details/posts?id=' + postId;
    let tempInput = $('<input>');
    $('body').append(tempInput);
    tempInput.val(link).select();
    document.execCommand('copy');
    tempInput.remove();
}
