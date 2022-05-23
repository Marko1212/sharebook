package com.udemy.demo.user;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserInfo, Integer> {
	
	List<UserInfo> findByEmail(String email);

}
