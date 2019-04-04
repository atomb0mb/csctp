
package edu.ncsu.csc.itrust2.apitest;

import static org.junit.Assert.assertEquals;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.itrust2.config.RootConfiguration;
import edu.ncsu.csc.itrust2.forms.admin.UserForm;
import edu.ncsu.csc.itrust2.forms.hcp.ObstetricsRecordForm;
import edu.ncsu.csc.itrust2.forms.hcp.PregnancyForm;
import edu.ncsu.csc.itrust2.forms.hcp_patient.PatientForm;
import edu.ncsu.csc.itrust2.models.enums.BloodType;
import edu.ncsu.csc.itrust2.models.enums.DeliveryMethod;
import edu.ncsu.csc.itrust2.models.enums.Ethnicity;
import edu.ncsu.csc.itrust2.models.enums.Gender;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.enums.State;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.models.persistent.LogEntry;
import edu.ncsu.csc.itrust2.models.persistent.Patient;
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

        mvc.perform( post( "/api/v1/obstetricsrecord/patient" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( obsForm ) ) ).andExpect( status().isOk() );

        mvc.perform( get( "/api/v1/obstetricsrecord/patient" ) ).andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) );

        final List<LogEntry> entries = LoggerUtil.getAllForUser( "hcp" );
        assertEquals( TransactionType.HCP_VIEW_OBS_RECORD, entries.get( entries.size() - 1 ).getLogCode() );

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

        mvc.perform( get( "/api/v1/obstetricsrecord" ) ).andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) );

        final List<LogEntry> entries = LoggerUtil.getAllForUser( "patient" );
        assertEquals( TransactionType.PATIENT_VIEW_OBS_RECORD, entries.get( entries.size() - 1 ).getLogCode() );

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

        mvc.perform( get( "/api/v1/pregnancy/patient" ) ).andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) );

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

    }
}
