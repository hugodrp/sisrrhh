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
public class HistorialCambiosAsignacionExcepcionPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "codigo_historial")
    private int codigoHistorial;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "codigo_excepcion")
    private String codigoExcepcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_asignacion")
    @Temporal(TemporalType.DATE)
    private Date fechaAsignacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "numero_documento_funcionario")
    private String numeroDocumentoFuncionario;

    public HistorialCambiosAsignacionExcepcionPK() {
    }

    public HistorialCambiosAsignacionExcepcionPK(int codigoHistorial, String codigoExcepcion, Date fechaAsignacion, String numeroDocumentoFuncionario) {
        this.codigoHistorial = codigoHistorial;
        this.codigoExcepcion = codigoExcepcion;
        this.fechaAsignacion = fechaAsignacion;
        this.numeroDocumentoFuncionario = numeroDocumentoFuncionario;
    }

    public int getCodigoHistorial() {
        return codigoHistorial;
    }

    public void setCodigoHistorial(int codigoHistorial) {
        this.codigoHistorial = codigoHistorial;
    }

    public String getCodigoExcepcion() {
        return codigoExcepcion;
    }

    public void setCodigoExcepcion(String codigoExcepcion) {
        this.codigoExcepcion = codigoExcepcion;
    }

    public Date getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(Date fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public String getNumeroDocumentoFuncionario() {
        return numeroDocumentoFuncionario;
    }

    public void setNumeroDocumentoFuncionario(String numeroDocumentoFuncionario) {
        this.numeroDocumentoFuncionario = numeroDocumentoFuncionario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codigoHistorial;
        hash += (codigoExcepcion != null ? codigoExcepcion.hashCode() : 0);
        hash += (fechaAsignacion != null ? fechaAsignacion.hashCode() : 0);
        hash += (numeroDocumentoFuncionario != null ? numeroDocumentoFuncionario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HistorialCambiosAsignacionExcepcionPK)) {
            return false;
        }
        HistorialCambiosAsignacionExcepcionPK other = (HistorialCambiosAsignacionExcepcionPK) object;
        if (this.codigoHistorial != other.codigoHistorial) {
            return false;
        }
        if ((this.codigoExcepcion == null && other.codigoExcepcion != null) || (this.codigoExcepcion != null && !this.codigoExcepcion.equals(other.codigoExcepcion))) {
            return false;
        }
        if ((this.fechaAsignacion == null && other.fechaAsignacion != null) || (this.fechaAsignacion != null && !this.fechaAsignacion.equals(other.fechaAsignacion))) {
            return false;
        }
        if ((this.numeroDocumentoFuncionario == null && other.numeroDocumentoFuncionario != null) || (this.numeroDocumentoFuncionario != null && !this.numeroDocumentoFuncionario.equals(other.numeroDocumentoFuncionario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.mca.sisrrhh.entitys.HistorialCambiosAsignacionExcepcionPK[ codigoHistorial=" + codigoHistorial + ", codigoExcepcion=" + codigoExcepcion + ", fechaAsignacion=" + fechaAsignacion + ", numeroDocumentoFuncionario=" + numeroDocumentoFuncionario + " ]";
    }
    
}
