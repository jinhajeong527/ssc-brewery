package guru.sfg.brewery.web.controllers.api;


import guru.sfg.brewery.domain.Beer;
import guru.sfg.brewery.repositories.BeerRepository;
import guru.sfg.brewery.web.controllers.BaseIT;
import guru.sfg.brewery.web.model.BeerStyleEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@WebMvcTest
@SpringBootTest
public class BeerRestControllerIT extends BaseIT {
    @Autowired
    BeerRepository beerRepository;

    /*
        @Test
        void deleteBeerWithURLParameterAndBadCredential() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/97df0c39-90c4-4ae0-b663-453e8e19c311")
    //                        .queryParam("apiKey", "spring")
                            .param("apiKey", "spring")
    //                        .queryParam("apiSecret", "guruxxxxx"))
                            .param("apiSecret", "guruxxxxx"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void deleteBeerWithURLParameterCredential() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/97df0c39-90c4-4ae0-b663-453e8e19c311")
                            .queryParam("apiKey", "spring")
                            .queryParam("apiSecret", "guru"))
                    .andExpect(status().isOk());
        }

        @Test
        void deleteBeerWithBadCreds() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/97df0c39-90c4-4ae0-b663-453e8e19c311")
                            .header("Api-Key", "spring").header("Api-Secret", "guruxxx"))
                    .andExpect(status().isUnauthorized());
        }
    */
    @DisplayName("Delete Tests")
    @Nested
    class DeleteTests {
        public Beer beerToDelete() {
            Random rand = new Random();

            return beerRepository.saveAndFlush(Beer.builder()
                    .beerName("Delete Me Beer")
                    .beerStyle(BeerStyleEnum.IPA)
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .upc(String.valueOf(rand.nextInt(99999999)))
                    .build());
        }

        // @WebMvcTest -> @SpringBootTest로 바뀌면서, 리포지토리 목객체를 사용하는 것이 아니라 실제로 데이터 베이스에서 데이터를 조회하게 되면서
        // 기존의 임의 beer id는 해당 엔티티 아이디로 존재하지 않는다는 에러 뱉게되면서 위와 같이 실제 beer 저장 후 아이디 가져오는 식으로 테스트가 수정되었다.
        @Test
        void deleteBeerHttpBasic() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId())
                            .with(httpBasic("spring", "guru")))
                    .andExpect(status().is2xxSuccessful());
        }

        @Test
        void deleteBeerHttpBasicUserRole() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId())
                            .with(httpBasic("user", "password")))
                    .andExpect(status().isForbidden());
        }

        @Test
        void deleteBeerHttpBasicCustomerRole() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId())
                            .with(httpBasic("scott", "tiger")))
                    .andExpect(status().isForbidden());
        }

        @Test
        void deleteBeerNoAuth() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId()))
                    .andExpect(status().isUnauthorized());
        }

    }

    @Test
    void findBeers() throws Exception {
        mockMvc.perform(get("/api/v1/beer/"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void findBeerById() throws Exception {
        Beer beer = beerRepository.findAll().get(0);

        mockMvc.perform(get("/api/v1/beer/"+beer.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void findBeerByUpc() throws Exception {
        Beer beer = beerRepository.findAll().get(0);

        mockMvc.perform(get("/api/v1/beerUpc/"+beer.getUpc()))
                .andExpect(status().isUnauthorized());
    }
}
