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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
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
@Table(name = "historial_cambios_asignacion_excepcion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HistorialCambiosAsignacionExcepcion.findAll", query = "SELECT h FROM HistorialCambiosAsignacionExcepcion h"),
    @NamedQuery(name = "HistorialCambiosAsignacionExcepcion.findByCodigoHistorial", query = "SELECT h FROM HistorialCambiosAsignacionExcepcion h WHERE h.historialCambiosAsignacionExcepcionPK.codigoHistorial = :codigoHistorial"),
    @NamedQuery(name = "HistorialCambiosAsignacionExcepcion.findByDescripcionCambio", query = "SELECT h FROM HistorialCambiosAsignacionExcepcion h WHERE h.descripcionCambio = :descripcionCambio"),
    @NamedQuery(name = "HistorialCambiosAsignacionExcepcion.findByMotivoCambio", query = "SELECT h FROM HistorialCambiosAsignacionExcepcion h WHERE h.motivoCambio = :motivoCambio"),
    @NamedQuery(name = "HistorialCambiosAsignacionExcepcion.findByCodigoExcepcion", query = "SELECT h FROM HistorialCambiosAsignacionExcepcion h WHERE h.historialCambiosAsignacionExcepcionPK.codigoExcepcion = :codigoExcepcion"),
    @NamedQuery(name = "HistorialCambiosAsignacionExcepcion.findByFechaAsignacion", query = "SELECT h FROM HistorialCambiosAsignacionExcepcion h WHERE h.historialCambiosAsignacionExcepcionPK.fechaAsignacion = :fechaAsignacion"),
    @NamedQuery(name = "HistorialCambiosAsignacionExcepcion.findByNumeroDocumentoFuncionario", query = "SELECT h FROM HistorialCambiosAsignacionExcepcion h WHERE h.historialCambiosAsignacionExcepcionPK.numeroDocumentoFuncionario = :numeroDocumentoFuncionario"),
    @NamedQuery(name = "HistorialCambiosAsignacionExcepcion.findByFechaCambio", query = "SELECT h FROM HistorialCambiosAsignacionExcepcion h WHERE h.fechaCambio = :fechaCambio")})
public class HistorialCambiosAsignacionExcepcion implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected HistorialCambiosAsignacionExcepcionPK historialCambiosAsignacionExcepcionPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "descripcion_cambio")
    private String descripcionCambio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "motivo_cambio")
    private String motivoCambio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "fecha_cambio")
    private String fechaCambio;
    @JoinColumns({
        @JoinColumn(name = "codigo_excepcion", referencedColumnName = "codigo_excepcion", insertable = false, updatable = false),
        @JoinColumn(name = "fecha_asignacion", referencedColumnName = "fecha_asignacion", insertable = false, updatable = false),
        @JoinColumn(name = "numero_documento_funcionario", referencedColumnName = "numero_documento_funcionario", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private AsignacionExcepcionesHorariosDiarios asignacionExcepcionesHorariosDiarios;

    public HistorialCambiosAsignacionExcepcion() {
    }

    public HistorialCambiosAsignacionExcepcion(HistorialCambiosAsignacionExcepcionPK historialCambiosAsignacionExcepcionPK) {
        this.historialCambiosAsignacionExcepcionPK = historialCambiosAsignacionExcepcionPK;
    }

    public HistorialCambiosAsignacionExcepcion(HistorialCambiosAsignacionExcepcionPK historialCambiosAsignacionExcepcionPK, String descripcionCambio, String motivoCambio, String fechaCambio) {
        this.historialCambiosAsignacionExcepcionPK = historialCambiosAsignacionExcepcionPK;
        this.descripcionCambio = descripcionCambio;
        this.motivoCambio = motivoCambio;
        this.fechaCambio = fechaCambio;
    }

    public HistorialCambiosAsignacionExcepcion(int codigoHistorial, String codigoExcepcion, Date fechaAsignacion, String numeroDocumentoFuncionario) {
        this.historialCambiosAsignacionExcepcionPK = new HistorialCambiosAsignacionExcepcionPK(codigoHistorial, codigoExcepcion, fechaAsignacion, numeroDocumentoFuncionario);
    }

    public HistorialCambiosAsignacionExcepcionPK getHistorialCambiosAsignacionExcepcionPK() {
        return historialCambiosAsignacionExcepcionPK;
    }

    public void setHistorialCambiosAsignacionExcepcionPK(HistorialCambiosAsignacionExcepcionPK historialCambiosAsignacionExcepcionPK) {
        this.historialCambiosAsignacionExcepcionPK = historialCambiosAsignacionExcepcionPK;
    }

    public String getDescripcionCambio() {
        return descripcionCambio;
    }

    public void setDescripcionCambio(String descripcionCambio) {
        this.descripcionCambio = descripcionCambio;
    }

    public String getMotivoCambio() {
        return motivoCambio;
    }

    public void setMotivoCambio(String motivoCambio) {
        this.motivoCambio = motivoCambio;
    }

    public String getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(String fechaCambio) {
        this.fechaCambio = fechaCambio;
    }

    public AsignacionExcepcionesHorariosDiarios getAsignacionExcepcionesHorariosDiarios() {
        return asignacionExcepcionesHorariosDiarios;
    }

    public void setAsignacionExcepcionesHorariosDiarios(AsignacionExcepcionesHorariosDiarios asignacionExcepcionesHorariosDiarios) {
        this.asignacionExcepcionesHorariosDiarios = asignacionExcepcionesHorariosDiarios;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (historialCambiosAsignacionExcepcionPK != null ? historialCambiosAsignacionExcepcionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HistorialCambiosAsignacionExcepcion)) {
            return false;
        }
        HistorialCambiosAsignacionExcepcion other = (HistorialCambiosAsignacionExcepcion) object;
        if ((this.historialCambiosAsignacionExcepcionPK == null && other.historialCambiosAsignacionExcepcionPK != null) || (this.historialCambiosAsignacionExcepcionPK != null && !this.historialCambiosAsignacionExcepcionPK.equals(other.historialCambiosAsignacionExcepcionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.mca.sisrrhh.entitys.HistorialCambiosAsignacionExcepcion[ historialCambiosAsignacionExcepcionPK=" + historialCambiosAsignacionExcepcionPK + " ]";
    }
    
}
