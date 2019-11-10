/**
 * 
 */
package com.cg.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cg.voting.dto.Poll;

/**
 * @author Swanand
 *
 */
public interface PollRepository extends JpaRepository<Poll, Long>{

	public Poll findByPollCenter(String center);
	
	@Query("SELECT pollVote FROM User WHERE email = :email")
	public Poll findUserPoll(@Param("email") String email);
}
