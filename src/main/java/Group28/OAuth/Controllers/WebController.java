package Group28.OAuth.Controllers;

import Group28.OAuth.DAO.AppsAccessObject;
import Group28.OAuth.DAO.DatabaseEditor;
import Group28.OAuth.DAO.IDatabaseEditor;
import Group28.OAuth.Domain.AuthCode;
import Group28.OAuth.Domain.ClientApp;
import Group28.OAuth.Domain.User;
import Group28.OAuth.Model.Context;
import Group28.OAuth.Model.State;
import Group28.OAuth.Model.VerifyingDataFromClient;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Controller
public class WebController {

    @RequestMapping("/auth/code/success")
    @ResponseBody
    public String veryficationClientIdSuccess() throws SQLException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails){
            username = ((UserDetails) principal).getUsername();
        }
        else {
            username = principal.toString();
        }
        System.out.println(username);

        IDatabaseEditor db = DatabaseEditor.getInstance();
        List<User> users = db.getUsersAccessObject().readAll();
        Group28.OAuth.Domain.User user1 = users.stream()
                .filter(user -> username.equals(user.getUsername()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Student " + username + " does not exists (while creating auth code)"));

        AuthCode authCode = new AuthCode();

        byte[] array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);
        String generatedString = new String(array, StandardCharsets.UTF_8);

        authCode.setContent(generatedString);
        authCode.setExpiresAt(Timestamp.valueOf(LocalDateTime.now().plusHours(1)));
        authCode.setRevoked(false);
        authCode.setClientApp(db.getAppsAccessObject().readById(1L));
        authCode.setUser(user1);
        db.getAuthCodesAccessObject().create(authCode);

        return "zalogowano i stworzono auth code (hope so)";
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
