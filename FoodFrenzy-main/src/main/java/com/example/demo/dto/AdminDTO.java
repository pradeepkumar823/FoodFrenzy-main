package com.example.demo.dto;

import com.example.demo.entities.Admin;

public class AdminDTO {
    private int id;
    private String name;
    private String email;
    private String password;
    private String phoneNumber;

    // Default constructor for Spring MVC
    public AdminDTO() {}

    //convert entity to dto
    public static AdminDTO fromEntity(Admin admin){
        if(admin==null)return null;
        AdminDTO dto=new AdminDTO();
        dto.setId(admin.getAdminId());
        dto.setName(admin.getAdminName());
        dto.setEmail(admin.getAdminEmail());
        dto.setPhoneNumber(admin.getAdminPhoneNumber());
        return dto;
    }
    public Admin toEntity(){
        Admin admin=new Admin();
        if(this.id != 0){
            admin.setAdminId(this.id);
        }
        admin.setAdminName(this.name);
        admin.setAdminEmail(this.email);
        admin.setAdminPassword(this.password);
        admin.setAdminPhoneNumber(this.phoneNumber);
        return admin;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
