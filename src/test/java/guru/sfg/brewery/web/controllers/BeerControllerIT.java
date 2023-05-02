package guru.sfg.brewery.web.controllers;

import guru.sfg.brewery.repositories.BeerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by jt on 6/12/20.
 */
//@WebMvcTest
@SpringBootTest // Brings up full context
public class BeerControllerIT extends BaseIT {

    @Autowired
    BeerRepository beerRepository;

    @DisplayName("Init New Form")
    @Nested
    class InitNewForm {

        @ParameterizedTest(name= "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamAllUsers")
        void initCreationFormAuth(String user, String pwd) throws Exception {
            mockMvc.perform(get("/beers/new").with(httpBasic(user, pwd)))
                    .andExpect(status().isOk())
                    .andExpect(view().name("beers/createBeer"))
                    .andExpect(model().attributeExists("beer"));
        }

        @Test
        void initCreationFormNotAuth() throws Exception {
            mockMvc.perform(get("/beers/new"))
                    .andExpect(status().isUnauthorized());
        }

    }
// 아래 세개의 테스트를 @ParameterizedTest를 사용해 위와 같이 바꾸었다.
//    @Test
//    void initCreationFormWithScott() throws Exception {
//        mockMvc.perform(get("/beers/new").with(httpBasic("scott", "tiger")))
//                .andExpect(status().isOk())
//                .andExpect(view().name("beers/createBeer"))
//                .andExpect(model().attributeExists("beer"));
//    }
//    // Bcrypt 써볼 것
//    @Test
//    void initCreationFormWithSpring() throws Exception {
//        mockMvc.perform(get("/beers/new").with(httpBasic("spring", "guru")))
//                .andExpect(status().isOk())
//                .andExpect(view().name("beers/createBeer"))
//                .andExpect(model().attributeExists("beer"));
//    }
//
//    @Test
//    void initCreationForm() throws Exception {
//        mockMvc.perform(get("/beers/new").with(httpBasic("user", "password")))
//                .andExpect(status().isOk())
//                .andExpect(view().name("beers/createBeer"))
//                .andExpect(model().attributeExists("beer"));
//    }
    @WithMockUser("spring")
    @Test
    void findBeers() throws Exception{
        mockMvc.perform(get("/beers/find"))
                .andExpect(status().isOk())
                .andExpect(view().name("beers/findBeers"))
                .andExpect(model().attributeExists("beer"));
    }

    @Test
    void findBeersWithHttpBasic() throws Exception{
        mockMvc.perform(get("/beers/find").with(httpBasic("spring","guru")))
                .andExpect(status().isOk())
                .andExpect(view().name("beers/findBeers"))
                .andExpect(model().attributeExists("beer"));
    }

    @Test
    void findBeersWithHttpBasicCustomer() throws Exception{
        mockMvc.perform(get("/beers/find").with(httpBasic("scott","tiger")))
                .andExpect(status().isOk())
                .andExpect(view().name("beers/findBeers"))
                .andExpect(model().attributeExists("beer"));
    }

    @Test
    void findBeersWithHttpBasicUser() throws Exception{
        mockMvc.perform(get("/beers/find").with(httpBasic("user","password")))
                .andExpect(status().isOk())
                .andExpect(view().name("beers/findBeers"))
                .andExpect(model().attributeExists("beer"));
    }

    @Test
    void findBeersWithHttpBasicNoRole() throws Exception{
        mockMvc.perform(get("/beers/find"))
                .andExpect(status().isUnauthorized());
    }

}
