package org.thevoids.oncologic.dto.entity;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private Long userId;
    private String fullName;
    private String identification;
    private Date birthDate;
    private String gender;
    private String address;
    private String phoneNumber;
    private String email;
    private long roleId;
    private String status;
    private String password;

    public UserDTO() {
    }
} 