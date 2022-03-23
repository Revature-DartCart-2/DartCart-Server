package com.revature.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.revature.models.ResetPassword;

@Repository
public interface ResetPasswordRepo extends CrudRepository<ResetPassword, String> {

	@Query(
			value = "select * from reset_password_emails where user_id=?", 
			nativeQuery = true
	)
	Iterable<ResetPassword> findAllByUserId(int id);
}
