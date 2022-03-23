package com.revature.models;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="reset_password_emails")
public class ResetPassword {

	@Id
	@Column(name="email_id")
	private String emailId;
	
	@OneToOne
	@JoinColumn(name="user_id")
	private User user;

	@Column(name="time")
	private String timestamp;
}
