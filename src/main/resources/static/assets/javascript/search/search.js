let pageIndex = 0, totalElements = 0, pageSize = 0;
let isFetching = false, isRemainResults = true;
let searchContainer = null;

function initSearch() {
    searchContainer = $('#search-results');
    scrollToTop();

    $('#search-input').on('change', function () {
        searchContainer.empty();
        pageIndex = 0;
        isRemainResults = true;
        findUsersByFullName();
    });

    registerScrollEvents();
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
                findUsersByFullName();
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
            handleLoadUsers(data.data);
        },
        error: function (xhr, status, error) {
            console.error(error);
        }
    });
    isFetching = false;
}

function handleLoadUsers(data) {
    pageSize = data?.pageSize;
    totalElements = data?.totalElements;
    if(data?.userResponses === null || data?.userResponses.length === 0) {
        searchContainer.append(`<div class="text-center">No Results Found!</div>`);
    }else{
        data?.userResponses?.forEach(function(user) {
            searchContainer.append(createUserCard(user));
        });
    }
}
function createUserCard(user) {
    return `
        <div class="col-md-12 col-sm-12 pe-2 ps-2">
          <div class="card d-block border-0 shadow-xss rounded-3 overflow-hidden mb-3">
            <div class="card-body w-100 ps-3 pb-4 row">
              <div class="col-md-1">
                <figure class="avatar ms-auto me-auto mb-0 w65 z-index-1">
                  <img src="${user.avatar}" alt="image" class="float-right p-0 bg-white rounded-circle w-100 shadow-xss">
                </figure>
              </div>
              <div class="col-md-9">
                <span class="fw-700 font-xsss mt-3 mb-1">${user.fullName}</span>
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
