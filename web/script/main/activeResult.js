'use strict';
require.config(requireConfig);
/**
 * Application
 */

define([

    'angular',
    'main/modules/activeResultIndex'

], function(angular) {

    angular.bootstrap(document, ['activeResultIndex']);

});
