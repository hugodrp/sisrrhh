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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "auditoria")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Auditoria.findAll", query = "SELECT a FROM Auditoria a"),
    @NamedQuery(name = "Auditoria.findByCodigo", query = "SELECT a FROM Auditoria a WHERE a.codigo = :codigo"),
    @NamedQuery(name = "Auditoria.findByFecha", query = "SELECT a FROM Auditoria a WHERE a.fecha = :fecha"),
    @NamedQuery(name = "Auditoria.findByUsuario", query = "SELECT a FROM Auditoria a WHERE a.usuario = :usuario"),
    @NamedQuery(name = "Auditoria.findByRolUsuario", query = "SELECT a FROM Auditoria a WHERE a.rolUsuario = :rolUsuario"),
    @NamedQuery(name = "Auditoria.findByTipoMovimiento", query = "SELECT a FROM Auditoria a WHERE a.tipoMovimiento = :tipoMovimiento"),
    @NamedQuery(name = "Auditoria.findByDescripcion", query = "SELECT a FROM Auditoria a WHERE a.descripcion = :descripcion"),
    @NamedQuery(name = "Auditoria.findByNombreTabla", query = "SELECT a FROM Auditoria a WHERE a.nombreTabla = :nombreTabla"),
    @NamedQuery(name = "Auditoria.findByClaveTabla", query = "SELECT a FROM Auditoria a WHERE a.claveTabla = :claveTabla"),
    @NamedQuery(name = "Auditoria.findByAux3", query = "SELECT a FROM Auditoria a WHERE a.aux3 = :aux3")})
public class Auditoria implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codigo")
    private Integer codigo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "usuario")
    private String usuario;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "rol_usuario")
    private String rolUsuario;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "tipo_movimiento")
    private String tipoMovimiento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "nombre_tabla")
    private String nombreTabla;
    @Size(max = 45)
    @Column(name = "clave_tabla")
    private String claveTabla;
    @Size(max = 45)
    @Column(name = "aux_3")
    private String aux3;

    public Auditoria() {
    }

    public Auditoria(Integer codigo) {
        this.codigo = codigo;
    }

    public Auditoria(Integer codigo, Date fecha, String usuario, String rolUsuario, String tipoMovimiento, String descripcion, String nombreTabla) {
        this.codigo = codigo;
        this.fecha = fecha;
        this.usuario = usuario;
        this.rolUsuario = rolUsuario;
        this.tipoMovimiento = tipoMovimiento;
        this.descripcion = descripcion;
        this.nombreTabla = nombreTabla;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getRolUsuario() {
        return rolUsuario;
    }

    public void setRolUsuario(String rolUsuario) {
        this.rolUsuario = rolUsuario;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombreTabla() {
        return nombreTabla;
    }

    public void setNombreTabla(String nombreTabla) {
        this.nombreTabla = nombreTabla;
    }

    public String getClaveTabla() {
        return claveTabla;
    }

    public void setClaveTabla(String claveTabla) {
        this.claveTabla = claveTabla;
    }

    public String getAux3() {
        return aux3;
    }

    public void setAux3(String aux3) {
        this.aux3 = aux3;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Auditoria)) {
            return false;
        }
        Auditoria other = (Auditoria) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.mca.sisrrhh.entitys.Auditoria[ codigo=" + codigo + " ]";
    }
    
}
