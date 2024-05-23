let btnDeletePost = null, modalDeletePost = null;
function confirmToDeletePost(postId){
    btnDeletePost = $('#btnConfirmModal');
    modalDeletePost = $('#confirmModal');
    modalDeletePost.find('.modal-title').text("Delete Post");
    modalDeletePost.find('.modal-body').text("Are you sure to delete this post?");
    modalDeletePost.find('#btnCancelModal').show().text("Cancel");
    modalDeletePost.modal('show');
    btnDeletePost.attr('data-postId', postId);
    btnDeletePost.attr('data-action', 'delete-post');
    btnDeletePost.text('Delete');
}

function deletePost(postId){
    $.ajax({
        url: '/api/posts/' + postId,
        type: 'DELETE',
        success: function(response) {
            if(response.code === 200) {
                $('#post-' + postId).remove();
            }
            modalDeletePost.modal('hide');
        },
        error: function(xhr) {
            alert('Error: ' + xhr.responseText);
        }
    });
}
