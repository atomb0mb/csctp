package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.time.ZonedDateTime;

import org.junit.Test;

import edu.ncsu.csc.itrust2.forms.hcp.LaborDeliveryForm;
import edu.ncsu.csc.itrust2.models.enums.DeliveryMethod;
import edu.ncsu.csc.itrust2.models.persistent.DomainObject;
import edu.ncsu.csc.itrust2.models.persistent.Hospital;
import edu.ncsu.csc.itrust2.models.persistent.LaborDeliveryReport;

/**
 * Class that contains all unit testing for labor and delivery reports
 *
 * @author msned
 *
 */
public class LaborDeliveryTest {
    /**
     * Tests the creation of a new labor and delivery report
     *
     * @throws ParseException
     *             if the data from the form can't be parsed
     */
    @Test
    public void testLaborDeliveryReport () throws ParseException {
        DomainObject.deleteAll( LaborDeliveryReport.class );

        final Hospital hosp = new Hospital( "Dr. Jenkins' Insane Asylum", "123 Main St", "12345", "NC" );
        hosp.save();

        final LaborDeliveryReport report = new LaborDeliveryReport();

        report.setDeliveryDate( ZonedDateTime.now() );
        report.setDeliverymethod( DeliveryMethod.VaginalDelivery );
        report.setDiastolic( 100 );
        report.setFirstname( "James" );
        report.setHeartRate( 88 );
        report.setLaborDate( ZonedDateTime.now() );
        report.setLastname( "Test" );
        report.setLength( 7 );
        report.setPatient( "AliceThirteen" );
        report.setSystolic( 40 );
        report.setWeight( 20 );

        report.setDeliveryDateTwin( ZonedDateTime.now() );
        report.setLaborDateTwin( ZonedDateTime.now() );
        report.setDiastolicTwin( 121 );
        report.setFirstnameTwin( "Ham" );
        report.setHeartRateTwin( 66 );
        report.setLastnameTwin( "Test" );
        report.setLengthTwin( 8 );
        report.setSystolicTwin( 65 );
        report.setWeightTwin( 3 );
        report.setDeliverymethodTwin( DeliveryMethod.VaginalDelivery );

        report.save();

        final LaborDeliveryReport retrieved = LaborDeliveryReport.getById( report.getId() );
        assertEquals( retrieved.getId(), report.getId() );
        assertTrue( retrieved.getLaborDate() != null );
        assertTrue( retrieved.getDeliveryDate() != null );
        assertTrue( retrieved.getLaborDateTwin() != null );
        assertTrue( retrieved.getDeliveryDateTwin() != null );
        assertEquals( retrieved.getDeliverymethod(), report.getDeliverymethod() );
        assertEquals( retrieved.getDiastolic(), report.getDiastolic() );
        assertEquals( retrieved.getFirstname(), report.getFirstname() );
        assertEquals( retrieved.getHeartRate(), report.getHeartRate() );
        assertEquals( retrieved.getLastname(), report.getLastname() );
        assertEquals( retrieved.getLength(), report.getLength(), .1 );
        assertEquals( retrieved.getPatient(), report.getPatient() );
        assertEquals( retrieved.getSystolic(), report.getSystolic() );
        assertEquals( retrieved.getWeight(), report.getWeight(), .1 );
        assertEquals( retrieved.getDiastolicTwin(), report.getDiastolicTwin() );
        assertEquals( retrieved.getFirstnameTwin(), report.getFirstnameTwin() );
        assertEquals( retrieved.getHeartRateTwin(), report.getHeartRateTwin() );
        assertEquals( retrieved.getLastnameTwin(), report.getLastnameTwin() );
        assertEquals( retrieved.getLengthTwin(), report.getLengthTwin(), .1 );
        assertEquals( retrieved.getSystolicTwin(), report.getSystolicTwin() );
        assertEquals( retrieved.getWeightTwin(), report.getWeightTwin(), .1 );
        assertEquals( retrieved.getDeliverymethodTwin(), report.getDeliverymethodTwin() );

        final LaborDeliveryForm form = new LaborDeliveryForm( retrieved );
        assertEquals( retrieved.getDeliverymethod(), form.getDeliverymethod() );
        assertEquals( retrieved.getDiastolic(), form.getDiastolic() );
        assertEquals( retrieved.getFirstname(), form.getFirstname() );
        assertEquals( retrieved.getHeartRate(), form.getHeartRate() );
        assertEquals( retrieved.getLaborDate().toString(), form.getLaborDate() );
        assertEquals( retrieved.getLastname(), form.getLastname() );
    }

    /**
     * Tests the creation of a labor and delivery form
     */
    @Test
    public void testLaborDeliveryForm () {
        final LaborDeliveryForm form = new LaborDeliveryForm();
        form.setDeliveryDate( "2048-04-16T09:50:00.000-04:00" );
        form.setDeliverymethod( DeliveryMethod.VaginalDelivery );
        form.setDiastolic( 100 );
        form.setFirstname( "James" );
        form.setHeartRate( 88 );
        form.setLaborDate( "2048-04-16T09:50:00.000-04:00" );
        form.setLastname( "Test" );
        form.setLength( 7 );
        form.setPatient( "AliceThirteen" );
        form.setSystolic( 40 );
        form.setWeight( 20 );

        form.setDiastolicTwin( 121 );
        form.setDeliveryDateTwin( "2048-04-16T09:50:00.000-04:00" );
        form.setDeliverymethodTwin( DeliveryMethod.VaginalDelivery );
        form.setLaborDateTwin( "2048-04-16T09:50:00.000-04:00" );
        form.setFirstnameTwin( "Ham" );
        form.setHeartRateTwin( 66 );
        form.setLastnameTwin( "Test" );
        form.setLengthTwin( 8 );
        form.setSystolicTwin( 65 );
        form.setWeightTwin( 3 );

        final LaborDeliveryReport report = new LaborDeliveryReport( form );
        assertEquals( form.getDeliverymethod(), report.getDeliverymethod() );
        assertEquals( form.getDiastolic(), report.getDiastolic() );
        assertEquals( form.getFirstname(), report.getFirstname() );
        assertEquals( form.getHeartRate(), report.getHeartRate() );
        assertEquals( form.getLastname(), report.getLastname() );
        assertEquals( form.getLength(), report.getLength(), .1 );
        assertEquals( form.getPatient(), report.getPatient() );
        assertEquals( form.getSystolic(), report.getSystolic() );
        assertEquals( form.getWeight(), report.getWeight(), .1 );
        assertEquals( form.getDiastolicTwin(), report.getDiastolicTwin() );
        assertEquals( form.getFirstnameTwin(), report.getFirstnameTwin() );
        assertEquals( form.getHeartRateTwin(), report.getHeartRateTwin() );
        assertEquals( form.getLastnameTwin(), report.getLastnameTwin() );
        assertEquals( form.getLengthTwin(), report.getLengthTwin(), .1 );
        assertEquals( form.getSystolicTwin(), report.getSystolicTwin() );
        assertEquals( form.getWeightTwin(), report.getWeightTwin(), .1 );
        assertEquals( form.getDeliverymethodTwin(), report.getDeliverymethodTwin() );
    }
}
