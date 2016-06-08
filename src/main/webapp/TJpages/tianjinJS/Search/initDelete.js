/**
 * Created by Administrator on 2016/3/31.
 */

require.config({
    paths:{
        'SVG':'../index/SVGfunction',
        'S':'BSsearch',
        'S2':'Select2',
        'D':'BSdelete'
    }
});

require(['SVG','S','S2','D'], function (SVG,S,S2,D) {

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
        S.BSsearch(map,SVG.compute,SVG.linear);
    });

    //为基站搜索框添加效果
    S2.initSelect2();

    //当点击搜索时，需要把值传给后台操作
    $("#S2Button").bind("click", function () {
        S2.beginS2(S.drawBS,map,SVG.compute,SVG.linear);
    });

    //点击删除按钮时，先弹出模态框确认
    $("#BSdelete").bind("click", function () {
        $("#BSdeleteModal").modal();
    });

    //当点击删除基站时，需要把cellidArray数组传递给后台删除
    $("#deletesure").bind("click", function () {
        D.BSdelete(S.getCellidArray());
    });

});
