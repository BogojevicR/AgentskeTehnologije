/**
 * 
 */
var services = angular.module('agent.services',['ngResource']);

services.service('agentService', ['$http', '$rootScope', 
	function($http, $rootScope) {
	
	
		this.getConsole = function(location) {
			return $http.get(location + "rest/messages/console");
		}
	
		this.createAgent = function (agentName, agentType, location) {
			return $http.put(location + "rest/agents/running/"+agentType+"/"+agentName);
		}
		
		this.stopAgent = function(aid, location) {
			return $http({
				method: 'DELETE',
				url: location+"rest/agents/running/"+aid
			})
		}
		
		this.getCenters = function(location) {
			return $http.get(location + "rest/agents/centers");
		}
		
		this.getRunning = function(location) {
			return $http.get(location + "rest/agents/running");
		}
		
		this.getClasses = function(location) {
			return $http.get(location + "rest/agents/classes");
		}
		
		this.getPerformative = function(location) {
			return $http.get(location + "rest/messages");
		}
		
		
		
		this.sendACLMessage = function(ACLMessage, location) {
			return $http.post(location + "rest/messages", ACLMessage);
		}
	}

	
])
