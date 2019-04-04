package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.text.ParseException;
import java.time.ZonedDateTime;

import org.junit.Test;

import edu.ncsu.csc.itrust2.forms.hcp.ObstetricsVisitForm;
import edu.ncsu.csc.itrust2.models.enums.AppointmentType;
import edu.ncsu.csc.itrust2.models.enums.HouseholdSmokingStatus;
import edu.ncsu.csc.itrust2.models.enums.PatientSmokingStatus;
import edu.ncsu.csc.itrust2.models.persistent.BasicHealthMetrics;
import edu.ncsu.csc.itrust2.models.persistent.DomainObject;
import edu.ncsu.csc.itrust2.models.persistent.Hospital;
import edu.ncsu.csc.itrust2.models.persistent.ObstetricsOfficeVisit;
import edu.ncsu.csc.itrust2.models.persistent.User;

/**
 * Unit testing class for the OstetricsOfficeVisit object and the
 * ObstetricsVisitForm form
 *
 * @author msned
 *
 */
public class ObstetricsOfficeVisitTest {
    /**
     * Tests creating a new ObstetricsOfficeVisit with fields
     *
     * @throws ParseException
     *             if the data from the form cant be parsed
     */
    @Test
    public void testObstetricsOfficeVisit () throws ParseException {
        DomainObject.deleteAll( ObstetricsOfficeVisit.class );

        final Hospital hosp = new Hospital( "Dr. Jenkins' Insane Asylum", "123 Main St", "12345", "NC" );
        hosp.save();

        final ObstetricsOfficeVisit visit = new ObstetricsOfficeVisit();

        final BasicHealthMetrics bhm = new BasicHealthMetrics();

        bhm.setDiastolic( 150 );
        bhm.setHcp( User.getByName( "bobbyOD" ) );
        bhm.setPatient( User.getByName( "AliceThirteen" ) );
        bhm.setHdl( 75 );
        bhm.setLdl( 75 );
        bhm.setHeight( 75f );
        bhm.setWeight( 130f );
        bhm.setTri( 300 );
        bhm.setSystolic( 150 );
        bhm.setHouseSmokingStatus( HouseholdSmokingStatus.NONSMOKING );
        bhm.setPatientSmokingStatus( PatientSmokingStatus.NEVER );

        bhm.save();

        visit.setBasicHealthMetrics( bhm );
        visit.setType( AppointmentType.OBGYN_OFFICE_VISIT );
        visit.setHospital( hosp );
        visit.setPatient( User.getByName( "AliceThirteen" ) );
        visit.setHcp( User.getByName( "bobbyOD" ) );
        visit.setDate( ZonedDateTime.now() );
        visit.save();

        visit.setFetalHeartRate( 80 );
        visit.setFundalHeight( 2.0 );
        visit.setNumOfWeeksPreg( 10 );
        visit.setTwins( false );
        visit.setLowLyingPlacenta( false );

        visit.save();

        // Test the visit's persistence
        final ObstetricsOfficeVisit copy = ObstetricsOfficeVisit.getById( visit.getId() );
        assertEquals( visit.getId(), copy.getId() );
        assertEquals( visit.getAppointment(), copy.getAppointment() );
        assertEquals( visit.getBasicHealthMetrics(), copy.getBasicHealthMetrics() );
        assertEquals( visit.getHcp(), copy.getHcp() );
        assertEquals( visit.getHospital().getName(), copy.getHospital().getName() );
        assertEquals( visit.getPatient(), copy.getPatient() );
        assertEquals( visit.getFetalHeartRate(), copy.getFetalHeartRate() );
        assertEquals( visit.getFundalHeight(), copy.getFundalHeight() );
        assertEquals( visit.getNumOfWeeksPreg(), copy.getNumOfWeeksPreg() );
        assertEquals( visit.isLowLyingPlacenta(), copy.isLowLyingPlacenta() );
        assertEquals( visit.isTwins(), copy.isTwins() );

        // Test the form object
        final ObstetricsVisitForm form = new ObstetricsVisitForm( visit );
        form.setPreScheduled( null );
        assertEquals( visit.getId().toString(), form.getId() );
        assertEquals( visit.getHcp().getUsername(), form.getHcp() );
        assertEquals( visit.getHospital().getName(), form.getHospital() );
        assertEquals( visit.getPatient().getUsername(), form.getPatient() );

        final ObstetricsOfficeVisit clone = new ObstetricsOfficeVisit( form );
        assertEquals( visit.getId(), clone.getId() );
        assertEquals( visit.getAppointment(), clone.getAppointment() );
        assertEquals( visit.getBasicHealthMetrics().getDiastolic(), clone.getBasicHealthMetrics().getDiastolic() );
        assertEquals( visit.getHcp(), clone.getHcp() );
        assertEquals( visit.getHospital().getName(), clone.getHospital().getName() );
        assertEquals( visit.getPatient(), clone.getPatient() );

        visit.save();

        visit.delete();
    }

