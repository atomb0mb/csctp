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
import edu.ncsu.csc.itrust2.forms.hcp.ObstetricsRecordForm;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.models.persistent.ObstetricsRecord;

/**
 * This cucumber tests the Labor and delivery report of add, view, and edit
 * functionality. It includes valid and invalid input. As checks the correct
 * transaction code.
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
        else {
            ( (JavascriptExecutor) driver )
                    .executeScript( "document.getElementById('OBGYNLaborDeliveryReport').click();" );

            assertEquals( "iTrust2: Labor and Delivery Reports", driver.getTitle() );
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
     *            of time
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
    @Then ( "^he submits (.+), (.+), (.+), (.+), (.+), (.+), (.+), (.+), (\\d+), (\\d+), (\\d+), (\\d+), (\\d+), (.+), (.+), (\\d+), (\\d+), (\\d+), (\\d+), (\\d+), (.+)$" )
    public void addvalidLd ( final String laborDate, final String laborTime, final String deliveryDate,
            final String deliveryTime, final String type, final String twin, final String first, final String last,
            final int weight, final int height, final int heartRate, final int systolic, final int diastolic,
            final String twinFirst, final String twinLast, final int twinWeight, final int twinHeight, final int twinHR,
            final int twinSystolic, final int twinDiastolic, final String twinType ) throws InterruptedException {
        Thread.sleep( 1000 );
        driver.findElement( By.id( "displayCreateForm" ) ).click();
        waitForAngular();

        // This will fill L&D date and time all at once
        fillLaborAndDeliveryDateTime( laborDate, laborTime, deliveryDate, deliveryTime );

        // Select twin or not first
        driver.findElement( By.cssSelector( "input[type=radio][name=" + twin + "]" ) ).click();
        // Select delivery type

        waitForAngular();
        Thread.sleep( 1000 );
        driver.findElement( By.name( type ) ).click();

        waitForAngular();
        Thread.sleep( 1000 );

        // INFO ON first INFANT

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

        // Second newborn
        if ( twin.equals( "Yes" ) ) {

            driver.findElement( By.name( twinType ) ).click();

            waitForAngular();
            Thread.sleep( 2000 );

            if ( !twinType.equals( "Miscarriage*" ) ) {

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
        // if ( transaction == 2403 ) {
        // driver.get( baseUrl + "/patient/index" );
        // }
        // else {
        driver.get( baseUrl + "/hcp/index" );
        // }
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

    // *************************BEGIN INVALID CREATE
    // LD*************************************

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
    @And ( "^he incorrectly enters invalid values (.+), (.+), (.+), (.+), (.+), (.+), (.+), (.+), (\\d+), (\\d+), (\\d+), (\\d+), (\\d+), (.+), (.+), (\\d+), (\\d+), (\\d+), (\\d+), (\\d+), (.+)$" )
    public void addReportInvalid ( final String laborDate, final String laborTime, final String deliveryDate,
            final String deliveryTime, final String type, final String twin, final String first, final String last,
            final int weight, final int height, final int heartRate, final int systolic, final int diastolic,
            final String twinFirst, final String twinLast, final int twinWeight, final int twinHeight, final int twinHR,
            final int twinSystolic, final int twinDiastolic, final String twinType ) throws InterruptedException {
        Thread.sleep( 1000 );
        driver.findElement( By.id( "displayCreateForm" ) ).click();
        waitForAngular();

        // This will fill L&D date and time all at once
        fillLaborAndDeliveryDateTime( laborDate, laborTime, deliveryDate, deliveryTime );

        // Select twin or not first
        driver.findElement( By.cssSelector( "input[type=radio][name=" + twin + "]" ) ).click();
        // Select delivery type

        waitForAngular();
        Thread.sleep( 1500 );
        driver.findElement( By.name( type ) ).click();

        waitForAngular();
        Thread.sleep( 1500 );

        // INFO ON first INFANT

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

        // Second newborn
        if ( twin.equals( "Yes" ) ) {

            driver.findElement( By.name( twinType ) ).click();

            waitForAngular();
            Thread.sleep( 2000 );

            if ( !twinType.equals( "Miscarriage*" ) ) {

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
    @And ( "^The labor and delivery reports display (.+), (.+), (.+), (.+), (.+), (.+), (.+), (.+), (\\d+), (\\d+), (\\d+), (\\d+), (\\d+), (.+), (.+), (\\d+), (\\d+), (\\d+), (\\d+), (\\d+), (.+)$" )
    public void viewReports ( final String laborDate, final String laborTime, final String deliveryDate,
            final String deliveryTime, final String type, final String twin, final String first, final String last,
            final int weight, final int height, final int heartRate, final int systolic, final int diastolic,
            final String twinFirst, final String twinLast, final int twinWeight, final int twinHeight, final int twinHR,
            final int twinSystolic, final int twinDiastolic, final String twinType ) throws InterruptedException {

        waitForAngular();

        driver.findElement( By.id( "laborDate-0" ) ).getText().contains( ( laborDate ) );
        driver.findElement( By.id( "laborTime-0" ) ).getText().contains( ( laborTime ) );
        driver.findElement( By.id( "deliveryDate-0" ) ).getText().contains( ( deliveryDate ) );
        driver.findElement( By.id( "deliveryTime-0" ) ).getText().contains( ( deliveryTime ) );

        driver.findElement( By.id( "deliverymethod-0" ) ).getText().contains( ( type ) );

        if ( !type.equals( "Miscarriage" ) ) {
            assertTrue( driver.findElement( By.id( "firstname-0" ) ).getText().contains( first ) );
            assertTrue( driver.findElement( By.id( "lastname-0" ) ).getText().contains( last ) );
            assertTrue( driver.findElement( By.id( "weight-0" ) ).getText().contains( Integer.toString( weight ) ) );
            assertTrue( driver.findElement( By.id( "length-0" ) ).getText().contains( Integer.toString( height ) ) );
            assertTrue(
                    driver.findElement( By.id( "heartRate-0" ) ).getText().contains( Integer.toString( heartRate ) ) );
            assertTrue(
                    driver.findElement( By.id( "systolic-0" ) ).getText().contains( Integer.toString( systolic ) ) );
            assertTrue(
                    driver.findElement( By.id( "diastolic-0" ) ).getText().contains( Integer.toString( diastolic ) ) );
        }

        // if ( twin.equals( "Yes" ) ) {
        // assertTrue( driver.findElement( By.id( "deliverymethodTwin-0" )
        // ).getText().contains( ( twinType ) ) );
        // assertTrue( driver.findElement( By.id( "firstnameTwin-0" )
        // ).getText().contains( twinFirst ) );
        // assertTrue( driver.findElement( By.id( "lastnameTwin-0" )
        // ).getText().contains( twinLast ) );
        // assertTrue( driver.findElement( By.id( "weightTwin-0" ) ).getText()
        // .contains( Integer.toString( twinWeight ) ) );
        // assertTrue( driver.findElement( By.id( "lengthTwin-0" ) ).getText()
        // .contains( Integer.toString( twinHeight ) ) );
        // assertTrue(
        // driver.findElement( By.id( "heartRateTwin-0" ) ).getText().contains(
        // Integer.toString( twinHR ) ) );
        // assertTrue( driver.findElement( By.id( "systolicTwin-0" ) ).getText()
        // .contains( Integer.toString( twinSystolic ) ) );
        // assertTrue( driver.findElement( By.id( "DiastolicTwin-0" )
        // ).getText()
        // .contains( Integer.toString( twinDiastolic ) ) );
        //
        // }
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
    @Then ( "^he edit pre-filled fields (.+), (.+), (.+), (.+), (.+), (\\d+), (\\d+), (\\d+), (\\d+), (\\d+), (.+), (.+), (\\d+), (\\d+), (\\d+), (\\d+), (\\d+), (.+)$" )
    public void editldr ( final String laborDate, final String deliveryDate, final String type, final String first,
            final String last, final int weight, final int height, final int heartRate, final int systolic,
            final int diastolic, final String twinFirst, final String twinLast, final int twinWeight,
            final int twinHeight, final int twinHR, final int twinSystolic, final int twinDiastolic,
            final String twinType ) throws InterruptedException {

        waitForAngular();
        Thread.sleep( 5000 );

        // This will fill L&D date only
        fillInDate( "ldate", laborDate );
        fillInDate( "ddate", deliveryDate );

        // Select delivery type

        Thread.sleep( 500 );

        final Select dropdown = new Select( driver.findElement( By.name( "editdeliveryMethod" ) ) );
        dropdown.selectByVisibleText( type );

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

        if ( !twinType.equals( "N/A" ) && !twinType.equals( "Miscarriage*" ) ) {

            final Select dropdownTwin = new Select( driver.findElement( By.name( "editdeliveryMethodTwin" ) ) );
            dropdownTwin.selectByVisibleText( twinType );

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
    @Then ( "^he put in invalid input in the fields (.+), (.+), (.+), (.+), (.+), (\\d+), (\\d+), (\\d+), (\\d+), (\\d+), (.+), (.+), (\\d+), (\\d+), (\\d+), (\\d+), (\\d+), (.+)$" )
    public void editInvalidldr ( final String laborDate, final String deliveryDate, final String type,
            final String first, final String last, final int weight, final int height, final int heartRate,
            final int systolic, final int diastolic, final String twinFirst, final String twinLast,
            final int twinWeight, final int twinHeight, final int twinHR, final int twinSystolic,
            final int twinDiastolic, final String twinType ) throws InterruptedException {

        waitForAngular();
        Thread.sleep( 500 );

        // This will fill L&D date only

        fillInDate( "ldate", laborDate );
        fillInDate( "ddate", deliveryDate );

        // Select delivery type

        Thread.sleep( 500 );

        final Select dropdown = new Select( driver.findElement( By.name( "editdeliveryMethod" ) ) );
        dropdown.selectByVisibleText( type );

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

        if ( !twinType.equals( "N/A" ) ) {
            final Select dropdownTwin = new Select( driver.findElement( By.name( "editdeliveryMethodTwin" ) ) );
            dropdownTwin.selectByVisibleText( twinType );

            Thread.sleep( 500 );

            if ( !twinType.equals( "Miscarriage*" ) ) {

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
            final String dtime ) throws InterruptedException {
        fillInDate( "ldate", ldate ); // labor date

        fillInTime( "ltime", ltime ); // labor time

        fillInDate( "ddate", ddate ); // delivery date

        fillInTime( "dtime", dtime ); // delivery time
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
