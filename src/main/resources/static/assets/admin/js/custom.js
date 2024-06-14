$(function() {
    "use strict";

    $(".preloader").fadeOut();
    // this is for close icon when navigation open in mobile view
    $(".nav-toggler").on('click', function() {
        $("#main-wrapper").toggleClass("show-sidebar");
        $(".nav-toggler i").toggleClass("ti-menu");
    });
    $(".search-box a, .search-box .app-search .srh-btn").on('click', function() {
        $(".app-search").toggle(200);
        $(".app-search input").focus();
    });

    // ============================================================== 
    // Resize all elements
    // ============================================================== 
    $("body, .page-wrapper").trigger("resize");
    $(".page-wrapper").delay(20).show();
    
    //****************************
    /* This is for the mini-sidebar if width is less then 1170*/
    //**************************** 
    var setsidebartype = function() {
        var width = (window.innerWidth > 0) ? window.innerWidth : this.screen.width;
        if (width < 1170) {
            $("#main-wrapper").attr("data-sidebartype", "mini-sidebar");
        } else {
            $("#main-wrapper").attr("data-sidebartype", "full");
        }
    };
    $(window).ready(setsidebartype);
    $(window).on("resize", setsidebartype);

});

function setContentForConfirmModal(modalId, content, functionName, ...args){
    $(modalId).find(".modal-title").text(content.title);
    $(modalId).find(".modal-body").html(content.body);
    let modalFooter = $(modalId).find(".modal-footer");
    modalFooter.empty();
    let btnCancel = $('<button>', {
        id: 'btnCancelModal',
        type: 'button',
        class: 'btn btn-dark',
        'data-bs-dismiss': 'modal',
        text: 'Cancel'
    });
    let btnOk = $('<button>', {
        type: 'button',
        class: 'btn-ok btn btn-primary text-white',
        'data-bs-dismiss': 'modal',
        text: `${content.btnText}`
    });
    modalFooter.append(btnCancel, btnOk);
    btnOk.on('click', function() {
        if (typeof window[functionName] === 'function') {
            window[functionName].apply(null, args);
        }
    });
    $(modalId).modal('show');
}
