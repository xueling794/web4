/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-7-31
 * Time: 下午11:51
 * To change this template use File | Settings | File Templates.
 */
'use strict';
require.config(requireConfig);
/**
 * Application
 */

define([

    'angular',
    'game/modules/mapApp'

], function(angular) {

    angular.bootstrap(document, ['mapApp']);

});