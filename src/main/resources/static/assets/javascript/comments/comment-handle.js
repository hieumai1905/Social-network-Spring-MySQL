let btnDisplayComments = null;
let comments = null;
let btnCommentsReply = null;
let commentsReply = null;
let btnComments = null;
let commentsInput = null;
let postIds = null;
let commentCounts = null;
let btnDeleteComments = null;
let commentIds = null;
let btnShowEditComments = null;

function loadComment() {
    registerShowCommentEvents();

    registerCommentReplyEvents();

    registerCommentEvents();

    registerCommentAction();
}

function registerShowCommentEvents() {
    btnDisplayComments = $('.btn-show-comment');
    comments = $('.comments-list');
    commentCounts = $('.comment-count');
    btnDisplayComments.off('click');
    if (btnDisplayComments.length > 0) {
        btnDisplayComments.each(function (index) {
            $(this).click(function (event) {
                comments.eq(index).toggleClass('d-none');
                let isHiddenComment = comments.eq(index).hasClass('d-none');
                if (!isHiddenComment)
                    invertScrollbar(comments.eq(index));
            });
        });
    }
}

function registerCommentEvents() {
    btnComments = $('.btn-comment');
    commentsInput = $('.comment-input');
    postIds = $(".current-post-id")

    btnComments.each(function (index) {
        $(this).click(function () {
            let postId = postIds.eq(index).val();
            let commentType = $(".comment-type[comment-type-post-id='" + postId + "']");
            let commentInput = commentsInput.eq(index);
            if (commentInput.val() === '') {
                commentInput.focus();
            } else {
                let isHiddenComment = comments.eq(index).hasClass('d-none');
                if (isHiddenComment) {
                    comments.eq(index).toggleClass('d-none');
                    invertScrollbar(comments.eq(index));
                }
                if (commentType.val() === 'create') {
                    createComment(commentInput.val(), index, postId);
                } else if (commentType.val() === 'update') {
                    updateComment(commentInput.val());
                }
                commentsInput.val('');
                commentType.val('create');
            }
        });
    });
}

function registerCommentAction() {
    btnShowEditComments = $('.btn-edit-comment');
    btnShowEditComments.each(function (index) {
        $(this).click(function () {
            let postId = $(this).attr('data-comment-post-id');
            let commentType = $(".comment-type[comment-type-post-id='" + postId + "']");
            commentType.val('update');
            let commentId = commentIds.eq(index).val();
            $('#comment-id-update').val(commentId);
            let commentElement = $(".comment[data-comment-id='" + commentId + "']");
            let commentContent = commentElement.find('p').text();
            let commentInput = $(".comment-input[comment-input-post-id='" + postId + "']")
            commentInput.val(commentContent);
            commentInput.focus();
        });
    });

    btnDeleteComments = $('.btn-delete-comment');
    commentIds = $('.comment-id');
    btnDeleteComments.off('click');
    btnDeleteComments.each(function (index) {
        $(this).click(function () {
            let commentId = commentIds.eq(index).val();
            deleteComment(commentId);
        });
    });
}

function registerCommentReplyEvents() {
    btnCommentsReply = $('.btn-comment-reply')
    commentsReply = $('.comments-reply-list');

    if (btnCommentsReply.length > 0) {
        btnCommentsReply.each(function (index) {
            $(this).click(function (event) {
                event.preventDefault();
                commentsReply.eq(index).toggleClass('d-none');
                let isHiddenComment = commentsReply.eq(index).hasClass('d-none');
                if (!isHiddenComment)
                    invertScrollbar(commentsReply.eq(index));
            });
        });
    }
}

function deleteComment(commentId) {
    $.ajax({
        url: "/api/comments/" + commentId,
        type: "DELETE",
        success: function (response) {
            if (response.code === 204) {
                console.log(response.message);
                handleDeleteCommentSuccess(commentId, response.data);
            }
        },
        error: function (error) {
            console.error(error);
        }
    });
}

function handleDeleteCommentSuccess(commentId, postId) {
    let commentElement = $(".comment[data-comment-id='" + commentId + "']");
    let commentCountPost = $(".comment-count[count-comment-post-id='" + postId + "']");
    commentElement.remove();

    let commentCount = commentCountPost.text();
    if (commentCount === "1") {
        commentCountPost.text("");
    } else {
        commentCountPost.text(parseInt(commentCount) - 1);
    }
}

