#Author mthyaga

Feature: Document Obstetrics Office Visit
	As an iTrust2 HCP OB/GYN
	I want to be able to document or edit an obstetrics visit
	And OB/GYN HCPs and patients should be able to view obstetrics office visits
	So that a record exits of a Patient visiting their OB/GYN and that record can be
	   updated based on the patient's progress of their pregnancy
	   
	
Scenario: Document an Obstetrics Office Visit
Given The required facilities exist
When I log in to iTrust2 as a HCP OB/GYN
When I navigate to the Document Obstetrics Office Visit page
When I fill in information on the obstetrics office visit
Then The obstetrics office visit is documented successfully

Scenario Outline: OB/GYN documents a valid obstetrics office visit
	Given There is a patient in the system
	And There exists an OB/GYN HCP in the system
	Then The HCP logs in and navigates to the Document Obstetrics Office Visit page
    When The HCP enters the date <date>, time <time> number of weeks pregnant <weeks>, fetal heart rate (HR, in BPM) <HR>, fundal height of uterus (in cm) <height>, twins (yes/no) <twins>, low-lying placenta (llp, present/not present) <llp>
    And in the Mother's Basic Health Metrics box enters height/length <length>, weight <weight>, household smoking status (smoking household/non-smoking household)  <status>
	And The HCP submits the obstetrics office visit
	Then The obstetrics office visit is documented successfully

Examples:
	| date			| time				| weeks 		| HR 		    | height 					| twins 			| llp     | length 					| weight 			| status     			|
	| 01/01/2018	| 10:15 am	    	| 40			| 120			| 20						| yes 				| present | 170                     | 130               | non-smoking household |

Scenario Outline: OB/GYN documents an invalid obstetrics office visit
	Given There is a patient in the system
	And There exists an OB/GYN HCP in the system
	Then The HCP logs in and navigates to the Document Obstetrics Office Visit page
    When The HCP enters the date <date>, time <time>, number of weeks pregnant <weeks>, fetal heart rate (HR, in BPM) <HR>, fundal height of uterus (in cm) <height>, twins (yes/no) <twins>, low-lying placenta (llp, present/not present) <llp>
    And in the Mother's Basic Health Metrics box enters height/length <length>, weight <weight>, household smoking status (smoking household/non-smoking household)  <status>
	And The HCP submits the obstetrics office visit
	Then The obstetrics office visit is not documented successfully

Examples:
	| date			| time			| weeks 		| HR 		    | height 					| twins 			| llp     | length 					| weight 			| status     			|
	|            	| 			    | 				| 				|							| 	 				|         |                         |                   | 					    |
	| 01/01/2018	| 10:15 am	    | 40 			| -120			| 20						| yes 				| present | 170                     | 130               | non-smoking household |
	| 01/01/2018	| 10:15 am	    | 40			| 120			| -20						| yes 				| present | 170                     | 130               | non-smoking household |
	| 01/01/2018	| 10:15 am	    | 40			| 120			| 20						| yes 				| present | -170                    | 130               | non-smoking household |
	| 01/01/2018	| 10:15 am	    | 40			| 120			| 20						| yes 				| present | 170                     | -130              | non-smoking household |
	| 01/01/2018	| 10:15 am	    | -40			| 120			| 20						| yes 				| present | 170                     | 130               | non-smoking household |

Scenario Outline: OB/GYN HCP correctly edits an obstetrics office visit
	Given There is a patient in the system
	And There exists an OB/GYN HCP in the system
	And There exists an obstetrics office visit with <date>, time <time>, number of weeks pregnant <weeks>, fetal heart rate (HR, in BPM) <HR>, fundal height of uterus (in cm) <height>, twins (yes/no) <twins>, low-lying placenta (llp, present/not present) <llp>
	Then The HCP logs in and navigates to the Edit Obstetrics Office Visit page
	When The HCP selects the existing obstetrics office visit
	And The HCP changes the date to be <newDate>, time <newTime>, number of weeks pregnant <newWeeks>, fetal heart rate (HR, in BPM) <newHR>, fundal height of uterus (in cm) <newHeight>, twins (yes/no) <newTwins>, low-lying placenta (llp, present/not present) <newLLP>
	And The HCP saves the obstetrics office visit
	Then The obstetrics office visit is updated successfully

Examples:
	| date			| time			| weeks 		| HR 		    | height 					| twins 			| llp     |  newDate			| newTime		| newWeeks 		| newHR 		    | newHeight 					| newTwins 			| newLLP     |
	| 01/01/2018	| 10:15 am	    | 40			| 120			| 20						| yes 				| present |  01/01/2018			| 10:15 am	    | 40			| 120				| 30							| yes 				| present 	 |
	| 01/01/2018	| 10:15 am	    | 40			| 120			| 30						| yes 				| present |  01/01/2018			| 10:15 am	    | 40			| 120				| 30							| no 				| present 	 |
	
Scenario Outline: OB/GYN HCP incorrectly edits an obstetrics office visit
	Given There is a patient in the system
	And There exists an OB/GYN HCP in the system
	And There exists an obstetrics office visit with <date>, time <time>, number of weeks pregnant <weeks>, fetal heart rate (HR, in BPM) <HR>, fundal height of uterus (in cm) <height>, twins (yes/no) <twins>, low-lying placenta (llp, present/not present) <llp>
	Then The HCP logs in and navigates to the Edit Obstetrics Office Visit page
	When The HCP selects the existing obstetrics office visit
	And The HCP changes the date to be <newDate>, time <newTime>, number of weeks pregnant <newWeeks>, fetal heart rate (HR, in BPM) <newHR>, fundal height of uterus (in cm) <newHeight>, twins (yes/no) <newTwins>, low-lying placenta (llp, present/not present) <newLLP>
	And The HCP saves the obstetrics office visit
	Then The obstetrics office visit is not updated successfully

Examples:
	| date			| time			| weeks 		| HR 		    | height 					| twins 			| llp     |  newDate			| newTime		| newWeeks 		| newHR 		    | newHeight 					| newTwins 			| newLLP     |
	| 01/01/2018	| 10:15 am	    | 40			| 120			| 30						| no 				| present |  01/01/2018			| 10:15 am	    | -40			| 120				| 30							| no 				| present 	 |
	