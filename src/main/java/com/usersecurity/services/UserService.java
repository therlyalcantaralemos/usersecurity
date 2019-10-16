package com.usersecurity.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usersecurity.domains.user.Role;
import com.usersecurity.domains.user.User;
import com.usersecurity.domains.user.UserDTO;
import com.usersecurity.repositories.RoleRepository;
import com.usersecurity.repositories.UserRepository;
import com.usersecurity.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private ObjectMapper objectMapper;

    @Autowired
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       @Lazy BCryptPasswordEncoder bCryptPasswordEncoder,
                       ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.objectMapper = objectMapper;

    }

    public void create(UserDTO userDTO) {
        User user = objectMapper.convertValue(userDTO, User.class);
        user.setActive(true);
        encondePassword(user);
        getRoles(user, "ROLE_ADMIN");
        userRepository.save(user);
    }

    private void encondePassword(User user){
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    }

    private void getRoles(User user, String roleName){
        user.setRoles(Collections.singletonList(roleRepository.findByName(roleName).orElseThrow(ObjectNotFoundException::new)));

    }

    @Override
    public UserDetails loadUserByUsername(String email){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("username not found"));
        return buildUserForAuthentication(user, getUserAuthority(user.getRoles()));

    }

    private List<GrantedAuthority> getUserAuthority(List<Role> userRoles) {
        return userRoles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    private UserDetails buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }
}
