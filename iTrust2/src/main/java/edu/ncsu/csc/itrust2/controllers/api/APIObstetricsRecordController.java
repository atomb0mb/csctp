package edu.ncsu.csc.itrust2.controllers.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
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
            return new ResponseEntity(
                    errorResponse( "Could not create ObstetricsRecord provided due to " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Deletes the ObstetricsRecord. Marks the end of a pregnancy
     *
     * @param patient
     *            username of patient belonging to the record
     * @return ResponseEntity based on success of function
     */
    @DeleteMapping ( BASE_PATH + "/obstetricsrecord/{patient}" )
    @PreAuthorize ( "hasRole('ROLE_OBGYN')" )
    public ResponseEntity deleteObstetricsRecord ( @PathVariable final String patient ) {
        if ( ObstetricsRecord.getByPatient( patient ).isEmpty() ) {
            return new ResponseEntity( errorResponse( "No Obstetrics Records found for " + patient ),
                    HttpStatus.NOT_FOUND );
        }
        try {
            ObstetricsRecord.getByPatient( patient ).get( 0 ).delete();
            return new ResponseEntity( patient, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            e.printStackTrace();
            return new ResponseEntity( "Could not delete record for " + patient, HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Saves a new Pregnancy instance in the database
     *
     * @param patient
     *            - username of patient who had the pregnancy
     * @param pForm
     *            - the PregnancyForm to read and create Pregnancy instance from
     * @return ResponseEntity based on success of creating the Pregnancy
     */
    @PreAuthorize ( "hasRole('ROLE_OBGYN')" )
    @PostMapping ( BASE_PATH + "/pregnancy/{patient}" )
    public ResponseEntity createPregnancy ( @PathVariable final String patient,
            @RequestBody final PregnancyForm pForm ) {
        try {
            final Pregnancy pregnancy = new Pregnancy( patient, pForm );
            // pregnancy.setPatient( patient );
            pregnancy.save();
            // Update the flags depending on previous pregnancies
            if ( ObstetricsRecord.getByPatient( patient ).size() > 0 ) {
                ObstetricsRecord.getByPatient( patient ).get( 0 ).updateFlags();
            }
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
     * @param patient
     *            username of patient to retrieve records for
     *
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

        final List<ObstetricsRecord> orList = ObstetricsRecord.getByPatient( patient );

        // Before returning the obstetrics record, update the pregnancy flags
        if ( orList.size() > 0 ) {
            orList.get( 0 ).updateFlags();
        }
        LoggerUtil.log( TransactionType.HCP_VIEW_OBS_RECORD, User.getByName( LoggerUtil.currentUser() ),
                User.getByName( patient ) );
        return new ResponseEntity( orList, HttpStatus.OK );
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
        final List<ObstetricsRecord> orList = ObstetricsRecord.getByPatient( LoggerUtil.currentUser() );

        // Before returning the obstetrics record, update the pregnancy flags
        if ( orList.size() > 0 ) {
            orList.get( 0 ).updateFlags();
        }
        LoggerUtil.log( TransactionType.PATIENT_VIEW_OBS_RECORD, User.getByName( LoggerUtil.currentUser() ),
                User.getByName( LoggerUtil.currentUser() ) );
        return new ResponseEntity( orList, HttpStatus.OK );
    }

    /**
     * Return list of past pregnancies for a patient regjstered to receive care
     * from this HCP
     *
     * @param patient
     *            <<<<<<< HEAD the username of the patient ======= username of
     *            patient to retrieve records for >>>>>>>
     *            bcef4a4804b52ca262953085bd0e2dcca8dfa11d
     *
     * @return List of pregnancies for the patient
     */
    @PreAuthorize ( "hasAnyRole('ROLE_HCP', 'ROLE_OD', 'ROLE_OPH', 'ROLE_OBGYN')" )
    @GetMapping ( BASE_PATH + "/pregnancy/{patient}" )
    public ResponseEntity getPregnanciesHCP ( @PathVariable final String patient ) {
        if ( null == Patient.getByName( patient ) ) {
            return new ResponseEntity( errorResponse( "No patients found with username " + patient ),
                    HttpStatus.NOT_FOUND );
        }
        if ( ObstetricsRecord.getByPatient( patient ).size() > 0 ) {
            ObstetricsRecord.getByPatient( patient ).get( 0 ).updateFlags();
            ObstetricsRecord.getByPatient( patient ).get( 0 ).save();
        }
        return new ResponseEntity( Pregnancy.getByPatient( patient ), HttpStatus.OK );
    }

    /**
     * Return list of previous pregnancies for currently logged in patient
     *
     * @return list of pregnancy objects
     */
    @PreAuthorize ( "hasRole('ROLE_PATIENT')" )
    @GetMapping ( BASE_PATH + "/pregnancy" )
    public ResponseEntity getPregnanciesPatient () {
        if ( null == Patient.getByName( LoggerUtil.currentUser() ) ) {
            return new ResponseEntity( errorResponse( "No patients found with username " + LoggerUtil.currentUser() ),
                    HttpStatus.NOT_FOUND );
        }
        return new ResponseEntity( Pregnancy.getByPatient( LoggerUtil.currentUser() ), HttpStatus.OK );
    }
}
