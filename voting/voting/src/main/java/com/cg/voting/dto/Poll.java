/**
 * 
 */
package com.cg.voting.dto;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

/**
 * @author Swanand
 *
 */
@Entity
@Table(name = "Poll")
public class Poll {

	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long pollId;
	
	@Column(name = "Center")
	private String pollCenter;
	
	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private LocalDateTime startTime;
	
	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private LocalDateTime endTime;
	
	@OneToMany(mappedBy = "pollNominee", cascade = CascadeType.PERSIST)
	private List<User> nominees;
	
	@OneToMany(mappedBy = "pollVote", cascade = CascadeType.PERSIST)
	private List<User> users;
	
	public Poll() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Poll(Long pollId, String pollCenter, LocalDateTime startTime, LocalDateTime endTime, List<User> nominees,
			List<User> users) {
		super();
		this.pollId = pollId;
		this.pollCenter = pollCenter;
		this.startTime = startTime;
		this.endTime = endTime;
		this.nominees = nominees;
		this.users = users;
	}


	public Long getPollId() {
		return pollId;
	}

	public void setPollId(Long pollId) {
		this.pollId = pollId;
	}

	public String getPollCenter() {
		return pollCenter;
	}

	public void setPollCenter(String pollCenter) {
		this.pollCenter = pollCenter;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public List<User> getNominees() {
		return nominees;
	}

	public void setNominees(List<User> nominees) {
		this.nominees = nominees;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	@Override
	public String toString() {
		return "Poll [pollId=" + pollId + ", pollCenter=" + pollCenter + ", startTime=" + startTime + ", endTime="
				+ endTime + ", nominees=" + nominees + ", users=" + users + "]";
	}

	@Override
	public int hashCode() {
		return pollId.intValue();
	}

	@Override
	public boolean equals(Object obj) {
		if(this.hashCode() == obj.hashCode()) {
			return true;
		}
		return false;
	}	
}
