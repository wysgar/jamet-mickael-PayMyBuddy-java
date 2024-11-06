package com.paymybuddy.appli.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.appli.model.DBUser;

@Repository
public interface UserRepository extends CrudRepository<DBUser, Integer>  {
	DBUser findByEmail(String email);

	DBUser findByUsername(String username);
}
