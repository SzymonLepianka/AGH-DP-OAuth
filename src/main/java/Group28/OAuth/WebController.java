package Group28.OAuth;

import Group28.OAuth.DAO.AppsAccessObject;
import Group28.OAuth.Domain.ClientApp;
import Group28.OAuth.Model.Context;
import Group28.OAuth.Model.State;
import Group28.OAuth.Model.VerifyingDataFromClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class WebController {

    @RequestMapping("/test")
    @ResponseBody
    public String index() {
        return "Zalogowano!";
    }

    @RequestMapping("/login.html")
    public String login() {
        return "login.html";
    }

    @RequestMapping("/login-error.html")
    public String loginerror(Model model) {
        model.addAttribute("loginError", true);
        return "login.html";
    }

    @GetMapping("/auth/code") //ta metoda będzie widoczna pod tym endpointem
    @ResponseBody
    public String authCode(@RequestParam String clientId, @RequestParam String scopes) throws SQLException {
        Map<String, String> params = new HashMap<>();
        params.put("clientID", clientId);
        params.put("scopes", scopes);
//        VerifyingDataFromClient verifyingDataFromClient = new VerifyingDataFromClient();
//        verifyingDataFromClient.updateState(params);
        State state = new VerifyingDataFromClient();
        Context context = new Context(state);
        state.updateState(context, params);

        return "Here should be authorization code flow mordo " + params.toString();
    }
}
