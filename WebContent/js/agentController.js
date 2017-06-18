/**
 * 
 */

var app = angular.module('agent.controllers',[]);

app.controller('agentController', ['$http','$rootScope','$scope','agentService', '$window',
function($http,$scope,$rootScope, agentService, $window) {
	var location = $window.location.href;
	
	var host;
	
	$scope.consoleMessages = [];
	$scope.running = [];
	$scope.centers = [];
	$scope.types = [];
	$scope.receivers=[];
	
	
	$scope.init = function() {
		console.log(location);
		
		var index = location.indexOf("//");
		host = location.substr(index+2);
		host = host.substr(0, host.indexOf("/"));
		
		
		
		$scope.addReceiver();
		$scope.getPerformative();
		$scope.getClasses();
		$scope.getRunning();
		$scope.addReceiver();
		$rootScope.host = host;
		
		console.log(host);
	}
	

	
	$scope.createAgent = function(agentName, agentType) {
		console.log("Agent name: "+agentName)
		console.log("Agent type: "+agentType)
		
		if (agentName == "" || agentType == "") return;
		agentService.createAgent(agentName,agentType, location).then(function(response) {
			console.log("Success");
			$scope.getRunning();
		},function(response) {
			console.log("Error");
		})
	}
	
	$scope.stopAgent = function(aid) {
		agentService.stopAgent(aid, location).then(function(response){
			console.log("Success");
			$scope.getRunning();
		},function(response){
			console.log("Error");
		})
	}
	
	$scope.getCenters = function() {
		agentService.getCenters(location).then(function(response){
			$scope.centers = response.data;
			$scope.$apply;
		}, function(response) {
			console.log("Error");
		})
	}
	
	$scope.getRunning = function() {
		agentService.getRunning(location).then(function(response) {
			$scope.running = response.data;
			$scope.$apply;
		}, function(response) {
			console.log("error")
		})
	}
	
	$scope.getClasses = function() {
		agentService.getClasses(location).then(function(response) {
			$scope.types = response.data;
			$scope.$apply;
		}, function(response) {
			console.log("error")
		});
	}
	
	$scope.getPerformative = function() {
		agentService.getPerformative(location).then(function(response) {
			$scope.performative = response.data;
			$scope.$apply;
		}, function(response) {
			console.log("error")
		})
	}
	
	$scope.addReceiver = function() {
		$rootScope.receivers.push("");
		$rootScope.$apply;
	}
	
	$scope.removeReceiver = function(id) {
		console.log("removeReceiver")
		if (id > -1) {
			$rootScope.receivers.splice(id, 1);
		}
	}
	
	$scope.sendACLMessage = function(perform, sender, replyTo, content, language, encoding,
										ontology, protocol, conversationId, replyWith, replyBy) {
		if(sender == undefined) console.log("sender is undefined");
		
		console.log("ACLMessage: " + perform + sender + replyTo + content + language + encoding +
										ontology + protocol + conversationId + replyWith + replyBy);
		
		for(var i = $rootScope.receivers.length - 1; i >= 0; i--) {
			if($rootScope.receivers[i] === "") {
				$rootScope.receivers.splice(i, 1);
			}
		}
		
		var receiversJSON = "[" +$rootScope.receivers + "]";
		
		console.log("receivers" + JSON.parse("["+$rootScope.receivers+"]"))
		
		console.log("receivers" + $rootScope.receivers)

		if (replyTo == undefined) replyTo = null;
		
		content = checkForUndefined(content)
		language = checkForUndefined(language)
		encoding = checkForUndefined(encoding)
		ontology = checkForUndefined(protocol)
		conversationId = checkForUndefined(conversationId)
		replyWith = checkForUndefined(replyWith)
		replyBy = checkForUndefined(replyBy)

					
		aclMessageJSON = {
			performative:perform,
			sender:JSON.parse(sender),
			receivers:JSON.parse(receiversJSON),
			replyTo:JSON.parse(replyTo),
			content:content,
			contentObject:JSON.parse(null),
			userArgs:JSON.parse(null),
			language:language,
			encoding:encoding,
			ontology:ontology,
			protocol:protocol,
			conversationId:conversationId,
			replyWith:replyWith,
			inReplyWith:"",
			inReplyTo:"",
			replyTo:replyTo
		}
		var acl = JSON.stringify(aclMessageJSON);
		agentService.sendACLMessage(acl, location).then(function(response){
			console.log(response);
	/*		document.getElementByClassName(".newAgentName").value = "";*/
		}, function(response) {
			console.log("error");
		})
		 
	
	}
	
	function checkForUndefined(parameter) {
		if (parameter == undefined) {
			console.log(parameter)
			return "";
		} else {
			return parameter;
		}
	}
	
	
}])





