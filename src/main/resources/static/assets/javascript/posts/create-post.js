let userTags = [], selectedUser = [], searchUserTags = [];
function createPost() {
    let formData = new FormData();
    let content = $('#content').val();
    if(content.trim() === '')
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

    $.ajax({
        url: '/api/posts',
        type: 'POST',
        data: formData,
        processData: false,
        contentType: false,
        success: function(response) {
            console.log('Post created successfully:', response);
            if(response.code === 201) {
                addPost(response.data);
                clearForm();
            }
        },
        error: function(xhr, status, error) {
            console.error('Error creating post:', error);
        }
    });
}

function addPost(post) {
    const postContainer = $('#post-container');
    const userTags = post.userTags || [];
    const userTag = userTags.length > 0 ? userTags[0].fullName : '';
    const additionalTags = userTags.length > 1 ? `và ${userTags.length - 1} người khác` : '';
    let createAt = formatTime(new Date(post.createAt));
    let mediaHtml = '';
    post.medias.forEach((image, index) => {
        if (index < 3) {
            mediaHtml += `
                <div class="col-xs-4 col-sm-4 p-1">
                    <a href="${image}" data-lightbox="roadtrip" class="${index === 2 && post.medias.length > 3 ? 'position-relative d-block' : ''}">
                        <img src="/assets/files-upload/${image}" class="rounded-3 w-100" alt="image">
                        ${index === 2 && post.medias.length > 3 ? `<span class="img-count font-sm text-white ls-3 fw-600"><b>+${post.medias.length - 3}</b></span>` : ''}
                    </a>
                </div>`;
        }
    });

    const postHtml = `
        <div class="card w-100 shadow-xss rounded-xxl border-0 p-4 mb-3">
            <div class="card-body p-0 d-flex">
                <input type="hidden" value="${post.postId}" class="post_current_id">
                <figure class="avatar me-3"><img src="${post.author.avatar}" alt="image" class="shadow-sm rounded-circle w45"></figure>
                <h4 class="fw-700 text-grey-900 font-xssss mt-1">
                    <span>${post.author.fullName}</span>
                    ${userTag ? `<i class="fa fa-caret-right" aria-hidden="true"></i> <span>${userTag}</span>` : ''}
                    ${additionalTags ? `<span>${additionalTags}</span>` : ''}
                    <span class="createAtSpan d-block font-xssss fw-500 mt-1 lh-3 text-grey-500">${createAt}</span>
                </h4>
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
                   class="emoji-btn d-flex align-items-center fw-600 text-grey-900 text-dark lh-26 font-xssss me-2">
                    <span class="btn-like">
                            <i class="feather-thumbs-up text-grey-900 me-1 btn-round-xs font-xss"></i>
                        </span>
                    <span class="like_count_current"></span>&nbsp;Like
                </a>
                <a class="cursor-pointer btn-comment d-flex align-items-center fw-600 text-grey-900 text-dark lh-26 font-xssss border-0">
                    <i class="feather-message-circle text-dark text-grey-900 btn-round-sm font-lg"></i>
                    <span class="d-none-xss comment_count_current"></span>
                    &nbsp; Comment
                </a>
            </div>
            <hr>
            <div th:fragment="fragment-comment-new" class="card-body p-0 mt-3 position-relative">
                <figure class="avatar position-absolute ms-2 mt-1 top-5">
                    <img src="${post.author.avatar}" alt="image" class="shadow-sm rounded-circle w30">
                </figure>
                <textarea name="message"
                          class="h100 bor-0 w-100 rounded-xxl p-2 ps-5 font-xssss text-grey-700 fw-500 comment_input border-light-md theme-dark-bg"
                          cols="30" rows="5" placeholder="What's on your mind?"></textarea>
                <figure class="avatar position-absolute font-xssss end-0 me-3 mt-2 top-5">
                    <i class="fa fa-camera" aria-hidden="true"></i>
                </figure>
                <figure class="avatar position-absolute mt-5 top-5 me-3 end-0 btn-comment-post">
                    <i class="fa fa-paper-plane" aria-hidden="true"></i>
                </figure>
            </div>
        </div>`;

    postContainer.prepend(postHtml);
}

function clearForm(){
    $('#content').val('');
    $('#access').val('PUBLIC');
    files = [];
    selectedUser = [];
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
            }).addClass("shadow-sm rounded-circle w45");
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
    $("#createPost").on("click", function () {
        createPost();
    });
    $("#search-friends").on("input", function () {
        let text = $(this).val().toLowerCase().trim();
        searchUserTags = userTags.filter(function (user) {
            return user.fullName.toLowerCase().includes(text);
        });
        displayFriendsView();
    });
}