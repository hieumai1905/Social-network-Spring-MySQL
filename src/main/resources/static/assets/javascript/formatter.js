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
