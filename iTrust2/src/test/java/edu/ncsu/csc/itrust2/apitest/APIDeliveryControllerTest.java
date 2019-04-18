package edu.ncsu.csc.itrust2.apitest;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import edu.ncsu.csc.itrust2.forms.hcp.LaborDeliveryForm;
import edu.ncsu.csc.itrust2.forms.hcp_patient.PatientForm;
import edu.ncsu.csc.itrust2.models.enums.BloodType;
import edu.ncsu.csc.itrust2.models.enums.DeliveryMethod;
import edu.ncsu.csc.itrust2.models.enums.Ethnicity;
import edu.ncsu.csc.itrust2.models.enums.Gender;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.enums.State;
import edu.ncsu.csc.itrust2.models.persistent.LaborDeliveryReport;
import edu.ncsu.csc.itrust2.models.persistent.Patient;
import edu.ncsu.csc.itrust2.mvc.config.WebMvcConfiguration;

/**
 * Testing for api for labor and delivery reports
 *
 * @author msned
 *
 */
@RunWith ( SpringJUnit4ClassRunner.class )
@ContextConfiguration ( classes = { RootConfiguration.class, WebMvcConfiguration.class } )
@WebAppConfiguration
public class APIDeliveryControllerTest {
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
     * Tests the api for retrieving a labor and delivery report
     *
     * @throws Exception
     */
    @Test
    @WithMockUser ( username = "OGBYN", roles = { "OBGYN" } )
    public void testGettingLaborDeliveryReports () throws Exception {
        mvc.perform( get( "/api/v1/LaborDelivery/-1" ) ).andExpect( status().isNotFound() );

        /*
         * Create a HCP and a Patient to use. If they already exist, this will
         * do nothing
         */
        final UserForm hcp = new UserForm( "hcp", "123456", Role.ROLE_HCP, 1 );
        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( hcp ) ) );

        final UserForm patientUser = new UserForm( "patient", "123456", Role.ROLE_PATIENT, 1 );
        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( patientUser ) ) );

        final PatientForm patient = new PatientForm();
        patient.setAddress1( "1 Test Street" );
        patient.setAddress2( "Some Location" );
        patient.setBloodType( BloodType.APos.toString() );
        patient.setCity( "FFF" );
        patient.setDateOfBirth( "1977-06-15" );
        patient.setEmail( "antti@itrust.fi" );
        patient.setEthnicity( Ethnicity.Caucasian.toString() );
        patient.setFirstName( "Test" );
        patient.setGender( Gender.Male.toString() );
        patient.setLastName( "Patient" );
        patient.setPhone( "123-456-7890" );
        patient.setSelf( "patient" );
        patient.setState( State.NC.toString() );
        patient.setZip( "27514" );
        mvc.perform( post( "/api/v1/patients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( patient ) ) );

        final LaborDeliveryForm form = new LaborDeliveryForm();
        form.setDeliveryDate( "2048-04-16T09:50:00.000-04:00" );
        form.setDeliverymethod( DeliveryMethod.CaesareanSection );
        form.setLaborDate( "2048-04-16T09:50:00.000-04:00" );
        form.setPatient( "patient" );
        form.setFirstname( "Henry" );
        form.setFirstnameTwin( "Twin" );
        form.setLastname( "Smith" );
        form.setLastnameTwin( "Smith" );

        mvc.perform( post( "/api/v1/LaborDelivery" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( form ) ) ).andExpect( status().isOk() );

        mvc.perform( get( "/api/v1/LaborDelivery" ) ).andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) );

        mvc.perform( get( "/api/v1/LaborDelivery/patients/patient" ) ).andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) );

        final List<LaborDeliveryReport> reports = LaborDeliveryReport.getAllReports();

        mvc.perform( get( "/api/v1/LaborDelivery/" + reports.get( 0 ).getId() ) ).andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) );
    }

    /**
     * Tests editing a labor and delivery report
     */
    @Test
    @WithMockUser ( username = "OGBYN", roles = { "OBGYN" } )
    public void testEditingLaborAndDeliveryReport () throws Exception {
        /*
         * Create a HCP and a Patient to use. If they already exist, this will
         * do nothing
         */
        final UserForm hcp = new UserForm( "hcp", "123456", Role.ROLE_HCP, 1 );
        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( hcp ) ) );

        final UserForm patient = new UserForm( "patient", "123456", Role.ROLE_PATIENT, 1 );
        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( patient ) ) );

        final LaborDeliveryForm form = new LaborDeliveryForm();
        form.setDeliveryDate( "2048-04-16T09:50:00.000-04:00" );
        form.setDeliverymethod( DeliveryMethod.CaesareanSection );
        form.setLaborDate( "2048-04-16T09:50:00.000-04:00" );
        form.setFirstname( "Henry" );

        mvc.perform( post( "/api/v1/LaborDelivery" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( form ) ) ).andExpect( status().isOk() );

        form.setDiastolic( 100 );
        form.setFirstname( "Joe" );

        List<LaborDeliveryReport> reports = LaborDeliveryReport.getAllReports();

        mvc.perform( put( "/api/v1/LaborDelivery/" + reports.get( 0 ).getId() )
                .contentType( MediaType.APPLICATION_JSON ).content( TestUtils.asJsonString( form ) ) )
                .andExpect( status().isOk() );

        reports = LaborDeliveryReport.getAllReports();

        assertEquals( "Joe", reports.get( 0 ).getFirstname() );
    }
}
