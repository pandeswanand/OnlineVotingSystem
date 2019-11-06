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
		try {	
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
			userOne.setIsNomineeApproved(false);
			userOne.setVoteCount(0L);
			userOne.setContestFrom("");
			userOne.setNomineeChosen(null);
			userOne.setPollLocation(null);
			if (user.getAddress().getArea().equals("Airoli") || user.getAddress().getArea().equals("Ghansoli")
					|| user.getAddress().getArea().equals("Koparkhairne") || user.getAddress().getArea().equals("Vashi")
					|| user.getAddress().getArea().equals("Turbhe")) {
				userOne.setPollLocation("Airoli");
			}
			else if(user.getAddress().getArea().equals("Chembur") || user.getAddress().getArea().equals("Govandi")) {
				userOne.setPollLocation("Chembur");
			}
			else {
				userOne.setPollLocation("Andheri(E)");
			}
			Poll poll = votingService.searchPoll(userOne.getPollLocation());
			if(poll == null) {
				userOne.setPollVote(null);
			}
			else {
				userOne.setPollVote(poll);
			}
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
	public ResponseEntity<?> approveNominee(@RequestParam("userid") Long id, @RequestParam("center") String center) {
		try {
			Poll poll = votingService.searchPoll(center);
			User foundUser = votingService.searchUser(id);
			foundUser.setIsNomineeApproved(true);
			foundUser.setPollNominee(poll);
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
			userList.forEach(user->{
				user.setPollVote(addedPoll);
				try {
					votingService.addUser(user);
				} catch (VotingException e) {
					return;
				}
			});
			return new ResponseEntity<Poll>(addedPoll, HttpStatus.OK);
		} catch (VotingException e) {
			return new ResponseEntity<String>(JSONObject.quote(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(value = "/vote")
	public ResponseEntity<?> vote(@RequestParam("userid") Long userid, @RequestParam("id") Long id){
		try {
			User votingUser = votingService.searchUser(userid);
			User nominee  = votingService.searchUser(id);
			Boolean voted = votingService.vote(votingUser, nominee);
			if(voted) {
				return new ResponseEntity<String>(JSONObject.quote("Voted Successfully!"), HttpStatus.OK);
			}
			else {
				return new ResponseEntity<String>(JSONObject.quote("Cannot Vote!"), HttpStatus.BAD_REQUEST);
			}
		} catch (VotingException e) {
			return new ResponseEntity<String>(JSONObject.quote(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	@PostMapping(value = "/result")
	public ResponseEntity<?> getResult(@RequestParam("center") String center){
		Long votes = votingService.calculateResult(center);
		try {
			List<User> userList = votingService.searchUserByVotesInCenter(votes, center);
			if(userList.size() == 1) {
				return new ResponseEntity<String>(JSONObject.quote("Nominee "+userList.get(0).getUsername()+" won by scoring "+votes+" votes!"), HttpStatus.OK);
			}
			else {
				StringBuilder names = new StringBuilder();
				userList.forEach(user->{
					names.append(user.getUsername()+" ");
				});
				return new ResponseEntity<String>(JSONObject.quote("There seems to be a draw between nominees with name - "+names), HttpStatus.OK);
			}
		} catch (VotingException e) {
			return new ResponseEntity<String>(JSONObject.quote(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}