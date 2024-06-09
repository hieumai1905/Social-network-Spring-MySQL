function _toggleFollow(element) {
    const userId = element.getAttribute('data-user-id');
    const isFollowing = element.getAttribute('data-is-following') === 'true';
    toggleFollowStatus(userId, !isFollowing, element);
}

function handleUnFriend(userId, element) {
    let elementOptions = document.getElementById(`dd-friend-${userId}`);
    elementOptions.remove();

    let container = document.querySelector(`.container-options[data-user-id="${userId}"]`);
    let html = `
    <a href="#" class="ms-3 btn-action"
       data-user-id="${userId}" data-is-requested="false"
       class="btn-add-friend"
       onclick="_addFriend(this)">
        <i class="fa fa-user-plus text-white bg-primary btn-round-md bg-greylight font-xss" aria-hidden="true"></i>
    </a>`;
    container.innerHTML = html;
}

function _unFriend_(element) {
    const userId = element.getAttribute('unfriend-user-id');
    $.ajax({
        url: "/api/relations/friends?user-id=" + userId,
        type: "DELETE",
        success: function (response) {
            if (response.code === 204) {
                console.log(response.message);
                handleUnFriend(userId, element);
            }
        }, error: function (error) {
            console.error(error);
        }
    });
}

function toggleFollowStatus(userId, isFollowing, element) {
    $.ajax({
        url: "/api/relations/follow?user-id=" + userId,
        type: "POST",
        success: function (response) {
            if (response.code === 201 || response.code === 204) {
                console.log(response.message);
                updateFollowStatus(userId, isFollowing, element);
            } else {
                console.error(response.message);
            }
        },
        error: function (error) {
            console.error(error);
        }
    });
}

function updateFollowStatus(userId, isFollowing, element) {
    if (element.classList.contains('dropdown-item')) {
        const icon = element.querySelector('i');
        const text = element.querySelector('h4');
        icon.classList.remove('fa-plus-square', 'fa-window-close');
        icon.classList.add(isFollowing ? 'fa-window-close' : 'fa-plus-square');
        text.textContent = isFollowing ? 'Unfollow' : 'Follow';
        element.setAttribute('data-is-following', isFollowing);
    } else {
        let elementCurrent = $('.btn-follow-profile[data-user-id=' + userId + ']');
        elementCurrent.removeClass('bg-primary text-white bg-grey text-grey-9000');

        if (isFollowing) {
            elementCurrent.addClass('bg-grey text-grey-9000');
            elementCurrent.text('Unfollow');
        } else {
            elementCurrent.addClass('bg-primary text-white');
            elementCurrent.text('Follow');
        }
        element.setAttribute('data-is-following', isFollowing);
    }
}

function handleRequestFriend(element) {
    element.classList.remove('btn-add-friend');
    element.classList.add('btn-cancel-request');
    element.setAttribute('onclick', '_cancelRequest(this)');

    const icon = element.querySelector('.fa');
    icon.classList.remove('fa-user-plus', 'bg-primary');
    icon.classList.add('fa-user-times', 'bg-grey', 'text-grey-700');
}

function handleCancelFriend(element) {
    element.classList.remove('btn-cancel-request');
    element.classList.add('btn-add-friend');
    element.setAttribute('onclick', '_addFriend(this)');

    const icon = element.querySelector('.fa');
    icon.classList.remove('fa-user-times', 'bg-grey', 'text-grey-700');
    icon.classList.add('fa-user-plus', 'bg-primary', 'text-white');
}

function requestFriend(userId, element) {
    $.ajax({
        url: "/api/relations/request?id=" + userId,
        type: "POST",
        success: function (response) {
            if (response.code === 201) {
                console.log(response.message);
                handleRequestFriend(element);
            } else if (response.code === 204) {
                console.error(response.message);
                handleCancelFriend(element);
            }
        },
        error: function (error) {
            console.error(error);
        }
    });
}

function _addFriend(element) {
    const userId = element.getAttribute('data-user-id');
    requestFriend(userId, element);
}

function _cancelRequest(element) {
    const userId = element.getAttribute('data-user-id');
    requestFriend(userId, element);
}

function handleUnfriendProfile(userId, element) {
    alert("Unfriend");
    updateFriendStatus(userId, false, false, element);
}

