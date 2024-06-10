function addRequestFriendToContainer(requestFriend) {
    let requestSetAt = new Date(requestFriend.setAt);
    let requestContainer = $('#request-friend-container');
    let html = `
      <div class="block request-friend" data-request-id="${requestFriend.user.userId}">
        <div class="card-body d-flex ps-4 pe-4 pb-0 bor-0">
          <figure class="avatar me-3">
            <img src="${requestFriend.user.avatar}" alt="image" class="shadow-sm rounded-circle w45">
          </figure>
          <div class="flex-column">
            <div class="d-flex">
              <h3 class="fw-700 text-grey-900 font-xssss mt-1">
                <a class="fw-700 text-grey-900 font-xssss mt-1" href="profile?id=${requestFriend.user.userId}">${requestFriend.user.fullName}</a>
              </h3>
              <p class="d-block font-xssss fw-500 mb-1 ms-1 lh-1 text-grey-500">${formatTime(requestSetAt)}</p>
            </div>
            ${requestFriend.mutualFriendCount > 0 ? `<p class="d-block font-xssss fw-500 lh-1 text-grey-500">${requestFriend.mutualFriendCount} mutual friends</p>` : ''}
          </div>
        </div>
        <div class="card-body d-flex align-items-center pt-0 ps-4 pe-4 border-bottom">
          <a href="#" class="p-2 lh-20 w100 bg-primary me-2 text-white text-center font-xsss fw-600 ls-1 rounded-xl" data-request-id="${requestFriend.user.userId}" onclick="confirmRequest(this, false)">Confirm</a>
          <a href="#" class="p-2 lh-20 w100 bg-grey text-grey-800 text-center font-xsss fw-600 ls-1 rounded-xl" data-request-id="${requestFriend.user.userId}" onclick="deleteRequest(this, false)">Delete</a>
        </div>
      </div>
    `;
    requestContainer.append(html);
}

function updateContainerRequestFriend() {
    $.ajax({
        url: "/api/relations/request",
        type: "GET",
        success: function (response) {
            if (response.code === 200) {
                console.log(response.message);
                let requestFriends = response.data;
                if (requestFriends.length > 1) {
                    addRequestFriendToContainer(requestFriends[1]);
                }
            }
        }, error: function (error) {
            console.error(error);
        }
    });
    let requestCount = $('#request-friend-count');
    let value = requestCount.text();
    if (requestCount.text() === '1') {
        requestCount.hide();
    } else if (requestCount.text() > 1) {
        requestCount.text(parseInt(requestCount.text()) - 1);
    }
}

function handleDeleteRequestSuccess(userId, isPageRequest) {
    if(isPageRequest) {
        let requestElement = $('.request-friend-page[data-request-friend-id=' + userId + ']');
        requestElement.remove();
    }else{
        let requestElement = $('.request-friend[data-request-id=' + userId + ']');
        requestElement.remove();
        updateContainerRequestFriend();
    }
}

function deleteRequest(element, isPageRequest) {
    let userId = element.getAttribute('data-request-id');
    $.ajax({
        url: "/api/relations/reject?user-id=" + userId,
        type: "DELETE",
        success: function (response) {
            if (response.code === 204) {
                console.log(response.message);
                handleDeleteRequestSuccess(userId, isPageRequest);
            }
        },
        error: function (error) {
            console.error(error);
        }
    });
}

function handleConfirmRequestSuccess(userId, isPageRequest) {
    if(isPageRequest) {
        let requestElement = $('.request-friend-page[data-request-friend-id=' + userId + ']');
        requestElement.remove();
    }else{
        let requestElement = $('.request-friend[data-request-id=' + userId + ']');
        requestElement.remove();
        updateContainerRequestFriend();
    }
}

function confirmRequest(element, isPageRequest) {
    let userId = element.getAttribute('data-request-id');
    $.ajax({
        url: "/api/relations/accept?user-id=" + userId,
        type: "POST",
        success: function (response) {
            if (response.code === 201) {
                console.log(response.message);
                handleConfirmRequestSuccess(userId, isPageRequest);
            }
        },
        error: function (error) {
            console.error(error);
        }
    });
}
