/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.mca.sisrrhh.entitys;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author vinsfran
 */
@Entity
@Table(name = "marcacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Marcacion.findAll", query = "SELECT m FROM Marcacion m"),
    @NamedQuery(name = "Marcacion.findByCedula", query = "SELECT m FROM Marcacion m WHERE m.marcacionPK.cedula = :cedula"),
    @NamedQuery(name = "Marcacion.findByFechaHoraMarcacion", query = "SELECT m FROM Marcacion m WHERE m.marcacionPK.fechaHoraMarcacion = :fechaHoraMarcacion"),
    @NamedQuery(name = "Marcacion.findByFechaMarcacionChar", query = "SELECT m FROM Marcacion m WHERE m.fechaMarcacionChar = :fechaMarcacionChar"),
    @NamedQuery(name = "Marcacion.findByHoraMarcacionChar", query = "SELECT m FROM Marcacion m WHERE m.horaMarcacionChar = :horaMarcacionChar"),
    @NamedQuery(name = "Marcacion.findByDiaMarcacion", query = "SELECT m FROM Marcacion m WHERE m.diaMarcacion = :diaMarcacion"),
    @NamedQuery(name = "Marcacion.findByTipoMacacion", query = "SELECT m FROM Marcacion m WHERE m.marcacionPK.tipoMacacion = :tipoMacacion"),
    @NamedQuery(name = "Marcacion.findByFormaMarcacion", query = "SELECT m FROM Marcacion m WHERE m.formaMarcacion = :formaMarcacion"),
    @NamedQuery(name = "Marcacion.findByNombreArchivo", query = "SELECT m FROM Marcacion m WHERE m.nombreArchivo = :nombreArchivo")})
public class Marcacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MarcacionPK marcacionPK;
    @Column(name = "fecha_marcacion_char")
    @Temporal(TemporalType.DATE)
    private Date fechaMarcacionChar;
    @Column(name = "hora_marcacion_char")
    @Temporal(TemporalType.TIME)
    private Date horaMarcacionChar;
    @Size(max = 20)
    @Column(name = "dia_marcacion")
    private String diaMarcacion;
    @Size(max = 20)
    @Column(name = "forma_marcacion")
    private String formaMarcacion;
    @Size(max = 45)
    @Column(name = "nombre_archivo")
    private String nombreArchivo;

    public Marcacion() {
    }

    public Marcacion(MarcacionPK marcacionPK) {
        this.marcacionPK = marcacionPK;
    }

    public Marcacion(String cedula, Date fechaHoraMarcacion, String tipoMacacion) {
        this.marcacionPK = new MarcacionPK(cedula, fechaHoraMarcacion, tipoMacacion);
    }

    public MarcacionPK getMarcacionPK() {
        return marcacionPK;
    }

    public void setMarcacionPK(MarcacionPK marcacionPK) {
        this.marcacionPK = marcacionPK;
    }

    public Date getFechaMarcacionChar() {
        return fechaMarcacionChar;
    }

    public void setFechaMarcacionChar(Date fechaMarcacionChar) {
        this.fechaMarcacionChar = fechaMarcacionChar;
    }

    public Date getHoraMarcacionChar() {
        return horaMarcacionChar;
    }

    public void setHoraMarcacionChar(Date horaMarcacionChar) {
        this.horaMarcacionChar = horaMarcacionChar;
    }

    public String getDiaMarcacion() {
        return diaMarcacion;
    }

    public void setDiaMarcacion(String diaMarcacion) {
        this.diaMarcacion = diaMarcacion;
    }

    public String getFormaMarcacion() {
        return formaMarcacion;
    }

    public void setFormaMarcacion(String formaMarcacion) {
        this.formaMarcacion = formaMarcacion;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (marcacionPK != null ? marcacionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Marcacion)) {
            return false;
        }
        Marcacion other = (Marcacion) object;
        if ((this.marcacionPK == null && other.marcacionPK != null) || (this.marcacionPK != null && !this.marcacionPK.equals(other.marcacionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.mca.sisrrhh.entitys.Marcacion[ marcacionPK=" + marcacionPK + " ]";
    }
    
}
