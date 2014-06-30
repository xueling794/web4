/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-5-18
 * Time: 下午2:16
 * To change this template use File | Settings | File Templates.
 */

'use strict';
require.config(requireConfig);

define([

    'angular',
    'main/modules/loginApp'

], function(angular) {

    angular.bootstrap(document, ['loginApp']);

});