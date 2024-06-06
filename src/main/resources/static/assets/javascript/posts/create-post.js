let selectedUser = [], searchUserTags = [];
let userTags = [];
function savePost(postId) {
    let formData = new FormData();
    let content = $('#content').val();
    if(content.trim() === '' && (files === null || files.length === 0))
        return;

    formData.append('access', $('#access').val());
    formData.append('content', content);
    formData.append('postType', "POST");

    files = files || [];
    for (let i = 0; i < files.length; i++) {
        formData.append('files', files[i]);
    }

    selectedUser.forEach(function(user) {
        formData.append('userTagIds', user.userId);
    });

    let hagTags = content.split(' ').filter(function(word) {
        return word.startsWith('#');
    });
    hagTags = hagTags || [];
    hagTags.forEach(function(tag) {
        formData.append('hagTags', tag);
    });

    let url = '/api/posts', type = 'POST';
    if(postId !== ""){
        url += `/${postId}`;
        type = 'PUT';
    }

    $.ajax({
        url: url,
        type: type,
        data: formData,
        processData: false,
        contentType: false,
        success: function(response) {
            console.log('Save successfully:', response);
            if(response.code === 201 && postId === "") {
                savePostToUI(response.data, true, false);
            }else if(response.code === 200 && postId !== ""){
                savePostToUI(response.data, false, false);
            }
            clearForm();
        },
        error: function(xhr, status, error) {
            console.error('Error save post:', error);
        }
    });
}

