package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.itrust2.apitest.TestUtils;
import edu.ncsu.csc.itrust2.config.RootConfiguration;
import edu.ncsu.csc.itrust2.forms.admin.UserForm;
import edu.ncsu.csc.itrust2.forms.hcp.PregnancyForm;
import edu.ncsu.csc.itrust2.forms.hcp_patient.PatientForm;
import edu.ncsu.csc.itrust2.models.enums.BloodType;
import edu.ncsu.csc.itrust2.models.enums.DeliveryMethod;
import edu.ncsu.csc.itrust2.models.enums.Ethnicity;
import edu.ncsu.csc.itrust2.models.enums.Gender;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.enums.State;
import edu.ncsu.csc.itrust2.models.persistent.DomainObject;
import edu.ncsu.csc.itrust2.models.persistent.Pregnancy;
import edu.ncsu.csc.itrust2.models.persistent.PregnancyComparator;
import edu.ncsu.csc.itrust2.models.persistent.User;
import edu.ncsu.csc.itrust2.mvc.config.WebMvcConfiguration;

/**
 * Tests each of the methods for Pregnancy.java
 *
 * @author Ben Johnson (bfjohns4)
 *
 */
@RunWith ( SpringJUnit4ClassRunner.class )
@ContextConfiguration ( classes = { RootConfiguration.class, WebMvcConfiguration.class } )
@WebAppConfiguration
public class PregnancyTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc               mvc;

    /** Patient to be used for testing */
    final User                    patient = new User( "patient",
            "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.", Role.ROLE_PATIENT, 1 );

    @Before
    public void setUp () {
        DomainObject.deleteAll( Pregnancy.class );

        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
    }

    /**
     * Creates patient for the tests
     *
     * @throws Exception
     */
    private void createPatient () throws Exception {
        final UserForm uForm = new UserForm( "patient", "123456", Role.ROLE_PATIENT, 1 );
        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( uForm ) ) );

        final PatientForm patient = new PatientForm();
        patient.setAddress1( "1 Test Street" );
        patient.setAddress2( "Some Location" );
        patient.setBloodType( BloodType.APos.toString() );
        patient.setCity( "Viipuri" );
        patient.setDateOfBirth( "1977-06-15" );
        patient.setEmail( "patient@itrust.fi" );
        patient.setEthnicity( Ethnicity.Hispanic.toString() );
        patient.setFirstName( "Yolanda" );
        patient.setGender( Gender.Female.toString() );
        patient.setLastName( "Perdida" );
        patient.setPhone( "123-456-7890" );
        patient.setSelf( "patient" );
        patient.setState( State.NC.toString() );
        patient.setZip( "27514" );
        mvc.perform( post( "/api/v1/patients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( patient ) ) );
    }

    /**
     * Test the ability to store and retrieve Pregnancy objects from the
     * database
     *
     * @throws Exception
     *
     */
    @Test
    public void testPregnancy () throws Exception {

        createPatient();
        final Pregnancy pregnancy = new Pregnancy();
        pregnancy.setPatient( "patient" );
        pregnancy.setConceptionYear( 2016 );
        pregnancy.setNumWeeksPregnant( 38 );
        pregnancy.setNumHoursLabor( 6 );
        pregnancy.setDeliverMethod( DeliveryMethod.VaginalDelivery );
        pregnancy.setTwins( false );

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
     * @throws Exception
     *
     */
    @Test
    public void testPregnancyByForm () throws Exception {
        final PregnancyForm pForm = new PregnancyForm();
        pForm.setConceptionYear( 2016 );
        pForm.setNumWeeksPregnant( 35 );
        pForm.setNumHoursInLabor( 9 );
        pForm.setDeliveryMethod( DeliveryMethod.CaesareanSection );
        pForm.setIsTwins( false );

        createPatient();
        final Pregnancy pregnancy = new Pregnancy( "patient", pForm );
        assertEquals( (Integer) 2016, pregnancy.getConceptionYear() );
        assertEquals( (Integer) 35, pregnancy.getNumberOfWeeksPregnant() );
        assertEquals( (Integer) 9, pregnancy.getNumberOfHoursInLabor() );
        assertEquals( DeliveryMethod.CaesareanSection, pregnancy.getDeliveryMethod() );
        assertFalse( pregnancy.getIsTwins() );
    }

    /**
     * Tests for error message thrown when an invalid date is set
     * 
     * @throws Exception
     *
     */
    @Test
    public void testInvalidDate () throws Exception {
        createPatient();
        final Pregnancy pregnancy = new Pregnancy();
        pregnancy.setPatient( "patient" );

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

    /**
     * Tests Pregnancy's comparator class that is called in get
     *
     * @throws Exception
     */
    @Test
    public void testComparator () throws Exception {
        createPatient();
        final Pregnancy p1 = new Pregnancy();
        final Pregnancy p2 = new Pregnancy();
        final Pregnancy p3 = new Pregnancy();
        final Pregnancy p4 = new Pregnancy();
        final Pregnancy p5 = new Pregnancy();
        p1.setPatient( "patient" );
        p2.setPatient( "patient" );
        p3.setPatient( "patient" );
        p4.setPatient( "patient" );
        p5.setPatient( "patient" );

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
