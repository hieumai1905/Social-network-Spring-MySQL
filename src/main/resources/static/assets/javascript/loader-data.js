let conversations = [];
let searchInput = null;
let currentUserAvatar = null;
let currentUserId = null;
let currentUserName = null;

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

window.onload = () => {
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
