package Group28.OAuth.DAO;

import Group28.OAuth.Domain.ClientApp;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class AppsAccessObject implements IDataBaseAccessObject<ClientApp>{
    private Connection connection;

    public AppsAccessObject(Connection connection) {
        this.connection = connection;
    }

    //TODO
    @Override
    public List<ClientApp> readAll() throws SQLException {
        return null;
    }

    @Override
    public ClientApp readById(Long id) throws SQLException {
        return null;
    }

    @Override
    public ClientApp create(ClientApp object) throws SQLException {
        return null;
    }

    @Override
    public ClientApp update(ClientApp object) throws SQLException {
        return null;
    }

    @Override
    public void remove(ClientApp object) throws SQLException {

    }
}
