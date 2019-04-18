package edu.ncsu.csc.itrust2.apitest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.itrust2.config.RootConfiguration;
import edu.ncsu.csc.itrust2.forms.admin.UserForm;
import edu.ncsu.csc.itrust2.forms.hcp.GeneralCheckupForm;
import edu.ncsu.csc.itrust2.forms.hcp.ObstetricsRecordForm;
import edu.ncsu.csc.itrust2.forms.hcp.ObstetricsVisitForm;
import edu.ncsu.csc.itrust2.forms.hcp.PregnancyForm;
import edu.ncsu.csc.itrust2.forms.hcp_patient.PatientForm;
import edu.ncsu.csc.itrust2.models.enums.AppointmentType;
import edu.ncsu.csc.itrust2.models.enums.BloodType;
import edu.ncsu.csc.itrust2.models.enums.DeliveryMethod;
import edu.ncsu.csc.itrust2.models.enums.Ethnicity;
import edu.ncsu.csc.itrust2.models.enums.Gender;
import edu.ncsu.csc.itrust2.models.enums.HouseholdSmokingStatus;
import edu.ncsu.csc.itrust2.models.enums.PatientSmokingStatus;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.enums.State;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.models.persistent.Hospital;
import edu.ncsu.csc.itrust2.models.persistent.LogEntry;
import edu.ncsu.csc.itrust2.models.persistent.ObstetricsOfficeVisit;
import edu.ncsu.csc.itrust2.models.persistent.ObstetricsRecord;
import edu.ncsu.csc.itrust2.models.persistent.OfficeVisit;
import edu.ncsu.csc.itrust2.models.persistent.Patient;
import edu.ncsu.csc.itrust2.models.persistent.Pregnancy;
import edu.ncsu.csc.itrust2.mvc.config.WebMvcConfiguration;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

/**
 * Test for the API functionality for interacting with ObstetricsRecords AND
 * Pregnancies
 *
 * @author Ben Johnson (bfjohns4)
 */
@RunWith ( SpringJUnit4ClassRunner.class )
@ContextConfiguration ( classes = { RootConfiguration.class, WebMvcConfiguration.class } )
@WebAppConfiguration
public class APIObstetricsRecordTest {

    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    /**
     * Sets up test
     */
    @Before
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();

        final Patient p = Patient.getByName( "patient" );
        if ( p != null ) {
            while ( !ObstetricsRecord.getByPatient( "patient" ).isEmpty() ) {
                ObstetricsRecord.getByPatient( "patient" ).get( 0 ).delete();
            }
            for ( final Pregnancy prg : Pregnancy.getByPatient( "patient" ) ) {
                prg.delete();
            }
            for ( final OfficeVisit ov : OfficeVisit.getForPatient( "patient" ) ) {
                ov.delete();
            }

            p.delete();
        }
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
        patient.setDateOfBirth( "1984-02-01" );
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
     * Ensure attempt to create obstetrics record or pregnancy as a generic HCP
     * is denied
     *
     */
    @Test
    @WithMockUser ( username = "hcp", roles = { "HCP" } )
    public void testInvalidHCPAccess () throws Exception {
        createPatient();

        // Create a form for a pregnancy that started January 15th, 2200
        final ObstetricsRecordForm obsForm = new ObstetricsRecordForm();
        obsForm.setLastMenstrualPeriod( "2019-01-15" );

        try {
            mvc.perform( post( "/api/v1/obstetricsrecord/patient" ).contentType( MediaType.APPLICATION_JSON )
                    .content( TestUtils.asJsonString( obsForm ) ) ).andExpect( status().isForbidden() );
            fail();
        }
        catch ( final Exception e ) {
            assertTrue( e.getCause() instanceof AccessDeniedException );
        }

        final PregnancyForm pForm = new PregnancyForm();
        pForm.setConceptionYear( 2016 );
        pForm.setNumWeeksPregnant( 35 );
        pForm.setNumHoursInLabor( 8 );
        pForm.setDeliveryMethod( DeliveryMethod.CaesareanSection );
        pForm.setIsTwins( false );

        try {
            mvc.perform( post( "/api/v1/pregnancy/patient" ).contentType( MediaType.APPLICATION_JSON )
                    .content( TestUtils.asJsonString( pForm ) ) ).andExpect( status().isForbidden() );
            fail();
        }
        catch ( final Exception e ) {
            assertTrue( e.getCause() instanceof AccessDeniedException );
        }

    }

