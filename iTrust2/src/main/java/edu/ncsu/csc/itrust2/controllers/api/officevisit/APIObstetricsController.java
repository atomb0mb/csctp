package edu.ncsu.csc.itrust2.controllers.api.officevisit;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.itrust2.controllers.api.APIController;
import edu.ncsu.csc.itrust2.forms.hcp.ObstetricsVisitForm;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.models.persistent.ObstetricsOfficeVisit;
import edu.ncsu.csc.itrust2.models.persistent.User;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

/**
 * Class that maintains all the api endpoints for REST for Obstetric office
 * visits. Connects the front end to the database mapping of objects
 *
 * @author msned
 *
 */
@RestController
@SuppressWarnings ( { "unchecked", "rawtypes" } )
public class APIObstetricsController extends APIController {

    /**
     * Returns the office visit specified by id
     *
     * @param id
     *            the id of the office visit
     * @return the obstetrics office visit
     */
    @GetMapping ( BASE_PATH + "/ObstetricsVisit/{id}" )
    @PreAuthorize ( "hasAnyRole('ROLE_HCP', 'ROLE_OD', 'ROLE_OPH', 'ROLE_PATIENT', 'ROLE_OBGYN')" )
    public ResponseEntity getObstetricsVisitById ( @PathVariable final Long id ) {
        final ObstetricsOfficeVisit visit = ObstetricsOfficeVisit.getById( id );
        if ( visit == null ) {
            return new ResponseEntity( errorResponse( "No Obstetrics office visit found for " + id ),
                    HttpStatus.NOT_FOUND );
        }
        else {
            final User current = User.getByName( LoggerUtil.currentUser() );
            if ( current != null && current.isDoctor() ) {
                LoggerUtil.log( TransactionType.OBGYNVISIT_HCP_VIEW, LoggerUtil.currentUser() );
            }
            else {
                LoggerUtil.log( TransactionType.OBGYNVISIT_PATIENT_VIEW, LoggerUtil.currentUser() );
            }
            return new ResponseEntity( visit, HttpStatus.OK );
        }
    }

    /**
     * API endpoint for deleting an obstetrics office visit
     *
     * @param id
     *            Long used to specify what visit to delete
     * @return response of whether it was successfully deleted
     */
    @DeleteMapping ( BASE_PATH + "/ObstetricsVisit/{id}" )
    @PreAuthorize ( "hasAnyRole('ROLE_OBGYN', 'ROLE_PATIENT')" )
    public ResponseEntity deleteObstetricsVisit ( @PathVariable final Long id ) {
        final ObstetricsOfficeVisit visit = ObstetricsOfficeVisit.getById( id );
        if ( visit == null ) {
            return new ResponseEntity( errorResponse( "No Obstetrics office visit found for " + id ),
                    HttpStatus.NOT_FOUND );
        }
        try {
            visit.delete();
            LoggerUtil.log( TransactionType.OBGYNVISIT_DELETE, LoggerUtil.currentUser() );
            return new ResponseEntity( id, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            e.printStackTrace();
            return new ResponseEntity( "Could not delete " + id, HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Adds a new obstetrics visit to the database
     *
     * @param visitF
     *            a form containing the values of the office visit
     * @return whether the form was successfully added
     */
    @PostMapping ( BASE_PATH + "/ObstetricsVisit" )
    @PreAuthorize ( "hasAnyRole('ROLE_OBGYN')" )
    public ResponseEntity addObstetricsVisit ( @RequestBody final ObstetricsVisitForm visitF ) {
        try {
            final ObstetricsOfficeVisit visit = new ObstetricsOfficeVisit( visitF );

            if ( null != ObstetricsOfficeVisit.getById( visit.getId() ) ) {
                return new ResponseEntity(
                        errorResponse( "Office visit with the id " + visit.getId() + " already exists" ),
                        HttpStatus.CONFLICT );
            }
            visit.save();
            LoggerUtil.log( TransactionType.OBGYNVISIT_CREATE, LoggerUtil.currentUser(),
                    visit.getPatient().getUsername() );
            return new ResponseEntity( visit, HttpStatus.OK );

        }
        catch ( final Exception e ) {
            e.printStackTrace();
            return new ResponseEntity(
                    errorResponse( "Could not validate or save the Obstetric visit provided due to " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Updates an exisiting office visit
     *
     * @param id
     *            long used to identify visit
     * @param form
     *            the form with updated values
     * @return whether the visit exists and could be updated
     */
    @PutMapping ( BASE_PATH + "/ObstetricsVisit/{id}" )
    @PreAuthorize ( "hasAnyRole('ROLE_OBGYN')" )
    public ResponseEntity editobstetricsVisit ( @PathVariable final Long id,
            @RequestBody final ObstetricsVisitForm form ) {
        try {
            final ObstetricsOfficeVisit visit = new ObstetricsOfficeVisit( form );
            if ( !visit.getId().equals( id ) ) {
                return new ResponseEntity( errorResponse( "Id does not match" ), HttpStatus.CONFLICT );
            }

            final ObstetricsOfficeVisit newVisit = ObstetricsOfficeVisit.getById( id );
            if ( newVisit == null ) {
                return new ResponseEntity( errorResponse( "Couldn't find visit" ), HttpStatus.NOT_FOUND );
            }

            visit.save();
            LoggerUtil.log( TransactionType.OBGYNVISIT_EDIT, LoggerUtil.currentUser() );
            return new ResponseEntity( visit, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity(
                    errorResponse( "Could not update " + form.toString() + " because of " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Tells the system that the visit was viewed by a patient
     *
     * @param id
     *            The id of the office visit
     * @param form
     *            the visit being viewed
     * @return whether the visit was found
     */
    public ResponseEntity viewAsPatient ( @PathVariable final Long id, @RequestBody final ObstetricsVisitForm form ) {
        final ObstetricsOfficeVisit visit = ObstetricsOfficeVisit.getById( id );
        if ( visit == null ) {
            return new ResponseEntity( errorResponse( "No visit found" ), HttpStatus.NOT_FOUND );
        }
        LoggerUtil.log( TransactionType.OBGYNVISIT_PATIENT_VIEW, form.getHcp(), form.getPatient(),
                form.getPatient() + " viewed their basic health metrics from " + form.getDate() );
        return new ResponseEntity( HttpStatus.OK );
    }

    /**
     * Tells the system that the visit was viewed by a HCP
     *
     * @param id
     *            The id of the office visit
     * @param form
     *            the visit being viewed
     * @return whether the visit was found
     */
    @PostMapping ( BASE_PATH + "/ObstetricsVisit/hcp/view/{id}" )
    @PreAuthorize ( "hasAnyRole('ROLE_HCP', 'ROLE_OD', 'ROLE_OPH', 'ROLE_OBGYN')" )
    public ResponseEntity viewAsHCP ( @PathVariable final Long id, @RequestBody final ObstetricsVisitForm form ) {
        final ObstetricsOfficeVisit visit = ObstetricsOfficeVisit.getById( id );
        if ( visit == null ) {
            return new ResponseEntity( errorResponse( "No visit found" ), HttpStatus.NOT_FOUND );
        }
        LoggerUtil.log( TransactionType.OBGYNVISIT_HCP_VIEW, form.getHcp(), form.getPatient(),
                form.getHcp() + " viewed basic health metrics for " + form.getPatient() + " from " + form.getDate() );
        return new ResponseEntity( HttpStatus.OK );
    }
}
