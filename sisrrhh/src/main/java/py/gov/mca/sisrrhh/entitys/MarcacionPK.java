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
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author vinsfran
 */
@Embeddable
public class MarcacionPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "cedula")
    private String cedula;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_marcacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraMarcacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "tipo_macacion")
    private String tipoMacacion;

    public MarcacionPK() {
    }

    public MarcacionPK(String cedula, Date fechaHoraMarcacion, String tipoMacacion) {
        this.cedula = cedula;
        this.fechaHoraMarcacion = fechaHoraMarcacion;
        this.tipoMacacion = tipoMacacion;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public Date getFechaHoraMarcacion() {
        return fechaHoraMarcacion;
    }

    public void setFechaHoraMarcacion(Date fechaHoraMarcacion) {
        this.fechaHoraMarcacion = fechaHoraMarcacion;
    }

    public String getTipoMacacion() {
        return tipoMacacion;
    }

    public void setTipoMacacion(String tipoMacacion) {
        this.tipoMacacion = tipoMacacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cedula != null ? cedula.hashCode() : 0);
        hash += (fechaHoraMarcacion != null ? fechaHoraMarcacion.hashCode() : 0);
        hash += (tipoMacacion != null ? tipoMacacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MarcacionPK)) {
            return false;
        }
        MarcacionPK other = (MarcacionPK) object;
        if ((this.cedula == null && other.cedula != null) || (this.cedula != null && !this.cedula.equals(other.cedula))) {
            return false;
        }
        if ((this.fechaHoraMarcacion == null && other.fechaHoraMarcacion != null) || (this.fechaHoraMarcacion != null && !this.fechaHoraMarcacion.equals(other.fechaHoraMarcacion))) {
            return false;
        }
        if ((this.tipoMacacion == null && other.tipoMacacion != null) || (this.tipoMacacion != null && !this.tipoMacacion.equals(other.tipoMacacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.mca.sisrrhh.entitys.MarcacionPK[ cedula=" + cedula + ", fechaHoraMarcacion=" + fechaHoraMarcacion + ", tipoMacacion=" + tipoMacacion + " ]";
    }
    
}
