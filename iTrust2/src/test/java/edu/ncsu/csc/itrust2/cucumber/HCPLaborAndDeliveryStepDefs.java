package edu.ncsu.csc.itrust2.cucumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import edu.ncsu.csc.itrust2.forms.hcp.ObstetricsRecordForm;
import edu.ncsu.csc.itrust2.models.persistent.ObstetricsRecord;

public class HCPLaborAndDeliveryStepDefs extends CucumberTest {

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
     * Logging in as an OBGYN
     *
     *
     */
    @Given ( "^Given OBGYN logs in using id (.+) password (.+) and goes to Labor Delivery page$" )
    public void logAsOBGYN ( final String id, final String passwrd ) {
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

        if ( id.equals( "OGBYN" ) || id.equals( "hcp" ) ) {
            assertEquals( "iTrust2: HCP Home", driver.getTitle() );
        }
        else {
            assertEquals( "iTrust2: Patient Home", driver.getTitle() );
        }

        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('laborDeliveryReports').click();" );

        assertEquals( "iTrust2: Labor and Delivery Reports", driver.getTitle() );
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
    @Then ( "^he selects patient (.+)$" )
    public void selectObstetricsPatient ( final String username ) throws InterruptedException {

        // wait for the patients to load before searching
        waitForAngular();
        driver.findElement( By.cssSelector( "input[type=radio][id=" + username + "]" ) ).click();
        waitForAngular();
    }

    /**
     * Makes sure the patient has an obstetrics record
     */
    @Then ( "^(.+) already has an obstetrics record$" )
    public void checkRecordExisits ( final String name ) {
        final List<ObstetricsRecord> exist = ObstetricsRecord.getByPatient( name );
        if ( exist.size() == 0 ) {
            final ObstetricsRecordForm obsform = new ObstetricsRecordForm();
            obsform.setLastMenstrualPeriod( "2019-01-15" );
            final ObstetricsRecord obr = new ObstetricsRecord( obsform );
            obr.setPatient( name );
            obr.save();
        }
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
    @And ( "^he enters (.+) (.+) (.+) (.+) (.+) (.+) (.+) (.+) (\\d+) (\\d+) (\\d+) (\\d+) (\\d+) (.+) (.+) (\\d+) (\\d+) (\\d+) (\\d+) (\\d+) (.+)$" )
    public void addReport ( final String laborDate, final String laborTime, final String deliveryDate,
            final String deliveryTime, final String type, final String twin, final String first, final String last,
            final int weight, final int height, final int HR, final int systolic, final int diastolic,
            final String twinFirst, final String twinLast, final int twinWeight, final int twinHeight, final int twinHR,
            final int twinSystolic, final int twinDiastolic, final String twinType ) throws InterruptedException {
        waitForAngular();
        driver.findElement( By.id( "displayCreateForm" ) ).click();
        waitForAngular();
        driver.findElement( By.name( "inputlabordate" ) ).clear();
        // driver.findElement( By.name( "inputlabordate" ) ).sendKeys(
        // Integer.toString( laborDate ) );
        driver.findElement( By.name( "inputlabordate" ) ).sendKeys( laborDate );

        driver.findElement( By.name( "inputlabortime" ) ).clear();
        // driver.findElement( By.name( "inputlabordate" ) ).sendKeys(
        // Integer.toString( laborDate ) );
        driver.findElement( By.name( "inputlabortime" ) ).sendKeys( laborTime );

        driver.findElement( By.name( "inputdeliverydate" ) ).clear();
        // driver.findElement( By.name( "inputlabordate" ) ).sendKeys(
        // Integer.toString( laborDate ) );
        driver.findElement( By.name( "inputdeliverydate" ) ).sendKeys( deliveryDate );

        driver.findElement( By.name( "inputdeliverytime" ) ).clear();
        // driver.findElement( By.name( "inputlabordate" ) ).sendKeys(
        // Integer.toString( laborDate ) );
        driver.findElement( By.name( "inputdeliverytime" ) ).sendKeys( deliveryTime );

        driver.findElement( By.cssSelector( "input[type=radio][id=" + twin + "]" ) ).click();

        driver.findElement( By.cssSelector( "input[type=radio][id=" + type + "]" ) ).click(); // type
                                                                                              // of
                                                                                              // delivery

        // driver.findElement( By.cssSelector( "input[type=radio][id=" + twin +
        // "]" ) ).click(); // whether
        // there
        // are
        // twins

        // INFO ON INFANT
        driver.findElement( By.name( "firstname" ) ).clear();
        // driver.findElement( By.name( "inputlabordate" ) ).sendKeys(
        // Integer.toString( laborDate ) );
        driver.findElement( By.name( "firstname" ) ).sendKeys( first );

        driver.findElement( By.name( "lastname" ) ).clear();
        // driver.findElement( By.name( "inputlabordate" ) ).sendKeys(
        // Integer.toString( laborDate ) );
        driver.findElement( By.name( "lastname" ) ).sendKeys( last );

        driver.findElement( By.name( "weight" ) ).clear();
        // driver.findElement( By.name( "inputlabordate" ) ).sendKeys(
        // Integer.toString( laborDate ) );
        driver.findElement( By.name( "weight" ) ).sendKeys( Integer.toString( weight ) );

        driver.findElement( By.name( "length" ) ).clear();
        // driver.findElement( By.name( "inputlabordate" ) ).sendKeys(
        // Integer.toString( laborDate ) );
        driver.findElement( By.name( "length" ) ).sendKeys( Integer.toString( height ) );

        driver.findElement( By.name( "heartrate" ) ).clear();
        // driver.findElement( By.name( "inputlabordate" ) ).sendKeys(
        // Integer.toString( laborDate ) );
        driver.findElement( By.name( "heartrate" ) ).sendKeys( Integer.toString( HR ) );

        driver.findElement( By.name( "systolic" ) ).clear();
        // driver.findElement( By.name( "inputlabordate" ) ).sendKeys(
        // Integer.toString( laborDate ) );
        driver.findElement( By.name( "systolic" ) ).sendKeys( Integer.toString( systolic ) );

        driver.findElement( By.name( "diastolic" ) ).clear();
        // driver.findElement( By.name( "inputlabordate" ) ).sendKeys(
        // Integer.toString( laborDate ) );
        driver.findElement( By.name( "diastolic" ) ).sendKeys( Integer.toString( diastolic ) );

        if ( twin.equals( "Yes" ) ) {

            driver.findElement( By.cssSelector( "input[type=radio][id=" + twinType + "]" ) ).click();
            // INFO ON TWIN
            driver.findElement( By.name( "firstnameTwin" ) ).clear();
            // driver.findElement( By.name( "inputlabordate" ) ).sendKeys(
            // Integer.toString( laborDate ) );
            driver.findElement( By.name( "firstnameTwin" ) ).sendKeys( twinFirst );

            driver.findElement( By.name( "lastnameTwin" ) ).clear();
            // driver.findElement( By.name( "inputlabordate" ) ).sendKeys(
            // Integer.toString( laborDate ) );
            driver.findElement( By.name( "lastnameTwin" ) ).sendKeys( twinLast );

            driver.findElement( By.name( "weightTwin" ) ).clear();
            // driver.findElement( By.name( "inputlabordate" ) ).sendKeys(
            // Integer.toString( laborDate ) );
            driver.findElement( By.name( "weightTwin" ) ).sendKeys( Integer.toString( twinWeight ) );

            driver.findElement( By.name( "lengthTwin" ) ).clear();
            // driver.findElement( By.name( "inputlabordate" ) ).sendKeys(
            // Integer.toString( laborDate ) );
            driver.findElement( By.name( "lengthTwin" ) ).sendKeys( Integer.toString( twinHeight ) );

            driver.findElement( By.name( "heartrateTwin" ) ).clear();
            // driver.findElement( By.name( "inputlabordate" ) ).sendKeys(
            // Integer.toString( laborDate ) );
            driver.findElement( By.name( "heartrateTwin" ) ).sendKeys( Integer.toString( twinHR ) );

            driver.findElement( By.name( "systolicTwin" ) ).clear();
            // driver.findElement( By.name( "inputlabordate" ) ).sendKeys(
            // Integer.toString( laborDate ) );
            driver.findElement( By.name( "systolicTwin" ) ).sendKeys( Integer.toString( twinSystolic ) );

            driver.findElement( By.name( "diastolic" ) ).clear();
            // driver.findElement( By.name( "inputlabordate" ) ).sendKeys(
            // Integer.toString( laborDate ) );
            driver.findElement( By.name( "diastolicTwin" ) ).sendKeys( Integer.toString( twinDiastolic ) );

        }
        waitForAngular();
        driver.findElement( By.id( "submitLD" ) ).click();
        waitForAngular();

    }

    @Then ( "^And he submits the form$" )
    public void submitFormClick ( final String username ) throws InterruptedException {

        waitForAngular();
        driver.findElement( By.id( "submitLD" ) ).click();
        waitForAngular();

    }

    /**
     * Checks if the changes were allowed to be made
     */
    @Then ( "^Then a success message is displayed (.+)$" )
    public void successfulLDCreate () {
        // confirm that the message is displayed
        try {
            driver.findElement( By.name( "success" ) ).getText().contains( "success" );
        }
        catch ( final Exception e ) {
            fail();
        }
    }

    // *************************BEGIN INVALID CREATE
    // LD*************************************

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
    @And ( "^he incorrectly enters invalid values (.+) (.+) (.+) (.+) (.+) (.+) (.+) (.+) (\\d+) (\\d+) (\\d+) (\\d+) (\\d+) (.+) (.+) (\\d+) (\\d+) (\\d+) (\\d+) (\\d+) (.+)$" )
    public void addReportInvalid ( final String laborDate, final String laborTime, final String deliveryDate,
            final String deliveryTime, final String type, final String twin, final String first, final String last,
            final int weight, final int height, final int HR, final int systolic, final int diastolic,
            final String twinFirst, final String twinLast, final int twinWeight, final int twinHeight, final int twinHR,
            final int twinSystolic, final int twinDiastolic, final String twinType ) throws InterruptedException {
        waitForAngular();
        driver.findElement( By.id( "displayCreateForm" ) ).click();
        waitForAngular();
        driver.findElement( By.name( "inputlabordate" ) ).clear();
        // driver.findElement( By.name( "inputlabordate" ) ).sendKeys(
        // Integer.toString( laborDate ) );
        driver.findElement( By.name( "inputlabordate" ) ).sendKeys( laborDate );

        driver.findElement( By.name( "inputlabortime" ) ).clear();
        // driver.findElement( By.name( "inputlabordate" ) ).sendKeys(
        // Integer.toString( laborDate ) );
        driver.findElement( By.name( "inputlabortime" ) ).sendKeys( laborTime );

        driver.findElement( By.name( "inputdeliverydate" ) ).clear();
        // driver.findElement( By.name( "inputlabordate" ) ).sendKeys(
        // Integer.toString( laborDate ) );
        driver.findElement( By.name( "inputdeliverydate" ) ).sendKeys( deliveryDate );

        driver.findElement( By.name( "inputdeliverytime" ) ).clear();
        // driver.findElement( By.name( "inputlabordate" ) ).sendKeys(
        // Integer.toString( laborDate ) );
        driver.findElement( By.name( "inputdeliverytime" ) ).sendKeys( deliveryTime );

        driver.findElement( By.cssSelector( "input[type=radio][id=" + twin + "]" ) ).click();

        driver.findElement( By.cssSelector( "input[type=radio][id=" + type + "]" ) ).click(); // type
                                                                                              // of
                                                                                              // delivery

        // driver.findElement( By.cssSelector( "input[type=radio][id=" + twin +
        // "]" ) ).click(); // whether
        // there
        // are
        // twins

        // INFO ON INFANT
        driver.findElement( By.name( "firstname" ) ).clear();
        // driver.findElement( By.name( "inputlabordate" ) ).sendKeys(
        // Integer.toString( laborDate ) );
        driver.findElement( By.name( "firstname" ) ).sendKeys( first );

        driver.findElement( By.name( "lastname" ) ).clear();
        // driver.findElement( By.name( "inputlabordate" ) ).sendKeys(
        // Integer.toString( laborDate ) );
        driver.findElement( By.name( "lastname" ) ).sendKeys( last );

        driver.findElement( By.name( "weight" ) ).clear();
        // driver.findElement( By.name( "inputlabordate" ) ).sendKeys(
        // Integer.toString( laborDate ) );
        driver.findElement( By.name( "weight" ) ).sendKeys( Integer.toString( weight ) );

        driver.findElement( By.name( "length" ) ).clear();
        // driver.findElement( By.name( "inputlabordate" ) ).sendKeys(
        // Integer.toString( laborDate ) );
        driver.findElement( By.name( "length" ) ).sendKeys( Integer.toString( height ) );

        driver.findElement( By.name( "heartrate" ) ).clear();
        // driver.findElement( By.name( "inputlabordate" ) ).sendKeys(
        // Integer.toString( laborDate ) );
        driver.findElement( By.name( "heartrate" ) ).sendKeys( Integer.toString( HR ) );

        driver.findElement( By.name( "systolic" ) ).clear();
        // driver.findElement( By.name( "inputlabordate" ) ).sendKeys(
        // Integer.toString( laborDate ) );
        driver.findElement( By.name( "systolic" ) ).sendKeys( Integer.toString( systolic ) );

        driver.findElement( By.name( "diastolic" ) ).clear();
        // driver.findElement( By.name( "inputlabordate" ) ).sendKeys(
        // Integer.toString( laborDate ) );
        driver.findElement( By.name( "diastolic" ) ).sendKeys( Integer.toString( diastolic ) );

        if ( twin.equals( "Yes" ) ) {

            driver.findElement( By.cssSelector( "input[type=radio][id=" + twinType + "]" ) ).click();
            // INFO ON TWIN
            driver.findElement( By.name( "firstnameTwin" ) ).clear();
            // driver.findElement( By.name( "inputlabordate" ) ).sendKeys(
            // Integer.toString( laborDate ) );
            driver.findElement( By.name( "firstnameTwin" ) ).sendKeys( twinFirst );

            driver.findElement( By.name( "lastnameTwin" ) ).clear();
            // driver.findElement( By.name( "inputlabordate" ) ).sendKeys(
            // Integer.toString( laborDate ) );
            driver.findElement( By.name( "lastnameTwin" ) ).sendKeys( twinLast );

            driver.findElement( By.name( "weightTwin" ) ).clear();
            // driver.findElement( By.name( "inputlabordate" ) ).sendKeys(
            // Integer.toString( laborDate ) );
            driver.findElement( By.name( "weightTwin" ) ).sendKeys( Integer.toString( twinWeight ) );

            driver.findElement( By.name( "lengthTwin" ) ).clear();
            // driver.findElement( By.name( "inputlabordate" ) ).sendKeys(
            // Integer.toString( laborDate ) );
            driver.findElement( By.name( "lengthTwin" ) ).sendKeys( Integer.toString( twinHeight ) );

            driver.findElement( By.name( "heartrateTwin" ) ).clear();
            // driver.findElement( By.name( "inputlabordate" ) ).sendKeys(
            // Integer.toString( laborDate ) );
            driver.findElement( By.name( "heartrateTwin" ) ).sendKeys( Integer.toString( twinHR ) );

            driver.findElement( By.name( "systolicTwin" ) ).clear();
            // driver.findElement( By.name( "inputlabordate" ) ).sendKeys(
            // Integer.toString( laborDate ) );
            driver.findElement( By.name( "systolicTwin" ) ).sendKeys( Integer.toString( twinSystolic ) );

            driver.findElement( By.name( "diastolic" ) ).clear();
            // driver.findElement( By.name( "inputlabordate" ) ).sendKeys(
            // Integer.toString( laborDate ) );
            driver.findElement( By.name( "diastolicTwin" ) ).sendKeys( Integer.toString( twinDiastolic ) );

        }
        waitForAngular();
        driver.findElement( By.id( "submitLD" ) ).click();
        waitForAngular();

    }
    // *************************BEGIN VIEW*************************************

    @And ( "^The labor and delivery reports display (.+) (.+) (.+) (.+) (.+) (.+) (.+) (.+) (\\d+) (\\d+) (\\d+) (\\d+) (\\d+) (.+) (.+) (\\d+) (\\d+) (\\d+) (\\d+) (\\d+) (.+)$" )
    public void viewReports ( final String laborDate, final String laborTime, final String deliveryDate,
            final String deliveryTime, final String type, final String twin, final String first, final String last,
            final int weight, final int height, final int HR, final int systolic, final int diastolic,
            final String twinFirst, final String twinLast, final int twinWeight, final int twinHeight, final int twinHR,
            final int twinSystolic, final int twinDiastolic, final String twinType ) throws InterruptedException {

        waitForAngular();
        driver.findElement( By.name( "firstname-0" ) ).getText().contains( "first" );
        driver.findElement( By.name( "lastname-0" ) ).getText().contains( "last" );
        driver.findElement( By.name( "firstnameTwin-0" ) ).getText().contains( "twinFirst" );
        driver.findElement( By.name( "lastnameTwin-0" ) ).getText().contains( "twinLast" );

        driver.findElement( By.name( "weight-0" ) ).getText().contains( "weight" );
        driver.findElement( By.name( "weightTwin-0" ) ).getText().contains( "twinWeight" );

    }

    // *************************BEGIN EDIT LABOR AND DELIVERY
    // TESTS*************************************

    /**
     * Logging in as an OBGYN
     *
     *
     */
    @Given ( "^Given OBGYN logs in using id (.+) password (.+) and goes to Edit Labor Delivery page$" )
    public void editAsOBGYN ( final String id, final String passwrd ) {
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

        if ( id.equals( "OGBYN" ) ) {
            assertEquals( "iTrust2: HCP Home", driver.getTitle() );
        }

        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('editLaborDeliveryReport').click();" );

        assertEquals( "iTrust2: Labor and Delivery Reports", driver.getTitle() );
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
    @Then ( "^he selects patient to edit(.+)$" )
    public void selectPatientToEdit ( final String username ) throws InterruptedException {

        // wait for the patients to load before searching
        waitForAngular();
        driver.findElement( By.cssSelector( "input[type=radio][id=" + username + "]" ) ).click();
        waitForAngular();
    }

}
