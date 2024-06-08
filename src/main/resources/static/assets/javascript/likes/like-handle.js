let btnLikePosts = null;
let btnLikeComments = null;
let btnLikeCommentReplys = null;

function handleCreatLikePostSuccess(index) {
    let likeCurrent = btnLikePosts.eq(index);
    let likeCount = likeCurrent.find('.like-count');
    if (likeCount.text() === "") {
        likeCount.text(1);
    } else {
        likeCount.text(parseInt(likeCount.text()) + 1);
    }
    likeCurrent.find('i').addClass('bg-primary');
    likeCurrent.find('i').removeClass('text-grey-900');
    likeCurrent.find('i').addClass('text-white');
}

function handleDeleteLikePostSuccess(index) {
    let likeCurrent = btnLikePosts.eq(index);
    let likeCount = likeCurrent.find('.like-count');
    if (likeCount.text() === "1") {
        likeCount.text("");
    } else {
        likeCount.text(parseInt(likeCount.text()) - 1);
    }
    likeCurrent.find('i').removeClass('bg-primary');
    likeCurrent.find('i').addClass('text-grey-900');
    likeCurrent.find('i').removeClass('text-white');
}

function likePost(postId, index) {
    $.ajax({
        type: "POST",
        url: "/api/likes/posts/" + postId,
        success: function (response) {
            console.log(response.message);
            if (response.code === 201) {
                handleCreatLikePostSuccess(index);
            } else if (response.code === 204) {
                handleDeleteLikePostSuccess(index);
            }
        }, error: function (xhr, status, error) {
            console.error(error);
        }
    });
}

function handleLikeCommentSuccess(commentId, element) {
    $(element).addClass('bg-primary');
    $(element).removeClass('text-grey-900');
    $(element).addClass('text-white');
    let likeCommentCount = $('.like-comment-count[data-comment-id="' + commentId + '"]');
    if (likeCommentCount.text() === "") {
        likeCommentCount.text(1);
    } else {
        likeCommentCount.text(parseInt(likeCommentCount.text()) + 1);
    }
}

function handleDeleteLikeCommentSuccess(commentId, element) {
    $(element).removeClass('bg-primary');
    $(element).addClass('text-grey-900');
    $(element).removeClass('text-white');
    let likeCommentCount = $('.like-comment-count[data-comment-id="' + commentId + '"]');
    if (likeCommentCount.text() === "1") {
        likeCommentCount.text("");
    } else {
        likeCommentCount.text(parseInt(likeCommentCount.text()) - 1);
    }
}

function likeComment(commentId, element) {
    $.ajax({
        url: "/api/likes/comments/" + commentId,
        type: "POST",
        success: function (response) {
            console.log(response.message);
            if (response.code === 201) {
                handleLikeCommentSuccess(commentId, element);
            } else if (response.code === 204) {
                handleDeleteLikeCommentSuccess(commentId, element);
            }
        }
    });
}

function likeCommentReply(commentReplyId, element) {
    $.ajax({
        url: "/api/likes/comment-replies/" + commentReplyId,
        type: "POST",
        success: function (response) {
            console.log(response.message);
            if (response.code === 201) {
                $(element).addClass('bg-primary');
                $(element).removeClass('text-grey-900');
                $(element).addClass('text-white');
                let likeCommentReplyCount = $('.like-comment-reply-count[data-comment-reply-id="' + commentReplyId + '"]');
                if (likeCommentReplyCount.text() === "") {
                    likeCommentReplyCount.text(1);
                } else {
                    likeCommentReplyCount.text(parseInt(likeCommentReplyCount.text()) + 1);
                }
            } else if (response.code === 204) {
                $(element).removeClass('bg-primary');
                $(element).addClass('text-grey-900');
                $(element).removeClass('text-white');
                let likeCommentReplyCount = $('.like-comment-reply-count[data-comment-reply-id="' + commentReplyId + '"]');
                if (likeCommentReplyCount.text() === "1") {
                    likeCommentReplyCount.text("");
                } else {
                    likeCommentReplyCount.text(parseInt(likeCommentReplyCount.text()) - 1);
                }
            }
        }
    });
}

function registerLikePostEvents() {
    btnLikePosts = $('.btn-like-post');
    btnLikePosts.each(function (index) {
        $(this).click(function (event) {
            event.preventDefault();
            let postId = postIds.eq(index).val();
            likePost(postId, index);
        })
    });
}

function registerLikeCommentEvents() {
    btnLikeComments = $('.btn-like-comment');
    btnLikeComments.each(function () {
        $(this).click(function (event) {
            event.preventDefault();
            let commentId = $(this).attr('data-comment-id');
            likeComment(commentId, this);
        })
    });
}

function registerLikeCommentReplyEvents() {
    btnLikeCommentReplys = $('.btn-like-comment-reply');
    btnLikeCommentReplys.each(function () {
        $(this).click(function (event) {
            event.preventDefault();
            let commentReplyId = $(this).attr('data-comment-reply-id');
            likeCommentReply(commentReplyId, this);
        })
    });
}

function initLike() {
    registerLikePostEvents();
    registerLikeCommentEvents();
    registerLikeCommentReplyEvents();
}
