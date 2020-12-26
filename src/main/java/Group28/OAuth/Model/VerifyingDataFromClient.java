package Group28.OAuth.Model;

import Group28.OAuth.DAO.DatabaseEditor;
import Group28.OAuth.DAO.IDatabaseEditor;
import Group28.OAuth.Domain.AuthCode;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class VerifyingDataFromClient extends State {

    @Override
    public Response handle(Context context, Map<String, String> params) throws SQLException {

        System.out.println("VerifyingDataFromClient");

        // jeśli params zawierają "scopes" wtedy CreatingAuthorizationCode
        if (params.containsKey("scopes")) {
            context.changeState(new CreatingAuthorizationCode());

            // jeśli params zawierają "code" wtedy ExchangingAuthorizationCodeForAccessToken
        } else if (params.containsKey("code")) {

            // pobieram 'code' i 'clientID' z 'params'
            String code = params.get("code");
            Long clientID = Long.parseLong(params.get("clientID"));

            // pobieram z bazy danych AuthCodes i szukam przekazanego w params 'code'
            IDatabaseEditor db = DatabaseEditor.getInstance();
            List<AuthCode> codesFromDataBase = db.getAuthCodesAccessObject().readAll();
            AuthCode authCode = codesFromDataBase.stream()
                    .filter(c -> code.equals(c.getContent()) && clientID.equals(c.getClientApp().getId()))
                    .findFirst()
                    .orElse(null);

            // jeśli udało się znaleźć AuthCode zmianiam stan na ExchangingAuthorizationCodeForAccessToken
            // w przyciwnym wypadku na Failure
            if (authCode != null) {
                context.changeState(new ExchangingAuthorizationCodeForAccessToken());
            } else {
                context.changeState(new Failure());
            }
            //TODO: else - RefreshingAccessToken

            // jeśli żadne parametry w params nie pasuje wtedy -> Failure
        } else {
            context.changeState(new Failure());
        }

        // wywołuję CreatingAuthorizationCode / ExchangingAuthorizationCodeForAccessToken / RefreshingAccessToken w przypadku gdy powodzenia / Failure w przeciwym przypadku
        return context.handle(params);
    }

    @Override
    public String toString() {
        return "VerifyingDataFromClient";
    }
}
