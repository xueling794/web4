'use strict';
require.config(requireConfig);
/**
 * Application
 */

define([

    'angular',
    'main/modules/activationIndex'

], function(angular) {

    angular.bootstrap(document, ['activationIndex']);

});
