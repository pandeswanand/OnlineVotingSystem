/**
 * 
 */
package com.cg.voting.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
	
	//Take a user object as input and add it to database 
	@Override
	public User addUser(User user) throws VotingException {
		User saveduser = userRepository.save(user);
		if(saveduser == null) {
			throw new VotingException(VotingExceptionMessage.DATABASEFULL);
		}
		return saveduser;
	}

	//Take a user as input and set approve flag as true
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

	//Take a user as input and set nominee flag as true
	@Override
	public User approveNominee(User user) throws VotingException{
		User saveduser = userRepository.save(user);
		if(saveduser == null) {
			throw new VotingException(VotingExceptionMessage.DATABASEFULL);
		}
		return saveduser;
	}

	//Take a user as input and if eligible set isnominee flag as true
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

	//Take poll object as input and add it to database
	@Override
	public Poll createPoll(Poll poll) throws VotingException {
		Poll savePoll = pollRepository.save(poll);
		if(savePoll == null) {
			throw new VotingException(VotingExceptionMessage.POLLNOTFOUND);
		}
		return savePoll;
	}

	//Take user voting and the user voted and set vote count
	@Override
	public synchronized Boolean vote(User votingUser, User nominee) throws VotingException {
		LocalDateTime currentTime = LocalDateTime.now();
		if(currentTime.isBefore(votingUser.getPollVote().getStartTime())) {
			throw new VotingException(VotingExceptionMessage.VOTEBEFORESTARTTIME);
		}
		else if(currentTime.isAfter(votingUser.getPollVote().getEndTime())) {
			throw new VotingException(VotingExceptionMessage.VOTEAFTERENDTIME);
		}
		else if(!votingUser.getIsApproved() || votingUser.getHasVoted() == true) {
			throw new VotingException(VotingExceptionMessage.CANNOTREVOTE);
		}
		else{
			votingUser.setHasVoted(true);
			votingUser.setNomineeChosen(nominee);
			Long count = nominee.getVoteCount();
			count = count + 1;
			nominee.setVoteCount(count);
			userRepository.save(votingUser);
			userRepository.save(nominee);
			return true;
		}
	}

	//calculate the result of a particular center
	@Override
	public Long calculateResult(String center) {
		Poll poll = pollRepository.findByPollCenter(center);
		Long votesFirst = 0L;
		if(poll != null) {
			List<User> nominees = poll.getNominees();
			List<Long> count = new ArrayList<Long>();
			nominees.forEach(nominee->{
				count.add(nominee.getVoteCount());
			});
			List<Long> votes = count.stream().sorted().collect(Collectors.toList());
			votesFirst = votes.get(votes.size()-1);
		}
		return votesFirst;
	}

	@Override
	public Boolean publishResult() {
		// TODO Auto-generated method stub
		return null;
	}

	//search a user based on his id
	@Override
	public User searchUser(Long id) throws VotingException {
		User user = userRepository.findByUserId(id);
		if(user == null) {
			throw new VotingException(VotingExceptionMessage.USERNOTFOUND);
		}
		return user;
	}

	//get all users of a particular center
	@Override
	public List<User> getUsers(String location) throws VotingException {
		List<User> users = userRepository.findAllUsersInCenter(location);
		if(users == null) {
			throw new VotingException(VotingExceptionMessage.USERNOTFOUNDINLOCATION);
		}
		return users;
	}

	//get all nominees in a particular location
	@Override
	public List<User> getNominees(String location) throws VotingException {
		List<User> nominees = userRepository.findAllNomineesInCenter(location);
		if(nominees == null) {
			throw new VotingException(VotingExceptionMessage.NOMINEENOTFOUNDINLOCATION);
		}
		return nominees;
	}

	//Find poll based on a center
	@Override
	public Poll searchPoll(String center) throws VotingException {
		Poll poll = pollRepository.findByPollCenter(center);
		return poll;
	}

	//search users with a particular votes in a given center
	@Override
	public List<User> searchUserByVotesInCenter(Long votes, String center) throws VotingException {
		List<User> userList = userRepository.findByVoteCountAndContestFrom(votes, center);
		if(userList == null) {
			throw new VotingException(VotingExceptionMessage.USERNOTFOUND);
		}
		return userList;
	}

	//get all unapproved users
	@Override
	public List<User> searchUnapprovedUsers() throws VotingException {
		List<User> list = userRepository.findAllUnapprovedUsers();
		if(list == null) {
			throw new VotingException(VotingExceptionMessage.USERNOTFOUND);
		}
		return list;
	}

	//get all unapproved nominees
	@Override
	public List<User> searchUnapprovedNominees() throws VotingException {
		List<User> list = userRepository.findAllUnapprovedNominees();
		if(list == null) {
			throw new VotingException(VotingExceptionMessage.USERNOTFOUND);
		}
		return list;
	}

	//search user based on his email
	@Override
	public User searchUser(String email) throws VotingException {
		Optional<User> user = userRepository.findByEmailId(email);
		if(user.get() == null) {
			throw new VotingException(VotingExceptionMessage.USERNOTFOUND);
		}
		return user.get();
	}

	//search the poll in which a user is voting
	@Override
	public Poll searchUserPoll(String email) throws VotingException {
		Poll poll = pollRepository.findUserPoll(email);
		if(poll == null) {
			throw new VotingException(VotingExceptionMessage.POLLNOTFOUND);
		}
		return poll;
	}
}
