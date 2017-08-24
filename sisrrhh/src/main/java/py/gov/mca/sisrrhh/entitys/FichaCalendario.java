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
import javax.persistence.ManyToOne;
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
@Table(name = "ficha_calendario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FichaCalendario.findAll", query = "SELECT f FROM FichaCalendario f"),
    @NamedQuery(name = "FichaCalendario.findByFechaCalendario", query = "SELECT f FROM FichaCalendario f WHERE f.fichaCalendarioPK.fechaCalendario = :fechaCalendario"),
    @NamedQuery(name = "FichaCalendario.findByNumeroDocumentoFuncionario", query = "SELECT f FROM FichaCalendario f WHERE f.fichaCalendarioPK.numeroDocumentoFuncionario = :numeroDocumentoFuncionario"),
    @NamedQuery(name = "FichaCalendario.findByDiaCalendarioLetra", query = "SELECT f FROM FichaCalendario f WHERE f.diaCalendarioLetra = :diaCalendarioLetra"),
    @NamedQuery(name = "FichaCalendario.findByDiaCalendarioNumero", query = "SELECT f FROM FichaCalendario f WHERE f.diaCalendarioNumero = :diaCalendarioNumero"),
    @NamedQuery(name = "FichaCalendario.findByMesCalendarioNumero", query = "SELECT f FROM FichaCalendario f WHERE f.mesCalendarioNumero = :mesCalendarioNumero"),
    @NamedQuery(name = "FichaCalendario.findByAnioCalendarioNumero", query = "SELECT f FROM FichaCalendario f WHERE f.anioCalendarioNumero = :anioCalendarioNumero"),
    @NamedQuery(name = "FichaCalendario.findByHoraEntrada", query = "SELECT f FROM FichaCalendario f WHERE f.horaEntrada = :horaEntrada"),
    @NamedQuery(name = "FichaCalendario.findByHoraSalida", query = "SELECT f FROM FichaCalendario f WHERE f.horaSalida = :horaSalida"),
    @NamedQuery(name = "FichaCalendario.findByHoraNoDefinida", query = "SELECT f FROM FichaCalendario f WHERE f.horaNoDefinida = :horaNoDefinida"),
    @NamedQuery(name = "FichaCalendario.findByPorcentajeMulta", query = "SELECT f FROM FichaCalendario f WHERE f.porcentajeMulta = :porcentajeMulta"),
    @NamedQuery(name = "FichaCalendario.findByMotivo", query = "SELECT f FROM FichaCalendario f WHERE f.motivo = :motivo"),
    @NamedQuery(name = "FichaCalendario.findByMotivoCambioHora", query = "SELECT f FROM FichaCalendario f WHERE f.motivoCambioHora = :motivoCambioHora"),
    @NamedQuery(name = "FichaCalendario.findByHorasExtra", query = "SELECT f FROM FichaCalendario f WHERE f.horasExtra = :horasExtra"),
    @NamedQuery(name = "FichaCalendario.findByMarcaCalculoPorcentaje", query = "SELECT f FROM FichaCalendario f WHERE f.marcaCalculoPorcentaje = :marcaCalculoPorcentaje"),
    @NamedQuery(name = "FichaCalendario.findByMarcaCalculoHorasExtras", query = "SELECT f FROM FichaCalendario f WHERE f.marcaCalculoHorasExtras = :marcaCalculoHorasExtras"),
    @NamedQuery(name = "FichaCalendario.findByMarcaBloqueo", query = "SELECT f FROM FichaCalendario f WHERE f.marcaBloqueo = :marcaBloqueo"),
    @NamedQuery(name = "FichaCalendario.findByMontoFijo", query = "SELECT f FROM FichaCalendario f WHERE f.montoFijo = :montoFijo"),
    @NamedQuery(name = "FichaCalendario.findByDiaTrabajado", query = "SELECT f FROM FichaCalendario f WHERE f.diaTrabajado = :diaTrabajado"),
    @NamedQuery(name = "FichaCalendario.findByDiaComplementario", query = "SELECT f FROM FichaCalendario f WHERE f.diaComplementario = :diaComplementario"),
    @NamedQuery(name = "FichaCalendario.findByMarcaFichaVerificada", query = "SELECT f FROM FichaCalendario f WHERE f.marcaFichaVerificada = :marcaFichaVerificada")})
