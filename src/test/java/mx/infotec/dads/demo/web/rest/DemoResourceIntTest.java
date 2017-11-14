package mx.infotec.dads.demo.web.rest;

import mx.infotec.dads.demo.DemoApp;

import mx.infotec.dads.demo.domain.Demo;
import mx.infotec.dads.demo.repository.DemoRepository;
import mx.infotec.dads.demo.repository.search.DemoSearchRepository;
import mx.infotec.dads.demo.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static mx.infotec.dads.demo.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DemoResource REST controller.
 *
 * @see DemoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApp.class)
public class DemoResourceIntTest {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Integer DEFAULT_EDAD = 1;
    private static final Integer UPDATED_EDAD = 2;

    private static final Long DEFAULT_NUMERO_CREDENCIAL = 1L;
    private static final Long UPDATED_NUMERO_CREDENCIAL = 2L;

    private static final BigDecimal DEFAULT_SUELDO = new BigDecimal(1);
    private static final BigDecimal UPDATED_SUELDO = new BigDecimal(2);

    private static final Float DEFAULT_IMPUESTO = 1F;
    private static final Float UPDATED_IMPUESTO = 2F;

    private static final Double DEFAULT_IMPUESTO_DETALLE = 1D;
    private static final Double UPDATED_IMPUESTO_DETALLE = 2D;

    private static final Boolean DEFAULT_ACTIVO = false;
    private static final Boolean UPDATED_ACTIVO = true;

    private static final LocalDate DEFAULT_FECHA_LOCAL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_LOCAL_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final ZonedDateTime DEFAULT_FECHA_ZONE_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FECHA_ZONE_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final byte[] DEFAULT_IMAGEN = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGEN = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_IMAGEN_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGEN_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_IMAGEN_ANY_BLOB = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGEN_ANY_BLOB = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_IMAGEN_ANY_BLOB_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGEN_ANY_BLOB_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_IMAGEN_BLOB = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGEN_BLOB = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_IMAGEN_BLOB_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGEN_BLOB_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_DESC = "AAAAAAAAAA";
    private static final String UPDATED_DESC = "BBBBBBBBBB";

    private static final Instant DEFAULT_INSTANTE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_INSTANTE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private DemoRepository demoRepository;

    @Autowired
    private DemoSearchRepository demoSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDemoMockMvc;

