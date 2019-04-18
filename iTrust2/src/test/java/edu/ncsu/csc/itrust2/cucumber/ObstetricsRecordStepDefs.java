package edu.ncsu.csc.itrust2.cucumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;

/**
 * Step definitions for HCP Obstetrics record.
 *
 * @author Chee Ng (cwng)
 */
public class ObstetricsRecordStepDefs extends CucumberTest {

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
     * Logging in as an HCPOBGYN or HCP or Patient
     *
     * @param id
     *            of user id
     * @param passwrd
     *            of password
     * @throws InterruptedException
     *             has the thread sleep
     */
    @Given ( "^Logging in system as an HCPOBGYN or HCP or Patient user id (.+) password (.+)$" )
    public void logAsOBGYN ( final String id, final String passwrd ) throws InterruptedException {
        Thread.sleep( 1000 );
        attemptLogout();

        driver.get( baseUrl );
        final WebElement username = driver.findElement( By.name( "username" ) );
        username.clear();
        username.sendKeys( id );
        final WebElement password = driver.findElement( By.name( "password" ) );
        password.clear();
        password.sendKeys( passwrd );
        final WebElement submit = driver.findElement( By.className( "btn" ) );
        submit.click();

        if ( id.equals( "OBGYN" ) || id.equals( "hcp" ) ) {
            assertEquals( "iTrust2: HCP Home", driver.getTitle() );
        }
        else {
            assertEquals( "iTrust2: Patient Home", driver.getTitle() );
        }
    }

    /**
     * Test when HCPOBGYN navigates to the view Obstetrics Records
     */
    @Then ( "^HCPOBGYN navigates to the view Obstetrics Records.$" )
    public void navigateToView () {
        waitForAngular();
        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('HCPObstetricsRecords').click();" );

        assertEquals( "iTrust2: View Patient Obstetrics Records", driver.getTitle() );

    }

    /**
     * Test when Patient navigates to the view Obstetrics Records
     */
    @Then ( "^Obstetrics patient views Obstetrics Records.$" )
    public void patientNavigateToView () {
        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('viewObstetricsRecords').click();" );

        assertEquals( "iTrust2: View Obstetrics Records", driver.getTitle() );

    }

    /**
     * HCP Obstetrics select particular patient on radio list
     *
     * @param first
     *            of first name of patient
     * @param last
     *            of last name of patient
     * @throws InterruptedException
     */
    @Then ( "^HCPOBGYN selects the patient with first name: (.+) and last name: (.+)" )
    public void selectObstetricsPatient ( final String first, final String last ) throws InterruptedException {
        final String username = first + last;

        // wait for the patients to load before searching
        waitForAngular();
        driver.findElement( By.cssSelector( "input[type=radio][id=" + username + "]" ) ).click();
        waitForAngular();
    }

    /**
     * Test when add new Obstetrics record
     *
     * @param date
     *            in string
     *
     * @param first
     *            of first name of patient
     * @param last
     *            of last name of patient
     * @throws InterruptedException
     */
    @Then ( "^HCPOBGYN creates a new Obstetrics Records with selected date: (.+) (.+) (.+)$" )
    public void initializeObPatient ( final String date, final String first, final String last )
            throws InterruptedException {
        waitForAngular();
        final String username = first + last;
        driver.findElement( By.cssSelector( "input[type=radio][id=" + username + "]" ) ).click();
        waitForAngular();
        final WebElement dateElement = driver.findElement( By.name( "date" ) );
        dateElement.sendKeys( date.replace( "/", "" ) );
        driver.findElement( By.id( "submitOb" ) ).click();
        Thread.sleep( 3000 );
        waitForAngular();

    }

    /**
     * Test add invalid last menstrual date new patient
     *
     * @param date
     *            in string
     *
     * @param first
     *            of first name of patient
     * @param last
     *            of last name of patient
     * @throws InterruptedException
     */
    @Then ( "^HCPOBGYN creates Invalid last menstrual date for the patient: (.+) (.+) (.+)$" )
    public void invalidlmd ( final String date, final String first, final String last ) throws InterruptedException {
        waitForAngular();
        final String username = first + last;
        driver.findElement( By.cssSelector( "input[type=radio][id=" + username + "]" ) ).click();
        waitForAngular();
        final WebElement dateElement = driver.findElement( By.name( "date" ) );
        dateElement.sendKeys( date.replace( "/", "" ) );
        driver.findElement( By.id( "submitOb" ) ).click();
        Thread.sleep( 3000 );
        waitForAngular();

        assertTextPresent( "Please input a valid date." );
    }

