/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.mca.sisrrhh.managedbeans;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import py.gov.mca.sisrrhh.entitys.Funcionarios;
import py.gov.mca.sisrrhh.sessionbeans.FuncionariosSB;
import py.gov.mca.sisrrhh.utiles.Converciones;

/**
 *
 * @author vinsfran
 */
@Named(value = "loginMB")
@SessionScoped
public class LoginMB implements Serializable {

    @EJB
    private FuncionariosSB ejbFuncionariosSB;

    private String loginUsuario;
    private String claveUsuario;

    private boolean menuLiquiHaberes;
    private boolean menuBeneficios;

    private Funcionarios funcionarioUsuario;

    public LoginMB() {
        //Control de tiempo de session de usuario, en segundos seteado en 1200 segundos equivalentes a 20 minutos
        //HttpSession sessionUsuario = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        //sessionUsuario.setMaxInactiveInterval(1200);
    }

    @PostConstruct
    public void init() {
        this.setLoginUsuario("");
        this.setClaveUsuario("");
        menuBeneficios = false;
        menuLiquiHaberes = false;
    }

    public String btnIngresar() {
        if (getLoginUsuario() != null) {
            if (getLoginUsuario().equals("") || getClaveUsuario().equals("")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Los campos con (*) no pueden estar vacio.", ""));
                return "/index";
            } else {
                setFuncionarioUsuario(null);
                setFuncionarioUsuario(ejbFuncionariosSB.findByUsuario(getLoginUsuario().trim()));
                if (getFuncionarioUsuario() != null) {
                    Converciones c = new Converciones();
                    String contrasenaMD5 = c.getMD5(getClaveUsuario());
                    /// System.out.println("Clave" +contrasenaMD5);
                    if (contrasenaMD5 == null) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo ingresar, intentelo de nuevo.", ""));
                        return "/index";
                    } else if (getFuncionarioUsuario().getContrasena().equals(getClaveUsuario()) && getFuncionarioUsuario().getEstadoFuncionario().getCodigo().equals("ACT")) {
                        String pagina;
                        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
                        httpSession.setAttribute("funcionarioUsuario", this.getFuncionarioUsuario());
                        switch (getFuncionarioUsuario().getRolUsuario().getCodigo()) {
                            case "LQH":
                                menuBeneficios = false;
                                menuLiquiHaberes = true;
                                break;
                            case "BEN":
                                menuBeneficios = true;
                                menuLiquiHaberes = false;
                                break;

                            case "FUN":
                                menuBeneficios = false;
                                menuLiquiHaberes = false;
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Acceso no permitido", ""));
                                break;
                            default:
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Bienvenido!", ""));
                                break;

                        }
                        System.out.println(getFuncionarioUsuario().getRolUsuario().getPaginaInicio());
                        return getFuncionarioUsuario().getRolUsuario().getPaginaInicio();
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuario o contraseña no validos, intentelo de nuevo.", ""));
                        return "/index";
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuario no exite, intentelo de nuevo.", ""));
                    return "/index";
                }
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Los campos con null.", ""));
            return "/index";
        }

    }

    /**
     * @return the loginUsuario
     */
    public String getLoginUsuario() {
        return loginUsuario;
    }

    /**
     * @param loginUsuario the loginUsuario to set
     */
    public void setLoginUsuario(String loginUsuario) {
        this.loginUsuario = loginUsuario;
    }

    /**
     * @return the claveUsuario
     */
    public String getClaveUsuario() {
        return claveUsuario;
    }

    /**
     * @param claveUsuario the claveUsuario to set
     */
    public void setClaveUsuario(String claveUsuario) {
        this.claveUsuario = claveUsuario;
    }

    /**
     * @return the funcionarioUsuario
     */
    public Funcionarios getFuncionarioUsuario() {
        return funcionarioUsuario;
    }

    /**
     * @param funcionarioUsuario the funcionarioUsuario to set
     */
    public void setFuncionarioUsuario(Funcionarios funcionarioUsuario) {
        this.funcionarioUsuario = funcionarioUsuario;
    }

    /**
     * @return the menuLiquiHaberes
     */
    public boolean isMenuLiquiHaberes() {
        return menuLiquiHaberes;
    }

    /**
     * @param menuLiquiHaberes the menuLiquiHaberes to set
     */
    public void setMenuLiquiHaberes(boolean menuLiquiHaberes) {
        this.menuLiquiHaberes = menuLiquiHaberes;
    }

    /**
     * @return the menuBeneficios
     */
    public boolean isMenuBeneficios() {
        return menuBeneficios;
    }

    /**
     * @param menuBeneficios the menuBeneficios to set
     */
    public void setMenuBeneficios(boolean menuBeneficios) {
        this.menuBeneficios = menuBeneficios;
    }

}
