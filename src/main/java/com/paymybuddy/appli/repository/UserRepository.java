package com.paymybuddy.appli.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.appli.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer>  {

}
