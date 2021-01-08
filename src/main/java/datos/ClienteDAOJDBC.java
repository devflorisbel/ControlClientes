package datos;

import static datos.Conexion.close;
import domain.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAOJDBC implements ClienteDAO {

    private static final String SQL_SELECT = "SELECT id_cliente, nombre, apellido, email, telefono, saldo FROM control_clientes.clientes";
    private static final String SQL_SEARCH = "SELECT id_cliente, nombre, apellido, email, telefono, saldo FROM control_clientes.clientes WHERE id_cliente = ?";
    private static final String SQL_INSERT = "INSERT INTO control_clientes.clientes (nombre, apellido, email, telefono, saldo) VALUES (?,?,?,?,?)";
    private static final String SQL_UPDATE = "UPDATE control_clientes.clientes SET nombre = ?, apellido = ?, email = ?, telefono = ?, saldo = ? WHERE id_cliente = ?";
    private static final String SQL_DELETE = "DELETE FROM control_clientes.clientes WHERE id_cliente = ?";

    @Override
    public List<Cliente> listar() throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Cliente cliente = null;
        List<Cliente> clientes = new ArrayList<>();

        try {
            conn = Conexion.getConnection();
            ps = conn.prepareStatement(SQL_SELECT);
            rs = ps.executeQuery();
            while (rs.next()) {
                int idCliente = rs.getInt("id_cliente");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String email = rs.getString("email");
                String telefono = rs.getString("telefono");
                double saldo = rs.getDouble("saldo");

                cliente = new Cliente(idCliente, nombre, apellido, email, telefono, saldo);
                clientes.add(cliente);
            }
        } finally {
            close(rs);
            close(ps);
            close(conn);
        }

        return clientes;
    }

    @Override
    public Cliente buscar(Cliente cliente) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = Conexion.getConnection();
            ps = conn.prepareStatement(SQL_SEARCH);
            ps.setInt(1, cliente.getIdCliente());
            rs = ps.executeQuery();
            while (rs.next()) {
                int idCliente = rs.getInt("id_cliente");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String email = rs.getString("email");
                String telefono = rs.getString("telefono");
                double saldo = rs.getDouble("saldo");
                cliente = new Cliente(idCliente, nombre, apellido, email, telefono, saldo);
            }
        } catch (Exception e) {
        }
        return cliente;
    }

    @Override
    public int insertar(Cliente cliente) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        int registro = 0;

        try {
            conn = Conexion.getConnection();
            ps = conn.prepareStatement(SQL_INSERT);
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getApellido());
            ps.setString(3, cliente.getEmail());
            ps.setString(4, cliente.getTelefono());
            ps.setDouble(5, cliente.getSaldo());
            registro = ps.executeUpdate();
        } finally {
            close(ps);
            close(conn);
        }

        return registro;
    }

    @Override
    public int actualizar(Cliente cliente) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        int registro = 0;

        try {
            conn = Conexion.getConnection();
            ps = conn.prepareStatement(SQL_UPDATE);
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getApellido());
            ps.setString(3, cliente.getEmail());
            ps.setString(4, cliente.getTelefono());
            ps.setDouble(5, cliente.getSaldo());
            ps.setInt(6, cliente.getIdCliente());
            registro = ps.executeUpdate();
        } finally {
            close(ps);
            close(conn);
        }

        return registro;
    }

    @Override
    public int eliminar(Cliente cliente) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        int registro = 0;

        try {
            conn = Conexion.getConnection();
            ps = conn.prepareStatement(SQL_DELETE);
            ps.setInt(1, cliente.getIdCliente());
            registro = ps.executeUpdate();
        } finally {
            close(ps);
            close(conn);
        }

        return registro;
    }

}
