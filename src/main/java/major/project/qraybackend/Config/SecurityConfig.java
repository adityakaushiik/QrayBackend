//package major.project.qraybackend.Config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class SecurityConfig {
//
//        @Bean
//        SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
//            http
//                    .authorizeRequests(authorizeRequests ->
//                            authorizeRequests
//                                    .anyRequest().authenticated()
//                    )
//            return http.build();
//        }
//}