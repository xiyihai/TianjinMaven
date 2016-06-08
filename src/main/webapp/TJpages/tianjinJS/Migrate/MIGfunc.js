/**
 * Created by Administrator on 2016/3/12.
 */
define([], function () {

    //用来放置市地方数组，里面元素和json中一样
    var countyJson = [];

    //放置迁徙人口的二维数组
    var migDoubleArray = [];

    var countyLocationMap= {};

    var countyNameArray = [];

    var nameIdMap = {};

    var setJSONdata = function (countyjson,migratejson) {
        countyJson = countyjson;
        migDoubleArray = migratejson;
    }

    //返回格式：
// {
//    '三沙市':[112.347212,16.842771],
//    '白沙黎族自治县':[109.453521,19.225518],
//    '保亭黎族苗族自治县':[109.720282,18.638998],
//    '昌江黎族自治县':[109.064879,19.301961],
//    '澄迈县':[110.019238,19.74026],
//    '儋州市':[109.59491,19.523509],
//    '定安县':[110.368787,19.687989],
//    '东方市':[108.649749,19.078058],
//    '海口':[110.330802,20.022071],
//    '临高县':[109.699585,19.91437],
//    '陵水黎族自治县':[110.044535,18.50521],
//    '琼海市':[110.487179,19.250706],
//    '琼中黎族苗族自治县':[109.853663,19.03316],
//    '三亚市':[109.522771,18.257776],
//    '屯昌县':[110.111225,19.349993],
//    '万宁市':[110.395193,18.797937],
//    '文昌市':[110.795334,19.538772],
//    '五指山市':[109.51775,18.831306],
//    '乐东黎族自治县':[109.19136,18.755154]
// }
    var getCountyLocationMap = function(){
        var countyMap = {};
        countyJson.forEach(function (d) {
            var name = d.name;
            var point = [];
            point[0] = d.longitude;
            point[1] = d.latitude;
            countyMap[name] = point;
        });
        countyLocationMap = countyMap;
        return countyMap;
    };

    //返回格式: ['海口市','东方市'.....]
    var getCountyNameArray = function (){
        var names = [];
        countyJson.forEach(function (d) {
            names.push(d.name);
        });
        countyNameArray = names;
        return names;
    };


    //返回格式：
    //{
    //    '三沙市':1,
    //    '白沙黎族自治县':2,
    //    '保亭黎族苗族自治县':3,
    //}
    var getNameIdMap = function (){
        var county = {};
        countyNameArray.forEach(function (d,i) {
            county[d]=i;
        });
        nameIdMap = county;
        return county;
    };

    //根据id号确定中心城市，isOut用来判断迁入还是迁出,
    // isPoint判断是否返回point需要的对象，否则是line需要的对象
    var getPath = function (id,isOut,isPoint){
//line格式如下(默认out)，若是in则前后数据换一下
//[
//       [{name: '海口市'}, {name: '定安县', value: 90}],
//       [{name: '海口市'}, {name: '万宁市', value: 80}],
//       [{name: '海口市'}, {name: '昌江黎族自治县', value: 70}],
//       [{name: '海口市'}, {name: '三亚市', value: 60}],
//       [{name: '海口市'}, {name: '澄迈县', value: 50}],
//       [{name: '海口市'}, {name: '临高县', value: 40}],
//       [{name: '海口市'}, {name: '五指山市', value: 30}],
//       [{name: '海口市'}, {name: '屯昌县', value: 20}],
//       [{name: '海口市'}, {name: '乐东黎族自治县', value: 10}]
//]

//point格式如下 out和in一样
//   [
//        {name: '定安县', value: 90},
//        {name: '万宁市', value: 80},
//        {name: '昌江黎族自治县', value: 70},
//        {name: '澄迈县', value: 50},
//        {name: '临高县', value: 40},
//        {name: '五指山市', value: 30},
//        {name: '屯昌县', value: 20},
//        {name: '乐东黎族自治县', value: 10}
//   ]

        if(isPoint != true){
            if(isOut === "out"){
                var migrateO = []; //放置返回的二维数组
                for(var i=0;i < migDoubleArray.length ;i++){
                    //value为0，和本身到本身都不放入
                    if(migDoubleArray[id][i]!==0 && id!==i){
                        var countyOut = {};
                        countyOut.name = countyNameArray[id];

                        var countyIn = {};
                        countyIn.name = countyNameArray[i];
                        countyIn.value = migDoubleArray[id][i];

                        var array = [];
                        array[0] = countyOut;
                        array[1] =countyIn;

                        migrateO.push(array);
                    };
                }
                return migrateO;
            }else {
                if (isOut === "in"){
                    var migrateI = []; //放置返回的二维数组
                    for(var i=0;i < migDoubleArray.length ;i++){
                        //value为0，和本身到本身都不放入
                        if(migDoubleArray[i][id]!==0 && id!==i){
                            var countyIn = {};
                            countyIn.name = countyNameArray[id];

                            var countyOut = {};
                            countyOut.name = countyNameArray[i];
                            countyOut.value = migDoubleArray[i][id];

                            var array = [];
                            array[0] =countyOut;
                            array[1] = countyIn;

                            migrateI.push(array);
                        }
                    }
                    return migrateI;
                }
            }
        }else { //这里返回point
            if (isOut === 'out'){
                var migrateO = []; //放置返回的一维数组
                for(var i=0;i < migDoubleArray.length ;i++){
                    //value为0，和本身到本身都不放入
                    if(migDoubleArray[id][i]!==0 && id!==i){
                        var countyIn = {};
                        countyIn.name = countyNameArray[i];
                        countyIn.value = migDoubleArray[id][i];

                        migrateO.push(countyIn);
                    };
                }
                //返回之前先赋值给range，这样可以排名
                return migrateO;
            }else {
                if (isOut === 'in'){
                    var migrateI = []; //放置返回的一维数组
                    for(var i=0;i < migDoubleArray.length ;i++){
                        //value为0，和本身到本身都不放入
                        if(migDoubleArray[i][id]!==0 && id!==i){
                            var countyOut = {};
                            countyOut.name = countyNameArray[i];
                            countyOut.value = migDoubleArray[i][id];

                            migrateI.push(countyOut);
                        };
                    }
                    return migrateI;
                }
            }
        }
    };

    var getSeries = function (IOflag){
        //数据格式
        //[
        //    {
        //        name: '海口市', //这里name会被legent.data索引相关，就是点击legent就出发这个name
        //        type: 'map',
        //        mapType: 'none',
        //        data: [],
        //        geoCoord: countysMap,  //鼠标移动到对应的经纬度，则显示对应的地方name
        //
        //        //数据标线内容
        //        markLine: {
        //            smooth: true,
        //            effect: {
        //                show: true,
        //                scaleSize: 1,
        //                period: 30,
        //                color: '#fff',
        //                shadowBlur: 10
        //            },
        //            itemStyle: {
        //                normal: {
        //                    borderWidth: 1,
        //                    lineStyle: {
        //                        type: 'solid',
        //                        shadowBlur: 10
        //                    }
        //                }
        //            },
        //            //标线数据，会根据上面的geoCoord数据确定位置
        //            data: getPath(9-1,'out')
        //        },
        //        //数据标注内容
        //        markPoint: {
        //            symbol: 'emptyCircle',
        //            symbolSize: function (v) {
        //                return 10 + v /500
        //            },
        //            effect: {
        //                show: true,
        //                shadowBlur: 0
        //            },
        //            itemStyle: {
        //                normal: {
        //                    label: {show: false}
        //                }
        //            },
        //            data:getPath(9-1,'out',true)
        //        }
        //    }
        //]

        var series = []; //返回的数组
        for(var i=0;i<countyJson.length;i++){
            var temp = {};
            temp.name = countyNameArray[i];
            temp.type = 'map';
            temp.mapType = 'none';
            temp.data =  [];
            temp.geoCoord = countyLocationMap;  //鼠标移动到对应的经纬度，则显示对应的地方name
            temp.markLine =
            {
                smooth: true,
                effect: {
                    show: true,
                    scaleSize: 1,
                    period: 30,
                    color: '#fff',
                    shadowBlur: 10
                },
                itemStyle: {
                    normal: {
                        borderWidth: 1,
                        lineStyle: {
                            type: 'solid',
                            shadowBlur: 10
                        }
                    }
                },
                //标线数据，会根据上面的geoCoord数据确定位置
                data: getPath(i,IOflag,false)
            };
            temp.markPoint =
            {
                symbol: 'emptyCircle',
                symbolSize: function (v) {
                    return 10 + v /200
                },
                effect: {
                    show: true,
                    shadowBlur: 0
                },
                itemStyle: {
                    normal: {
                        label: {show: false}
                    }
                },
                data:getPath(i,IOflag,true)
            };
            series.push(temp);
        }
        return series;
    };


    var range = function (id,IOflag){
        var count = 8;
        var points;
        if (IOflag === 'out'){
            $("#cityLabel").text('迁出 '+countyNameArray[id]+' 人口排行榜');
        }else {
            $("#cityLabel").text('迁入 '+countyNameArray[id]+' 人口排行榜');
        }

        $("#cityList").empty().append('<thead><tr><th>排名</th><th>城市</th><th>人数</th></tr></thead><tbody>');

        points = getPath(id,IOflag,true);

        if(points.length < count){
            count = points.length;
        }
        var orderPoint = quickSort(points,count);

        for (var i=0; i<count ;i++) {
            str = '<tr><td>'+(i+1)+'</td><td>'+orderPoint[i].name+'</td><td>'+orderPoint[i].value+'</td></tr>';
            $("#cityList").append(str);
        }
        $("#cityList").append('</tbody>');
    }

    //针对{name，value}的快速排序，array为传入的数组，num是前多少个
    function quickSort(array,num) {
        var max, k,temp;
        for(var i=0;i<num;i++){
            max = array[i].value;
            k = i;
            for (var j=i;j<array.length;j++){
                if (max < array[j].value){
                    max = array[j].value;
                    k = j;
                }
            }
            temp = array[k];
            array[k] = array[i];
            array[i] = temp;
        }
        return array;
    }

    return {
        getCLMap:getCountyLocationMap,
        getCNArray:getCountyNameArray,
        getNIMap:getNameIdMap,
        getSeries:getSeries,
        range:range,
        setJSONdata:setJSONdata
    }

});