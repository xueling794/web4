'use strict';

define(['app', 'routeConfig'], function (app, routeConfig) {

    return app.config(function ($routeProvider) {
        $routeProvider.when('/view1', routeConfig.config('../partials/view.html', 'controllers/first'));
        $routeProvider.when('/view2', routeConfig.config('../partials/view2.html', 'controllers/second', ['directives/version']));

        $routeProvider.otherwise({redirectTo:'/view1'});
    });

    return app;
});
