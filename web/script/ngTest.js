'use strict';

/**
 * Bootstrap file
 */

require.config({
    paths: {
        jquery: 'lib/jquery.min',
        bootstrap: '../css/metro/js/bootstrap',
        angular: 'lib/angular.min',
        angularResource: 'lib/angular-resource.min'
    },
    shim: {
        jquery: {
            exports: 'jquery'
        },
        bootstrap: {
            exports: 'bootstrap'
        },
        angular: {
            deps: ['jquery', 'bootstrap'],
            exports: 'angular'
        },
        angularResource: {
            deps: ['angular'],
            exports: 'angularResource'
        }
    }
});

require(['app']);
