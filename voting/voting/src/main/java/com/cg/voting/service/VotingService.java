/**
 * 
 */
package com.cg.voting.service;

/**
 * @author Swanand
 *
 */
import com.cg.voting.dto.Poll;
import com.cg.voting.dto.User;
import com.cg.voting.exception.VotingException;

public interface VotingService {

	public User addUser(User user) throws VotingException;
	public User approveUser(User user);
	public User approveNominee(User user);
	public User registerNominee(User user);
	public Poll createPoll(Poll poll);
	public Boolean vote();
	public Long calculateResult();
	public Boolean publishResult();
}
