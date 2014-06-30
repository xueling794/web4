'use strict';
require.config(requireConfig);
/**
 * Application
 */

define([

    'angular',
    'userSetting/modules/indexApp'

], function(angular) {

    angular.bootstrap(document, ['indexApp']);

});