public class FichaCalendario implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FichaCalendarioPK fichaCalendarioPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "dia_calendario_letra")
    private String diaCalendarioLetra;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dia_calendario_numero")
    private int diaCalendarioNumero;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mes_calendario_numero")
    private int mesCalendarioNumero;
    @Basic(optional = false)
    @NotNull
    @Column(name = "anio_calendario_numero")
    private int anioCalendarioNumero;
    @Column(name = "hora_entrada")
    @Temporal(TemporalType.TIME)
    private Date horaEntrada;
    @Column(name = "hora_salida")
    @Temporal(TemporalType.TIME)
    private Date horaSalida;
    @Column(name = "hora_no_definida")
    @Temporal(TemporalType.TIME)
    private Date horaNoDefinida;
    @Column(name = "porcentaje_multa")
    private Integer porcentajeMulta;
    @Size(max = 2000)
    @Column(name = "motivo")
    private String motivo;
    @Size(max = 2000)
    @Column(name = "motivo_cambio_hora")
    private String motivoCambioHora;
    @Column(name = "horas_extra")
    private Integer horasExtra;
    @Column(name = "marca_calculo_porcentaje")
    private Integer marcaCalculoPorcentaje;
    @Column(name = "marca_calculo_horas_extras")
    private Integer marcaCalculoHorasExtras;
    @Size(max = 2)
    @Column(name = "marca_bloqueo")
    private String marcaBloqueo;
    @Column(name = "monto_fijo")
    private Integer montoFijo;
    @Column(name = "dia_trabajado")
    private Boolean diaTrabajado;
    @Column(name = "dia_complementario")
    private Boolean diaComplementario;
    @Column(name = "marca_ficha_verificada")
    private Boolean marcaFichaVerificada;
    @JoinColumn(name = "numero_documento_funcionario", referencedColumnName = "numero_documento", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Funcionarios funcionarios;
    @JoinColumn(name = "horario_asignado", referencedColumnName = "horario")
    @ManyToOne(optional = false)
    private Horarios horarioAsignado;
    @JoinColumn(name = "codigo_excepcion", referencedColumnName = "codigo_excepcion")
    @ManyToOne
    private TiposExcepciones codigoExcepcion;

    public FichaCalendario() {
    }

    public FichaCalendario(FichaCalendarioPK fichaCalendarioPK) {
        this.fichaCalendarioPK = fichaCalendarioPK;
    }

    public FichaCalendario(FichaCalendarioPK fichaCalendarioPK, String diaCalendarioLetra, int diaCalendarioNumero, int mesCalendarioNumero, int anioCalendarioNumero) {
        this.fichaCalendarioPK = fichaCalendarioPK;
        this.diaCalendarioLetra = diaCalendarioLetra;
        this.diaCalendarioNumero = diaCalendarioNumero;
        this.mesCalendarioNumero = mesCalendarioNumero;
        this.anioCalendarioNumero = anioCalendarioNumero;
    }

    public FichaCalendario(Date fechaCalendario, String numeroDocumentoFuncionario) {
        this.fichaCalendarioPK = new FichaCalendarioPK(fechaCalendario, numeroDocumentoFuncionario);
    }

    public FichaCalendarioPK getFichaCalendarioPK() {
        return fichaCalendarioPK;
    }

    public void setFichaCalendarioPK(FichaCalendarioPK fichaCalendarioPK) {
        this.fichaCalendarioPK = fichaCalendarioPK;
    }

    public String getDiaCalendarioLetra() {

        switch (diaCalendarioLetra) {
            case "SUNDAY":
                diaCalendarioLetra = "DOMINGO";
                break;
            case "MONDAY":
                diaCalendarioLetra = "LUNES";
                break;
            case "TUESDAY":
                diaCalendarioLetra = "MARTES";
                break;

            case "WEDNESDAY":
                diaCalendarioLetra = "MIÉRCOLES";
                break;
            case "THURSDAY":
                diaCalendarioLetra = "JUEVES";
                break;
            case "FRIDAY":
                diaCalendarioLetra = "VIERNES";
                break;
            case "SATURDAY":
                diaCalendarioLetra = "SÁBADO";
                break;
            default:
                break;
        }

        return diaCalendarioLetra;
    }

    public void setDiaCalendarioLetra(String diaCalendarioLetra) {
        this.diaCalendarioLetra = diaCalendarioLetra;
    }

    public int getDiaCalendarioNumero() {
        return diaCalendarioNumero;
    }

    public void setDiaCalendarioNumero(int diaCalendarioNumero) {
        this.diaCalendarioNumero = diaCalendarioNumero;
    }

    public int getMesCalendarioNumero() {
        return mesCalendarioNumero;
    }

    public void setMesCalendarioNumero(int mesCalendarioNumero) {
        this.mesCalendarioNumero = mesCalendarioNumero;
    }

    public int getAnioCalendarioNumero() {
        return anioCalendarioNumero;
    }

    public void setAnioCalendarioNumero(int anioCalendarioNumero) {
        this.anioCalendarioNumero = anioCalendarioNumero;
    }

    public Date getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(Date horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public Date getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(Date horaSalida) {
        this.horaSalida = horaSalida;
    }

    public Date getHoraNoDefinida() {
        return horaNoDefinida;
    }

    public void setHoraNoDefinida(Date horaNoDefinida) {
        this.horaNoDefinida = horaNoDefinida;
    }

    public Integer getPorcentajeMulta() {
        return porcentajeMulta;
    }

    public void setPorcentajeMulta(Integer porcentajeMulta) {
        this.porcentajeMulta = porcentajeMulta;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getMotivoCambioHora() {
        return motivoCambioHora;
    }

    public void setMotivoCambioHora(String motivoCambioHora) {
        this.motivoCambioHora = motivoCambioHora;
    }

    public Integer getHorasExtra() {
        return horasExtra;
    }

    public void setHorasExtra(Integer horasExtra) {
        this.horasExtra = horasExtra;
    }

    public Integer getMarcaCalculoPorcentaje() {
        return marcaCalculoPorcentaje;
    }

    public void setMarcaCalculoPorcentaje(Integer marcaCalculoPorcentaje) {
        this.marcaCalculoPorcentaje = marcaCalculoPorcentaje;
    }

    public Integer getMarcaCalculoHorasExtras() {
        return marcaCalculoHorasExtras;
    }

    public void setMarcaCalculoHorasExtras(Integer marcaCalculoHorasExtras) {
        this.marcaCalculoHorasExtras = marcaCalculoHorasExtras;
    }

    public String getMarcaBloqueo() {
        return marcaBloqueo;
    }

    public void setMarcaBloqueo(String marcaBloqueo) {
        this.marcaBloqueo = marcaBloqueo;
    }

    public Integer getMontoFijo() {
        return montoFijo;
    }

    public void setMontoFijo(Integer montoFijo) {
        this.montoFijo = montoFijo;
    }

    public Boolean getDiaTrabajado() {
        return diaTrabajado;
    }

    public void setDiaTrabajado(Boolean diaTrabajado) {
        this.diaTrabajado = diaTrabajado;
    }

    public Boolean getDiaComplementario() {
        return diaComplementario;
    }

    public void setDiaComplementario(Boolean diaComplementario) {
        this.diaComplementario = diaComplementario;
    }

    public Boolean getMarcaFichaVerificada() {
        return marcaFichaVerificada;
    }

    public void setMarcaFichaVerificada(Boolean marcaFichaVerificada) {
        this.marcaFichaVerificada = marcaFichaVerificada;
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

    public TiposExcepciones getCodigoExcepcion() {
        return codigoExcepcion;
    }

    public void setCodigoExcepcion(TiposExcepciones codigoExcepcion) {
        this.codigoExcepcion = codigoExcepcion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fichaCalendarioPK != null ? fichaCalendarioPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FichaCalendario)) {
            return false;
        }
        FichaCalendario other = (FichaCalendario) object;
        if ((this.fichaCalendarioPK == null && other.fichaCalendarioPK != null) || (this.fichaCalendarioPK != null && !this.fichaCalendarioPK.equals(other.fichaCalendarioPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.mca.sisrrhh.entitys.FichaCalendario[ fichaCalendarioPK=" + fichaCalendarioPK + " ]";
    }

}
