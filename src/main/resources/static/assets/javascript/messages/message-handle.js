'use strict';

let stompClient = null;
let userCurrentId = null;
let room = null;
let prevMessageTime = null;
let userTargetId = null;

function setHeaderPopup(conversationId) {
    const conversation = $(`.model-popup-chat[data-conversation-id="${conversationId}"]`);
    const avatar = $(`.image-conversation[data-conversation-id="${conversationId}"]`).attr('src');
    const name = conversation.text();
    const userId = conversation.attr('data-user-id');
    const link = `/profile?user-id=${userId}`;

    const conversationImage = $('#conversation-image-chat');
    const conversationName = $('#conversation-name-chat');

    conversationImage.attr('src', avatar);
    conversationName.text(name);
    conversationName.attr('href', link);

    conversationName.off('click').on('click', function(e) {
        e.preventDefault();
        window.location.href = link;
    });
}

function showPopupConversation(data, conversationId) {
    setHeaderPopup(conversationId);
    setMessage(data);
    popupChat.addClass('d-block');
}

function setMessage(messages) {
    let userCurrentId = $('#current_user_id').val();
    let html = '';
    prevMessageTime = null;
    messages.forEach(message => {
        const currentMessageTime = new Date(message.sendAt);
        if (message.userSender.userId !== userCurrentId) {
            if (prevMessageTime === null || (currentMessageTime - prevMessageTime) > 600000) {
                html += `<div class="date-break font-xsssss lh-24 fw-500 text-grey-500 mt-2 mb-2 ms-5">${formatTimeFull(message.sendAt)}</div>`;
            }
            html += `
        <div class="message mt-2">
          <figure class="avatar mb-0 float-left me-2">
            <img src="${message.userSender.avatar}" alt="image" class="custom-avatar-35 me-1 rounded-circle">
          </figure>
          <div class="message-content font-xssss lh-24 fw-500">${message.content}</div>
        </div>
      `;
        } else {
            if (prevMessageTime === null || (currentMessageTime - prevMessageTime) > 600000) {
                html += `<div class="text-right date-break font-xsssss lh-24 fw-500 text-grey-500 mt-2 mb-2 me-5 message-time" 
                data-message-id="${message.messageId}">
                        ${formatTimeFull(message.sendAt)}
                        </div>`;
            }
            html += `
        <div class="message self text-right mt-2" data-message-id="${message.messageId}">
            <a href="#" class="ms-auto" id="dd-message-id-${message.messageId}" data-bs-toggle="dropdown"
                   aria-expanded="false"><i
                        class="ti-more-alt text-grey-900 btn-round-md bg-greylight font-xss"></i>
           </a>
           <div class="dropdown-menu dropdown-menu-end p-1 mt-2 rounded-xxxl cursor-pointer border-0 shadow-lg dropup"
                     aria-labelledby="dd-message-id-${message.messageId}">
                  <div class="card-body p-0 dropdown-item rounded-xxxl d-flex" data-message-id="${message.messageId}" onclick="deleteMessage(this)">
                        <i class="fa fa-trash-o text-grey-500 ms-2 fw-600 font-sm"></i>
                        <h4 class="fw-600 text-grey-900 font-xssss ms-2 mt-1">Delete Message</h4>
                  </div>
             </div>
          <div class="message-content font-xssss lh-24 fw-500">${message.content}</div>
          <figure class="avatar mb-0 float-right ms-2">
            <img src="${message.userSender.avatar}" alt="image" class="custom-avatar-35 me-1 rounded-circle">
          </figure>
        </div>
      `;
        }
        prevMessageTime = currentMessageTime;
    });
    let messageContainer = $('#message-container');
    if (html === '') {
        html = '<div class="text-center mt-5" id="no-message">No messages!</div>';
    }
    messageContainer.html(html);
    invertScrollbar(messageContainer);
}

function deleteMessage(element) {
    let messageId = element.getAttribute('data-message-id');
    $.ajax({
        url: '/api/messages/' + messageId,
        type: 'DELETE',
        success: function (response) {
            if (response.code === 200) {
                console.log(response.message);
                let blockMessage = $('.message[data-message-id=' + messageId + ']');
                let blockMessageTime = $('.message-time[data-message-id=' + messageId + ']');
                blockMessage.remove();
                blockMessageTime.remove();
            }
        },
        error: function (data) {
            console.log('Error deleting message');
        }
    });

}

function showMessageConversation(conversationId) {
    room = conversationId;
    connectChatSocket();
    $.ajax({
        url: '/api/messages/conversations/' + conversationId,
        type: 'GET',
        success: function (response) {
            if (response.code === 200) {
                console.log(response.message);
                showPopupConversation(response.data, conversationId);
            }
        },
        error: function (data) {
            console.log('Error loading messages');
        }
    });
}

function connectChatSocket() {
    userCurrentId = $('#current_user_id').val();
    if (userCurrentId) {
        let socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, onConnected, onError);
    }
}

