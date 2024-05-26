function formatTime(time) {
    let currentTime = new Date();
    let timeDiff = currentTime - time;
    let minutesDiff = Math.floor(timeDiff / (1000 * 60));

    if (minutesDiff < 60) {
        if (minutesDiff === 0) {
            return "1m";
        }
        return minutesDiff + "m";
    } else if (minutesDiff < 24 * 60) {
        let hoursDiff = Math.floor(minutesDiff / 60);
        return hoursDiff + "h";
    } else {
        let daysDiff = Math.floor(minutesDiff / (60 * 24));
        return daysDiff + "d";
    }
}

function formatTimeFull(timeString) {
    const time = new Date(timeString);
    const currentTime = new Date();

    const months = [
        'January', 'February', 'March', 'April', 'May', 'June',
        'July', 'August', 'September', 'October', 'November', 'December'
    ];

    const days = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];

    if (time.getMonth() === currentTime.getMonth() && time.getDate() === currentTime.getDate()) {
        const hours = time.getHours();
        const minutes = time.getMinutes();
        const ampm = hours >= 12 ? 'PM' : 'AM';
        const formattedHours = hours % 12 || 12;
        const formattedMinutes = minutes < 10 ? '0' + minutes : minutes;
        return `${formattedHours}:${formattedMinutes} ${ampm}`;
    } else if (time.getMonth() === currentTime.getMonth()) {
        const hours = time.getHours();
        const minutes = time.getMinutes();
        const ampm = hours >= 12 ? 'PM' : 'AM';
        const formattedHours = hours % 12 || 12;
        const formattedMinutes = minutes < 10 ? '0' + minutes : minutes;
        const day = days[time.getDay()];
        return `${day} ${formattedHours}:${formattedMinutes} ${ampm}`;
    } else {
        const month = months[time.getMonth()];
        const day = time.getDate();
        const year = time.getFullYear();
        const hours = time.getHours();
        const minutes = time.getMinutes();
        const ampm = hours >= 12 ? 'PM' : 'AM';
        const formattedHours = hours % 12 || 12;
        const formattedMinutes = minutes < 10 ? '0' + minutes : minutes;
        return `${month} ${day}, ${year} ${formattedHours}:${formattedMinutes} ${ampm}`;
    }
}
