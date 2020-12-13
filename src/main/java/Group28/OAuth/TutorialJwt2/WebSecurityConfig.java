//package Group28.OAuth.TutorialJwt2;
//import Group28.OAuth.TutorialJwt.JwtFilter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//@Configuration
//public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//
//    //autoryzacja typu basic: (login i hasło)
////    @Bean
////    PasswordEncoder getPasswordEncoder(){
////        return new BCryptPasswordEncoder();
////    }
////
////
////    //definicja użytkowników inmemory:
////    @Override
////    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
////        auth.inMemoryAuthentication().withUser(User.builder()
////                        .username("test")
////                        .password(getPasswordEncoder().encode("test"))
////                        .roles("ADMIN"));
////
////    }
//
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests().antMatchers("/test2").authenticated()
//                .antMatchers("/test3").hasRole("ADMIN")
//                .and().addFilter(new JwtFilter2(authenticationManager()));
////                .and().formLogin().permitAll(); //basic logowanie
//    }
//}
