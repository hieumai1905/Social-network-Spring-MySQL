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

loadConversations();
