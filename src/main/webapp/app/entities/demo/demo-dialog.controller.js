(function() {
    'use strict';

    angular
        .module('demoApp')
        .controller('DemoDialogController', DemoDialogController);

    DemoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Demo'];

    function DemoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Demo) {
        var vm = this;

        vm.demo = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.demo.id !== null) {
                Demo.update(vm.demo, onSaveSuccess, onSaveError);
            } else {
                Demo.save(vm.demo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('demoApp:demoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.fechaLocalDate = false;
        vm.datePickerOpenStatus.fechaZoneDateTime = false;

        vm.setImagen = function ($file, demo) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        demo.imagen = base64Data;
                        demo.imagenContentType = $file.type;
                    });
                });
            }
        };

        vm.setImagenAnyBlob = function ($file, demo) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        demo.imagenAnyBlob = base64Data;
                        demo.imagenAnyBlobContentType = $file.type;
                    });
                });
            }
        };

        vm.setImagenBlob = function ($file, demo) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        demo.imagenBlob = base64Data;
                        demo.imagenBlobContentType = $file.type;
                    });
                });
            }
        };
        vm.datePickerOpenStatus.instante = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
