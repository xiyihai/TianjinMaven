/**
 * Created by Administrator on 2016/4/2.
 */
$(document).ready(function () {

    //用来判断是否通过了校验
    var flag = [];

    //这样使得只校验，不提交表单
    $.validator.setDefaults({
        debug: true
    });

    //自定义验证point的方法
    $.validator.addMethod("isPoint", function (value,element) {
        var tel = /^\d{2,3}\.\d{2,8},\d{1,2}\.\d{2,8}$/;
        return tel.test(value) || this.optional(element);
        },
        "Please enter a point like 110.3451,20.3345."
    );

    //自定义验证type/region的方法
    $.validator.addMethod("istypeRegion", function (value,element) {
            var tel = /^室(内|外)\/[\u4E00-\u9FA5\uF900-\uFA2D]+$/;
            return tel.test(value) || this.optional(element);
        },
        "Please input the type/region like 室内(外)/高铁."
    );


    $("#cellid").validate({
        rules: {
            add_cellid: {
                required: true,
                digits: true,
                rangelength: [9, 10]
            }
        },
        success: function () {
            flag[0] = 1;
        }
    });
    $("#enBidrnc_id").validate({
        rules:{
            add_enBidrnc_id:{
                required:true,
                digits:true,
                rangelength:[6,7]
            }
        },
        success: function () {
            flag[1] = 1;
        }
    });
    $("#name").validate({
        rules:{
            add_name:{
                required:true,
                maxlength:20
            }
        },
        success: function () {
            flag[2] = 1;
        }
    });
    $("#county").validate({
        rules:{
            add_county:{
                required:true,
                rangelength:[3,3]
            }
        },
        success: function () {
            flag[3] = 1;
        }
    });
    $("#typeRegion").validate({
        rules:{
           add_typeRegion:{
               required:true,
               istypeRegion:true
           }
        },
        success: function () {
            flag[4]=1;
        }
    });
    $("#point").validate({
        rules:{
            add_point:{
                required:true,
                isPoint:true
            }
        },
        success: function () {
            flag[5] = 1;
        }
    });


    $("#addBS").bind("click", function () {
        var num = 0;
        flag.forEach(function(d,i){
            if (d===1){
                num++;
            }
        });

        //通过判断警告标签是否出现，结合之前必须通过标签审查，两者协作
        var label = 0;
        $("label").each(function () {
           if ($(this).html() != ""){
                label=1;
           }
        });

        if (num === 6 && label === 0){
            addBS();
        }else {
            alert("请按规范操作!");
        }
    });
});

//定义点击保存按钮后的函数
function addBS(){

    //把经纬度解析出来
    var coordinate = $("#add_point").val().split(',');
    //把type和region解析出来
    var typeRegion = $("#add_typeRegion").val().split('/');

    var bs = new BaseStation($("#add_cellid").val(),$("#add_enBidrnc_id").val(),$("#add_name").val(),typeRegion[0],typeRegion[1],
    $("#add_county").val(),coordinate[0],coordinate[1]);

    //把bs信息发送到后台,这里把整个对象发送给后台
    $.ajax({
        type:'post',
        url:'AddBSAction',
        data:{
            bs:JSON.stringify(bs)
        },
        dataType:'json'
    }).then(function () {
        alert("添加成功！");
    }, function () {
        alert("添加失败，这里是AddBS.js");
    })

}

//定义一个对象用来保存数据，方便看、
function BaseStation(cellid,enBidrnc_id,name,covertype,coverregion,countyName,lng,lat){
    this._cellid = cellid;
    this._name = name;
    this._covertype = covertype;
    this._coverregion = coverregion;
    this._countyName = countyName;
    this._enBidrnc_id = enBidrnc_id;
    this._lng=lng;
    this._lat=lat;
}