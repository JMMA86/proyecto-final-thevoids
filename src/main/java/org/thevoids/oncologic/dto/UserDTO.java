package org.thevoids.oncologic.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

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
    private String roleName;
    private String status;
    private String password;

    public UserDTO() {
    }
}