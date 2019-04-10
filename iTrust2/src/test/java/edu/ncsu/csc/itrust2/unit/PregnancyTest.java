package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust2.forms.hcp.PregnancyForm;
import edu.ncsu.csc.itrust2.models.enums.DeliveryMethod;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.persistent.DomainObject;
import edu.ncsu.csc.itrust2.models.persistent.Pregnancy;
import edu.ncsu.csc.itrust2.models.persistent.PregnancyComparator;
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
        assertEquals( pregnancy.getDeliveryMethod(), copy.getDeliveryMethod() );
        assertEquals( pregnancy.getIsTwins(), copy.getIsTwins() );
        assertTrue( pregnancy.getPatient().equals( copy.getPatient() ) );

        final Pregnancy copy2 = Pregnancy.getByPatient( "patient" ).get( 0 );

        assertEquals( pregnancy.getConceptionYear(), copy2.getConceptionYear() );
        assertEquals( pregnancy.getNumberOfWeeksPregnant(), copy2.getNumberOfWeeksPregnant() );
        assertEquals( pregnancy.getNumberOfHoursInLabor(), copy2.getNumberOfHoursInLabor() );
        assertEquals( pregnancy.getDeliveryMethod(), copy2.getDeliveryMethod() );
        assertEquals( pregnancy.getIsTwins(), copy2.getIsTwins() );
        assertTrue( pregnancy.getPatient().equals( copy2.getPatient() ) );

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
     * Tests for error message thrown when an invalid date is set
     *
     */
    @Test
    public void testInvalidDate () {
        final Pregnancy pregnancy = new Pregnancy();

        try {
            pregnancy.setConceptionYear( 2200 );
        }
        catch ( final Exception e ) {
            assertEquals( "Year must be on or before current year", e.getMessage() );
        }
    }

    /**
     * Tests for error message thrown when an invalid date is set
     *
     */
    @Test
    public void testInvalidNumWeeksPregnant () {
        final Pregnancy pregnancy = new Pregnancy();

        try {
            pregnancy.setNumWeeksPregnant( 0 );
        }
        catch ( final Exception e ) {
            assertEquals( "Number of weeks pregnant must be a positive, nonzero number", e.getMessage() );
        }
    }

    /**
     * Tests for error message thrown when an invalid date is set
     *
     */
    @Test
    public void testInvalidNumHoursInLabor () {
        final Pregnancy pregnancy = new Pregnancy();

        try {
            pregnancy.setNumHoursLabor( 0 );
        }
        catch ( final Exception e ) {
            assertEquals( "Number of hours in labor must be a positive, nonzero number", e.getMessage() );
        }
    }

    /** Tests Pregnancy's comparator class that is called in get */
    @Test
    public void testComparator () {
        final Pregnancy p1 = new Pregnancy();
        final Pregnancy p2 = new Pregnancy();
        final Pregnancy p3 = new Pregnancy();
        final Pregnancy p4 = new Pregnancy();
        final Pregnancy p5 = new Pregnancy();
        p1.setConceptionYear( 2017 );
        p2.setConceptionYear( 2014 );
        p3.setConceptionYear( 2012 );
        p4.setConceptionYear( 2019 );
        p5.setConceptionYear( 2016 );

        final List<Pregnancy> pList = new ArrayList<Pregnancy>();

        pList.add( p1 );
        pList.add( p2 );
        pList.add( p3 );
        pList.add( p4 );
        pList.add( p5 );

        pList.sort( new PregnancyComparator() );

        assertEquals( (Integer) 2019, pList.get( 0 ).getConceptionYear() );
        assertEquals( (Integer) 2017, pList.get( 1 ).getConceptionYear() );
        assertEquals( (Integer) 2016, pList.get( 2 ).getConceptionYear() );
        assertEquals( (Integer) 2014, pList.get( 3 ).getConceptionYear() );
        assertEquals( (Integer) 2012, pList.get( 4 ).getConceptionYear() );

    }

}