function createComment(content, index, postId) {
    $.ajax({
        url: "/api/comments",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({
            content: content,
            postId: postId,
        }),
        success: function (response) {
            if (response.code === 201) {
                console.log(response.message);
                handleCreatCommentSuccess(response.data, index, postId);
            }
        },
        error: function (error) {
            console.error(error);
        }
    });
}

function updateComment(content) {
    let commentId = $('#comment-id-update').val();
    $.ajax({
        url: "/api/comments/" + commentId,
        type: "PUT",
        contentType: "application/json",
        data: JSON.stringify({
            content: content
        }),
        success: function (response) {
            if (response.code === 200) {
                console.log(response.message);
                handleUpdateCommentSuccess(response.data);
            }
        },
        error: function (error) {
            console.error(error);
        }
    });
}

function handleUpdateCommentSuccess(data) {
    let commentElement = $(".comment[data-comment-id='" + data.commentId + "']");
    let commentContentElement = commentElement.find('p');
    commentContentElement.text(data.content);
}


function handleCreatCommentSuccess(data, index, postId) {
    let html = `
    <div class="comment" data-comment-id="${data.commentId}">
        <div class="card-body p-0 d-flex">
            <figure class="avatar me-3">
                <img src="${currentUserAvatar}" alt="image" class="shadow-sm rounded-circle custom-avatar-50"></figure>
            <div class="bg-comment rounded-xxxl px-2">
                <h4 class="fw-700 text-grey-900 font-xssss mt-2">${currentUserName}</h4>
                <p>${data.content}</p>
            </div>
            <a href="#" class="ms-2" id="dd-comment-${data.commentId}" data-bs-toggle="dropdown"
                   aria-expanded="false">
                    <i class="ti-more-alt text-grey-900 btn-round-md bg-greylight font-xss"></i>
            </a>
            <div class="dropdown-menu p-1 rounded-xxxl border-0 shadow-lg"
                    aria-labelledby="dd-comment-${data.commentId}">
               <input type="hidden" value="${data.commentId}" class="comment-id">
               <h4 class="fw-600 w-100 cursor-pointer p-1 text-grey-900 font-xsss dropdown-item mt-0 me-4 hover-bg-light-gray btn-edit-comment"
               data-comment-post-id="${postId}">Edit</h4>
               <h4 class="fw-600 w-100 cursor-pointer p-1 text-grey-900 font-xsss dropdown-item mt-0 me-4 btn-delete-comment">Delete</h4>
            </div>
        </div>
        <div class="ms-4">
            <div class="card-body d-flex p-0 mt-1">
                    <span class="d-flex align-items-center fw-600 text-grey-500 lh-26 font-xssss me-2">${formatTime(new Date())}</span>
                <a href="#"
                   class="emoji-bttn d-flex align-items-center fw-600 text-grey-900 text-dark lh-26 font-xssss me-2">
                    <span>
                        <i class="feather-thumbs-up text-grey-900 me-1 btn-round-xs font-xss"></i>
                    </span>
                    <span></span> &nbsp; 
                    <span class="link-hover">Like</span>
                </a>
                <a href="#" class="btn-comment-reply d-flex align-items-center fw-600 text-grey-900 text-dark lh-26 font-xssss"><i
                        class="feather-message-circle text-dark text-grey-900 btn-round-sm font-lg"></i>
                    <span class="d-none-xss"></span> &nbsp;
                    <span class="link-hover">Reply</span>
                </a>
                <div class="cursor-pointer mt-1 d-none">
                    <a class="link-hover btn-comment-reply ms-2 text-decoration-underline d-flex text-grey-900
                            text-dark lh-26 font-xssss">
                        View more
                    </a>
                </div>
            </div>
        </div>
        <div class="comments-reply-list ms-5 d-none"></div>
    </div>`;
    comments.eq(index).append(html);
    let commentCount = commentCounts.eq(index);
    if (commentCount.text() === "") {
        commentCount.text(1)
    } else {
        commentCount.text(parseInt(commentCount.text()) + 1);
    }
    invertScrollbar(comments.eq(index));
    registerCommentAction();
}
