/**
 * Created by Administrator on 2016/3/12.
 */
require.config({
   paths:{
       'echarts':'../../js/echarts/build/dist/echarts',
       'MIGinit':'MIGinit',
       'MIGfunc':'MIGfunc'
   }
});

require(['MIGinit','MIGfunc','echarts'], function (MIGinit,MIGfunc) {

    //用来获取MIGfunc模块中的值，给MIGinit使用
    var countyNameArray;
    var nameIdMap;

    //读取json数据
    $.when(

        //$.getJSON("tianjinGEOJSON/hainanCounty.json"),
        $.ajax({
            type:'get',
            url:'getCountyAction',
            data:null,
            dataType:'json'
        }),
        //这个二维数据是用来初始化页面用的
        $.getJSON("tianjinGEOJSON/tianjinMigrate.json")

    ).done(function (dataCounty,dataMig) {

        //ajax请求回来的是字符串，需要解析，getJSON不需要解析
        //这里需要注意，$.when().done()这里的参数是一个数组(里面包含response.text等信息)，想要获取想要的值[0]
        Countyjson = $.parseJSON(dataCounty[0]);
        Migratejson = dataMig[0];

        //给MIGfunc中的变量赋值
        MIGfunc.setJSONdata(Countyjson.features,Migratejson.values);

    }).fail(function () {
        alert("系统异常，请稍后重试！");
    }).done(function () {
        //给变量赋值
        MIGfunc.getCLMap();
        countyNameArray = MIGfunc.getCNArray();
        nameIdMap = MIGfunc.getNIMap();

    }).done(function () {

        MIGinit.initMIG(countyNameArray,nameIdMap,MIGfunc.getSeries,MIGfunc.range);

        //显示日期表
        $("#datetimepicker").datetimepicker({
            theme:'dark',
            inline:true,
            step:60,
            onSelectTime: function (t) {
                console.log(MIGinit.dateToStr(t));//正常情况下，这里应该根据时间数据，查询这个时间点的二维数组
            }
        });

        //按钮绑定事件
        $("#countyOut").bind("click",function(){
            MIGinit.setOutOrIn(1,MIGfunc.getSeries,MIGfunc.range);
        });
        $("#countyIn").bind("click", function () {
            MIGinit.setOutOrIn(2,MIGfunc.getSeries,MIGfunc.range);
        });
    });
});
