/**
 * 
 */
package com.cg.voting.controller;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cg.voting.dto.Poll;
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

	@PostMapping(value = "/user/add")
	public ResponseEntity<?> addUser(@RequestBody User user) {
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
		userOne.setContestFrom("");
		userOne.setNomineeChosen(null);
		if (user.getAddress().getArea() == "Airoli" || user.getAddress().getArea() == "Ghansoli"
				|| user.getAddress().getArea() == "Koparkhairne" || user.getAddress().getArea() == "Vashi"
				|| user.getAddress().getArea() == "Turbhe") {
			userOne.setPollLocation("Airoli");
		}
		else if(user.getAddress().getArea() == "Chembur" || user.getAddress().getArea() == "Govandi") {
			userOne.setPollLocation("Chembur");
		}
		else {
			userOne.setPollLocation("Andheri(E)");
		}
		try {
			votingService.addUser(userOne);
			return new ResponseEntity<User>(userOne, HttpStatus.OK);
		} catch (VotingException e) {
			return new ResponseEntity<String>(JSONObject.quote(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(value = "/nominee/add")
	public ResponseEntity<?> addNominee(@RequestParam("userid") Long id, @RequestParam("area") String place) {
		try {
			User foundUser = votingService.searchUser(id);
			foundUser.setContestFrom(place);
			foundUser.setIsNominee(true);
			votingService.registerNominee(foundUser);
			return new ResponseEntity<String>(JSONObject.quote("Approved as Nominee!"), HttpStatus.OK);
		} catch (VotingException e) {
			return new ResponseEntity<String>(JSONObject.quote(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(value = "/user/approve")
	public ResponseEntity<?> approveUser(@RequestParam("userid") Long id) {
		try {
			User searchedUser = votingService.searchUser(id);
			User approvedUser = votingService.approveUser(searchedUser);
			return new ResponseEntity<User>(approvedUser, HttpStatus.OK);
		} catch (VotingException e) {
			return new ResponseEntity<String>(JSONObject.quote(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(value = "/nominee/approve")
	public ResponseEntity<?> approveNominee(@RequestParam("userid") Long id) {
		try {
			User foundUser = votingService.searchUser(id);
			foundUser.setIsNomineeApproved(true);
			votingService.approveNominee(foundUser);
			return new ResponseEntity<String>(JSONObject.quote("Approved as a Nominee!"), HttpStatus.OK);
		} catch (VotingException e) {
			return new ResponseEntity<String>(JSONObject.quote(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(value = "/poll/add")
	public ResponseEntity<?> addPoll(@ModelAttribute("poll") Poll poll) {
		try {
			List<User> userList = votingService.getUsers(poll.getPollCenter());
			List<User> nomineeList = votingService.getNominees(poll.getPollCenter());
			Poll addPoll = new Poll();
			addPoll.setPollCenter(poll.getPollCenter());
			addPoll.setStartTime(poll.getStartTime());
			addPoll.setEndTime(poll.getEndTime());
			addPoll.setUsers(userList);
			addPoll.setNominees(nomineeList);
			Poll addedPoll = votingService.createPoll(addPoll);
			return new ResponseEntity<Poll>(addedPoll, HttpStatus.OK);
		} catch (VotingException e) {
			return new ResponseEntity<String>(JSONObject.quote(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
