package mx.infotec.dads.demo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Demo.
 */
@Entity
@Table(name = "demo")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "demo")
public class Demo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "edad")
    private Integer edad;

    @Column(name = "numero_credencial")
    private Long numeroCredencial;

    @Column(name = "sueldo", precision=10, scale=2)
    private BigDecimal sueldo;

    @Column(name = "impuesto")
    private Float impuesto;

    @Column(name = "impuesto_detalle")
    private Double impuestoDetalle;

    @Column(name = "activo")
    private Boolean activo;

    @Column(name = "fecha_local_date")
    private LocalDate fechaLocalDate;

    @Column(name = "fecha_zone_date_time")
    private ZonedDateTime fechaZoneDateTime;

    @Lob
    @Column(name = "imagen")
    private byte[] imagen;

    @Column(name = "imagen_content_type")
    private String imagenContentType;

    @Lob
    @Column(name = "imagen_any_blob")
    private byte[] imagenAnyBlob;

    @Column(name = "imagen_any_blob_content_type")
    private String imagenAnyBlobContentType;

    @Lob
    @Column(name = "imagen_blob")
    private byte[] imagenBlob;

    @Column(name = "imagen_blob_content_type")
    private String imagenBlobContentType;

    @Lob
    @Column(name = "jhi_desc")
    private String desc;

    @Column(name = "instante")
    private Instant instante;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public Demo nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getEdad() {
        return edad;
    }

    public Demo edad(Integer edad) {
        this.edad = edad;
        return this;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public Long getNumeroCredencial() {
        return numeroCredencial;
    }

    public Demo numeroCredencial(Long numeroCredencial) {
        this.numeroCredencial = numeroCredencial;
        return this;
    }

    public void setNumeroCredencial(Long numeroCredencial) {
        this.numeroCredencial = numeroCredencial;
    }

    public BigDecimal getSueldo() {
        return sueldo;
    }

    public Demo sueldo(BigDecimal sueldo) {
        this.sueldo = sueldo;
        return this;
    }

    public void setSueldo(BigDecimal sueldo) {
        this.sueldo = sueldo;
    }

    public Float getImpuesto() {
        return impuesto;
    }

    public Demo impuesto(Float impuesto) {
        this.impuesto = impuesto;
        return this;
    }

    public void setImpuesto(Float impuesto) {
        this.impuesto = impuesto;
    }

    public Double getImpuestoDetalle() {
        return impuestoDetalle;
    }

    public Demo impuestoDetalle(Double impuestoDetalle) {
        this.impuestoDetalle = impuestoDetalle;
        return this;
    }

    public void setImpuestoDetalle(Double impuestoDetalle) {
        this.impuestoDetalle = impuestoDetalle;
    }

    public Boolean isActivo() {
        return activo;
    }

    public Demo activo(Boolean activo) {
        this.activo = activo;
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public LocalDate getFechaLocalDate() {
        return fechaLocalDate;
    }

    public Demo fechaLocalDate(LocalDate fechaLocalDate) {
        this.fechaLocalDate = fechaLocalDate;
        return this;
    }

    public void setFechaLocalDate(LocalDate fechaLocalDate) {
        this.fechaLocalDate = fechaLocalDate;
    }

    public ZonedDateTime getFechaZoneDateTime() {
        return fechaZoneDateTime;
    }

    public Demo fechaZoneDateTime(ZonedDateTime fechaZoneDateTime) {
        this.fechaZoneDateTime = fechaZoneDateTime;
        return this;
    }

    public void setFechaZoneDateTime(ZonedDateTime fechaZoneDateTime) {
        this.fechaZoneDateTime = fechaZoneDateTime;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public Demo imagen(byte[] imagen) {
        this.imagen = imagen;
        return this;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public String getImagenContentType() {
        return imagenContentType;
    }

    public Demo imagenContentType(String imagenContentType) {
        this.imagenContentType = imagenContentType;
        return this;
    }

    public void setImagenContentType(String imagenContentType) {
        this.imagenContentType = imagenContentType;
    }

    public byte[] getImagenAnyBlob() {
        return imagenAnyBlob;
    }

    public Demo imagenAnyBlob(byte[] imagenAnyBlob) {
        this.imagenAnyBlob = imagenAnyBlob;
        return this;
    }

    public void setImagenAnyBlob(byte[] imagenAnyBlob) {
        this.imagenAnyBlob = imagenAnyBlob;
    }

    public String getImagenAnyBlobContentType() {
        return imagenAnyBlobContentType;
    }

    public Demo imagenAnyBlobContentType(String imagenAnyBlobContentType) {
        this.imagenAnyBlobContentType = imagenAnyBlobContentType;
        return this;
    }

    public void setImagenAnyBlobContentType(String imagenAnyBlobContentType) {
        this.imagenAnyBlobContentType = imagenAnyBlobContentType;
    }

    public byte[] getImagenBlob() {
        return imagenBlob;
    }

    public Demo imagenBlob(byte[] imagenBlob) {
        this.imagenBlob = imagenBlob;
        return this;
    }

    public void setImagenBlob(byte[] imagenBlob) {
        this.imagenBlob = imagenBlob;
    }

    public String getImagenBlobContentType() {
        return imagenBlobContentType;
    }

    public Demo imagenBlobContentType(String imagenBlobContentType) {
        this.imagenBlobContentType = imagenBlobContentType;
        return this;
    }

    public void setImagenBlobContentType(String imagenBlobContentType) {
        this.imagenBlobContentType = imagenBlobContentType;
    }

    public String getDesc() {
        return desc;
    }

    public Demo desc(String desc) {
        this.desc = desc;
        return this;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Instant getInstante() {
        return instante;
    }

    public Demo instante(Instant instante) {
        this.instante = instante;
        return this;
    }

    public void setInstante(Instant instante) {
        this.instante = instante;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Demo demo = (Demo) o;
        if (demo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), demo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Demo{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", edad='" + getEdad() + "'" +
            ", numeroCredencial='" + getNumeroCredencial() + "'" +
            ", sueldo='" + getSueldo() + "'" +
            ", impuesto='" + getImpuesto() + "'" +
            ", impuestoDetalle='" + getImpuestoDetalle() + "'" +
            ", activo='" + isActivo() + "'" +
            ", fechaLocalDate='" + getFechaLocalDate() + "'" +
            ", fechaZoneDateTime='" + getFechaZoneDateTime() + "'" +
            ", imagen='" + getImagen() + "'" +
            ", imagenContentType='" + imagenContentType + "'" +
            ", imagenAnyBlob='" + getImagenAnyBlob() + "'" +
            ", imagenAnyBlobContentType='" + imagenAnyBlobContentType + "'" +
            ", imagenBlob='" + getImagenBlob() + "'" +
            ", imagenBlobContentType='" + imagenBlobContentType + "'" +
            ", desc='" + getDesc() + "'" +
            ", instante='" + getInstante() + "'" +
            "}";
    }
}
