let btnConfirmDeletePost = null, modalConfirmDeletePost = null;
function confirmToDeletePost(postId){
    btnConfirmDeletePost = $('#btnConfirmModal');
    modalConfirmDeletePost = $('#confirmModal');
    modalConfirmDeletePost.find('.modal-title').text("Delete Post");
    modalConfirmDeletePost.find('.modal-body').text("Are you sure to delete this post?");
    modalConfirmDeletePost.find('#btnCancelModal').show().text("Cancel");
    modalConfirmDeletePost.modal('show');
    btnConfirmDeletePost.attr('data-postId', postId);
    btnConfirmDeletePost.attr('data-action', 'delete-post');
    btnConfirmDeletePost.text('Delete');
}

function deletePost(postId){
    $.ajax({
        url: '/api/posts/' + postId,
        type: 'DELETE',
        success: function(response) {
            if(response.code === 200) {
                $('#post-' + postId).remove();
            }
            modalConfirmDeletePost.modal('hide');
        },
        error: function(xhr) {
            alert('Error: ' + xhr.responseText);
        }
    });
}
