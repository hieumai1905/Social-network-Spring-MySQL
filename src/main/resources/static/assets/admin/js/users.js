let userId = null, userTable = null, btnLockUser = null, btnActiveUser = null, btnChangeRole = null;
$(window).on('load', function() {
    userTable = $('#userTable');
    btnLockUser = $('.btn-lock-user');
    btnActiveUser = $('.btn-active-user');
    btnChangeRole = $('.btn-change-role');
    userTable.DataTable({
        "paging": true,
        "ordering": true,
        "info": true,
    });

    userTable.on('draw.dt', function() {
        btnLockUser.off('click').on('click', function() {
            userId = $(this).attr('data-userId');
            changeUserStatus('locked', $(this));
        });

        btnActiveUser.off('click').on('click', function() {
            userId = $(this).attr('data-userId');
            changeUserStatus('active', $(this));
        });

        btnChangeRole.on('click', function () {
            userId = $(this).attr('data-userId');
            let newRole = $(this).text() === 'Admin' ? 'role_user' : 'role_admin';
            changeUserRole(newRole, $(this));
        });
    });

    btnLockUser.on('click', function () {
        userId = $(this).attr('data-userId');
        changeUserStatus( 'locked', $(this));
    });

    btnActiveUser.on('click', function () {
        userId = $(this).attr('data-userId');
        changeUserStatus('active', $(this));
    });

    btnChangeRole.on('click', function () {
        userId = $(this).attr('data-userId');
        let newRole = $(this).text() === 'Admin' ? 'role_user' : 'role_admin';
        changeUserRole(newRole, $(this));
    });
});

function changeUserStatus(newStatus, element) {
    $.ajax({
        url: '/api/users/change-status-user',
        type: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify({
            userId: userId,
            statusOrRole: newStatus
        }),
        success: function(response) {
            console.log('Status changed successfully:', response);
            if (response.code === 200) {
                replaceButton(newStatus, element);
                $("#status-" + userId).text(newStatus.toUpperCase());
            }
        },
        error: function(xhr, status, error) {
            console.error('Error changing status:', error);
        }
    });
}

function changeUserRole(newRole, element) {
    $.ajax({
        url: '/api/users/change-role-user',
        type: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify({
            userId: userId,
            statusOrRole: newRole
        }),
        success: function(response) {
            console.log('Role changed successfully:', response);
            if (response.code === 200) {
                if (newRole === 'role_user') {
                    element.removeClass('btn-primary').addClass('btn-secondary').text('User');
                } else {
                    element.removeClass('btn-secondary').addClass('btn-primary').text('Admin');
                }
            }
        },
        error: function(xhr, status, error) {
            console.error('Error changing role:', error);
        }
    });
}

function replaceButton(newStatus, element) {
    let newBtn = null;
    if (newStatus === 'locked') {
        newBtn = $('<button>', {
            'class': 'btn btn-success btn-active-user text-white w-68',
            'data-userId': userId,
            'text': 'Active',
            'click': function() {
                userId = $(this).attr('data-userId');
                changeUserStatus( 'active', $(this));
            }
        });
        element.replaceWith(newBtn);
    } else if (newStatus === 'active') {
        newBtn = $('<button>', {
            'class': 'btn btn-danger btn-lock-user text-white w-68',
            'data-userId': userId,
            'text': 'Lock',
            'click': function() {
                userId = $(this).attr('data-userId');
                changeUserStatus('locked', $(this));
            }
        });
        element.replaceWith(newBtn);
    }
}
