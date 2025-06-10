package com.DM.dairyManagement.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.DM.dairyManagement.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(String email);  // Find user by email only



	;

}

