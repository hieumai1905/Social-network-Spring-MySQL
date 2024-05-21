function loadComment() {
    let btnShowComments = $('.btn-show-comment');
    let comments = $('.comments-list');
    if (btnShowComments.length > 0) {
        btnShowComments.each(function(i) {
            $(this).click(function(event) {
                event.preventDefault();
                comments.eq(i).toggleClass('d-none');
            });
        });
    }

    let btnCommentsReply = $('.btn-comment-reply')
    let commentsReply = $('.comments-reply-list');
    if (btnCommentsReply.length > 0) {
        btnCommentsReply.each(function(i) {
            $(this).click(function(event) {
                event.preventDefault();
                commentsReply.eq(i).toggleClass('d-none');
            });
        });
    }
    let btnComments = $('.btn-comment');
    btnComments.each(function(i) {
        $(this).click(function(event) {
            alert("comment")
        });
    });
}
