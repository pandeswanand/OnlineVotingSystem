/**
 * 
 */
package com.cg.voting.service;

import java.util.List;

/**
 * @author Swanand
 *
 */
import com.cg.voting.dto.Poll;
import com.cg.voting.dto.User;
import com.cg.voting.exception.VotingException;

public interface VotingService {

	public User addUser(User user) throws VotingException;
	public User approveUser(User user) throws VotingException;
	public User approveNominee(User user) throws VotingException;
	public User registerNominee(User user) throws VotingException;
	public Poll createPoll(Poll poll) throws VotingException;
	public Boolean vote();
	public Long calculateResult();
	public Boolean publishResult();
	public User searchUser(Long id) throws VotingException;
	public List<User> getUsers(String location) throws VotingException;
	public List<User> getNominees(String location) throws VotingException;
}
