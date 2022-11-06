/*
 * Report.java is going to be called from the main method when an admin of the program asks the program to generate a report of an election.
 * When used correctly, the object can generate formated Strings that the program can use to display:
 * 		- the current number of registered voters
 * 		- the number of votes placed in the given election
 * 		- the ballot
 * 		- the current vote breakdown
 * It can also seperatly output information regarding number of registered voters, number of votes placed in a given election, ballot information, and current vote breakdown.
 */

import java.util.ArrayList;
import java.util.Collections;

public class Report {
	
	private ArrayList<Candidate> ballot = new ArrayList<Candidate>();	// Holds all the Candidate information relevant to the given electionID
	private ArrayList<Voter> voters = new ArrayList<Voter>();		// Holds all the registered Voter information
	private int electionID;							// Used in reporting the name of the election
	private int numVoters;							// Used to report how many registered voters there are
	private int voteCount;							// Used to report the numbers of votes placed in the given election
	private int candidates;							// Used in calculating percentages of votes
	private int officeCount;						// Used for looping through the ArrayLists
	
	// default constructor not used
	public Report() {}
	
	/*
	 * Constructor used when trying to create reports on election results or ballot information.
	 */
	public Report(int electionID, ArrayList<Candidate> ballot, ArrayList<Vote> votes, ArrayList<Voter> voters) {
		this.ballot = ballot;
		this.voters = voters;
		this.electionID = electionID;
		this.numVoters = voters.size();
		candidates = ballot.size();
		officeCount = votes.get(0).getVotes().length;
		voteCount = votes.size();
	}
	
	/*
	 * Constructor used when trying to create reports on voter information.
	 */
	public Report(ArrayList<Voter> voters) {
		this.voters = voters;
		this.numVoters = voters.size();
	}
	
	/*
	 * Constructor used when trying to validate election votes.
	 */
	public Report(ArrayList<Voter> voters, ArrayList<Vote> votes, int electionID) {
		this.voters = voters;
		this.numVoters = voters.size();
		this.electionID = electionID;
		voteCount = votes.size();
	}
	
	/*
	 * A "report everything" method
	 * Will generate a large formated string of everything related to the given election.
	 * Other methods are used to generate specific parts of the report.
	 */
	public String electionReport() {
		String result = "";
		result += ballotReport();
		result += participationReport();		
		return result;
	}
	
	/*
	 * Returns a string in this format:
	 * 
	 * 	Running for President:        Votes:    Percent:
	 *  	Joe         Biden                 22        53.7
	 *  	Donald      Trump                 12        29.3
	 *	Jo          Jorgensen              4         9.8
	 *	Howie       Hawkins                3         7.3
	 *  
	 *  Copies the values in the votes ArrayList into another ArrayList and then sorts those votes. 
	 *  Then loops through the the ballot n number of times (n being the number of offices being run
	 *  for on the ballot), each time adding the n office's ballot results in order from most votes 
	 *  to least votes to the string that will be returned by the method.
	 */
	public String ballotReport() {
		String office = "\n";		
		String result = "Displaying ballot for the year of " + electionID + ":\n\n";
		
		for(int i = 0; i < officeCount; i++) {
			ArrayList<Candidate> sortedVotes = new ArrayList<Candidate>();
			switch (i) {
				case 0: office = "President"; break;
				case 1: office = "House Rep"; break;
				case 2: office = "Senate"; break;
				case 3: office = "Mayor"; break;
				default: office = "President"; break;
			}
			result += "\t" + String.format("%-24s", "Running for " + office + ":");
			result += "\t" + String.format("%12s", "Votes:");
			result += "\t" + String.format("%12s", "Percent:") + "\n";
			
			for(int j = 0; j < ballot.size(); j++) {
				if(ballot.get(j).getOfficeID() == i) {
					sortedVotes.add(ballot.get(j));
				}
			}
			Collections.sort(sortedVotes);
			for(int j = sortedVotes.size() - 1; j >= 0; j--) {
				result += "\t" + String.format("%-12s", sortedVotes.get(j).getFirstName());
				result += "\t" + String.format("%-12s", sortedVotes.get(j).getLastName());
				result += "\t" + String.format("%12s", sortedVotes.get(j).getVotes());
				result += "\t" + String.format("%12.1f", calcPercentage(sortedVotes.get(j), sortedVotes)) + "\n";
			}
			result += "\n";
		}
		return result;
	}
	
