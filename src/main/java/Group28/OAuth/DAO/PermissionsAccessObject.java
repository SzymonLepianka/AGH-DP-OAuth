package Group28.OAuth.DAO;

import Group28.OAuth.Domain.Permission;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class PermissionsAccessObject implements IDataBaseAccessObject<Permission>{
    private Connection connection;

    public PermissionsAccessObject(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Permission> readAll() throws SQLException {
        return null;
    }

    @Override
    public Permission readById(Long id) throws SQLException {
        return null;
    }

    @Override
    public Permission create(Permission object) throws SQLException {
        return null;
    }

    @Override
    public Permission update(Permission object) throws SQLException {
        return null;
    }

    @Override
    public void remove(Permission object) throws SQLException {

    }
}
