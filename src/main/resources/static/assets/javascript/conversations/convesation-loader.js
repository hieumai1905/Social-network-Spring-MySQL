let conversationPrivate = $('#conversations-private');
let conversationGroup = $('#conversations-group');
let popupChat = $('#modal-popup-chat')
let btnCreateConversation = $("#btnCreateConversation");
let createConversationModal = $("#createConversationModal");
let friendsConversation = [], searchFriendConversation = [], selectedFriendConversation = [];
let updateConversationId = null, isManager = true;

async function displayConversations() {
    let htmlPrivate = '';
    let htmlGroup = '';

    for (let i = 0; i < conversations.length; i++) {
        const conversation = conversations[i];
        if (conversation.type === 'GROUP') {
            htmlGroup += renderConversation(conversation.conversationId, conversation.name, conversation.avatar, true, null);
        } else {
            try {
                const data = await fetchPersonalConversation(conversation);
                htmlPrivate += renderConversation(conversation.conversationId, data.nickName, data.avatar, false, data.userId);
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

function renderConversation(conversationId, conversationName, conversationAvatar, isGroup, userId) {
    const commonHtml = `
      <li class="bg-transparent list-group-item no-icon pe-0 ps-0 pt-2 pb-2 border-0 d-flex align-items-center">
        <figure class="avatar float-left mb-0 me-2">
          <p class="d-none conversation">${conversationId}</p>
          <img src="${conversationAvatar}" alt="image" class="custom-avatar-50 image-conversation rounded-circle" data-conversation-id="${conversationId}">
        </figure>
        <h3 class="fw-700 mb-0 mt-0">
          <a onclick="showMessageConversation(${conversationId}, ${isGroup})" class="font-xssss text-grey-600 d-block text-dark model-popup-chat" href="#" data-conversation-id="${conversationId}" 
          data-user-id="${userId}">${conversationName}</a>
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
            <div class="card-body p-2 dropdown-item rounded-xxxl d-flex" onclick="showConfirmDeleteConversationModal(${conversationId})">
                <i class="fa fa fa-trash text-grey-500 me-2 fw-600 font-sm"></i>
                <h4 class="fw-600 text-grey-900 font-xsss mt-1">&ensp;Delete group</h4>
            </div>
            <div class="card-body p-2 dropdown-item rounded-xxxl d-flex" onclick="loadConversationGroupToModal(${conversationId})">
                <i class="fa fa-pencil-square-o text-grey-500 me-2 fw-600 font-sm"></i>
                <h4 class="fw-600 text-grey-900 font-xsss mt-1">&ensp;Update group</h4>
            </div>
            <div class="card-body p-2 dropdown-item rounded-xxxl d-flex" onclick="loadMembersToModal(${conversationId})">
                <i class="fa fa-users text-grey-500 me-2 fw-600 font-sm"></i>
                <h4 class="fw-600 text-grey-900 font-xsss mt-1">&ensp;Members</h4>
            </div>
        </div>
      </li>
    `;
    } else {
        return `${commonHtml}
            <div class="card-body p-2 dropdown-item rounded-xxxl d-flex" onclick="showConfirmDeleteConversationModal(${conversationId})">
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
function deleteConversation(conversationId){

}

function loadMembersToModal(conversationId){
    $.ajax({
        url: '/api/conversations/' + conversationId,
        type: 'GET',
        success: function(response) {
            if(response.code === 200){
                console.log(response);
                let body = '<div class="custom-overflow250">';
                response.data.members.forEach(member => {
                    body += displayMemberItem(conversationId, member, response.data.userId);
                });
                body += '</div>';

                setContentForMembersModal({
                    title: 'Members in conversation',
                    body: body,
                    btnText: 'Save',
                    isNeedBtnSave: false
                }, '', null);

            }
        },
        error: function(xhr, status, error) {
            alert('Error: ' + xhr.responseText);
            console.log(xhr, status, error);
        }
    });
}

function displayMemberItem(conversationId, member, managerId){
    return `
        <div class="card-body d-flex pt-4 ps-4 pe-4 pb-0 border-top-xs bor-0" id="conversation-member-card-${member.userId}">
            <figure class="avatar me-3">
                <img src="${member.avatar}" alt="image" class="shadow-sm rounded-circle custom-avatar-50">
            </figure>
            <h4 class="fw-700 text-grey-900 font-xssss mt-2">${member.fullName}</h4>
            <a href="#" class="ms-auto" id="dropdownMenuMembers2" data-bs-toggle="dropdown"
               aria-expanded="false"><i
                    class="ti-more-alt text-grey-900 btn-round-md bg-greylight font-xss"></i></a>
            <div class="dropdown-menu dropdown-menu-end p-2 rounded-xxxl cursor-pointer border-0 shadow-lg"
                 aria-labelledby="dropdownMenuMembers2">
                 ${currentUserId === managerId ? `
                    <div class="set-manager-conversation card-body p-2 dropdown-item rounded-xxxl d-flex"
                        onclick="updateManager('${conversationId}', '${member.userId}')">
                        <i class="fa fa-user text-grey-500 me-2 fw-600 font-sm"></i>
                        <h4 class="fw-600 text-grey-900 font-xsss mt-1">Set Manager</h4>
                    </div>
                 ` : ``}
                 <div class="set-manager-conversation card-body p-2 dropdown-item rounded-xxxl d-flex">
                        <i class="fa fa-eye text-grey-500 me-2 fw-600 font-sm"></i>
                        <a href="/profile?user-id=${member.userId}" class="fw-600 text-grey-900 font-xsss">View Profile</a>
                 </div>
            </div>
        </div>
    `;
}

function updateManager(conversationId, memberId){
    $.ajax({
        url: '/api/conversations/update-manager?conversationId=' + conversationId + "&managerId=" + memberId,
        type: 'PUT',
        success: function(response) {
            if(response.code === 200){
                console.log(response);
                _membersModal.modal('hide');
                setContentForNotifyModal({
                    title: 'Message',
                    body: 'Set manager successfully!',
                    btnText: "Ok"
                });
            }
        },
        error: function(xhr, status, error) {
            console.log('Error: ' + xhr.responseText);
            console.log(xhr, status, error);
        }
    });
}

function setContentForMembersModal(content, functionName, ...args) {
    _membersModal.find(".modal-title").text(content.title);
    _membersModal.find(".modal-body").html(content.body);
    let modalFooter = _membersModal.find(".modal-footer");
    modalFooter.empty();
    let btnCancel = $('<button>', {
        id: 'btnCancelModal',
        type: 'button',
        class: 'btn btn-dark',
        'data-bs-dismiss': 'modal',
        text: 'Cancel'
    });
    if(content.isNeedBtnSave){
        let btnOk = $('<button>', {
            type: 'button',
            class: 'btn-ok btn btn-primary text-white',
            'data-bs-dismiss': 'modal',
            text: `${content.btnText}`
        });
        modalFooter.append(btnCancel, btnOk);
        btnOk.on('click', function() {
            if (typeof window[functionName] === 'function') {
                window[functionName].apply(null, args);
            }
        });
    }else{
        modalFooter.append(btnCancel);
    }
    _membersModal.modal('show');
}
function updateConversationGroup(){
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
    userIds.push(currentUserId);
    let formData = new FormData();
    formData.append("nameConversation", nameConversation);
    formData.append("participantIds", userIds);
    let fileInput = $("#avatar-conversation")[0];
    if (fileInput.files.length > 0) {
        formData.append("file", fileInput.files[0]);
    }
    $.ajax({
        url: '/api/conversations/' + updateConversationId,
        type: 'PUT',
        data: formData,
        processData: false,
        contentType: false,
        success: function(response) {
            if(response.code === 200){
                console.log(response);
                loadConversations();
                resetDataInConversationModal();
                createConversationModal.modal('hide');
            }
        },
        error: function(xhr, status, error) {
            alert('Error: ' + xhr.responseText);
            console.log(xhr, status, error);
        }
    });
}

function loadConversationGroupToModal(conversationId){
    $.ajax({
        url: '/api/conversations/' + conversationId,
        type: 'GET',
        success: function(response) {
            if(response.code === 200){
                console.log(response);
                updateConversationId = conversationId;
                setContentForConversationModal({
                   title: 'Update conversation',
                    btnText: 'Update',
                    action: 'update'
                });
                $("#name-conversation").val(response.data.name);
                isManager = response.data.userId === currentUserId;
                response.data.members.forEach(member => {
                    if(member.userId !== currentUserId)
                        addUserTagToConversation(member);
                });
                createBlobFromUrl(response.data.avatar)
                    .then(blob => {
                        const fileName = extractFileNameFromUrl(response.data.avatar);
                        return blobToFile(blob, fileName);
                    })
                    .then(avatar => {
                        const file = new File([avatar], avatar.name, { type: avatar.type });
                        const dataTransfer = new DataTransfer();
                        dataTransfer.items.add(file);
                        const fileInput = document.getElementById("avatar-conversation");
                        fileInput.files = dataTransfer.files;
                    })
                    .catch(error => {
                        console.error('Error creating file from URL:', error);
                    });
            }
        },
        error: function(xhr, status, error) {
            alert('Error: ' + xhr.responseText);
            console.log(xhr, status, error);
        }
    });
}

function createBlobFromUrl(url) {
    return fetch(url)
        .then(response => response.blob())
        .then(blob => blob)
        .catch(error => {
            console.error('Error creating Blob from URL:', error);
            throw error;
        });
}

function extractFileNameFromUrl(url) {
    return url.substring(url.lastIndexOf('/') + 1);
}

function blobToFile(blob, fileName) {
    return new File([blob], fileName, {
        type: blob.type,
        lastModified: Date.now()
    });
}

function showConfirmDeleteConversationModal(conversationId) {
    setContentForConfirmModal(
        {
            title: "Delete conversation",
            body: "Are you sure you want to delete this conversation?",
            btnText: "Delete"
        },
        "deleteConversationGroup",
        conversationId
    );
}

function deleteConversationGroup(conversationId) {
    $.ajax({
        url: '/api/conversations/' + conversationId,
        type: 'DELETE',
        success: function(response) {
            if(response.code === 200){
                console.log(response);
                loadConversations();
                showMessageConversation(conversationId);
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
    let isUserAlreadySelected = selectedFriendConversation.some(function(u) {
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

    if(isManager)
        userTagItemContainer.append(userTagButton, removeButton);
    else
        userTagItemContainer.append(userTagButton);
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
                resetDataInConversationModal();
                createConversationModal.modal('hide');
            }
        },
        error: function(xhr, status, error) {
            alert('Error: ' + xhr.responseText);
            console.log(xhr, status, error);
        }
    });
}

function resetDataInConversationModal() {
    resetUsersTagToConversation();
    $("#name-conversation").val("");
    $("#avatar-conversation").val("");
}

function setContentForConversationModal(content){
    createConversationModal.find("#createConversationModalLabel").text(content.title);
    createConversationModal.find("#createNewConversation").text(content.btnText);
    createConversationModal.find("#createNewConversation").attr("action", content.action);
    resetDataInConversationModal();
    createConversationModal.modal('show');
}
