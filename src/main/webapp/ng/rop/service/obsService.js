angular.module("obsService", ["models"])
.service(
	"obsService",
	["$http", "$q", "$filter", "TableData", function($http, $q, $filter, TableData){
		return {
			getObs:getObs
		}

		function getObs(data){
			var DATE_FORMAT = "ddMMyy";
			var url = "/meto/WeatherData";
			var regions = [];
			for(var i=0; i< data.regions.length; i++){
				regions.push(data.regions[i].id);
			}
			url += '/' + $filter("date")(data.from, DATE_FORMAT);
			url += "/" + $filter("date")(data.to, DATE_FORMAT);
			url += "/" + regions.join('-');
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
			TableData.data = response.data;
			return TableData.data;
		}

		function handleError(response){
			return $q.reject("Unknown error in obsService");
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