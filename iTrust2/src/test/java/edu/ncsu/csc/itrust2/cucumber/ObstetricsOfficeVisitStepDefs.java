package edu.ncsu.csc.itrust2.cucumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.itrust2.models.enums.AppointmentType;
import edu.ncsu.csc.itrust2.models.persistent.OfficeVisit;

/**
 * Step definitions for Ophthalmology Office Surgery Visit feature.
 *
 * @author Joseph Tysinger (jltysin2)
 */
public class ObstetricsOfficeVisitStepDefs extends CucumberTest {

    private final String baseUrl       = "http://localhost:8080/iTrust2";
    private final String obgynString   = "OGBYN";
    private final String patientString = "bobby";

    /**
     * Asserts that the text is on the page
     *
     * @param text
     *            text to check
     */
    public void assertTextPresent ( final String text ) {
        try {
            assertTrue( driver.getPageSource().contains( text ) );
        }
        catch ( final Exception e ) {
            fail();
        }
    }

    /**
     * Fills in the date and time fields with the specified date and time.
     *
     * @param date
     *            The date to enter.
     * @param time
     *            The time to enter.
     */
    private void fillInDateTime ( final String dateField, final String date, final String timeField,
            final String time ) {
        fillInDate( dateField, date );
        fillInTime( timeField, time );
    }

    /**
     * Fills in the date field with the specified date.
     *
     * @param date
     *            The date to enter.
     */
    private void fillInDate ( final String dateField, final String date ) {
        driver.findElement( By.name( dateField ) ).clear();
        final WebElement dateElement = driver.findElement( By.name( dateField ) );
        dateElement.sendKeys( date.replace( "/", "" ) );
    }

    /**
     * Fills in the time field with the specified time.
     *
     * @param time
     *            The time to enter.
     */
    private void fillInTime ( final String timeField, String time ) {
        // Zero-pad the time for entry
        if ( time.length() == 7 ) {
            time = "0" + time;
        }

        driver.findElement( By.name( timeField ) ).clear();
        final WebElement timeElement = driver.findElement( By.name( timeField ) );
        timeElement.sendKeys( time.replace( ":", "" ).replace( " ", "" ) );
    }

    /**
     * Creates a new OPH HCP User
     *
     * @Given ( "^There exists an OB/GYN HCP in the system$" ) public void
     *        obgynExists () { attemptLogout();
     *
     *        final User obHCP = new User( obgynString, "123456",
     *        Role.ROLE_OBGYN, 1 ); obHCP.save();
     *
     *        // All tests can safely assume the existence of the
     *        'ophthalmologist // hcp', // 'admin', // 'OBGYN' and // 'patient'
     *        users }/
     *
     *        /** Logs in HCP and navigates them to the document Office Visit
     *        page
     */
    @Then ( "^The OBGYN logs in and navigates to the Document Obstetrics Office Visit page$" )
    public void loginObgynDiaries () {
        attemptLogout();

        driver.get( baseUrl );
        final WebElement username = driver.findElement( By.name( "username" ) );
        username.clear();
        username.sendKeys( obgynString );
        final WebElement password = driver.findElement( By.name( "password" ) );
        password.clear();
        password.sendKeys( "123456" );
        final WebElement submit = driver.findElement( By.className( "btn" ) );
        submit.click();

        assertEquals( "iTrust2: HCP Home", driver.getTitle() );

        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('documentOfficeVisit').click();" );

        assertEquals( "iTrust2: Document Office Visit", driver.getTitle() );
    }

    /**
     * Adds basic information about the office visit into the page
     *
     * @param date
     *            the date of the office visit
     * @param time
     *            the time of the office visit
     * @param patient
     *            the patient to select from the list of patients
     * @param type
     *            the type of office visit to select
     * @param hospital
     *            the hospital that the office visit is scheduled at
     */
    @When ( "^The OBGYN enters the date (.+), time (.+), patient (.+), type of visit (.+), hospital (.+)$" )
    public void addBasicInfo ( final String date, final String time, final String patient, final String type,
            final String hospital ) {
        waitForAngular();

        fillInDateTime( "date", date, "time", time );

        final WebElement patientElement = driver.findElement( By.cssSelector( "input[value=\"" + patient + "\"]" ) );
        patientElement.click();

        final WebElement typeElement = driver.findElement( By.cssSelector( "input[value=\"" + type + "\"]" ) );
        typeElement.click();

        final WebElement hospitalElement = driver.findElement( By.cssSelector( "input[value=\"" + hospital + "\"]" ) );
        hospitalElement.click();
    }

    /**
     * Correctly enter the obstetrics information into the page
     *
     * @param weeks
     *            number of weeks into patient's pregnancy
     * @param heartRate
     *            the patient's heart rate
     * @param fundalHeight
     *            size of uterus, used to measure fetal growth
     * @param twins
     *            whether or not the patient is pregnant with twins
     * @param lowLyingPlacenta
     *            whether or not the patient has a low-lying placenta
     */
    @And ( "^The OBGYN enters the number of weeks pregnant (.+), fetal heart rate (HR, in BPM) (.+), fundal height of uterus (in cm) (.+), twins (yes/no) (.+), low-lying placenta (llp, present/not present) (.+)$" )
    public void documentValid ( final String weeks, final String heartRate, final String fundalHeight,
            final String twins, final String lowLyingPlacenta ) {
        waitForAngular();

        driver.findElement( By.name( "weeks" ) ).clear();
        driver.findElement( By.name( "weeks" ) ).sendKeys( weeks );

        driver.findElement( By.name( "heartRate" ) ).clear();
        driver.findElement( By.name( "heartRate" ) ).sendKeys( heartRate );

        driver.findElement( By.name( "height" ) ).clear();
        driver.findElement( By.name( "height" ) ).sendKeys( fundalHeight );

        driver.findElement( By.name( "twins" ) ).clear();
        driver.findElement( By.name( "twins" ) ).sendKeys( twins );

        driver.findElement( By.name( "placenta" ) ).clear();
        driver.findElement( By.name( "placenta" ) ).sendKeys( lowLyingPlacenta );

        // final WebElement patientSmokingElement = driver
        // .findElement( By.cssSelector( "input[value=\"" + patientSmoking +
        // "\"]" ) );
        // patientSmokingElement.click();
        //
        // final WebElement smokingElement = driver
        // .findElement( By.cssSelector( "input[value=\"" + smokingStatus +
        // "\"]" ) );
        // smokingElement.click();
    }

