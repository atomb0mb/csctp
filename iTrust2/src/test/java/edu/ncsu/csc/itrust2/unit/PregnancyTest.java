package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust2.forms.hcp.PregnancyForm;
import edu.ncsu.csc.itrust2.models.enums.DeliveryMethod;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.persistent.DomainObject;
import edu.ncsu.csc.itrust2.models.persistent.Pregnancy;
import edu.ncsu.csc.itrust2.models.persistent.User;

/**
 * Tests each of the methods for Pregnancy.java
 *
 * @author Ben Johnson (bfjohns4)
 *
 */
public class PregnancyTest {

    /** Patient to be used for testing */
    final User patient = new User( "patient", "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.",
            Role.ROLE_PATIENT, 1 );

    @Before
    public void setUp () {
        DomainObject.deleteAll( Pregnancy.class );
    }

    /**
     * Test the ability to store and retrieve Pregnancy objects from the
     * database
     *
     */
    @Test
    public void testPregnancy () {

        final Pregnancy pregnancy = new Pregnancy();
        pregnancy.setConceptionYear( 2016 );
        pregnancy.setNumWeeksPregnant( 38 );
        pregnancy.setNumHoursLabor( 6 );
        pregnancy.setDeliverMethod( DeliveryMethod.VaginalDelivery );
        pregnancy.setTwins( false );
        pregnancy.setPatient( "patient" );
        pregnancy.save();

        final Pregnancy copy = Pregnancy.getById( pregnancy.getId() );

        assertEquals( pregnancy.getConceptionYear(), copy.getConceptionYear() );
        assertEquals( pregnancy.getNumberOfWeeksPregnant(), copy.getNumberOfWeeksPregnant() );
        assertEquals( pregnancy.getNumberOfHoursInLabor(), copy.getNumberOfHoursInLabor() );
        assertEquals( pregnancy.getDeliveryMethod(), copy.getConceptionYear() );
        assertEquals( pregnancy.getConceptionYear(), copy.getConceptionYear() );
        assertEquals( pregnancy.getConceptionYear(), copy.getDeliveryMethod() );
        assertTrue( pregnancy.getPatient().equals( copy.getPatient() ) );
        assertTrue( pregnancy.getId() == copy.getId() );

    }

    /**
     * Test if a valid pregnancy object can be created using either constructor
     *
     */
    @Test
    public void testValidConstructor () {
        final Pregnancy pregnancy = new Pregnancy( 2016, 38, 10, DeliveryMethod.VaginalDelivery, false, "Yolanda" );
        assertEquals( (Integer) 2016, pregnancy.getConceptionYear() );
        assertEquals( (Integer) 38, pregnancy.getNumberOfWeeksPregnant() );
        assertEquals( (Integer) 10, pregnancy.getNumberOfHoursInLabor() );
        assertEquals( DeliveryMethod.VaginalDelivery, pregnancy.getDeliveryMethod() );
        assertFalse( pregnancy.getIsTwins() );
        assertTrue( pregnancy.getPatient().equals( "Yolanda" ) );

        final Pregnancy pregnancy2 = new Pregnancy( pregnancy );
        assertEquals( (Integer) 2016, pregnancy2.getConceptionYear() );
        assertEquals( (Integer) 38, pregnancy2.getNumberOfWeeksPregnant() );
        assertEquals( (Integer) 10, pregnancy2.getNumberOfHoursInLabor() );
        assertEquals( DeliveryMethod.VaginalDelivery, pregnancy2.getDeliveryMethod() );
        assertFalse( pregnancy2.getIsTwins() );
        assertTrue( pregnancy2.getPatient().equals( "Yolanda" ) );

        assertTrue( pregnancy.getId() == pregnancy2.getId() );

    }

    /**
     * Test Pregnancy object initialization using a PregnancyForm object
     *
     */
    @Test
    public void testPregnancyByForm () {
        final PregnancyForm pForm = new PregnancyForm();
        pForm.setConceptionYear( 2016 );
        pForm.setNumWeeksPregnant( 35 );
        pForm.setNumHoursInLabor( 9 );
        pForm.setDeliveryMethod( DeliveryMethod.CaesareanSection );
        pForm.setIsTwins( false );

        final Pregnancy pregnancy = new Pregnancy( pForm );
        assertEquals( (Integer) 2016, pregnancy.getConceptionYear() );
        assertEquals( (Integer) 35, pregnancy.getNumberOfWeeksPregnant() );
        assertEquals( (Integer) 9, pregnancy.getNumberOfHoursInLabor() );
        assertEquals( DeliveryMethod.CaesareanSection, pregnancy.getDeliveryMethod() );
        assertFalse( pregnancy.getIsTwins() );
    }

    /**
     * Test if pregnancy rejects invalid year input and returns correct error
     * statement
     *
     */
    @Test
    public void testConceptionYear () {

        try {
            final Pregnancy pregnancy2 = new Pregnancy( 3000, 38, 10, DeliveryMethod.VaginalDelivery, false,
                    "Yolanda" );
            fail();
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "Year must be on or before current year", e.getMessage() );
        }
    }

    /**
     * Test if pregnancy rejects invalid weeks pregnant input and returns
     * correct error statement
     *
     */
    @Test
    public void testWeeksPregnant () {
        try {
            final Pregnancy pregnancy2 = new Pregnancy( 2016, 0, 10, DeliveryMethod.VaginalDelivery, false, "Yolanda" );
            fail();
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "Number of weeks pregnant must be a positive, nonzero number", e.getMessage() );
        }
    }

    /**
     * Test if pregnancy rejects invalid hours in labor input and returns
     * correct error statement
     *
     */
    @Test
    public void testNumberOfHoursInLabor () {
        try {
            final Pregnancy pregnancy2 = new Pregnancy( 2016, 38, 0, DeliveryMethod.VaginalDelivery, false, "Yolanda" );
            fail();
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "Number of hours in labor must be a positive, nonzero number", e.getMessage() );
        }
    }

    /**
     * Test if pregnancy accepts all 3 possible delivery method inputs
     *
     */
    @Test
    public void testDeliveryMethod () {
        try {
            final Pregnancy pregnancy = new Pregnancy( 2016, 38, 10, DeliveryMethod.VaginalDelivery, false, "Yolanda" );
            final Pregnancy pregnancy2 = new Pregnancy( 2016, 38, 10, DeliveryMethod.CaesareanSection, false,
                    "Yolanda" );
            final Pregnancy pregnancy3 = new Pregnancy( 2016, 38, 10, DeliveryMethod.Miscarriage, false, "Yolanda" );
        }
        catch ( final IllegalArgumentException e ) {
            fail();
        }
    }

    /**
     * Test if pregnancy accepts both true and false for twins
     *
     */
    @Test
    public void testTwins () {
        try {
            final Pregnancy pregnancy = new Pregnancy( 2016, 38, 10, DeliveryMethod.VaginalDelivery, false, "Yolanda" );
            final Pregnancy pregnancy2 = new Pregnancy( 2016, 38, 10, DeliveryMethod.CaesareanSection, true,
                    "Yolanda" );
        }
        catch ( final IllegalArgumentException e ) {
            fail();
        }
    }

    /**
     * Test if the pregnancy class allows us to store a patient's username long
     */
    @Test
    public void testPatient () {
        final Pregnancy pregnancy = new Pregnancy();
        pregnancy.setPatient( "YolandaYolanda" );
        assertTrue( pregnancy.getPatient().equals( "YolandaYolanda" ) );
    }

}
