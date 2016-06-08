/**
 * Created by Administrator on 2016/3/12.
 */
define([], function () {

    //设为全局变量，方便修改option
    var BMapExt;

    var mapecharts;

    //同上，主要是修改option.series.data中in or out
    var option;

    //为了排名使用
    var IOflag = 'out';

    //为了点击迁入迁出，能够重新排名
    var selectedId;


    //时间转换为字符串中间没有符号
    function dateToStr(datetime){
        var year = datetime.getFullYear();
        var month = datetime.getMonth()+1;//js从0开始取
        var date = datetime.getDate();
        var hour = datetime.getHours();
        var minute = datetime.getMinutes(); //分

        if(month<10){
            month = "0" + month;
        }
        if(date<10){
            date = "0" + date;
        }

        return year+month+date+hour+minute;
    }

    //判断迁入还是迁出
    function setOutOrIn(num,getSeries,range){
        if (num === 1){
            IOflag = 'out';
            //setOption默认是合并处理！！！！！！！！！！！，放这里要达到的效果是覆盖
            option.series = getSeries('out');
            BMapExt.setOption(option,{'notMerger':true});
            //更改排名
            if (typeof (selectedId) != 'undefined'){
                range(selectedId,IOflag);
            }
        }else if(num === 2){
            IOflag = 'in';
            option.series = getSeries('in');
            BMapExt.setOption(option,{'notMerger':true});
            //更改排名
            if (typeof (selectedId) != 'undefined'){
                range(selectedId,IOflag);
            }
        }
    }

    function initMigrate(countyNameArray,nameIdMap,getSeries,range){
        //画图
        require.config({
            //配置echarts.js路径
            paths: {
                echarts: '../../js/echarts/build/dist'
            },
            //这个包就是用来配置所用到的js文件路径，给其一个name
            packages: [
                {
                    name: 'BMap',
                    location: '../../js/echarts/extension/BMap/src',
                    main: 'main'
                },
                {
                    name:'config',
                    location:'../../js/echarts/src/',
                    main:'config'
                }
            ]
        });
        //需要用到的包，里面的name都是上面自己定义的
        require([
            'echarts',
            'BMap',
            'config',
            'echarts/chart/map'
        ], function (echarts, BMapExtension,config) {  //这个回调函数参数就一一对应上面js的对象，也可以采用require('BMap','config')效果相同，获取到对象

            // 初始化地图
            BMapExt = new BMapExtension($('#mapCanvas')[0], BMap, echarts,{
                enableMapClick: false
            });
            var map = BMapExt.getMap();

            var startPoint = {
                x:117.4988,
                y:39.1497
            };
            var point = new BMap.Point(startPoint.x, startPoint.y);
            map.centerAndZoom(point, 10);
            map.enableScrollWheelZoom(true);
            map.addControl(new BMap.ScaleControl()); //添加比例尺控件
            map.enableKeyboard();//启动键盘操作地图

            //地图上添加标注

            // 地图自定义样式
            map.setMapStyle({
                styleJson: [
                    {
                        "featureType": "water",
                        "elementType": "all",
                        "stylers": {
                            "color": "#044161"
                        }
                    },
                    {
                        "featureType": "land",
                        "elementType": "all",
                        "stylers": {
                            "color": "#004981"
                        }
                    },
                    {
                        "featureType": "boundary",
                        "elementType": "geometry",
                        "stylers": {
                            "color": "#064f85"
                        }
                    },
                    {
                        "featureType": "railway",
                        "elementType": "all",
                        "stylers": {
                            "visibility": "off"
                        }
                    },
                    {
                        "featureType": "highway",
                        "elementType": "geometry",
                        "stylers": {
                            "color": "#004981"
                        }
                    },
                    {
                        "featureType": "highway",
                        "elementType": "geometry.fill",
                        "stylers": {
                            "color": "#005b96",
                            "lightness": 1
                        }
                    },
                    {
                        "featureType": "highway",
                        "elementType": "labels",
                        "stylers": {
                            "visibility": "off"
                        }
                    },
                    {
                        "featureType": "arterial",
                        "elementType": "geometry",
                        "stylers": {
                            "color": "#004981"
                        }
                    },
                    {
                        "featureType": "arterial",
                        "elementType": "geometry.fill",
                        "stylers": {
                            "color": "#00508b"
                        }
                    },
                    {
                        "featureType": "poi",
                        "elementType": "all",
                        "stylers": {
                            "visibility": "off"
                        }
                    },
                    {
                        "featureType": "green",
                        "elementType": "all",
                        "stylers": {
                            "color": "#056197",
                            "visibility": "off"
                        }
                    },
                    {
                        "featureType": "subway",
                        "elementType": "all",
                        "stylers": {
                            "visibility": "off"
                        }
                    },
                    {
                        "featureType": "manmade",
                        "elementType": "all",
                        "stylers": {
                            "visibility": "off"
                        }
                    },
                    {
                        "featureType": "local",
                        "elementType": "all",
                        "stylers": {
                            "visibility": "off"
                        }
                    },
                    {
                        "featureType": "arterial",
                        "elementType": "labels",
                        "stylers": {
                            "visibility": "off"
                        }
                    },
                    {
                        "featureType": "boundary",
                        "elementType": "geometry.fill",
                        "stylers": {
                            "color": "#029fd4"
                        }
                    },
                    {
                        "featureType": "building",
                        "elementType": "all",
                        "stylers": {
                            "color": "#1a5787"
                        }
                    },

                ]
            });

            option = {
                color: ['gold','aqua','lime'],
                title : {
                    text: '天津市人口迁徙',
                    subtext:'数据来自移动信号基站',
                    x:'center',
                    textStyle : {
                        color: '#fff'
                    }
                },
                //提示框
                tooltip : {
                    trigger: 'item',
                    formatter: function (v) {
                        return v[1].replace(':', '->');
                    }
                },
                //图例
                legend: {
                    orient: 'vertical',
                    x:'left',
                    data: countyNameArray,
                    selectedMode: 'single',
                    selected:{}, //默认选中状态
                    textStyle : {
                        color: 'auto'
                    }
                },
                //工具箱
                toolbox: {
                    show : true,
                    orient : 'vertical',
                    x: 'right',
                    y: 'center',
                    feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        restore : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                //值域范围
                dataRange: {
                    min : 0,
                    max : 1500,
                    x: 'right',
                    calculable : true,
                    color: ['#ff3333', 'orange', 'yellow','lime','aqua'],
                    textStyle:{
                        color:'#fff'
                    }
                },
                //驱动数据表内容的数组，数组每一项是一个系列的选项和数据
                series : getSeries('out')
            };



            var container = BMapExt.getEchartsContainer();
            mapecharts = BMapExt.initECharts(container);
            BMapExt.setOption(option);

            //添加图例点击事件
            mapecharts.on(config.EVENT.LEGEND_SELECTED, function (param) {
                selectedId = nameIdMap[param.target];
                range(selectedId,IOflag);
            });

        });
    }

    return {
        initMIG:initMigrate,
        dateToStr:dateToStr,
        setOutOrIn:setOutOrIn
    }

});




