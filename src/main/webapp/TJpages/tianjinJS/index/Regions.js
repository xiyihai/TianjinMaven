/**
 * Created by Administrator on 2016/2/26.
 */
define([], function () {

    //为了复用
    var map;

    //数据库中读取到的景点region保存到这个数组中
    var regionsPolygons = [];

    //用户点击确定时，开始画出景点的区域
    function drawBegin(){
        //先要获取select选择框的value值,即数组中id值
        var id = $("#regionsSelect").find("option:selected").val();
        drawRegion(id);
    }


    //这里的id不同于region中id，这里是已有景点排序号，也就是数组中序号
    function drawRegion(id){
        //覆盖物多边形
        var regionOverlay = new BMap.Polygon(regionsPolygons[id].points, {strokeWeight:4});
        //覆盖物 点
        var marker = new BMap.Marker(regionsPolygons[id].center);

        //显示这个地区的具体信息infowindow
        var content = "<h4 style='margin:0 0 5px 0;padding:0.2em 0'>"+regionsPolygons[id].name+"</h4>"+
            "<div><div><br>面积："+regionsPolygons[id].area+" 平方米"+
            "<br>经纬度坐标："+regionsPolygons[id].center.lng+","+regionsPolygons[id].center.lat+
            "</div><img class='images' style='float:right margin:4px' width='200px' height='120px' src='images/regions/"+regionsPolygons[id].image+"'/></div>";

        var infoWindow = new BMap.InfoWindow(content);
        marker.addEventListener("click",function(){
            this.openInfoWindow(infoWindow);
            //图片加载完毕重绘infowindow
            $(".images").ready(function(){
                infoWindow.redraw();   //防止在网速较慢，图片未加载时，生成的信息框高度比图片的总高度小，导致图片部分被隐藏
            });

        });

        //点上面的标签
        var label = new BMap.Label(regionsPolygons[id].name,{offset:new BMap.Size(20,-10)});
        label.setStyle({color : "red", "border":"0"});
        marker.setLabel(label);

        map.addOverlay(marker);
        map.addOverlay(regionOverlay);

        var pointsArray = regionOverlay.getPath();
        map.setViewport(pointsArray);
    }

    //初始化结点
    function initializeRegion(_map) {

        map = _map;

        //这里再换一种异步方式，下面两个变量是方便操作
        var def = $.Deferred();
        var Regionjson;

        //$.getJSON("tianjinGEOJSON/hainanRegion.json").then(
        //    function (data) {
        //        Regionjson = data;
        //        def.resolve();
        //    });

        $.ajax({
                type:'get',
                url:'getRegionAction',
                data:null,
                dataType:'json',
                success: function (data) {
                    Regionjson = $.parseJSON(data);
                    def.resolve();
                },
                error: function () {
                    alert("系统异常，请稍后重试！这里是Region.js");
                }
        });

        $.when(def).done(function () {
                Regionjson.forEach(function (d) {
                    var region = {};
                    region.id = d.id;
                    region.name = d.name;
                    region.area = d.area;
                    region.center = new BMap.Point(d.longitude, d.latitude);
                    region.image = d.image;

                    //转换成BMap中坐标，放到points数组中
                    var polygondata = d.coordinates;
                    var points = [];
                    polygondata.forEach(function (d) {
                        points.push(new BMap.Point(d[0], d[1]));
                    });

                    region.points = points;

                    regionsPolygons[region.id]=region;
                });

                regionsPolygons.forEach(function (d, i) {

                    if (d.id !== undefined){
                        var context = '<option' +
                            ' value="' + d.id +
                            '" >'+ d.name +'</option>';

                        $("#regionsSelect").append(context);
                    }

                    //这里清除功能交给总按钮，不再一个一个添加事件
                });

            });

        //给确定按钮绑定事件，在地图上画出具体的图
        $("#regionBegin").bind("click", drawBegin);
    }

    return{
        initializeRegion:initializeRegion
    }

});
