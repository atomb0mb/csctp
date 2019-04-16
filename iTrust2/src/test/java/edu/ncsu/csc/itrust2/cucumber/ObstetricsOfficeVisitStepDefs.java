package edu.ncsu.csc.itrust2.cucumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.itrust2.forms.hcp.ObstetricsRecordForm;
import edu.ncsu.csc.itrust2.models.enums.AppointmentType;
import edu.ncsu.csc.itrust2.models.persistent.ObstetricsRecord;
import edu.ncsu.csc.itrust2.models.persistent.OfficeVisit;

/**
 * Step definitions for Obstetrics Office visit feature.
 *
 * @author msnedec, mthyaga
 */
public class ObstetricsOfficeVisitStepDefs extends CucumberTest {

    private final String baseUrl = "http://localhost:8080/iTrust2";

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
     * Makes sure the patient has an obstetrics record
     */
    @Then ( "^the patient has an obstetrics record already$" )
    public void checkRecordExisits () {
        final List<ObstetricsRecord> exist = ObstetricsRecord.getByPatient( "bobby" );
        if ( exist.size() == 0 ) {
            final ObstetricsRecordForm obsform = new ObstetricsRecordForm();
            obsform.setLastMenstrualPeriod( "2019-01-15" );
            final ObstetricsRecord obr = new ObstetricsRecord( obsform );
            obr.setPatient( "bobby" );
            obr.save();
        }
    }

    /**
     * Logs in HCP and navigates them to the document Office Visit page
     */
    @Then ( "^The OBGYN logs in and goes to document an office visit$" )
    public void loginOBGYN () {
        attemptLogout();

        driver.get( baseUrl );
        final WebElement username = driver.findElement( By.name( "username" ) );
        username.clear();
        username.sendKeys( "OGBYN" );
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
     * Adds the required obstetric values to the visit
     *
     * @param numWeeks
     *            the number of weeks pregnant
     * @param hr
     *            the fetal heart rate
     * @param height
     *            the fundal height
     */
    @And ( "^Adds obstetric values (.+) (.+) (.+)$" )
    public void addObstetricValues ( final String numWeeks, final String hr, final String height ) {
        waitForAngular();

        driver.findElement( By.name( "weeks" ) ).clear();
        driver.findElement( By.name( "weeks" ) ).sendKeys( numWeeks );

        driver.findElement( By.name( "heartRate" ) ).clear();
        driver.findElement( By.name( "heartRate" ) ).sendKeys( hr );

        driver.findElement( By.name( "fundalHeight" ) ).clear();
        driver.findElement( By.name( "fundalHeight" ) ).sendKeys( height );

    }

    /**
     * Method to check if the log was updated for oph office visits
     */
    @Then ( "The obstetric visit is added to the log" )
    public void logObstetricVisit () {
        waitForAngular();
        driver.get( baseUrl );
        final WebDriverWait wait = new WebDriverWait( driver, 20 );
        wait.until( ExpectedConditions.titleContains( "HCP Home" ) );
        assertEquals( "iTrust2: HCP Home", driver.getTitle() );
        wait.until( ExpectedConditions.elementToBeClickable( By.name( "transactionTypeCell" ) ) );
        assertTextPresent( "OBGYN office visit created" );

    }

    /**
     * Method to submit the office visit
     */
    @And ( "^The HCP submits the obstetric office visit$" )
    public void submitObstetricVisit () {
        waitForAngular();
        driver.findElement( By.name( "submit" ) ).click();

    }

    /**
     * Selects an office visit on the editing office visit page
     */
    @And ( "^The most recent obstetrics office visit is chosen$" )
    public void hcpObstetricSelect () {
        waitForAngular();
        assertEquals( "iTrust2: Edit Office Visit", driver.getTitle() );
        final List<OfficeVisit> visits = OfficeVisit.getOfficeVisits();
        long targetId = 0;

        for ( int i = 0; i < visits.size(); i++ ) {
            if ( visits.get( i ).getType().equals( AppointmentType.OBGYN_OFFICE_VISIT ) ) {
                targetId = visits.get( i ).getId();
            }
        }

        final WebElement elem = driver.findElement( By.cssSelector( "input[value=\"" + targetId + "\"]" ) );
        elem.click();
    }

    /**
     * Checks if the changes were allowed to be made
     */
    @Then ( "^The obstetrics office visit is updated successfully$" )
    public void hcpSuccessfulObstetricsVisit () {
        // confirm that the message is displayed
        try {
            driver.findElement( By.name( "success" ) ).getText().contains( "Office visit edited successfully" );
        }
        catch ( final Exception e ) {
            fail();
        }
    }

    /**
     * Checks if the changes were not allowed to be made
     */
    @Then ( "^The obstetrics office visit is not updated successfully$" )
    public void hcpUnsuccessfulObstetricsVisit () {
        // confirm that the error message is displayed
        try {
            final String temp = driver.findElement( By.name( "errorMsg" ) ).getText();
            if ( temp.equals( "" ) ) {
                fail();
            }
        }
        catch ( final Exception e ) {
            // Empty block
        }
    }

    /**
     * Modifies the date and number of weeks pregnant
     *
     * @param date
     *            the new date of the visit
     * @param weeks
     *            the new number of weeks pregnant
     */
    @And ( "^The HCP edits a new date (.+) and weeks (.+)$" )
    public void modifyObstetrics ( final String date, final String weeks ) {
        waitForAngular();

        final WebElement dateElement = driver.findElement( By.name( "date" ) );
        dateElement.sendKeys( date.replace( "/", "" ) );

        driver.findElement( By.name( "weeks" ) ).clear();
        driver.findElement( By.name( "weeks" ) ).sendKeys( weeks );
    }

    /**
     * Logins in as an HCP and navigates to the Edit Office Visit page
     */
    @When ( "^The OBGYN logs in to edit an office visit$" )
    public void editPageLogin () {
        attemptLogout();

        driver.get( baseUrl );

        final WebElement username = driver.findElement( By.name( "username" ) );
        username.clear();
        username.sendKeys( "OGBYN" );
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
     * Whether the visit has been added successfully
     */
    @Then ( "^The obstetric visit is valid and added$" )
    public void obstetricAddedValid () {
        waitForAngular();

        assertTextPresent( "created successfully" );
    }

    /**
     * Whether the visit has not been added
     */
    @Then ( "^The obstetric visit is not added$" )
    public void obstetricNotAdded () {
        waitForAngular();

        try {
            final String temp = driver.findElement( By.name( "errorMsg" ) ).getText();
            if ( temp.equals( "" ) ) {
                fail();
            }
        }
        catch ( final Exception e ) {
            // Empty block
        }
    }

}
