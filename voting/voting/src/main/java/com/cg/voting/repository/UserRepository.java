/**
 * 
 */
package com.cg.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cg.voting.dto.User;

/**
 * @author Swanand
 *
 */
public interface UserRepository extends JpaRepository<User, Long>{

}
