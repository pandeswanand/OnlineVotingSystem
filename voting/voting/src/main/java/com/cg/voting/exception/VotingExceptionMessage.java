/**
 * 
 */
package com.cg.voting.exception;

/**
 * @author Swanand
 *
 */
public class VotingExceptionMessage {

	public static final String DATABASEFULL = "The User cannot be added as database is full!";
	public static final String USERNOTFOUND = "The User does not exist!";
	public static final String USERNOTAPPROVED = "The User is not approved!";
	public static final String NOMINEENOTAPPROVED = "Cannot contest election as age is less than 25 years!";
	public static final String USERNOTFOUNDINLOCATION = "No users are present in given location";
	public static final String NOMINEENOTFOUNDINLOCATION = "No nominees are present in given location";
	public static final String POLLNOTFOUND = "The Poll does not exist!";
}
