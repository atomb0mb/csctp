Feature: Obstetrics HCP create, view, and edit a patient's labor and delivery reports
	As an OB HCP
        I want to create a patient's labor and delivery reports
	After create the labor and deliver report, I want to view a patient's labor and delivery reports
        Beside, I want to able to edit a patient's labor and delivery reports
	So that all of their labor and delivery report is displayed and updated 


Scenario Outline: Create a Patient labor and delivery reports
	Given OBGYN logs in using id <id> password <pw> and goes to Labor Delivery page to <text>
	Given <name> already has an obstetrics record
	Then he chooses the pregnant patient <name>
	Then he submits <dateL>, <timeL>, <dateD>, <timeD>, <type>, <twin>, <first>, <last>, <weight>, <height>, <HR>, <systolic>, <diastolic>, <twinFirst>, <twinLast>, <twinWeight>, <twinHeight>, <twinHR>, <twinSystolic>, <twinDiastolic>, <twinType>
	And Verify log history <code>
Examples:
	| name       | dateL      | timeL      | dateD      | timeD   | type             | twin   |   first    |   last        | weight  | height   |  HR    | systolic | diastolic | twinFirst | twinLast | twinWeight | twinHeight | twinHR | twinSystolic | twinDiastolic |    code     | id    | pw     |    twinType    	  | text |
	| AliceSmith | 01/01/2018 | 10:15 AM   | 09/16/2018 | 6:15 PM | Vaginal Delivery | Yes    |   Peach    |	Salinger   |  8      |    14    |  120   |  60     	|    64     | April     | Salinger |    8       |      16    |   120  |     60       |   64          |    2602     | OBGYN | 123456 |  Vaginal Delivery*  |  N/A |
	| JillBob    | 01/01/2018 | 10:16 AM   | 09/16/2018 | 6:19 PM | Caesarean Section| No     |   Maya     |	Bob	       |  8      |    16    |  100   |  60      |    64     |   N/A     |   N/A    |    0       |      0     |   0    |     0        |   0           |    2601     | OBGYN | 123456 |       N/A           | N/A |
	| ZaraZi     | 04/01/2019 | 11:15 AM   | 10/02/2019 | 3:15 AM | Miscarriage      | No     |   N/A      |     N/A       |  0      |    0     |   0    |   0      |     0     |     0     |    0     |     0      |     0      |   0    |     0        |    0          |    2601     | OBGYN | 123456 |       N/A        	  | N/A |


Scenario Outline: View a Patient labor and delivery reports
	Given OBGYN logs in using id <id> password <pw> and goes to Labor Delivery page to <text>
	Then he chooses the pregnant patient <name>
	Then The labor and delivery reports display <dateL>, <timeL>, <dateD>, <timeD>, <type>, <twin>, <first>, <last>, <weight>, <height>, <HR>, <systolic>, <diastolic>, <twinFirst>, <twinLast>, <twinWeight>, <twinHeight>, <twinHR>, <twinSystolic>, <twinDiastolic>, <twinType>
	And Verify log history <code>
	
Examples:
	| name       | dateL      | timeL      | dateD      | timeD   | type             | twin   |   first    |   last        | weight  | height   |  HR    | systolic | diastolic | twinFirst | twinLast | twinWeight | twinHeight | twinHR | twinSystolic | twinDiastolic |    code     | id    | pw     |  twinType    	  	  |  text | 
	| AliceSmith | 01/01/2018 | 10:15 AM   | 09/16/2018 | 6:15 PM | Vaginal Delivery | Yes    |   Peach    |	Salinger   |  8      |    14    |  120   |  60     	|    64     | April     | Salinger |    8       |      16    |   120  |     60       |   64          |    2601     | OBGYN | 123456 |  Vaginal Delivery*  |  N/A  |
	| JillBob    | 01/01/2018 | 10:16 AM   | 09/16/2018 | 6:19 PM | Caesarean Section| No     |   Maya     |	Bob	       |  8      |    16    |  100   |  60      |    64     |   N/A     |   N/A    |    0       |      0     |   0    |     0        |   0           |    2601     | OBGYN | 123456 |   N/A               |  N/A  |
	
Scenario Outline: Create an Invalid Patient labor and delivery reports
	Given OBGYN logs in using id <id> password <pw> and goes to Labor Delivery page to <text>
	Given <name> already has an obstetrics record
	Then he chooses the pregnant patient <name>
	Then <name> already has an obstetrics record
	And he incorrectly enters invalid values <dateL>, <timeL>, <dateD>, <timeD>, <type>, <twin>, <first>, <last>, <weight>, <height>, <HR>, <systolic>, <diastolic>, <twinFirst>, <twinLast>, <twinWeight>, <twinHeight>, <twinHR>, <twinSystolic>, <twinDiastolic>, <twinType>
	
	
