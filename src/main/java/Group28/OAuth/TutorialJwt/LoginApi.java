//package Group28.OAuth.TutorialJwt;
//
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Date;
//
//@RestController
//public class LoginApi {
//
//    @PostMapping("/login1") //żądanie POST
//    public String get(@RequestBody User user){ //po dostarczeniu usera będzie możliwe zalogowanie się
//        //elementem zwracanym będzie JWT
//        //potrzebny jest mechanizm filtra. filter przechwyca dane rządanie
//        //zanim uzytkownik dostanie się do danego żadania będę chciał sprawdzić czy w ramach teg ożadania jest stwozony odpowieni token
//        //tworzę token, podaję co ma się w nim znajdować:
//        long currentTimeMillis = System.currentTimeMillis();
//        return Jwts.builder()
//                .setSubject(user.getLogin()) // podaję kto jest uzytkownikkiem - kto otrzymuje klucz
//                .claim("roles","user") //klucz, wartość, np.rola
//                .setIssuedAt(new Date(currentTimeMillis)) //ustawienie czasu zaczęcia ważności tokenu = chwila tworzenia tokenu
//                .setExpiration(new Date(currentTimeMillis + 20 * 1000)) //kiedy wygasa; 1000 = 1s
//                .signWith(SignatureAlgorithm.HS512, user.getPassword())
//                .compact(); //sprowadza do stringa
//    }
//}
