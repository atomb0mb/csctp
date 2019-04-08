package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust2.forms.hcp.ObstetricsRecordForm;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.persistent.DomainObject;
import edu.ncsu.csc.itrust2.models.persistent.ObstetricsRecord;
import edu.ncsu.csc.itrust2.models.persistent.User;

/**
 * Test the ObstetricsRecord class and its ability to store data read from the
 * ObsteticsRecordForm
 *
 * @author Ben Johnson (bfjohns4)
 *
 */
public class ObstetricsRecordTest {

    /** Patient to be used for testing */
    final User patient = new User( "patient", "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.",
            Role.ROLE_PATIENT, 1 );

    /**
     * Erase all data before trying to write new things
     *
     */
    @Before
    public void setUp () {
        DomainObject.deleteAll( ObstetricsRecord.class );
    }

    /**
     * Test creation of ObstetricsRecord w/o a form, by setting the LMP by hand
     * and testing the getters for both LMP and est due date
     *
     */
    @Test
    public void testObstetricsRecord () {
        final LocalDate testDate = LocalDate.of( 2019, 01, 01 );
        final ObstetricsRecord obs = new ObstetricsRecord();
        obs.setLastMenstrualPeriod( testDate );
        obs.setId( (long) 2001 );
        obs.setPatient( "patient" );

        assertEquals( testDate, obs.getLastMenstrualPeriod() );
        assertEquals( testDate.plusDays( 280 ), obs.getEstimatedDueDate() );
        assertTrue( obs.getPatient().equals( "patient" ) );
        assertTrue( obs.getId() == 2001 );
        assertTrue( 10 < obs.getNumWeeksPregnant() );

        final LocalDate testDate2 = LocalDate.of( 2018, 12, 01 );
        obs.setLastMenstrualPeriod( testDate2 );
        assertTrue( 13 < obs.getNumWeeksPregnant() );

    }

    /**
     * Test the ability to initialize ObstetricsRecord objects using the Form
     *
     */
    @Test
    public void testObstetricsRecordByForm () {
        final LocalDate testDate = LocalDate.of( 2016, 10, 17 );

        final ObstetricsRecordForm obsForm = new ObstetricsRecordForm();
        obsForm.setLastMenstrualPeriod( "2016-10-17" );

        final ObstetricsRecord obs = new ObstetricsRecord( obsForm );
        obs.setPatient( "patient" );

        assertEquals( testDate, obs.getLastMenstrualPeriod() );
        assertEquals( testDate.plusDays( 280 ), obs.getEstimatedDueDate() );

        obs.save();

        final ObstetricsRecord copy = ObstetricsRecord.getById( obs.getId() );

        assertEquals( obs.getLastMenstrualPeriod(), copy.getLastMenstrualPeriod() );
        assertEquals( obs.getEstimatedDueDate(), copy.getEstimatedDueDate() );
        assertEquals( obs.getId(), copy.getId() );
        assertTrue( obs.getPatient().equals( copy.getPatient() ) );

        final ObstetricsRecord copy2 = ObstetricsRecord.getByPatient( "patient" ).get( 0 );

        assertEquals( obs.getLastMenstrualPeriod(), copy2.getLastMenstrualPeriod() );
        assertEquals( obs.getEstimatedDueDate(), copy2.getEstimatedDueDate() );
        assertEquals( obs.getId(), copy2.getId() );
        assertTrue( obs.getPatient().equals( copy2.getPatient() ) );

    }

    /**
     * Test to ensure ObstetricsRecord rejects invalid date inputs
     *
     */
    @Test
    public void testInvalidDate () {
        final ObstetricsRecordForm obsForm = new ObstetricsRecordForm();
        obsForm.setLastMenstrualPeriod( "2200-01-01" );

        try {
            final ObstetricsRecord obs = new ObstetricsRecord( obsForm );
        }
        catch ( final Exception e ) {
            assertEquals( "Date must be before current date", e.getMessage() );
        }

    }

}