    private Demo demo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DemoResource demoResource = new DemoResource(demoRepository, demoSearchRepository);
        this.restDemoMockMvc = MockMvcBuilders.standaloneSetup(demoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Demo createEntity(EntityManager em) {
        Demo demo = new Demo()
            .nombre(DEFAULT_NOMBRE)
            .edad(DEFAULT_EDAD)
            .numeroCredencial(DEFAULT_NUMERO_CREDENCIAL)
            .sueldo(DEFAULT_SUELDO)
            .impuesto(DEFAULT_IMPUESTO)
            .impuestoDetalle(DEFAULT_IMPUESTO_DETALLE)
            .activo(DEFAULT_ACTIVO)
            .fechaLocalDate(DEFAULT_FECHA_LOCAL_DATE)
            .fechaZoneDateTime(DEFAULT_FECHA_ZONE_DATE_TIME)
            .imagen(DEFAULT_IMAGEN)
            .imagenContentType(DEFAULT_IMAGEN_CONTENT_TYPE)
            .imagenAnyBlob(DEFAULT_IMAGEN_ANY_BLOB)
            .imagenAnyBlobContentType(DEFAULT_IMAGEN_ANY_BLOB_CONTENT_TYPE)
            .imagenBlob(DEFAULT_IMAGEN_BLOB)
            .imagenBlobContentType(DEFAULT_IMAGEN_BLOB_CONTENT_TYPE)
            .desc(DEFAULT_DESC)
            .instante(DEFAULT_INSTANTE);
        return demo;
    }

    @Before
    public void initTest() {
        demoSearchRepository.deleteAll();
        demo = createEntity(em);
    }

    @Test
    @Transactional
    public void createDemo() throws Exception {
        int databaseSizeBeforeCreate = demoRepository.findAll().size();

        // Create the Demo
        restDemoMockMvc.perform(post("/api/demos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(demo)))
            .andExpect(status().isCreated());

        // Validate the Demo in the database
        List<Demo> demoList = demoRepository.findAll();
        assertThat(demoList).hasSize(databaseSizeBeforeCreate + 1);
        Demo testDemo = demoList.get(demoList.size() - 1);
        assertThat(testDemo.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testDemo.getEdad()).isEqualTo(DEFAULT_EDAD);
        assertThat(testDemo.getNumeroCredencial()).isEqualTo(DEFAULT_NUMERO_CREDENCIAL);
        assertThat(testDemo.getSueldo()).isEqualTo(DEFAULT_SUELDO);
        assertThat(testDemo.getImpuesto()).isEqualTo(DEFAULT_IMPUESTO);
        assertThat(testDemo.getImpuestoDetalle()).isEqualTo(DEFAULT_IMPUESTO_DETALLE);
        assertThat(testDemo.isActivo()).isEqualTo(DEFAULT_ACTIVO);
        assertThat(testDemo.getFechaLocalDate()).isEqualTo(DEFAULT_FECHA_LOCAL_DATE);
        assertThat(testDemo.getFechaZoneDateTime()).isEqualTo(DEFAULT_FECHA_ZONE_DATE_TIME);
        assertThat(testDemo.getImagen()).isEqualTo(DEFAULT_IMAGEN);
        assertThat(testDemo.getImagenContentType()).isEqualTo(DEFAULT_IMAGEN_CONTENT_TYPE);
        assertThat(testDemo.getImagenAnyBlob()).isEqualTo(DEFAULT_IMAGEN_ANY_BLOB);
        assertThat(testDemo.getImagenAnyBlobContentType()).isEqualTo(DEFAULT_IMAGEN_ANY_BLOB_CONTENT_TYPE);
        assertThat(testDemo.getImagenBlob()).isEqualTo(DEFAULT_IMAGEN_BLOB);
        assertThat(testDemo.getImagenBlobContentType()).isEqualTo(DEFAULT_IMAGEN_BLOB_CONTENT_TYPE);
        assertThat(testDemo.getDesc()).isEqualTo(DEFAULT_DESC);
        assertThat(testDemo.getInstante()).isEqualTo(DEFAULT_INSTANTE);

        // Validate the Demo in Elasticsearch
        Demo demoEs = demoSearchRepository.findOne(testDemo.getId());
        assertThat(demoEs).isEqualToComparingFieldByField(testDemo);
    }

    @Test
    @Transactional
    public void createDemoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = demoRepository.findAll().size();

        // Create the Demo with an existing ID
        demo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDemoMockMvc.perform(post("/api/demos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(demo)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Demo> demoList = demoRepository.findAll();
        assertThat(demoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = demoRepository.findAll().size();
        // set the field null
        demo.setNombre(null);

        // Create the Demo, which fails.

        restDemoMockMvc.perform(post("/api/demos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(demo)))
            .andExpect(status().isBadRequest());

        List<Demo> demoList = demoRepository.findAll();
        assertThat(demoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDemos() throws Exception {
        // Initialize the database
        demoRepository.saveAndFlush(demo);

        // Get all the demoList
        restDemoMockMvc.perform(get("/api/demos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(demo.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
            .andExpect(jsonPath("$.[*].edad").value(hasItem(DEFAULT_EDAD)))
            .andExpect(jsonPath("$.[*].numeroCredencial").value(hasItem(DEFAULT_NUMERO_CREDENCIAL.intValue())))
            .andExpect(jsonPath("$.[*].sueldo").value(hasItem(DEFAULT_SUELDO.intValue())))
            .andExpect(jsonPath("$.[*].impuesto").value(hasItem(DEFAULT_IMPUESTO.doubleValue())))
            .andExpect(jsonPath("$.[*].impuestoDetalle").value(hasItem(DEFAULT_IMPUESTO_DETALLE.doubleValue())))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO.booleanValue())))
            .andExpect(jsonPath("$.[*].fechaLocalDate").value(hasItem(DEFAULT_FECHA_LOCAL_DATE.toString())))
            .andExpect(jsonPath("$.[*].fechaZoneDateTime").value(hasItem(sameInstant(DEFAULT_FECHA_ZONE_DATE_TIME))))
            .andExpect(jsonPath("$.[*].imagenContentType").value(hasItem(DEFAULT_IMAGEN_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imagen").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGEN))))
            .andExpect(jsonPath("$.[*].imagenAnyBlobContentType").value(hasItem(DEFAULT_IMAGEN_ANY_BLOB_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imagenAnyBlob").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGEN_ANY_BLOB))))
            .andExpect(jsonPath("$.[*].imagenBlobContentType").value(hasItem(DEFAULT_IMAGEN_BLOB_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imagenBlob").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGEN_BLOB))))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC.toString())))
            .andExpect(jsonPath("$.[*].instante").value(hasItem(DEFAULT_INSTANTE.toString())));
    }

    @Test
    @Transactional
    public void getDemo() throws Exception {
        // Initialize the database
        demoRepository.saveAndFlush(demo);

        // Get the demo
        restDemoMockMvc.perform(get("/api/demos/{id}", demo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(demo.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.edad").value(DEFAULT_EDAD))
            .andExpect(jsonPath("$.numeroCredencial").value(DEFAULT_NUMERO_CREDENCIAL.intValue()))
            .andExpect(jsonPath("$.sueldo").value(DEFAULT_SUELDO.intValue()))
            .andExpect(jsonPath("$.impuesto").value(DEFAULT_IMPUESTO.doubleValue()))
            .andExpect(jsonPath("$.impuestoDetalle").value(DEFAULT_IMPUESTO_DETALLE.doubleValue()))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO.booleanValue()))
            .andExpect(jsonPath("$.fechaLocalDate").value(DEFAULT_FECHA_LOCAL_DATE.toString()))
            .andExpect(jsonPath("$.fechaZoneDateTime").value(sameInstant(DEFAULT_FECHA_ZONE_DATE_TIME)))
            .andExpect(jsonPath("$.imagenContentType").value(DEFAULT_IMAGEN_CONTENT_TYPE))
            .andExpect(jsonPath("$.imagen").value(Base64Utils.encodeToString(DEFAULT_IMAGEN)))
            .andExpect(jsonPath("$.imagenAnyBlobContentType").value(DEFAULT_IMAGEN_ANY_BLOB_CONTENT_TYPE))
            .andExpect(jsonPath("$.imagenAnyBlob").value(Base64Utils.encodeToString(DEFAULT_IMAGEN_ANY_BLOB)))
            .andExpect(jsonPath("$.imagenBlobContentType").value(DEFAULT_IMAGEN_BLOB_CONTENT_TYPE))
            .andExpect(jsonPath("$.imagenBlob").value(Base64Utils.encodeToString(DEFAULT_IMAGEN_BLOB)))
            .andExpect(jsonPath("$.desc").value(DEFAULT_DESC.toString()))
            .andExpect(jsonPath("$.instante").value(DEFAULT_INSTANTE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDemo() throws Exception {
        // Get the demo
        restDemoMockMvc.perform(get("/api/demos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDemo() throws Exception {
        // Initialize the database
        demoRepository.saveAndFlush(demo);
        demoSearchRepository.save(demo);
        int databaseSizeBeforeUpdate = demoRepository.findAll().size();

        // Update the demo
        Demo updatedDemo = demoRepository.findOne(demo.getId());
        updatedDemo
            .nombre(UPDATED_NOMBRE)
            .edad(UPDATED_EDAD)
            .numeroCredencial(UPDATED_NUMERO_CREDENCIAL)
            .sueldo(UPDATED_SUELDO)
            .impuesto(UPDATED_IMPUESTO)
            .impuestoDetalle(UPDATED_IMPUESTO_DETALLE)
            .activo(UPDATED_ACTIVO)
            .fechaLocalDate(UPDATED_FECHA_LOCAL_DATE)
            .fechaZoneDateTime(UPDATED_FECHA_ZONE_DATE_TIME)
            .imagen(UPDATED_IMAGEN)
            .imagenContentType(UPDATED_IMAGEN_CONTENT_TYPE)
            .imagenAnyBlob(UPDATED_IMAGEN_ANY_BLOB)
            .imagenAnyBlobContentType(UPDATED_IMAGEN_ANY_BLOB_CONTENT_TYPE)
            .imagenBlob(UPDATED_IMAGEN_BLOB)
            .imagenBlobContentType(UPDATED_IMAGEN_BLOB_CONTENT_TYPE)
            .desc(UPDATED_DESC)
            .instante(UPDATED_INSTANTE);

        restDemoMockMvc.perform(put("/api/demos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDemo)))
            .andExpect(status().isOk());

        // Validate the Demo in the database
        List<Demo> demoList = demoRepository.findAll();
        assertThat(demoList).hasSize(databaseSizeBeforeUpdate);
        Demo testDemo = demoList.get(demoList.size() - 1);
        assertThat(testDemo.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testDemo.getEdad()).isEqualTo(UPDATED_EDAD);
        assertThat(testDemo.getNumeroCredencial()).isEqualTo(UPDATED_NUMERO_CREDENCIAL);
        assertThat(testDemo.getSueldo()).isEqualTo(UPDATED_SUELDO);
        assertThat(testDemo.getImpuesto()).isEqualTo(UPDATED_IMPUESTO);
        assertThat(testDemo.getImpuestoDetalle()).isEqualTo(UPDATED_IMPUESTO_DETALLE);
        assertThat(testDemo.isActivo()).isEqualTo(UPDATED_ACTIVO);
        assertThat(testDemo.getFechaLocalDate()).isEqualTo(UPDATED_FECHA_LOCAL_DATE);
        assertThat(testDemo.getFechaZoneDateTime()).isEqualTo(UPDATED_FECHA_ZONE_DATE_TIME);
        assertThat(testDemo.getImagen()).isEqualTo(UPDATED_IMAGEN);
        assertThat(testDemo.getImagenContentType()).isEqualTo(UPDATED_IMAGEN_CONTENT_TYPE);
        assertThat(testDemo.getImagenAnyBlob()).isEqualTo(UPDATED_IMAGEN_ANY_BLOB);
        assertThat(testDemo.getImagenAnyBlobContentType()).isEqualTo(UPDATED_IMAGEN_ANY_BLOB_CONTENT_TYPE);
        assertThat(testDemo.getImagenBlob()).isEqualTo(UPDATED_IMAGEN_BLOB);
        assertThat(testDemo.getImagenBlobContentType()).isEqualTo(UPDATED_IMAGEN_BLOB_CONTENT_TYPE);
        assertThat(testDemo.getDesc()).isEqualTo(UPDATED_DESC);
        assertThat(testDemo.getInstante()).isEqualTo(UPDATED_INSTANTE);

        // Validate the Demo in Elasticsearch
        Demo demoEs = demoSearchRepository.findOne(testDemo.getId());
        assertThat(demoEs).isEqualToComparingFieldByField(testDemo);
    }

    @Test
    @Transactional
    public void updateNonExistingDemo() throws Exception {
        int databaseSizeBeforeUpdate = demoRepository.findAll().size();

        // Create the Demo

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDemoMockMvc.perform(put("/api/demos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(demo)))
            .andExpect(status().isCreated());

        // Validate the Demo in the database
        List<Demo> demoList = demoRepository.findAll();
        assertThat(demoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDemo() throws Exception {
        // Initialize the database
        demoRepository.saveAndFlush(demo);
        demoSearchRepository.save(demo);
        int databaseSizeBeforeDelete = demoRepository.findAll().size();

        // Get the demo
        restDemoMockMvc.perform(delete("/api/demos/{id}", demo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean demoExistsInEs = demoSearchRepository.exists(demo.getId());
        assertThat(demoExistsInEs).isFalse();

        // Validate the database is empty
        List<Demo> demoList = demoRepository.findAll();
        assertThat(demoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDemo() throws Exception {
        // Initialize the database
        demoRepository.saveAndFlush(demo);
        demoSearchRepository.save(demo);

        // Search the demo
        restDemoMockMvc.perform(get("/api/_search/demos?query=id:" + demo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(demo.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
            .andExpect(jsonPath("$.[*].edad").value(hasItem(DEFAULT_EDAD)))
            .andExpect(jsonPath("$.[*].numeroCredencial").value(hasItem(DEFAULT_NUMERO_CREDENCIAL.intValue())))
            .andExpect(jsonPath("$.[*].sueldo").value(hasItem(DEFAULT_SUELDO.intValue())))
            .andExpect(jsonPath("$.[*].impuesto").value(hasItem(DEFAULT_IMPUESTO.doubleValue())))
            .andExpect(jsonPath("$.[*].impuestoDetalle").value(hasItem(DEFAULT_IMPUESTO_DETALLE.doubleValue())))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO.booleanValue())))
            .andExpect(jsonPath("$.[*].fechaLocalDate").value(hasItem(DEFAULT_FECHA_LOCAL_DATE.toString())))
            .andExpect(jsonPath("$.[*].fechaZoneDateTime").value(hasItem(sameInstant(DEFAULT_FECHA_ZONE_DATE_TIME))))
            .andExpect(jsonPath("$.[*].imagenContentType").value(hasItem(DEFAULT_IMAGEN_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imagen").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGEN))))
            .andExpect(jsonPath("$.[*].imagenAnyBlobContentType").value(hasItem(DEFAULT_IMAGEN_ANY_BLOB_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imagenAnyBlob").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGEN_ANY_BLOB))))
            .andExpect(jsonPath("$.[*].imagenBlobContentType").value(hasItem(DEFAULT_IMAGEN_BLOB_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imagenBlob").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGEN_BLOB))))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC.toString())))
            .andExpect(jsonPath("$.[*].instante").value(hasItem(DEFAULT_INSTANTE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Demo.class);
        Demo demo1 = new Demo();
        demo1.setId(1L);
        Demo demo2 = new Demo();
        demo2.setId(demo1.getId());
        assertThat(demo1).isEqualTo(demo2);
        demo2.setId(2L);
        assertThat(demo1).isNotEqualTo(demo2);
        demo1.setId(null);
        assertThat(demo1).isNotEqualTo(demo2);
    }
}
