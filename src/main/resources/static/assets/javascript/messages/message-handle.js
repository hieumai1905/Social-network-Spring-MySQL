'use strict';

let stompClient = null;
let userCurrentId = null;
let room = null;
let prevMessageTime = null;
let userTargetId = null;

function setHeaderPopup(conversationId) {
    let avatar = $('.image-conversation[data-conversation-id=' + conversationId + ']').attr('src');
    let name = $('.model-popup-chat[data-conversation-id=' + conversationId + ']').text();
    let conversationImage = $('#conversation-image-chat');
    let conversationName = $('#conversation-name-chat');
    conversationImage.attr('src', avatar);
    conversationName.text(name);
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
                html += `<div class="text-right date-break font-xsssss lh-24 fw-500 text-grey-500 mt-2 mb-2 me-5">${formatTimeFull(message.sendAt)}</div>`;
            }
            html += `
        <div class="message self text-right mt-2">
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
        JSON.stringify({senderId: userCurrentId, conversationId: room})
    );
}

function onError(error) {
    console.log('Error/ disconnect to chat socket');
}

function onMessageReceived(payload) {
    loadConversations();
    let messageContainer = $('#message-container');
    let message = JSON.parse(payload.body);
    if (message === null) {
        return;
    }
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
            html += `<div class="text-right date-break font-xsssss lh-24 fw-500 text-grey-500 mt-2 mb-2 me-5">${formatTimeFull(message.sendAt)}</div>`;
        }
        html += `
        <div class="message self text-right mt-2">
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
        if (stompClient) {
            let chatMessage = {
                senderId: userCurrentId,
                content: content
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
