package mx.infotec.dads.demo.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.matchPhraseQuery;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import mx.infotec.dads.demo.domain.Demo;
import mx.infotec.dads.demo.repository.DemoRepository;
import mx.infotec.dads.demo.repository.search.DemoSearchRepository;
import mx.infotec.dads.demo.web.rest.util.HeaderUtil;
import mx.infotec.dads.demo.web.rest.util.PaginationUtil;

/**
 * REST controller for managing Demo.
 */
@RestController
@RequestMapping("/api")
public class DemoResource {

    private final Logger log = LoggerFactory.getLogger(DemoResource.class);

    private static final String ENTITY_NAME = "demo";

    private final DemoRepository demoRepository;

    private final DemoSearchRepository demoSearchRepository;

    public DemoResource(DemoRepository demoRepository, DemoSearchRepository demoSearchRepository) {
        this.demoRepository = demoRepository;
        this.demoSearchRepository = demoSearchRepository;
    }

    /**
     * POST /demos : Create a new demo.
     *
     * @param demo
     *            the demo to create
     * @return the ResponseEntity with status 201 (Created) and with body the
     *         new demo, or with status 400 (Bad Request) if the demo has
     *         already an ID
     * @throws URISyntaxException
     *             if the Location URI syntax is incorrect
     */
    @PostMapping("/demos")
    @Timed
    public ResponseEntity<Demo> createDemo(@Valid @RequestBody Demo demo) throws URISyntaxException {
        log.debug("REST request to save Demo : {}", demo);
        if (demo.getId() != null) {
            return ResponseEntity.badRequest().headers(
                    HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new demo cannot already have an ID"))
                    .body(null);
        }
        Demo result = demoRepository.save(demo);
        demoSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/demos/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
    }

    /**
     * PUT /demos : Updates an existing demo.
     *
     * @param demo
     *            the demo to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated
     *         demo, or with status 400 (Bad Request) if the demo is not valid,
     *         or with status 500 (Internal Server Error) if the demo couldnt be
     *         updated
     * @throws URISyntaxException
     *             if the Location URI syntax is incorrect
     */
    @PutMapping("/demos")
    @Timed
    public ResponseEntity<Demo> updateDemo(@Valid @RequestBody Demo demo) throws URISyntaxException {
        log.debug("REST request to update Demo : {}", demo);
        if (demo.getId() == null) {
            return createDemo(demo);
        }
        Demo result = demoRepository.save(demo);
        demoSearchRepository.save(result);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, demo.getId().toString()))
                .body(result);
    }

    /**
     * GET /demos : get all the demos.
     *
     * @param pageable
     *            the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of demos in
     *         body
     */
    @GetMapping("/demos")
    @Timed
    public ResponseEntity<List<Demo>> getAllDemos(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Demos");
        Page<Demo> page = demoRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/demos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET /demos/:id : get the "id" demo.
     *
     * @param id
     *            the id of the demo to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the demo,
     *         or with status 404 (Not Found)
     */
    @GetMapping("/demos/{id}")
    @Timed
    public ResponseEntity<Demo> getDemo(@PathVariable Long id) {
        log.debug("REST request to get Demo : {}", id);
        Demo demo = demoRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(demo));
    }

    /**
     * DELETE /demos/:id : delete the "id" demo.
     *
     * @param id
     *            the id of the demo to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/demos/{id}")
    @Timed
    public ResponseEntity<Void> deleteDemo(@PathVariable Long id) {
        log.debug("REST request to delete Demo : {}", id);
        demoRepository.delete(id);
        demoSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH /_search/demos?query=:query : search for the demo corresponding to
     * the query.
     *
     * @param query
     *            the query of the demo search
     * @param pageable
     *            the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/demos")
    @Timed
    public ResponseEntity<List<Demo>> searchDemos(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Demos for query {}", query);
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchPhraseQuery("desc", query).slop(1)).build();
        Page<Demo> page = demoSearchRepository.search(searchQuery);

        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/demos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
