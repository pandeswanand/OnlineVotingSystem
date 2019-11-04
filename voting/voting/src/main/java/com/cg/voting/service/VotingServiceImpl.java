/**
 * 
 */
package com.cg.voting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.voting.dto.Poll;
import com.cg.voting.dto.User;
import com.cg.voting.exception.VotingException;
import com.cg.voting.exception.VotingExceptionMessage;
import com.cg.voting.repository.PollRepository;
import com.cg.voting.repository.UserRepository;

/**
 * @author Swanand
 *
 */
@Service("votingService")
public class VotingServiceImpl implements VotingService{

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	PollRepository pollRepository;
	
	@Override
	public User addUser(User user) throws VotingException {
		User saveduser = userRepository.save(user);
		if(saveduser == null) {
			throw new VotingException(VotingExceptionMessage.DATABASEFULL);
		}
		return saveduser;
	}

	@Override
	public User approveUser(User user) throws VotingException {
		if(user != null && user.getAge() >= 18) {
			user.setIsApproved(true);
			userRepository.save(user);
			return user;
		}
		else {
			throw new VotingException(VotingExceptionMessage.USERNOTFOUND);
		}
	}

	@Override
	public User approveNominee(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User registerNominee(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Poll createPoll(Poll poll) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean vote() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long calculateResult() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean publishResult() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User searchUser(Long id) throws VotingException {
		// TODO Auto-generated method stub
		User user = userRepository.findByUserId(id);
		if(user == null) {
			throw new VotingException(VotingExceptionMessage.USERNOTFOUND);
		}
		return user;
	}

}
