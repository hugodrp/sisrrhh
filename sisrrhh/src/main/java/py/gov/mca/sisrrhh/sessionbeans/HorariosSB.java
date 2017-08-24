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
import py.gov.mca.sisrrhh.entitys.Horarios;

/**
 *
 * @author vinsfran
 */
@Stateless
public class HorariosSB {

    @PersistenceContext(unitName = "sisrrhhPU")
    private EntityManager em;

    @SuppressWarnings("unchecked")
    public Horarios findByHorario(String horario) {
        Query q = em.createNamedQuery("Horarios.findByHorario");
        q.setParameter("horario", horario);
        if (q.getResultList().isEmpty()) {
            return null;
        } else {
            return (Horarios) q.getResultList().get(0);
        }

    }

    @SuppressWarnings("unchecked")
    public List<Horarios> findAll() {
        Query q = em.createNamedQuery("Horarios.findAll");
        return q.getResultList();
    }

    public void create(Horarios horario) {
        em.persist(horario);
    }

    public void udtade(Horarios horario) {
        System.out.println("ENTRO " + horario.getHorario());
        em.merge(horario);
    }

    public String udtadeMultiple(List<Horarios> horarios) {
        try {
            for (int i = 0; i < horarios.size(); i++) {
                Horarios horario = horarios.get(i);
                System.out.println("ENTRO ANTES DE CLEAR " + i);
                em.merge(horario);
                if ((i % 1000) == 0) {
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
