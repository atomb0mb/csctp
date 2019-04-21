package edu.ncsu.csc.itrust2.cucumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import edu.ncsu.csc.itrust2.cucumber.CucumberTest;
import edu.ncsu.csc.itrust2.forms.hcp.ObstetricsRecordForm;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.models.persistent.ObstetricsRecord;

/**
 * This cucumber tests the Labor and delivery report of add, view, and edit
 * functionality. It includes valid and invalid input. As checks the correct
 * transaction code.
 *
 * Flow: The OBGYN select patient with obstetrics record. Then he select the
 * twin option, delivery method, date and time of labor and delivery report.
 * Then selection of delivery method determines whether he should fill in
 * newborn information of basic health metrics or not.
 *
 * @author Chee Ng (cwng)
 *
 */
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
     * Login as an OBGYN
     *
     * @param id
     *            of user id
     * @param passwrd
     *            of password
     * @param text
     *            to determine what action
     */
    @Given ( "^OBGYN logs in using id (.+) password (.+) and goes to Labor Delivery page to (.+)$" )
    public void logAsOBGYN ( final String id, final String passwrd, final String text ) {
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

        if ( text.equals( "edit" ) ) {
            ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('editLDReport').click();" );

        }
        else if ( text.equals( "hcpview" ) ) {
            ( (JavascriptExecutor) driver )
                    .executeScript( "document.getElementById('HCPLaborDeliveryReports').click();" );
        }
        else {
            ( (JavascriptExecutor) driver )
                    .executeScript( "document.getElementById('OBGYNLaborDeliveryReport').click();" );

            assertEquals( "iTrust2: Labor and Delivery Reports", driver.getTitle() );
        }
    }

    /**
     * Login as different user
     *
     * @param role
     *            of user
     * @param id
     *            of user id
     * @param pwd
     *            of user password
     */
    @Given ( "^The role of user is (.+). User id is (.+) and password (.+)$" )
    public void logAsDifferentRole ( final String role, final String id, final String pwd ) {
        attemptLogout();

        driver.get( baseUrl );
        final WebElement username = driver.findElement( By.name( "username" ) );
        username.clear();
        username.sendKeys( id );
        final WebElement password = driver.findElement( By.name( "password" ) );
        password.clear();
        password.sendKeys( pwd );
        final WebElement submit = driver.findElement( By.className( "btn" ) );
        submit.click();

        if ( role.equals( "HCP" ) ) {
            assertEquals( "iTrust2: HCP Home", driver.getTitle() );
        }
        else {
            assertEquals( "iTrust2: Patient Home", driver.getTitle() );
        }

        if ( role.equals( "HCP" ) ) {
            ( (JavascriptExecutor) driver )
                    .executeScript( "document.getElementById('HCPLaborDeliveryReports').click();" );

        }

        else {
            ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('laborDeliveryReports').click();" );

            assertEquals( "iTrust2: Labor and Delivery Reports", driver.getTitle() );

            // driver.get( baseUrl + "/patient/index" );
        }
    }

    /**
     * HCP Obstetrics select particular patient on radio list
     *
     * @param username
     *            of username of patient
     * @throws InterruptedException
     */
    @Then ( "^he chooses the pregnant patient (.+)$" )
    public void selectObstetricsPatient ( final String username ) throws InterruptedException {

        // wait for the patients to load before searching
        waitForAngular();
        driver.findElement( By.cssSelector( "input[type=radio][id=" + username + "]" ) ).click();
        waitForAngular();
    }

    /**
     * Makes sure the patient has an obstetrics record
     *
     * @param name
     *            of patient to check
     */
    @Given ( "^(.+) already has an obstetrics record$" )
    public void checkRecordExisits ( final String name ) {
        final List<ObstetricsRecord> exist = ObstetricsRecord.getByPatient( name );
        if ( exist.size() == 0 ) {
            final ObstetricsRecordForm obsform = new ObstetricsRecordForm();
            obsform.setLastMenstrualPeriod( "2019-01-15" );
            final ObstetricsRecord obr = new ObstetricsRecord( obsform );
            obr.setPatient( name );
            obr.save();
        }
        waitForAngular();
    }

    /**
     * When HCP input info of first newborn and or second newborn
     *
     * @param laborDate
     *            of date
     * @param laborTime
     *            of time
     * @param deliveryDate
     *            of date
     * @param deliveryTime
     *            of twin time
     * @param laborDateTwin
     *            of twin date
     * @param laborTimeTwin
     *            of twin time
     * @param deliveryDateTwin
     *            of twin date
     * @param deliveryTimeTwin
     *            of twin time
     * @param type
     *            of delivery method
     * @param twin
     *            or not
     * @param first
     *            name
     * @param last
     *            name
     * @param weight
     *            of new born
     * @param height
     *            of new born
     * @param heartRate
     *            of heart rate
     * @param systolic
     *            of new born
     * @param diastolic
     *            of new born
     * @param twinFirst
     *            of second born first name
     * @param twinLast
     *            of second born last name
     * @param twinWeight
     *            of second born weight
     * @param twinHeight
     *            of second born height
     * @param twinHR
     *            of heart rate of second born
     * @param twinSystolic
     *            of second born
     * @param twinDiastolic
     *            of second born
     * @param twinType
     *            of delivery method
     * @throws InterruptedException
     *             if the time thread did not work
     */
    @Then ( "^he submits (.+), (.+), (.+), (.+), (.+), (.+), (.+), (.+), (.+), (.+), (.+), (.+), (\\d+), (\\d+), (\\d+), (\\d+), (\\d+), (.+), (.+), (\\d+), (\\d+), (\\d+), (\\d+), (\\d+), (.+)$" )
    public void addvalidLd ( final String laborDate, final String laborTime, final String deliveryDate,
            final String deliveryTime, final String laborDateTwin, final String laborTimeTwin,
            final String deliveryDateTwin, final String deliveryTimeTwin, final String type, final String twin,
            final String first, final String last, final int weight, final int height, final int heartRate,
            final int systolic, final int diastolic, final String twinFirst, final String twinLast,
            final int twinWeight, final int twinHeight, final int twinHR, final int twinSystolic,
            final int twinDiastolic, final String twinType ) throws InterruptedException {

        Thread.sleep( 1000 );
        // Click Create button
        driver.findElement( By.id( "displayCreateForm" ) ).click();
        waitForAngular();

        // Select twin or not first
        driver.findElement( By.cssSelector( "input[type=radio][name=" + twin + "]" ) ).click();

        waitForAngular();
        Thread.sleep( 1000 );

        // if twin selection if no
        if ( twin.equals( "No" ) ) {
            // Then choose the type of delivery method
            driver.findElement( By.name( type ) ).click();

            waitForAngular();
            Thread.sleep( 1000 );

            // fill L&D date and time all at once
            fillLaborAndDeliveryDateTime( laborDate, laborTime, deliveryDate, deliveryTime, "N" );

            // Information of first new born
            if ( !type.equals( "Miscarriage" ) ) {

                driver.findElement( By.name( "firstname" ) ).clear();

                driver.findElement( By.name( "firstname" ) ).sendKeys( first );

                driver.findElement( By.name( "lastname" ) ).clear();

                driver.findElement( By.name( "lastname" ) ).sendKeys( last );

                driver.findElement( By.name( "weight" ) ).clear();

                driver.findElement( By.name( "weight" ) ).sendKeys( Integer.toString( weight ) );

                driver.findElement( By.name( "length" ) ).clear();

                driver.findElement( By.name( "length" ) ).sendKeys( Integer.toString( height ) );

                driver.findElement( By.name( "heartrate" ) ).clear();

                driver.findElement( By.name( "heartrate" ) ).sendKeys( Integer.toString( heartRate ) );

                driver.findElement( By.name( "systolic" ) ).clear();

                driver.findElement( By.name( "systolic" ) ).sendKeys( Integer.toString( systolic ) );

                driver.findElement( By.name( "diastolic" ) ).clear();

                driver.findElement( By.name( "diastolic" ) ).sendKeys( Integer.toString( diastolic ) );

            }
            else {
                // Still need to fill out info for first new born
                fillLaborAndDeliveryDateTime( laborDate, laborTime, deliveryDate, deliveryTime, "N" );
            }

        }
        // If selection is twin
        else if ( twin.equals( "Yes" ) ) {

            driver.findElement( By.name( type ) ).click();

            waitForAngular();
            Thread.sleep( 1000 );

            // fill L&D date and time all at once
            fillLaborAndDeliveryDateTime( laborDate, laborTime, deliveryDate, deliveryTime, "N" );

            // Information of first new born
            if ( !type.equals( "Miscarriage" ) ) {

                driver.findElement( By.name( "firstname" ) ).clear();

                driver.findElement( By.name( "firstname" ) ).sendKeys( first );

                driver.findElement( By.name( "lastname" ) ).clear();

                driver.findElement( By.name( "lastname" ) ).sendKeys( last );

                driver.findElement( By.name( "weight" ) ).clear();

                driver.findElement( By.name( "weight" ) ).sendKeys( Integer.toString( weight ) );

                driver.findElement( By.name( "length" ) ).clear();

                driver.findElement( By.name( "length" ) ).sendKeys( Integer.toString( height ) );

                driver.findElement( By.name( "heartrate" ) ).clear();

                driver.findElement( By.name( "heartrate" ) ).sendKeys( Integer.toString( heartRate ) );

                driver.findElement( By.name( "systolic" ) ).clear();

                driver.findElement( By.name( "systolic" ) ).sendKeys( Integer.toString( systolic ) );

                driver.findElement( By.name( "diastolic" ) ).clear();

                driver.findElement( By.name( "diastolic" ) ).sendKeys( Integer.toString( diastolic ) );

            }
            else {
                // Still need to fill out info for first new born
                fillLaborAndDeliveryDateTime( laborDate, laborTime, deliveryDate, deliveryTime, "N" );
            }

            waitForAngular();
            Thread.sleep( 1000 );

            // Then choose the type of delivery method for twin
            driver.findElement( By.name( twinType + " " ) ).click();

            if ( !twinType.equals( "Miscarriage" ) ) {
                Thread.sleep( 1000 );
                // Date and time for twin
                fillLaborAndDeliveryDateTime( laborDateTwin, laborTimeTwin, deliveryDateTwin, deliveryTimeTwin, "Y" );

                Thread.sleep( 1000 );

                driver.findElement( By.name( "firstnameTwin" ) ).clear();

                driver.findElement( By.name( "firstnameTwin" ) ).sendKeys( twinFirst );

                driver.findElement( By.name( "lastnameTwin" ) ).clear();

                driver.findElement( By.name( "lastnameTwin" ) ).sendKeys( twinLast );

                driver.findElement( By.name( "weightTwin" ) ).clear();

                driver.findElement( By.name( "weightTwin" ) ).sendKeys( Integer.toString( twinWeight ) );

                driver.findElement( By.name( "lengthTwin" ) ).clear();

                driver.findElement( By.name( "lengthTwin" ) ).sendKeys( Integer.toString( twinHeight ) );

                driver.findElement( By.name( "heartrateTwin" ) ).clear();

                driver.findElement( By.name( "heartrateTwin" ) ).sendKeys( Integer.toString( twinHR ) );

                driver.findElement( By.name( "systolicTwin" ) ).clear();

                driver.findElement( By.name( "systolicTwin" ) ).sendKeys( Integer.toString( twinSystolic ) );

                driver.findElement( By.name( "diastolicTwin" ) ).clear();

                driver.findElement( By.name( "diastolicTwin" ) ).sendKeys( Integer.toString( twinDiastolic ) );

            }
            else {
                // still need to fill out date and time

                fillLaborAndDeliveryDateTime( laborDateTwin, laborTimeTwin, deliveryDateTwin, deliveryTimeTwin, "Y" );

            }

        }

        Thread.sleep( 1000 );

        driver.findElement( By.id( "submitLD" ) ).click();
    }

    /**
     * This function helps checking log history of user behavior
     *
     * @param transaction
     *            of type
     * @throws InterruptedException
     */
    @And ( "^Verify log history (\\d+)$" )
    public void checkTransactionHistory ( final int transaction ) throws InterruptedException {
        Thread.sleep( 1500 );

        driver.get( baseUrl + "/hcp/index" );

        Thread.sleep( 1500 );
        waitForAngular();
        switch ( transaction ) {
            case 2601:
                assertTextPresent( TransactionType.HCP_VIEWS_LABOR_DELIVERY_REPORTS.getDescription() );
                break;
            case 2602:
                assertTextPresent( TransactionType.OBGYN_CREATES_LABOR_DELIVERY_REPORT.getDescription() );
                break;
            case 2603:
                assertTextPresent( TransactionType.OBGYN_EDITS_LABOR_DELIVERY_REPORT.getDescription() );
                break;
            default:
                System.err.println( "Invalid Transaction Code." );
                break;
        }
    }

    /**
     * When HCP enters invalid info of first newborn and or second newborn
     *
     * @param laborDate
     *            of date
     * @param laborTime
     *            of time
     * @param deliveryDate
     *            of date
     * @param deliveryTime
     *            of time
     * @param deliveryTimeTwin
     *            of twin time
     * @param laborDateTwin
     *            of twin date
     * @param laborTimeTwin
     *            of twin time
     * @param deliveryDateTwin
     *            of twin date
     * @param type
     *            of delivery method
     * @param twin
     *            or not
     * @param first
     *            name
     * @param last
     *            name
     * @param weight
     *            of new born
     * @param height
     *            of new born
     * @param heartRate
     *            of heart rate
     * @param systolic
     *            of new born
     * @param diastolic
     *            of new born
     * @param twinFirst
     *            of second born first name
     * @param twinLast
     *            of second born last name
     * @param twinWeight
     *            of second born weight
     * @param twinHeight
     *            of second born height
     * @param twinHR
     *            of heart rate of second born
     * @param twinSystolic
     *            of second born
     * @param twinDiastolic
     *            of second born
     * @param twinType
     *            of delivery method
     * @throws InterruptedException
     *             if the time thread did not work
     */
    @And ( "^he incorrectly enters invalid values (.+), (.+), (.+), (.+), (.+), (.+), (.+), (.+), (.+), (.+), (.+), (.+), (\\d+), (\\d+), (\\d+), (\\d+), (\\d+), (.+), (.+), (\\d+), (\\d+), (\\d+), (\\d+), (\\d+), (.+)$" )
    public void entersInvalidLd ( final String laborDate, final String laborTime, final String deliveryDate,
            final String deliveryTime, final String laborDateTwin, final String laborTimeTwin,
            final String deliveryDateTwin, final String deliveryTimeTwin, final String type, final String twin,
            final String first, final String last, final int weight, final int height, final int heartRate,
            final int systolic, final int diastolic, final String twinFirst, final String twinLast,
            final int twinWeight, final int twinHeight, final int twinHR, final int twinSystolic,
            final int twinDiastolic, final String twinType ) throws InterruptedException {

        Thread.sleep( 1000 );
        // Click Create button
        driver.findElement( By.id( "displayCreateForm" ) ).click();
        waitForAngular();

        // Select twin or not first
        driver.findElement( By.cssSelector( "input[type=radio][name=" + twin + "]" ) ).click();

        waitForAngular();
        Thread.sleep( 1000 );

        // if twin selection if no
        if ( twin.equals( "No" ) ) {
            // Then choose the type of delivery method
            driver.findElement( By.name( type ) ).click();

            waitForAngular();
            Thread.sleep( 1000 );

            // fill L&D date and time all at once
            fillLaborAndDeliveryDateTime( laborDate, laborTime, deliveryDate, deliveryTime, "N" );

            // Information of first new born
            if ( !type.equals( "Miscarriage" ) ) {

                driver.findElement( By.name( "firstname" ) ).clear();

                driver.findElement( By.name( "firstname" ) ).sendKeys( first );

                driver.findElement( By.name( "lastname" ) ).clear();

                driver.findElement( By.name( "lastname" ) ).sendKeys( last );

                driver.findElement( By.name( "weight" ) ).clear();

                driver.findElement( By.name( "weight" ) ).sendKeys( Integer.toString( weight ) );

                driver.findElement( By.name( "length" ) ).clear();

                driver.findElement( By.name( "length" ) ).sendKeys( Integer.toString( height ) );

                driver.findElement( By.name( "heartrate" ) ).clear();

                driver.findElement( By.name( "heartrate" ) ).sendKeys( Integer.toString( heartRate ) );

                driver.findElement( By.name( "systolic" ) ).clear();

                driver.findElement( By.name( "systolic" ) ).sendKeys( Integer.toString( systolic ) );

                driver.findElement( By.name( "diastolic" ) ).clear();

                driver.findElement( By.name( "diastolic" ) ).sendKeys( Integer.toString( diastolic ) );

            }
            else {
                // Still need to fill out info for first new born
                fillLaborAndDeliveryDateTime( laborDate, laborTime, deliveryDate, deliveryTime, "N" );
            }

        }
        // If selection is twin
        else if ( twin.equals( "Yes" ) ) {

            driver.findElement( By.name( type ) ).click();

            waitForAngular();
            Thread.sleep( 1000 );

            // fill L&D date and time all at once
            fillLaborAndDeliveryDateTime( laborDate, laborTime, deliveryDate, deliveryTime, "N" );

            // Information of first new born
            if ( !type.equals( "Miscarriage" ) ) {

                driver.findElement( By.name( "firstname" ) ).clear();

                driver.findElement( By.name( "firstname" ) ).sendKeys( first );

                driver.findElement( By.name( "lastname" ) ).clear();

                driver.findElement( By.name( "lastname" ) ).sendKeys( last );

                driver.findElement( By.name( "weight" ) ).clear();

                driver.findElement( By.name( "weight" ) ).sendKeys( Integer.toString( weight ) );

                driver.findElement( By.name( "length" ) ).clear();

                driver.findElement( By.name( "length" ) ).sendKeys( Integer.toString( height ) );

                driver.findElement( By.name( "heartrate" ) ).clear();

                driver.findElement( By.name( "heartrate" ) ).sendKeys( Integer.toString( heartRate ) );

                driver.findElement( By.name( "systolic" ) ).clear();

                driver.findElement( By.name( "systolic" ) ).sendKeys( Integer.toString( systolic ) );

                driver.findElement( By.name( "diastolic" ) ).clear();

                driver.findElement( By.name( "diastolic" ) ).sendKeys( Integer.toString( diastolic ) );

            }
            else {
                // Still need to fill out info for first new born
                fillLaborAndDeliveryDateTime( laborDate, laborTime, deliveryDate, deliveryTime, "N" );
            }

            // Then choose the type of delivery method for twin
            driver.findElement( By.name( twinType + " " ) ).click();

            waitForAngular();
            Thread.sleep( 1000 );

            if ( !twinType.equals( "Miscarriage" ) ) {

                // Date and time for twin
                fillLaborAndDeliveryDateTime( laborDateTwin, laborTimeTwin, deliveryDateTwin, deliveryTimeTwin, "Y" );

                driver.findElement( By.name( "firstnameTwin" ) ).clear();

                driver.findElement( By.name( "firstnameTwin" ) ).sendKeys( twinFirst );

                driver.findElement( By.name( "lastnameTwin" ) ).clear();

                driver.findElement( By.name( "lastnameTwin" ) ).sendKeys( twinLast );

                driver.findElement( By.name( "weightTwin" ) ).clear();

                driver.findElement( By.name( "weightTwin" ) ).sendKeys( Integer.toString( twinWeight ) );

                driver.findElement( By.name( "lengthTwin" ) ).clear();

                driver.findElement( By.name( "lengthTwin" ) ).sendKeys( Integer.toString( twinHeight ) );

                driver.findElement( By.name( "heartrateTwin" ) ).clear();

                driver.findElement( By.name( "heartrateTwin" ) ).sendKeys( Integer.toString( twinHR ) );

                driver.findElement( By.name( "systolicTwin" ) ).clear();

                driver.findElement( By.name( "systolicTwin" ) ).sendKeys( Integer.toString( twinSystolic ) );

                driver.findElement( By.name( "diastolicTwin" ) ).clear();

                driver.findElement( By.name( "diastolicTwin" ) ).sendKeys( Integer.toString( twinDiastolic ) );

            }
            else {
                // still need to fill out date and time
                fillLaborAndDeliveryDateTime( laborDateTwin, laborTimeTwin, deliveryDateTwin, deliveryTimeTwin, "Y" );
            }

        }

        Thread.sleep( 1000 );

        driver.findElement( By.id( "submitLD" ) ).click();
    }

    // *************************BEGIN VIEW*************************************
    /**
     * When HCP view labor and delivery report
     *
     * @param laborDate
     *            of date
     * @param laborTime
     *            of time
     * @param deliveryDate
     *            of date
     * @param deliveryTime
     *            of time
     * @param laborDateTwin
     *            of Twin date
     * @param laborTimeTwin
     *            of Twin time
     * @param deliveryDateTwin
     *            of Twin date
     * @param deliveryTimeTwin
     *            of Twin time
     * @param type
     *            of delivery method
     * @param twin
     *            or not
     * @param first
     *            name
     * @param last
     *            name
     * @param weight
     *            of new born
     * @param height
     *            of new born
     * @param heartRate
     *            of heart rate
     * @param systolic
     *            of new born
     * @param diastolic
     *            of new born
     * @param twinFirst
     *            of second born first name
     * @param twinLast
     *            of second born last name
     * @param twinWeight
     *            of second born weight
     * @param twinHeight
     *            of second born height
     * @param twinHR
     *            of heart rate of second born
     * @param twinSystolic
     *            of second born
     * @param twinDiastolic
     *            of second born
     * @param twinType
     *            of delivery method
     * @throws InterruptedException
     *             if the time thread did not work
     */
    @And ( "^The labor and delivery reports display (.+), (.+), (.+), (.+), (.+), (.+), (.+), (.+), (.+), (.+), (.+), (.+), (\\d+), (\\d+), (\\d+), (\\d+), (\\d+), (.+), (.+), (\\d+), (\\d+), (\\d+), (\\d+), (\\d+), (.+)$" )
    public void viewReports ( final String laborDate, final String laborTime, final String deliveryDate,
            final String deliveryTime, final String laborDateTwin, final String laborTimeTwin,
            final String deliveryDateTwin, final String deliveryTimeTwin, final String type, final String twin,
            final String first, final String last, final int weight, final int height, final int heartRate,
            final int systolic, final int diastolic, final String twinFirst, final String twinLast,
            final int twinWeight, final int twinHeight, final int twinHR, final int twinSystolic,
            final int twinDiastolic, final String twinType ) throws InterruptedException {

        waitForAngular();
        if ( !type.equals( "N/A" ) ) {
            assertTrue( driver.findElement( By.id( "deliverymethod-0" ) ).getText().contains( ( type ) ) );
            assertTrue( driver.findElement( By.id( "laborDate-0" ) ).getText().contains( ( laborDate ) ) );
            assertTrue( driver.findElement( By.id( "deliveryDate-0" ) ).getText().contains( ( deliveryDate ) ) );

            assertTrue( driver.findElement( By.id( "laborTime-0" ) ).getText().contains( ( laborTime ) ) );
            assertTrue( driver.findElement( By.id( "deliveryTime-0" ) ).getText().contains( ( deliveryTime ) ) );
        }

        if ( !type.equals( "Miscarriage" ) ) {
            driver.findElement( By.id( "firstname-0" ) ).getText().contains( first );
            driver.findElement( By.id( "lastname-0" ) ).getText().contains( last );
            driver.findElement( By.id( "weight-0" ) ).getText().contains( Integer.toString( weight ) );
            driver.findElement( By.id( "length-0" ) ).getText().contains( Integer.toString( height ) );

            driver.findElement( By.id( "heartRate-0" ) ).getText().contains( Integer.toString( heartRate ) );

            driver.findElement( By.id( "systolic-0" ) ).getText().contains( Integer.toString( systolic ) );

            driver.findElement( By.id( "diastolic-0" ) ).getText().contains( Integer.toString( diastolic ) );
        }

        if ( twin.equals( "Yes" ) ) {
            if ( !twinType.equals( "N/A" ) ) {
                driver.findElement( By.id( "deliverymethodTwin-0" ) ).getText().contains( ( twinType ) );
            }
            assertTrue( driver.findElement( By.id( "laborDateTwin-0" ) ).getText().contains( ( laborDateTwin ) ) );
            assertTrue(
                    driver.findElement( By.id( "deliveryDateTwin-0" ) ).getText().contains( ( deliveryDateTwin ) ) );

            assertTrue( driver.findElement( By.id( "laborTimeTwin-0" ) ).getText().contains( ( laborTimeTwin ) ) );
            assertTrue(
                    driver.findElement( By.id( "deliveryTimeTwin-0" ) ).getText().contains( ( deliveryTimeTwin ) ) );
            driver.findElement( By.id( "firstnameTwin-0" ) ).getText().contains( twinFirst );
            driver.findElement( By.id( "lastnameTwin-0" ) ).getText().contains( twinLast );
            driver.findElement( By.id( "weightTwin-0" ) ).getText().contains( Integer.toString( twinWeight ) );
            driver.findElement( By.id( "lengthTwin-0" ) ).getText().contains( Integer.toString( twinHeight ) );
            driver.findElement( By.id( "heartRateTwin-0" ) ).getText().contains( Integer.toString( twinHR ) );
            driver.findElement( By.id( "systolicTwin-0" ) ).getText().contains( Integer.toString( twinSystolic ) );
            driver.findElement( By.id( "diastolicTwin-0" ) ).getText().contains( Integer.toString( twinDiastolic ) );

        }
    }

    // *************************BEGIN EDIT LABOR AND DELIVERY
    // TESTS*************************************

    /**
     * When HCP edits the labor and delivery report
     *
     * @param laborDate
     *            of date
     *
     * @param deliveryDate
     *            of date
     * @param laborDateTwin
     *            of Twin date
     *
     * @param deliveryDateTwin
     *            of Twin date
     *
     * @param type
     *            of delivery method
     *
     * @param first
     *            name
     * @param last
     *            name
     * @param weight
     *            of new born
     * @param height
     *            of new born
     * @param heartRate
     *            of heart rate
     * @param systolic
     *            of new born
     * @param diastolic
     *            of new born
     * @param twinFirst
     *            of second born first name
     * @param twinLast
     *            of second born last name
     * @param twinWeight
     *            of second born weight
     * @param twinHeight
     *            of second born height
     * @param twinHR
     *            of heart rate of second born
     * @param twinSystolic
     *            of second born
     * @param twinDiastolic
     *            of second born
     * @param twinType
     *            of delivery method
     * @throws InterruptedException
     *             if the time thread did not work
     */
    @Then ( "^he edit pre-filled fields (.+), (.+), (.+), (.+), (.+), (.+), (.+), (\\d+), (\\d+), (\\d+), (\\d+), (\\d+), (.+), (.+), (\\d+), (\\d+), (\\d+), (\\d+), (\\d+), (.+)$" )
    public void editldr ( final String laborDate, final String deliveryDate, final String laborDateTwin,
            final String deliveryDateTwin, final String type, final String first, final String last, final int weight,
            final int height, final int heartRate, final int systolic, final int diastolic, final String twinFirst,
            final String twinLast, final int twinWeight, final int twinHeight, final int twinHR, final int twinSystolic,
            final int twinDiastolic, final String twinType ) throws InterruptedException {

        waitForAngular();
        Thread.sleep( 1000 );

        // We want to see if twin exists or not
        if ( driver.findElement( By.name( "editdeliveryMethodTwin" ) ).isDisplayed() && !twinType.equals( "N/A" ) ) {

            final Select dropdown = new Select( driver.findElement( By.name( "editdeliveryMethod" ) ) );
            dropdown.selectByVisibleText( type );

            Thread.sleep( 500 );

            // This will fill L&D date only
            fillInDate( "ldate", laborDate );
            fillInDate( "ddate", deliveryDate );

            Thread.sleep( 500 );
            if ( !type.equals( "Miscarriage" ) ) {
                // INFO ON first INFANT

                driver.findElement( By.name( "firstname" ) ).clear();

                driver.findElement( By.name( "firstname" ) ).sendKeys( first );

                driver.findElement( By.name( "lastname" ) ).clear();

                driver.findElement( By.name( "lastname" ) ).sendKeys( last );

                driver.findElement( By.name( "weight" ) ).clear();

                driver.findElement( By.name( "weight" ) ).sendKeys( Integer.toString( weight ) );

                driver.findElement( By.name( "length" ) ).clear();

                driver.findElement( By.name( "length" ) ).sendKeys( Integer.toString( height ) );

                driver.findElement( By.name( "heartrate" ) ).clear();

                driver.findElement( By.name( "heartrate" ) ).sendKeys( Integer.toString( heartRate ) );

                driver.findElement( By.name( "systolic" ) ).clear();

                driver.findElement( By.name( "systolic" ) ).sendKeys( Integer.toString( systolic ) );

                driver.findElement( By.name( "diastolic" ) ).clear();

                driver.findElement( By.name( "diastolic" ) ).sendKeys( Integer.toString( diastolic ) );
            }
            // Second newborn
            // Fill in date too

            final Select dropdownTwin = new Select( driver.findElement( By.name( "editdeliveryMethodTwin" ) ) );
            dropdownTwin.selectByVisibleText( twinType );

            fillInDate( "ldateTwin", laborDateTwin );
            fillInDate( "ddateTwin", deliveryDateTwin );

            if ( !twinType.equals( "Miscarriage" ) ) {

                Thread.sleep( 500 );

                driver.findElement( By.name( "firstnameTwin" ) ).clear();

                driver.findElement( By.name( "firstnameTwin" ) ).sendKeys( twinFirst );

                driver.findElement( By.name( "lastnameTwin" ) ).clear();

                driver.findElement( By.name( "lastnameTwin" ) ).sendKeys( twinLast );

                driver.findElement( By.name( "weightTwin" ) ).clear();

                driver.findElement( By.name( "weightTwin" ) ).sendKeys( Integer.toString( twinWeight ) );

                driver.findElement( By.name( "lengthTwin" ) ).clear();

                driver.findElement( By.name( "lengthTwin" ) ).sendKeys( Integer.toString( twinHeight ) );

                driver.findElement( By.name( "heartrateTwin" ) ).clear();

                driver.findElement( By.name( "heartrateTwin" ) ).sendKeys( Integer.toString( twinHR ) );

                driver.findElement( By.name( "systolicTwin" ) ).clear();

                driver.findElement( By.name( "systolicTwin" ) ).sendKeys( Integer.toString( twinSystolic ) );

                driver.findElement( By.name( "diastolicTwin" ) ).clear();

                driver.findElement( By.name( "diastolicTwin" ) ).sendKeys( Integer.toString( twinDiastolic ) );
            }

        }
        else {
            // If only has first new born
            final Select dropdown = new Select( driver.findElement( By.name( "editdeliveryMethod" ) ) );
            dropdown.selectByVisibleText( type );

            Thread.sleep( 500 );

            fillInDate( "ldate", laborDate );
            fillInDate( "ddate", deliveryDate );

            Thread.sleep( 500 );
            if ( !type.equals( "Miscarriage" ) ) {
                // INFO ON first INFANT

                driver.findElement( By.name( "firstname" ) ).clear();

                driver.findElement( By.name( "firstname" ) ).sendKeys( first );

                driver.findElement( By.name( "lastname" ) ).clear();

                driver.findElement( By.name( "lastname" ) ).sendKeys( last );

                driver.findElement( By.name( "weight" ) ).clear();

                driver.findElement( By.name( "weight" ) ).sendKeys( Integer.toString( weight ) );

                driver.findElement( By.name( "length" ) ).clear();

                driver.findElement( By.name( "length" ) ).sendKeys( Integer.toString( height ) );

                driver.findElement( By.name( "heartrate" ) ).clear();

                driver.findElement( By.name( "heartrate" ) ).sendKeys( Integer.toString( heartRate ) );

                driver.findElement( By.name( "systolic" ) ).clear();

                driver.findElement( By.name( "systolic" ) ).sendKeys( Integer.toString( systolic ) );

                driver.findElement( By.name( "diastolic" ) ).clear();

                driver.findElement( By.name( "diastolic" ) ).sendKeys( Integer.toString( diastolic ) );
            }

        }

        Thread.sleep( 500 );

        driver.findElement( By.id( "updateLD" ) ).click();

    }

    /**
     * When HCP inputs the invalid entry to the labor and delivery report
     *
     * @param laborDate
     *            of date
     *
     * @param deliveryDate
     *            of date
     *
     * @param laborDateTwin
     *            of Twin date
     *
     * @param deliveryDateTwin
     *            of Twin date
     * @param type
     *            of delivery method
     *
     * @param first
     *            name
     * @param last
     *            name
     * @param weight
     *            of new born
     * @param height
     *            of new born
     * @param heartRate
     *            of heart rate
     * @param systolic
     *            of new born
     * @param diastolic
     *            of new born
     * @param twinFirst
     *            of second born first name
     * @param twinLast
     *            of second born last name
     * @param twinWeight
     *            of second born weight
     * @param twinHeight
     *            of second born height
     * @param twinHR
     *            of heart rate of second born
     * @param twinSystolic
     *            of second born
     * @param twinDiastolic
     *            of second born
     * @param twinType
     *            of delivery method
     * @throws InterruptedException
     *             if the time thread did not work
     */
    @Then ( "^he put in invalid input in the fields (.+), (.+), (.+), (.+), (.+), (.+), (.+), (\\d+), (\\d+), (\\d+), (\\d+), (\\d+), (.+), (.+), (\\d+), (\\d+), (\\d+), (\\d+), (\\d+), (.+)$" )
    public void editInvalidldr ( final String laborDate, final String deliveryDate, final String laborDateTwin,
            final String deliveryDateTwin, final String type, final String first, final String last, final int weight,
            final int height, final int heartRate, final int systolic, final int diastolic, final String twinFirst,
            final String twinLast, final int twinWeight, final int twinHeight, final int twinHR, final int twinSystolic,
            final int twinDiastolic, final String twinType ) throws InterruptedException {

        waitForAngular();
        Thread.sleep( 500 );

        // Select delivery type
        final Select dropdown = new Select( driver.findElement( By.name( "editdeliveryMethod" ) ) );
        dropdown.selectByVisibleText( type );

        Thread.sleep( 500 );

        // This will fill L&D date only
        fillInDate( "ldate", laborDate );
        fillInDate( "ddate", deliveryDate );

        Thread.sleep( 500 );
        if ( !type.equals( "Miscarriage" ) ) {
            // INFO ON first INFANT

            driver.findElement( By.name( "firstname" ) ).clear();
            if ( !first.equals( "N/A" ) ) {
                driver.findElement( By.name( "firstname" ) ).sendKeys( first );
            }
            else {
                driver.findElement( By.name( "firstname" ) ).sendKeys( "" );
            }
            driver.findElement( By.name( "lastname" ) ).clear();
            if ( !last.equals( "N/A" ) ) {
                driver.findElement( By.name( "lastname" ) ).sendKeys( first );
            }
            else {
                driver.findElement( By.name( "lastname" ) ).sendKeys( "" );
            }

            driver.findElement( By.name( "weight" ) ).clear();

            driver.findElement( By.name( "weight" ) ).sendKeys( Integer.toString( weight ) );

            driver.findElement( By.name( "length" ) ).clear();

            driver.findElement( By.name( "length" ) ).sendKeys( Integer.toString( height ) );

            driver.findElement( By.name( "heartrate" ) ).clear();

            driver.findElement( By.name( "heartrate" ) ).sendKeys( Integer.toString( heartRate ) );

            driver.findElement( By.name( "systolic" ) ).clear();

            driver.findElement( By.name( "systolic" ) ).sendKeys( Integer.toString( systolic ) );

            driver.findElement( By.name( "diastolic" ) ).clear();

            driver.findElement( By.name( "diastolic" ) ).sendKeys( Integer.toString( diastolic ) );
        }
        // Second newborn

        if ( !twinType.equals( "N/A" ) ) {
            final Select dropdownTwin = new Select( driver.findElement( By.name( "editdeliveryMethodTwin" ) ) );
            dropdownTwin.selectByVisibleText( twinType );

            Thread.sleep( 500 );

            fillInDate( "ldateTwin", laborDateTwin );
            fillInDate( "ddateTwin", deliveryDateTwin );

            if ( !twinType.equals( "Miscarriage" ) ) {

                driver.findElement( By.name( "firstnameTwin" ) ).clear();
                if ( !first.equals( "N/A" ) ) {
                    driver.findElement( By.name( "firstnameTwin" ) ).sendKeys( first );
                }
                else {
                    driver.findElement( By.name( "firstnameTwin" ) ).sendKeys( "" );
                }
                driver.findElement( By.name( "lastnameTwin" ) ).clear();
                if ( !last.equals( "N/A" ) ) {
                    driver.findElement( By.name( "lastnameTwin" ) ).sendKeys( first );
                }
                else {
                    driver.findElement( By.name( "lastnameTwin" ) ).sendKeys( "" );
                }

                driver.findElement( By.name( "weightTwin" ) ).clear();

                driver.findElement( By.name( "weightTwin" ) ).sendKeys( Integer.toString( twinWeight ) );

                driver.findElement( By.name( "lengthTwin" ) ).clear();

                driver.findElement( By.name( "lengthTwin" ) ).sendKeys( Integer.toString( twinHeight ) );

                driver.findElement( By.name( "heartrateTwin" ) ).clear();

                driver.findElement( By.name( "heartrateTwin" ) ).sendKeys( Integer.toString( twinHR ) );

                driver.findElement( By.name( "systolicTwin" ) ).clear();

                driver.findElement( By.name( "systolicTwin" ) ).sendKeys( Integer.toString( twinSystolic ) );

                driver.findElement( By.name( "diastolicTwin" ) ).clear();

                driver.findElement( By.name( "diastolicTwin" ) ).sendKeys( Integer.toString( twinDiastolic ) );
            }
        }

        Thread.sleep( 500 );

        driver.findElement( By.id( "updateLD" ) ).click();

        waitForAngular();

        Thread.sleep( 500 );

        driver.findElement( By.id( "backLD" ) ).click();

    }

    /**
     * Fills in the date and time fields with the specified date and time.
     *
     * @param date
     *            The date to enter.
     * @param time
     *            The time to enter.
     * @throws InterruptedException
     */
    private void fillLaborAndDeliveryDateTime ( final String ldate, final String ltime, final String ddate,
            final String dtime, final String twinDateTime ) throws InterruptedException {
        if ( twinDateTime.equals( "N" ) ) {
            fillInDate( "ldate", ldate ); // labor date

            fillInTime( "ltime", ltime ); // labor time

            fillInDate( "ddate", ddate ); // delivery date

            fillInTime( "dtime", dtime ); // delivery time
        }

        if ( twinDateTime.equals( "Y" ) ) {
            fillInDate( "ldateTwin", ldate ); // labor date

            fillInTime( "ltimeTwin", ltime ); // labor time

            fillInDate( "ddateTwin", ddate ); // delivery date

            fillInTime( "dtimeTwin", dtime ); // delivery time
        }
    }

    /**
     * Fills in the date field with the specified date.
     *
     * @param date
     *            The date to enter.
     */
    private void fillInDate ( final String dateField, final String date ) {
        driver.findElement( By.id( dateField ) ).clear();
        final WebElement dateElement = driver.findElement( By.id( dateField ) );
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

        driver.findElement( By.id( timeField ) ).clear();
        final WebElement timeElement = driver.findElement( By.id( timeField ) );
        timeElement.sendKeys( time.replace( ":", "" ).replace( " ", "" ) );
    }

}
