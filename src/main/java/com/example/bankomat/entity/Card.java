package com.example.bankomat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class Card implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Size(min = 16,max = 16)
    @Column(nullable = false,unique = true)
    private String specialNumber;
    private String bankOfCard;
    private String cvvCode;
    private String cardHoldername;
    private String cardHoldersurname;
    private LocalDate expireDate;
    private Integer cardInMoney;
    @Size(min = 4,max=4)
    @Column(nullable = false,unique = true)
    private String pincode;
    @ManyToOne
    private CardType cardType;
    @ManyToMany
    private Set<Role> role;
    private Integer blockedCount=0;
    private boolean enabled;
    @CreationTimestamp
    private Timestamp createdAt;
    @CreatedBy
    private UUID createdBy;
    @LastModifiedBy
    private UUID updatedBy;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.role;
    }

    @Override
    public String getPassword() {
        return this.pincode;
    }

    @Override
    public String getUsername() {
        return this.specialNumber;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
