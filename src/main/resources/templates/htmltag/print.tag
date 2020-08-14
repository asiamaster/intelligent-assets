<script>

function te(){
	let url =   	
	print(orderCode,url);	
}

function print(url,temp){
	
      if(typeof callbackObj != 'undefined'){
          window.printFinish=function(){
          }
          var paramStr ="";
          var data=loadPrintData(url);
          debugger;
          if(!data){
              return;
          }
          paramStr = JSON.stringify(data);
          
          console.log("打印信息--:"+paramStr);
          if(paramStr==""){
              return;
          }
          //callbackObj.printDirect(paramStr,"PassCheckPaidDocument");
          callbackObj.printDirect(paramStr,temp);
      }else{
          bs4pop.alert("请检查打印的设备是否已连接", { type: "error" });
      }
  }
  // 加载打印数据
  function loadPrintData(url){
      console.log("调用打印信息："+orderCode);
      var result;
      $.ajax({
  		type: "POST",
  		url: url,
  		success: function(ret) {
              if(!ret.success){
                  bs4pop.alert(ret.message, {type: 'error'});
              }else{
              	return ret.data;
              }
          },
  		error: function(error) {
  			bui.loading.hide();
  			bs4pop.alert('远程访问失败', {
  				type: 'error'
  			});
  		}
  	});
</script>