/*
 * Candidate.java will represent a candidate for a particular office
 * This object will allow a user to return a string of information the user wants about the candidate including:
 * 	-first name
 * 	-last name
 * 	-office they are running for
 * 	-officeID
 * 	-candidateID
 * 	-year they are running for
 */

public class Candidate implements Comparable<Candidate> {
	
	private String firstName;		// First name of candidate
	private String lastName;		// Last name of candidate
	private String office;			// Name of the office the candidate is running for
	private int officeID;			// Number used to identify the office the candidate is running for
	private int candidateID;		// Number used in ####votes.txt to show the votes belongs to this candidate
	private int year;			// Year(electionID) the candidate is running in an election
	private int votes;			// Keeps track of how many votes this candidate has received
	
	// default constructor not used
	public Candidate() {}
	
	/*
	 * argument constructor used in setting the values of all the variables for the object
	 */
	public Candidate(String firstName, String lastName, int id, int office, int year) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.office = generateOffice(office);
		this.officeID = office;
		this.candidateID = id;
		this.year = year;
		this.votes = 0;
	}
	
	// method is called when a vote is counted for this candidate, increments the vote variable
	public void addVote() {
		votes++;
	}
	
	// used to sort the candidates by how many votes they have
	public int compareTo(Candidate c) {
		return this.votes - c.votes;
	}
	
	/*
	 * Format of output:
	 * 		Biden, J  Running for 2020 President
	 */
	public String toString() {
		String result = "";
		result += String.format("%16s", lastName + ", " + firstName.charAt(0));
		result += String.format("%-30s", "Running for " + year + " " + office);
		return result;
	}
	
	// used to convert the number from the txt file to the name of the office the candidate belongs to on the ballot
	public String generateOffice(int o) {
		String result = "";
		switch (o) {
			case 0: result = "President"; break;
			case 1: result = "House Rep"; break;
			case 2: result = "Senate"; break;
			case 3: result = "Mayor"; break;
			default: result = "President"; break;
		}
		return result;
	}

	/***************************************************
	 *               Getters and Setters               *
	 ***************************************************/
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getOffice() {
		return office;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	public int getCandidateID() {
		return candidateID;
	}

	public void setCandidateID(int candidateID) {
		this.candidateID = candidateID;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getOfficeID() {
		return officeID;
	}

	public void setOfficeID(int officeID) {
		this.officeID = officeID;
	}

	public int getVotes() {
		return votes;
	}

	public void setVotes(int votes) {
		this.votes = votes;
	}
}