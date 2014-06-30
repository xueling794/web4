'use strict';
require.config(requireConfig);
/**
 * Application
 */

define([

    'angular',
    'main/modules/defaultApp'

], function(angular) {

    angular.bootstrap(document, ['defaultApp']);

});
