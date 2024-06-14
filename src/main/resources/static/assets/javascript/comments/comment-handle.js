let btnDisplayComments = null;
let comments = null;
let btnCommentsReply = null;
let btnComments = null;
let commentsInput = null;
let postIds = null;
let commentCounts = null;
let btnDeleteComments = null;
let commentIds = null;
let btnShowEditComments = null;
let btnReplyComments = null
let tempReplyOfUserId = null;
let tempPostCurrentEdit = null;
let btnDeleteCommentReply = null;
let btnEditCommentReply = null;

function loadComment() {
    registerCancelOptionComment();

    registerShowCommentEvents();

    registerCommentReplyEvents();

    registerCommentEvents();

    registerCommentAction();

    registerReplyCommentAction();
}

function handleDeleteCommentReplySuccess(commentReplyId, commentId) {
    let commentReplyElement = $(`.comment-reply[data-comment-reply-id="${commentReplyId}"]`);
    commentReplyElement.remove();
    let countCommentReply = $('.count-comment-reply[data-comment-id="' + commentId + '"]');
    let count = countCommentReply.text();
    if (count === '1') {
        countCommentReply.text('');
        let btnViewMore = $('.view-more[data-comment-id="' + commentId + '"]');
        btnViewMore.addClass('d-none');
    } else {
        countCommentReply.text(parseInt(count) - 1);
    }
}

function deleteCommentReply(commentReplyId, commentId) {
    $.ajax({
        url: "/api/comments/replies/" + commentReplyId,
        type: "DELETE",
        success: function (response) {
            if (response.code === 204) {
                console.log(response.message);
                handleDeleteCommentReplySuccess(commentReplyId, commentId);
            }
        },
    });
}

function registerReplyCommentAction() {
    btnDeleteCommentReply = $('.btn-delete-comment-reply');
    if (btnDeleteCommentReply.length > 0) {
        btnDeleteCommentReply.each(function () {
            $(this).click(function () {
                let commentReplyId = $(this).attr('data-comment-reply-id');
                let commentId = $(this).attr('data-comment-id');
                setContentForConfirmModal({
                    title: 'Delete Comment',
                    body: 'Do you want to delete this comment?',
                    btnText: 'Delete'
                }, 'deleteCommentReply', commentReplyId, commentId);
                // deleteCommentReply(commentReplyId, commentId);
            });
        });
    }

    btnEditCommentReply = $('.btn-edit-comment-reply');
    if (btnEditCommentReply.length > 0) {
        btnEditCommentReply.each(function () {
            $(this).click(function () {
                let commentReplyId = $(this).attr('data-comment-reply-id');
                let commentId = $(this).attr('data-comment-id');
                let commentReply = $('.comment-reply[data-comment-reply-id="' + commentReplyId + '"]');
                let contentCommentReply = commentReply.find('p.content-comment-reply').text();
                let commentReplyInput = `
                <div class="card-body p-0 mt-1 ms-5 w-90 mx-3 position-relative">
                    <figure class="avatar position-absolute ms-4 mt-1 top-5">
                        <img src="${currentUserAvatar}" alt="image" class="shadow-sm rounded-circle custom-avatar-40 current-user-avatar">
                    </figure>
                    <textarea class="h100 bor-0 w-100 rounded-xxl ms-3 p-2 ps-5 comment-reply-input font-xssss text-grey-700 fw-500 border-light-md theme-dark-bg"
                     cols="30" rows="5" placeholder="What's on your mind?"
                    comment-reply-input-comment-id="${commentId}" style="height: 222px;">${contentCommentReply}</textarea>
                    <input type="hidden" value="update" class="reply-type" data-comment-id="${commentId}">
                    <figure class="avatar position-absolute font-xssss end-0 me-3 mt-2 top-5">
                        <i class="fa fa-camera" aria-hidden="true"></i>
                    </figure>
                    <figure class="avatar position-absolute mt-5 top-5 me-3 end-0 btn-reply" data-comment-id="${commentId}" data-comment-reply-id="${commentReplyId}">
                        <i class="fa fa-paper-plane" aria-hidden="true"></i>
                    </figure>
                    <p class="m-0 p-0 font-xssss text-center option-comment-reply" id="option-comment-reply-${commentId}">
                        <span>Editting...</span>
                        <a class="cursor-pointer btn-cancel-option-comment-reply" data-comment-id="${commentId}">Cancel</a>
                    </p>
                </div>
                `;
                let commentContainer = $(".input-comment-reply[data-comment-id='" + commentId + "']");
                commentContainer.html('');
                commentContainer.append(commentReplyInput);
                let commentReplyInputElement = commentContainer.find('textarea');
                commentReplyInputElement.focus();
                commentReplyInputElement.val('').val(contentCommentReply);
                registerReplyCommentSubmit();
                let btnCancelOptionCommentReply = $('.btn-cancel-option-comment-reply');
                btnCancelOptionCommentReply.each(function () {
                    $(this).click(function () {
                        let optionCommentReply = $(`#option-comment-reply-${commentId}`);
                        optionCommentReply.remove();
                        commentReplyInputElement.val('');
                        let commentType = $('.reply-type[data-comment-id="' + commentId + '"]');
                        commentType.val('create');
                    });
                });
            });
        });
    }
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
            let optionComment = $(`#option-comment-${postId}`);
            optionComment.removeClass('d-none');
            let span = optionComment.find('span');
            span.text('Editing... ');
            tempPostCurrentEdit = postId;
            let commentId = commentIds.eq(index).val();
            $('#comment-id-update').val(commentId);
            let commentElement = $(".comment[data-comment-id='" + commentId + "']");
            let commentContent = commentElement.find('p.content-comment').text();
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
            setContentForConfirmModal({
                title: 'Delete Comment',
                body: 'Do you want to delete this comment?',
                btnText: 'Delete'
            }, 'deleteComment', commentId);
            // deleteComment(commentId);
        });
    });
}

