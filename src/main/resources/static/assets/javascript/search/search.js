let pageIndex = 1, totalElements = 0, pageSize = 0;
let isFetching = false, isRemainResults = true, isChangeConditionSearch = false;
let searchContainer = null, selectSearchType = 'post';

function initSearch() {
    searchContainer = $('#search-results');
    scrollToTop();

    $('#search-input').on('change', function () {
        searchContainer.empty();
        pageIndex = 0;
        isRemainResults = true;
        isChangeConditionSearch = true;
        handleSearch();
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

function handleSearch(){
    let newUrl = `/search?type=${selectSearchType}&q=${$('#search-input').val()}`;
    history.pushState(null, '', newUrl);
    $("#searchBy").text(selectSearchType);
    if(selectSearchType === 'people') {
        findUsersByFullName();
    }else if(selectSearchType === 'post'){
        findPostByContentAndHashtags();
    }
}

function findPostByContentAndHashtags(){
    isFetching = true;
    let content = $('#search-input').val();
    $.ajax({
        url: "api/posts/search",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(setSearchPostRequest(content)),
        success: function(response) {
            console.log(response);
            handleLoadSearchResults(response.data, 'post');
            if(isChangeConditionSearch){
                pageIndex++;
                isChangeConditionSearch = false;
            }
        },
        error: function(xhr, status, error) {
            console.log('error: ', xhr.responseText);
        }
    });
    isFetching = false;
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
    $(window).on('load', function() {
        $("html, body").animate({ scrollTop: 0 }, "slow");
    });
}

function registerScrollEvents(){
    $(window).scroll(function() {
        if($(window).scrollTop() + $(window).height() === $(document).height()) {
            let remainElements = totalElements - pageIndex * pageSize;
            if (!isFetching && (pageIndex === 0 || remainElements > 0)) {
                handleSearch();
                pageIndex++;
            }else{
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
            if(isChangeConditionSearch){
                pageIndex++;
                isChangeConditionSearch = false;
            }
        },
        error: function (xhr, status, error) {
            console.error(error);
        }
    });
    isFetching = false;
}

function handleLoadSearchResults(data, type) {
    pageSize = data?.pageSize;
    totalElements = data?.totalElements;
    if(type === 'people') {
        if(data?.userResponses === null || data?.userResponses.length === 0) {
            searchContainer.append(`<div class="text-center">No Results Found!</div>`);
        }else {
            data?.userResponses?.forEach(function (user) {
                searchContainer.append(createUserCard(user));
            });
        }
    }
    else if(type === 'post'){
        if(data?.postResponseDTOS === null || data?.postResponseDTOS.length === 0) {
            searchContainer.append(`<div class="text-center">No Results Found!</div>`);
        }else {
            data?.postResponseDTOS?.forEach(post => {
                savePostToUI(post, true, true);
            });
        }
    }
}
function createUserCard(user) {
    return `
        <div class="col-md-12 col-sm-12 pe-2 ps-2">
          <div class="card d-block border-0 shadow-xss rounded-3 overflow-hidden mb-3">
            <div class="card-body w-100 ps-3 pb-4 row">
              <div class="col-md-1">
                <figure class="avatar ms-auto me-auto mb-0 z-index-1">
                  <img src="${user.avatar}" alt="image" class="p-0 bg-white rounded-circle custom-avatar-65 shadow-xss">
                </figure>
              </div>
              <div class="col-md-9">
                <span class="fw-700 font-xss mt-5 ms-3 text-grey-900">${user.fullName}</span>
                <p class="custom-text-overflow">
                </p>
              </div>
              <div class="col-md-2 mt-4">
                ${user.isFriend ?
        `<a href="#" class="mt-0 btn pt-2 pb-2 ps-3 pe-3 lh-24 ms-1 ls-3 d-inline-block rounded-xl bg-danger font-xss fw-700 ls-lg text-white">FRIEND</a>` :
        `<a href="#" style="width:120px; height: 45px;" class="mt-0 btn pt-2 pb-2 ps-3 pe-3 lh-24 ms-1 ls-3 d-inline-block rounded-xl bg-success font-xssss fw-700 ls-lg text-white">ADD FRIEND</a>`
    }
              </div>
            </div>
          </div>
        </div>
      `;
}
