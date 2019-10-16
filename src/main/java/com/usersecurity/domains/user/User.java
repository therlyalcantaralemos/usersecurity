package com.usersecurity.domains.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document
public class User {
    @Id
    private String id;
    private String name;
    private String email;
    private String password;
    private List<Role> roles;
    private boolean active;
    private LocalDateTime lastAccess;

    @Version
    @JsonIgnore
    private Long version;

    @LastModifiedDate
    private LocalDateTime updatedAt;
    @CreatedDate
    private LocalDateTime createdAt;

}
