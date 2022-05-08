package com.example.demo.entity.important;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ALL,CUSTOMER,ADMINISTRATOR,TRAVEL_AGENCY,MODERATOR,COURIER;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
