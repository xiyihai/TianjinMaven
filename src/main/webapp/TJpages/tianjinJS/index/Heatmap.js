/**
 * Created by Administrator on 2016/3/1.
 */
define([], function () {
    var heatmapOverlay;

    function getHM(){
        return heatmapOverlay;
    }

    //开启热力图
    function openHeatmap(map){
        //$.getJSON('tianjinGEOJSON/hainanBS.json')

        $.ajax({
            type:'get',
            url:'getBSDataAction',
            data:null,
            dataType:'json'
        }).then(function (data) {
            var BSjson = $.parseJSON(data);
            drawHM(BSjson);
        }, function () {
            alert("系统异常，请稍后重试！这里是heatmap.js");
        });

        function drawHM(BSjson){
            var points = [];

            var pointsdata = BSjson;
            pointsdata.forEach(function (d) {
                var heatpoint = {
                    "lng": d.coordinates[0],
                    "lat": d.coordinates[1],
                    "count": d.value
                };
                points.push(heatpoint);
            });

            heatmapOverlay = new BMapLib.HeatmapOverlay({"radius":20, "visible":true});
            map.addOverlay(heatmapOverlay);
            heatmapOverlay.setDataSet({data:points,max:5000});
        }
    }
    return {
        openHeatmap:openHeatmap,
        getHM:getHM
    }
});
