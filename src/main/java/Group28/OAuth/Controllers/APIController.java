package Group28.OAuth.Controllers;

import Group28.OAuth.Model.AuthenticatingClient;
import Group28.OAuth.Model.Context;
import Group28.OAuth.Model.Response;
import Group28.OAuth.View.APIView;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/api")
public class APIController {
    private AuthenticatingClient model;
    private APIView view;

//    public APIController(AuthenticatingClient model, APIView view) {
//        this.model = model;
//        this.view = view;
//    }

    //TODO Bartek
//    @GetMapping("/validateToken")
//    public @ResponseBody
//    String validateToken(@RequestParam String clientId, @RequestParam String scopes) throws SQLException {
//        Map<String, String> params = new HashMap<>();
//        params.put("clientID", clientId);
//        params.put("scopes", scopes);
//        Context context = new Context();
//        context.changeState(new VerifyingDataFromClient());
//        Response response = context.handle(params);
//
//        return "cos dla response";
//    }

    @GetMapping("/createToken")
    public @ResponseBody
    String createToken(@RequestParam String clientId, @RequestParam String authCode, HttpServletResponse httpServletResponse) throws SQLException {
        Map<String,String> params = new HashMap<>();
        params.put("clientID", clientId);
        params.put("code", authCode);
        Context context = new Context();
        Response response = context.handle(params);
        // response.content to obiekt AuthCode
        view.showToken(response, httpServletResponse);
        return "ok";
    }
    @GetMapping("/refreshToken")
    public @ResponseBody
    String refreshToken(@RequestParam String clientID, @RequestParam String refreshToken) throws SQLException {
        Map<String, String> params = new HashMap<>();
        params.put("clientID", clientID);
        params.put("refreshToken", refreshToken);

        Context context = new Context();
        context.changeState(new AuthenticatingClient());
        Response response = context.handle(params);
        // response.content to String[] - [accessToken, refreshToken]

        //TODO: tu się wywoła view

        return null;
    }


    //TODO Bartek
//    @GetMapping("/revokeToken")
//    public @ResponseBody
//    String revokeToken() throws SQLException {
//        return null;
//    }
//    @GetMapping("/revokeGrantType")
//    public @ResponseBody
//    String revokeGrantType() throws SQLException {
//        return null;
//    }
//    @GetMapping("/getUserData")
//    public @ResponseBody
//    String getUserData() throws SQLException {
//        return null;
//    }

}
