let postId = null, reportTable = null, btnViewPost = null, btnDeletePost = null;
$(window).on('load', function() {
    reportTable = $('#reportTable');
    btnViewPost = $('.view-post');
    btnDeletePost = $('.delete-post');
    reportTable.DataTable({
        "paging": true,
        "ordering": true,
        "info": true,
    });

    reportTable.on('draw.dt', function() {
        btnDeletePost.off('click').on('click', function(){
            postId = $(this).attr('data-postId');
            deletePost(postId);
        });
        btnViewPost.off('click').on('click', function(){
            postId = $(this).attr('data-postId');
            openInNewTab(postId);
        });
    });

    btnDeletePost.on('click', function(){
       postId = $(this).attr('data-postId');
       deletePost(postId);
    });
    btnViewPost.on('click', function(){
        postId = $(this).attr('data-postId');
        openInNewTab(postId);
    });
});

function openInNewTab(postId) {
    var url = '/details/posts?id=' + postId;
    var a = document.createElement('a');
    a.href = url;
    a.target = '_blank';
    a.rel = 'noopener noreferrer';
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
}


function deletePost(postId){
    $.ajax({
        url: '/api/posts/' + postId,
        type: 'DELETE',
        success: function(response) {
            if(response.code === 200) {
                $('#tr-post-' + postId).remove();
            }
        },
        error: function(xhr) {
            alert('Error: ' + xhr.responseText);
        }
    });
}
