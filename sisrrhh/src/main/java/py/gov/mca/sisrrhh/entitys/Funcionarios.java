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
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author vinsfran
 */
@Entity
@Table(name = "funcionarios")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Funcionarios.findAll", query = "SELECT f FROM Funcionarios f"),
    @NamedQuery(name = "Funcionarios.findByNumeroDocumento", query = "SELECT f FROM Funcionarios f WHERE f.numeroDocumento = :numeroDocumento"),
    @NamedQuery(name = "Funcionarios.findByNombres", query = "SELECT f FROM Funcionarios f WHERE f.nombres = :nombres"),
    @NamedQuery(name = "Funcionarios.findByApellidos", query = "SELECT f FROM Funcionarios f WHERE f.apellidos = :apellidos"),
    @NamedQuery(name = "Funcionarios.findByFechaIngreso", query = "SELECT f FROM Funcionarios f WHERE f.fechaIngreso = :fechaIngreso"),
    @NamedQuery(name = "Funcionarios.findByUsuario", query = "SELECT f FROM Funcionarios f WHERE f.usuario = :usuario"),
    @NamedQuery(name = "Funcionarios.findByContrasena", query = "SELECT f FROM Funcionarios f WHERE f.contrasena = :contrasena"),
    @NamedQuery(name = "Funcionarios.findByTelefono1", query = "SELECT f FROM Funcionarios f WHERE f.telefono1 = :telefono1"),
    @NamedQuery(name = "Funcionarios.findByTelefono2", query = "SELECT f FROM Funcionarios f WHERE f.telefono2 = :telefono2"),
    @NamedQuery(name = "Funcionarios.findByCorreo1", query = "SELECT f FROM Funcionarios f WHERE f.correo1 = :correo1"),
    @NamedQuery(name = "Funcionarios.findByCorreo2", query = "SELECT f FROM Funcionarios f WHERE f.correo2 = :correo2"),
    @NamedQuery(name = "Funcionarios.findByDireccion", query = "SELECT f FROM Funcionarios f WHERE f.direccion = :direccion"),
    @NamedQuery(name = "Funcionarios.findBySueldoDiario", query = "SELECT f FROM Funcionarios f WHERE f.sueldoDiario = :sueldoDiario")})
