package example;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.rsocket.EnableRSocketSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@Configuration
@EnableRSocketSecurity
public class SecurityConfiguration {
  @Bean
  MapReactiveUserDetailsService service() {
    var user = User.withDefaultPasswordEncoder()
        .username("user")
        .password("password")
        .roles("SETUP")
        .build();
    return new MapReactiveUserDetailsService(user);
  }
  
}