    /**
     * Tests APIObstetricsRecord's createObstetricsRecord method with a valid
     * ObstetricsRecordForm
     */
    @Test
    @WithMockUser ( username = "hcp", roles = { "OBGYN" } )
    public void testObstetricsRecordValidInvalidForm () throws Exception {

        createPatient();

        // Create a form for a pregnancy that started January 15th, 2200
        final ObstetricsRecordForm obsForm = new ObstetricsRecordForm();
        obsForm.setLastMenstrualPeriod( "2200-01-15" );
        // Try to instantialize with this invalid form, expect error
        mvc.perform( post( "/api/v1/obstetricsrecord/patient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( obsForm ) ) ).andExpect( status().isBadRequest() );

        // Lets try again, but with a valid form
        obsForm.setLastMenstrualPeriod( "2019-01-15" );
        mvc.perform( post( "/api/v1/obstetricsrecord/patient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( obsForm ) ) ).andExpect( status().isOk() );

        final List<LogEntry> entries = LoggerUtil.getAllForUser( "hcp" );
        assertEquals( TransactionType.OBGYN_CREATE_OBS_RECORD, entries.get( entries.size() - 1 ).getLogCode() );

        // Now, delete it
        mvc.perform( delete( "/api/v1/obstetricsrecord/patient" ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );

        // Try to delete again, expect error
        mvc.perform( delete( "/api/v1/obstetricsrecord/patient" ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isNotFound() );

    }

    /**
     * Tests APIObstetricsRecordController's createPregnancy endpoint with valid
     * and invalid inputs
     *
     * @throws Exception
     */
    @Test
    @WithMockUser ( username = "hcp", roles = { "OBGYN" } )
    public void testPregnancyValidInvalidForm () throws Exception {

        createPatient();

        final PregnancyForm pForm = new PregnancyForm();
        pForm.setConceptionYear( 2016 );
        pForm.setNumWeeksPregnant( 35 );
        pForm.setNumHoursInLabor( 8 );
        pForm.setDeliveryMethod( DeliveryMethod.CaesareanSection );
        pForm.setIsTwins( false );

        mvc.perform( post( "/api/v1/pregnancy/patient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( pForm ) ) ).andExpect( status().isOk() );

        // BVA tests for several data points that are selective about what input
        // is acceptable

        final PregnancyForm invalidDate = pForm;
        invalidDate.setConceptionYear( 2200 );

        mvc.perform( post( "/api/v1/pregnancy/patient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( invalidDate ) ) ).andExpect( status().isBadRequest() );

        final PregnancyForm invalidWeeks = pForm;
        invalidWeeks.setNumWeeksPregnant( 0 );

        mvc.perform( post( "/api/v1/pregnancy/patient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( invalidWeeks ) ) ).andExpect( status().isBadRequest() );

        final PregnancyForm invalidHours = pForm;
        invalidDate.setConceptionYear( 0 );

        mvc.perform( post( "/api/v1/pregnancy/patient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( invalidHours ) ) ).andExpect( status().isBadRequest() );
    }

    /**
     * Tests APIObstetricsRecordController's endpoint that retrieves
     * ObstetricRecords of a patient for an HCP user
     *
     * @throws Exception
     */
    @Test
    @WithMockUser ( username = "hcp", roles = { "OBGYN" } )
    public void testObstetricsRecordGetHCP () throws Exception {

        createPatient();

        // Create a form for a pregnancy that started January 15th, 2019
        final ObstetricsRecordForm obsForm = new ObstetricsRecordForm();
        obsForm.setLastMenstrualPeriod( "2019-01-15" );

        mvc.perform( get( "/api/v1/obstetricsrecord/doesnotexist" ) ).andExpect( status().isNotFound() );

        mvc.perform( post( "/api/v1/obstetricsrecord/patient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( obsForm ) ) ).andExpect( status().isOk() );

        mvc.perform( get( "/api/v1/obstetricsrecord/patient" ) ).andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) );

        final List<LogEntry> entries = LoggerUtil.getAllForUser( "hcp" );
        assertEquals( TransactionType.HCP_VIEW_OBS_RECORD, entries.get( entries.size() - 1 ).getLogCode() );

        try {
            mvc.perform( get( "/api/v1/obstetricsrecord" ) ).andExpect( status().isForbidden() );
            fail();
        }
        catch ( final Exception e ) {
            assertTrue( e.getCause() instanceof AccessDeniedException );
        }

    }

    /**
     * Tests APIObstetricsRecordController's endpoint that retrieves
     * ObstetricRecords of logged in Patient user
     *
     * @throws Exception
     */
    @Test
    @WithMockUser ( username = "patient", roles = { "PATIENT" } )
    public void testObstetricsRecordGetPatient () throws Exception {
        createPatient();

        mvc.perform( get( "/api/v1/obstetricsrecord" ) ).andExpect( status().isOk() );
        // .andExpect( content().contentType(
        // MediaType.APPLICATION_JSON_UTF8_VALUE ) );

        final List<LogEntry> entries = LoggerUtil.getAllForUser( "patient" );
        assertEquals( TransactionType.PATIENT_VIEW_OBS_RECORD, entries.get( entries.size() - 1 ).getLogCode() );

        try {
            mvc.perform( get( "/api/v1/obstetricsrecord/patient" ) ).andExpect( status().isForbidden() );
            fail();
        }
        catch ( final Exception e ) {
            assertTrue( e.getCause() instanceof AccessDeniedException );
        }
    }

    /**
     * Tests APIObstetricsRecordController's endpoint that retrieves Pregnancies
     * of a female patient of logged in HCP
     *
     * @throws Exception
     */
    @Test
    @WithMockUser ( username = "hcp", roles = { "HCP" } )
    public void testPregnancyGetHCP () throws Exception {
        createPatient();

        mvc.perform( get( "/api/v1/pregnancy/doesnotexist" ) ).andExpect( status().isNotFound() );

        mvc.perform( get( "/api/v1/pregnancy/patient" ) ).andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) );

        try {
            mvc.perform( get( "/api/v1/pregnancy" ) ).andExpect( status().isForbidden() );
            fail();
        }
        catch ( final Exception e ) {
            assertTrue( e.getCause() instanceof AccessDeniedException );
        }
    }

    /**
     * Tests APIObstetricsRecordController's endpoint that retrieves previous
     * Pregnancies of logged in Patient user
     *
     * @throws Exception
     */
    @Test
    @WithMockUser ( username = "patient", roles = { "PATIENT" } )
    public void testPregnancyGetPatient () throws Exception {

        createPatient();

        mvc.perform( get( "/api/v1/pregnancy" ) ).andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) );

        try {
            mvc.perform( get( "/api/v1/pregnancy/patient" ) ).andExpect( status().isForbidden() );
            fail();
        }
        catch ( final Exception e ) {
            assertTrue( e.getCause() instanceof AccessDeniedException );
        }
    }

    /**
     * Tests for the special flags related to an ObstetricsRecord
     *
     * @throws Exception
     *
     */
    @Test
    @WithMockUser ( username = "hcp", roles = { "OBGYN", "ADMIN" } )
    public void testPregnancyFlags () throws Exception {

        // Initialize a patient, an hcp user, and a hospital
        createPatient();
        final UserForm hcp = new UserForm( "hcp", "123456", Role.ROLE_HCP, 1 );
        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( hcp ) ) );

        final Hospital hospital = new Hospital( "iTrust Test Hospital 2", "1 iTrust Test Street", "27607", "NC" );
        mvc.perform( post( "/api/v1/hospitals" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( hospital ) ) );

        // Create and record a prior pregnancy
        final PregnancyForm pForm = new PregnancyForm();
        pForm.setConceptionYear( 2016 );
        pForm.setNumWeeksPregnant( 35 );
        pForm.setNumHoursInLabor( 8 );
        pForm.setDeliveryMethod( DeliveryMethod.Miscarriage );
        pForm.setIsTwins( false );

        mvc.perform( post( "/api/v1/pregnancy/patient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( pForm ) ) ).andExpect( status().isOk() );

        // Prior general check-up office visit
        final GeneralCheckupForm visit = new GeneralCheckupForm();
        visit.setDate( "2018-11-19T04:50:00.000-05:00" ); // 11/19/2016 4:50 AM
        visit.setPatient( "patient" );
        visit.setHcp( "hcp" );
        visit.setDiastolic( 82 );
        visit.setHospital( "iTrust Test Hospital 2" );
        visit.setHdl( 70 );
        visit.setHeight( 69.1f );
        visit.setHouseSmokingStatus( HouseholdSmokingStatus.INDOOR );
        visit.setLdl( 30 );
        visit.setPatientSmokingStatus( PatientSmokingStatus.FORMER );
        visit.setSystolic( 102 );
        visit.setTri( 150 );
        visit.setWeight( 175.2f );

        mvc.perform( post( "/api/v1/generalcheckups" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( visit ) ) ).andExpect( status().isOk() );

        // Create a obstetrics record for pregnancy that started 01/15/2019
        final ObstetricsRecordForm obsForm = new ObstetricsRecordForm();
        obsForm.setLastMenstrualPeriod( "2019-02-02" );

        mvc.perform( post( "/api/v1/obstetricsrecord/patient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( obsForm ) ) ).andExpect( status().isOk() );

        ObstetricsRecord orec = ObstetricsRecord.getByPatient( "patient" ).get( 0 );

        // Check values of flags that can be set using basic user info
        assertTrue( orec.hasAdvancedMaternalAge() );
        assertFalse( orec.isRHNegative() );
        assertTrue( orec.hasMiscarriagePotential() );

        assertFalse( orec.hasHighBloodPressure() );
        assertFalse( orec.hasAbnormalFetalHeartRate() );

        // Now create + save an obstetrics office visit for the patient
        final ObstetricsVisitForm obForm = new ObstetricsVisitForm();
        obForm.setDate( "2019-02-02T09:50:00.000-04:00" ); // 2/2/2019 9:50 AM
        obForm.setHcp( "hcp" );
        obForm.setPatient( "patient" );
        obForm.setNotes( "Test office visit" );
        obForm.setType( AppointmentType.OBGYN_OFFICE_VISIT.toString() );
        obForm.setHospital( "iTrust Test Hospital 2" );
        obForm.setFundalHeight( 0.2 );
        obForm.setFetalHeartRate( 100 );
        obForm.setNumOfWeeksPreg( 3 );
        obForm.setTwins( false );
        obForm.setLowLyingPlacenta( false );
        obForm.setDiastolic( 92 );
        obForm.setSystolic( 105 );

        mvc.perform( post( "/api/v1/ObstetricsVisit" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( obForm ) ) ).andExpect( status().isOk() );

        assertTrue( !ObstetricsOfficeVisit.getForPatient( "patient" ).isEmpty() );

        orec = ObstetricsRecord.getByPatient( "patient" ).get( 0 );

        assertTrue( orec.hasHighBloodPressure() );
        assertTrue( orec.hasAbnormalFetalHeartRate() );
        System.out.println( orec.getRecommendedWeightGain() );

    }
}
