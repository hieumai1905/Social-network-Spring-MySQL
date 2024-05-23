let btnLikePosts = null;

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

function registerLikeEvents() {
    btnLikePosts = $('.btn-like-post');
    btnLikePosts.each(function (index) {
        $(this).click(function (event) {
            event.preventDefault();
            let postId = postIds.eq(index).val();
            likePost(postId, index);
        })
    });
}

function initLike() {
    registerLikeEvents();
}
