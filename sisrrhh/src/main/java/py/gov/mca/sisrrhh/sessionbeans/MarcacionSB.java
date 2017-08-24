/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.mca.sisrrhh.sessionbeans;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import py.gov.mca.sisrrhh.entitys.Marcacion;
import py.gov.mca.sisrrhh.utiles.MsgUtil;

/**
 *
 * @author vinsfran
 */
@Stateless
public class MarcacionSB {

    @PersistenceContext(unitName = "sisrrhhPU")
    private EntityManager em;

    @SuppressWarnings("unchecked")
    public List<Marcacion> findByCedula(String cedula) {
        Query q = em.createNamedQuery("Marcacion.findByCedula");
        q.setParameter("cedula", cedula);
        return q.getResultList();
    }

    public List<Marcacion> findByCedulaFechaDesdeFechaHasta(String cedula, Date fechaDesde, Date fechaHasta) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT e ");
        jpql.append("FROM Marcacion e ");
        jpql.append("WHERE e.marcacionPK.cedula = :cedula ");
        jpql.append("AND CAST(e.fechaMarcacionChar AS DATE)  BETWEEN :paramFechaDesde AND :paramFechaHasta ");
        Query q = em.createQuery(jpql.toString());
        q.setParameter("cedula", cedula);
        q.setParameter("paramFechaDesde", fechaDesde);
        q.setParameter("paramFechaHasta", fechaHasta);
        return q.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Marcacion> findAll() {
        Query q = em.createNamedQuery("Marcacion.findAll");
        return q.getResultList();
    }

    public void create(Marcacion marcacion) {
        em.persist(marcacion);
    }

    public void udtade(Marcacion marcacion) {
        String err = "MARCACION "
                + marcacion.getMarcacionPK().getCedula() + " "
                + marcacion.getMarcacionPK().getTipoMacacion() + " "
                + marcacion.getMarcacionPK().getFechaHoraMarcacion();
        System.out.println(err);
        try {
            em.merge(marcacion);
        } catch (Exception e) {
            Logger.getLogger(MarcacionSB.class.getName()).log(Level.SEVERE, null, e);
            MsgUtil.msg("Error en transacción", err + " " + e.getMessage(), "ERROR");
        }
    }

    public void insert(Marcacion marcacion) {
        String err = "MARCACION ";
        Marcacion mar = em.find(Marcacion.class, marcacion.getMarcacionPK());
        if (mar == null) {
            err = err + "persist "
                + marcacion.getMarcacionPK().getCedula() + " "
                + marcacion.getMarcacionPK().getTipoMacacion() + " "
                + marcacion.getMarcacionPK().getFechaHoraMarcacion();
            //attached = new AnyEntity();
            em.persist(marcacion);
        } else {
            err = err + "merge "
                + marcacion.getMarcacionPK().getCedula() + " "
                + marcacion.getMarcacionPK().getTipoMacacion() + " "
                + marcacion.getMarcacionPK().getFechaHoraMarcacion();
            em.merge(marcacion);
        }
        System.out.println(err);
    }

    public String udtadeMultiple(List<Marcacion> marcaciones) {
        try {
            for (int i = 0; i < marcaciones.size(); i++) {
                Marcacion marcacion = marcaciones.get(i);
                em.merge(marcacion);
                if ((i % 10000) == 0) {
                    flushAndClear();
                    System.out.println("ENTRO EN CLEAR " + i);
                }
                System.out.println("Cnt : " + (i + 1) + " - RESTO: " + (i % 10000));
            }
            flushAndClear();
            return "OK";
        } catch (Exception e) {
            e.printStackTrace();
            flushAndClear();
            return "FAIL";
        }
    }

    public void flushAndClear() {
        em.flush();
        em.clear();
    }
}
