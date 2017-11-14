(function() {
    'use strict';

    angular
        .module('demoApp')
        .factory('DemoSearch', DemoSearch);

    DemoSearch.$inject = ['$resource'];

    function DemoSearch($resource) {
        var resourceUrl =  'api/_search/demos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
