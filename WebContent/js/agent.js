var app= angular.module("agentApp",[]);

app.controller("agent",function($scope,$rootScope,$window,$http){
	
	var location = window.location.href;
	
	var host;
	
	$scope.init = function() {
	console.log(location);
	
	var index = location.indexOf("//");
	host = location.substr(index+2);
	
	host = host.substr(0, host.indexOf("/"));
	
	$rootScope.host = host;
	
	console.log(host);
	
	
	}
	
});