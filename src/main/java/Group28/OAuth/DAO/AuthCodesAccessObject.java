package Group28.OAuth.DAO;

import Group28.OAuth.Domain.AuthCode;
import Group28.OAuth.Domain.ClientApp;
import Group28.OAuth.Domain.User;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public class AuthCodesAccessObject implements IDataBaseAccessObject<AuthCode>{
    private Connection connection;

    public AuthCodesAccessObject(Connection connection) {
        this.connection = connection;
    }

    private AuthCode createAuthCodeFromResult(ResultSet rs) throws SQLException {
        AuthCode authCode = new AuthCode();
        authCode.setId(rs.getLong("auth_code_id"));
        authCode.setExpiresAt(rs.getDate("expires_at"));
        authCode.setRevoked(rs.getBoolean("revoked"));
        ClientApp clientApp = DatabaseEditor.getInstance().getAppsAccessObject().readById(rs.getLong("client_app_id"));
        authCode.setClientApp(clientApp);
        User user = DatabaseEditor.getInstance().getUsersAccessObject().readById(rs.getLong("user_id"));
        authCode.setUser(user);
        return authCode;
    }

    @Override
    public List<AuthCode> readAll() throws SQLException {
        Statement statement = this.connection.createStatement();
        ResultSet result = statement.executeQuery("select * FROM auth_codes");
        List<AuthCode> resultList = new LinkedList<>();
        while(result.next()){
            resultList.add(createAuthCodeFromResult(result));
        }
        return resultList;
    }

    @Override
    public AuthCode readById(Long id) throws SQLException {
        Statement statement = this.connection.createStatement();
        ResultSet result = statement.executeQuery(String.format("SELECT * from auth_codes where auth_code_id=%f", id));
        AuthCode authCode = createAuthCodeFromResult(result);
        return authCode;
    }

    @Override
    public AuthCode create(AuthCode object) throws SQLException {
        String query = "INSERT INTO auth_codes (expires_at, revoked, client_app_id, user_id)" + "values (?,?,?,?)";
        PreparedStatement preparedStmt = this.connection.prepareStatement(query);
        preparedStmt.setDate(1, object.getExpiresAt());
        preparedStmt.setBoolean(2, object.isRevoked());
        preparedStmt.setLong(3, object.getClientApp().getId());
        preparedStmt.setLong(4, object.getUser().getId());
        preparedStmt.execute();
        return object;
    }

    @Override
    public AuthCode update(AuthCode object) throws SQLException {
        String query = "UPDATE auth_codes SET expires_at = ?, revoked = ?, client_app_id = ?, user_id = ? WHERE auth_code_id = ?";
        PreparedStatement preparedStmt = this.connection.prepareStatement(query);
        preparedStmt.setDate(1, object.getExpiresAt());
        preparedStmt.setBoolean(2, object.isRevoked());
        preparedStmt.setLong(3, object.getClientApp().getId());
        preparedStmt.setLong(4, object.getUser().getId());
        //where
        preparedStmt.setLong(5, object.getId());
        preparedStmt.execute();
        return object;
    }

    @Override
    public void remove(AuthCode object) throws SQLException {
        String query = "DELETE FROM auth_codes WHERE auth_code_id = ?";
        PreparedStatement preparedStmt = this.connection.prepareStatement(query);
        //where
        preparedStmt.setLong(1, object.getId());
        preparedStmt.execute();
    }
}
