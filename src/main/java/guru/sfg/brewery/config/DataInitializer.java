//package guru.sfg.brewery.config;
//
//import guru.sfg.brewery.domain.security.Authority;
//import guru.sfg.brewery.domain.security.User;
//import guru.sfg.brewery.repositories.security.AuthorityRepository;
//import guru.sfg.brewery.repositories.security.UserRepository;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.HashSet;
//import java.util.Set;
//
//@Configuration
//public class DataInitializer {
//    @Bean
//    public CommandLineRunner initData(UserRepository repository, AuthorityRepository authorityRepository) {
//        return args -> {
//            User admin = new User();
//            admin.setUsername("spring");
//            admin.setPassword("{bcrypt}$2a$10$9WwPvn17BvR4a1qhbnJA8u/6LEsUXwZSEMsSYY7QmD6hnnnM.uy0a");
//            Authority authority = new Authority();
//            authority.setRole("ADMIN");
//            Set<Authority> set = new HashSet<>();
//            set.add(authority);
//            admin.setAuthorities(set);
//            authorityRepository.save(authority);
//            // Initialize your data here
//            repository.save(admin);
//
//            User user = new User();
//            admin.setUsername("user");
//            admin.setPassword("{sha256}122a76f891b85acb2c5d1bc5e8e76873b31a0c540121658097425e0e2ea2c5e0b6011e01efbc8341");
//            Authority userAuthority = new Authority();
//            userAuthority.setRole("USER");
//            Set<Authority> set2 = new HashSet<>();
//            set2.add(userAuthority);
//            authorityRepository.save(userAuthority);
//            user.setAuthorities(set2);
//            // Initialize your data here
//            repository.save(user);
//
//            User customer = new User();
//            admin.setUsername("user");
//            admin.setPassword("{bcrypt15}$2a$15$VPAVghq7J9aa87GjnwGai.dC8Y4GX9sXIvghSS.47PJq1RcrbCrLi");
//            Authority customerAuthority = new Authority();
//            userAuthority.setRole("CUSTOMER");
//            Set<Authority> set3 = new HashSet<>();
//            set3.add(customerAuthority);
//            authorityRepository.save(customerAuthority);
//            user.setAuthorities(set3);
//            // Initialize your data here
//            repository.save(customer);
//
//        };
//    }
//}
//
