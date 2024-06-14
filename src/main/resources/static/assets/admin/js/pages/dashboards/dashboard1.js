
function displayListYear(){
    const currentYear = new Date().getFullYear();
    let $selectYear = $('#selectYear');
    $selectYear.append($('<option>', {
        text: currentYear,
        value: currentYear
    }));
    for (let i = 1; i <= 10; i++) {
        $selectYear.append($('<option>', {
            text: currentYear - i,
            value: currentYear - i
        }));
    }
}
function getStatistics(year){
    if(year === null || year === undefined)
        year = new Date().getFullYear();

    $.ajax({
        url: `/api/statistic/${year}`,
        type: 'GET',
        success: function(data) {
            console.log(data);
            if(data.code !== 200) {
                return false;
            }
            data = data.data;
            $("#totalUsers").text(data.totalUsers);
            $("#totalPosts").text(data.totalPosts);
            $("#totalReports").text(data.totalReports);
            const months = Object.keys(data.newUserByMonth);
            const newUsersData = Object.values(data.newUserByMonth);
            const newPostsData = Object.values(data.newPostsByMonth);
            console.log(months, newPostsData, newUsersData);
            // Tạo mảng chứa dữ liệu cho biểu đồ Chartist
            const chartData = {
                labels: months,
                series: [
                    newUsersData,
                    newPostsData
                ]
            };

            // Cấu hình biểu đồ Chartist
            const options = {
                top: 0,
                low: 1,
                showPoint: true,
                fullWidth: true,
                plugins: [
                    Chartist.plugins.tooltip()
                ],
                axisY: {
                    labelInterpolationFnc: function (value) {
                        return Math.round(value);
                    }
                },
                showArea: true
            };

            // Vẽ biểu đồ Chartist
            new Chartist.Line('#ct-visits', chartData, options);
        },
        error: function(xhr, status, error) {
            console.error('Error fetching data:', error);
        }
    });
}
$(function () {
    displayListYear();
    getStatistics(null);


    var chart = [chart];

    var sparklineLogin = function () {
        $('#sparklinedash').sparkline([0, 5, 6, 10, 9, 12, 4, 9], {
            type: 'bar',
            height: '30',
            barWidth: '4',
            resize: true,
            barSpacing: '5',
            barColor: '#7ace4c'
        });
        $('#sparklinedash2').sparkline([0, 5, 6, 10, 9, 12, 4, 9], {
            type: 'bar',
            height: '30',
            barWidth: '4',
            resize: true,
            barSpacing: '5',
            barColor: '#7460ee'
        });
        $('#sparklinedash3').sparkline([0, 5, 6, 10, 9, 12, 4, 9], {
            type: 'bar',
            height: '30',
            barWidth: '4',
            resize: true,
            barSpacing: '5',
            barColor: '#11a0f8'
        });
        $('#sparklinedash4').sparkline([0, 5, 6, 10, 9, 12, 4, 9], {
            type: 'bar',
            height: '30',
            barWidth: '4',
            resize: true,
            barSpacing: '5',
            barColor: '#f33155'
        });
    }
    var sparkResize;
    $(window).on("resize", function (e) {
        clearTimeout(sparkResize);
        sparkResize = setTimeout(sparklineLogin, 500);
    });

    $(window).on("load", function () {
        $("#selectYear").on("change", function(){
            console.log('vv', ($(this).val()));
            getStatistics($(this).val());
        });
    });

    sparklineLogin();
});
