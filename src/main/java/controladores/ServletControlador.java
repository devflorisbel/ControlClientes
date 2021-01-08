package controladores;

import datos.ClienteDAOJDBC;
import domain.Cliente;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/ServletControlador")
public class ServletControlador extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accion = req.getParameter("accion");
        if (accion != null) {
            switch (accion) {
                case "editar": 
                    try {
                    this.editarCliente(req, resp);
                    break;
                } catch (SQLException ex) {
                    ex.printStackTrace(System.out);
                }
                case "eliminar": {
                    try {
                        this.eliminarCliente(req, resp);
                        break;
                    } catch (SQLException ex) {
                        Logger.getLogger(ServletControlador.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
        } else {
            this.accionDefault(req, resp);
        }
    }

    private double calcularSaldoTotal(List<Cliente> clientes) {
        double saltoTotal = 0;
        for (Cliente cliente : clientes) {
            saltoTotal += cliente.getSaldo();
        }
        return saltoTotal;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accion = req.getParameter("accion");
        if (accion != null) {
            switch (accion) {
                case "insertar": 
                    try {
                    this.insertarCliente(req, resp);
                    break;
                } catch (SQLException ex) {
                    ex.printStackTrace(System.out);
                }
                case "modificar": 
                    try {
                    this.modificarCliente(req, resp);
                    break;
                } catch (SQLException ex) {
                    ex.printStackTrace(System.out);
                }
                default:
                    this.accionDefault(req, resp);
            }
        } else {
            this.accionDefault(req, resp);
        }
    }

    private void editarCliente(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        int idCliente = Integer.parseInt(req.getParameter("idCliente"));
        Cliente cliente = new ClienteDAOJDBC().buscar(new Cliente(idCliente));
        req.setAttribute("cliente", cliente);
        String jspEditar = "/WEB-INF/paginas/cliente/editarCliente.jsp";
        req.getRequestDispatcher(jspEditar).forward(req, resp);
    }

    private void modificarCliente(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        int idCliente = Integer.parseInt(req.getParameter("idCliente"));
        String nombre = req.getParameter("nombre");
        String apellido = req.getParameter("apellido");
        String email = req.getParameter("email");
        String telefono = req.getParameter("telefono");
        String saldoString = req.getParameter("saldo");
        double saldo = 0;
        if (saldoString != null && !saldoString.equals("")) {
            saldo = Double.parseDouble(saldoString);
        }
        Cliente cliente = new Cliente(idCliente, nombre, apellido, email, telefono, saldo);
        int resultado = new ClienteDAOJDBC().actualizar(cliente);
        System.out.println("registros agregados: " + resultado);
        this.accionDefault(req, resp);
    }

    private void insertarCliente(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        String nombre = req.getParameter("nombre");
        String apellido = req.getParameter("apellido");
        String email = req.getParameter("email");
        String telefono = req.getParameter("telefono");
        String saldoString = req.getParameter("saldo");
        double saldo = 0;
        if (saldoString != null && !saldoString.equals("")) {
            saldo = Double.parseDouble(saldoString);
        }
        Cliente cliente = new Cliente(nombre, apellido, email, telefono, saldo);
        int resultado = new ClienteDAOJDBC().insertar(cliente);
        System.out.println("registros agregados: " + resultado);
        this.accionDefault(req, resp);
    }

    private void eliminarCliente(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        int idCliente = Integer.parseInt(req.getParameter("idCliente"));
        int resultado = new ClienteDAOJDBC().eliminar(new Cliente(idCliente));
        System.out.println("registros agregados: " + resultado);
        this.accionDefault(req, resp);
    }

    private void accionDefault(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Cliente> clientes = new ClienteDAOJDBC().listar();
            HttpSession sesion = req.getSession();
            sesion.setAttribute("clientes", clientes);
            sesion.setAttribute("totalClientes", clientes.size());
            sesion.setAttribute("saldoTotal", this.calcularSaldoTotal(clientes));
            resp.sendRedirect("clientes.jsp");
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
    }

}