    /**
     * Incorrectly enter the obstetrics information into the page
     *
     * @param weeks
     *            number of weeks into patient's pregnancy
     * @param heartRate
     *            the patient's heart rate
     * @param fundalHeight
     *            size of uterus, used to measure fetal growth
     * @param twins
     *            whether or not the patient is pregnant with twins
     * @param lowLyingPlacenta
     *            whether or not the patient has a low-lying placenta
     */
    @And ( "^The OBGYN incorrectly enters the invalid number of weeks pregnant (.+), fetal heart rate (HR, in BPM) (.+), fundal height of uterus (in cm) (.+), twins (yes/no) (.+), low-lying placenta (llp, present/not present) (.+)$" )
    public void documentInvalid ( final String weeks, final String heartRate, final String fundalHeight,
            final String twins, final String lowLyingPlacenta ) {
        waitForAngular();

        driver.findElement( By.name( "weeks" ) ).clear();
        driver.findElement( By.name( "weeks" ) ).sendKeys( weeks );

        driver.findElement( By.name( "heartRate" ) ).clear();
        driver.findElement( By.name( "heartRate" ) ).sendKeys( heartRate );

        driver.findElement( By.name( "height" ) ).clear();
        driver.findElement( By.name( "height" ) ).sendKeys( fundalHeight );

        driver.findElement( By.name( "twins" ) ).clear();
        driver.findElement( By.name( "twins" ) ).sendKeys( twins );

        driver.findElement( By.name( "placenta" ) ).clear();
        driver.findElement( By.name( "placenta" ) ).sendKeys( lowLyingPlacenta );

        // final WebElement patientSmokingElement = driver
        // .findElement( By.cssSelector( "input[value=\"" + patientSmoking +
        // "\"]" ) );
        // patientSmokingElement.click();
        //
        // final WebElement smokingElement = driver
        // .findElement( By.cssSelector( "input[value=\"" + smokingStatus +
        // "\"]" ) );
        // smokingElement.click();
    }

    /**
     * Method to submit the office visit
     */
    @And ( "^The OBGYN submits the obstetrics office visit$" )
    public void submitOfficeVisit () {
        waitForAngular();
        driver.findElement( By.name( "submit" ) ).click();
    }

    /**
     * Method to check if the documentation was successful
     */
    @Then ( "^The obstetrics office visit is documented successfully$" )
    public void documentedSuccessfully () {
        waitForAngular();

        // confirm that the message is displayed
        try {
            driver.findElement( By.name( "success" ) ).getText().contains( "Office visit created successfully" );
        }
        catch ( final Exception e ) {
            fail();
        }
    }

    /**
     * Method to check if the documentation failed
     */
    @Then ( "^The obstetrics office visit is not documented$" )
    public void notSubmitted () {
        waitForAngular();

        // confirm that the error message is displayed
        try {
            if ( driver.findElement( By.name( "success" ) ).getText()
                    .contains( "Office visit created successfully" ) ) {
                fail();
            }
        }
        catch ( final Exception e ) {
        }

    }

    /**
     * Logs in HCP and navigates them to the document Office Visit page
     */
    @Then ( "^Then The OBGYN logs in and navigates to the Edit Obstetrics Office Visit page$" )
    public void loginToEdit () {
        attemptLogout();

        driver.get( baseUrl );

        final WebElement username = driver.findElement( By.name( "username" ) );
        username.clear();
        username.sendKeys( obgynString );
        final WebElement password = driver.findElement( By.name( "password" ) );
        password.clear();
        password.sendKeys( "123456" );
        final WebElement submit = driver.findElement( By.className( "btn" ) );
        submit.click();

        assertEquals( "iTrust2: HCP Home", driver.getTitle() );

        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('editOfficeVisit').click();" );

        assertEquals( "iTrust2: Edit Office Visit", driver.getTitle() );
    }

    /**
     * Selects an office visit on the editing office visit page
     */
    @When ( "^The OBGYN selects the existing obstetrics office visit$" )
    public void hcpSelectOfficeVisit () {
        final List<OfficeVisit> visits = OfficeVisit.getOfficeVisits();
        long targetId = 0;

        for ( int i = 0; i < visits.size(); i++ ) {
            if ( visits.get( i ).getType().equals( AppointmentType.OBGYN_OFFICE_VISIT )
                    && visits.get( i ).getPatient().getUsername().equals( patientString ) ) {
                targetId = visits.get( i ).getId();
            }
        }

        final WebElement elem = driver.findElement( By.cssSelector( "input[value=\"" + targetId + "\"]" ) );
        elem.click();
    }

}
