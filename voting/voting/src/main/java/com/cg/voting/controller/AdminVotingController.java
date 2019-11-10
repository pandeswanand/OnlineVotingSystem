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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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
@CrossOrigin(origins = "http://localhost:4200")
public class AdminVotingController {

	@Autowired
	VotingService votingService;

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
			return new ResponseEntity<String>(JSONObject.quote("User is approved!"), HttpStatus.OK);
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
	
	@GetMapping(value = "/list/user/unapproved")
	public ResponseEntity<?> listUnapprovedUsers(){
		try {
			List<User> list = votingService.searchUnapprovedUsers();
			List<User> displayList = new ArrayList<User>();
			list.forEach(user->{
				User addUser = new User();
				addUser.setUserId(user.getUserId());
				addUser.setUsername(user.getUsername());
				addUser.setAadharNo(user.getAadharNo());
				addUser.setAge(user.getAge());
				addUser.setPollLocation(user.getPollLocation());
				displayList.add(addUser);
			});
			return new ResponseEntity<List<User>>(displayList, HttpStatus.OK);
		} catch (VotingException e) {
			return new ResponseEntity<String>(JSONObject.quote(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(value = "/list/nominee/unapproved")
	public ResponseEntity<?> listUnapprovedNominees(){
		try {
			List<User> list = votingService.searchUnapprovedNominees();
			List<User> displayList = new ArrayList<User>();
			list.forEach(user->{
				User addUser = new User();
				addUser.setUserId(user.getUserId());
				addUser.setUsername(user.getUsername());
				addUser.setAadharNo(user.getAadharNo());
				addUser.setAge(user.getAge());
				addUser.setContestFrom(user.getContestFrom());
				displayList.add(addUser);
			});
			return new ResponseEntity<List<User>>(displayList, HttpStatus.OK);
		} catch (VotingException e) {
			return new ResponseEntity<String>(JSONObject.quote(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(value = "/search/user")
	public ResponseEntity<?> searchByEmail(@RequestParam("email") String email){
		try {
			User user = votingService.searchUser(email);
			User sendUser = new User();
			sendUser.setEmailId(user.getEmailId());
			sendUser.setIsAdmin(user.getIsAdmin());
			sendUser.setUserId(user.getUserId());
			sendUser.setPollLocation(user.getPollLocation());
			return new ResponseEntity<User>(sendUser, HttpStatus.OK);
		} catch (VotingException e) {
			return new ResponseEntity<String>(JSONObject.quote(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping(value = "/search/poll")
	public ResponseEntity<?> searchUserPoll(@RequestParam("email") String email){
		try {
			Poll poll = votingService.searchUserPoll(email);
			Poll sendPoll = new Poll();
			sendPoll.setPollCenter(poll.getPollCenter());
			sendPoll.setStartTime(poll.getStartTime());
			sendPoll.setEndTime(poll.getEndTime());
			return new ResponseEntity<Poll>(sendPoll, HttpStatus.OK);
		} catch (VotingException e) {
			return new ResponseEntity<String>(JSONObject.quote(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(value = "/list/nominee")
	public ResponseEntity<?> getNominees(@RequestParam("center") String center){
		try {
			List<User> nominees = votingService.getNominees(center);
			List<User> sendUser = new ArrayList<User>();
			nominees.forEach(nominee->{
				User user = new User();
				user.setUserId(nominee.getUserId());
				user.setUsername(nominee.getUsername());
				sendUser.add(user);
			});
			return new ResponseEntity<List<User>>(sendUser, HttpStatus.OK);
		} catch (VotingException e) {
			return new ResponseEntity<String>(JSONObject.quote(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}