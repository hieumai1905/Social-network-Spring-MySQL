let notificationContainer;
let notificationCount;

function loadNotification() {
    connectNotificationSocket();
}

function connectNotificationSocket() {
    userCurrentId = $('#current_user_id').val();
    if (userCurrentId) {
        let socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, onNotificationConnected, onError);
    }
}

function sendNotificationEvent(message, userId, type, userTargetId, dataId) {
    if (userId === userTargetId) {
        return;
    }
    if (stompClient) {
        let data = {
            senderId: userId,
            content: message,
            messageType: 'NOTIFICATION',
            userTargetId: userTargetId,
            typeNotification: type,
            dataId: dataId
        };
        stompClient.send("/app/chat.sendMessage/-999999",
            {},
            JSON.stringify(data));
    }
}

function incrementNotificationCount(userId) {
    notificationCount = $('#notification-count[data-user-id="' + userId + '"]');
    if (notificationCount.hasClass("d-none")) {
        notificationCount.removeClass("d-none");
        notificationCount.text(1);
    } else {
        let count = parseInt(notificationCount.text());
        notificationCount.text(count + 1);
    }
}

function onNotificationReceived(payload) {
    let notification = JSON.parse(payload.body);
    if (notification === null) {
        return;
    }
    notificationContainer = $("#notification-container[data-user-id='" + notification.userTarget.userId + "']");
    let html = `
    <div class="card custom-card-hover cursor-pointer pt-2 px-1 pb-2 bg-noseen bg-current-shade w-100 border-0 ps-5 mb-1"
     data-notification-id="${notification.notificationId}"
     data-urlRedirect="${notification.urlRedirect}" 
     onclick="markAsSeenAndRedirect(this)">
        <img src="${notification.image ? notification.image : notification.user.avatar}" alt="user" class="w50 position-absolute left-0 custom-avatar-50 rounded-circle">
        <h5 class="font-xsss mb-1 mt-0 fw-700 d-block">
            <span class="ms-2">${notification.user.fullName}</span>
            <span class="font-xsssss fw-600 float-right mt-1 text-primary">${formatTime(new Date())}</span>
        </h5>
        <h6 class="ms-2 text-grey-600 fw-500 font-xssss lh-4">
            ${notification.user.gender === 'MALE' ? 'He ' + notification.content :
        (notification.user.gender === 'FEMALE' ? 'She ' + notification.content :
            notification.user.fullName + ' ' + notification.content)}
        </h6>
    </div>
                `;
    notificationContainer.prepend(html);
    incrementNotificationCount(notification.userTarget.userId);
    let notNotification = $('#notification-not-data[data-user-id="' + notification.userTarget.userId + '"]');
    if (notNotification) {
        notNotification.remove();
    }
}

function onNotificationConnected() {
    stompClient.subscribe('/conversations/-999999', onNotificationReceived);
    stompClient.send("/app/chat.addUser/-999999",
        {},
        JSON.stringify({senderId: userCurrentId, conversationId: '-999999', type: 'NOTIFICATION'})
    );
}

function markAsSeenAndRedirect(element) {
    element.classList.remove('bg-noseen');
    let notificationId = parseInt(element.getAttribute('data-notification-id'));
    if (notificationId) {
        $.ajax({
            url: `/api/notifications/${notificationId}/mask-as-seen`,
            type: 'PUT',
            success: function (response) {
                if (response.code === 204) {
                    const urlRedirect = element.getAttribute('data-urlRedirect');
                    element.classList.remove('bg-noseen');
                    redirectToURL(urlRedirect);
                }
            },
            error: function (xhr, status, error) {
                console.log(error);
            }
        });
    }
}