let btnLikePosts, btnLikeComments, btnLikeCommentReplys;

function handleLikePostSuccess(index, isLiked) {
    const likeCurrent = btnLikePosts.eq(index);
    const likeCount = likeCurrent.find('.like-count');
    const likeIcon = likeCurrent.find('i');

    if (isLiked) {
        likeCount.text(parseInt(likeCount.text()) + 1 || 1);
        likeIcon.addClass('bg-primary text-white').removeClass('text-grey-900');
    } else {
        likeCount.text(parseInt(likeCount.text()) - 1 || '');
        likeIcon.removeClass('bg-primary text-white').addClass('text-grey-900');
    }
}

function sendNotificationLikePost(postId, userTargetId, content) {
    const notificationContent = content !== null && content.length > 0 ? `liked your post: "${content}"` : 'liked your post';
    sendNotificationEvent(notificationContent, userCurrentId, 'POST', userTargetId, postId);
}

function likePost(postId, index, element) {
    $.ajax({
        type: "POST",
        url: `/api/likes/posts/${postId}`,
        success: function (response) {
            console.log(response.message);
            const isLiked = response.code === 201;
            handleLikePostSuccess(index, isLiked);
            if (isLiked) {
                let userTargetId = $(element).attr('data-author-id');
                let postContent = $('.content-post-item[data-post-id="' + postId + '"]').text();
                postContent = postContent.length > 12 ? postContent.substring(0, 12) + '...' : postContent;
                sendNotificationLikePost(postId, userTargetId, postContent);
            }
        },
        error: function (xhr, status, error) {
            console.error(error);
        }
    });
}

function handleLikeCommentSuccess(commentId, element, isLiked) {
    const likeIcon = $(element);
    const likeCommentCount = $(`.like-comment-count[data-comment-id="${commentId}"]`);

    if (isLiked) {
        likeIcon.addClass('bg-primary text-white').removeClass('text-grey-900');
        likeCommentCount.text(parseInt(likeCommentCount.text()) + 1 || 1);
    } else {
        likeIcon.removeClass('bg-primary text-white').addClass('text-grey-900');
        likeCommentCount.text(parseInt(likeCommentCount.text()) - 1 || '');
    }
}

function sendNotificationLikeComment(postId, userTargetId, content) {
    const notificationContent = content !== null && content.length > 0 ? `liked your comment: "${content}"` : 'liked your comment';
    sendNotificationEvent(notificationContent, userCurrentId, 'POST', userTargetId, postId);
}

function likeComment(commentId, element) {
    $.ajax({
        url: `/api/likes/comments/${commentId}`,
        type: "POST",
        success: function (response) {
            console.log(response.message);
            const isLiked = response.code === 201;
            handleLikeCommentSuccess(commentId, element, isLiked);
            if (isLiked) {
                let userTargetId = $(element).attr('data-author-id');
                let postId = $(element).attr('data-post-id');
                let commentContent = $('.content-comment[data-comment-id="' + commentId + '"]').text();
                commentContent = commentContent.length > 12 ? commentContent.substring(0, 12) + '...' : commentContent;
                sendNotificationLikeComment(postId, userTargetId, commentContent);
            }
        }
    });
}

function handleLikeCommentReplySuccess(commentReplyId, element, isLiked) {
    const likeIcon = $(element);
    const likeCommentReplyCount = $(`.like-comment-reply-count[data-comment-reply-id="${commentReplyId}"]`);

    if (isLiked) {
        likeIcon.addClass('bg-primary text-white').removeClass('text-grey-900');
        likeCommentReplyCount.text(parseInt(likeCommentReplyCount.text()) + 1 || 1);
    } else {
        likeIcon.removeClass('bg-primary text-white').addClass('text-grey-900');
        likeCommentReplyCount.text(parseInt(likeCommentReplyCount.text()) - 1 || '');
    }
}

function sendNotificationLikeCommentReply(postId, userTargetId, content) {
    const notificationContent = content !== null && content.length > 0 ? `liked your comment reply: "${content}"` : 'liked your comment reply';
    sendNotificationEvent(notificationContent, userCurrentId, 'POST', userTargetId, postId);
}

function likeCommentReply(commentReplyId, element) {
    $.ajax({
        url: `/api/likes/comment-replies/${commentReplyId}`,
        type: "POST",
        success: function (response) {
            console.log(response.message);
            const isLiked = response.code === 201;
            handleLikeCommentReplySuccess(commentReplyId, element, isLiked);
            if (isLiked) {
                let userTargetId = $(element).attr('data-author-id');
                let postId = $(element).attr('data-post-id');
                let commentReplyContent = $('.content-comment-reply[data-comment-reply-id="' + commentReplyId + '"]').text();
                commentReplyContent = commentReplyContent.length > 12 ? commentReplyContent.substring(0, 12) + '...' : commentReplyContent;
                sendNotificationLikeCommentReply(postId, userTargetId, commentReplyContent);
            }
        }
    });
}

function registerLikePostEvents() {
    btnLikePosts = $('.btn-like-post');
    btnLikePosts.each(function (index) {
        $(this).click(function (event) {
            event.preventDefault();
            const postId = postIds.eq(index).val();
            likePost(postId, index, this);
        })
    });
}

function registerLikeCommentEvents() {
    btnLikeComments = $('.btn-like-comment');
    btnLikeComments.each(function () {
        $(this).click(function (event) {
            event.preventDefault();
            const commentId = $(this).attr('data-comment-id');
            likeComment(commentId, this);
        })
    });
}

function registerLikeCommentReplyEvents() {
    btnLikeCommentReplys = $('.btn-like-comment-reply');
    btnLikeCommentReplys.each(function () {
        $(this).click(function (event) {
            event.preventDefault();
            const commentReplyId = $(this).attr('data-comment-reply-id');
            likeCommentReply(commentReplyId, this);
        })
    });
}

function initLike() {
    registerLikePostEvents();
    registerLikeCommentEvents();
    registerLikeCommentReplyEvents();
}
