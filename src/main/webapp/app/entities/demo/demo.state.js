(function() {
    'use strict';

    angular
        .module('demoApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('demo', {
            parent: 'entity',
            url: '/demo?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'demoApp.demo.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/demo/demos.html',
                    controller: 'DemoController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('demo');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('demo-detail', {
            parent: 'demo',
            url: '/demo/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'demoApp.demo.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/demo/demo-detail.html',
                    controller: 'DemoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('demo');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Demo', function($stateParams, Demo) {
                    return Demo.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'demo',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('demo-detail.edit', {
            parent: 'demo-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/demo/demo-dialog.html',
                    controller: 'DemoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Demo', function(Demo) {
                            return Demo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('demo.new', {
            parent: 'demo',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/demo/demo-dialog.html',
                    controller: 'DemoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nombre: null,
                                edad: null,
                                numeroCredencial: null,
                                sueldo: null,
                                impuesto: null,
                                impuestoDetalle: null,
                                activo: null,
                                fechaLocalDate: null,
                                fechaZoneDateTime: null,
                                imagen: null,
                                imagenContentType: null,
                                imagenAnyBlob: null,
                                imagenAnyBlobContentType: null,
                                imagenBlob: null,
                                imagenBlobContentType: null,
                                desc: null,
                                instante: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('demo', null, { reload: 'demo' });
                }, function() {
                    $state.go('demo');
                });
            }]
        })
        .state('demo.edit', {
            parent: 'demo',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/demo/demo-dialog.html',
                    controller: 'DemoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Demo', function(Demo) {
                            return Demo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('demo', null, { reload: 'demo' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('demo.delete', {
            parent: 'demo',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/demo/demo-delete-dialog.html',
                    controller: 'DemoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Demo', function(Demo) {
                            return Demo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('demo', null, { reload: 'demo' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
