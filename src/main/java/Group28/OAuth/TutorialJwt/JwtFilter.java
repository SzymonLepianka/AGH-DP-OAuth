//package Group28.OAuth.TutorialJwt;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//import java.rmi.ServerException;
//
//public class JwtFilter implements javax.servlet.Filter {
//    // w ramach tej klasy mówi co ma się stać żeby ktoś się dostał pod /api/hello
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
//        String header = httpServletRequest.getHeader("authorization"); //sprawdza poprawność tokenu
//        //tokem = "Bearer "+reszta
//        //sprawdzam czy token zaczyna się od "Bearer ":
//        if (httpServletRequest == null || !header.startsWith("Bearer ")) {
//            throw new ServerException("Wrong or empty header, przyjmuję tylko Bearer");
//        } else {
//            try {
//                //przed wykonaniem tego psrawdzić czy użytkownik znajduje się w bazie
//                String token = header.substring(7); // ucinam pierwsze 7 znaków
//
//                Claims claims = Jwts.parser().setSigningKey("szymek123") // wyciągam wszsytkie informacje jakie potrzzebuje z tokenu, szymek123 = klucz do parsowania
//                        .parseClaimsJws(token) //podaję token który będzie parsowany
//                        .getBody(); //wyciągam
//
//                request.setAttribute("claims", claims);
//            } catch (Exception e) {
//                throw new ServerException("Wrong key (password)");
//
//            }
//
//        }
//        //jeśli claims są prawidłowe to przekazuje do chain
//        //chain przekazuje elementy od klienta do serwera
//        chain.doFilter(request, response);
//
//    }
//}
