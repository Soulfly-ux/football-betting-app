package _bg.footballbettingapp.config;



import org.springframework.boot.security.autoconfigure.web.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // да се върне статични файлове
                        .requestMatchers("/", "/register").permitAll()
                        .anyRequest().permitAll() // засега никакъв login
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/home")
                        .failureUrl("/login?error")
                        .permitAll()

                )



        ;

        // Махаш formLogin/Logout за момента – няма нужда
        return http.build();
    }
}
