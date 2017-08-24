/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.mca.sisrrhh.entitys;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author vinsfran
 */
@Entity
@Table(name = "fechas_especiales")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FechasEspeciales.findAll", query = "SELECT f FROM FechasEspeciales f"),
    @NamedQuery(name = "FechasEspeciales.findByFechaEspecial", query = "SELECT f FROM FechasEspeciales f WHERE f.fechaEspecial = :fechaEspecial"),
    @NamedQuery(name = "FechasEspeciales.findByDescripcionFechaEspecial", query = "SELECT f FROM FechasEspeciales f WHERE f.descripcionFechaEspecial = :descripcionFechaEspecial"),
    @NamedQuery(name = "FechasEspeciales.findByAux1", query = "SELECT f FROM FechasEspeciales f WHERE f.aux1 = :aux1"),
    @NamedQuery(name = "FechasEspeciales.findByAux2", query = "SELECT f FROM FechasEspeciales f WHERE f.aux2 = :aux2"),
    @NamedQuery(name = "FechasEspeciales.findByFechasEspecialescol", query = "SELECT f FROM FechasEspeciales f WHERE f.fechasEspecialescol = :fechasEspecialescol")})
public class FechasEspeciales implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_especial")
    @Temporal(TemporalType.DATE)
    private Date fechaEspecial;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "descripcion_fecha_especial")
    private String descripcionFechaEspecial;
    @Size(max = 45)
    @Column(name = "aux_1")
    private String aux1;
    @Size(max = 45)
    @Column(name = "aux_2")
    private String aux2;
    @Size(max = 45)
    @Column(name = "fechas_especialescol")
    private String fechasEspecialescol;
    @JoinColumn(name = "codigo_excepcion", referencedColumnName = "codigo_excepcion")
    @ManyToOne(optional = false)
    private TiposExcepciones codigoExcepcion;

    public FechasEspeciales() {
    }

    public FechasEspeciales(Date fechaEspecial) {
        this.fechaEspecial = fechaEspecial;
    }

    public FechasEspeciales(Date fechaEspecial, String descripcionFechaEspecial) {
        this.fechaEspecial = fechaEspecial;
        this.descripcionFechaEspecial = descripcionFechaEspecial;
    }

    public Date getFechaEspecial() {
        return fechaEspecial;
    }

    public void setFechaEspecial(Date fechaEspecial) {
        this.fechaEspecial = fechaEspecial;
    }

    public String getDescripcionFechaEspecial() {
        return descripcionFechaEspecial;
    }

    public void setDescripcionFechaEspecial(String descripcionFechaEspecial) {
        this.descripcionFechaEspecial = descripcionFechaEspecial;
    }

    public String getAux1() {
        return aux1;
    }

    public void setAux1(String aux1) {
        this.aux1 = aux1;
    }

    public String getAux2() {
        return aux2;
    }

    public void setAux2(String aux2) {
        this.aux2 = aux2;
    }

    public String getFechasEspecialescol() {
        return fechasEspecialescol;
    }

    public void setFechasEspecialescol(String fechasEspecialescol) {
        this.fechasEspecialescol = fechasEspecialescol;
    }

    public TiposExcepciones getCodigoExcepcion() {
        return codigoExcepcion;
    }

    public void setCodigoExcepcion(TiposExcepciones codigoExcepcion) {
        this.codigoExcepcion = codigoExcepcion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fechaEspecial != null ? fechaEspecial.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FechasEspeciales)) {
            return false;
        }
        FechasEspeciales other = (FechasEspeciales) object;
        if ((this.fechaEspecial == null && other.fechaEspecial != null) || (this.fechaEspecial != null && !this.fechaEspecial.equals(other.fechaEspecial))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.mca.sisrrhh.entitys.FechasEspeciales[ fechaEspecial=" + fechaEspecial + " ]";
    }
    
}