Examples:
	| name       | dateL      | timeL      | dateD      | timeD    | type             | twin   |   first    |   last        | weight   | height   |  HR    | systolic | diastolic | twinFirst | twinLast | twinWeight | twinHeight | twinHR | twinSystolic | twinDiastolic |  id    | pw      |    twinType    	   |  text |
	| RebeccaJess | 01/01/2018 | 10:15 AM   | 09/16/2018 | 6:15 PM | Vaginal Delivery | Yes    |   Peach    |	Salinger	|  	0      |    0     |  0     |   0      |    0      | April     | Salinger |    0       |      0     |   0    |     0        |      0        | OBGYN  | 123456  |  Vaginal Delivery* | N/A |
	| RebeccaJess | 01/01/2018 | 10:15 AM   | 09/16/2018 | 6:15 PM | Vaginal Delivery | No     |   Peach    |	Salinger	|   99999  |    99999 |  99999 |  99999   |    99999  | April     | Salinger |    0       |      0     |   0    |    0         |      0        | OBGYN  | 123456  |    N/A   		   | N/A |
	| RebeccaJess | 01/01/2019 | 10:15 AM   | 09/16/2018 | 6:15 PM | Miscarriage      | No     |   N/A      |     N/A       |  0       |    0     |   0    |   0      |     0     |     0     |    0     |     0      |     0      |   0    |     0        |    0          | OBGYN  | 123456  |   N/A              | N/A |
	| RebeccaJess | 01/01/2014 | 10:15 AM   | 09/16/2020 | 6:15 PM | Miscarriage      | No     |   N/A      |     N/A       |  0       |    0     |   0    |   0      |     0     |     0     |    0     |     0      |     0      |   0    |     0        |    0          | OBGYN  | 123456  |   N/A              | N/A |
	| RebeccaJess | 01/01/2022 | 10:15 AM   | 09/16/2018 | 6:15 PM | Miscarriage      | No     |   N/A      |     N/A       |  0       |    0     |   0    |   0      |     0     |     0     |    0     |     0      |     0      |   0    |     0        |    0          | OBGYN  | 123456  |   N/A              | N/A |	


Scenario Outline: Edit a Patient labor and delivery reports
	Given OBGYN logs in using id <id> password <pw> and goes to Labor Delivery page to <text>
	Then he chooses the pregnant patient <name>
	Then he edit pre-filled fields <dateL>, <dateD>, <type>, <first>, <last>, <weight>, <height>, <HR>, <systolic>, <diastolic>, <twinFirst>, <twinLast>, <twinWeight>, <twinHeight>, <twinHR>, <twinSystolic>, <twinDiastolic>, <twinType>
	And Verify log history <code>	

Examples:
	| name       | dateL       | dateD      |   type             |   first    |   last      | weight  | height   |  HR    | systolic | diastolic | twinFirst | twinLast | twinWeight | twinHeight | twinHR | twinSystolic | twinDiastolic |     id    | pw     | code   |  twinType    	        |  text |
	| AliceSmith | 03/04/2018  | 12/16/2018 | Caesarean Section  |   Peach    |	Salinger	|  8      |    14    |  120   |  60      |    64     | April     | Salinger |    8       |      16    |   120  |     60       |      64       |    OBGYN  | 123456 |  2603  |  Caesarean Section*   |  edit |

Scenario Outline: Invalid edit a Patient labor and delivery reports
	Given OBGYN logs in using id <id> password <pw> and goes to Labor Delivery page to <text>
	Then he chooses the pregnant patient <name>
	Then he put in invalid input in the fields  <dateL>, <dateD>, <type>, <first>, <last>, <weight>, <height>, <HR>, <systolic>, <diastolic>, <twinFirst>, <twinLast>, <twinWeight>, <twinHeight>, <twinHR>, <twinSystolic>, <twinDiastolic>, <twinType>	

Examples:
	| name       | dateL       | dateD      |   type             |   first  |   last  | weight  | height  |  HR  | systolic | diastolic | twinFirst | twinLast | twinWeight | twinHeight | twinHR | twinSystolic | twinDiastolic  |     id     | pw       |  twinType  	 | text |
	| JillBob	 | 03/04/2020  | 12/16/2018 |  Miscarriage       |   N/A    |	N/A   |  0      |    0    |  0   |  0       |    0      | N/A       | N/A      |    0       |      0     |   0    |     0        |      0         |     OBGYN  | 123456   |    N/A       | edit |
	| JillBob	 | 03/04/2018  | 12/16/2029 |  Miscarriage       |   N/A    |	N/A   |  0      |    0    |  0   |  0       |    0      | N/A       | N/A      |    0       |      0     |   0    |     0        |      0         |     OBGYN  | 123456   |    N/A       | edit |
	| JillBob	 | 11/12/2017  | 08/16/2018 |  Caesarean Section |   Jimmy  |	Bob   |  0      |    0    |  0   |  0       |    0      | N/A       | N/A      |    0       |      0     |   0    |     0        |      0         |     OBGYN  | 123456   |    N/A       | edit |



