package edu.ncsu.csc.itrust2.controllers.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.itrust2.forms.hcp.ObstetricsRecordForm;
import edu.ncsu.csc.itrust2.forms.hcp.PregnancyForm;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.models.persistent.ObstetricsRecord;
import edu.ncsu.csc.itrust2.models.persistent.Patient;
import edu.ncsu.csc.itrust2.models.persistent.Pregnancy;
import edu.ncsu.csc.itrust2.models.persistent.User;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

/**
 * Class that provides REST API endpoints for the ObstetricsRecord model. Uses a
 * patient's username to create and retrieve entries.
 *
 * @author Ben Johnson (bfjohns4)
 */
@SuppressWarnings ( { "rawtypes", "unchecked" } )
@RestController
public class APIObstetricsRecordController extends APIController {
    /**
     * Saves a new ObstetricsRecord instance
     *
     * @param obsForm
     *            the ObstetricsRecordForm to read and create ObstetricsRecords
     *            based off of
     * @param patient
     *            - username of patient the record is being made for
     *
     * @return ResponseEntity based on success of creating the ObstetricsRecord
     */
    @PreAuthorize ( "hasRole('ROLE_OBGYN')" )
    @PostMapping ( BASE_PATH + "/obstetricsrecord/{patient}" )
    public ResponseEntity createObstetricsRecord ( @PathVariable final String patient,
            @RequestBody final ObstetricsRecordForm obsForm ) {
        try {
            final ObstetricsRecord obr = new ObstetricsRecord( obsForm );
            obr.setPatient( patient );
            obr.save();

            LoggerUtil.log( TransactionType.OBGYN_CREATE_OBS_RECORD, LoggerUtil.currentUser() );
            return new ResponseEntity( obr, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            e.printStackTrace();
            return new ResponseEntity(
                    errorResponse( "Could not create ObstetricsRecord provided due to " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Saves a new Pregnancy instance in the database
     *
     * @param pForm
     *            - the PregnancyForm to read and create Pregnancy instance from
     * @return ResponseEntity based on success of creating the Pregnancy
     */
    @PreAuthorize ( "hasRole('ROLE_OBGYN')" )
    @PostMapping ( BASE_PATH + "/pregnancy/{patient}" )
    public ResponseEntity createPregnancy ( @PathVariable final String patient, final PregnancyForm pForm ) {
        try {
            final Pregnancy pregnancy = new Pregnancy( pForm );
            pregnancy.setPatient( patient );
            pregnancy.save();

            return new ResponseEntity( pregnancy, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            e.printStackTrace();
            return new ResponseEntity( errorResponse( "Could not create Pregnancy provided due to " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Retrieves the Obstetrics Record for a patient of the HCP currently logged
     * into iTrust2
     *
     * @return ResponseEntity with the ObstetricsRecord for the patient, or an
     *         error message if cannot be found
     */
    @PreAuthorize ( "hasAnyRole('ROLE_HCP', 'ROLE_OD', 'ROLE_OPH', 'ROLE_OBGYN')" )
    @GetMapping ( BASE_PATH + "/obstetricsrecord/{patient}" )
    public ResponseEntity getObstetricsRecordHCP ( @PathVariable final String patient ) {
        if ( null == Patient.getByName( patient ) ) {
            return new ResponseEntity( errorResponse( "No patients found with username " + patient ),
                    HttpStatus.NOT_FOUND );
        }
        LoggerUtil.log( TransactionType.HCP_VIEW_OBS_RECORD, User.getByName( LoggerUtil.currentUser() ),
                User.getByName( patient ) );
        return new ResponseEntity( ObstetricsRecord.getByPatient( patient ), HttpStatus.OK );
    }

    /**
     * Retrieves the Obstetrics Record for the patient currently logged into
     * iTrust2
     *
     * @return ResponseEntity with the ObstetricsRecord for the patient, or an
     *         error message if cannot be found
     */
    @PreAuthorize ( "hasRole('ROLE_PATIENT')" )
    @GetMapping ( BASE_PATH + "/obstetricsrecord" )
    public ResponseEntity getObstetricsRecordPatient () {
        if ( null == Patient.getByName( LoggerUtil.currentUser() ) ) {
            return new ResponseEntity( errorResponse( "No patients found with username " + LoggerUtil.currentUser() ),
                    HttpStatus.NOT_FOUND );
        }
        LoggerUtil.log( TransactionType.PATIENT_VIEW_OBS_RECORD, User.getByName( LoggerUtil.currentUser() ),
                User.getByName( LoggerUtil.currentUser() ) );
        return new ResponseEntity( ObstetricsRecord.getByPatient( LoggerUtil.currentUser() ), HttpStatus.OK );
    }

    /**
     * Return list of past pregnancies for a patient regjstered to receive care
     * from this HCP
     *
     * @return List of pregnancies for the patient
     */
    @PreAuthorize ( "hasAnyRole('ROLE_HCP', 'ROLE_OD', 'ROLE_OPH', 'ROLE_OBGYN')" )
    @GetMapping ( BASE_PATH + "/pregnancies/{patient}" )
    public ResponseEntity getPregnanciesHCP ( @PathVariable final String patient ) {
        if ( null == Patient.getByName( patient ) ) {
            return new ResponseEntity( errorResponse( "No patients found with username " + patient ),
                    HttpStatus.NOT_FOUND );
        }
        return new ResponseEntity( Pregnancy.getByPatient( patient ), HttpStatus.OK );
    }

    /**
     * Return list of previous pregnancies for currently logged in patient
     *
     * @return list of pregnancy objects
     */
    @PreAuthorize ( "hasRole('ROLE_PATIENT')" )
    @GetMapping ( BASE_PATH + "/pregnancies" )
    public ResponseEntity getPregnanciesPatient () {
        if ( null == Patient.getByName( LoggerUtil.currentUser() ) ) {
            return new ResponseEntity( errorResponse( "No patients found with username " + LoggerUtil.currentUser() ),
                    HttpStatus.NOT_FOUND );
        }
        return new ResponseEntity( Pregnancy.getByPatient( LoggerUtil.currentUser() ), HttpStatus.OK );
    }
}
