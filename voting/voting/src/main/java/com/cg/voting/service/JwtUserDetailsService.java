/**
 * 
 */
package com.cg.voting.service;
import com.cg.voting.dto.MyUserDetails;
import com.cg.voting.dto.Poll;
import com.cg.voting.dto.User;
import com.cg.voting.exception.VotingException;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cg.voting.repository.UserRepository;

/**
 * Author: Swanand Pande
 * Description: Loads the Data from the database and saves the data into the database
 */
@Service
public class JwtUserDetailsService implements UserDetailsService{

	@Autowired
	private UserRepository repository;
	
	@Autowired
	private VotingService votingservice;

	@Autowired
	private PasswordEncoder bcryptEncoder;
	
	List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		Optional<User> user = repository.findByEmailId(email);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with emailId: " + email);
		}
		
		return  user.map(MyUserDetails::new).get();
	}
	
	public User save(User user) throws VotingException {
		User newUser = new User();
		newUser.setUsername(user.getUsername());
		newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
		newUser.setIsAdmin(user.getIsAdmin());
		newUser.setAadharNo(user.getAadharNo());
		newUser.setAge(user.getAge());
		newUser.setAddress(user.getAddress());
		newUser.setEmailId(user.getEmailId());
		newUser.setGender(user.getGender());
		newUser.setHasVoted(false);
		newUser.setIsAdmin(false);
		newUser.setIsApproved(false);
		newUser.setIsNominee(false);
		newUser.setIsNomineeApproved(false);
		newUser.setVoteCount(0L);
		newUser.setContestFrom("");
		newUser.setNomineeChosen(null);
		newUser.setPollLocation(null);
		if (user.getAddress().getArea().equals("Airoli") || user.getAddress().getArea().equals("Ghansoli")
				|| user.getAddress().getArea().equals("Koparkhairne") || user.getAddress().getArea().equals("Vashi")
				|| user.getAddress().getArea().equals("Turbhe")) {
			newUser.setPollLocation("Airoli");
		}
		else if(user.getAddress().getArea().equals("Chembur") || user.getAddress().getArea().equals("Govandi")) {
			newUser.setPollLocation("Chembur");
		}
		else {
			newUser.setPollLocation("Andheri(E)");
		}
		Poll poll;
		try {
			poll = votingservice.searchPoll(newUser.getPollLocation());
			if(poll == null) {
				newUser.setPollVote(null);
			}
			else {
				newUser.setPollVote(poll);
			}		
		} catch (VotingException e) {
			throw e;
		}		
		return repository.save(newUser);
	}
}
