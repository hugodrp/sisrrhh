/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.mca.sisrrhh.sessionbeans;

import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import py.gov.mca.sisrrhh.entitys.Auditoria;
import py.gov.mca.sisrrhh.entitys.Estados;
import py.gov.mca.sisrrhh.entitys.Funcionarios;
import py.gov.mca.sisrrhh.entitys.Horarios;
import py.gov.mca.sisrrhh.entitys.RelacionLaboral;
import py.gov.mca.sisrrhh.entitys.Roles;

/**
 *
 * @author vinsfran
 */
@Stateless
public class FuncionariosSB {

    @PersistenceContext(unitName = "sisrrhhPU")
    private EntityManager em;

    @SuppressWarnings("unchecked")
    public Funcionarios findByUsuario(String usuario) {
        Query q = em.createNamedQuery("Funcionarios.findByUsuario");
        q.setParameter("usuario", usuario);
        if (q.getResultList().isEmpty()) {
            return null;
        } else {
            return (Funcionarios) q.getResultList().get(0);
        }
    }

    @SuppressWarnings("unchecked")
    public Funcionarios findByNumeroDocumento(String numeroDocumento) {
        Query q = em.createNamedQuery("Funcionarios.findByNumeroDocumento");
        q.setParameter("numeroDocumento", numeroDocumento);
        if (q.getResultList().isEmpty()) {
            return null;
        } else {
            return (Funcionarios) q.getResultList().get(0);
        }
    }

    @SuppressWarnings("unchecked")
    public List<Funcionarios> findAll() {
        Query q = em.createNamedQuery("Funcionarios.findAll");
        if (q.getResultList().isEmpty()) {
            return null;
        } else {
            return (List<Funcionarios>) q.getResultList();
        }
    }

    public List<Funcionarios> findAll2() {

        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT e.numero_documento, ");
        jpql.append("e.nombres, ");
        jpql.append("e.apellidos, ");
        jpql.append("e.fecha_ingreso, ");
        jpql.append("e.estado_funcionario, ");
        jpql.append("e.relacion_laboral, ");
        jpql.append("e.sueldo_diario, ");
        jpql.append("e.horario_normal ");

        jpql.append("FROM funcionarios e ");
//        jpql.append("WHERE e.fichaCalendarioPK.numeroDocumentoFuncionario = :cedula ");
//        jpql.append("AND e.mesCalendarioNumero = :mes ");
        jpql.append("ORDER BY e.numero_documento");

        //System.err.println("EEEEEEEEEEEEEEE");
        Query q = em.createNativeQuery(jpql.toString(), Funcionarios.class);
        List<Funcionarios> funcionarios = q.getResultList();

        if (funcionarios.isEmpty()) {
            return null;
        } else {
            return funcionarios;
        }
    }

    public List<Funcionarios> findByRangoCedula(String cedulaInicio, String cedulaFin) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT e ");
        jpql.append("FROM Funcionarios e ");
        jpql.append("WHERE e.numeroDocumento BETWEEN :cedulaInicio AND :cedulaFin ");
        Query q = em.createQuery(jpql.toString());
        q.setParameter("cedulaInicio", cedulaInicio);
        q.setParameter("cedulaFin", cedulaFin);
        return q.getResultList();
    }

    public List<Funcionarios> findByEstado(String estado) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT e ");
        jpql.append("FROM Funcionarios e ");
        jpql.append("WHERE e.estadoFuncionario.codigo = :estado ");
        Query q = em.createQuery(jpql.toString());
        q.setParameter("estado", estado);
        return q.getResultList();
    }

    public List<Funcionarios> findByEstadoRelacionLaboral(String estado, String relacionLaboral) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT e ");
        jpql.append("FROM Funcionarios e ");
        jpql.append("WHERE e.estadoFuncionario.codigo = :estado ");
        jpql.append("AND e.relacionLaboral.relacionLaboral = :relacionLaboral ");
        Query q = em.createQuery(jpql.toString());
        q.setParameter("estado", estado);
        return q.getResultList();
    }

    public List<Funcionarios> findByEstadoRelacionLaboralFiltro(String estado, String relacionLaboral) {

        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT e ");
        jpql.append("FROM Funcionarios e ");
        jpql.append("WHERE e.estadoFuncionario.codigo = :estado ");
        if (relacionLaboral.equals("JOR")) {
            jpql.append("AND e.relacionLaboral.relacionLaboral = :relacionLaboral ");
        } else {
            relacionLaboral = "JOR";
            jpql.append("AND e.relacionLaboral.relacionLaboral != :relacionLaboral ");
        }

        Query q = em.createQuery(jpql.toString());
        q.setParameter("estado", estado);
        q.setParameter("relacionLaboral", relacionLaboral);
        return q.getResultList();
    }

    public void create(Funcionarios funcionario) {
        em.persist(funcionario);
    }

    public String guardarFuncionarioMantenimiento(Funcionarios funcionario, String modo, String campos, String usuario, String rol) {
        //RelacionLaboral r = em.getReference(RelacionLaboral.class, funcionario.getRelacionLaboral().getRelacionLaboral());
       // Horarios h = em.getReference(Horarios.class, funcionario.getHorarioNormal().getHorario());
       // Estados e = em.getReference(Estados.class, funcionario.getEstadoFuncionario().getCodigo());
        //Roles rolFun = em.getReference(Roles.class, funcionario.getRolUsuario().getCodigo());

        //funcionario.setRelacionLaboral(r);
        //funcionario.setHorarioNormal(h);
        //funcionario.setEstadoFuncionario(e);
        //funcionario.setRolUsuario(rolFun);
        return guardar(funcionario, modo, campos, usuario, rol);
    }

    public void update(Funcionarios funcionario) {
        em.merge(funcionario);
    }

    private String guardar(Funcionarios funcionario, String modo, String campos, String usuario, String rol) {
        int ban = 0;
        String resultado = "NO";
        try {
            if (modo.equals("Update")) {
                em.merge(funcionario).getNumeroDocumento();
            } else {
                Funcionarios fu = em.find(Funcionarios.class, funcionario.getNumeroDocumento());
                if (fu == null) {
                    em.persist(funcionario);
                } else {
                    ban = 1;
                }
            }
            if (ban == 0) {
                //llama a auditoria
                String descriAudit = "Fecha: " + new Date() + ", "
                        + modo + " funcionario: " + funcionario.getNumeroDocumento()+", " + campos;
                guardarAuditoria(new Date(), usuario, rol, modo, "funcionarios", funcionario.getNumeroDocumento(), descriAudit);
                resultado = "OK";
            }

        } catch (EntityExistsException e) {
            System.out.println("EntityExistsException2: " + e.getMessage());
        } catch (PersistenceException e) {
            System.out.println("PersistenceException2: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Exception2: " + e.getMessage());
        }
        return resultado;
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
}
