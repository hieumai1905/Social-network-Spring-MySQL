let conversations = [];
let searchInput = null;

function loadConversations() {
    $.ajax({
        url: '/api/conversations', type: 'GET', success: function (data) {
            conversations = data;
            console.log('Conversations loaded');
            displayConversations();
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
    if (typeof loadComment() === 'function') {
        loadComment();
    }
};
