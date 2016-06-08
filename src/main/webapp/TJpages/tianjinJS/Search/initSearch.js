/**
 * Created by Administrator on 2016/3/31.
 */

require.config({
    paths:{
        'SVG':'../index/SVGfunction',
        'VP':'../index/VoronoiPart',
        'S':'BSsearch',
        'S2':'Select2',
        'D':'BSdelete',
        'F':'forecast'
    }
});

require(['SVG','VP','S','S2','D','F'], function (SVG,VP,S,S2, D,F) {

    var map = SVG.initializeBMap(new BMap.Point(117.1988,39.1497),16,300);

    $("#clearMap").bind("click", function () {
        SVG.clearMap();
    });

    //基站搜索
    $("#BSsearch").bind("click", function () {
        //弹出模态框提示
        $("#drawModalS").modal();
    });
    //当模态框点击确定时
    $("#beginSearch").bind("click", function () {
        S.BSsearch(map,SVG.compute,SVG.linear, F.initDigraph);
    });

    //为基站搜索框添加效果
    S2.initSelect2();

    //当点击搜索时，需要把值传给后台操作
    $("#S2Button").bind("click", function () {
        S2.beginS2(S.drawBS,map,SVG.compute,SVG.linear,F.initDigraph);
    });

    //当点击删除基站时，需要把cellidArray数组传递给后台删除
    $("#BSdelete").bind("click", function () {
        D.BSdelete(S.getCellidArray());
    });

    //为开启局部泰森多边形添加事件,这里面一些和全局变量一样的逻辑放在了open函数里
    $("#voronoiPart").bind("click", function () {
        VP.openVoronoiPart(map,SVG.compute,SVG.linear,SVG.baseInfoEvent);
    });
});
