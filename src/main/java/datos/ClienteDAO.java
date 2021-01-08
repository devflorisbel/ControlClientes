package datos;

import domain.*;
import java.sql.SQLException;
import java.util.List;

public interface ClienteDAO {
    
    public List<Cliente> listar() throws SQLException;
    
    public Cliente buscar(Cliente cliente) throws SQLException;
    
    public int insertar(Cliente cliente) throws SQLException;;
    
    public int actualizar(Cliente cliente) throws SQLException;
    
    public int eliminar(Cliente cliente) throws SQLException;
    
}
