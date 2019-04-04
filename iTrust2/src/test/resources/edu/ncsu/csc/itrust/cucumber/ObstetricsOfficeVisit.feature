#Author mthyaga

Feature: Document Obstetrics Office Visit
	As an iTrust2 HCP OB/GYN
	I want to be able to document or edit an obstetrics visit
	And OB/GYN HCPs and patients should be able to view obstetrics office visits
	So that a record exits of a Patient visiting their OB/GYN and that record can be
	   updated based on the patient's progress of their pregnancy

Scenario Outline: OB/GYN documents a valid obstetrics office visit
	Given There exists a patient in the system
	Then The OBGYN logs in and goes to document an office visit
	When The HCP enters the date <date>, time <time>, patient <patient>, type of visit <type>, hospital <hospital>
    And Adds obstetric values <weeks> <HR> <height>
    And The HCP submits the obstetric office visit
	Then The office visit is documented successfully

Examples:
	| date			| time				| patient 		| type 					| hospital 			| weeks 		| HR 		    | height 					| twins 			| llp     |
	| 01/01/2018	| 10:15 am	    	| bobby	        | OBGYN_OFFICE_VISIT	| General Hospital 	| 40			| 120			| 20						| no 				| no |

Scenario Outline: OB/GYN documents an invalid obstetrics office visit
	Given There exists a patient in the system
	Then The OBGYN logs in and goes to document an office visit
	When The HCP enters the date <date>, time <time>, patient <patient>, type of visit <type>, hospital <hospital>
    And Adds obstetric values <weeks> <HR> <height>
    And The HCP submits the obstetric office visit
	Then The office visit is not documented

Examples:
	| date			| time			| patient 		| type 					| hospital 			| weeks 		| HR 		    | height 					| twins 			| llp     |
	| 01/01/2018	| 10:15 am	    | bobby	        | OBGYN_OFFICE_VISIT    | General Hospital 	| 40 			| -120			| 20						| no 				| no |
	
	
Scenario Outline: OB/GYN HCP correctly edits an obstetrics office visit
	Given There exists a patient in the system
	Then The OBGYN logs in and goes to document an office visit
	When The HCP enters the date <date>, time <time>, patient <patient>, type of visit <type>, hospital <hospital>
    And Adds obstetric values <weeks> <HR> <height>
    And The HCP submits the obstetric office visit
	Then The office visit is documented successfully
	When The OBGYN logs in to edit an office visit
	And The most recent obstetrics office visit is chosen
	And The HCP edits a new date <newDate>
	And The HCP saves the office visit
	Then The obstetrics office visit is updated successfully

Examples:
	| date			| time				| patient 		| type 					| hospital 			| weeks 		| HR 		    | height 					| twins 			| llp     | newDate    | 
	| 01/02/2018	| 10:15 am	    	| bobby	        | OBGYN_OFFICE_VISIT	| General Hospital 	| 40			| 140			| 20						| no 				| no | 01/03/2018 |

Scenario Outline: OB/GYN HCP incorrectly edits an obstetrics office visit
	Given There exists a patient in the system
	Then The OBGYN logs in and goes to document an office visit
	When The HCP enters the date <date>, time <time>, patient <patient>, type of visit <type>, hospital <hospital>
    And Adds obstetric values <weeks> <HR> <height>
    And The HCP submits the obstetric office visit
	Then The office visit is documented successfully
	When The OBGYN logs in to edit an office visit
	And The most recent obstetrics office visit is chosen
	And The HCP edits a new date <newDate>
	And The HCP saves the office visit
	Then The obstetrics office visit is not updated successfully

Examples:
	| date			| time				| patient 		| type 					| hospital 			| weeks 		| HR 		    | height 					| twins 			| llp     | newDate    |
	| 01/02/2018	| 10:15 am	    	| bobby	        | OBGYN_OFFICE_VISIT	| General Hospital 	| 40			| 140			| 20						| no 				| no	  | 01/03/2018 |