function savePostToUI(post, isNewPost, isSearch) {
    const postContainer = $(isSearch ? '#search-results' : '#post-container');
    const userTags = post.userTags || [];
    const userTag = userTags.length > 0 ? userTags[0].fullName : '';
    const additionalTags = userTags.length > 1 ? `và ${userTags.length - 1} người khác` : '';
    let createAt = formatTime(new Date(post.createAt));
    let mediaHtml = '';
    let accessIcon = `<i class="fa fa-users" aria-hidden="true" data-bs-toggle="tooltip" data-bs-placement="top" title="${post.access}"></i>`;
    if(post.access === 'PUBLIC')
        accessIcon = `<i class="fa fa-globe" aria-hidden="true" data-bs-toggle="tooltip" data-bs-placement="top" title="${post.access}"></i>`;
    else if(post.access === 'PRIVATE')
        accessIcon = `<i class="fa fa-lock" aria-hidden="true" data-bs-toggle="tooltip" data-bs-placement="top" title="${post.access}"></i>`

    post.medias.forEach((image, index) => {
        if (index < 3) {
            mediaHtml += `
                <div class="col-xs-4 col-sm-4 p-1">
                    <a href="${image}" data-lightbox="roadtrip" class="${index === 2 && post.medias.length > 3 ? 'position-relative d-block' : ''}">
                        <img src="${image}" class="rounded-3 w-100" alt="image">
                        ${index === 2 && post.medias.length > 3 ? `<span class="img-count font-sm text-white ls-3 fw-600"><b>+${post.medias.length - 3}</b></span>` : ''}
                    </a>
                </div>`;
        }
    });

    const postHtml = `
        <div id="post-${post.postId}" class="card w-100 shadow-xss rounded-xxl border-0 p-4 mb-3">
            <div class="card-body p-0 d-flex">
                <input type="hidden" value="${post.postId}" class="current-post-id">
                <figure class="avatar me-3"><img src="${post.author.avatar}" alt="image" class="shadow-sm rounded-circle current-user-avatar custom-avatar-50"></figure>
                <h4 class="fw-700 text-grey-900 font-xssss mt-1">
                    <span>${post.author.fullName}</span>
                    ${userTag ? `<i class="fa fa-caret-right" aria-hidden="true"></i> <span>${userTag}</span>` : ''}
                    ${additionalTags ? `<span>${additionalTags}</span>` : ''}
                    <span class="d-block font-xssss fw-500 mt-1 lh-3 text-grey-700">
                        ${accessIcon}
                    </span>
                    <span class="createAtSpan d-block font-xssss fw-500 mt-1 lh-3 text-grey-500">${createAt}</span>
                </h4>
                <a href="#" class="ms-auto" id="dropdownMenu2" data-bs-toggle="dropdown"
                   aria-expanded="false"><i
                        class="ti-more-alt text-grey-900 btn-round-md bg-greylight font-xss"></i></a>
                <div class="dropdown-menu dropdown-menu-end p-2 rounded-xxxl cursor-pointer border-0 shadow-lg"
                     aria-labelledby="dropdownMenu2">
                     <div data-postId="${post.postId}" id="save-post-${post.postId}" class="card-body p-2 dropdown-item rounded-xxxl d-flex">
                        <i class="feather-bookmark text-grey-500 me-2 fw-600 font-sm"></i>
                        <h4 class="fw-600 text-grey-900 font-xsss mt-1">Save Post</h4>
                    </div>
                    <div data-postId="${post.postId}" id="hide-post-${post.postId}" class="card-body p-2 dropdown-item rounded-xxxl d-flex">
                        <i class="fa fa-eye-slash text-grey-500 me-2 fw-600 font-sm"></i>
                        <h4 class="fw-600 text-grey-900 font-xsss mt-1">Hide Post</h4>
                    </div>
                    <div data-postId="${post.postId}" id="update-post-${post.postId}" class="card-body p-2 dropdown-item rounded-xxxl d-flex">
                        <i class="fa fa-pencil-square-o text-grey-500 me-2 fw-600 font-sm"></i>
                        <h4 class="fw-600 text-grey-900 font-xsss mt-1">Update Post
                        </h4>
                    </div>
                    <div data-postId="${post.postId}" id="delete-post-${post.postId}" class="card-body p-2 dropdown-item rounded-xxxl d-flex">
                        <i class="fa fa-trash-o text-grey-500 me-2 fw-600 font-sm"></i>
                        <h4 class="fw-600 text-grey-900 font-xsss mt-1">Delete Post</h4>
                    </div>
                </div>
            </div>
            <div class="card-body p-0 me-lg-5">
                <p class="fw-500 text-grey-500 lh-26 font-xssss w-100">${post.postContent}</p>
            </div>
            <div class="card-body d-block p-0">
                <div class="row ps-2 pe-2">
                    ${mediaHtml}
                </div>
            </div>
            <div class="card-body d-flex p-0 mt-3">
                <a href="#"
                   class="emoji-btn d-flex align-items-center fw-600 text-grey-900 text-dark lh-26 font-xssss me-2 btn-like-post">
                    <span class="btn-like">
                        <i class="feather-thumbs-up text-grey-900 me-1 btn-round-xs font-xss"></i>
                    </span>
                    <span class="like-count"></span>&nbsp;Like
                </a>
                <a class="cursor-pointer btn-show-comment d-flex align-items-center fw-600 text-grey-900 text-dark lh-26 font-xssss border-0">
                    <i class="feather-message-circle text-dark text-grey-900 btn-round-sm font-lg"></i>
                    <span class="d-none-xss comment-count" count-comment-post-id="${post.postId}"></span>
                    &nbsp; Comment
                </a>
                <a href="#" id="shareDropdownMenu" data-bs-toggle="dropdown" aria-expanded="false"
                   class="ms-auto d-flex align-items-center fw-600 text-grey-900 text-dark lh-26 font-xssss"><i
                        class="feather-share-2 text-grey-900 text-dark btn-round-sm font-lg"></i><span
                        class="d-none-xs">Share</span></a>
                <div class="dropdown-menu dropdown-menu-end p-3 rounded-xxl border-0 shadow-lg"
                     aria-labelledby="shareDropdownMenu" style="width: 400px">
                    <textarea id="share-content-${post.postId}" placeholder="Say something..." type="text"
                              class="bg-grey text-grey-800 font-xssss border-0 lh-32 p-2 font-xssss fw-600 rounded-3 w-100 theme-dark-bg"></textarea>
                    <div th:data-postId="${post.postId}" class="btn-share-post card-body p-0 rounded-xxxl d-flex float-right">
                        <h4 class="fw-600 text-white font-xsss mt-1 btn btn-primary">Share</h4>
                    </div>
                </div>
            </div>
            <hr>
            <div class="card-body p-0 mt-1 position-relative">
                <figure class="avatar position-absolute ms-2 mt-1 top-5">
                    <img src="${post.author.avatar}" alt="image" class="shadow-sm rounded-circle current-user-avatar custom-avatar-40">
                </figure>
                <textarea comment-input-post-id="${post.postId}"
                        class="h100 bor-0 w-100 rounded-xxl p-2 ps-5 font-xssss text-grey-700 fw-500 comment-input border-light-md theme-dark-bg"
                        cols="30" rows="5" placeholder="What's on your mind?"></textarea>
                </textarea>
                <input type="hidden" value="create" class="comment-type" comment-type-post-id="${post.postId}">
                <input type="hidden" value="" id="comment-id-update">
                <figure class="avatar position-absolute font-xssss end-0 me-3 mt-2 top-5">
                    <i class="fa fa-camera" aria-hidden="true"></i>
                </figure>
                <figure class="avatar position-absolute mt-5 top-5 me-3 end-0 btn-comment">
                    <i class="fa fa-paper-plane" aria-hidden="true"></i>
                </figure>
            </div>
        </div>`;
    if (isNewPost) {
        if(isSearch)
            postContainer.append(postHtml);
        else
            postContainer.prepend(postHtml);
    } else {
        $(`#post-${post.postId}`).replaceWith(postHtml);
    }
    $(document).on('click', `#delete-post-${post.postId}`, function() {
        confirmToDeletePost(post.postId);
    });
    $(document).on('click', `#update-post-${post.postId}`, function() {
        findPostById(post.postId);
    });
    $(document).on('click', `#save-post-${post.postId}`, function() {
        updatePostInteract('saved', post.postId, $(this));
    });
    $(document).on('click', `#hide-post-${post.postId}`, function() {
        updatePostInteract('hidden', post.postId, $(this));
    });
    $('.btn-share-post').off('click');
    $(".btn-share-post").on("click", function () {
        updatePostInteract('shared', post.postId, $(this));
    });
    if(!isSearch)
        loadComment();
}

