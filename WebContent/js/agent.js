var app= angular.module('agentApp',['ui.router',
                        'agent.services','agent.controllers']);

app.config(function($stateProvider) {
	$stateProvider
	.state('home', {
		url:'/',
		templateUrl:'/index.html',
		controller:'agentController'
	})
})

