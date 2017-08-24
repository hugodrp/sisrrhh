/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.mca.sisrrhh.entitys;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "asignacion_excepciones_horarios_diarios")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AsignacionExcepcionesHorariosDiarios.findAll", query = "SELECT a FROM AsignacionExcepcionesHorariosDiarios a"),
    @NamedQuery(name = "AsignacionExcepcionesHorariosDiarios.findByCodigoExcepcion", query = "SELECT a FROM AsignacionExcepcionesHorariosDiarios a WHERE a.asignacionExcepcionesHorariosDiariosPK.codigoExcepcion = :codigoExcepcion"),
    @NamedQuery(name = "AsignacionExcepcionesHorariosDiarios.findByFechaAsignacion", query = "SELECT a FROM AsignacionExcepcionesHorariosDiarios a WHERE a.asignacionExcepcionesHorariosDiariosPK.fechaAsignacion = :fechaAsignacion"),
    @NamedQuery(name = "AsignacionExcepcionesHorariosDiarios.findByNumeroDocumentoFuncionario", query = "SELECT a FROM AsignacionExcepcionesHorariosDiarios a WHERE a.asignacionExcepcionesHorariosDiariosPK.numeroDocumentoFuncionario = :numeroDocumentoFuncionario"),
    @NamedQuery(name = "AsignacionExcepcionesHorariosDiarios.findByMesAsignacion", query = "SELECT a FROM AsignacionExcepcionesHorariosDiarios a WHERE a.mesAsignacion = :mesAsignacion"),
    @NamedQuery(name = "AsignacionExcepcionesHorariosDiarios.findByAnioAsignacion", query = "SELECT a FROM AsignacionExcepcionesHorariosDiarios a WHERE a.anioAsignacion = :anioAsignacion"),
    @NamedQuery(name = "AsignacionExcepcionesHorariosDiarios.findByDescripcionAsignacion", query = "SELECT a FROM AsignacionExcepcionesHorariosDiarios a WHERE a.descripcionAsignacion = :descripcionAsignacion")})
public class AsignacionExcepcionesHorariosDiarios implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AsignacionExcepcionesHorariosDiariosPK asignacionExcepcionesHorariosDiariosPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mes_asignacion")
    private int mesAsignacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "anio_asignacion")
    private int anioAsignacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "descripcion_asignacion")
    private String descripcionAsignacion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "asignacionExcepcionesHorariosDiarios")
    private List<HistorialCambiosAsignacionExcepcion> historialCambiosAsignacionExcepcionList;
    @JoinColumn(name = "estado_horario_asignado", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Estados estadoHorarioAsignado;
    @JoinColumn(name = "codigo_excepcion", referencedColumnName = "codigo_excepcion", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private TiposExcepciones tiposExcepciones;
    @JoinColumn(name = "numero_documento_funcionario", referencedColumnName = "numero_documento", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Funcionarios funcionarios;
    @JoinColumn(name = "horario_asignado", referencedColumnName = "horario")
    @ManyToOne(optional = false)
    private Horarios horarioAsignado;

    public AsignacionExcepcionesHorariosDiarios() {
    }

    public AsignacionExcepcionesHorariosDiarios(AsignacionExcepcionesHorariosDiariosPK asignacionExcepcionesHorariosDiariosPK) {
        this.asignacionExcepcionesHorariosDiariosPK = asignacionExcepcionesHorariosDiariosPK;
    }

    public AsignacionExcepcionesHorariosDiarios(AsignacionExcepcionesHorariosDiariosPK asignacionExcepcionesHorariosDiariosPK, int mesAsignacion, int anioAsignacion, String descripcionAsignacion) {
        this.asignacionExcepcionesHorariosDiariosPK = asignacionExcepcionesHorariosDiariosPK;
        this.mesAsignacion = mesAsignacion;
        this.anioAsignacion = anioAsignacion;
        this.descripcionAsignacion = descripcionAsignacion;
    }

    public AsignacionExcepcionesHorariosDiarios(String codigoExcepcion, Date fechaAsignacion, String numeroDocumentoFuncionario) {
        this.asignacionExcepcionesHorariosDiariosPK = new AsignacionExcepcionesHorariosDiariosPK(codigoExcepcion, fechaAsignacion, numeroDocumentoFuncionario);
    }

    public AsignacionExcepcionesHorariosDiariosPK getAsignacionExcepcionesHorariosDiariosPK() {
        return asignacionExcepcionesHorariosDiariosPK;
    }

    public void setAsignacionExcepcionesHorariosDiariosPK(AsignacionExcepcionesHorariosDiariosPK asignacionExcepcionesHorariosDiariosPK) {
        this.asignacionExcepcionesHorariosDiariosPK = asignacionExcepcionesHorariosDiariosPK;
    }

    public int getMesAsignacion() {
        return mesAsignacion;
    }

    public void setMesAsignacion(int mesAsignacion) {
        this.mesAsignacion = mesAsignacion;
    }

    public int getAnioAsignacion() {
        return anioAsignacion;
    }

    public void setAnioAsignacion(int anioAsignacion) {
        this.anioAsignacion = anioAsignacion;
    }

    public String getDescripcionAsignacion() {
        return descripcionAsignacion;
    }

    public void setDescripcionAsignacion(String descripcionAsignacion) {
        this.descripcionAsignacion = descripcionAsignacion;
    }

    @XmlTransient
    public List<HistorialCambiosAsignacionExcepcion> getHistorialCambiosAsignacionExcepcionList() {
        return historialCambiosAsignacionExcepcionList;
    }

    public void setHistorialCambiosAsignacionExcepcionList(List<HistorialCambiosAsignacionExcepcion> historialCambiosAsignacionExcepcionList) {
        this.historialCambiosAsignacionExcepcionList = historialCambiosAsignacionExcepcionList;
    }

    public Estados getEstadoHorarioAsignado() {
        return estadoHorarioAsignado;
    }

    public void setEstadoHorarioAsignado(Estados estadoHorarioAsignado) {
        this.estadoHorarioAsignado = estadoHorarioAsignado;
    }

    public TiposExcepciones getTiposExcepciones() {
        return tiposExcepciones;
    }

    public void setTiposExcepciones(TiposExcepciones tiposExcepciones) {
        this.tiposExcepciones = tiposExcepciones;
    }

    public Funcionarios getFuncionarios() {
        return funcionarios;
    }

    public void setFuncionarios(Funcionarios funcionarios) {
        this.funcionarios = funcionarios;
    }

    public Horarios getHorarioAsignado() {
        return horarioAsignado;
    }

    public void setHorarioAsignado(Horarios horarioAsignado) {
        this.horarioAsignado = horarioAsignado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (asignacionExcepcionesHorariosDiariosPK != null ? asignacionExcepcionesHorariosDiariosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AsignacionExcepcionesHorariosDiarios)) {
            return false;
        }
        AsignacionExcepcionesHorariosDiarios other = (AsignacionExcepcionesHorariosDiarios) object;
        if ((this.asignacionExcepcionesHorariosDiariosPK == null && other.asignacionExcepcionesHorariosDiariosPK != null) || (this.asignacionExcepcionesHorariosDiariosPK != null && !this.asignacionExcepcionesHorariosDiariosPK.equals(other.asignacionExcepcionesHorariosDiariosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.mca.sisrrhh.entitys.AsignacionExcepcionesHorariosDiarios[ asignacionExcepcionesHorariosDiariosPK=" + asignacionExcepcionesHorariosDiariosPK + " ]";
    }
    
}
