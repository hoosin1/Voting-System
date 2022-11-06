A Simulated Voting System

The purpose of this application is to simulate a voting system in the United States.

Requirements
You will need Java 7 or later installed to compile and run this project.

Installation
The project is run from the command line. Download all files to the same directory, being sure to maintain the sub-directories `admin`, `ballots`, `/voter`, `/votes` and their included text files. Make sure that read-write permissions are enabled for those sub-directories.

All `.java` files may already  be compiled; however, you may re-compile the code by executing `javac *.java`.

Running the Program
To run the project, execute the following code from the command line: `java VotingSystem`.

Different types of users:
	Voter:
		To use as a voter, simply select the vote option, enter your voter ID, select the election you want to participate in, and then place your votes.

		If you do not know your voter ID you can get with an admin user to look up your voter information by your last name.

	Admin:
		To use as an admin, simply select the admin option, enter your login information, and then select from many admin actions.

		If you do not know your login information, you can find it located in the admin folder, in the admin.txt file. Each line is an admin’s information. 
		The first string is the username and the second string is the password, both separated by a comma.

Important Notes:
	Do not close the program by closing the window. Use the exit commands to terminate the program so that it saves election progress. 
	If you do not use the exit commands to terminate, the program will never reach the method that writes data back onto the txt files.

	With the current 2020votes.txt and voters.txt files, the number of votes cast and the number of voters that have participated in the 2020 election
	do not match. Meaning when you go to validate the votes, the system will display that a mistake was found between votes cast and voter participation.
	(We thought this was a funny addition)
