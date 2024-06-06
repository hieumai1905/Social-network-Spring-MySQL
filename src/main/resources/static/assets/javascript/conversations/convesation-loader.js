let conversationPrivate = $('#conversations-private');
let conversationGroup = $('#conversations-group');
let popupChat = $('#modal-popup-chat')
let btnCreateConversation = $("#btnCreateConversation");
let createConversationModal = $("#createConversationModal");
let friendsConversation = [], searchFriendConversation = [], selectedFriendConversation = [];

async function displayConversations() {
    let htmlPrivate = '';
    let htmlGroup = '';

    for (let i = 0; i < conversations.length; i++) {
        const conversation = conversations[i];
        if (conversation.type === 'GROUP') {
            htmlGroup += renderConversation(conversation.conversationId, conversation.name, conversation.avatar, true);
        } else {
            try {
                const data = await fetchPersonalConversation(conversation);
                htmlPrivate += renderConversation(conversation.conversationId, data.nickName, data.avatar, false);
            } catch (error) {
                console.log('Error loading participants');
            }
        }
    }

    conversationPrivate.html(htmlPrivate);
    conversationGroup.html(htmlGroup);
}

function fetchPersonalConversation(conversation) {
    return new Promise((resolve, reject) => {
        $.ajax({
            url: 'api/participants/' + conversation.conversationId + '/personal/users',
            type: 'GET',
            success: function (data) {
                resolve(data);
            },
            error: function (data) {
                reject(data);
            }
        });
    });
}

function renderConversation(conversationId, conversationName, conversationAvatar, isGroup) {
    const commonHtml = `
      <li class="bg-transparent list-group-item no-icon pe-0 ps-0 pt-2 pb-2 border-0 d-flex align-items-center"
      onclick="showMessageConversation(${conversationId})">
        <figure class="avatar float-left mb-0 me-2">
          <p class="d-none conversation">${conversationId}</p>
          <img src="${conversationAvatar}" alt="image" class="custom-avatar-50 image-conversation rounded-circle" data-conversation-id="${conversationId}">
        </figure>
        <h3 class="fw-700 mb-0 mt-0">
          <a class="font-xssss text-grey-600 d-block text-dark model-popup-chat" href="#" data-conversation-id="${conversationId}">
            ${conversationName}
<!--               <span class="badge-primary text-white badge-pill fw-500 mt-0"></span>-->
          </a>
        </h3>
        
        <a href="#" class="ms-auto" id="dd-conversation-${conversationId}" data-bs-toggle="dropdown" aria-expanded="false">
          <i class="ti-more-alt text-grey-900 btn-round-md bg-greylight font-xss"></i>
        </a>
        <div class="p-2 dropdown-menu dropdown-menu-end rounded-xxxl border-0 shadow-lg" aria-labelledby="dd-conversation-${conversationId}">
    `;

    if (isGroup) {
        return `${commonHtml}
            <div class="card-body p-2 dropdown-item rounded-xxxl d-flex" onclick="leaveConversationGroup(${conversationId})">
                <i class="fa fa-sign-out text-grey-500 me-2 fw-600 font-sm"></i>
                <h4 class="fw-600 text-grey-900 font-xsss mt-1">&ensp;Leave group</h4>
            </div>
            <div class="card-body p-2 dropdown-item rounded-xxxl d-flex" onclick="deleteConversationGroup(${conversationId})">
                <i class="fa fa fa-trash text-grey-500 me-2 fw-600 font-sm"></i>
                <h4 class="fw-600 text-grey-900 font-xsss mt-1">&ensp;Delete group</h4>
            </div>
            <div class="card-body p-2 dropdown-item rounded-xxxl d-flex" onclick="updateConversationGroup(${conversationId})">
                <i class="fa fa-pencil-square-o text-grey-500 me-2 fw-600 font-sm"></i>
                <h4 class="fw-600 text-grey-900 font-xsss mt-1">&ensp;Update group</h4>
            </div>
        </div>
      </li>
    `;
    } else {
        return `${commonHtml}
            <div class="card-body p-2 dropdown-item rounded-xxxl d-flex" onclick="deleteConversationGroup(${conversationId})">
                <i class="fa fa fa-trash text-grey-500 me-2 fw-600 font-sm"></i>
                <h4 class="fw-600 text-grey-900 font-xsss mt-1">&ensp;Delete conversation</h4>
            </div>
            <div class="card-body p-2 dropdown-item rounded-xxxl d-flex" onclick="updateConversation(${conversationId})">
                <i class="fa fa-pencil-square-o text-grey-500 me-2 fw-600 font-sm"></i>
                <h4 class="fw-600 text-grey-900 font-xsss mt-1">&ensp;Update conversation</h4>
            </div>
        </div>
      </li>
    `;
    }
}

