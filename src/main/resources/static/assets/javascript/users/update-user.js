function getCodeChangeEmail() {
    const error = $('#update-email-error');
    const email = $('#change-email-current').val();
    const newEmail = $('#change-email-new').val();
    const btnGetCode = $('#btn-get-code');
    const btnChangeEmail = $('#btn-save-change-email');
    btnGetCode.prop('disabled', true);

    if (validateEmails(email, newEmail, error)) {
        $.ajax({
            url: '/api/settings/change-email/get-code',
            type: 'POST',
            data: {
                oldEmail: email,
                newEmail: newEmail
            },
            success: handleGetCodeSuccess,
            error: handleAjaxError
        });
    }
}

function validateEmails(currentEmail, newEmail, error) {
    const emailRegex = /^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$/i;

    if (!emailRegex.test(currentEmail) || !emailRegex.test(newEmail)) {
        displayError(error, 'Please enter valid email addresses');
        currentEmail ? $('#change-email-new').focus() : $('#change-email-current').focus();
        return false;
    }

    if (currentEmail.toLowerCase() === newEmail.toLowerCase()) {
        displayError(error, 'The new email must be different from the current email');
        $('#change-email-new').focus();
        return false;
    }

    return true;
}

function saveChangeEmail() {
    const code = $('#input-code').val().trim();
    const codeRegex = /^[0-9]{6}$/;
    const error = $('#update-email-error');
    const email = $('#change-email-current').val();
    const newEmail = $('#change-email-new').val();

    if (!codeRegex.test(code)) {
        displayError(error, 'Please enter a 6-digit code');
        $('#input-code').focus();
    } else {
        $.ajax({
            url: '/api/settings/change-email/confirm-code?code=' + code,
            type: 'POST',
            data: {
                oldEmail: email,
                newEmail: newEmail
            },
            success: handleConfirmCodeSuccess,
            error: handleAjaxError
        });
    }
}

function displayError(errorElement, message) {
    errorElement.removeClass('d-none');
    errorElement.html(`<p class="p-2 ps-3 text-white font-xsss">${message}</p>`);
}

function handleGetCodeSuccess(response) {
    const error = $('#update-email-error');
    const btnGetCode = $('#btn-get-code');
    const btnChangeEmail = $('#btn-save-change-email');

    if (response.code === 200) {
        $('#change-email-current').prop('disabled', true);
        $('#change-email-new').prop('disabled', true);
        $('#input-code-label').removeClass('d-none');
        btnGetCode.addClass('d-none');
        displayError(error, response.message);
        btnChangeEmail.removeClass('d-none');
    } else if (response.code === 400) {
        btnGetCode.prop('disabled', false);
        displayError(error, response.message);
    }
}

function handleConfirmCodeSuccess(response) {
    const error = $('#update-email-error');

    if (response.code === 200) {
        displayError(error, response.message);
        resetForm();
    } else {
        displayError(error, response.message);
    }
}

function handleAjaxError(xhr, status, error) {
    alert('An error occurred: ' + error);
}

function resetForm() {
    $('#change-email-current').prop('disabled', false);
    $('#change-email-new').prop('disabled', false);
    $('#input-code-label').addClass('d-none');
    $('#btn-get-code').removeClass('d-none');
    $('#btn-save-change-email').addClass('d-none');
    $('#input-code').val('');
    $('#change-email-current').val('');
    $('#change-email-new').val('');
}