    /**
     * hcp addPregnancy information
     *
     * @param year
     *            of conception
     * @param weeks
     *            of pregnant
     * @param hours
     *            of labor
     * @param deliveryType
     *            of method
     * @param twins
     *            whether yes or no
     * @throws InterruptedException
     */
    @Then ( "^HCPOBGYN inputs pregnancy information: (\\d+) (\\d+) (\\d+) (.+) (.+)$" )
    public void addPregnancy ( final int year, final int weeks, final int hours, final String deliveryType,
            final String twins ) throws InterruptedException {
        waitForAngular();
        driver.findElement( By.id( "displayPregForm" ) ).click();
        waitForAngular();
        driver.findElement( By.name( "conceptionYear" ) ).clear();
        driver.findElement( By.name( "conceptionYear" ) ).sendKeys( Integer.toString( year ) );
        driver.findElement( By.name( "numWeeksPregnant" ) ).clear();
        driver.findElement( By.name( "numWeeksPregnant" ) ).sendKeys( Integer.toString( weeks ) );
        driver.findElement( By.name( "numHoursInLabor" ) ).clear();
        driver.findElement( By.name( "numHoursInLabor" ) ).sendKeys( Integer.toString( hours ) );

        final Select dropdown = new Select( driver.findElement( By.name( "deliveryMethod" ) ) );
        dropdown.selectByVisibleText( deliveryType );

        final Select dropdown2 = new Select( driver.findElement( By.name( "twinsYes" ) ) );
        dropdown2.selectByVisibleText( twins );

        Thread.sleep( 3000 );

        driver.findElement( By.id( "submitPregnancy" ) ).click();

    }

    /**
     * OBGYN add Invalid Pregnancy information
     *
     * @param year
     *            of conception
     * @param weeks
     *            of pregnant
     * @param hours
     *            of labor
     * @param deliveryType
     *            of method
     * @param twins
     *            whether yes or no
     * @param text
     *            is error message displayed
     * @throws InterruptedException
     */
    @Then ( "^HCPOBGYN enters invalid pregnancy information (\\d+), (.+), (.+), (.+), (.+), (.+)$" )
    public void addInavlidPregnancy ( final int year, final String weeks, final String hours, final String deliveryType,
            final String twins, final String text ) throws InterruptedException {
        waitForAngular();
        driver.findElement( By.id( "displayPregForm" ) ).click();
        waitForAngular();

        driver.findElement( By.name( "conceptionYear" ) ).clear();

        driver.findElement( By.name( "conceptionYear" ) ).sendKeys( Integer.toString( year ) );

        driver.findElement( By.name( "numWeeksPregnant" ) ).clear();
        if ( !weeks.equals( "N/A" ) ) {
            driver.findElement( By.name( "numWeeksPregnant" ) ).sendKeys( weeks );
        }

        driver.findElement( By.name( "numHoursInLabor" ) ).clear();
        if ( !hours.equals( "N/A" ) ) {
            driver.findElement( By.name( "numHoursInLabor" ) ).sendKeys( hours );
        }

        if ( !deliveryType.equals( "N/A" ) ) {
            final Select dropdown = new Select( driver.findElement( By.name( "deliveryMethod" ) ) );
            dropdown.selectByVisibleText( deliveryType );
        }
        if ( !twins.equals( "N/A" ) ) {
            final Select dropdown2 = new Select( driver.findElement( By.name( "twinsYes" ) ) );
            dropdown2.selectByVisibleText( twins );
        }
        Thread.sleep( 3000 );

        driver.findElement( By.id( "submitPregnancy" ) ).click();

        // waitForAngular();

        assertTextPresent( text );

    }

    /**
     * This function helps checking log history of user behavior
     *
     * @param transaction
     *            of type
     * @throws InterruptedException
     */
    @And ( "^The transaction code is (\\d+)$" )
    public void checkTransactionHistory ( final int transaction ) throws InterruptedException {
        Thread.sleep( 1000 );
        if ( transaction == 2403 ) {
            driver.get( baseUrl + "/patient/index" );
        }
        else {
            driver.get( baseUrl + "/hcp/index" );
        }
        Thread.sleep( 3000 );
        waitForAngular();
        switch ( transaction ) {
            case 2401:
                assertTextPresent( TransactionType.OBGYN_CREATE_OBS_RECORD.getDescription() );
                break;
            case 2402:
                assertTextPresent( TransactionType.HCP_VIEW_OBS_RECORD.getDescription() );
                break;
            case 2403:
                assertTextPresent( TransactionType.PATIENT_VIEW_OBS_RECORD.getDescription() );
                break;
            default:
                System.err.println( "Invalid Transaction Code." );
                break;
        }
    }

}
