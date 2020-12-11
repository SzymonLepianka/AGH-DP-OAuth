package Group28.OAuth.DAO;

import Group28.OAuth.Domain.*;

public interface IDatabaseEditor {
    boolean connect(String database);
    boolean disconnect();

    IDatabaseEditor getInstance();

    IDataBaseAccessObject<User> getUsersAccessObject();
    IDataBaseAccessObject<Permission> getPermissionsAccessObject();
    IDataBaseAccessObject<ClientApp> getAppsAccessObject();
    IDataBaseAccessObject<AuthCode> getAuthCodesAccessObject();
    IDataBaseAccessObject<AccessToken> getAccessTokensAccessObject();
    IDataBaseAccessObject<RefreshToken> getRefreshTokensAccessObject();

}
