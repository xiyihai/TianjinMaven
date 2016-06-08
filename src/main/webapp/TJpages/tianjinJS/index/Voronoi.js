/**
 * Created by Administrator on 2016/2/26.
 */
define([], function () {

    //定义全局画布
    var svgoverlay;
    var overlay;

    //可以通过这个进行类似flag判断，是否开启了基站图层
    function getoverlay(){
        return overlay;
    }

    function destoryoverlay(){
        overlay = undefined;
    }

    function getsvgoverlay(){
        return svgoverlay;
    }

    function openVoronoi(map,BMapProjection,getScreenPoint,d3DrawShape,compute,linear) {

        //$.getJSON('hainanGEOJSON/hainanBS.json',function (BSjson) {
        //    drawBS(BSjson);
        //});
        $.ajax({
            type:'get',
            url:'getBSDataAction',
            data:null,
            dataType:'json',
            success: function (data) {
                var BSjson = $.parseJSON(data);
                drawBS(BSjson);
            },
            error: function () {
                alert("系统异常，请稍后重试！这里是Voronoi.js");
            }
        });

        function drawBS(BSjson) {
            //定义出覆盖物
            overlay = new BMap.Overlay();

            //定义初始化属性
            overlay.initialize = function () {
                var div = document.createElement("div");
                var layer = d3.select(div).attr("class", "SvgOverlay");
                svgoverlay = layer.append("svg").append("g");
                //div添加到图层
                map.getPanes().markerPane.appendChild(div);
                return div;
            };

            //编写draw函数
            overlay.draw = function () {
                //获取geojson中数组
                var pointdata = BSjson;
                //声明一个数组放置转换后的像素
                var positions = [];

                //声明一个数组放置enbid字符串
                var enBidrnc_ids = [];

                //方便着色,里面放置权值
                var value = [];

                //获取geojson中经纬度，并转换,顺便获取enBid数组，然后就可以通过enBid号获取到对应聚合基站信息(多个)
                pointdata.forEach(function (d) {
                    positions.push(BMapProjection(d.coordinates));

                    enBidrnc_ids.push(d.enBidrnc_id);
                    //取出权值放入数组中
                    value.push(d.value);
                });

                //d3把离散点坐标转传入，得到多边形点集，并且匡注范围
                var voronoi = d3.geom.voronoi()
                    .clipExtent([[4000 + getScreenPoint().sw.x, 4000 + getScreenPoint().ne.y], [4000 + getScreenPoint().ne.x, 4000 + getScreenPoint().sw.y]]);
                //像素多边形数组，直接传给画图函数,传入点有多少个就会有多少个多边形数组,且第几个点对应第几个多边形是一致的
                //如果有任何顶点重合或具有NaN的位置，这个方法的行为是undefined
                // 最有可能的，将返回无效的多边形！你应该在计算曲面细分之前过滤无效的顶点，合并重合的顶点。
                var polygons = voronoi(positions);

                //绘制path
                var path = {
                    "d": function (d, i) {
                        return "M" + d.join("L") + "Z"
                    },
                    "stroke": "#FF7744",
                    "stroke-width": 2,
                    "fill": function (d,i) {
                        return  compute(linear(value[i]));
                    },
                    "fill-opacity":'0.3'
                };
                d3DrawShape(svgoverlay,"path", path, polygons,enBidrnc_ids,value);

                //绘制circle
                var circle = {
                    "transform": function (d) {
                        return "translate(" + d + ")";
                    },
                    "r": 2,
                    "fill": "red"
                };
                d3DrawShape(svgoverlay,"circle", circle, positions);
            };
            //添加覆盖物
            map.addOverlay(overlay);
        }
    }

    return {
        getoverlay:getoverlay,
        getsvgoverlay:getsvgoverlay,
        destoryoverlay:destoryoverlay,
        openVoronoi:openVoronoi
    }
});

