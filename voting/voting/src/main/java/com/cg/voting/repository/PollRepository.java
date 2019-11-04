/**
 * 
 */
package com.cg.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cg.voting.dto.Poll;

/**
 * @author Swanand
 *
 */
public interface PollRepository extends JpaRepository<Poll, Long>{

}