	/*
	 * Returns a formated string that displays ballot information including: 
	 * 	year of ballot, names of candidates, and the offices they are running for.
	 * Similar format as ballotReport method.
	 */
	public String ballot() {
		String result = "\n\t" + "Displaying ballot for the year of " + electionID + ":\n\n";
		result += "\t" + String.format("%-12s", "First:");
		result += "\t" + String.format("%-12s", "Last:");
		result += "\t" + String.format("%-20s", "Office running for:");
		result += "\n\n";
		for(int i = 0; i < officeCount; i++) {
			for(int j = 0; j < candidates; j++) {
				if(ballot.get(j).getOfficeID() == i) {
					result += "\t" + String.format("%-12s", ballot.get(j).getFirstName());
					result += "\t" + String.format("%-12s", ballot.get(j).getLastName());
					result += "\t" + String.format("%-20s", ballot.get(j).getOffice());
					result += "\n";
				}
			}
			result += "\n";
		}
		return result;
	}
	
	/*
	 * Reports on the participation of the voters in the given election. Formats a string 
	 * to display information about the voter turnout and then returns it.
	 */
	public String participationReport() {
		String result = "\n";
		double participation = ((double)voteCount/numVoters) * 100;		
		result += "\t" + String.format("%-35s", "Number of registered voters: ") + String.format("%10s", numVoters) + "\n";
		result += "\t" + String.format("%-35s", "Number of votes placed: ") + String.format("%10s", voteCount) + "\n";
		result += "\t" + String.format("%-35s", "Percent participation: ") + String.format("%10.1f", participation) + "\n";
		return result;
	}
	
	/*
	 *  Does the required math to figure out the percentages of votes to each candidate.
	 *  Percentages are based off total votes for each given office, not total votes.
	 */
	private Double calcPercentage(Candidate x,ArrayList<Candidate> c) {
		double percent = 0;
		int totalVotes = 0;
		for(Candidate can: c) {
			totalVotes += can.getVotes();
		}
		percent = ((double)x.getVotes() / (double)totalVotes) * 100;
		return percent;
	}
	
	/*
	 * Returns a formated string that displays all registered voter information.
	 * Will also return a string of no found voters if necessary.
	 */
	public String reportVoters() {
		String result = "";
		if(voters.isEmpty()) {
			result = "No registered voters";
			return result;
		}
		else {
			for(Voter v: voters) {
				result += v.toString() + "\n";
			}
		}
		return result;
	}
	
	/*
	 * Returns a formated string that displays voter information for all voters from
	 * a specified state. Will also return a string of no found voters if necessary.
	 */
	public String reportVotersState(String state) {
		String result = "";
		int count = 0;
		if(voters.isEmpty()) {
			result = "No registered voters";
			return result;
		}
		for(Voter v: voters) {
			if(v.getState().equals(state)) {
				result += String.format("%-30s", v.toString()) + "\n";
				count++;
			}
		}
		if(count > 0)
			return result;
		else {
			result = "No registered voters in " + state;
			return result;
		}
	}
	
	public String validateVotes() {
		String result = "\n";
		int count = 0;
		for(Voter v: voters) {
			for(int election: v.getElection()) {
				if(election == electionID) {
					count++;
				}
			}
		}
		if(count == voteCount) {
			result += "\t" + "The number of voters that have participated in the election match the number of votes counted.\n";
			result += "\t" + "Election results have been validated.";
		}
		else {
			result += "\t" + "The number of voters that have participated in the election does not match the number of votes counted.\n";
			result += "\t" + "Election results are not valid.";
		}
		return result;
	}

	/***************************************************
	 *               Getters and Setters               *
	 ***************************************************/
	
	public int getElectionID() {
		return electionID;
	}

	public void setElectionID(int electionID) {
		this.electionID = electionID;
	}

	public int getNumVoters() {
		return numVoters;
	}

	public void setNumVoters(int numVoters) {
		this.numVoters = numVoters;
	}

	public int getVoteCount() {
		return voteCount;
	}

	public void setVoteCount(int voteCount) {
		this.voteCount = voteCount;
	}

	public int getCandidates() {
		return candidates;
	}

	public void setCandidates(int candidates) {
		this.candidates = candidates;
	}

	public int getOfficeCount() {
		return officeCount;
	}

	public void setOfficeCount(int officeCount) {
		this.officeCount = officeCount;
	}
}