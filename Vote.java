/*
 * This object represents a voter's votes on a ballot
 * The candidates that were voted for along with the year the vote was placed is stored here.
 */
import java.util.Arrays;

public class Vote {

	private int electionID;		// stores which election this vote object belongs to
	private int[] votes;		// stores all votes cast on the ballot

	// default constructor not used
	public Vote() {}

	/*
	 *  arg constructor that stores the votes in an int array along with the candidateID (year)
	 */
	public Vote(int electionID, int[] votes) {
		this.electionID = electionID;
		this.votes = votes;
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

	public int[] getVotes() {
		return votes;
	}

	public void setVotes(int[] votes) {
		for (int i : votes)
		{
			this.votes[i] = votes[i];
		}
	}
	
	// Prints out each vote's election ID and the votes, one per line.
	public String toString()
	{
		return electionID + " " + Arrays.toString(votes);
	}
}
