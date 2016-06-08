/**
 * Created by Administrator on 2016/3/6.
 */
define([], function () {

    var cellidArray;

    //解决异步问题典型方法，不能直接返回变量，否则外部永远取到的是underfined
    //利用函数的话外部才会实时更新
    function getCellidArray(){
        return cellidArray;
    }

    function drawBS(data,map,compute,linear,initDigraph){

        var bsjson = data;
        var points = bsjson.BSinfos;
        //获取cellidArray
        cellidArray = bsjson.cellidArray;
        if (points.length > 0){
            points.forEach(function (d,i) {
                //这里采用BMap的多边形画图，不用d3勾画
                //用来放置多边形的点
                var ppoints = [];
                //获取json数据中的二维数组,并把里面的多边形点全部取出来
                var polygonArray = d.polygon_p;
                polygonArray.forEach(function (dd,i) {
                    var point = new BMap.Point(dd[0],dd[1]);
                    ppoints.push(point);
                });

                //绘制多边形覆盖物
                var polygonMarker = new BMap.Polygon(ppoints,
                    {   strokeWeight:2,
                        strokeColor:"#FF7744",
                        fillOpacity:0.3,
                        fillColor:compute(linear(d.value))
                    });

                var marker = new BMap.Marker(new BMap.Point(d.longitude, d.latitude));


                //应该在这个marker上添加一个事件，当点击时，出现人口预测的图
                //当然这里判断一下initDigraph需不需要
                if (initDigraph != undefined){
                    marker.addEventListener('click', function () {
                        initDigraph(d.value);
                    });
                }


                var label = new BMap.Label(d.name+" (当前人数："+d.value+") 相关基站搜索共"+points.length+"个",{offset:new BMap.Size(20,-10)});
                label.setStyle({color : "red",border:0,fontSize:"14px"});
                marker.setLabel(label);
                map.addOverlay(marker);
                map.addOverlay(polygonMarker);

            });
            map.centerAndZoom(new BMap.Point(points[0].longitude, points[0].latitude),16);
        }
    }


    function BSsearch(map,compute,linear,initDigraph){
        //获取模态框数值
        var modal = $("#drawModalS");
        var cellId = modal.find('.modal-body #cellIdS').val();
        var cellName = modal.find('.modal-body #cellNameS').val();
        modal.modal('hide');

        //把cellId,cellName传给后台

        //根据返回的json数据,在百度地图上添加marker
        //$.getJSON("tianjinGEOJSON/SearchBS.json")
        $.ajax({
            type:'post',
            url:'searchBSAction',
            data:{
                cellId:cellId,
                cellName:cellName
            },
            dataType:'json'
        }).then(function (data) {
                drawBS($.parseJSON(data),map,compute,linear,initDigraph);
            })
    }

    return {
        BSsearch:BSsearch,
        drawBS:drawBS,
        getCellidArray:getCellidArray
    }
});
