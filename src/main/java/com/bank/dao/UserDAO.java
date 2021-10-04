package com.bank.dao;

import java.util.List;
import java.util.Optional;

import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import com.bank.beans.Account;
import com.bank.beans.Transaction;
import com.bank.beans.User;
import com.bank.controllers.AccountRepository;
import com.bank.controllers.TransactionRepository;
import com.bank.controllers.UserRepository;

@Service
public class UserDAO {

	@Autowired
	private UserRepository userRepo;
	@Autowired
	private AccountRepository accRepo;
	@Autowired
	private TransactionRepository transacRepo;

	public void addUserToDB(User user, Account account) {
		userRepo.save(user);
		accRepo.save(account);
	}

	public List<Account> fetchUserAndAccount() {
		return accRepo.findAll();
	}

	public User findUserByEmailPassword(String email, String password) {
		return userRepo.findByEmailAndPassword(email, password);
	}

	public String getBalance(int id, String pin) {

		Optional<User> user = userRepo.findByIdAndPin(id, pin);
		try {
			Account account = user.get().getAccount();
			return String.valueOf(account.getBal());
		} catch (Exception e) {
			return "Invalid PIN";
		}

	}
	
	public User getUser(int id) {
		return userRepo.findById(id).get();
	}
	
	public Optional<Account> findAccount(int id) {
		return accRepo.findById(id);
	}
	
	public void transferFund(Transaction t) {
		int fromAcc= t.getFrom();
		int toAcc= t.getTo();
		
		Account fromList= accRepo.findById(fromAcc).get();
		Account toList= accRepo.findById(toAcc).get();
		
		fromList.setBal(fromList.getBal() - t.getAmount());
		toList.setBal(toList.getBal() + t.getAmount());
		
		accRepo.save(fromList);
		accRepo.save(toList);transacRepo.save(t);
		
	}
	
	public List<Transaction>  getAllTransactions(int id) {
		User user= getUser(id);
		List<Transaction> list= transacRepo.findAllByFrom(user.getAccount().getAccno());
		List<Transaction> list2= transacRepo.findAllByTo(user.getAccount().getAccno());
		for(Transaction t: list) {
			t.setType("DEBIT");
		}
		for(Transaction t: list2) {
			t.setType("CREDIT");
		}
		
		list.addAll(list2);
		list2=null;
		return list;
	}	
	
}
