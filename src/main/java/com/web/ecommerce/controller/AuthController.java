package com.web.ecommerce.controller;

import com.web.ecommerce.exceptions.APIException;
import com.web.ecommerce.model.enums.AppRole;
import com.web.ecommerce.model.Role;
import com.web.ecommerce.model.User;
import com.web.ecommerce.repository.RoleRepository;
import com.web.ecommerce.repository.UserRepository;
import com.web.ecommerce.security.jwt.JwtUtils;
import com.web.ecommerce.security.request.LoginRequest;
import com.web.ecommerce.security.request.SignUpRequest;
import com.web.ecommerce.security.response.LoginResponse;
import com.web.ecommerce.security.response.MessageResponse;
import com.web.ecommerce.security.services.UserDetailsImp;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest){
        Authentication authentication;
        try{
            authentication=authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
        }catch (Exception e){
            Map<String,Object> map = new HashMap<>();
            map.put("message","Bad credentials");
            map.put("status",false);
            return new ResponseEntity<Object>(map, HttpStatus.UNAUTHORIZED);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImp userDetails = (UserDetailsImp) authentication.getPrincipal();
        ResponseCookie jwtCookie=jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        LoginResponse response=new LoginResponse(userDetails.getId(),jwtCookie.toString(),userDetails.getUsername(),roles);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(response);
    }

    @PostMapping("/signup")
            public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest){
            if(userRepository.existsByUsername(signUpRequest.getUsername())){
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Username is already in use"));
            }
        if(userRepository.existsByEmail(signUpRequest.getEmail())){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use"));
        }

        User user=new User(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword())
        );

        Set<String> strRoles=signUpRequest.getRole();
        Set<Role> roles=new HashSet<>();

        if(strRoles==null){
            Role userRole=roleRepository.findByRoleName(AppRole.ROLE_CUSTOMER).orElseThrow(()->new RuntimeException("Error: Role is not found"));
        roles.add(userRole);
        }else {
            strRoles.forEach(role->{
               switch (role){
                   case "admin": Role adminRole= roleRepository.findByRoleName(AppRole.ROLE_ADMIN).orElseThrow(()->new RuntimeException("Error: Role is not found"));
                   roles.add(adminRole);
                       break;
                   case "customer":Role cutomerRole= roleRepository.findByRoleName(AppRole.ROLE_CUSTOMER).orElseThrow(()->new RuntimeException("Error: Role is not found"));
                   roles.add(cutomerRole);
                   break;

               }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));

    }


    @GetMapping("/username")
    public   ResponseEntity<?> currentUserName(Authentication authentication){
        if (authentication == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        return new ResponseEntity<>(authentication.getName(),HttpStatus.OK) ;
    }



    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(Authentication authentication){
        if (authentication == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        UserDetailsImp userDetails = (UserDetailsImp) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        LoginResponse response=new LoginResponse(userDetails.getId(),userDetails.getUsername(),roles);

        return ResponseEntity.ok().body(response);
    }


    @PostMapping("/signout")
    public ResponseEntity<?> signOutUser(HttpServletRequest request){
        String token = jwtUtils.getJwtFromCookie(request); // or parseJwt(request) if you want cookie + header

        // treat null/blank as already logged out
        if (token == null || token.isBlank()) {
            return ResponseEntity.status(401).body(new MessageResponse("You are already logged out"));
            // or: return ResponseEntity.noContent().build();
        }


        ResponseCookie cookie=jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(new MessageResponse("Signed out successfully"));
    }

}
