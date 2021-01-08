<script type="text/javascript">

var city = new Vue({
    el: '.el-city',
    data() {
        return {
        	options:[],
        	value :[],
        	originName :"",
        	optionProps: {
        		value: 'id',
        		label: 'name',
        		children: 'childs',
        		checkStrictly: true
        	}
        	
        }
    },
    mounted:function(){
    	this.getCity();
    },
    methods: {
    	 handleChange(value) {
  			var pathvalue = this.$refs.cascaderAddr.getCheckedNodes()[0];
  			this.originName="";
  			if(pathvalue.parent != null){
  				if(pathvalue.parent.parent != null){
  	  				this.originName = this.originName+pathvalue.parent.parent.label+"/"
  	  			}
  				this.originName = this.originName+pathvalue.parent.label+"/"
  			}
  			this.originName = this.originName+pathvalue.label
            this.value = value;
         },
          getCity(){
            let that = this;
              $.ajax({
                  type: "GET",
                  url: '${contextPath}/city/tree.action',
                  success: function (ret) {
                	  that.options = ret.data.childs;
                	  //销地城市
                	  if('${stockIn.origin!}' != ''){
                		  let city2 = '${stockIn.origin!}'.split(',');
                    	  let c2 = [];
                    	  for (var i = 0; i < city2.length; i++) {
                    		  c2.push(parseInt(city2[i]));
                    	  }
                    	  that.value = c2;  
                	  }
                  },
                  error: function (error) {
                     
                  }
              });
          
          }
    },

})

</script>