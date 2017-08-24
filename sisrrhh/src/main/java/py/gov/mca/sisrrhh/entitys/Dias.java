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
@Table(name = "dias")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Dias.findAll", query = "SELECT d FROM Dias d"),
    @NamedQuery(name = "Dias.findByCodigo", query = "SELECT d FROM Dias d WHERE d.codigo = :codigo"),
    @NamedQuery(name = "Dias.findByDescripcion", query = "SELECT d FROM Dias d WHERE d.descripcion = :descripcion"),
    @NamedQuery(name = "Dias.findByNumeroDia", query = "SELECT d FROM Dias d WHERE d.numeroDia = :numeroDia")})
public class Dias implements Serializable {

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
    @Basic(optional = false)
    @NotNull
    @Column(name = "numero_dia")
    private int numeroDia;

    public Dias() {
    }

    public Dias(String codigo) {
        this.codigo = codigo;
    }

    public Dias(String codigo, String descripcion, int numeroDia) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.numeroDia = numeroDia;
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

    public int getNumeroDia() {
        return numeroDia;
    }

    public void setNumeroDia(int numeroDia) {
        this.numeroDia = numeroDia;
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
        if (!(object instanceof Dias)) {
            return false;
        }
        Dias other = (Dias) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.mca.sisrrhh.entitys.Dias[ codigo=" + codigo + " ]";
    }
    
}