function clearForm(){
    $('#content').val('');
    $('#access').val('PUBLIC');
    files = [];
    resetUsersTag();
    $('#file-input').val('');
    $('#file-list').empty();
    $('#createPostModal').modal('hide');
}

function getFriends() {
    $.ajax({
        type: "GET",
        url: "/api/users/friends",
        success: function (response) {
            console.log("API Response:", response);
            if(response.code === 200){
                userTags = response.data.map(function(user) {
                    return {
                        userId: user.userId,
                        fullName: user.fullName,
                        avatar: user.avatar,
                        isSelected: true
                    };
                });
                searchUserTags = userTags;
                friendsConversation = userTags;
                searchFriendConversation = userTags;
                displayFriendsViewToConversation();
                displayFriendsView();
            }
        },
        error: function (xhr, status, error) {
            console.error("There was a problem with the AJAX request:", error);
        },
    });
}

function displayFriendsView() {
    const listFriends = $("#list-friends");
    listFriends.empty();

    searchUserTags.forEach((user) => {
        if(user.isSelected === true){
            const cardBody = $("<div>").addClass("card-body d-flex pt-4 ps-4 pe-4 pb-0 border-top-xs bor-0")
                .attr("id", "user-card-" + user.userId)
                .click(function() {
                    addUserTag(user);
                });

            const figure = $("<figure>").addClass("avatar me-3");
            const img = $("<img>").attr({
                src: user.avatar,
                alt: "image"
            }).addClass("shadow-sm rounded-circle custom-avatar-50");
            figure.append(img);

            const h4 = $("<h4>").addClass("fw-700 text-grey-900 font-xssss mt-2").text(user.fullName);

            cardBody.append(figure, h4);
            listFriends.append(cardBody);
        }
    });
}

function addUserTag(user) {
    var isUserAlreadySelected = selectedUser.some(function(u) {
        return u.userId === user.userId;
    });

    if (!isUserAlreadySelected) {
        selectedUser.push(user);
        changeStatusUserTag(user.userId, false);
        displayFriendsView();
        addUsersTagDiv(user);
    }
}

function resetUsersTag() {
    $("#users-tag").empty();
    for (let user of userTags)
        user.isSelected = true;
    displayFriendsView();
    selectedUser = [];
}

function changeStatusUserTag(userId, status){
    userTags = userTags.map(function(u) {
        if (u.userId === userId) {
            u.isSelected = status;
        }
        return u;
    });
}

function addUsersTagDiv(user){
    var userTagItemContainer = $("<div>")
        .addClass("btn-group m-1")
        .attr("id", "user-tag-item-" + user.userId)
        .attr("role", "group")
        .attr("aria-label", "Second group");

    var userTagButton = $("<button>")
        .addClass("btn btn-dark")
        .text(user.fullName);

    var removeButton = $("<button>")
        .addClass("btn btn-dark remove-user-tag")
        .attr("type", "button")
        .attr("data-userId", user.userId)
        .text("x").click(function() {
            var removeUserId = $(this).data('userid');
            $('#user-tag-item-' + removeUserId).remove();
            removeUserById(removeUserId);
            changeStatusUserTag(removeUserId, true);
            displayFriendsView();
        });

    userTagItemContainer.append(userTagButton, removeButton);
    $("#users-tag").append(userTagItemContainer);
}
function removeUserById(userIdToRemove) {
    selectedUser = selectedUser.filter(function(user) {
        return user.userId !== userIdToRemove;
    });
}

function initCreatePost(){
    getFriends();

    $("#search-friends").on("input", function () {
        let text = $(this).val().toLowerCase().trim();
        searchUserTags = userTags.filter(function (user) {
            return user.fullName.toLowerCase().includes(text);
        });
        displayFriendsView();
    });
    $(".delete-post").on("click", function () {
       let postId = $(this).data("postid");
        confirmToDeletePost(postId);
    });
}
