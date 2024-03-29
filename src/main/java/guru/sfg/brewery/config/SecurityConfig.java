package guru.sfg.brewery.config;

import guru.sfg.brewery.security.JpaUserDetailsService;
import guru.sfg.brewery.security.RestHeaderAuthFilter;
import guru.sfg.brewery.security.RestQueryParamAuthFilter;
import guru.sfg.brewery.security.SfgPasswordEncoderFactories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // 메서드에 @Secured (롤명시) 작동하게 하려면 필요한 어노테이션(프로퍼티까지 놓치지 말 것)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
//    public RestHeaderAuthFilter restHeaderAuthFilter(AuthenticationManager authenticationManager) {
//        RestHeaderAuthFilter filter = new RestHeaderAuthFilter(new AntPathRequestMatcher("/api/**"));
//        // authenticationManager setting
//        filter.setAuthenticationManager(authenticationManager);
//        return filter;
//    }
//
//    public RestQueryParamAuthFilter restQueryParamAuthFilter(AuthenticationManager authenticationManager) {
//        RestQueryParamAuthFilter filter = new RestQueryParamAuthFilter(new AntPathRequestMatcher("/api/**"));
//        filter.setAuthenticationManager(authenticationManager);
//        return filter;
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // restHeaderAuthFilter를 filter chain에 추가했음
//        http
//                .addFilterBefore(restHeaderAuthFilter(authenticationManager()),
//                        UsernamePasswordAuthenticationFilter.class)
//                .csrf().disable(); // globally set
//
//        http
//                .addFilterBefore(restQueryParamAuthFilter(authenticationManager()),
//                        UsernamePasswordAuthenticationFilter.class);

        http
                .authorizeRequests(authorize -> {
                    authorize
                            .antMatchers("/h2-console/**").permitAll() // do not use this in production !!
                            .antMatchers("/", "/login", "/webjars/**", "/resources/**").permitAll()
                            .antMatchers("/brewery/breweries/**").hasAnyRole("ADMIN","CUSTOMER")
                            .antMatchers(HttpMethod.GET, "/api/v1/beer/**").hasAnyRole("ADMIN","CUSTOMER","USER")
                            .antMatchers(HttpMethod.GET, "/brewery/api/v1/breweries").hasAnyRole("ADMIN","CUSTOMER")
                            .mvcMatchers(HttpMethod.DELETE, "/api/v1/beer/**").hasRole("ADMIN")
                            .mvcMatchers(HttpMethod.GET, "/api/v1/beerUpc/{upc}").hasAnyRole("ADMIN","CUSTOMER","USER")
                            .mvcMatchers("/beers/find", "/beers/{beerId}").hasAnyRole("ADMIN","CUSTOMER","USER");
                })
                // 어떤 요청에 대해서든 authorization을 하겠다는 것이므로 그전에 다른 authorization 관련 코드를 작성해야 한다.
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin().and()
                .httpBasic().and()
                .csrf().disable();

        // h2 console config
        http.headers().frameOptions().sameOrigin();
    }

    // Custom Password Encoder가 있고, Custom User Service Detail Service가 존재하기 때문에
    // 스프링 오토 컨피규레이션이 wire that into spring security 할 것이다.
    // 아래 configure 코멘트 아웃한 이유
    @Bean
    PasswordEncoder passwordEncoder() {
//        return NoOpPasswordEncoder.getInstance();
//        return new LdapShaPasswordEncoder();
//        return new StandardPasswordEncoder();
//        return new BCryptPasswordEncoder();
        // PasswordEncoderFactories 안의 키를 확인하여 비밀 번호 앞에 붙여주면 된다.
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        // Custom Delegate Password Encoder를 만들고 싶으면 아래와 같이 하면 된다.
        return SfgPasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
//    @Autowired
//    JpaUserDetailsService jpaUserDetailsService;
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        super.configure(auth);
//        auth.userDetailsService(this.jpaUserDetailsService).passwordEncoder(passwordEncoder());


//        auth.inMemoryAuthentication()
//                .withUser("spring")
//                .password("{bcrypt}$2a$10$9WwPvn17BvR4a1qhbnJA8u/6LEsUXwZSEMsSYY7QmD6hnnnM.uy0a") // Password Encoder 설정한 것이 아니면 {noop}과 같이 인코더 지정 해주어야 한다.
//                .roles("ADMIN")
//                .and()
//                .withUser("user")
//                .password("{sha256}122a76f891b85acb2c5d1bc5e8e76873b31a0c540121658097425e0e2ea2c5e0b6011e01efbc8341")
//                .roles("USER")
//                .and()
//                .withUser("scott")
//                .password("{bcrypt15}$2a$15$VPAVghq7J9aa87GjnwGai.dC8Y4GX9sXIvghSS.47PJq1RcrbCrLi")
//                .roles("CUSTOMER");
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