function registerCommentReplyEvents() {
    btnCommentsReply = $('.view-more');
    btnCommentsReply.each(function () {
        $(this).click(function () {
            let commentId = $(this).attr('data-comment-id');
            let commentReplies = $('.comments-reply-list[data-comment-id="' + commentId + '"]');
            commentReplies.toggleClass('d-none');
            invertScrollbar(commentReplies);
        });
    });

    btnReplyComments = $('.btn-reply-comment');
    if (btnReplyComments.length > 0) {
        btnReplyComments.each(function (index) {
            $(this).click(function () {
                let userId = $(this).attr('data-user-comment');
                let fullName = '';
                if (userId !== currentUserId) {
                    fullName = $(this).attr('data-full-name');
                }
                let commentIdReply = $(this).attr('data-comment-id');
                let commentReplyInput = `
                <div class="card-body p-0 mt-1 ms-5 w-90 mx-3 position-relative">
                    <figure class="avatar position-absolute ms-4 mt-1 top-5">
                        <img src="${currentUserAvatar}" alt="image" class="shadow-sm rounded-circle custom-avatar-40 current-user-avatar">
                    </figure>
                    <textarea class="h100 bor-0 w-100 rounded-xxl ms-3 p-2 ps-5 comment-reply-input font-xssss text-grey-700 fw-500 border-light-md theme-dark-bg" cols="30" rows="5" placeholder="What's on your mind?" 
                    comment-reply-input-comment-id="${commentIdReply}" style="height: 222px;"></textarea>
                    <input type="hidden" value="create" class="reply-type" data-comment-id="${commentIdReply}">
                    <figure class="avatar position-absolute font-xssss end-0 me-3 mt-2 top-5">
                        <i class="fa fa-camera" aria-hidden="true"></i>
                    </figure>
                    <figure class="avatar position-absolute mt-5 top-5 me-3 end-0 btn-reply" data-comment-id="${commentIdReply}">
                        <i class="fa fa-paper-plane" aria-hidden="true"></i>
                    </figure>
                </div>
                `;
                let commentContainer = $(".input-comment-reply[data-comment-id='" + commentIdReply + "']");
                commentContainer.html('');
                commentContainer.append(commentReplyInput);
                let commentReplyInputElement = commentContainer.find('textarea');
                commentReplyInputElement.focus();

                let commentReplyInputValue = `${fullName !== '' ? `@${fullName} ` : ''}`;
                commentReplyInputElement.val('').val(commentReplyInputValue);
                tempReplyOfUserId = userId;
                registerReplyCommentSubmit();
            });
        });
    }
}

