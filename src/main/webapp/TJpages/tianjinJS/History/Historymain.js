/**
 * Created by Administrator on 2016/2/26.
 */

require.config({
    paths:{
        'SVG':'../index/SVGfunction',
        'VP':"../index/VoronoiPart"
    }
});

require(['SVG','VP'], function (SVG,VP) {

    //用来确定时间戳范围，这里不能选择一个是因为 和数据库中时间可能不一致
    //数据库精确到秒，但这里只需要分钟
	//现在改进成 = 即可
    var time;

    var map = SVG.initializeBMap(new BMap.Point(117.1988,39.1497),16,150);

    //显示日期表
    $("#datetimepicker").datetimepicker({
        inline:true,
        step:1,
        onSelectTime: function (t) {
            dateToStr(t);
        }
    });

    //当点击时调用VoronoiPart.js中函数把时间戳发送过去，然后画出泰森多边形
    $("#voronoiAll").bind("click", function () {
        if (time == undefined){
            alert("请选择时间!");
        }else {

            VP.historyVoronoi(map,SVG.compute,SVG.linear,SVG.baseInfoEvent,time);

        }
    });

    //为按钮绑定清除所有覆盖物功能
    $("#clearMap").bind("click", function () {
        SVG.clearMap();
    });

    //时间转换为字符串中间没有符号
    function dateToStr(datetime){
        var year = datetime.getFullYear();
        var month = datetime.getMonth()+1;//js从0开始取
        var date = datetime.getDate();
        var hour = datetime.getHours();
        var minute = datetime.getMinutes(); //分

        if(month<10){
            month = "0" + month;
        }
        if(date<10){
            date = "0" + date;
        }
        if(minute<10){
            minute = "0" + minute;
        }
        if(hour<10){
            hour = "0" + hour;
        }

        //2016-04-23 21:00
        time =  year+"-"+month+"-"+date+" "+hour+":"+minute;
    }
});