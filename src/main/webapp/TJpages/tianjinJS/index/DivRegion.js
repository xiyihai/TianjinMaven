/**
 * Created by Administrator on 2016/2/27.
 */
define([], function () {

    //画图工具
    var myDrawingManagerObject;
    //画的图形，用来更加规范保存数据
    var divRegion;
    //用数组存添加在屏幕的覆盖物
    var divOverlay = [];
    //判断添加成功与否的逻辑标志
    var flag = 0;

    //根据画出的图形，得到相应数据
    function initDraw(map){

        divRegion = {};

        //获取模态框中数值，并且隐藏模态框
        var modal = $("#drawModal");
        divRegion.name = modal.find('.modal-body #nameForRegion').val();
        modal.modal('hide');

        myDrawingManagerObject= new BMapLib.DrawingManager(map,
            {
                isOpen: true,
                enableDrawingTool: true,
                enableCalculate: true,
                drawingToolOptions: {
                    anchor: BMAP_ANCHOR_TOP_RIGHT,
                    offset: new BMap.Size(5, 5),
                    scale: 0.7,
                    drawingModes: [BMAP_DRAWING_MARKER, BMAP_DRAWING_POLYGON]
                }
            }
        );

        //完成一个覆盖物添加后的回调函数
        myDrawingManagerObject.addEventListener('overlaycomplete', function (e) {
            if(e.drawingMode == BMAP_DRAWING_MARKER){
                flag = flag+1;
                divRegion.longitude = e.overlay.getPosition().lng;
                divRegion.latitude = e.overlay.getPosition().lat;
            }else if (e.drawingMode == BMAP_DRAWING_POLYGON){
                flag = flag+4;
                divRegion.area = e.calculate;
                divRegion.points = e.overlay.getPath();
                e.label.setStyle({color : "red", "border":"0"});
                divOverlay.push(e.label);
                if (e.calculate==null){
                    flag=10;
                }
            }
            divOverlay.push(e.overlay);
        });
    }

    //当点击模态框的确定按钮触发
    function openDraw(map){
        cancelDivRegion(map);
        initDraw(map);
        myDrawingManagerObject.open();
    }

    //保存数据
    function saveDivRegion(){
        //这里需要制作json数据传给后台
        if (divOverlay.length == 3 && flag == 5){
            //console.log(divRegion);
            //这里需要把points封装成一个二维数组[[110,19],[112,45]...]，方便后台解析成数据库中需要的字符串
        	var points = [];
            divRegion.points.forEach(function (d) {
                var point = [];
                point.push(d.lng);
                point.push(d.lat);
                points.push(point);
            });
            $.ajax({
                type:'post',
                url:'setDivRegionAction',
                data:{
                	name:divRegion.name,
                    area:divRegion.area,
                    longitude:divRegion.longitude,
                    latitude:divRegion.latitude,
                    points:JSON.stringify(points)
                },
                dataType:'json',
                success: function () {
                    alert("添加成功！");
                },
                error: function () {
                    alert("添加失败，请稍后再试！这里是DivRegion.js");
                }
            });
        }else {
            alert("请安规范操作！");
        }
    }

    //取消数据
    function cancelDivRegion(map){
        for (var i=0;i<divOverlay.length;i++){
            map.removeOverlay(divOverlay[i]);
        }
        //这里需要将flag和divOverlay数组清空，否则影响下一次画图
        flag = 0;
        divOverlay = [];
    }

    return {
        openDraw:openDraw,
        saveDivRegion:saveDivRegion,
        cancelDivRegion:cancelDivRegion
    }

});