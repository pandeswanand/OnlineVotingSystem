/**
 * 
 */
package com.cg.voting.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cg.voting.dto.User;

/**
 * @author Swanand
 *
 */
public interface UserRepository extends JpaRepository<User, Long>{

	public User findByUserId(Long id);
	public List<User> findByVoteCountAndPollLocation(Long votes, String center);
	
	@Query("FROM User where pollLocation = :location")
	public List<User> findAllUsersInCenter(String location);
	
	@Query("FROM User WHERE isNominee = true and isNomineeApproved = true and contestFrom = :location")
	public List<User> findAllNomineesInCenter(String location);
}
