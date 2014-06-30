
'use strict';
require.config(requireConfig);
/**
 * Application
 */

define([

    'angular',
    'main/modules/sendResetUrlApp'

], function(angular) {

    angular.bootstrap(document, ['sendResetUrlApp']);

});
