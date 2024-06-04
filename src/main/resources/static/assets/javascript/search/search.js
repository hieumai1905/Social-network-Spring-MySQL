let pageIndex = 1, totalElements = 0, pageSize = 0;
let isFetching = false, isRemainResults = true, isChangeConditionSearch = false;
let searchContainer = null, selectSearchType = 'people';

function initSearch() {
    searchContainer = $('#search-results');
    scrollToTop();
    findUsersByFullName();

    $('#search-input').on('change', function () {
        searchContainer.empty();
        pageIndex = 0;
        isRemainResults = true;
        isChangeConditionSearch = true;
        if ($('#search-input').val()) {
            handleSearch();
        }
    });

    $('.search-results-by').on('click', function () {
        selectSearchType = $(this).attr('data-search-by');
        searchContainer.empty();
        pageIndex = 0;
        isRemainResults = true;
        isChangeConditionSearch = true;
        handleSearch();
    });

    registerScrollEvents();
}

function handleSearch() {
    let newUrl = `/search?type=${selectSearchType}&q=${$('#search-input').val()}`;
    history.pushState(null, '', newUrl);
    $("#searchBy").text(selectSearchType);
    if (selectSearchType === 'people') {
        findUsersByFullName();
    } else if (selectSearchType === 'post') {
        findPostByContentAndHashtags();
    }
}

function findPostByContentAndHashtags() {
    isFetching = true;
    let content = $('#search-input').val();
    $.ajax({
        url: "api/posts/search",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(setSearchPostRequest(content)),
        success: function (response) {
            console.log(response);
            handleLoadSearchResults(response.data, 'post');
            if (isChangeConditionSearch) {
                pageIndex++;
                isChangeConditionSearch = false;
            }
            isFetching = false;
        },
        error: function (xhr, status, error) {
            console.log('error: ', xhr.responseText);
        }
    });
}

function setSearchPostRequest(inputText) {
    const words = inputText.trim().split(/\s+/);
    const searchPostRequestDTO = {
        hashtags: [],
        content: "",
        pageIndex: pageIndex
    };
    const contentBuilder = [];
    words.forEach(word => {
        if (word.startsWith("#")) {
            searchPostRequestDTO.hashtags.push(word);
        } else {
            contentBuilder.push(word);
        }
    });
    searchPostRequestDTO.content = contentBuilder.join(" ").trim();
    return searchPostRequestDTO;
}

function scrollToTop() {
    $(window).on('load', function () {
        $("html, body").animate({scrollTop: 0}, "slow");
    });
}

function registerScrollEvents() {
    $(window).scroll(function () {
        if ($(window).scrollTop() + $(window).height() + 100 >= $(document).height()) {
            if (isFetching) return;
            let remainElements = totalElements - pageIndex * pageSize;
            if (!isFetching && (pageIndex === 0 || remainElements > 0)) {
                handleSearch();
                pageIndex++;
            } else {
                if ($('#search-results #no-user-remaining').length === 0) {
                    searchContainer.append('<div class="text-center" id="no-user-remaining">No Results Remaining!</div>');
                }
            }
        }
    });
}

function findUsersByFullName() {
    isFetching = true;
    let formData = {
        fullName: $('#search-input').val(),
        pageIndex: pageIndex,
        type: 'people'
    };

    $.ajax({
        type: "GET",
        url: "/api/search",
        data: formData,
        success: function (data) {
            console.log(data);
            handleLoadSearchResults(data.data, 'people');
            if (isChangeConditionSearch) {
                pageIndex++;
                isChangeConditionSearch = false;
            }
            isFetching = false;
        },
        error: function (xhr, status, error) {
            console.error(error);
        }
    });
}

function handleLoadSearchResults(data, type) {
    pageSize = data?.pageSize;
    totalElements = data?.totalElements;
    if (type === 'people') {
        if (data?.userResponses === null || data?.userResponses.length === 0) {
            searchContainer.append(`<div class="text-center">No Results Found!</div>`);
        } else {
            data?.userResponses?.forEach(function (user) {
                searchContainer.append(createUserCard(user));
            });
        }
    } else if (type === 'post') {
        if (data?.postResponseDTOS === null || data?.postResponseDTOS.length === 0) {
            searchContainer.append(`<div class="text-center">No Results Found!</div>`);
        } else {
            data?.postResponseDTOS?.forEach(post => {
                savePostToUI(post, true, true);
            });
        }
    }
}

function createUserCard(user) {
    return `
    <div class="col-md-12 col-sm-12 pe-2 ps-2">
      <div class="card d-block border-0 shadow-xss rounded-3 mb-3">
        <div class="card-body w-100 ps-3 pb-4 row">
          <div class="col-md-1">
            <figure class="avatar ms-auto me-auto mb-0 z-index-1">
              <img src="${user.avatar}" alt="image" class="p-0 bg-white rounded-circle custom-avatar-65 shadow-xss">
            </figure>
          </div>
          <div class="col-md-9">
            <a href="/profile?user-id=${user.userId}" class="fw-700 font-xss mt-5 ms-2 text-grey-900">${user.fullName}</a>
            <div>
                <p class="fw-500 p-0 m-0 ms-2 font-xssss text-grey-500" ${user.mutualFriendCount > 0 ? '' : 'style="display: none;"'}>
                  ${user.mutualFriendCount} mutual friends
                </p>
            </div>
          </div>
          <div class="col-md-2 col-sm-2 mt-2 container-options text-right" data-user-id="${user.userId}">
            ${user.isFriend
        ? `<a href="#" class="ms-3" id="dd-friend-${user.userId}" data-bs-toggle="dropdown" aria-expanded="false">
                   <i class="ti-more-alt text-grey-900 btn-round-md bg-greylight font-xss"></i>
                 </a>
                 <div class="dropdown-menu p-1 rounded-xxxl mt-3 cursor-pointer border-0 shadow-lg" aria-labelledby="dd-friend-${user.userId}">
                   <div class="card-body p-1 dropdown-item rounded-xxxl d-flex" onclick="_toggleFollow(this)" data-user-id="${user.userId}" data-is-following="${user.isFollowing}">
                     <i class="fa font-lg mt-1 ms-2 ${user.isFollowing ? 'fa-window-close' : 'fa-plus-square'}" aria-hidden="true"></i>
                     <h4 class="fw-600 text-grey-900 font-xsss mt-2 ms-2 fw-bold">${user.isFollowing ? 'Unfollow' : 'Follow'}</h4>
                   </div>
                   <div class="card-body p-1 dropdown-item rounded-xxxl d-flex" onclick="_unFriend_(this)" unfriend-user-id="${user.userId}">
                     <i class="fa fa-user-times font-sm mt-1 ms-2" aria-hidden="true"></i>
                     <h4 class="fw-600 text-grey-900 font-xsss mt-2 ms-2 fw-bold">Unfriend</h4>
                   </div>
                 </div>`
        : user.isRequested
            ? `<a href="#" class="ms-3 btn-action btn-cancel-request" data-user-id="${user.userId}" data-is-requested="${user.isRequested}" onclick="_cancelRequest(this)">
                     <i class="fa fa-user-times text-grey-700 bg-grey btn-round-md bg-greylight font-xss" aria-hidden="true"></i>
                   </a>`
            : `<a href="#" class="ms-3 btn-action btn-add-friend" data-user-id="${user.userId}" data-is-requested="${user.isRequested}" onclick="_addFriend(this)">
                     <i class="fa fa-user-plus text-white bg-primary btn-round-md bg-greylight font-xss" aria-hidden="true"></i>
                   </a>`
    }
          </div>
        </div>
      </div>
    </div>
  `;
}
