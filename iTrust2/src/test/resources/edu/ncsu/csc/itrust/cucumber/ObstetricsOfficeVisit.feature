#Author mthyaga

Feature: Document Obstetrics Office Visit
	As an iTrust2 HCP OB/GYN
	I want to be able to document or edit an obstetrics visit
	And OB/GYN HCPs and patients should be able to view obstetrics office visits
	So that a record exits of a Patient visiting their OB/GYN and that record can be
	   updated based on the patient's progress of their pregnancy

Scenario Outline: OB/GYN documents a valid obstetrics office visit
	Then The OBGYN logs in and navigates to the Document Obstetrics Office Visit page
	When The OBGYN enters the date <date>, time <time>, patient <patient>, type of visit <type>, hospital <hospital>
    When The OBGYN enters the number of weeks pregnant <weeks>, fetal heart rate (HR, in BPM) <HR>, fundal height of uterus (in cm) <height>, twins (yes/no) <twins>, low-lying placenta (llp, present/not present) <llp>
	And The OBGYN submits the obstetrics office visit
	Then The obstetrics office visit is documented successfully

Examples:
	| date			| time				| patient 		| type 					| hospital 			| weeks 		| HR 		    | height 					| twins 			| llp     |
	| 01/01/2018	| 10:15 am	    	| bobby	        | OBGYN_OFFICE_VISIT	| General Hospital 	| 40			| 120			| 20						| yes 				| present |

Scenario Outline: OB/GYN documents an invalid obstetrics office visit
	Then The OBGYN logs in and navigates to the Document Obstetrics Office Visit page
	When The OBGYN enters the date <date>, time <time>, patient <patient>, type of visit <type>, hospital <hospital>
    When The OBGYN incorrectly enters the invalid number of weeks pregnant <weeks>, fetal heart rate (HR, in BPM) <HR>, fundal height of uterus (in cm) <height>, twins (yes/no) <twins>, low-lying placenta (llp, present/not present) <llp>
	And The OBGYN submits the obstetrics office visit
	Then The obstetrics office visit is not documented

Examples:
	| date			| time			| patient 		| type 					| hospital 			| weeks 		| HR 		    | height 					| twins 			| llp     |
	| 01/01/2018	| 10:15 am	    | bobby	        | OBGYN_OFFICE_VISIT	| General Hospital 	| 40 			| -120			| 20						| yes 				| present |
	| 01/01/2018	| 10:15 am	    | bobby	        | OBGYN_OFFICE_VISIT	| General Hospital 	| 40			| 120			| -20						| yes 				| present |
	| 01/01/2018	| 10:15 am	    | bobby	        | OBGYN_OFFICE_VISIT	| General Hospital 	| 40			| 120			| 20						| yes 				| present |
	| 01/01/2018	| 10:15 am	    | bobby	        | OBGYN_OFFICE_VISIT	| General Hospital 	| 40			| 120			| 20						| yes 				| present |
	| 01/01/2018	| 10:15 am	    | bobby	        | OBGYN_OFFICE_VISIT	| General Hospital 	| -40			| 120			| 20						| yes 				| present |

Scenario Outline: OB/GYN HCP correctly edits an obstetrics office visit
	And There exists an obstetrics office visit with <date>, time <time>, number of weeks pregnant <weeks>, fetal heart rate (HR, in BPM) <HR>, fundal height of uterus (in cm) <height>, twins (yes/no) <twins>, low-lying placenta (llp, present/not present) <llp>
	Then The OBGYN logs in and navigates to the Edit Obstetrics Office Visit page
	When The OBGYN selects the existing obstetrics office visit
	And The OBGYN changes the date to <newDate>, time <newTime>, number of weeks pregnant <newWeeks>, fetal heart rate (HR, in BPM) <newHR>, fundal height of uterus (in cm) <newHeight>, twins (yes/no) <newTwins>, low-lying placenta (llp, present/not present) <newLLP>
	And The OBGYN saves the obstetrics office visit
	Then The obstetrics office visit is updated successfully

Examples:
	| date			| time			| weeks 		| HR 		    | height 					| twins 			| llp     |  newDate			| newTime		| newWeeks 		| newHR 		    | newHeight 					| newTwins 			| newLLP     |
	| 01/01/2018	| 10:15 am	    | 40			| 120			| 20						| yes 				| present |  01/01/2018			| 10:15 am	    | 40			| 120				| 30							| yes 				| present 	 |
	| 01/01/2018	| 10:15 am	    | 40			| 120			| 30						| yes 				| present |  01/01/2018			| 10:15 am	    | 40			| 120				| 30							| no 				| present 	 |
	
Scenario Outline: OB/GYN HCP incorrectly edits an obstetrics office visit
	And There exists an obstetrics office visit with <date>, time <time>, number of weeks pregnant <weeks>, fetal heart rate (HR, in BPM) <HR>, fundal height of uterus (in cm) <height>, twins (yes/no) <twins>, low-lying placenta (llp, present/not present) <llp>
	Then The OBGYN logs in and navigates to the Edit Obstetrics Office Visit page
	When The OBGYN selects the existing obstetrics office visit
	And The OBGYN incorrectly changes the date to <newDate>, time <newTime>, number of weeks pregnant <newWeeks>, fetal heart rate (HR, in BPM) <newHR>, fundal height of uterus (in cm) <newHeight>, twins (yes/no) <newTwins>, low-lying placenta (llp, present/not present) <newLLP>
	And The OBGYN saves the obstetrics office visit
	Then The obstetrics office visit is not updated successfully

Examples:
	| date			| time			| weeks 		| HR 		    | height 					| twins 			| llp     |  newDate			| newTime		| newWeeks 		| newHR 		    | newHeight 					| newTwins 			| newLLP     |
	| 01/01/2018	| 10:15 am	    | 40			| 120			| 30						| no 				| present |  01/01/2018			| 10:15 am	    | -40			| 120				| 30							| no 				| present 	 |
	