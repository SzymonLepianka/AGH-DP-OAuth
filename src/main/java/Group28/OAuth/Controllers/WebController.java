package Group28.OAuth.Controllers;

import Group28.OAuth.Model.Authorization;
import Group28.OAuth.Model.LogInUser;
import Group28.OAuth.View.WebView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

@Controller
@RequestMapping(path="/web", produces = MediaType.TEXT_HTML_VALUE)
public class WebController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping(value = "/login", params = "clientID")
    public String loginFormWithClientID(@RequestParam String clientID, HttpServletResponse httpServletResponse, Model model) {
        model.addAttribute("clientID", clientID);

        try {
            Authorization.Authorize(httpServletResponse);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ResponseStatusException responseStatusException) {
            if (responseStatusException.getStatus() == HttpStatus.UNAUTHORIZED) {
                return "loginForm";
            }
        }
        return "AlreadyLogged";
    }

    @GetMapping("/login")
    public String loginForm(HttpServletResponse httpServletResponse, Model model) {
        model.addAttribute("clientID", 1);

        try {
            Authorization.Authorize(httpServletResponse);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ResponseStatusException responseStatusException) {
            if (responseStatusException.getStatus() == HttpStatus.UNAUTHORIZED) {
                return "loginForm";
            }
        }
        return "AlreadyLogged";
    }

    @PostMapping("/login")
    public @ResponseBody String handleLogin(@RequestParam String clientID, @RequestParam String username, @RequestParam String password, HttpServletResponse httpServletResponse) {
        try {
            var modelResponse = LogInUser.handle(username, password, clientID, passwordEncoder);
            return WebView.LoginView(modelResponse, httpServletResponse);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
