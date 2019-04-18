package edu.ncsu.csc.itrust2.controllers.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.itrust2.forms.hcp.LaborDeliveryForm;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.models.persistent.LaborDeliveryReport;
import edu.ncsu.csc.itrust2.models.persistent.Patient;
import edu.ncsu.csc.itrust2.models.persistent.User;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

/**
 * API class that contains endpoints for labor and delivery reports
 *
 * @author msned
 *
 */
@SuppressWarnings ( { "rawtypes", "unchecked" } )
@RestController
public class APIDeliveryController extends APIController {

    /**
     * Adds a new labor and delivery report
     *
     * @param form
     *            the form with the delivery values
     * @return a response entity based on the success of adding to the database
     */
    @PreAuthorize ( "hasRole('ROLE_OBGYN')" )
    @PostMapping ( BASE_PATH + "/LaborDelivery" )
    public ResponseEntity createLaborDeliveryReport ( @RequestBody final LaborDeliveryForm form ) {
        try {
            final LaborDeliveryReport report = new LaborDeliveryReport( form );

            if ( LaborDeliveryReport.getById( report.getId() ) != null ) {
                return new ResponseEntity(
                        errorResponse( "Delivery report with the id " + report.getId() + " already exists" ),
                        HttpStatus.CONFLICT );
            }

            report.save();
            LoggerUtil.log( TransactionType.OBGYN_CREATES_LABOR_DELIVERY_REPORT, LoggerUtil.currentUser(),
                    report.getPatient() );
            try {
                if ( report.getFirstname() != null ) {
                    final Patient first = new Patient();
                    first.setFirstName( report.getFirstname() );
                    final User babyuser = new User( report.getFirstname() + report.getLastname(),
                            "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.", Role.ROLE_PATIENT, 1 );
                    babyuser.save();
                    first.setSelf( babyuser );
                    first.setDateOfBirth( report.getDeliveryDate().toLocalDate() );
                    first.setMother( User.getByName( report.getPatient() ) );
                    first.save();
                    LoggerUtil.log( TransactionType.CREATE_USER, LoggerUtil.currentUser(),
                            report.getFirstname() + report.getLastname(), null );
                    LoggerUtil.log( TransactionType.CREATE_DEMOGRAPHICS, LoggerUtil.currentUser() );
                }
                if ( report.getFirstnameTwin() != null ) {
                    final Patient second = new Patient();
                    second.setFirstName( report.getFirstnameTwin() );
                    final User twin = new User( report.getFirstnameTwin() + report.getLastnameTwin(),
                            "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.", Role.ROLE_PATIENT, 1 );
                    twin.save();
                    second.setSelf( twin );
                    second.setDateOfBirth( report.getDeliveryDate().toLocalDate() );
                    second.setMother( User.getByName( report.getPatient() ) );
                    second.save();
                    LoggerUtil.log( TransactionType.CREATE_USER, LoggerUtil.currentUser(),
                            report.getFirstname() + report.getLastname(), null );
                    LoggerUtil.log( TransactionType.CREATE_DEMOGRAPHICS, LoggerUtil.currentUser() );
                }
            }
            catch ( final Exception e ) {
                System.out.println( e.getMessage() );
                return new ResponseEntity( errorResponse( "Could not create users because of " + e.getMessage() ),
                        HttpStatus.BAD_REQUEST );
            }

            return new ResponseEntity( report, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            e.printStackTrace();
            return new ResponseEntity(
                    errorResponse( "Could not validate or save the delivery report provided due to " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Returns the report with the given ID
     *
     * @param id
     *            the id of the report to return
     * @return response entity with report
     */
    @GetMapping ( BASE_PATH + "/LaborDelivery/{id}" )
    @PreAuthorize ( "hasAnyRole('ROLE_HCP', 'ROLE_OD', 'ROLE_OPH', 'ROLE_OBGYN')" )
    public ResponseEntity getLaborDeliveryReport ( @PathVariable final Long id ) {
        final LaborDeliveryReport report = LaborDeliveryReport.getById( id );
        if ( report == null ) {
            return new ResponseEntity( errorResponse( "No delivery report found for " + id ), HttpStatus.NOT_FOUND );
        }
        else {
            LoggerUtil.log( TransactionType.HCP_VIEWS_LABOR_DELIVERY_REPORTS, LoggerUtil.currentUser() );
            return new ResponseEntity( report, HttpStatus.OK );
        }
    }

    /**
     * Api returns all reports for a given patient
     *
     * @param username
     *            the user name of the patient
     * @return a list of all labor and delivery reports for that patient
     */
    @GetMapping ( BASE_PATH + "/LaborDelivery/patients/{username}" )
    @PreAuthorize ( "hasAnyRole('ROLE_HCP', 'ROLE_OD', 'ROLE_OPH', 'ROLE_OBGYN')" )
    public ResponseEntity getReportForPatient ( @PathVariable final String username ) {
        if ( null == Patient.getByName( username ) ) {
            return new ResponseEntity( errorResponse( "No patients found with username " + username ),
                    HttpStatus.NOT_FOUND );
        }
        LoggerUtil.log( TransactionType.HCP_VIEWS_LABOR_DELIVERY_REPORTS, User.getByName( LoggerUtil.currentUser() ),
                User.getByName( username ) );
        return new ResponseEntity( LaborDeliveryReport.getByPatient( username ), HttpStatus.OK );
    }

    /**
     * Edits an existing labor and delivery report
     *
     * @param id
     *            the id of the report to edit
     * @param form
     *            a new form with updated values
     * @return response entity based on success of edit
     */
    @PutMapping ( BASE_PATH + "/LaborDelivery/{id}" )
    @PreAuthorize ( "hasAnyRole('ROLE_OBGYN')" )
    public ResponseEntity editLaborDeliveryReport ( @PathVariable final Long id,
            @RequestBody final LaborDeliveryForm form ) {
        final LaborDeliveryReport report = LaborDeliveryReport.getById( id );

        if ( report != null && report.getId() == id ) {
            report.updateValues( form );
            report.save();
            LoggerUtil.log( TransactionType.OBGYN_EDITS_LABOR_DELIVERY_REPORT, LoggerUtil.currentUser() );
            return new ResponseEntity( report, HttpStatus.OK );
        }
        else {
            return new ResponseEntity( errorResponse( "No report found" ), HttpStatus.NOT_FOUND );
        }
    }

    /**
     * Retrieves a list of all labor and delivery reports
     *
     * @return list of reports
     */
    @GetMapping ( BASE_PATH + "/LaborDelivery" )
    @PreAuthorize ( "hasRole('ROLE_HCP') or hasRole('ROLE_OD') or hasRole('ROLE_OPH') or hasRole('ROLE_OBGYN')" )
    public List<LaborDeliveryReport> getLaborReports () {
        return LaborDeliveryReport.getAllReports();
    }

    /**
     * Returns all the labor and delivery reports for a patient
     *
     * @return a list of labor and delivery reports
     */
    @PreAuthorize ( "hasRole('ROLE_PATIENT')" )
    @GetMapping ( BASE_PATH + "/LaborDeliveryPatient" )
    public ResponseEntity getEntriesPatient () {
        return new ResponseEntity( LaborDeliveryReport.getByPatient( LoggerUtil.currentUser() ), HttpStatus.OK );
    }
}