function updateConversation(conversationId){

}

function updateConversationGroup(conversationId){

}

function deleteConversationGroup(conversationId) {
    $.ajax({
        url: '/api/conversations/' + conversationId,
        type: 'DELETE',
        success: function(response) {
            if(response.code === 200){
                alert('Delete conversation group successfully!');
                console.log(response);
            }
        },
        error: function(xhr, status, error) {
            alert('Error: ' + xhr.responseText);
            console.log(xhr, status, error);
        }
    });
}

function displayFriendsViewToConversation() {
    const listFriends = $("#list-friends-conversation");
    listFriends.empty();

    searchFriendConversation.forEach((user) => {
        if(user.isSelected === true){
            const cardBody = $("<div>").addClass("card-body d-flex pt-4 ps-4 pe-4 pb-0 border-top-xs bor-0")
                .attr("id", "conversation-user-card-" + user.userId)
                .click(function() {
                    addUserTagToConversation(user);
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

function addUserTagToConversation(user) {
    var isUserAlreadySelected = selectedFriendConversation.some(function(u) {
        return u.userId === user.userId;
    });

    if (!isUserAlreadySelected) {
        selectedFriendConversation.push(user);
        changeStatusUserTagToConversation(user.userId, false);
        displayFriendsViewToConversation();
        addUsersTagDivToConversation(user);
    }
}

function resetUsersTagToConversation() {
    $("#members").empty();
    for (let user of friendsConversation)
        user.isSelected = true;
    displayFriendsViewToConversation();
    selectedFriendConversation = [];
}

function changeStatusUserTagToConversation(userId, status){
    friendsConversation = friendsConversation.map(function(u) {
        if (u.userId === userId) {
            u.isSelected = status;
        }
        return u;
    });
}

function addUsersTagDivToConversation(user){
    let userTagItemContainer = $("<div>")
        .addClass("btn-group m-1")
        .attr("id", "conversation-user-tag-item-" + user.userId)
        .attr("role", "group")
        .attr("aria-label", "Second group");

    let userTagButton = $("<button>")
        .addClass("btn btn-dark")
        .text(user.fullName);

    let removeButton = $("<button>")
        .addClass("btn btn-dark remove-user-tag")
        .attr("type", "button")
        .attr("data-userId", user.userId)
        .text("x").click(function() {
            var removeUserId = $(this).data('userid');
            $('#conversation-user-tag-item-' + removeUserId).remove();
            removeUserByIdToConversation(removeUserId);
            changeStatusUserTagToConversation(removeUserId, true);
            displayFriendsViewToConversation();
        });

    userTagItemContainer.append(userTagButton, removeButton);
    $("#members").append(userTagItemContainer);
}
function removeUserByIdToConversation(userIdToRemove) {
    selectedFriendConversation = selectedFriendConversation.filter(function(user) {
        return user.userId !== userIdToRemove;
    });
}

function createConversation(){
    let nameConversation = $("#name-conversation").val();
    if(nameConversation === ''){
        alert('Please enter name conversation!');
        return;
    }
    let userIds = selectedFriendConversation.map(function(friend) {
        return friend.userId;
    });
    if(userIds.length < 2){
        alert('Please select two more friends to create a conversation!');
        return;
    }
    let formData = new FormData();
    formData.append("nameConversation", nameConversation);
    formData.append("participantIds", userIds);
    let fileInput = $("#avatar-conversation")[0];
    if (fileInput.files.length > 0) {
        formData.append("file", fileInput.files[0]);
    }
    $.ajax({
        url: '/api/conversations',
        type: 'POST',
        data: formData,
        processData: false,
        contentType: false,
        success: function(response) {
            if(response.code === 200){
                console.log(response);
                loadConversations();
                resetUsersTagToConversation();
                $("#name-conversation").val("");
                $("#avatar-conversation").val("");
                createConversationModal.modal('hide');
            }
        },
        error: function(xhr, status, error) {
            alert('Error: ' + xhr.responseText);
            console.log(xhr, status, error);
        }
    });
}