function onConnected() {
    stompClient.subscribe('/conversations/' + room, onMessageReceived);
    stompClient.send("/app/chat.addUser/" + room,
        {},
        JSON.stringify({senderId: userCurrentId, conversationId: room, type: 'CHAT'})
    );
}

function onError(error) {
    console.log('Error/ disconnect to chat socket');
}

function onMessageReceived(payload) {
    let messageContainer = $('#message-container');
    let message = JSON.parse(payload.body);
    if (message === null) {
        return;
    }
    loadConversations();
    const currentMessageTime = new Date(message.sendAt);
    $('#no-message').remove();
    let html = '';
    if (message.sender.userId !== userCurrentId) {
        if (prevMessageTime === null || (currentMessageTime - prevMessageTime) > 600000) {
            html += `<div class="date-break font-xsssss lh-24 fw-500 text-grey-500 mt-2 mb-2 ms-5">${formatTimeFull(message.sendAt)}</div>`;
        }
        html += `
        <div class="message mt-2">
          <figure class="avatar mb-0 float-left me-2">
            <img src="${message.sender.avatar}" alt="image" class="custom-avatar-35 me-1 rounded-circle">
          </figure>
          <div class="message-content font-xssss lh-24 fw-500">${message.content}</div>
        </div>
      `;
    } else {
        if (prevMessageTime === null || (currentMessageTime - prevMessageTime) > 600000) {
            html += `<div class="text-right date-break font-xsssss lh-24 fw-500 text-grey-500 mt-2 mb-2 me-5 message-time" 
                data-message-id="${message.messageId}"></div>`;
        }
        html += `
        <div class="message self text-right mt-2" data-message-id="${message.messageId}">
            <a href="#" class="ms-auto" id="dd-message-id-${message.messageId}" data-bs-toggle="dropdown"
                   aria-expanded="false"><i
                        class="ti-more-alt text-grey-900 btn-round-md bg-greylight font-xss"></i>
            </a>
           <div class="dropdown-menu dropdown-menu-end p-1 mt-2 rounded-xxxl cursor-pointer border-0 shadow-lg dropup"
                     aria-labelledby="dd-message-id-${message.messageId}">
                  <div class="card-body p-0 dropdown-item rounded-xxxl d-flex" data-message-id="${message.messageId}" onclick="deleteMessage(this)">
                        <i class="fa fa-trash-o text-grey-500 ms-2 fw-600 font-sm"></i>
                        <h4 class="fw-600 text-grey-900 font-xssss ms-2 mt-1">Delete Message</h4>
                  </div>
             </div>
          <div class="message-content font-xssss lh-24 fw-500">${message.content}</div>
          <figure class="avatar mb-0 float-right ms-2">
            <img src="${message.sender.avatar}" alt="image" class="custom-avatar-35 me-1 rounded-circle">
          </figure>
        </div>
      `;
    }
    prevMessageTime = currentMessageTime;
    messageContainer.append(html);
    invertScrollbar(messageContainer);
}

function sendMessageEvent() {
    let inputMessage = $('#input-message');
    let content = inputMessage.val().trim();
    if (content) {
        if (stompClient && room) {
            let chatMessage = {
                senderId: userCurrentId,
                content: content,
                messageType: 'CHAT'
            };
            stompClient.send("/app/chat.sendMessage/" + room,
                {},
                JSON.stringify(chatMessage));
            inputMessage.val('');
        } else {
            handleForNewConversation();
        }
    }
}

function handleForNewConversation() {
    $.ajax({
        url: '/api/conversations/target/users/' + userTargetId,
        type: 'POST',
        success: function (response) {
            if (response.code === 200) {
                console.log(response.message);
                room = response.data.conversationId;
                connectChatSocket();
                sendMessageEvent();
            }
        },
        error: function (data) {
            console.log('Error loading messages');
        }
    });
}

function initMessage() {
    let inputMessage = $('#input-message');

    inputMessage.on('keydown', function (event) {
        if (event.keyCode === 13) {
            sendMessageEvent();
        }
    });
}

function showConversationInProfile(element) {
    let userId = element.getAttribute('data-user-id');
    $.ajax({
        url: '/api/conversations/target/users?id=' + userId,
        type: 'GET',
        success: function (response) {
            if (response.code === 200) {
                console.log(response.message);
                showMessageConversation(response.data.conversationId);
            } else if (response.code === 404) {
                handleForConversationNotFound(userId);
            }
        },
        error: function (data) {
            console.log('Error loading messages');
        }
    });
}

function handleForConversationNotFound(userId) {
    $.ajax({
        url: '/api/users?id=' + userId,
        type: 'GET',
        success: function (response) {
            if (response.code === 200) {
                console.log(response.message);
                let avatar = response.data.avatar;
                let name = response.data.fullName;
                let conversationImage = $('#conversation-image-chat');
                let conversationName = $('#conversation-name-chat');
                conversationImage.attr('src', avatar);
                conversationName.text(name);
                popupChat.addClass('d-block');
                userTargetId = response.data.userId;
            }
        },
        error: function (data) {
            console.log('Error loading messages');
        }
    });
}