function handleCreateReplyCommentSuccess(data, commentId) {
    let commentReplyContainer = $('.comments-reply-list[data-comment-id="' + commentId + '"]');
    let html = `
                        <div class="ms-6 mt-2 comment-reply" data-comment-reply-id="${data.commentReplyId}">
                            <div class="card-body p-0 d-flex">
                                <figure class="avatar me-3">
                                    <img src="${currentUserAvatar}" alt="image" class="shadow-sm rounded-circle custom-avatar-50">
                                </figure>
                                <div class="bg-comment rounded-xxxl px-2">
                                    <a href="profile?user-id=${data.userId}" class="fw-700 text-grey-900 font-xssss mt-1">${currentUserName}</a>
                                    <p class="content-comment-reply">${data.content}</p>
                                </div>
                                <a href="#" class="ms-2" id="'dd-comment-reply-' + ${data.commentReplyId}" data-bs-toggle="dropdown"
                                   aria-expanded="false">
                                    <i class="ti-more-alt text-grey-900 btn-round-md bg-greylight font-xss"></i>
                                </a>
                                <div class="dropdown-menu p-1 rounded-xxxl border-0 shadow-lg" aria-labelledby="'dd-comment-reply-' + ${data.commentReplyId}">
                                    <h4 class="fw-600 w-100 cursor-pointer p-1 text-grey-900 font-xsss dropdown-item mt-0 me-4 hover-bg-light-gray btn-edit-comment-reply"
                                    data-comment-reply-id="${data.commentReplyId}" data-comment-id="${commentId}">Edit
                                    </h4>
                                    <h4 class="fw-600 w-100 cursor-pointer p-1 text-grey-900 font-xsss dropdown-item mt-0 me-4 btn-delete-comment-reply"
                                    data-comment-reply-id="${data.commentReplyId}" data-comment-id="${commentId}">Delete
                                    </h4>
                                </div>
                            </div>
                            <div class="ms-4">
                                <div class="card-body d-flex p-0 mt-1">
                                            <span class="d-flex align-items-center fw-600 text-grey-500 lh-26 font-xssss me-2">${formatTime(new Date())}</span>
                                    <a href="#"
                                       class="emoji-btn d-flex align-items-center fw-600 text-grey-900 text-dark lh-26 font-xssss me-2">
                                        <span>
                                            <i class="feather-thumbs-up text-grey-900 me-1 btn-round-xs font-xss btn-like-comment-reply"
                                                    data-comment-reply-id="${data.commentReplyId}"></i>
                                        </span>
                                        <span class="like-comment-reply-count" data-comment-reply-id="${data.commentReplyId}"></span> &nbsp;
                                        <span class="link-hover">Like</span>
                                    </a>
                                    <button class="link-hover d-flex align-items-center fw-600 text-grey-900 text-dark lh-26 font-xssss border-0 bg-white">
                                        Reply
                                    </button>
                                </div>
                            </div>
                        </div>
                        `;
    commentReplyContainer.append(html);
    invertScrollbar(commentReplyContainer);
    let replyCount = $('.count-comment-reply[data-comment-id="' + commentId + '"]');
    if (replyCount.text() === "") {
        replyCount.text(1);
        let btnViewMore = $('.view-more[data-comment-id="' + commentId + '"]');
        btnViewMore.removeClass('d-none');
    } else {
        replyCount.text(parseInt(replyCount.text()) + 1);
    }
    registerReplyCommentAction();
    registerLikeCommentReplyEvents();
}

function createReplyComment(val, commentId) {
    $.ajax({
        url: "/api/comments/replies",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({
            content: val,
            commentId: commentId,
        }),
        success: function (response) {
            if (response.code === 201) {
                console.log(response.message);
                handleCreateReplyCommentSuccess(response.data, commentId);
            }
        },
        error: function (error) {
            console.error(error);
        }
    })
}

