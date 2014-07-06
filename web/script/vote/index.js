/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-7-4
 * Time: 下午10:45
 * To change this template use File | Settings | File Templates.
 */

'use strict';
require.config(requireConfig);
/**
 * Application
 */

define([

    'angular',
    'vote/modules/indexApp'

], function(angular) {

    angular.bootstrap(document, ['indexApp']);

});

