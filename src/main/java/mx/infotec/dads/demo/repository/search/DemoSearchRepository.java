package mx.infotec.dads.demo.repository.search;

import mx.infotec.dads.demo.domain.Demo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Demo entity.
 */
public interface DemoSearchRepository extends ElasticsearchRepository<Demo, Long> {
}