public class Funcionarios implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "numero_documento")
    private String numeroDocumento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "nombres")
    private String nombres;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "apellidos")
    private String apellidos;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.DATE)
    private Date fechaIngreso;
    @Size(max = 20)
    @Column(name = "usuario")
    private String usuario;
    @Size(max = 100)
    @Column(name = "contrasena")
    private String contrasena;
    @Size(max = 45)
    @Column(name = "telefono_1")
    private String telefono1;
    @Size(max = 45)
    @Column(name = "telefono_2")
    private String telefono2;
    @Size(max = 45)
    @Column(name = "correo_1")
    private String correo1;
    @Size(max = 45)
    @Column(name = "correo_2")
    private String correo2;
    @Size(max = 45)
    @Column(name = "direccion")
    private String direccion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "sueldo_diario")
    private Double sueldoDiario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "funcionarios")
    private List<AsignacionExcepcionesHorariosDiarios> asignacionExcepcionesHorariosDiariosList;
    @JoinColumn(name = "comentarios_id", referencedColumnName = "id")
    @ManyToOne
    private Comentarios comentariosId;
    @JoinColumn(name = "estado_funcionario", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Estados estadoFuncionario;
    @JoinColumn(name = "horario_normal", referencedColumnName = "horario")
    @ManyToOne(optional = false)
    private Horarios horarioNormal;
    @JoinColumn(name = "relacion_laboral", referencedColumnName = "relacion_laboral")
    @ManyToOne(optional = false)
    private RelacionLaboral relacionLaboral;
    @JoinColumn(name = "rol_usuario", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Roles rolUsuario;
    @JoinColumn(name = "ubicaciones_fisicas_codigo", referencedColumnName = "codigo")
    @ManyToOne
    private UbicacionesFisicas ubicacionesFisicasCodigo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "funcionarios")
    private List<FichaCalendario> fichaCalendarioList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "funcionarios")
    private List<VacacionesFuncionarios> vacacionesFuncionariosList;

    public Funcionarios() {
    }

    public Funcionarios(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public Funcionarios(String numeroDocumento, String nombres, String apellidos) {
        this.numeroDocumento = numeroDocumento;
        this.nombres = nombres;
        this.apellidos = apellidos;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getTelefono1() {
        return telefono1;
    }

    public void setTelefono1(String telefono1) {
        this.telefono1 = telefono1;
    }

    public String getTelefono2() {
        return telefono2;
    }

    public void setTelefono2(String telefono2) {
        this.telefono2 = telefono2;
    }

    public String getCorreo1() {
        return correo1;
    }

    public void setCorreo1(String correo1) {
        this.correo1 = correo1;
    }

    public String getCorreo2() {
        return correo2;
    }

    public void setCorreo2(String correo2) {
        this.correo2 = correo2;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Double getSueldoDiario() {
        return sueldoDiario;
    }

    public void setSueldoDiario(Double sueldoDiario) {
        this.sueldoDiario = sueldoDiario;
    }

    @XmlTransient
    public List<AsignacionExcepcionesHorariosDiarios> getAsignacionExcepcionesHorariosDiariosList() {
        return asignacionExcepcionesHorariosDiariosList;
    }

    public void setAsignacionExcepcionesHorariosDiariosList(List<AsignacionExcepcionesHorariosDiarios> asignacionExcepcionesHorariosDiariosList) {
        this.asignacionExcepcionesHorariosDiariosList = asignacionExcepcionesHorariosDiariosList;
    }

    public Comentarios getComentariosId() {
        return comentariosId;
    }

    public void setComentariosId(Comentarios comentariosId) {
        this.comentariosId = comentariosId;
    }

    public Estados getEstadoFuncionario() {
        return estadoFuncionario;
    }

    public void setEstadoFuncionario(Estados estadoFuncionario) {
        this.estadoFuncionario = estadoFuncionario;
    }

    public Horarios getHorarioNormal() {
        return horarioNormal;
    }

    public void setHorarioNormal(Horarios horarioNormal) {
        this.horarioNormal = horarioNormal;
    }

    public RelacionLaboral getRelacionLaboral() {
        return relacionLaboral;
    }

    public void setRelacionLaboral(RelacionLaboral relacionLaboral) {
        this.relacionLaboral = relacionLaboral;
    }

    public Roles getRolUsuario() {
        return rolUsuario;
    }

    public void setRolUsuario(Roles rolUsuario) {
        this.rolUsuario = rolUsuario;
    }

    public UbicacionesFisicas getUbicacionesFisicasCodigo() {
        return ubicacionesFisicasCodigo;
    }

    public void setUbicacionesFisicasCodigo(UbicacionesFisicas ubicacionesFisicasCodigo) {
        this.ubicacionesFisicasCodigo = ubicacionesFisicasCodigo;
    }

    @XmlTransient
    public List<FichaCalendario> getFichaCalendarioList() {
        return fichaCalendarioList;
    }

    public void setFichaCalendarioList(List<FichaCalendario> fichaCalendarioList) {
        this.fichaCalendarioList = fichaCalendarioList;
    }

    @XmlTransient
    public List<VacacionesFuncionarios> getVacacionesFuncionariosList() {
        return vacacionesFuncionariosList;
    }

    public void setVacacionesFuncionariosList(List<VacacionesFuncionarios> vacacionesFuncionariosList) {
        this.vacacionesFuncionariosList = vacacionesFuncionariosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numeroDocumento != null ? numeroDocumento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Funcionarios)) {
            return false;
        }
        Funcionarios other = (Funcionarios) object;
        if ((this.numeroDocumento == null && other.numeroDocumento != null) || (this.numeroDocumento != null && !this.numeroDocumento.equals(other.numeroDocumento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.mca.sisrrhh.entitys.Funcionarios[ numeroDocumento=" + numeroDocumento + " ]";
    }
    
}
