let conversations = [];

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

window.onload = () => {
    loadConversations();
    replaceTimeFormat();
}
