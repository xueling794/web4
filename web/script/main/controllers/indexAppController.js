'use strict';

/**
 * Controllers
 */

define(['angular'], function(angular) {

    return {
        Index: function($scope) {
            // $scope.users = Project.query();
            $scope.legend = "testLegend";
            $scope.user ={
                photo :"testPhoto",
                firstName : "testFirst"

            }  ;
            $scope.save = function(){
                alert( $scope.user.firstName );
            } ;
        },

        Create: function($scope, $location, Project) {
            $scope.legend = 'New User';
            $scope.save = function() {
                Project.save($scope.user, function(user) {
                    $location.path('/' + user._id.$oid);
                });
            }
        }

    }

});
