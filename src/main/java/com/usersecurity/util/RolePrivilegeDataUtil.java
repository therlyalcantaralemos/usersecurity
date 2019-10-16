package com.usersecurity.util;

import com.usersecurity.domains.user.Privilege;
import com.usersecurity.domains.user.Role;
import com.usersecurity.repositories.PrivilegeRepository;
import com.usersecurity.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
public class RolePrivilegeDataUtil implements ApplicationListener<ContextRefreshedEvent> {

    private final RoleRepository roleRepository;
    private PrivilegeRepository privilegeRepository;
    boolean alreadySetup = false;

    @Autowired
    public RolePrivilegeDataUtil(RoleRepository roleRepository, PrivilegeRepository privilegeRepository) {
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
            Privilege readPrivilege = createPrivilege("READ_PRIVILEGE");
            Privilege writePrivilege = createPrivilege("WRITE_PRIVILEGE");

            createRole("ROLE_ADMIN", Arrays.asList(readPrivilege, writePrivilege));
            createRole("ROLE_USER", Collections.singletonList(readPrivilege));

    }

    @Transactional
    private Privilege createPrivilege(String name) {
        Optional<Privilege> privilege = privilegeRepository.findByName(name);
        return (privilege.isEmpty()) ? privilegeRepository.save(new Privilege(null, name)) : privilege.get();
    }

    @Transactional
    private Role createRole(String name, List<Privilege> privileges) {
        Optional<Role> role = roleRepository.findByName(name);
        return (role.isEmpty()) ? roleRepository.save(new Role(null, name, privileges)) : role.get();
    }
}
