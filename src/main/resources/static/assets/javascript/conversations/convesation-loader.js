let conversationPrivate = $('#conversations-private');
let conversationGroup = $('#conversations-group');
let popupChat = $('#modal-popup-chat')

function displayConversations() {
    let htmlPrivate = '';
    let htmlGroup = '';
    const promises = conversations.map(conversation => {
        if (conversation.type === 'GROUP') {
            htmlGroup += renderConversation(conversation.conversationId, conversation.name, conversation.avatar, true);
        } else {
            return fetchPersonalConversation(conversation)
                .then(data => {
                    htmlPrivate += renderConversation(conversation.conversationId, data.nickName, data.avatar, false);
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

function renderConversation(conversationId, conversationName, conversationAvatar, isGroup) {
    const commonHtml = `
      <li class="bg-transparent list-group-item no-icon pe-0 ps-0 pt-2 pb-2 border-0 d-flex align-items-center"
      onclick="showMessageConversation(${conversationId})">
        <figure class="avatar float-left mb-0 me-2">
          <p class="d-none conversation">${conversationId}</p>
          <img src="${conversationAvatar}" alt="image" class="custom-avatar-50 image-conversation" data-conversation-id="${conversationId}">
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
        <div class="ps-1 px-1 dropdown-menu dropdown-menu-end rounded-xxxl border-0 shadow-lg" aria-labelledby="dd-conversation-${conversationId}">
    `;

    if (isGroup) {
        return `${commonHtml}
          <div class="card-body p-0 dropdown-item d-flex">
            <i class="px-1 ms-3 fa fa-sign-out text-grey-600 mt-1" aria-hidden="true"></i>
            <span class="px-1 cursor-pointer d-block font-xssss fw-500 mt-1 lh-3 text-grey-600" onclick="leaveConversationGroup(${conversationId})">&ensp;Leave group</span>
          </div>
        </div>
      </li>
    `;
    } else {
        return `${commonHtml}
        </div>
      </li>
    `;
    }
}
