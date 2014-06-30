'use strict';
require.config(requireConfig);
/**
 * Application
 */

define([

    'angular',
    'main/modules/indexApp'

], function(angular) {

    angular.bootstrap(document, ['indexApp']);

});
