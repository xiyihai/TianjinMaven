/**
 * Created by Administrator on 2016/4/27.
 */
define([], function () {

    var myChart;

    //放置实际值
    var value;

    //保留预测值
    var predict;

    //放置显示的值
    var data = [];

    var oneDay = 24 * 3600 * 1000;
    var now ;

    var timeclick;

    //这边的数据模拟基站发过来的，共1000个。然后同时计算预测值，但保存最新的即可
    //返回的值当然还是实际值，因为已经得到了
    function randomData() {
        now = new Date(+now + oneDay);
        predict = value;
        value = value + Math.random() * 21 - 10;

        predict = Math.round(0.7*value + 0.3*predict);

        return {
            name: now.toString(),
            value: [
                [now.getFullYear(), now.getMonth() + 1, now.getDate()].join('-'),
                Math.round(value)
            ]
        }
    }

    //利用第801个预测值predict，开始下面的计算
    // 不过每次预测的时候需要从后台收到一个最新的值才可以，这里采用模拟生成来实现
    //返回的值是 预测值
    function ESData(){

        now = new Date(+now + oneDay);
        //这个值模拟最新得到的数据
        value = value + Math.random() * 21 - 10;

        predict = Math.round(0.7*value + 0.3*predict);

        return {
            name: now.toString(),
            value: [
                [now.getFullYear(), now.getMonth() + 1, now.getDate()].join('-'),
                Math.round(predict)
            ]
        }
    }

    function initDigraph(_value){
        //初始化图表,初始化之前先要销毁之前的实例，不然不出现叠加情况
        if (myChart != undefined){
            echarts.dispose(myChart);
            //这里必须取消这个，不然会越来越快
            clearInterval(timeclick);
            data = [];
        }

        myChart = echarts.init(document.getElementById("digraph"));

        now = new Date();

        //接受基站传过来的value值，以这个为基准模拟数据
        value = _value;

        //先产生1000个数据当作基站传过来的数据，再再生200个数据作为预测的数值
        for (var i = 0; i < 1000; i++) {
            data.push(randomData());
        }

        option = {
            title: {
                text: '区域范围内人流预测'
            },
            tooltip: {
                trigger: 'axis',  //表示坐标轴触发
                formatter: function (params) {
                    params = params[0];
                    var date = new Date(params.name);
                    return date.getDate() + '/' + (date.getMonth() + 1) + '/' + date.getFullYear() + ' : ' + params.value[1];
                },
                axisPointer: {
                    animation: false
                }
            },
            xAxis: {
                type: 'time', //表示时间刻度
                splitLine: {
                    show: true
                }
            },
            yAxis: {
                type: 'value',
                boundaryGap: [0, '100%'],
                splitLine: {
                    show: true
                }
            },
            series: [{
                name: '模拟数据',
                type: 'line',
                showSymbol: false,
                hoverAnimation: false,
                data: data
            }]
        };

        myChart.setOption(option,{'notMerger':true});

        timeclick = setInterval(function () {

            //这里每次更新的值都是采用预测值显示
            for (var i = 0; i < 5; i++) {
                data.shift();
                data.push(ESData());
            }
            myChart.setOption({
                series: [{
                    data: data
                }]
            });
        }, 1000);
    }


    return {
        initDigraph:initDigraph
    }
});