package com.example.demo.repositories;


import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.entities.Admin;

public interface AdminRepository extends CrudRepository<Admin, Integer>
{
	Optional <Admin> findByAdminId(int adminId);
	Optional<Admin>findByAdminEmail(String Email);
	// Optional<Admin>findByAdminPhoneNumber(String adminPhoneNumber);
}	