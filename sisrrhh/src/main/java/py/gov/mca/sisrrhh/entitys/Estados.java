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
@Table(name = "estados")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Estados.findAll", query = "SELECT e FROM Estados e"),
    @NamedQuery(name = "Estados.findByCodigo", query = "SELECT e FROM Estados e WHERE e.codigo = :codigo"),
    @NamedQuery(name = "Estados.findByDescripcion", query = "SELECT e FROM Estados e WHERE e.descripcion = :descripcion"),
    @NamedQuery(name = "Estados.findByAux1", query = "SELECT e FROM Estados e WHERE e.aux1 = :aux1"),
    @NamedQuery(name = "Estados.findByAux2", query = "SELECT e FROM Estados e WHERE e.aux2 = :aux2")})
public class Estados implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "codigo")
    private String codigo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "descripcion")
    private String descripcion;
    @Size(max = 45)
    @Column(name = "aux_1")
    private String aux1;
    @Size(max = 45)
    @Column(name = "aux_2")
    private String aux2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estadoRol")
    private List<Roles> rolesList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estadoHorarioAsignado")
    private List<AsignacionExcepcionesHorariosDiarios> asignacionExcepcionesHorariosDiariosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estadoFuncionario")
    private List<Funcionarios> funcionariosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estadoVacacion")
    private List<VacacionesFuncionarios> vacacionesFuncionariosList;

    public Estados() {
    }

    public Estados(String codigo) {
        this.codigo = codigo;
    }

    public Estados(String codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    @XmlTransient
    public List<Roles> getRolesList() {
        return rolesList;
    }

    public void setRolesList(List<Roles> rolesList) {
        this.rolesList = rolesList;
    }

    @XmlTransient
    public List<AsignacionExcepcionesHorariosDiarios> getAsignacionExcepcionesHorariosDiariosList() {
        return asignacionExcepcionesHorariosDiariosList;
    }

    public void setAsignacionExcepcionesHorariosDiariosList(List<AsignacionExcepcionesHorariosDiarios> asignacionExcepcionesHorariosDiariosList) {
        this.asignacionExcepcionesHorariosDiariosList = asignacionExcepcionesHorariosDiariosList;
    }

    @XmlTransient
    public List<Funcionarios> getFuncionariosList() {
        return funcionariosList;
    }

    public void setFuncionariosList(List<Funcionarios> funcionariosList) {
        this.funcionariosList = funcionariosList;
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
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Estados)) {
            return false;
        }
        Estados other = (Estados) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.mca.sisrrhh.entitys.Estados[ codigo=" + codigo + " ]";
    }
    
}
