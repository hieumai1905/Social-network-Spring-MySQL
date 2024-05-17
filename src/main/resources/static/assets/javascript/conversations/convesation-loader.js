let conversationPrivate = $('#conversations-private');
let conversationGroup = $('#conversations-group');

function displayConversations() {
    let htmlPrivate = '';
    let htmlGroup = '';
    const promises = conversations.map(conversation => {
        if (conversation.type === 'GROUP') {
            htmlGroup += renderConversation(conversation.conversationId, conversation.name, conversation.avatar);
        } else {
            return fetchPersonalConversation(conversation)
                .then(data => {
                    htmlPrivate += renderConversation(conversation.conversationId, data.nickName, data.avatar);
                })
                .catch(error => {
                    console.log('Error loading participants');
                });
        }
    });

    Promise.all(promises)
        .then(() => {
            conversationPrivate.html(htmlPrivate);
            conversationGroup.html(htmlGroup);
        });
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

function renderConversation(conversationId, conversationName, conversationAvatar) {
    return `<li class="bg-transparent list-group-item no-icon pe-0 ps-0 pt-2 pb-2 border-0 d-flex align-items-center">
            <figure class="avatar float-left mb-0 me-2">
            <p class="d-none conversation">${conversationId}</p>
              <img src="${conversationAvatar}" alt="image" class="w35">
            </figure>
            <h3 class="fw-700 mb-0 mt-0">
              <a class="font-xssss text-grey-600 d-block text-dark model-popup-chat" href="#">${conversationName}</a>
            </h3>
            <span class="badge badge-primary text-white badge-pill fw-500 mt-0"></span>
          </li>`;
}
