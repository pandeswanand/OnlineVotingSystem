/**
 * 
 */
package com.cg.voting.service;

import java.util.List;

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
	public User approveNominee(User user) throws VotingException{
		User saveduser = userRepository.save(user);
		if(saveduser == null) {
			throw new VotingException(VotingExceptionMessage.DATABASEFULL);
		}
		return saveduser;
	}

	@Override
	public User registerNominee(User user) throws VotingException {
		if(user == null) {
			throw new VotingException(VotingExceptionMessage.USERNOTFOUND);
		}
		else if(user.getAge() < 25) {
			throw new VotingException(VotingExceptionMessage.NOMINEENOTAPPROVED);
		}
		else if(!user.getIsApproved()){
			throw new VotingException(VotingExceptionMessage.USERNOTAPPROVED);
		}
		else {
			userRepository.save(user);
		}
		return user;
	}

	@Override
	public Poll createPoll(Poll poll) throws VotingException {
		Poll savePoll = pollRepository.save(poll);
		if(savePoll == null) {
			throw new VotingException(VotingExceptionMessage.POLLNOTFOUND);
		}
		return savePoll;
	}

	@Override
	public Boolean vote(User votingUser, User nominee) throws VotingException {
		if(votingUser.getIsApproved() && votingUser.getHasVoted() == false) {
			votingUser.setHasVoted(true);
			votingUser.setNomineeChosen(nominee);
			int count = nominee.getVoteCount();
			count = count + 1;
			userRepository.save(votingUser);
			userRepository.save(nominee);
		}
		else {
			throw new VotingException(VotingExceptionMessage.CANNOTREVOTE);
		}
		return true;
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
		User user = userRepository.findByUserId(id);
		if(user == null) {
			throw new VotingException(VotingExceptionMessage.USERNOTFOUND);
		}
		return user;
	}

	@Override
	public List<User> getUsers(String location) throws VotingException {
		List<User> users = userRepository.findAllUsersInCenter(location);
		if(users == null) {
			throw new VotingException(VotingExceptionMessage.USERNOTFOUNDINLOCATION);
		}
		return users;
	}

	@Override
	public List<User> getNominees(String location) throws VotingException {
		List<User> nominees = userRepository.findAllNomineesInCenter(location);
		if(nominees == null) {
			throw new VotingException(VotingExceptionMessage.NOMINEENOTFOUNDINLOCATION);
		}
		return nominees;
	}

	@Override
	public Poll searchPoll(String center) throws VotingException {
		Poll poll = pollRepository.findByPollCenter(center);
		return poll;
	}
}
