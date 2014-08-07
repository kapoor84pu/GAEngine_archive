angular.module("productsService", ["models"])
.service(
	"productsService",
	["$http", "$q", "$filter", "ProductsData", function($http, $q, $filter, ProductsData){
		return {getObs:getObs};

		function getObs(data){
			var DATE_FORMAT = "ddMMyy";
			var url = "/meto/products";

			url += '/' + $filter("date")(data.from, DATE_FORMAT);
			url += "/" + $filter("date")(data.to, DATE_FORMAT);
			var request = $http({
				method:"get",
				url:url
			});

			return request.then(handleSuccess, handleError);
		}

		function handleSuccess(response){
			for(var i=0; i<response.data.length; i++){
				var item = response.data[i];
				if(item['date']){
					item['date'] = strToDate(item['date']);
				}
			}
			ProductsData.data = response.data;
			return ProductsData.data;
		}

		function handleError(){
			return $q.reject("Unknown error in tableService");
		}

		function strToDate(str){
			var date = null;
			try{
				date = new Date(parseInt(str[4]+str[5]), parseInt(str[2]+str[3]) - 1, parseInt(str[0]+str[1]), 0, 0, 0, 0);
			} catch(e){}
			return date;
		}
	}]
);