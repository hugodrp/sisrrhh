/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.mca.sisrrhh.sessionbeans;

import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import py.gov.mca.sisrrhh.entitys.AsignacionExcepcionesHorariosDiarios;

/**
 *
 * @author vinsfran
 */
@Stateless
public class AsignacionExcepcionesHorariosDiariosSB {

    @PersistenceContext(unitName = "sisrrhhPU")
    private EntityManager em;

    @SuppressWarnings("unchecked")
    public List<AsignacionExcepcionesHorariosDiarios> findAll() {
        Query q = em.createNamedQuery("AsignacionExcepcionesHorariosDiarios.findAll");
        return q.getResultList();
    }

    public AsignacionExcepcionesHorariosDiarios findByFechaAsignacionNumeroDocumentoFuncionario(Date fechaAsignacion, String numeroDocumentoFuncionario, String codigoExcepcion, String estadoHorarioAsignado) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT e ");
        jpql.append("FROM AsignacionExcepcionesHorariosDiarios e ");
        jpql.append("WHERE e.asignacionExcepcionesHorariosDiariosPK.fechaAsignacion = :fechaAsignacion ");
        jpql.append("AND e.asignacionExcepcionesHorariosDiariosPK.numeroDocumentoFuncionario = :numeroDocumentoFuncionario ");
        jpql.append("AND e.asignacionExcepcionesHorariosDiariosPK.codigoExcepcion = :codigoExcepcion ");
        jpql.append("AND e.estadoHorarioAsignado.codigo = :estadoHorarioAsignado ");
        Query q = em.createQuery(jpql.toString());
        q.setParameter("fechaAsignacion", fechaAsignacion);
        q.setParameter("numeroDocumentoFuncionario", numeroDocumentoFuncionario);
        q.setParameter("codigoExcepcion", codigoExcepcion);
        q.setParameter("estadoHorarioAsignado", estadoHorarioAsignado);
        if (q.getResultList().isEmpty()) {
            return null;
        } else {
            return (AsignacionExcepcionesHorariosDiarios) q.getResultList().get(0);
        }

    }

    public List<AsignacionExcepcionesHorariosDiarios> findByCedula(String cedula) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT e ");
        jpql.append("FROM AsignacionExcepcionesHorariosDiarios e ");
        jpql.append("WHERE e.asignacionExcepcionesHorariosDiariosPK.numeroDocumentoFuncionario = :cedula ");
        Query q = em.createQuery(jpql.toString());
        q.setParameter("cedula", cedula);
        return q.getResultList();
    }

    public List<AsignacionExcepcionesHorariosDiarios> findByCedulaMesAnio(String cedula, Integer mes, Integer anio) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT e ");
        jpql.append("FROM AsignacionExcepcionesHorariosDiarios e ");
        jpql.append("WHERE e.asignacionExcepcionesHorariosDiariosPK.numeroDocumentoFuncionario = :cedula ");
        jpql.append("AND e.mesAsignacion = :mes ");
        jpql.append("AND e.anioAsignacion = :anio ");
        Query q = em.createQuery(jpql.toString());
        q.setParameter("cedula", cedula);
        q.setParameter("mes", mes);
        q.setParameter("anio", anio);
        return q.getResultList();
    }

    public List<AsignacionExcepcionesHorariosDiarios> findByCedulaMesAnioTipo(String cedula, Integer mes, Integer anio, String tipo) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT e ");
        jpql.append("FROM AsignacionExcepcionesHorariosDiarios e ");
        jpql.append("WHERE e.asignacionExcepcionesHorariosDiariosPK.numeroDocumentoFuncionario = :cedula ");
        jpql.append("AND e.mesAsignacion = :mes ");
        jpql.append("AND e.anioAsignacion = :anio ");
        jpql.append("AND e.tiposExcepciones.codigoExcepcion = :tipo ");
        Query q = em.createQuery(jpql.toString());
        q.setParameter("cedula", cedula);
        q.setParameter("mes", mes);
        q.setParameter("anio", anio);
        q.setParameter("tipo", tipo);
        return q.getResultList();
    }

    public List<AsignacionExcepcionesHorariosDiarios> findByCedulaFechaDesdeFechaHasta(String cedula, Date fechaDesde, Date fechaHasta) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT e ");
        jpql.append("FROM AsignacionExcepcionesHorariosDiarios e ");
        jpql.append("WHERE e.asignacionExcepcionesHorariosDiariosPK.numeroDocumentoFuncionario = :cedula ");
        jpql.append("AND CAST( e.asignacionExcepcionesHorariosDiariosPK.fechaAsignacion AS DATE)  BETWEEN :paramFechaDesde AND :paramFechaHasta ");
        Query q = em.createQuery(jpql.toString());
        q.setParameter("cedula", cedula);
        q.setParameter("paramFechaDesde", fechaDesde);
        q.setParameter("paramFechaHasta", fechaHasta);
        return q.getResultList();
    }

    public void create(AsignacionExcepcionesHorariosDiarios asignacionExcepcionesHorariosDiarios) {
        em.persist(asignacionExcepcionesHorariosDiarios);
    }

    public void delete(AsignacionExcepcionesHorariosDiarios asignacionExcepcionesHorariosDiarios) {
        AsignacionExcepcionesHorariosDiarios asignacionExcepcionesHorariosDiariosAux = em.find(AsignacionExcepcionesHorariosDiarios.class, asignacionExcepcionesHorariosDiarios.getAsignacionExcepcionesHorariosDiariosPK());
        em.remove(asignacionExcepcionesHorariosDiariosAux);
    }

    public void udtade(AsignacionExcepcionesHorariosDiarios asignacionExcepcionesHorariosDiarios) {
        em.merge(asignacionExcepcionesHorariosDiarios);
    }

    public void flushAndClear() {
        em.flush();
        em.clear();
    }

}
