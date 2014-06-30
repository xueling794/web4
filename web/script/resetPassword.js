
'use strict';
require.config(requireConfig);
/**
 * Application
 */

define([

    'angular',
    'main/modules/resetPasswordApp'

], function(angular) {

    angular.bootstrap(document, ['resetPasswordApp']);

});
