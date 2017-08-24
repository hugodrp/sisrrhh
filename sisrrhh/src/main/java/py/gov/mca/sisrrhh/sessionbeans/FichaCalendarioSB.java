/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.mca.sisrrhh.sessionbeans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.lang.StringUtils;
import py.gov.mca.sisrrhh.entitys.Auditoria;
import py.gov.mca.sisrrhh.entitys.FichaCalendario;
import py.gov.mca.sisrrhh.entitys.Horarios;
import py.gov.mca.sisrrhh.entitys.TiposExcepciones;

/**
 *
 * @author vinsfran
 */
@Stateless
public class FichaCalendarioSB {

    @PersistenceContext(unitName = "sisrrhhPU")
    private EntityManager em;

    public List<FichaCalendario> findByCedulaMesAnio(String cedula, Integer mes, Integer anio) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT e ");
        jpql.append("FROM FichaCalendario e ");
        jpql.append("WHERE e.fichaCalendarioPK.numeroDocumentoFuncionario = :cedula ");
        jpql.append("AND e.mesCalendarioNumero = :mes ");
        jpql.append("AND e.anioCalendarioNumero = :anio ");
        Query q = em.createQuery(jpql.toString());
        q.setParameter("cedula", cedula);
        q.setParameter("mes", mes);
        q.setParameter("anio", anio);
        return q.getResultList();
    }

    public String findTest() {
        System.err.println("EEEEEEEEEEEEEEE");
        String retorno = "Test ";
        Query q = em.createNativeQuery("SELECT a.fecha_calendario, a.numero_documento_funcionario, a.dia_calendario_letra, a.dia_calendario_numero FROM ficha_calendario a", FichaCalendario.class);
        List<FichaCalendario> fichaCalendarios = q.getResultList();

        for (FichaCalendario a : fichaCalendarios) {
            System.out.println("FichaCalendario "
                    + a.getDiaCalendarioLetra()
                    + " "
                    + a.getDiaCalendarioNumero());
            retorno = "Test Retorno";
        }

        return retorno;
    }

    public List<FichaCalendario> findByCedulaFechaDesdeFechaHasta(String cedula, Date fechaDesde, Date fechaHasta) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT e ");
        jpql.append("FROM FichaCalendario e ");
        jpql.append("WHERE e.fichaCalendarioPK.numeroDocumentoFuncionario = :cedula ");
        jpql.append("AND CAST( e.fichaCalendarioPK.fechaCalendario AS DATE)  BETWEEN :paramFechaDesde AND :paramFechaHasta ");
        Query q = em.createQuery(jpql.toString());
        q.setParameter("cedula", cedula);
        q.setParameter("paramFechaDesde", fechaDesde);
        q.setParameter("paramFechaHasta", fechaHasta);
        return q.getResultList();
    }

    public List<FichaCalendario> findByCedulaFechaDesdeFechaHastaConNull(String cedula, Date fechaDesde, Date fechaHasta) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT e ");
        jpql.append("FROM FichaCalendario e ");
        jpql.append("WHERE e.fichaCalendarioPK.numeroDocumentoFuncionario = :cedula ");
        jpql.append("AND CAST( e.fichaCalendarioPK.fechaCalendario AS DATE)  BETWEEN :paramFechaDesde AND :paramFechaHasta ");
        Query q = em.createQuery(jpql.toString());
        q.setParameter("cedula", cedula);
        q.setParameter("paramFechaDesde", fechaDesde);
        q.setParameter("paramFechaHasta", fechaHasta);
        if (q.getResultList().isEmpty()) {
            return null;
        } else {
            return q.getResultList();
        }
    }

    public FichaCalendario findByFechaCalendarioNumeroDocumentoFuncionario(Date fechaCalendario, String numeroDocumentoFuncionario) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT e ");
        jpql.append("FROM FichaCalendario e ");
        jpql.append("WHERE e.fichaCalendarioPK.fechaCalendario = :fechaCalendario ");
        jpql.append("AND e.fichaCalendarioPK.numeroDocumentoFuncionario = :numeroDocumentoFuncionario");
        Query q = em.createQuery(jpql.toString());
        q.setParameter("fechaCalendario", fechaCalendario);
        q.setParameter("numeroDocumentoFuncionario", numeroDocumentoFuncionario);
        if (q.getResultList().isEmpty()) {
            return null;
        } else {
            return (FichaCalendario) q.getResultList().get(0);
        }
    }

    public List<FichaCalendario> findByFechaCalendarioNumeroDocumentoFuncionarioLista(List<FichaCalendario> lista) {
        List<FichaCalendario> listaAux = new ArrayList<>();
        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT e ");
        jpql.append("FROM FichaCalendario e ");
        jpql.append("WHERE e.fichaCalendarioPK.fechaCalendario = :fechaCalendario ");
        jpql.append("AND e.fichaCalendarioPK.numeroDocumentoFuncionario = :numeroDocumentoFuncionario");

        for (int i = 0; i < lista.size(); i++) {

            Query q = em.createQuery(jpql.toString());
            q.setParameter("fechaCalendario", lista.get(i).getFichaCalendarioPK().getFechaCalendario());
            q.setParameter("numeroDocumentoFuncionario", lista.get(i).getFichaCalendarioPK().getNumeroDocumentoFuncionario());
            if (q.getResultList().isEmpty()) {
                System.out.println("NUEVA FICHA: " + lista.get(i).getFichaCalendarioPK().getNumeroDocumentoFuncionario());
                listaAux.add(lista.get(i));
            } else {
                System.out.println("FICHA YA EXISTE: " + lista.get(i).getFichaCalendarioPK().getNumeroDocumentoFuncionario());
            }

        }
        return listaAux;

    }

    public List<FichaCalendario> findAll() {
        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT e ");
        jpql.append("FROM FichaCalendario e ");
        jpql.append("WHERE e.mesCalendarioNumero = 6 ");
        Query q = em.createQuery(jpql.toString());
        return q.getResultList();
    }

    public List<FichaCalendario> listar() {
        Query q = em.createNamedQuery("FichaCalendario.findAll");
        return q.getResultList();
    }

    public void create(FichaCalendario fichaCalendario) {
        em.persist(fichaCalendario);
    }

    public void udtade(FichaCalendario fichaCalendario) {
        TiposExcepciones te = new TiposExcepciones();
        te.setCodigoExcepcion("VACI");
        if (fichaCalendario.getCodigoExcepcion() != null && !StringUtils.isBlank(fichaCalendario.getCodigoExcepcion().getCodigoExcepcion())) {
            te = em.getReference(TiposExcepciones.class, fichaCalendario.getCodigoExcepcion().getCodigoExcepcion());
        }
        fichaCalendario.setCodigoExcepcion(te);
        Horarios h = em.getReference(Horarios.class, fichaCalendario.getHorarioAsignado().getHorario());
        fichaCalendario.setHorarioAsignado(h);
        //      FichaCalendario f = em.getReference(FichaCalendario.class, fichaCalendario.getFichaCalendarioPK());
        //      f.setCodigoExcepcion(new TiposExcepciones());

        //imprimir(fichaCalendario);
        em.merge(fichaCalendario);
        flushAndClear();
    }

    public String udtadeMultiple(List<FichaCalendario> fichasCalendarios) {
        try {
            for (int i = 0; i < fichasCalendarios.size(); i++) {
                FichaCalendario fichaCalendario = fichasCalendarios.get(i);
                em.merge(fichaCalendario);
                if ((i % 10000) == 0) {
                    flushAndClear();
                }
            }
            flushAndClear();
            return "OK";
        } catch (Exception e) {
            e.printStackTrace();
            flushAndClear();
            return "FAIL";
        }
    }

    private void guardarAuditoria(Date fechaAudit, String usuarioAudit, String rolAudit, String tipoMovimientoAudit, String nombreTablaAudit, String campoClaveAudit, String descriAudit) {
        Auditoria auditoria = new Auditoria();
        auditoria.setFecha(fechaAudit);
        auditoria.setUsuario(usuarioAudit);
        auditoria.setRolUsuario(rolAudit);
        auditoria.setTipoMovimiento(tipoMovimientoAudit);
        auditoria.setNombreTabla(nombreTablaAudit);
        auditoria.setClaveTabla(campoClaveAudit);
        auditoria.setDescripcion(descriAudit);
        em.persist(auditoria);
        flushAndClear();
    }

    public void flushAndClear() {
        em.flush();
        em.clear();
    }

    private void imprimir(FichaCalendario fichaCalendario) {
        System.out.println("NumeroDocumentoFuncionario: " + fichaCalendario.getFichaCalendarioPK().getNumeroDocumentoFuncionario());
        System.out.println("FechaCalendario: " + fichaCalendario.getFichaCalendarioPK().getFechaCalendario());
        if (fichaCalendario.getCodigoExcepcion() != null) {
            System.out.println("CodigoExcepcion: " + fichaCalendario.getCodigoExcepcion().getCodigoExcepcion());
            System.out.println("DescripcionExcepcion: " + fichaCalendario.getCodigoExcepcion().getDescripcionExcepcion());
        }

        System.out.println("Horario Codigo: " + fichaCalendario.getHorarioAsignado().getHorario());
        System.out.println("Horario Descripcion: " + fichaCalendario.getHorarioAsignado().getDescripcion());
    }

}
