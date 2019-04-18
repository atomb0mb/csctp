# Feature: Obstetrics HCP create, view, and edit a patient's labor and delivery reports
	# As an OB HCP
        # I want to create a patient's labor and delivery reports
	# After create the labor and deliver report, I want to view a patient's labor and delivery reports
        # Beside, I want to able to edit a patient's labor and delivery reports
	# So that all of their labor and delivery report is displayed and updated 


# Scenario Outline: Create a Patient labor and delivery reports
	# Given the required patient and HCP exist
	# And Dr Jenkin has logged in and chosen to create a patient new labor and delivery record
	# Then he selects patient <name>
	# And he enters <dateL> <timeL> <dateD> <timeD> <type> <pounds> <inches> <bpm> <mmhg> <first> and <last>
	# And he submits the form
	# Then a success message is displayed <text>
	# Then the system sends him to the screen displays labor and delivery record
	
# Examples:
	# | name    | dateL       | timeL     | dateD      | timeD    | type             | pounds | inches | bpm | mmhg | first | last     | text |
	# | patient | 01/01/2018 | 10:15 AM   | 09/16/2018 | 06:15 PM | Vaginal Delivery | 8.2    | 14.0   | 120 | 64   | Peach | Salinger | Labor and Delivery Report created successfully |
	# | patient | 01/01/2018 | 10:16 AM   | 09/16/2018 | 06:19 PM | Cesarean Section | 8.8    | 13.6   | 116 | 63   | Peach | Savanna  | Labor and Delivery Report created successfully |
	# | emy13   | 04/01/2019 | 11:15 AM   | 10/02/2019 | 03:15 AM | Miscarriage      |        |        |     |      |       |          | Labor and Delivery Report created successfully |


# Scenario Outline: View a Patient labor and delivery reports
	# Given the required patient and HCP exist
	# And Dr Jenkin has logged in and chosen to view a patient labor and delivery record
	# Then he selects patient <name>
	# And he selects date to view <date>
	# The labor and delivery reports display <dateL> <timeL> <dateD> <timeD <type> <pounds> <inches> <bpm> <mmhg> <first> and <last>
	
# Examples:
	# | name      | dateL       | timeL     | dateD      | timeD    | type             | pounds | inches | bpm | mmhg | first | last     |
	# | patient   | 01/01/2018 | 10:15 AM   | 09/16/2018 | 06:15 PM | Vaginal Delivery | 8.2    | 14.0   | 120 | 64   | Peach | Salinger |
        # | patient   | 01/01/2018 | 10:16 AM   | 09/16/2018 | 06:19 PM | Cesarean Section | 8.8    | 13.6   | 116 | 63   | Peach | Savanna  |
	# | emy13     | 04/01/2019 | 11:15 AM   | 10/02/2019 | 03:15 AM | Miscarriage      |        |        |     |      |       |          |


# Scenario Outline: Edit a Patient labor and delivery reports
	# Given the required patient and HCP exist
	# And Dr Jenkin has logged in and chosen to view a patient labor and delivery record
	# Then he selects patient <name>
	# And he selects date to view <date>
	# When labor and delivery reports displays, it shows <dateL> <timeL> <dateD> <timeD <type> <pounds> <inches> <bpm> <mmhg> <first> and <last>
	# Then he edit pre-filled fields <dateL1> <timeL1> <dateD1> <timeD1> <type1> <pounds1> <inches1> <bpm1> <mmhg1> <first1> and <last1>
	# Then he submits the edited information <text>
	

# Examples:
	# | name      | dateL       | timeL     | dateD      | timeD    | type             | pounds | inches | bpm | mmhg | first | last     |   dateL1      | timeL1    | dateD1     | timeD1   | type1                      | pounds1| inches1 | bpm1 | mmhg1 | first1 | last1   | text            |
	# | patient   | 01/01/2018 | 10:15 AM   | 09/16/2018 | 06:15 PM | Cesarean Section | 8.2    | 14.0   | 120 | 64   | Peach | Salinger | 01/02/2018    | 10:19 AM  | 09/17/2018 | 10:20 AM | Cesarean Section  |  8.1   | 14.1    | 116  | 61    | Peach1 | Sven    | Update success! |
	# | patient   | 01/01/2018 | 10:16 AM   | 09/16/2018 | 06:19 PM | Vaginal Delivery | 8.8    | 13.6   | 116 | 63   | Peach | Savanna  | 01/02/2018    | 10:21 AM  | 09/17/2018 | 10:25 AM | Vaginal Delivery |  7.9   | 13.1    | 119  | 62    | Peach2 | Swanson | Update success! |
	# | emy13     | 04/01/2019 | 11:15 AM   | 10/02/2019 | 03:15 AM | Miscarriage      |        |        |     |      |       |          | 04/02/2019    | 12:15 PM  | 10/03/2019 | 04:00 PM | Miscarriage |         |      |       |        |                  | |update success! |

# Scenario Outline: Create an Invalid Patient labor and delivery reports
	# Given the required patient and HCP exist
	# And Dr Jenkin has logged in and chosen to create a patient new labor and delivery record
	# Then he selects patient <name>
	# And he enters <dateL> <timeL> <dateD> <timeD> <type> <pounds> <inches> <bpm> <mmhg> <first> and <last>
	# And he submits the form
	# Then error message is displayed <error>
	
# Examples:
	# | name     | dateL       | timeL     | dateD      | timeD    | type             | pounds | inches | bpm | mmhg | first | last     | error           |
	# | patient2 | 01/01/2019 | 10:15 AM   | 09/16/2018 | 06:15 PM | Vaginal Delivery | 8.2    | 14.0   | 120 | 64   | Peach | Salinger | Invalid Date    |
	# | patient2 | 01/01/2019 | 10:15 AM   | 09/16/2018 | 06:15 PM | Cesarean Section | 8.2    | 14.0   | 120 | 64   | Peach | Salinger | Invalid Date    |
	# | patient2 | 01/01/2019 | 10:15 AM   | 09/16/2018 | 06:15 PM | Miscarriage      |        |        |     |      |       |          | Invalid Date    |
	# | patient2 | 01/01/2019 | 10:15 AM   | 09/16/2019 | 06:15 PM | Vaginal Delivery |        | 14.0   | 120 | 64   | Peach | Salinger | Invalid Weight  |
	# | patient2 | 01/01/2019 | 10:15 AM   | 09/16/2019 | 06:15 PM | Vaginal Delivery | 8.7    |        | 120 | 64   | Peach | Salinger | Invalid Length  |
	# | patient2 | 01/01/2019 | 10:15 AM   | 09/16/2019 | 06:15 PM | Vaginal Delivery | 8.7    | 14.0   |     | 64   | Peach | Salinger | Invalid BPM     |
	# | patient2 | 01/01/2019 | 10:15 AM   | 09/16/2019 | 06:15 PM | Vaginal Delivery | 8.7    | 14.0   | 120 |      | Peach | Salinger | Invalid Blood Pressure  |
