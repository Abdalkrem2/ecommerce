package com.web.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long userId;
//    @Column(nullable = false, length = 50)
    private String firstName;

//    @Column(nullable = false, length = 50)
    private String lastName;
    @Column(nullable = false, unique = true, length = 50)
    private String username;
    @JsonIgnore
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, unique = true, length = 120)
    private String email;
    @Column(unique = true, length = 20)
    private String phone;

    @CreationTimestamp
    private Instant createdAt;

    public User(String firstName, String lastName, String username, String email,String phone, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    @UpdateTimestamp
    private Instant updatedAt;

    public User( String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }


    @ManyToMany(fetch = FetchType.EAGER ,cascade = CascadeType.MERGE) //check if lazy is better or not.
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name ="user_id"),inverseJoinColumns = @JoinColumn(name="role_id"))
    private Set<Role> roles= new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses= new ArrayList<>();
}
