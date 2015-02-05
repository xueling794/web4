'use strict';
require.config(requireConfig);
/**
 * Application
 */

define([

    'angular',
    'profile/modules/indexApp'

], function(angular) {

    angular.bootstrap(document, ['indexApp']);

});