function handleAddFriendProfile(userId, element) {
    alert("Add friend");
    updateFriendStatus(userId, false, true, element);
}

function cancelRequest(userId, element) {
    alert("Cancel request");
    $.ajax({
        url: "/api/relations/request?id=" + userId,
        type: "POST",
        success: function (response) {
            if (response.code === 204) {
                console.error(response.message);
                handleCancelFriend(element);
            }
        },
        error: function (error) {
            console.error(error);
        }
    });
}

function unFriendProfile(userId, element) {
    $.ajax({
        url: "/api/relations/friends?user-id=" + userId,
        type: "DELETE",
        success: function (response) {
            if (response.code === 204) {
                console.log(response.message);
                handleUnfriendProfile(userId, element);
            }
        },
        error: function (error) {
            console.error(error);
        }
    });
}

function addFriendProfile(userId, element) {
    $.ajax({
        url: "/api/relations/request?id=" + userId,
        type: "POST",
        success: function (response) {
            if (response.code === 201) {
                console.log(response.message);
                handleAddFriendProfile(userId, element);
            }
        },
        error: function (error) {
            console.error(error);
        }
    });
}

function _toggleFriend(element) {
    const userId = element.getAttribute('data-user-id');
    const isFriend = element.getAttribute('data-is-friend') === 'true';
    const isRequested = element.getAttribute('data-is-requested') === 'true';
    if (isFriend) {
        unFriendProfile(userId, element);
    } else if (!isRequested) {
        addFriendProfile(userId, element);
    } else {
        cancelRequest(userId, element);
    }
}

function updateFriendStatus(userId, isFriend, isRequested, element) {
    let elementCurrent = $('.btn-friend-profile[data-user-id=' + userId + ']');
    elementCurrent.removeClass('bg-success text-white bg-grey text-grey-900');

    if (isFriend) {
        elementCurrent.addClass('bg-grey text-grey-900');
        elementCurrent.text('Cancel Friend');
    } else if (isRequested) {
        elementCurrent.addClass('bg-grey text-grey-900');
        elementCurrent.text('Cancel Request');
    } else {
        elementCurrent.addClass('bg-success text-white');
        elementCurrent.text('Add Friend');
    }
    element.setAttribute('data-is-friend', isFriend);
    element.setAttribute('data-is-requested', isRequested);
}

function handleBlockUser() {

    window.location.href = "/";
}

function handleUnBlockUser(userId) {
    let userBlockItem = $('.user-block[data-user-id="' + userId + '"]');
    userBlockItem.remove();
}

function _blockUser(element, isBlock) {
    let userId = element.getAttribute('data-user-id');
    let fullName = element.getAttribute('data-full-name');
    let title = `Block ${fullName}?`;
    let body = `<p>${fullName} will no longer be able to:</p>
                 <div class="ms-3">
                     <ol class="list-group list-group-numbered">
                         <li>See your posts on your timeline</li>
                         <li>Tag you</li>
                         <li>Invite you to events or groups</li>
                         <li>Message you</li>
                         <li>Add you as a friend</li>
                    </ol>
                </div>`;
    if(!isBlock){
        title = `UnBlock ${fullName}`;
        body = `<p>Are you sure you want to unblock ${fullName}?</p>
               <div class="ms-3">
                     <ol class="list-group list-group-numbered">
                         <li>${fullName} may be able to see your timeline or contact you, depending on your privacy settings</li>
                         <li>Tags you and ${fullName} previously added of each other may be restored</li>
                         <li>You can remove tags of yourself on your activity log</li>
                         <li>Please remember you'll have to wait 48 hours before you can re-block ${fullName}.</li>
                    </ol>
                </div>`;
    }
    setContentForConfirmModal({
        title: title,
        body: body,
        btnText: "Confirm"
    }, 'callApiBlockUser', userId);
}

function callApiBlockUser(userId) {
    $.ajax({
        url: "/api/relations/block?user-id=" + userId,
        type: "POST",
        success: function (response) {
            if (response.code === 201) {
                console.log(response.message);
                handleBlockUser();
            } else if (response.code === 204) {
                console.log(response.message);
                handleUnBlockUser(userId);
            }
        },
        error: function (error) {
            console.error(error);
        }
    });
}
