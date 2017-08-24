/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.mca.sisrrhh.entitys;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author vinsfran
 */
@Entity
@Table(name = "tipos_excepciones")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TiposExcepciones.findAll", query = "SELECT t FROM TiposExcepciones t"),
    @NamedQuery(name = "TiposExcepciones.findByCodigoExcepcion", query = "SELECT t FROM TiposExcepciones t WHERE t.codigoExcepcion = :codigoExcepcion"),
    @NamedQuery(name = "TiposExcepciones.findByDescripcionExcepcion", query = "SELECT t FROM TiposExcepciones t WHERE t.descripcionExcepcion = :descripcionExcepcion")})
public class TiposExcepciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "codigo_excepcion")
    private String codigoExcepcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "descripcion_excepcion")
    private String descripcionExcepcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tiposExcepciones")
    private List<AsignacionExcepcionesHorariosDiarios> asignacionExcepcionesHorariosDiariosList;
    @OneToMany(mappedBy = "codigoExcepcion")
    private List<FichaCalendario> fichaCalendarioList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codigoExcepcion")
    private List<FechasEspeciales> fechasEspecialesList;

    public TiposExcepciones() {
    }

    public TiposExcepciones(String codigoExcepcion) {
        this.codigoExcepcion = codigoExcepcion;
    }

    public TiposExcepciones(String codigoExcepcion, String descripcionExcepcion) {
        this.codigoExcepcion = codigoExcepcion;
        this.descripcionExcepcion = descripcionExcepcion;
    }

    public String getCodigoExcepcion() {
        return codigoExcepcion;
    }

    public void setCodigoExcepcion(String codigoExcepcion) {
        this.codigoExcepcion = codigoExcepcion;
    }

    public String getDescripcionExcepcion() {
        return descripcionExcepcion;
    }

    public void setDescripcionExcepcion(String descripcionExcepcion) {
        this.descripcionExcepcion = descripcionExcepcion;
    }

    @XmlTransient
    public List<AsignacionExcepcionesHorariosDiarios> getAsignacionExcepcionesHorariosDiariosList() {
        return asignacionExcepcionesHorariosDiariosList;
    }

    public void setAsignacionExcepcionesHorariosDiariosList(List<AsignacionExcepcionesHorariosDiarios> asignacionExcepcionesHorariosDiariosList) {
        this.asignacionExcepcionesHorariosDiariosList = asignacionExcepcionesHorariosDiariosList;
    }

    @XmlTransient
    public List<FichaCalendario> getFichaCalendarioList() {
        return fichaCalendarioList;
    }

    public void setFichaCalendarioList(List<FichaCalendario> fichaCalendarioList) {
        this.fichaCalendarioList = fichaCalendarioList;
    }

    @XmlTransient
    public List<FechasEspeciales> getFechasEspecialesList() {
        return fechasEspecialesList;
    }

    public void setFechasEspecialesList(List<FechasEspeciales> fechasEspecialesList) {
        this.fechasEspecialesList = fechasEspecialesList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigoExcepcion != null ? codigoExcepcion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TiposExcepciones)) {
            return false;
        }
        TiposExcepciones other = (TiposExcepciones) object;
        if ((this.codigoExcepcion == null && other.codigoExcepcion != null) || (this.codigoExcepcion != null && !this.codigoExcepcion.equals(other.codigoExcepcion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.mca.sisrrhh.entitys.TiposExcepciones[ codigoExcepcion=" + codigoExcepcion + " ]";
    }
    
}
