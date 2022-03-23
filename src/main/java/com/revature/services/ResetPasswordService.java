package com.revature.services;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.models.ResetPassword;
import com.revature.repositories.ResetPasswordRepo;

@Service
public class ResetPasswordService {
	
	private final ResetPasswordRepo resetPasswordRepo;
	
	@Autowired
	public ResetPasswordService(ResetPasswordRepo resetPasswordRepo) {
		this.resetPasswordRepo = resetPasswordRepo;
	}
	
	//Create
	@Transactional
	public ResetPassword createNewResetPassword(ResetPassword resetPassword) {
		return resetPasswordRepo.save(resetPassword);
	}
	//Read
	public Iterable<ResetPassword> findAll() {
		return resetPasswordRepo.findAll();
	}
	
	
	public Iterable<ResetPassword> findAllByUserId(int userId) {
		return resetPasswordRepo.findAllByUserId(userId);
	}
	
	public Optional<ResetPassword> findResetPasswordById(String id) {			
		return resetPasswordRepo.findById(id);
	}
	//Update
	
	
	//Delete
	@Transactional
	public void deleteResetPasswordById(String id) {
		resetPasswordRepo.deleteById(id);
	}
	
	@Transactional
	public void deleteByID(String id) {
		resetPasswordRepo.deleteById(id);
	}
	
}













