// Voter.java
// Represents a voter
// @Author Douglas Kiang

import java.util.ArrayList;

public class Voter
{
    private String firstName;			// first name of voter
    private String lastName;			// last name of voter
    private String address;			// address of voter
    private String city;			// city of voter
    private String state;			// state of voter
    private int id;				// unique id generated from name, address, city, state
    private ArrayList<Integer> elections;	// elections that the voter has participated in
    
    // arg constructor used to generate a new voter in the system
    public Voter(String first, String last, String address, String city, String state)
    {
        firstName = first;
        lastName = last;
        this.address = address;
        this.city = city;
        this.state = state;
        this.id = generateID();
        elections = new ArrayList<Integer>();
    }
    
    // arg constructor to add a voter, that has participated in previous elections, into the system
    public Voter(String first, String last, String address, String city, String state, ArrayList<Integer> elections)
    {
        this(first,last,address,city,state);
        for (Integer i : elections)
        {
            this.elections.add(i);
        }
    }
    
    // Generates a unique Voter ID (hash) based on the voter's name and address
    // Hash will always be the same given the same name and address
    private int generateID()
    {
        String temp = firstName + lastName + address + city + state;
        int hash = 7;
        for (int i = 0, n = temp.length(); i < n; i++)
        {
            hash = hash * 31 + temp.charAt(i);
        }
        return hash;
    }
    
	/***************************************************
	 *               Getters and Setters               *
	 ***************************************************/
    
    public String getFirstName()
    {
        return firstName;
    }
    
    public String getLastName()
    {
        return lastName;
    }
    
    public String getAddress()
    {
        return address;
    }
    
    public String getCity()
    {
        return city;
    }
    
    public String getState()
    {
        return state;
    }
    
    public ArrayList<Integer> getElection()
    {
        return elections;
    }
    
    public void addElection(int id)
    {
        elections.add(id);
    }
    
    public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}

	/*
	 * To string method used to print out a formatted string of the voter information
	 */
	public String toString()
    {
    	String result = "";
    	result += "\t" + String.format("%-14s", firstName);
    	result += String.format("%-14s", lastName) + "\n";
    	result += "\t" + String.format("%-28s", address) + "\n";
    	result += "\t" + String.format("%-20s", city);
    	result += String.format("%4s", state) + "\n";
    	result += "\t" + String.format("%-24s", "Voter ID:");
    	result += String.format("%20s", id) + "\n";
    	if(elections.isEmpty()) {
    		result += "\t" + String.format("%-28s", "Elections participated in:");
    		result += String.format("%20s", "[none]") + "\n";
    	}
    	else {
    		result += "\t" + String.format("%-28s", "Elections participated in:");
    		result += String.format("%20s", elections) + "\n";
    	}
        return result;
    }
}
