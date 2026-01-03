package com.web.ecommerce.security;

import com.web.ecommerce.model.enums.AppRole;
import com.web.ecommerce.model.Role;
import com.web.ecommerce.model.User;
import com.web.ecommerce.repository.RoleRepository;
import com.web.ecommerce.repository.UserRepository;
import com.web.ecommerce.security.jwt.AuthEntryPointJwt;
import com.web.ecommerce.security.jwt.AuthTokenFilter;
import com.web.ecommerce.security.services.UserDetailsServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
import java.util.Set;

@Configuration
@EnableWebSecurity
//@EnableMethodSecurity
public class WebSecurityConfig {
    @Autowired
    UserDetailsServiceImp userDetailsService;
    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean//الخلاصة: عملنا الفلتر @Bean لأننا بدنا نتحكم بتسجيله وإنشائه وإدراجه في سلسلة الفلاتر. الـ @Autowired بنستخدمها لما يكون الكائن مُسجّل مسبقًا كـ Bean (مثلاً بـ @Component).
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }


    public DaoAuthenticationProvider authenticationProvider() {//AuthenticationProvider
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    //هو المسؤول الأساسي عن عملية المصادقة في Spring Security.
    //
    //وظيفته: يستقبل Authentication (مثلاً username + password) ويحاول يعمل authenticate باستخدام الـ AuthenticationProvider المسجّل (زي DaoAuthenticationProvider).

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .exceptionHandling(exception->exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests.requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
//                        .requestMatchers("/api/public/**").permitAll()
//                        .requestMatchers("/api/admin/**").permitAll()
                        .requestMatchers("/api/test/**").permitAll()
                        .requestMatchers("/images/**").permitAll()
                        .anyRequest().authenticated());

http.authenticationProvider(authenticationProvider());
http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
//for h2 console
http.headers(headers ->headers.frameOptions(
        HeadersConfigurer.FrameOptionsConfig::sameOrigin
));
      return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web-> web.ignoring().requestMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**"));
    }
    
 //i added it for connection between front and back 
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    //to create user data when we launch project because h2 database doesnt keep data when it shut off
    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Retrieve or create roles


            Role customerRole = roleRepository.findByRoleName(AppRole.ROLE_CUSTOMER)
                    .orElseGet(() -> {
                        Role newSellerRole = new Role(AppRole.ROLE_CUSTOMER);
                        return roleRepository.save(newSellerRole);
                    });

            Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                    .orElseGet(() -> {
                        Role newAdminRole = new Role(AppRole.ROLE_ADMIN);
                        return roleRepository.save(newAdminRole);
                    });


            Set<Role> customerRoles = Set.of(customerRole);
            Set<Role> adminRoles = Set.of(customerRole, adminRole);


            // Create users if not already present

            if (!userRepository.existsByUsername("customer")) {
                User customer = new User("abdalkrem","ali","customer", "customer@example.com","00899938822",passwordEncoder.encode("customer"));
                userRepository.save(customer);
            }

            if (!userRepository.existsByUsername("admin")) {
                User admin = new User("abdalkrem","ali","admin", "admin@example.com","00899938821", passwordEncoder.encode("admin"));
                userRepository.save(admin);
            }

            // Update roles for existing users


            userRepository.findByUsername("customer").ifPresent(customer -> {
                customer.setRoles(customerRoles);
                userRepository.save(customer);
            });

            userRepository.findByUsername("admin").ifPresent(admin -> {
                admin.setRoles(adminRoles);
                userRepository.save(admin);
            });
        };
    }


}
