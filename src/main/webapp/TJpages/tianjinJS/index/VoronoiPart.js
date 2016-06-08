/**
 * Created by Administrator on 2016/3/28.
 */
define([], function () {

    //为了方便在这个函数中使用参数，所以直接声明变量取值
    var map;
    var compute;
    var linear;
    var baseInfoEvent;

    function drawBS(bsinfo){

        var points = [];
        if (bsinfo.length > 0){
            bsinfo.forEach(function (d,i) {
                //这里采用BMap的多边形画图，不用d3勾画
                //用来放置多边形的点
                var ppoints = [];
                //获取json数据中的二维数组,并把里面的多边形点全部取出来
                var polygonArray = d.polygon_p;
                polygonArray.forEach(function (dd,i) {
                    var point = new BMap.Point(dd[0],dd[1]);
                    ppoints.push(point);
                });

                //绘制基站多边形覆盖物
                var polygonMarker = new BMap.Polygon(ppoints,
                    {   strokeWeight:2,
                        strokeColor:"#FF7744",
                        fillOpacity:0.3,
                        fillColor:compute(linear(d.value))
                    });
                //多边形还要添加鼠标点击事件，这里函数应该和d3中实现方法一样
                polygonMarker.addEventListener("mouseover",function () {

                    this.setFillColor("#0000AA");
                    baseInfoEvent(d.enBidrnc_id, d.value);
                });
                polygonMarker.addEventListener("mouseout",function() {

                    this.setFillColor(compute(linear(d.value)));  //恢复原来的颜色，只能重新上色

                });

                map.addOverlay(polygonMarker);

                //绘制基站点point
                points.push(new BMap.Point(d.coordinates[0], d.coordinates[1]));
            });
            var pointCollection = new BMap.PointCollection(points,{color:"red",size:2});
            map.addOverlay(pointCollection);

        }
    }

    //发送屏幕范围内
    function postData(){
        var sw = map.getBounds().getSouthWest();
        var ne = map.getBounds().getNorthEast();

        var minlng = sw.lng;var minlat = sw.lat;
        var maxlng = ne.lng;var maxlat = ne.lat;

        $.ajax({
            url:'getPartBSDataAction',
            type:'post',
            datatype:'json',
            data:{
                minlng:minlng,
                maxlng:maxlng,
                minlat:minlat,
                maxlat:maxlat
            }
        }).then(function (data) {
            var bsinfo = $.parseJSON(data);
            drawBS(bsinfo);
        }, function () {
            alert("系统异常，请稍后重试！这里是VoronoiPart.js");
        })
    }


    function addVoronoiEvent(){
        //先要清除地图上已有的覆盖物，才能重新画出局部泰森多边形
        map.clearOverlays();
        postData();
    }

    function openVoronoiPart(_map,_compute,_linear,_baseInfoEvent){

        map = _map;
        compute = _compute;
        linear = _linear;
        baseInfoEvent = _baseInfoEvent;
        if ($("#voronoiPart").text() === "局部基站图层"){

            postData();
            //画完之后应该改变按钮文字
            $("#voronoiPart").text("关闭局部基站图层");

            //地图级别改变时重新画图,添加事件不能加(),所以要是带参数则不能这么写
            map.addEventListener("zoomend", addVoronoiEvent);
            //地图拖拽时重新画图
            map.addEventListener("dragend", addVoronoiEvent);

        }else {
            if ($("#voronoiPart").text() === "关闭局部基站图层"){
                map.clearOverlays();
                //消除map上画局部泰森多边形的事件,消除事件需要有指向之前加添事件的函数名，不能带参数
                map.removeEventListener("zoomend",addVoronoiEvent);
                map.removeEventListener("dragend",addVoronoiEvent);

                $("#voronoiPart").text("局部基站图层");
            }
        }
    }

    //这个函数专门给历史数据查询模块画泰森多边形服务
    function historyVoronoi(_map,_compute,_linear,_baseInfoEvent,time){

        map = _map;
        compute = _compute;
        linear = _linear;
        baseInfoEvent = _baseInfoEvent;

        //显示一个gif图
        $("#target").append("<img class='images' style='width: 200px;height: 200px;' src='images/loading-gif/default.gif'/>");

        $.ajax({
            type:'post',
            url:'getTimestampAction',
            data:{
               time:time
            },
            dataType:'json'
        }).then(function (data) {

            if (data === null){
                alert("查询数据不存在！这里是VoronoiPart.js");
            }else {
                var BSjson = $.parseJSON(data);
                drawBS(BSjson);
            }
            $("#target").children().remove();

        }, function () {

        	alert("系统异常，请稍后重试！这里是VoronoiPart.js");
            $("#target").children().remove();

        });

    }


    function openVoronoi(_map,_compute,_linear,_baseInfoEvent) {

        map = _map;
        compute = _compute;
        linear = _linear;
        baseInfoEvent = _baseInfoEvent;

        $.ajax({
            type: 'get',
            url: 'getBSDataAction',
            data: null,
            dataType: 'json',
            success: function (data) {
                var BSjson = $.parseJSON(data);
                drawBS(BSjson);
            },
            error: function () {
                alert("系统异常，请稍后重试！这里是VoronoiPart.js");
            }
        });
    }

    return {
        openVoronoiPart:openVoronoiPart,
        historyVoronoi:historyVoronoi,
        openVoronoi:openVoronoi
    };
});

