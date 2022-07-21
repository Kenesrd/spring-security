package com.example.services;

import com.example.entities.Role;
import com.example.entities.User;
import com.example.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByUserName(String username){
       return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
//    @Secured(value = "ADMIN")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUserName(username);
        if (user == null){
            throw new UsernameNotFoundException(String.format("User %s not found", username));
        }

        return new org.springframework.security.core.userdetails
                .User(user.getUsername(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

//    Берет пачку ролей и делает пачку Authorities с точно такими же строками
    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

}
