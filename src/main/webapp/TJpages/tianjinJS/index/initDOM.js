/**
 * Created by Administrator on 2016/2/26.
 */

require.config({
    paths:{
        'HM':'Heatmap',
        'R':'Regions',
        'DIV':'DivRegion',
        'V':'Voronoi',
        'SVG':'SVGfunction',
        'VP':"VoronoiPart"
    }
});

require(['HM','R','DIV','V','SVG','VP'], function (HM,R,DIV,V,SVG,VP) {

    var map = SVG.initializeBMap(new BMap.Point(117.1988,39.1497),16,150);

    //这里改变策略，全局基站可以开启，但不利用d3.js，直接利用JTS套件
    $("#voronoiAll").bind("click", function () {
        VP.openVoronoi(map,SVG.compute,SVG.linear,SVG.baseInfoEvent);
    });

    //为开启局部泰森多边形添加事件,这里面一些和全局变量一样的逻辑放在了open函数里
    $("#voronoiPart").bind("click", function () {
        VP.openVoronoiPart(map,SVG.compute,SVG.linear,SVG.baseInfoEvent);
    });

    //自定义景区
    $("#divDraw").bind("click", function () {
        //弹出模态框提示
        $("#drawModal").modal();
    });
    $("#DIVdone").bind("click", function () {
        DIV.openDraw(map);
    });
    $("#DIVsave").bind("click", function () {
        DIV.saveDivRegion();
    });
    $("#DIVcancel").bind("click", function () {
        DIV.cancelDivRegion(map);
    });


    //为热力图按钮绑定事件
    $("#heatmap").bind("click",function(){
        HM.openHeatmap(map);
    });

    //初始化显示景区的按钮，根据数据库中确定
    R.initializeRegion(map);

    //为按钮绑定清除所有覆盖物功能
    $("#clearMap").bind("click", function () {
        SVG.clearMap();
        //并且把基站信息显示窗口清楚信息
        $(".blas").remove();
    })

});