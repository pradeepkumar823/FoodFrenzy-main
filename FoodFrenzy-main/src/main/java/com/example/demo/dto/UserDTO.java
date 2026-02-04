package com.example.demo.dto;

import com.example.demo.entities.User;

public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String role;
    private String phoneNumber;
    private String address;

    // Default constructor for Spring MVC
    public UserDTO() {}

    //convert entity to DTO
    public static UserDTO fromEntity(User user){
        if(user == null)return null;
        UserDTO dto =new UserDTO();
        dto.setId((long)user.getUserId());
        dto.setName(user.getUserName());
        dto.setEmail(user.getUserEmail());
        dto.setPhoneNumber(user.getUserPhoneNumber()!=null ? user.getUserPhoneNumber():null);
        dto.setRole("USER");
        return dto;
    }
    //convert DTO to entity
    public User toEntity(){
        User user=new User();
        if(this.id != null){
            user.setUserId(this.id.intValue());
        }
        user.setUserName(this.name);
        user.setUserEmail(this.email);
        user.setUserPassword(this.password);
        if(this.phoneNumber != null){
            user.setUserPhoneNumber(this.phoneNumber);
        }
        return user;
    }
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
