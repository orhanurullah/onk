package com.onk.security.jwt;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class JwtTokenBlackList {

    private final Set<String> blackList = new HashSet<>();

    public void addBlackListToken(String token){
        this.blackList.add(token);
    }

    public boolean isContainsBlackListToken(String token){
        return blackList.contains(token);
    }
}
