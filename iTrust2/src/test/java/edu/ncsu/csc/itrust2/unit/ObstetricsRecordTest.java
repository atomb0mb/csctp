package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc.itrust2.forms.hcp.ObstetricsRecordForm;
import edu.ncsu.csc.itrust2.models.persistent.DomainObject;
import edu.ncsu.csc.itrust2.models.persistent.ObstetricsRecord;

/**
 * Test the ObstetricsRecord class and its ability to store data read from the
 * ObsteticsRecordForm
 *
 * @author Ben Johnson (bfjohns4)
 *
 */
public class ObstetricsRecordTest {

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
        final LocalDate testDate = LocalDate.of( 2016, 10, 17 );
        final ObstetricsRecord obs = new ObstetricsRecord();
        obs.setLastMenstrualPeriod( testDate );
        obs.setId( (long) 2001 );
        obs.setPatient( "Yolanda" );

        assertEquals( testDate, obs.getLastMenstrualPeriod() );
        assertEquals( testDate.plusDays( 280 ), obs.getEstimatedDueDate() );
        assertTrue( obs.getPatient().equals( "Yolanda" ) );
        assertTrue( obs.getId() == 2001 );

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

        obs.save();

        assertEquals( testDate, obs.getLastMenstrualPeriod() );
        assertEquals( testDate.plusDays( 280 ), obs.getEstimatedDueDate() );
    }

}
