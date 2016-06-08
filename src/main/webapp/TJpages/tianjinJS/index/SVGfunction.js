/**
 * Created by Administrator on 2016/2/26.
 */

define([], function () {

    var map;

    //定义经纬度转换成像素坐标函数,凡是传给百度地图函数的点都要是BMap.Point封装过后的，取到的只是数组
    function BMapProjection(coordinates) {
        var BCoordinates = new BMap.Point(coordinates[0], coordinates[1]);
        var pixelCoordinates = map.pointToOverlayPixel(BCoordinates);
        return [pixelCoordinates.x+4000, pixelCoordinates.y+4000];
    }

    //颜色渐变
    var compute = d3.interpolateHsl(d3.rgb(0,255,0).hsl(),d3.rgb(255,0,0).hsl());

    //比例尺
    var linear = d3.scale.linear()
        .domain([0,1000])
        .range([0,1]);


    //这个是鼠标点击泰森多边形产生事件函数，在全局和局部泰森多边形中复用
    function baseInfoEvent(enBidrnc_id,value){

        //通过传入enBidrnc_id字符串值，返回基站包含的cell信息数组
        //解析查找任务交给后台处理，后端返回json数据，里面是匹配到的3个聚合基站信息

        //$.getJSON('tianjinGEOJSON/cellIdInfo.json')

        $.ajax({
            type:'post',
            url:'getCellidInfoAction',
            data:{
                enBidrnc_id:enBidrnc_id
            },
            dataType:'json'
        }).done(function (data){
            var celljson = $.parseJSON(data);
            var bases = [];
            var cells = celljson.cells;
            cells.forEach(function (d) {
                var bs = new BaseStation(d.cellid, d.name, d.covertype, d.coverregion, d.countyname);
                bases.push(bs);
            });

            //显示基站信息,但显示之前需要先把原来的元素清楚
            $(".blas").remove();
            bases.forEach(function (baseInfo) {
                $("#BStable").append('<div class="blas"><li class="wicked">' + baseInfo._cellId +
                        '/' + baseInfo._name +
                        '</li><li class="mullet">' + baseInfo._covertype +
                        '/' + baseInfo._coverregion +
                        '/' + baseInfo._countyName +
                        '</li><li class="see">value</li><li class="com">' + value +
                        '</li>');
            });
        }).fail(function () {
            alert("系统异常，请稍后重试！这里是SVGfunction.js");
        })
    }

    //在g标签中绘制基本图形，dom是图形类别，attr是图形基本属性，
    //这里注意attr写两次，是为了重新画的时候修改原来的线
    function d3DrawShape(svgoverlay,dom, attr, data,enBidrnc_ids,value) {
        svgoverlay.selectAll(dom)
            .data(data)
            .attr(attr)
            .enter()
            .append(dom)
            .attr(attr);
        if(dom == "path"){
            svgoverlay.selectAll(dom)
                .on("click",function(d,i){
                    d3.select(this)
                        .attr("fill","#0000AA");

                    baseInfoEvent(enBidrnc_ids[i],value[i]);

                })
                .on("mouseleave",function(d,i) {
                    d3.select(this)
                        .attr("fill", compute(linear(value[i])));  //恢复原来的颜色，只能重新上色
                });
        }
    }

    //返回屏幕左下角和右上角像素坐标
    function getScreenPoint(){
        return  {
            sw: map.pointToOverlayPixel(map.getBounds().getSouthWest()),
            ne: map.pointToOverlayPixel(map.getBounds().getNorthEast())
        };
    }

    function initializeBMap(center,zoom,height){

        if (typeof(map)=="undefined") {
            //设置地图块元素
            $("#mapCanvas").css("height", $(window).height()-height);

            var tempBMap = new BMap.Map("mapCanvas",{enableMapClick:false});          // 创建地图实例
            tempBMap.centerAndZoom(center, zoom);                 // 初始化地图，设置中心点坐标和地图级别
            tempBMap.addControl(new BMap.NavigationControl()); //添加缩略控件
            tempBMap.addControl(new BMap.ScaleControl()); //添加比例尺控件
            tempBMap.enableScrollWheelZoom();//启动鼠标滚轮缩放地图
            tempBMap.enableKeyboard();//启动键盘操作地图
            tempBMap.disableDoubleClickZoom(); //关闭双击放大事件

            map = tempBMap;
            return tempBMap;
        }else return map;
    }

    //声明一个base基站对象,里面放基站信息
    function BaseStation(cellId,name,covertype,coverregion,countyName){
        this._cellId = cellId;
        this._name = name;
        this._covertype =covertype;
        this._coverregion = coverregion;
        this._countyName =countyName;
    }

    //声明一个ClearMap的函数，用来清除Map上所有覆盖物
    function clearMap(){
        map.clearOverlays();
    }

    return{
        BMapProjection:BMapProjection,
        d3DrawShape:d3DrawShape,
        compute:compute,
        linear:linear,
        initializeBMap:initializeBMap,
        getScreenPoint:getScreenPoint,
        baseInfoEvent:baseInfoEvent,
        clearMap:clearMap
    }

});

