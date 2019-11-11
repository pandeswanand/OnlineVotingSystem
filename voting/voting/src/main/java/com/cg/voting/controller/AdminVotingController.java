/**
 * 
 */
package com.cg.voting.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.cg.voting.dto.Poll;
import com.cg.voting.dto.User;
import com.cg.voting.exception.VotingException;
import com.cg.voting.service.VotingService;

/**
 * @author Swanand
 *
 */
@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class AdminVotingController {

	@Autowired
	VotingService votingService;

	private static final Logger logger = LoggerFactory.getLogger(AdminVotingController.class);
	//Add a nominee with a id in a particular poll area
	@PostMapping(value = "/nominee/add")
	public ResponseEntity<?> addNominee(@RequestParam("userid") Long id, @RequestParam("area") String place) {
		try {
			logger.info("Entered add nominee");
			User foundUser = votingService.searchUser(id);
			foundUser.setContestFrom(place);
			foundUser.setIsNominee(true);
			votingService.registerNominee(foundUser);
			logger.info("Added nominee");
			return new ResponseEntity<String>(JSONObject.quote("Approved as Nominee!"), HttpStatus.OK);
		} catch (VotingException e) {
			logger.error(e.getMessage());
			return new ResponseEntity<String>(JSONObject.quote(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	//Take user id aand approve a user
	@PostMapping(value = "/user/approve")
	public ResponseEntity<?> approveUser(@RequestParam("userid") Long id) {
		try {
			logger.info("Entered approve user");
			User searchedUser = votingService.searchUser(id);
			User approvedUser = votingService.approveUser(searchedUser);
			logger.info("Approved user");
			return new ResponseEntity<String>(JSONObject.quote("User is approved!"), HttpStatus.OK);
		} catch (VotingException e) {
			logger.error(e.getMessage());
			return new ResponseEntity<String>(JSONObject.quote(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	//Take a nominee input and approve him for a particular center
	@PostMapping(value = "/nominee/approve")
	public ResponseEntity<?> approveNominee(@RequestParam("userid") Long id, @RequestParam("center") String center) {
		try {
			logger.info("Entered approve nominee");
			Poll poll = votingService.searchPoll(center);
			User foundUser = votingService.searchUser(id);
			foundUser.setIsNomineeApproved(true);
			foundUser.setPollNominee(poll);
			votingService.approveNominee(foundUser);
			logger.info("Approved nominee");
			return new ResponseEntity<String>(JSONObject.quote("Approved as a Nominee!"), HttpStatus.OK);
		} catch (VotingException e) {
			logger.error(e.getMessage());
			return new ResponseEntity<String>(JSONObject.quote(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	//Take a poll input through a form and add poll
	@PostMapping(value = "/poll/add")
	public ResponseEntity<?> addPoll(@ModelAttribute("poll") Poll poll) {
		try {
			logger.info("Entered add poll");
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
			logger.info("Added poll");
			return new ResponseEntity<Poll>(addedPoll, HttpStatus.OK);
		} catch (VotingException e) {
			logger.error(e.getMessage());
			return new ResponseEntity<String>(JSONObject.quote(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//Take a user id of user voting and the id of user chosen to that particular user
	@PostMapping(value = "/vote")
	public ResponseEntity<?> vote(@RequestParam("userid") Long userid, @RequestParam("id") Long id){
		try {
			logger.info("Entered vote");
			User votingUser = votingService.searchUser(userid);
			User nominee  = votingService.searchUser(id);
			Boolean voted = votingService.vote(votingUser, nominee);
			if(voted) {
				logger.info("Voted");
				return new ResponseEntity<String>(JSONObject.quote("Voted Successfully!"), HttpStatus.OK);
			}
			else {
				return new ResponseEntity<String>(JSONObject.quote("Cannot Vote!"), HttpStatus.BAD_REQUEST);
			}
		} catch (VotingException e) {
			logger.error(e.getMessage());
			return new ResponseEntity<String>(JSONObject.quote(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	//Return the result of a particular center
	@PostMapping(value = "/result")
	public ResponseEntity<?> getResult(@RequestParam("center") String center){
		Long votes = votingService.calculateResult(center);
		try {
			logger.info("Entered result");
			List<User> userList = votingService.searchUserByVotesInCenter(votes, center);
			if(userList.size() == 1) {
				logger.info("Displayed result");
				return new ResponseEntity<String>(JSONObject.quote("Nominee "+userList.get(0).getUsername()+" won by scoring "+votes+" votes!"), HttpStatus.OK);
			}
			else {
				logger.info("Displaying draw result");
				StringBuilder names = new StringBuilder();
				userList.forEach(user->{
					names.append(user.getUsername()+" ");
				});
				return new ResponseEntity<String>(JSONObject.quote("There seems to be a draw between nominees with name - "+names), HttpStatus.OK);
			}
		} catch (VotingException e) {
			logger.error(e.getMessage());
			return new ResponseEntity<String>(JSONObject.quote(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//List all the unapproved users
	@GetMapping(value = "/list/user/unapproved")
	public ResponseEntity<?> listUnapprovedUsers(){
		try {
			logger.info("Entered list unapproved users");
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
			logger.error(e.getMessage());
			return new ResponseEntity<String>(JSONObject.quote(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//List all the unapproved nominees
	@GetMapping(value = "/list/nominee/unapproved")
	public ResponseEntity<?> listUnapprovedNominees(){
		try {
			logger.info("Entered list unapproved nominees");
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
			logger.error(e.getMessage());
			return new ResponseEntity<String>(JSONObject.quote(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//search a user based on his email id
	@GetMapping(value = "/search/user")
	public ResponseEntity<?> searchByEmail(@RequestParam("email") String email){
		try {
			logger.info("Entered search user");
			User user = votingService.searchUser(email);
			User sendUser = new User();
			sendUser.setEmailId(user.getEmailId());
			sendUser.setIsAdmin(user.getIsAdmin());
			sendUser.setUserId(user.getUserId());
			sendUser.setPollLocation(user.getPollLocation());
			return new ResponseEntity<User>(sendUser, HttpStatus.OK);
		} catch (VotingException e) {
			logger.error(e.getMessage());
			return new ResponseEntity<String>(JSONObject.quote(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}
	
	//search the voting poll of a particular user given his email id 
	@GetMapping(value = "/search/poll")
	public ResponseEntity<?> searchUserPoll(@RequestParam("email") String email){
		try {
			logger.info("Entered search poll");
			Poll poll = votingService.searchUserPoll(email);
			Poll sendPoll = new Poll();
			sendPoll.setPollCenter(poll.getPollCenter());
			sendPoll.setStartTime(poll.getStartTime());
			sendPoll.setEndTime(poll.getEndTime());
			return new ResponseEntity<Poll>(sendPoll, HttpStatus.OK);
		} catch (VotingException e) {
			logger.error(e.getMessage());
			return new ResponseEntity<String>(JSONObject.quote(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//list all the nominees of a particular center
	@GetMapping(value = "/list/nominee")
	public ResponseEntity<?> getNominees(@RequestParam("center") String center){
		try {
			logger.info("Entered list nominees method");
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
			logger.error(e.getMessage());
			return new ResponseEntity<String>(JSONObject.quote(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(value = "/upload/aadhar")
	public ResponseEntity<?> uploadPdf(@RequestParam("aadhar") MultipartFile file) {
		try {
			System.out.println("entered");
			String UPLOAD_DIRECTORY = System.getProperty("catalina.home")+ "\\Aadhar";
			System.out.println(System.getProperty("catalina.base"));
			System.out.println(file);
			String fileName = file.getOriginalFilename();
			File pathFile = new File(UPLOAD_DIRECTORY);
			if (!pathFile.exists()) {  //If the given path does not exist then create the directory
				pathFile.mkdir();
			}

			pathFile = new File(UPLOAD_DIRECTORY + "\\" + fileName);
			file.transferTo(pathFile);    //Transfer the file to the given path
		} catch (IOException e) {
			return new ResponseEntity<String>(JSONObject.quote(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(JSONObject.quote("File uploaded successfully!"), HttpStatus.OK);
	}
	
	@GetMapping(path = "/download")
	    public StreamingResponseBody getSteamingFile(HttpServletResponse response, @RequestParam("image") String image) throws IOException {
	        response.setContentType("application/pdf");
	        response.setHeader("Content-Disposition", "attachment; filename=\"demo.pdf\"");
	        InputStream inputStream = new FileInputStream(new File(System.getProperty("catalina.home")+ "\\Aadhar\\"+image+".pdf"));
	        return outputStream -> {
	            int nRead;
	            byte[] data = new byte[1024];
	            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
	                System.out.println("Writing some bytes..");
	                outputStream.write(data, 0, nRead);
	            }
	        };
	    }
}