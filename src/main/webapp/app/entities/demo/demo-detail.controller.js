(function() {
    'use strict';

    angular
        .module('demoApp')
        .controller('DemoDetailController', DemoDetailController);

    DemoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Demo'];

    function DemoDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Demo) {
        var vm = this;

        vm.demo = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('demoApp:demoUpdate', function(event, result) {
            vm.demo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
