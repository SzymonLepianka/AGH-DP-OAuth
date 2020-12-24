package Group28.OAuth.Controllers;

import Group28.OAuth.Domain.AuthCode;
import Group28.OAuth.Model.Context;
import Group28.OAuth.Model.VerifyingDataFromClient;
import Group28.OAuth.Model.Response;
import com.fasterxml.jackson.core.JsonFactoryBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class WebController {

//    @RequestMapping("/auth/code/success")
//    @ResponseBody
//    public String veryficationClientIdSuccess() throws SQLException {
//        return "zalogowano i stworzono auth code (hope so)";
//    }
//
//    @RequestMapping("/auth/code/failure")
//    @ResponseBody
//    public String veryficationClientIdFailure() {
//        return "to kiedyś przekieruje do redirect URL klienta z kodem failure";
//    }

    @GetMapping("/auth/code")
    @ResponseBody
    public String authCode(@RequestParam String clientId, @RequestParam String scopes) throws SQLException {

        // wrzucam parametry requestu do Map
        Map<String, String> params = new HashMap<>();
        params.put("clientID", clientId);
        params.put("scopes", scopes);

        // tworzę Context i ustawiam stan na VerifyingDataFromClient
        Context context = new Context();
        context.changeState(new VerifyingDataFromClient());

        // wywołuję metodę handle
        // w optymistycznym scenariuszu wywoła się:
        // VerifyingDataFromClient handle -> CreatingAuthorizationCode handle -> RedirectingToAppRedirectURL handle
        // i zwróci obiekt Response z redirectURL i obiektem authCode
        Response response = context.handle(params);

//        AuthCode authCode = (AuthCode)response.content;
//        System.out.println(authCode.getContent());
//        System.out.println(response.redirect);



        // TODO:
        // tu się wywoła view

        // jestesmy we view:
//        AuthCode authCode;
//        if (response.content instanceof AuthCode){
//            authCode = (AuthCode) response.content;
//        }
//        else {
//            throw new ;
//        }

        // zwóci coś
        // i to coś do return

        return "tu będzie coś od view co zawróci klienta na jego URI z paramentrem code / albo kod o błędzie";
    }
}