    /**
     * Tests the creation of an obstetrics visit form
     *
     * @throws NumberFormatException
     *             if the string can't be converted to int
     * @throws ParseException
     *             if form can't be parsed to office visit object
     */
    @Test
    public void testObstetricsVisitForm () throws NumberFormatException, ParseException {

        final ObstetricsVisitForm visit = new ObstetricsVisitForm();
        visit.setPreScheduled( null );
        visit.setDate( "2048-04-16T09:50:00.000-04:00" ); // 4/16/2048 9:50 AM
        visit.setHcp( "hcp" );
        visit.setPatient( "patient" );
        visit.setNotes( "Test office visit" );
        visit.setType( AppointmentType.OBGYN_OFFICE_VISIT.toString() );
        visit.setHospital( "iTrust Test Hospital 2" );
        visit.setDiastolic( 150 );
        visit.setHdl( 75 );
        visit.setLdl( 75 );
        visit.setHeight( 75f );
        visit.setWeight( 130f );
        visit.setTri( 300 );
        visit.setSystolic( 150 );
        visit.setHouseSmokingStatus( HouseholdSmokingStatus.NONSMOKING );
        visit.setPatientSmokingStatus( PatientSmokingStatus.NEVER );

        visit.setFetalHeartRate( 80 );
        visit.setFundalHeight( 2.0 );
        visit.setNumOfWeeksPreg( 10 );
        visit.setTwins( false );
        visit.setLowLyingPlacenta( false );

        final ObstetricsOfficeVisit ov = new ObstetricsOfficeVisit( visit );

        assertEquals( visit.getHcp(), ov.getHcp().getUsername() );
        assertEquals( visit.getPatient(), ov.getPatient().getUsername() );
    }

    /**
     * Tests creating the office visit without optional fields
     */
    @Test
    public void testOptionalHealthMetrics () {
        DomainObject.deleteAll( ObstetricsOfficeVisit.class );

        final Hospital hosp = new Hospital( "Dr. Jenkins' Insane Asylum", "123 Main St", "12345", "NC" );
        hosp.save();

        final ObstetricsOfficeVisit visit = new ObstetricsOfficeVisit();

        final BasicHealthMetrics bhm = new BasicHealthMetrics();

        bhm.setDiastolic( 150 );
        bhm.setHcp( User.getByName( "bobbyOD" ) );
        bhm.setPatient( User.getByName( "AliceThirteen" ) );
        bhm.setHeight( 75f );
        bhm.setWeight( 130f );
        bhm.setHouseSmokingStatus( HouseholdSmokingStatus.NONSMOKING );

        bhm.save();

        visit.setBasicHealthMetrics( bhm );
        visit.setType( AppointmentType.OBGYN_OFFICE_VISIT );
        visit.setHospital( hosp );
        visit.setPatient( User.getByName( "AliceThirteen" ) );
        visit.setHcp( User.getByName( "bobbyOD" ) );
        visit.setDate( ZonedDateTime.now() );
        visit.save();

        visit.setFetalHeartRate( 80 );
        visit.setFundalHeight( 2.0 );
        visit.setNumOfWeeksPreg( 10 );
        visit.setTwins( false );
        visit.setLowLyingPlacenta( false );
        visit.save();

        // Test the visit's persistence
        final ObstetricsOfficeVisit copy = ObstetricsOfficeVisit.getById( visit.getId() );
        assertFalse( copy == null );
        assertEquals( visit.getId(), copy.getId() );
        assertEquals( visit.getAppointment(), copy.getAppointment() );
        assertEquals( visit.getBasicHealthMetrics(), copy.getBasicHealthMetrics() );
        assertEquals( visit.getHcp(), copy.getHcp() );
        assertEquals( visit.getHospital().getName(), copy.getHospital().getName() );
        assertEquals( visit.getPatient(), copy.getPatient() );
        assertEquals( visit.getFetalHeartRate(), copy.getFetalHeartRate() );
        assertEquals( visit.getFundalHeight(), copy.getFundalHeight() );
        assertEquals( visit.getNumOfWeeksPreg(), copy.getNumOfWeeksPreg() );
        assertEquals( visit.isLowLyingPlacenta(), copy.isLowLyingPlacenta() );
        assertEquals( visit.isTwins(), copy.isTwins() );

        visit.delete();
    }
}
