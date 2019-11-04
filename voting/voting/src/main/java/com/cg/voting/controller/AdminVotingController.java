/**
 * 
 */
package com.cg.voting.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cg.voting.dto.User;
import com.cg.voting.exception.VotingException;
import com.cg.voting.service.VotingService;

/**
 * @author Swanand
 *
 */
@RequestMapping(value = "/admin")
@RestController
public class AdminVotingController {

	@Autowired
	VotingService votingService;
	
	@PostMapping(value = "/add")
	public ResponseEntity<?> addUser(@ModelAttribute("user") User user){
		User userOne = new User();
		userOne.setUsername(user.getUsername());
		userOne.setPassword(user.getPassword());
		userOne.setEmailId(user.getEmailId());
		userOne.setAge(user.getAge());
		userOne.setAadharNo(user.getAadharNo());
		userOne.setGender(user.getGender());
		userOne.setAddress(user.getAddress());
		userOne.setHasVoted(false);
		userOne.setIsAdmin(false);
		userOne.setIsApproved(false);
		userOne.setIsNominee(false);
		userOne.setNomineeChosen(null);
		userOne.setVoteCount(0);
		try {
			votingService.addUser(userOne);
			return new ResponseEntity<User>(userOne, HttpStatus.OK);
		} catch (VotingException e) {
			return new ResponseEntity<String>(JSONObject.quote(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
