let conversations = [];
let searchInput = null;
let currentUserAvatar = null;
let currentUserId = null;
let currentUserName = null;
let _confirmModal = null;

function loadConversations() {
    $.ajax({
        url: '/api/conversations', type: 'GET', success: function (data) {
            conversations = data;
            console.log('Conversations loaded');
            displayConversations().then(r => {
                console.log('Conversations displayed');
            });
        }, error: function (data) {
            console.log('Error loading conversations');
        }
    });
}


function registerConversationsEvents(){
    btnCreateConversation.on("click", function(){
        createConversationModal.modal("show");
    });
    $("#search-friends-conversation").on("input", function () {
        let text = $(this).val().toLowerCase().trim();
        searchFriendConversation = friendsConversation.filter(function (user) {
            return user.fullName.toLowerCase().includes(text);
        });
        displayFriendsViewToConversation();
    });

    $("#createNewConversation").on("click", function() {
        createConversation();
    });
}

function replaceTimeFormat() {
    let createAtSpans = $(".createAtSpan");
    Array.from(createAtSpans).forEach(function (span) {
        let createAtValue = span.getAttribute("data-createAt");
        let createAt = new Date(createAtValue);
        span.textContent = formatTime(createAt);
    });
}

function initVariables() {
    searchInput = $('#search-text');
    currentUserId = $('#current_user_id').val();
    currentUserName = $('#current_user_name').val();
    currentUserAvatar = $('#current_user_avatar').val();
}

function invertScrollbar(element) {
    element.animate({
        scrollTop: 10000000000
    }, 'slow');
}

function redirectToURL(url) {
    window.location.href = url;
}

function setContentForConfirmModal(content, functionName, ...args){
    _confirmModal.find(".modal-title").text(content.title);
    _confirmModal.find(".modal-body").html(content.body);
    let modalFooter = _confirmModal.find(".modal-footer");
    modalFooter.empty();
    let btnCancel = $('<button>', {
        id: 'btnCancelModal',
        type: 'button',
        class: 'btn btn-dark',
        'data-bs-dismiss': 'modal',
        text: 'Cancel'
    });
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
    _confirmModal.modal('show');
}

function setContentForNotifyModal(content){
    _confirmModal.find(".modal-title").text(content.title);
    _confirmModal.find(".modal-body").html(content.body);
    let modalFooter = _confirmModal.find(".modal-footer");
    modalFooter.empty();
    let btnOk = $('<button>', {
        type: 'button',
        class: 'btn-ok btn btn-primary text-white',
        'data-bs-dismiss': 'modal',
        text: `${content.btnText}`
    });
    modalFooter.append(btnOk);
    _confirmModal.modal('show');
}

window.onload = () => {
    registerConversationsEvents();
    _confirmModal = $("#_confirmModal");
    if (typeof loadConversations === 'function') {
        loadConversations();
    }

    if (typeof replaceTimeFormat === 'function') {
        replaceTimeFormat();
    }

    if (typeof initVariables === 'function') {
        initVariables();
    }

    if (typeof initSearch === 'function') {
        initSearch();
    }
    if (typeof registerModalEvent === 'function') {
        registerModalEvent();
    }
    if (typeof initCreatePost === 'function') {
        initCreatePost();
    }
    if (typeof initLike === 'function') {
        initLike();
    }
    if (typeof loadComment === 'function') {
        loadComment();
    }
    if (typeof registerUpdatePhotoEvents === 'function') {
        registerUpdatePhotoEvents();
    }
    if (typeof initMessage === 'function') {
        initMessage();
    }
};
