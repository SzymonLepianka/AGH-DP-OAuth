package Group28.OAuth.Controllers;

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

    @RequestMapping("/auth/code/success")
    @ResponseBody
    public String veryficationClientIdSuccess() {
        return "zalogowano";
    }

    @RequestMapping("/auth/code/failure")
    @ResponseBody
    public String veryficationClientIdFailure() {
        return "to kiedyś przekieruje do redirect URL klienta z kodem failure";
    }


    @GetMapping("/auth/code")
//    @ResponseBody
    public String authCode(@RequestParam String clientId, @RequestParam String scopes) throws SQLException {
        Map<String, String> params = new HashMap<>();
        params.put("clientID", clientId);
        params.put("scopes", scopes);
        State state = new VerifyingDataFromClient();
        Context context = new Context(state);
        if (state.validate(context, params)){
            // użytkownik z danym clientID istnieje
            return "redirect:/auth/code/success";
        }
        else {
            //failure
            return "redirect:/auth/code/failure";
        }
    }
}
