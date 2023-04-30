package guru.sfg.brewery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //super.configure(http);
        http
                .authorizeRequests(authorize -> {
                    authorize
                            .antMatchers("/", "login", "/webjars/**", "/resources/**").permitAll()
                            .antMatchers("/beers/find", "/beers").permitAll()
                            .antMatchers(HttpMethod.GET, "/api/v1/beer/**").permitAll()
                            .mvcMatchers(HttpMethod.GET, "/api/v1/beerUpc/{upc}").permitAll();
                })
                // 어떤 요청에 대해서든 authorization을 하겠다는 것이므로 그전에 다른 authorization 관련 코드를 작성해야 한다.
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .and()
                .httpBasic();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
//        return NoOpPasswordEncoder.getInstance();
//        return new LdapShaPasswordEncoder();
//        return new StandardPasswordEncoder();
//        return new BCryptPasswordEncoder();
        // PasswordEncoderFactories 안의 키를 확인하여 비밀 번호 앞에 붙여주면 된다.
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        super.configure(auth);
        auth.inMemoryAuthentication()
                .withUser("spring")
                .password("{bcrypt}$2a$10$9WwPvn17BvR4a1qhbnJA8u/6LEsUXwZSEMsSYY7QmD6hnnnM.uy0a") // Password Encoder 설정한 것이 아니면 {noop}과 같이 인코더 지정 해주어야 한다.
                .roles("ADMIN")
                .and()
                .with User("user")
                .password("{sha256}122a76f891b85acb2c5d1bc5e8e76873b31a0c540121658097425e0e2ea2c5e0b6011e01efbc8341")
                .roles("USER")
                .and()
                .withUser("scott")
                .password("{ldap}{SSHA}iCvvBdFTHVw4uQemNRjAxT88bOc6K03C4ZThJA==")
                .roles("CUSTOMER");
    }
    //    @Override
//    @Bean // 이렇게 @Bean 설정 해주지 않으면 Spring Context로 가져와지지 않는다.
//    protected UserDetailsService userDetailsService() {
////        return super.userDetailsService();
//        UserDetails admin = User.withDefaultPasswordEncoder()
//                .username("spring")
//                .password("guru")
//                .roles("ADMIN")
//                .build();
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username("user")
//                .password("password")
//                .roles("USER")
//                .build();
//        return new InMemoryUserDetailsManager(admin, user);
//    }
}
