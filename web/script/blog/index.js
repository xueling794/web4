'use strict';
require.config(requireConfig);
/**
 * Application
 */

define([

    'angular',
    'blog/modules/indexApp'

], function(angular) {

    angular.bootstrap(document, ['indexApp']);

});
