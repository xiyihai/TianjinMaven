/**
 * Created by Administrator on 2016/4/15.
 */
define([], function () {

    function BSdelete(cellidArray){
        //把这个数组发送到后台，需要删除这些基站
        $.ajax({
            type:'post',
            url:'deleteBSAction',
            data:{
                cellidArray:cellidArray
            },
            dataType:'json',
            traditional:true
        }).then(function () {
            alert("删除成功！");
        }, function () {
            alert("删除失败！这里是BSdelete.js");
        })
    }


    return{
        BSdelete:BSdelete
    }
});