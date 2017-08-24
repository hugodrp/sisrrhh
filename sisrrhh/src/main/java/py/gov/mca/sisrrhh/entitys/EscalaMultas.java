/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.mca.sisrrhh.entitys;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author vinsfran
 */
@Entity
@Table(name = "escala_multas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EscalaMultas.findAll", query = "SELECT e FROM EscalaMultas e"),
    @NamedQuery(name = "EscalaMultas.findById", query = "SELECT e FROM EscalaMultas e WHERE e.id = :id"),
    @NamedQuery(name = "EscalaMultas.findByDescripcion", query = "SELECT e FROM EscalaMultas e WHERE e.descripcion = :descripcion"),
    @NamedQuery(name = "EscalaMultas.findByCantidadMinutosDesde", query = "SELECT e FROM EscalaMultas e WHERE e.cantidadMinutosDesde = :cantidadMinutosDesde"),
    @NamedQuery(name = "EscalaMultas.findByCantidadMinutosHasta", query = "SELECT e FROM EscalaMultas e WHERE e.cantidadMinutosHasta = :cantidadMinutosHasta"),
    @NamedQuery(name = "EscalaMultas.findByPorcentajeMulta", query = "SELECT e FROM EscalaMultas e WHERE e.porcentajeMulta = :porcentajeMulta"),
    @NamedQuery(name = "EscalaMultas.findByTipoEscala", query = "SELECT e FROM EscalaMultas e WHERE e.tipoEscala = :tipoEscala")})
public class EscalaMultas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad_minutos_desde")
    private int cantidadMinutosDesde;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad_minutos_hasta")
    private int cantidadMinutosHasta;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "porcentaje_multa")
    private Double porcentajeMulta;
    @Size(max = 20)
    @Column(name = "tipo_escala")
    private String tipoEscala;

    public EscalaMultas() {
    }

    public EscalaMultas(Integer id) {
        this.id = id;
    }

    public EscalaMultas(Integer id, String descripcion, int cantidadMinutosDesde, int cantidadMinutosHasta) {
        this.id = id;
        this.descripcion = descripcion;
        this.cantidadMinutosDesde = cantidadMinutosDesde;
        this.cantidadMinutosHasta = cantidadMinutosHasta;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCantidadMinutosDesde() {
        return cantidadMinutosDesde;
    }

    public void setCantidadMinutosDesde(int cantidadMinutosDesde) {
        this.cantidadMinutosDesde = cantidadMinutosDesde;
    }

    public int getCantidadMinutosHasta() {
        return cantidadMinutosHasta;
    }

    public void setCantidadMinutosHasta(int cantidadMinutosHasta) {
        this.cantidadMinutosHasta = cantidadMinutosHasta;
    }

    public Double getPorcentajeMulta() {
        return porcentajeMulta;
    }

    public void setPorcentajeMulta(Double porcentajeMulta) {
        this.porcentajeMulta = porcentajeMulta;
    }

    public String getTipoEscala() {
        return tipoEscala;
    }

    public void setTipoEscala(String tipoEscala) {
        this.tipoEscala = tipoEscala;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EscalaMultas)) {
            return false;
        }
        EscalaMultas other = (EscalaMultas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.mca.sisrrhh.entitys.EscalaMultas[ id=" + id + " ]";
    }
    
}
