package Group28.OAuth.Controllers;

import Group28.OAuth.Model.*;
import Group28.OAuth.Model.State.AuthenticatingClient;
import Group28.OAuth.Model.State.Context;
import Group28.OAuth.Model.State.Response;
import Group28.OAuth.View.APIView;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/api")
public class APIController {

    private final Context context;
    private final APIView view;


    public APIController() {
        this.context = new Context();
        this.view = new APIView();
    }

    @GetMapping("/validateToken")
    public @ResponseBody
    String validateToken(@RequestParam String accessToken) throws SQLException {

        boolean response = ValidateToken.validateToken(accessToken);
        /* funkcja zwraca false gdy:
            - minął expiration time
            - nie ma tokenu o takich parametrach
         */
        return view.validToken(response);
    }

    @GetMapping(value = "/createToken", params = "authCode")
    public @ResponseBody
    String createToken(@RequestParam String clientID, @RequestParam String authCode, HttpServletResponse httpServletResponse) throws SQLException {
        Map<String, String> params = new HashMap<>();
        params.put("clientID", clientID);
        params.put("code", authCode);
        context.changeState(new AuthenticatingClient());
        Response response = context.handle(params);
        // response.content to obiekt AuthCode
        view.createToken(response, httpServletResponse);
        return "Token was created successfully";
    }

    @GetMapping("/createToken")
    public @ResponseBody
    String createTokenFromCookie(@RequestParam String clientID, HttpServletResponse httpServletResponse) throws SQLException {
        String authCode="";
        try {
            authCode = CheckAuthCodeCookie.Check(httpServletResponse);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ResponseStatusException responseStatusException) {
            if (responseStatusException.getStatus() == HttpStatus.UNAUTHORIZED) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "AuthCode cookie is not set");
            }
        }
        return createToken(clientID, authCode, httpServletResponse);
    }

    @GetMapping("/refreshToken")
    public @ResponseBody
    String refreshToken(@RequestParam String clientID, @RequestParam String refreshToken, HttpServletResponse httpServletResponse) throws SQLException {
        Authorization.Authorize(httpServletResponse);
        Map<String, String> params = new HashMap<>();
        params.put("clientID", clientID);
        params.put("refreshToken", refreshToken);
        context.changeState(new AuthenticatingClient());
        Response response = context.handle(params);
        view.refreshToken(response, httpServletResponse);
        return "Token was refreshed successfully";
    }

    @GetMapping("/revokeToken")
    public @ResponseBody
    String revokeToken(@RequestParam String clientID, @RequestParam String accessToken,  HttpServletResponse httpServletResponse) throws SQLException {

        try {
            Authorization.Authorize(httpServletResponse);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ResponseStatusException responseStatusException) {
            if (responseStatusException.getStatus() == HttpStatus.UNAUTHORIZED) {
                httpServletResponse.addHeader("Location", "/web/login?clientID="+clientID);
                httpServletResponse.setStatus(302);
            }
        }
        boolean response = RevokeToken.revokeToken(Long.parseLong(clientID), accessToken);
        System.out.println(response);

        /* funkcja zwraca true gdy udało się zrobić revoke
           w przeciwnym przypadku wyrzuca Bad Request / IllegalStateException
         */

        return view.revokeToken(response);
    }

    @GetMapping("/revokeGrantType")
    public @ResponseBody
    String revokeGrantType(@RequestParam String clientID, @RequestParam String authCode, HttpServletResponse httpServletResponse) throws SQLException {

        try {
            Authorization.Authorize(httpServletResponse);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ResponseStatusException responseStatusException) {
            if (responseStatusException.getStatus() == HttpStatus.UNAUTHORIZED) {
                httpServletResponse.addHeader("Location", "/web/login?clientID=" + clientID);
                httpServletResponse.setStatus(302);
            }
        }

        boolean response = RevokeGrantType.revokeGrantType(Long.parseLong(clientID), authCode);
        System.out.println(response);

        /* funkcja zwraca true gdy udało się zrobić revoke
           w przeciwnym przypadku wyrzuca Bad Request
         */
        return view.revokeGrantType(response);
    }

    @GetMapping("/getUserData")
    public @ResponseBody
    String getUserData(@RequestParam String clientID, @RequestParam String accessToken, HttpServletResponse httpServletResponse) throws SQLException {

        try {
            Authorization.Authorize(httpServletResponse);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ResponseStatusException responseStatusException) {
            if (responseStatusException.getStatus() == HttpStatus.UNAUTHORIZED) {
                httpServletResponse.addHeader("Location", "/web/login?clientID="+clientID);
                httpServletResponse.setStatus(302);
            }
        }

        JSONObject userData = GetUserData.getUserData(Long.parseLong(clientID), accessToken);
        System.out.println(userData);

        /* funkcja zwraca JSONObject gdy accessToken jest valid
           przykład: {"user_email":"slepianka@wp.pl2","user_username":"slepianka2"}
           scopes muszą być zdefinowane w bazie: user_birthdate, user_email, user_firstname, user_phonenumber, user_surname, user_username
           w przeciwnym przypadku wyrzuca Bad Request
         */
        return userData.toString();
    }
}
