/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-7-17
 * Time: 下午10:55
 * To change this template use File | Settings | File Templates.
 */

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
    'vote/modules/detailApp'

], function(angular) {

    angular.bootstrap(document, ['detailApp']);

});

