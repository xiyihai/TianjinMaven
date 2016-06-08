/**
 * Created by Administrator on 2016/3/23.
 */
define([], function () {
   function initSelect2(){

       $("#S2Search").select2({
           placeholder: "Search...",
           allowClear: true, //允许用户删除之前的选项
           maximumInputLength: 15,//用户最大输入长度，
           minimumInputLength: 2,
           closeOnSelect: false,//选择之后下拉框不会关闭
           ajax:{
               url:'select2CellAction',
               type:'post',
               dataType: 'json',
               delay: 250,  //每当用户按下键过多久之后发送ajax请求
               data: function (params) {  //这里的参数自动包含你的输入
                   return {
                       cellName: params.term
                   };
               },
               processResults: function (data) { //这里处理返回结果形式[{id:'cc',text:'dd'},{},{}]
                   return {
                       results: $.parseJSON(data)
                   }
               }

           }
       });
    }

   function beginS2(drawBS,map,compute,linear,initDigraph){

       $.ajax({
           type:'post',
           url:'S2ByNamesArrayAction',
           data:{
               cellNames:$("#S2Search").val()
           },
           dataType:'json',
           traditional: true
       }).then(function (data) {
           drawBS($.parseJSON(data),map,compute,linear,initDigraph);
       })
   }

    return {
        initSelect2:initSelect2,
        beginS2:beginS2
    }
});