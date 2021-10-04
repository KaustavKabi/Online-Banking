package com.bank.controllers;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.beans.Account;

public interface AccountRepository extends JpaRepository<Account, Integer>{
	
	

}
