function leaveConversationGroup(conversationId) {
    return fetchParticipantJoined(conversationId)
        .then(data => {
            if (data.code !== 200) {
                console.log(data.message);
                return;
            }
            loadConversations();
        })
        .catch(error => {
            console.log(error);
        });
}

function fetchParticipantJoined(conversationId) {
    return new Promise((resolve, reject) => {
        $.ajax({
            url: 'api/participants/conversations/' + conversationId + '/leave',
            type: 'POST',
            success: function (data) {
                resolve(data);
            },
            error: function (data) {
                reject(data);
            }
        });
    });
}
