/**
 * 
 */
package com.cg.voting.dto;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;


/**
 * @author Swanand
 *
 */
@Entity
@Table(name = "User")
public class User {
	
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;
	
	@Column(name = "Name")
	private String username;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "Email", unique = true)
	private String emailId;
	
	@Column(name = "Age")
	private Integer age;
	
	@Column(name = "Gender")
	private String gender;
	
	@Column(name = "AadharNo", unique = true)
	private String aadharNo;
	
	private Boolean isAdmin;
	private Boolean isApproved;
	private Boolean isNominee;
	private Boolean hasVoted;
	private Boolean isNomineeApproved;
	private String pollLocation;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "chosen_nominee")
	private User nomineeChosen;
	
	@Column(name = "VoteCount")
	private Long voteCount;
	
	@Column(name = "Contest_From")
	private String contestFrom;
	
	@Embedded
	private Address address;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "poll_vote_Id")
	private Poll pollVote;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "poll_nominee_Id")
	private Poll pollNominee;
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public User(Long userId, String username, String password, String emailId, Integer age, String gender,
			String aadharNo, Boolean isAdmin, Boolean isApproved, Boolean isNominee, Boolean hasVoted,
			Boolean isNomineeApproved, String pollLocation, User nomineeChosen, Long voteCount, String contestFrom,
			Address address, Poll pollVote, Poll pollNominee) {
		super();
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.emailId = emailId;
		this.age = age;
		this.gender = gender;
		this.aadharNo = aadharNo;
		this.isAdmin = isAdmin;
		this.isApproved = isApproved;
		this.isNominee = isNominee;
		this.hasVoted = hasVoted;
		this.isNomineeApproved = isNomineeApproved;
		this.pollLocation = pollLocation;
		this.nomineeChosen = nomineeChosen;
		this.voteCount = voteCount;
		this.contestFrom = contestFrom;
		this.address = address;
		this.pollVote = pollVote;
		this.pollNominee = pollNominee;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAadharNo() {
		return aadharNo;
	}

	public void setAadharNo(String aadharNo) {
		this.aadharNo = aadharNo;
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public Boolean getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(Boolean isApproved) {
		this.isApproved = isApproved;
	}

	public Boolean getIsNominee() {
		return isNominee;
	}

	public void setIsNominee(Boolean isNominee) {
		this.isNominee = isNominee;
	}

	public Boolean getHasVoted() {
		return hasVoted;
	}

	public void setHasVoted(Boolean hasVoted) {
		this.hasVoted = hasVoted;
	}

	public Boolean getIsNomineeApproved() {
		return isNomineeApproved;
	}

	public void setIsNomineeApproved(Boolean isNomineeApproved) {
		this.isNomineeApproved = isNomineeApproved;
	}

	public String getPollLocation() {
		return pollLocation;
	}

	public void setPollLocation(String pollLocation) {
		this.pollLocation = pollLocation;
	}

	public User getNomineeChosen() {
		return nomineeChosen;
	}

	public void setNomineeChosen(User nomineeChosen) {
		this.nomineeChosen = nomineeChosen;
	}

	public Long getVoteCount() {
		return voteCount;
	}

	public void setVoteCount(Long voteCount) {
		this.voteCount = voteCount;
	}

	public String getContestFrom() {
		return contestFrom;
	}

	public void setContestFrom(String contestFrom) {
		this.contestFrom = contestFrom;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Poll getPollVote() {
		return pollVote;
	}

	public void setPollVote(Poll pollVote) {
		this.pollVote = pollVote;
	}

	public Poll getPollNominee() {
		return pollNominee;
	}

	public void setPollNominee(Poll pollNominee) {
		this.pollNominee = pollNominee;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", username=" + username + ", password=" + password + ", emailId=" + emailId
				+ ", age=" + age + ", gender=" + gender + ", aadharNo=" + aadharNo + ", isAdmin=" + isAdmin
				+ ", isApproved=" + isApproved + ", isNominee=" + isNominee + ", hasVoted=" + hasVoted
				+ ", isNomineeApproved=" + isNomineeApproved + ", pollLocation=" + pollLocation + ", nomineeChosen="
				+ nomineeChosen + ", voteCount=" + voteCount + ", contestFrom=" + contestFrom + ", address=" + address
				+ ", pollVote=" + pollVote + ", pollNominee=" + pollNominee + "]";
	}

	@Override
	public int hashCode() {
		return userId.intValue();
	}

	@Override
	public boolean equals(Object obj) {
		if(this.hashCode() == obj.hashCode()) {
			return true;
		}
		return false;
	}	
		
}