function registerReplyCommentSubmit() {
    let btnReply = $('.btn-reply');
    let commentType = 'update';
    btnReply.off('click');
    btnReply.click(function () {
        let commentIdReply = $(this).attr('data-comment-id');
        let commentReplyInput = $(".comment-reply-input[comment-reply-input-comment-id='" + commentIdReply + "']");
        let content = commentReplyInput.val();
        if (content === '') {
            commentReplyInput.focus();
        } else {
            commentType = $('.reply-type[data-comment-id="' + commentIdReply + '"]');
            if (commentType.val() === 'create') {
                createReplyComment(content, commentIdReply);
            } else {
                let commentReplyId = $(this).attr('data-comment-reply-id');
                $.ajax({
                    url: "/api/comments/replies/" + commentReplyId,
                    type: "PUT",
                    contentType: "application/json",
                    data: JSON.stringify({
                        content: content
                    }),
                    success: function (response) {
                        if (response.code === 204) {
                            console.log(response.message);
                            let optionCommentReply = $(`#option-comment-reply-${commentIdReply}`);
                            optionCommentReply.remove();
                            commentType.val('create');
                            let commentReply = $('.comment-reply[data-comment-reply-id="' + commentReplyId + '"]');
                            commentReply.find('p.content-comment-reply').text(content);
                        }
                    },
                });
            }
            commentReplyInput.val('');
        }
    });
}

function registerCancelOptionComment() {
    let btnCancelOptionComment = $('.btn-cancel-option-comment');
    btnCancelOptionComment.each(function () {
        $(this).click(function () {
            let postId = $(this).attr('data-post-id');
            let optionComment = $(`#option-comment-${postId}`);
            let commentType = $(".comment-type[comment-type-post-id='" + postId + "']");
            commentType.val('create');
            optionComment.addClass('d-none');
            let span = optionComment.find('span');
            span.text('');
            let commentInput = $(".comment-input[comment-input-post-id='" + postId + "']")
            commentInput.val('');
        });
    });
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
    let inputCommentReply = $(".input-comment-reply[data-comment-id='" + commentId + "']");
    inputCommentReply.remove();

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
                handleCreateCommentSuccess(response.data, index, postId);
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
    let commentContentElement = commentElement.find('p.content-comment');
    commentContentElement.text(data.content);
    let optionComment = $(`#option-comment-${tempPostCurrentEdit}`);
    optionComment.addClass('d-none');
    tempPostCurrentEdit = null;
}


function handleCreateCommentSuccess(data, index, postId) {
    let html = `
    <div class="comment mt-1" data-comment-id="${data.commentId}">
        <div class="card-body p-0 d-flex">
            <figure class="avatar me-3">
                <img src="${currentUserAvatar}" alt="image" class="shadow-sm rounded-circle custom-avatar-50"></figure>
            <div class="bg-comment rounded-xxxl px-2">
                <a href="profile?user-id=${data.userId}" class="fw-700 text-grey-900 font-xssss mt-2">${currentUserName}</a>
                <p class="content-comment" data-comment-id="${data.commentId}">${data.content}</p>
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
                <a
                   class="emoji-bttn d-flex align-items-center cursor-pointer fw-600 text-grey-900 text-dark lh-26 font-xssss me-2">
                    <span>
                        <i class="feather-thumbs-up text-grey-900 me-1 btn-round-xs font-xss btn-like-comment" data-comment-id="${data.commentId}"
                        data-author-id="${data.userId}" data-post-id="${data.postId}"></i>
                    </span>
                    <span class="like-comment-count" data-comment-id="${data.commentId}"></span> &nbsp; 
                    <span class="link-hover">Like</span>
                </a>
                <a class="d-flex align-items-center fw-600 text-grey-900 text-dark lh-26 font-xssss cursor-pointer btn-reply-comment"
                data-user-comment="${currentUserId}" data-full-name="${currentUserName}" data-comment-id="${data.commentId}"><i
                        class="feather-message-circle text-dark text-grey-900 btn-round-sm font-lg"></i>
                    <span class="d-none-xss count-comment-reply" data-comment-id="${data.commentId}"></span> &nbsp;
                    <span class="link-hover">Reply</span>
                </a>
                <div class="cursor-pointer mt-1 d-none view-more" data-comment-id="${data.commentId}">
                    <a class="link-hover btn-comment-reply ms-2 text-decoration-underline d-flex text-grey-900
                            text-dark lh-26 font-xssss">
                        View more
                    </a>
                </div>
            </div>
        </div>
        <div class="comments-reply-list ms-5 d-none" data-comment-id="${data.commentId}"></div>
    </div>
    <div class="input-comment-reply" data-comment-id="${data.commentId}">
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
    loadComment();
    registerLikeCommentEvents();
}
