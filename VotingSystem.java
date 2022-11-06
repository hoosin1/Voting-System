/*
 * Simulates a voting system in the United States. Requires a registered voter or admin to log in and
 * proceeds to simulate actions related to a voting system: Voting, Registering, Looking up election 
 * information, Looking up voter information, Looking up candidate information.
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class VotingSystem {
	
	/*
	 * Main method will create DB and Report objects to bring in all of the 
	 * information from txt files and store it in the program. It then calls 
	 * a method that creates a loop of menus that continue to prompt the user 
	 * for inputs until an exit scenario is met to save all progress in the 
	 * election and exit the program.
	 */
	public static void main(String[] args) {
		DB db = new DB();
		Scanner sn = new Scanner(System.in);
		homeScreen(db, sn);
		sn.close();
		db.writeVoters();
		db.writeVotes();
		System.out.println("\nElection progress saved.");
	}	
	
	/*
	 * Creates a loop that continues to repeat until the user enters 'exit'
	 * This loop continues to ask the user to either enter the vote screen, admin screen, 
	 * or exit program.
	 */
	public static void homeScreen(DB db, Scanner sn) {
		int exit = 0;				// used to exit the program
		String givenString = "";	// used to store user input, which is then used to "change screens"
		while(exit == 0) {
			for(int i = 0; i < 5; i++) {
				System.out.println();
			}
			System.out.print("Enter 'vote' to place a vote, 'admin' for admin options, or 'exit' to exit: ");
			givenString = sn.next();
			// exits the program
			if(givenString.equalsIgnoreCase("exit"))
				exit++;
			// brings user to login screen
			else if(givenString.equalsIgnoreCase("vote") || givenString.equalsIgnoreCase("admin"))
				loginScreen(db, sn, givenString);
			// print statement if an incorrect input is given
			else 
				System.out.println("Unknown command.");
		}
	}
	
	/*
	 * Asks the user to enter login information. If invalid information is entered, display message and ask again.
	 * If correct information is entered, proceed to selected menu.
	 */
	public static void loginScreen(DB db, Scanner sn, String menu) {
		int exit = 0;				// used to keep loops going if incorrect inputs are entered
		String voterID = "";		// stores the entered voterID to be used further in the menus
		String adminID = "";		// used to verify valid admin login information is entered
		String password = "";		// used to verify valid admin login information is entered
		
		for(int i = 0; i < 5; i++) {
			System.out.println();
		}
		// brings user to vote screen if requirements met
		while(exit == 0) {
			if(menu.equalsIgnoreCase("vote")) {
				System.out.print("Enter voter ID: ");
				voterID = sn.next();
				if(voterID.equalsIgnoreCase("exit"))
					break;
				if(!isNumeric(voterID))
					continue;
				for(int i = 0; i < db.getVoters().size(); i++) {
					if(db.getVoters().get(i).getID() == Integer.parseInt(voterID)) {
						voteScreen(db,sn,Integer.parseInt(voterID));
						exit++;
						break;
					}
				}
			}
			// brings user to admin screen if requirements met
			if(menu.equalsIgnoreCase("admin")) {
				System.out.print("Enter admin ID: ");
				adminID = sn.next();
				if(adminID.equalsIgnoreCase("exit"))
					break;
				System.out.print("Enter password: ");
				password = sn.next();
				for(Admin a: db.getAdmin()) {
					if(adminID.equalsIgnoreCase(a.getUsername()) && password.equalsIgnoreCase(a.getPassword())) {
						adminScreen(db,sn);
						exit++;
						break;
					}
				}
			}
			// if access to the vote screen or admin screen was never given, the exit variable will still be zero
			// if the variable is still zero, then incorrect login information was given
			if(exit == 0)
				System.out.println("Incorrect login information, try again or type 'exit' as ID to go back to main options.");
		}
	}
	
	/*
	 * Creates a loop that continues to repeat until the user enters 'exit'
	 * This loop prompts the user to enter the year election they want to participate in and 
	 * then gives them the ability to place their votes on the given year's ballot.
	 */
	public static void voteScreen(DB db, Scanner sn, int id) {		
		int exiter = 0;								// used to keep loops going if incorrect inputs are entered
		int counter = 0;							// used to keep loops going if incorrect inputs are entered
		int givenNumber = 0;						// used to store the year election to participate in
		int[] ballots = db.getElectionIDs();		// used to print out the ballots to choose from
		
		for(int i = 0; i < 5; i++) {
			System.out.println();
		}
		/*
		 * Finds the election that the voter wants to participate in and then creates an array to hold the votes
		 */
		System.out.println("Available ballots: ");
		for(int i = 0; i < ballots.length; i++) {
			System.out.print("\t" + ballots[i]);
		}
		System.out.print("\nType the year of the listed election that you want to participate in: ");
		givenNumber = sn.nextInt();
		for(Voter v: db.getVoters()) {
			if(v.getID() == id) {
				for(int i: v.getElection()) {
					if(givenNumber == i) {
						System.out.println("\nYou have already voted in that election.");
						counter++;
					}
				}
			}
		}
		if(counter == 0) {
			System.out.println("\n");
			Set<Integer> numOffices = new HashSet<Integer>();	// used to store the officeIDs of all offices on the ballot so correct text can be displayed
			for(Candidate c: db.getBallot(givenNumber)) {
				numOffices.add(c.getOfficeID());
			}
			int[] votes = new int[numOffices.size()];	// used to store the voter's votes on the ballot
			
			/*
			 * Asks the voter to place votes 1 at a time based on the offices on the ballot.
			 */
			int count = 0;
			for(int office: numOffices) {
				exiter = 0;
				ArrayList<Integer> ids = new ArrayList<Integer>();
				System.out.print("\t" + String.format("%-30s", "Running for " + db.getBallot(givenNumber).get(0).generateOffice(office) + ":"));
				System.out.print("\t" + String.format("%25s", "Candidate ID:") + "\n");
				for(int j = 0; j < db.getBallot(givenNumber).size(); j++) {
					Candidate c = db.getBallot(givenNumber).get(j);
					if(c.getOfficeID() == office) {
						System.out.print("\t" + String.format("%-15s", c.getFirstName()));
						System.out.print("\t" + String.format("%-15s", c.getLastName()));
						System.out.print("\t" + String.format("%25s", c.getCandidateID()) + "\n");
						ids.add(c.getCandidateID());
					}
				}
				while(exiter == 0) {
					System.out.print("\tEnter Candidate ID that you want to vote for: ");
					votes[count] = sn.nextInt();
					for(int i: ids) {
						if(i == votes[count]) {
							exiter++;
							break;
						}
					}
					if(exiter == 0) 
						System.out.println("\tThat is not a valid Candidate ID for this office.");
				}
				System.out.print("\n");
				count++;
			}
			
			/*
			 * Puts the vote in the correct Candidate object and also creates a Vote object
			 * and places it in the database. It contains who the user voted for.
			 */
			for(int i = 0; i < votes.length; i++) {
				for(Candidate c: db.getBallot(givenNumber)) {
					if(votes[i] == c.getCandidateID()) {
						c.addVote();
						break;
					}
				}
			}
			db.addVote(new Vote(givenNumber,votes));
			db.recordVote(givenNumber, id);
		}
	}
	
	/*
	 * Creates a loop that continues to repeat until the user enters 'exit'
	 * This loop continues to ask the user for which admin menu they want access to.
	 */
	public static void adminScreen(DB db, Scanner sn) {	
		int exit = 0;
		String givenString = "";
		
		while(exit == 0) {
			for(int i = 0; i < 5; i++) {
				System.out.println();
			}
			System.out.println("\t Type 'voter' if you want to see voter information.");
			System.out.println("\t Type 'reports' if you want to see ballot information.");
			System.out.println("\t Type 'register' if you want to register a voter");
			System.out.println("\t Type 'validate' if you want to validate votes");
			System.out.println("\t Type 'exit' if you want to return to previous options.");
			System.out.print("\nWhat action do you want to perform: ");
			givenString = sn.next();
			// depending on what the user enters, the program will either ask more information or print out requested information
			switch (givenString) {
				// Tests the voter reports
				case "voter": 
					lookupVoterScreen(db,sn);
					break;
				// Tests the ballot reports
				case "reports": 
					reportsScreen(db,sn);
					break;
				// Tests the general reports
				case "register": 
					registerVoterScreen(db,sn);
					break;
					// Tests the general reports
				case "validate": 
					validationScreen(db,sn);
					break;
				// Exits the loop, to either close the program or return to main screen options
				case "exit": 
					exit++;
					break;
				// If input is wrong, print out message and then ask again.
				default: 
					System.out.println("Error with input");
					break;
			}
		}
	}
	
	/*
	 * Will prompt the user to enter information in a specific order to register a voter. Will then
	 * check to see if a Voter is already registered with that voterID. If no, it will create a new 
	 * Voter object. If yes, will display that the voter is already registered and return to the 
	 * previous screen.
	 */
	public static void registerVoterScreen(DB db, Scanner sn) {
		int id = 0;		// used to see if the voter already exists in the system
		String[] voterInfo = new String[5];		// used to store user input about voter info, and also used for verification
		
		System.out.print("\tEnter first name: ");
		voterInfo[0] = sn.next();
		System.out.print("\tEnter last name: ");
		voterInfo[1] = sn.next();
		System.out.print("\tEnter street address: ");
		voterInfo[2] = getInput();
		System.out.print("\tEnter city: ");
		voterInfo[3] = getInput();
		System.out.print("\tEnter state: ");
		voterInfo[4] = sn.next();
		id = generateID(voterInfo[0],voterInfo[1],voterInfo[2],voterInfo[3],voterInfo[4]);
		int counter = 0;
		for(int i = 0; i < db.getVoters().size(); i++) {
			if(db.getVoters().get(i).getID() == id) {
				System.out.println("\nVoter already exists");
				counter++;
				break;
			}
		}
		if (counter == 0) {
			Voter v = new Voter(voterInfo[0],voterInfo[1],voterInfo[2],voterInfo[3],voterInfo[4]);
			db.addVoter(v);
			System.out.println("\nVoter has successfully been registered.");
			System.out.println("\n" + v.toString());
		}
	}
	
	/*
	 * Creates a loop that continues to ask what type of report the admin wants to view. 
	 * Will print out the selected report and ask again what report the admin wants to see until the admin enters exit.
	 */
	public static void reportsScreen(DB db, Scanner sn) {
		int exit = 0;				// used to exit the screen
		String givenString = "";	// used to store which menu the user has selected
		int givenNumber = 0;		// used to store which year ballot to be looking for
		int[] ballots = db.getElectionIDs();		// used to print out the ballots to choose from
		
		for(int i = 0; i < 5; i++) {
			System.out.println();
		}
		// Loop that continues to ask for the year election that you want to view until you give a valid year
		while(exit == 0) {
			System.out.println("Available ballots: ");
			for(int i = 0; i < ballots.length; i++) {
				System.out.print("\t" + ballots[i]);
			}
			System.out.print("\nWhat year ballot information do you want to see? ");
			givenNumber = sn.nextInt();
			for(int year: db.getElectionIDs()) {
				// If the given year is valid, exit loop and go to next menu
				if(givenNumber == year) {
					exit++;
				}
			}
			if(exit == 0)
				System.out.println("No election information for that year.");
		}
		exit = 0;
		Report r = new Report(givenNumber, db.getBallot(givenNumber), db.getVotes(givenNumber), db.getVoters());
		// loop that continues to ask what kind of report you want until you enter 'exit'
		while(exit == 0) {
			for(int i = 0; i < 5; i++) {
				System.out.println();
			}
			System.out.println("\t Type 'ballot' if you want to see ballot information.");
			System.out.println("\t Type 'general' if you want to see " + givenNumber + " election results.");
			System.out.println("\t Type 'exit' if you want to return to previous options.");
			System.out.print("\nWhat action do you want to perform: ");
			givenString = sn.next();
			// depending on what the user enters, the program will either ask more information or print out requested information
			switch (givenString) {
				// Tests the ballot reports
				case "ballot": 
					System.out.println(r.ballot());
					break;
				// Tests the general reports
				case "general": 
					System.out.println(r.electionReport());
					break;
				// Exits the loop, to either close the program or return to main screen options
				case "exit": 
					exit++;
					break;
				// If input is wrong, print out message and then ask again.
				default: 
					System.out.println("Error with input");
					break;
			}
		}
	}
	
	/*
	 * Will prompt the user to enter information about a voter in order to look them up. 
	 * Once entered, a voter or a list of voters is printed to the screen and then the program returns to the previous menu.
	 */
	public static void lookupVoterScreen(DB db, Scanner sn) {
		String givenString = "";				// used in searching the list of voters to find a match in given information
		Report r = new Report(db.getVoters());	// report generated with a list of all voters, used to print formatted strings of voters out
		
		for(int i = 0; i < 5; i++) {
			System.out.println();
		}
		System.out.print("Enter last name of voter you want to look up, or enter 'all' to see all voter information: ");
		givenString = sn.next();
		System.out.println("");
		if (givenString.equals("all"))
			System.out.println(r.reportVoters());
		else {
			String[] name = givenString.split(" ");
			int counter = 0;
			for(Voter v: db.getVoters()) {
				if(name[0].equalsIgnoreCase(v.getLastName())) {
					System.out.println("");
					System.out.println(v.toString());
					counter++;
				}
			}
			if(counter == 0)
				System.out.println("No voters with that name.");
		}
	}
	
	/*
	 * Will prompt the user to select from a list of active ballots to validate. Will then print out a formatted string,
	 * displaying if all votes are valid or if there is a discrepancy in votes to voter records.
	 */
	public static void validationScreen(DB db, Scanner sn) {
		int exit = 0;				// used to exit the screen
		int givenNumber = 0;		// used to store which year ballot to validate
		int[] ballots = db.getElectionIDs();		// used to print out the ballots to choose from
		
		for(int i = 0; i < 5; i++) {
			System.out.println();
		}
		// Loop that continues to ask for the year election that you want to view until you give a valid year
		while(exit == 0) {
			System.out.println("Available ballots: ");
			for(int i = 0; i < ballots.length; i++) {
				System.out.print("\t" + ballots[i]);
			}
			System.out.print("\nWhat year ballot do you want to validate? ");
			givenNumber = sn.nextInt();
			for(int year: db.getElectionIDs()) {
				// If the given year is valid, exit loop and go to next menu
				if(givenNumber == year) {
					exit++;
				}
			}
			if(exit == 0)
				System.out.println("No election information for that year.");
		}
		// Creates a report object that is passed voter and vote information so that votes can be validated, then prints out results.
		Report r = new Report(db.getVoters(), db.getVotes(givenNumber), givenNumber);
		System.out.println(r.validateVotes());
	}
	
	/*
	 * Used to generate a unique ID for voter registeration, which will be used to test if the voter
	 * is already registered.
	 */
	private static int generateID(String fName, String lName, String address, String city, String state) {
        String temp = fName + lName + address + city + state;
        int hash = 7;
        for (int i = 0, n = temp.length(); i < n; i++)
        {
            hash = hash * 31 + temp.charAt(i);
        }
        return hash;
    }
	
	/*
	 * Used to test if inputs are in a correct format. Used to prevent the program from crashing.
	 * It takes a string and sees if it can convert it into an int. Returning false if it cannot.
	 */
	public static boolean isNumeric(String strNum) {
	    if (strNum == null) {
	        return false;
	    }
	    try {
	        @SuppressWarnings("unused")
			int d = Integer.parseInt(strNum);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}
	
	/*
	 * Had to use this method for gathering the street address of a voter during registration,
	 * otherwise it was causing an issue and reading the program's prompt as user input. Scanner
	 * not closed, and not sure how to in this situation.
	 */
	private static String getInput() {
	    @SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
	    return scanner.nextLine();
	}
}