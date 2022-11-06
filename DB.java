// DB.java
// @author Eric Barrow, Douglas Kiang

import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class DB
{
    private final int ELECTIONDATA = 5; 			// Number of data fields for a voter (ex: name,address,etc..)
    private int numOffices; 					// Used when we need to know how many offices are being run for on a ballot
    private int elections; 					// Number of ballots in the ballots directory
    private int[] electionIDs; 					// Array of all electionIDs
    private File[] ballotNames; 				// List of all the ballot names in the ballots directory
    private File[] voteNames; 					// List of all the vote names in the votes directory
    private ArrayList<Voter> voters; 				// Voter database
    private ArrayList<Admin> admin; 				// All admin login information
    private ArrayList<ArrayList<Vote>> votes;			// All votes for all elections
    private ArrayList<ArrayList<Candidate>> ballots;		// All ballots for all elections

    Scanner sn;

    /*
     * Arg constructor used at the start of program run.
     * This class is used as the database, so all variables are initialized and set durring this object creation.
     */
    public DB()
    {
        elections = countBallots();
        ballots = new ArrayList<ArrayList<Candidate>>();
        voters = new ArrayList<Voter>();
        votes = new ArrayList<ArrayList<Vote>>();
        admin = new ArrayList<Admin>();
        electionIDs = new int[countBallots()];
        loadAllBallots();
        loadVoters();
        loadAllVotes();
        loadAdmin();
        numOffices = 0;
        tallyVotes();
    }

    /***************************************************
	 *               Getter Methods                    *
	 ***************************************************/

    // Returns an ArrayList of Candidates who are running in a given election year.
    public ArrayList<Candidate> getBallot(int electionID)
    {
        for (ArrayList<Candidate> b : ballots)
        {
            if(b.get(0).getYear() == electionID)
            	return b;
        }
        return null;
    }

    // Returns all voters
    public ArrayList<Voter> getVoters()
    {
        return voters;
    }

    // Returns an ArrayList of all votes recorded for all elections
    public ArrayList<Vote> getVotes(int electionID)
    {
    	for(int i = 0; i < votes.size(); i++) {
    		if(votes.get(i).get(0).getElectionID() == electionID)
    			return votes.get(i);
    	}
        return null;
    }

    public ArrayList<Admin> getAdmin()
    {
    	return admin;
    }

    public int[] getElectionIDs()
    {
    	return electionIDs;
    }

    /***************************************************
	 *               Setter Methods                    *
	 ***************************************************/

    // Allows external classes to add a voter to the voter database.
    public void addVoter(Voter v)
    {
        voters.add(v);
    }

    // Updates Voter's record to show they voted in a given election
    public void recordVote(int electionID, int voterID)
    {
        for(int i = 0; i < voters.size(); i++)
        {
        	if(voters.get(i).getID() == voterID) {
        		voters.get(i).addElection(electionID);
        		break;
        	}
        }
    }

    // Writes voters database to voters.txt
    public void writeVoters() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File("voters/voters.txt")))) {
            for (Voter v : voters) {
                writer.write(v.getFirstName() + ",");
                writer.write(v.getLastName() + ",");
                writer.write(v.getAddress() + ",");
                writer.write(v.getCity() + ",");
                int size = v.getElection().size();
                if (size <= 0)
                {
                    writer.write(v.getState() + "\n");
                }
                else
                {
                    writer.write(v.getState() + ",");
                    for (int i = 0, n = size - 1; i < n; i++)
                    {
                        writer.write(v.getElection().get(i) + ",");
                    }
                    writer.write(v.getElection().get(size - 1) + "\n");
                }
             }
             writer.close();
        }
        catch (IOException ex) {
             System.out.println("File not found.");
        }
    }

    // Adds a Vote to the votes database.
    // Loops through all elections until it finds a match on electionID, then adds the vote to that election.
    public void addVote(Vote vote)
    {
        for(int i = 0; i < votes.size(); i++)
        {
        	if(votes.get(i).get(0).getElectionID() == vote.getElectionID())
        	{
        		votes.get(i).add(vote);
        	}
        }
    }

    // Saves votes for each ballot year to each respective votes.txt file
    public void writeVotes()
    {
        for (ArrayList<Vote> v : votes)
        {
            // Get next Vote
            int id = v.get(0).getElectionID();
            String filename = "votes/" + String.valueOf(id) + "votes.txt";
            // Open corresponding votes text file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filename))))
            {
                for (int i = 0, n = v.size(); i < n; i++)
                {
                    int[] myVotes = v.get(i).getVotes();
                    for (int j = 0; j < myVotes.length - 1; j++)
                    {
                        writer.write(myVotes[j] + ",");
                    }
                    writer.write(myVotes[myVotes.length - 1] + "\n");
                }
                writer.close();
            }
            catch (IOException ex) {
                System.out.println("File not found.");
            }
        }


    }

    /***************************************************
	 *               Helper Methods                    *
	 ***************************************************/

    // Returns the number of files in the current directory that
    // end in "ballot.txt". Should be equivalent to the number of elections.
    private int countBallots()
    {
        File currentDir = new File("ballots/");
        int output = 0;
        try {
            // Finds number of ballot.txt files in current directory
            ballotNames = currentDir.listFiles();
            output = ballotNames.length;
        }
        catch (NullPointerException e) {
            System.out.println("No ####ballot.txt files found.");
        }
        return output;
    }

    public void tallyVotes() {
    	for(int x = 0; x < electionIDs.length; x++) {
    		for(int i = 0; i < votes.get(x).size(); i++) {
    			for(int j = 0; j < votes.get(x).get(0).getVotes().length; j++) {
    				for(int k = 0; k < ballots.get(x).size(); k++) {
    					if(ballots.get(x).get(k).getCandidateID() == votes.get(x).get(i).getVotes()[j]) {
    						ballots.get(x).get(k).addVote();
    					}
    				}
    			}
    		}
    	}
	}

    // Fills the voters ArrayList with voters read in from voters.txt
    // First five fields are voter info, sixth is generated ID, any fields beyond that are added to an ArrayList of electionIDs.
    private void loadVoters()
    {
    	// This will be used to hold 1 line from the voters.txt file at each index of the ArrayList
    	ArrayList<String> votersTxtFileLine = new ArrayList<String>();
    	try
        {
            sn = new Scanner(new File("voters/voters.txt"));
            while (sn.hasNextLine())
            {
            	// Scans through the file adding all lines to votersTxtFileLine ArrayList as Strings
            	votersTxtFileLine.add(sn.nextLine());
            }
            for(String voterLine: votersTxtFileLine)
            {
            	// creates a new ArrayList 'values' which will hold the Strings that are created by the getRecordFromLine method
            	ArrayList<String> values = new ArrayList<String>();
            	values = getRecordFromLine(voterLine);
            	// If voter hasn't voted in past elections, create a default Voter
                if (values.size() == ELECTIONDATA)
                {
                     addVoter(new Voter(values.get(0), values.get(1), values.get(2), values.get(3), values.get(4)));
                }
                // Otherwise, create new ArrayList 'past' that holds all past election info the voter has.
                // Then 'past' and 'values' are used to create a more detailed voter object
                else
                {
                    ArrayList<Integer> past = new ArrayList<Integer>();
                    for (int i = ELECTIONDATA; i < values.size(); i++)
                    {
                    	int e = Integer.parseInt(values.get(i));
                    	past.add(e);
                    }
                    addVoter(new Voter(values.get(0), values.get(1), values.get(2), values.get(3), values.get(4), past));
                }
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("File not found! Make sure that voters.txt is in the same folder as the class.");
        }
    }

    // Loads in all ballots into the ArrayList<ArrayList<candidate>> ballots.
    private void loadAllBallots()
    {
        // For each ballot file in directory
        for (int i = 0; i < elections; i++)
        {
        	ArrayList<Candidate> ballot = new ArrayList<Candidate>();
            try
            {
                // Scan through the ballot
                // ballotNames contains the names of every ballot file in the ballots directory
                sn = new Scanner(ballotNames[i]);
                String filename = ballotNames[i].getName();
                int ballotYear = Integer.valueOf(filename.substring(0,filename.indexOf("ballot")));
                // Add ballotYear to electionIDs
                electionIDs[i] = ballotYear;
                // Construct a new object for each candidate in the ballot
                while (sn.hasNextLine())
                {
                    ArrayList<String> values = getRecordFromLine(sn.nextLine());
                    String first = values.get(0);
                    String last = values.get(1);
                    int id = Integer.parseInt(values.get(2));
                    int office = Integer.parseInt(values.get(3));
                    ballot.add(new Candidate(first,last,id,office,ballotYear));
                }
                //printBallot(ballots);
            }
            catch (FileNotFoundException e)
            {
                System.out.println("File not found! Make sure that ballot.txt is in the same folder as the class.");
            }
            ballots.add(ballot);
        }
    }

    // Loads votes from all ####votes.txt files into results array
    private void loadAllVotes()
    {
        // Load all ####votes.txt filenames into voteNames
        File currentDir = new File("votes/");
        try {
            voteNames = currentDir.listFiles();
        }
        catch (NullPointerException e) {
            System.out.println("No 'votes/' directory. Create directory.");
        }
        // Iterate over voteNames and process votes into results
        for (int i = 0; i < voteNames.length; i++)
        {
        	ArrayList<Vote> v = new ArrayList<Vote>();
            try {
                sn = new Scanner(voteNames[i]);
                String filename = voteNames[i].getName();
                // Set ballotYear to the date prefix from filename
                int ballotYear = Integer.parseInt(filename.substring(0,filename.indexOf("v")));
                while (sn.hasNextLine())
                {
                    // Gets each line as an array of Strings
                    ArrayList<String> voteStrings = getRecordFromLine(sn.nextLine());
                    numOffices = voteStrings.size();
                    int[] temp = new int[numOffices];
                    for (int j = 0; j < numOffices; j++)
                    {
                           temp[j] = Integer.parseInt(voteStrings.get(j));
                    }
                    v.add(new Vote(ballotYear, temp));
                }
            }
            catch (FileNotFoundException | NullPointerException e) {
                System.out.println("Error in loading votes.");
                System.out.println("Check directory or ####votes.txt files.");
            }
            votes.add(v);
        }
    }

    // Loads all admin information from the admin.txt file in the admin directory
    public void loadAdmin() {
    	try
        {
            sn = new Scanner(new File("admin/admin.txt"));
            ArrayList<String> loginInfo = new ArrayList<String>();
            while (sn.hasNextLine())
            {
            	loginInfo.add(sn.nextLine());
            }
            ArrayList<String> temp = new ArrayList<String>();
            for(String s: loginInfo) {
            	temp = getRecordFromLine(s);
            	admin.add(new Admin(temp.get(0), temp.get(1)));
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("File not found! Make sure that admin.txt is in admin folder");
        }
    }

    // Takes a line of comma-separated values and
    // returns an ArrayList of individual strings.
    // from https://www.baeldung.com/java-csv-file-array
    private ArrayList<String> getRecordFromLine(String line)
    {
        ArrayList<String> values = new ArrayList<String>();
        try (Scanner rowScanner = new Scanner(line))
        {
            rowScanner.useDelimiter(",");
            while (rowScanner.hasNext())
            {
                values.add(rowScanner.next());
            }
        }
        return values;
    }
}