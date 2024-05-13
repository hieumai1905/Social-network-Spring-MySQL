function validateName(name) {
    return name !== '';
}

function validateEmail(email) {
    let emailRegex = /^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/;
    if (email === '') {
        return false;
    }
    return emailRegex.test(email);
}

function validatePhone(phone) {
    let phoneRegex = /^\d{10}$/;
    if (phone === '') {
        return false;
    }
    return phoneRegex.test(phone);
}

function validateImage(image) {
    let allowedExtensions = ['jpg', 'jpeg', 'png', 'gif'];
    if (image === '') {
        return false;
    }
    let fileExtension = image.substr(image.lastIndexOf('.') + 1).toLowerCase();
    return allowedExtensions.includes(fileExtension);
}

function validateVideo(video) {
    let allowedExtensions = ['mp4', 'avi', 'mov'];

    if (video === '') {
        return false;
    }
    let fileExtension = video.substr(video.lastIndexOf('.') + 1).toLowerCase();
    return allowedExtensions.includes(fileExtension);
}

function validatePassword(password) {
    if (password === '') {
        return false;
    }
    return password.length >= 6;
}

function validateDOB(dob) {
    if (dob === '') {
        return false;
    }
    let selectedDate = new Date(dob);
    let currentDate = new Date();
    return selectedDate <= currentDate;
}

function validateCode(code) {
    if (code.length !== 6) {
        return false;
    }
    return /^\d+$/.test(code);
}
