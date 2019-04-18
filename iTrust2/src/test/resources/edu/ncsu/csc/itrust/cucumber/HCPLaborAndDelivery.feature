Feature: Obstetrics HCP create, view, and edit a patient's labor and delivery reports
	As an OB HCP
        I want to create a patient's labor and delivery reports
	After create the labor and deliver report, I want to view a patient's labor and delivery reports
        Beside, I want to able to edit a patient's labor and delivery reports
	So that all of their labor and delivery report is displayed and updated 


Scenario Outline: Create a Patient labor and delivery reports
	Given OBGYN logs in using id <id> password <pw> and goes to Labor Delivery page
	Then he selects patient <name>
	Then <name> already has an obstetrics record
	And he enters <dateL> <timeL> <dateD> <timeD> <type> <twin> <first> <last> <weight> <height> <HR> <systolic> <diastolic> <twinFirst> <twinLast> <twinWeight> <twinHeight> <twinHR> <twinSystolic> <twinDiastolic>
	And he submits the form
	Then a success message is displayed <text>
	Then the system sends him to the screen displays labor and delivery record
Examples:
	| name       | dateL      | timeL      | dateD      | timeD    | type             | twin   |   first    |   last        | weight  | height   |  HR    | systolic | diastolic | twinFirst | twinLast | twinWeight | twinHeight | twinHR | twinSystolic | twinDiastolic |                     text                                  | id    | pw     |
	| AliceSmith | 01/01/2018 | 10:15 AM   | 09/16/2018 | 06:15 PM | Vaginal Delivery | Yes    |   Peach    |	Salinger	|  8      |    14    |  120   |  60      |    64     | April     | Salinger |    8       |      16    |   120  |     60       |      64       |   Labor and Delivery Report created successfully          | OGBYN | 123456 |
	| JillBob    | 01/01/2018 | 10:16 AM   | 09/16/2018 | 06:19 PM | Cesarean Section | No     |   Maya     |	Bob	        |  8      |    16    |  100   |  60      |    64     |           |          |            |            |        |              |               |   Labor and Delivery Report created successfully          | OGBYN | 123456 |
	| ZaraZi     | 04/01/2019 | 11:15 AM   | 10/02/2019 | 03:15 AM | Miscarriage      |        |            |               |         |          |        |          |           |           |          |            |            |        |              |               |   Labor and Delivery Report created successfully          | OGBYN | 123456 |


Scenario Outline: View a Patient labor and delivery reports
	Given OBGYN logs in using id <id> password <pw> and goes to Labor Delivery page
	And Dr Jenkin has logged in and chosen to view a patient labor and delivery record
	Then he selects patient <name>
	And he selects date to view <date>
	Then The labor and delivery reports display <dateL> <timeL> <dateD> <timeD> <type> <twin> <first> <last> <weight> <height> <HR> <systolic> <diastolic> <twinFirst> <twinLast> <twinWeight> <twinHeight> <twinHR> <twinSystolic> <twinDiastolic>
	
Examples:
	| name       | dateL      | timeL      | dateD      | timeD    | type             | twin   |   first    |   last        | weight  | height   |  HR    | systolic | diastolic | twinFirst | twinLast | twinWeight | twinHeight | twinHR | twinSystolic | twinDiastolic |  id    | pw     |
	| AliceSmith | 01/01/2018 | 10:15 AM   | 09/16/2018 | 06:15 PM | Vaginal Delivery | Yes    |   Peach    |	Salinger	|  8      |    14    |  120   |  60      |    64     | April     | Salinger |    8       |      16    |   120  |     60       |      64       |



Scenario Outline: Edit a Patient labor and delivery reports
	Given OBGYN logs in using id <id> password <pw> and goes to Labor Delivery page
	And Dr Jenkin has logged in and chosen to view a patient labor and delivery record
	Then he selects patient <name>
	And he selects date to view <date>
	When labor and delivery reports displays, it shows <dateL> <timeL> <dateD> <timeD> <type> <twin> <first> <last> <weight> <height> <HR> <systolic> <diastolic> <twinFirst> <twinLast> <twinWeight> <twinHeight> <twinHR> <twinSystolic> <twinDiastolic>
	Then he edit pre-filled fields <weightNew>	

Examples:
	| name       | dateL      | timeL      | dateD      | timeD    | type             | twin   |   first    |   last        | weight  | height   |  HR    | systolic | diastolic | twinFirst | twinLast | twinWeight | twinHeight | twinHR | twinSystolic | twinDiastolic |   weightNew  |  id    | pw     |
	| AliceSmith | 01/01/2018 | 10:15 AM   | 09/16/2018 | 06:15 PM | Vaginal Delivery | Yes    |   Peach    |	Salinger	|  8      |    14    |  120   |  60      |    64     | April     | Salinger |    8       |      16    |   120  |     60       |      64       |     9        | OGBYN  | 123456 |

Scenario Outline: Invalid edit a Patient labor and delivery reports
	Given OBGYN logs in using id <id> password <pw> and goes to Labor Delivery page
	And Dr Jenkin has logged in and chosen to view a patient labor and delivery record
	Then he selects patient <name>
	And he selects date to view <date>
	When labor and delivery reports displays, it shows <dateL> <timeL> <dateD> <timeD> <type> <twin> <first> <last> <weight> <height> <HR> <systolic> <diastolic> <twinFirst> <twinLast> <twinWeight> <twinHeight> <twinHR> <twinSystolic> <twinDiastolic>
	Then he edit pre-filled fields <weightNew>	

Examples:
	| name       | dateL      | timeL      | dateD      | timeD    | type             | twin   |   first    |   last        | weight  | height   |  HR    | systolic | diastolic | twinFirst | twinLast | twinWeight | twinHeight | twinHR | twinSystolic | twinDiastolic |    weightNew   |  id    | pw     |
	| JillBob    | 01/01/2018 | 10:16 AM   | 09/16/2018 | 06:19 PM | Caesarean Section | No     |   Maya     |	Bob	        |  8      |    16    |  100   |  60      |    64     |           |          |            |            |        |              |               |        -8      | OGBYN  | 123456 |


Scenario Outline: Create an Invalid Patient labor and delivery reports
	Given OBGYN logs in using id <id> password <pw> and goes to Labor Delivery page
	And Dr Jenkin has logged in and chosen to create a patient new labor and delivery record
	Then he selects patient <name>
	Then <name> already has an obstetrics record
	And he enters <dateL> <timeL> <dateD> <timeD> <type> <twin> <first> <last> <weight> <height> <HR> <systolic> <diastolic> <twinFirst> <twinLast> <twinWeight> <twinHeight> <twinHR> <twinSystolic> <twinDiastolic>
	And he submits the form
	
Examples:
	| name       | dateL      | timeL      | dateD      | timeD    | type             | twin   |   first    |   last        | weight   | height   |  HR    | systolic | diastolic | twinFirst | twinLast | twinWeight | twinHeight | twinHR | twinSystolic | twinDiastolic |  id    | pw     |
	| AliceSmith | 01/01/2018 | 10:15 AM   | 09/16/2018 | 06:15 PM | Vaginal Delivery | Yes    |   Peach    |	Salinger	|  -8      |    14    |  120   |  60      |    64     | April     | Salinger |    8       |      16    |   120  |     60       |      64       | OGBYN  | 123456 |
	| AliceSmith | 01/01/2018 | 10:15 AM   | 09/16/2018 | 06:15 PM | Vaginal Delivery | Yes    |   Peach    |	Salinger	|   8      |    14    |  120   |  60      |    64     | April     | Salinger |    8       |      16    |   120  |     60       |      64       | OGBYN  | 123456 |

