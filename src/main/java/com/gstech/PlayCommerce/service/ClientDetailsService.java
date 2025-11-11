package com.gstech.PlayCommerce.service;

import com.gstech.PlayCommerce.repository.ClientRepository;
import com.gstech.PlayCommerce.security.MyUserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ClientDetailsService implements UserDetailsService {

    @Autowired
    private ClientRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        var client = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente com email " + email + " não encontrado"));

        return new MyUserPrincipal(client);
    }
}
