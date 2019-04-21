#Author Chee Ng (cwng)
Feature: Patient view obstetrics record only. OBGYNHCP view and create a patient's obstetrics record and preganancy record
	As an OB HCP
    I want to create a patient's obstetrics
	After create obstetrics record, I want to create pregnancy record
    Then, I want to able to view a patient's obstetrics record
	And pregnancy record
	
	As a Patient
	I want to view my obstetrics record.


Scenario Outline: Create obstetrics record for the patient
	Given Logging in system as an HCPOBGYN or HCP or Patient user id <id> password <pw>
	Then HCPOBGYN navigates to the view Obstetrics Records.
	Then HCPOBGYN selects the patient with first name: <firstname> and last name: <lastname>
	Then HCPOBGYN creates a new Obstetrics Records with selected date: <date> <firstname> <lastname>
	Then HCPOBGYN selects the patient with first name: <firstname> and last name: <lastname>
	Then HCPOBGYN inputs pregnancy information: <year> <weeks> <hours> <dm> <twins>
	And The transaction code is <code>
	
Examples:
	| firstname    | lastname       | date 		 | year | weeks | hours | dm 				 | twins     | id    | pw     | code |
	| Alice		   | Smith          | 01/03/2018 | 2018 |  40   |  12   | Vaginal Delivery   | No        | OBGYN | 123456 | 2402 |
	| Jill		   | Bob			| 09/09/2017 | 2019 |  41   |  6    | Caesarean Section  | Yes       | OBGYN | 123456 | 2402 |
	| Rebecca      | Jess           | 11/01/2018 | 2018 |  2    |  18   | Miscarriage        | No        | OBGYN | 123456 | 2402 |
	
Scenario Outline: View existing obstetrics record and add additional info for the patient
	Given Logging in system as an HCPOBGYN or HCP or Patient user id <id> password <pw>
	Then HCPOBGYN navigates to the view Obstetrics Records.
	Then HCPOBGYN selects the patient with first name: <firstname> and last name: <lastname>
	Then HCPOBGYN inputs pregnancy information: <year> <weeks> <hours> <dm> <twins>

Examples:
	| firstname    | lastname       |  year | weeks | hours | dm 				| twins     | id    | pw     |
	| Jill		   | Bob			|  2016 |  44   |  24   | Caesarean Section | Yes       | OBGYN | 123456 |
	
	
Scenario Outline: View Obstetrics Records as an HCP	
	Given Logging in system as an HCPOBGYN or HCP or Patient user id <id> password <pw>
	Then the HCP views the restricted records.
	
Examples:
	| id      | pw     |
	| hcp     | 123456 |

Scenario Outline: View OB as Patient	
	Given Logging in system as an HCPOBGYN or HCP or Patient user id <id> password <pw>
	Then Obstetrics patient views Obstetrics Records.
	And The transaction code is <code>
	
Examples:
	| id      | pw     | code |
	| JillBob | 123456 | 2403 |
	
Scenario Outline: Create Invalid last menstrual date for the patient
	Given Logging in system as an HCPOBGYN or HCP or Patient user id <id> password <pw>
	Then HCPOBGYN navigates to the view Obstetrics Records.
	Then HCPOBGYN selects the patient with first name: <firstname> and last name: <lastname>
	Then HCPOBGYN creates Invalid last menstrual date for the patient: <date> <firstname> <lastname>

Examples:
	| firstname    | lastname       | date 		 | id 	   | pw 	|
	| Zara		   | Zi				| 01/03/2020 | OBGYN   | 123456 |

Scenario Outline: Create valid lmd for check
	Given Logging in system as an HCPOBGYN or HCP or Patient user id <id> password <pw>
	Then HCPOBGYN navigates to the view Obstetrics Records.
	Then HCPOBGYN selects the patient with first name: <firstname> and last name: <lastname>
	Then HCPOBGYN creates a new Obstetrics Records with selected date: <date> <firstname> <lastname>

	
Examples:
	| firstname    | lastname       | date 		 |  id    | pw     |
	| Zara		   | Zi          	| 01/03/2018 | OBGYN  | 123456 |
	
Scenario Outline: Create Invalid pregnancy Information for the patient
	Given Logging in system as an HCPOBGYN or HCP or Patient user id <id> password <pw>
	Then HCPOBGYN navigates to the view Obstetrics Records.
	Then HCPOBGYN selects the patient with first name: <firstname> and last name: <lastname>
	Then HCPOBGYN enters invalid pregnancy information <year>, <weeks>, <hours>, <dm>, <twins>, <text>

Examples:
	| firstname    | lastname       | year | weeks | hours | dm 				 | twins     | id    | pw     | text 													   |
	| Zara		   | Zi				| 2020 |  40   |  12   | Vaginal Delivery    | No        | OBGYN | 123456 | Could not add pregnancy record.   |
	| Zara		   | Zi		    	| 2018 | N/A   |  40   | Vaginal Delivery    | No        | OBGYN | 123456 |	Could not add pregnancy record.   |
	| Zara		   | Zi		    	| 2018 |  40   |  N/A  | Vaginal Delivery    | No        | OBGYN | 123456 |	Could not add pregnancy record.   |
	| Zara		   | Zi		    	| 2018 |  40   |  40   | N/A                 | No        | OBGYN | 123456 |	Could not add pregnancy record.   |
	| Zara		   | Zi		    	| 2018 |  40   |  40   | Vaginal Delivery    | N/A       | OBGYN | 123456 |	Could not add pregnancy record.   |		

	
	