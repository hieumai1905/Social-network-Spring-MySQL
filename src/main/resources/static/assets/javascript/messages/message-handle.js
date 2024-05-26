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
    let prevMessageTime = null;
    messages.forEach(message => {
        const currentMessageTime = new Date(message.sendAt);
        if (message.userSender.userId !== userCurrentId) {
            if (prevMessageTime === null || (currentMessageTime - prevMessageTime) > 600000) {
                html += `<div class="date-break font-xsssss lh-24 fw-500 text-grey-500 mt-2 mb-2 ms-5">${formatTimeFull(message.sendAt)}</div>`;
            }
            html += `
        <div class="message mt-2">
          <figure class="avatar mb-0 float-left me-2">
            <img src="${message.userSender.avatar}" alt="image" class="custom-avatar-35 me-1">
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
            <img src="${message.userSender.avatar}" alt="image" class="custom-avatar-35 me-1">
          </figure>
        </div>
      `;
        }
        prevMessageTime = currentMessageTime;
    });
    let messageContainer = $('#message-container');
    if(html === '') {
        html = '<div class="text-center mt-5">No messages!</div>';
    }
    messageContainer.html(html);
    invertScrollbar(messageContainer);
}

function showMessageConversation(conversationId) {
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
