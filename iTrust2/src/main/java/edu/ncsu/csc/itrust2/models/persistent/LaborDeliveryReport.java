package edu.ncsu.csc.itrust2.models.persistent;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Vector;

import javax.persistence.Basic;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.criterion.Criterion;

import com.google.gson.annotations.JsonAdapter;

import edu.ncsu.csc.itrust2.adapters.ZonedDateTimeAdapter;
import edu.ncsu.csc.itrust2.adapters.ZonedDateTimeAttributeConverter;
import edu.ncsu.csc.itrust2.forms.hcp.LaborDeliveryForm;
import edu.ncsu.csc.itrust2.models.enums.DeliveryMethod;

/**
 * Pojo for labor and delivery report
 *
 * @author msned
 *
 */
@Entity
@Table ( name = "LaborDeliveryReport" )
public class LaborDeliveryReport extends DomainObject<LaborDeliveryReport> implements Serializable {
    /**
     * Randomly generated ID.
     */
    private static final long serialVersionUID = 424094627895115508L;

    /**
     * The patient who is giving birth
     */
    private String            patient;

    /**
     * The date and time that labor started
     */
    @NotNull
    @Basic
    @Convert ( converter = ZonedDateTimeAttributeConverter.class )
    @JsonAdapter ( ZonedDateTimeAdapter.class )
    private ZonedDateTime     laborDate;

    /**
     * The date and time the delivery happened
     */
    @NotNull
    @Basic
    @Convert ( converter = ZonedDateTimeAttributeConverter.class )
    @JsonAdapter ( ZonedDateTimeAdapter.class )
    private ZonedDateTime     deliveryDate;

    /**
     * First name of baby
     */
    private String            firstname;

    /**
     * Last name of baby
     */
    private String            lastname;

    /**
     * Weight of baby
     */
    private double            weight;

    /**
     * Length of baby
     */
    private double            length;

    /**
     * Heart rate of baby
     */
    private int               heartRate;

    /**
     * Systolic blood pressure of baby
     */
    private int               systolic;

    /**
     * Diastolic blood pressure of baby
     */
    private int               diastolic;

    /**
     * The type of delivery
     */
    private DeliveryMethod    deliverymethod;

    /**
     * The first name of a twin
     */
    private String            firstnameTwin;

    /**
     * Last name of a twin
     */
    private String            lastnameTwin;

    /**
     * Weight of a twin
     */
    private double            weightTwin;

    /**
     * Length of a twin
     */
    private double            lengthTwin;

    /**
     * Heart rate of a twin
     */
    private int               heartRateTwin;

    /**
     * Systolic blood pressure of the twin
     */
    private int               systolicTwin;

    /**
     * Diastolic blood pressure of the twin
     */
    private int               diastolicTwin;

    /**
     * The id of this report
     */
    @Id
    @GeneratedValue ( strategy = GenerationType.AUTO )
    private Long              id;

    /**
     * The twins delivery method
     */
    private DeliveryMethod    deliverymethodTwin;

    /**
     * Empty Constructor hibernate
     */
    public LaborDeliveryReport () {
    }

    /**
     * Cosntructor that creates report from form
     *
     * @param form
     *            the form with user input for the report
     */
    public LaborDeliveryReport ( final LaborDeliveryForm form ) {
        setPatient( form.getPatient() );
        setLaborDate( ZonedDateTime.parse( form.getLaborDate() ) );
        setDeliveryDate( ZonedDateTime.parse( form.getDeliveryDate() ) );
        setFirstname( form.getFirstname() );
        setLastname( form.getLastname() );
        setWeight( form.getWeight() );
        setLength( form.getLength() );
        setHeartRate( form.getHeartRate() );
        setSystolic( form.getSystolic() );
        setDiastolic( form.getDiastolic() );
        setDeliverymethod( form.getDeliverymethod() );
        setFirstnameTwin( form.getFirstnameTwin() );
        setLastnameTwin( form.getLastnameTwin() );
        setWeightTwin( form.getWeightTwin() );
        setLengthTwin( form.getLengthTwin() );
        setHeartRateTwin( form.getHeartRateTwin() );
        setSystolicTwin( form.getSystolicTwin() );
        setDiastolicTwin( form.getDiastolicTwin() );
        setDeliverymethodTwin( form.getDeliverymethodTwin() );
    }

    /**
     * Get a report based on id
     *
     * @param id
     *            the database ID
     * @return the the report with matching id
     */
    public static LaborDeliveryReport getById ( final Long id ) {
        try {
            return (LaborDeliveryReport) getWhere( LaborDeliveryReport.class, eqList( ID, id ) ).get( 0 );
        }
        catch ( final Exception e ) {
            return null;
        }
    }

    /**
     * Get a list of reports for a specific patient
     *
     * @param patient
     *            the username of the patient who the reports are for
     * @return a list of DiaryEntries for the given patient
     */
    @SuppressWarnings ( "unchecked" )
    public static List<LaborDeliveryReport> getByPatient ( final String patient ) {
        final Vector<Criterion> criteria = new Vector<Criterion>();
        criteria.add( eq( "patient", patient ) );

        return (List<LaborDeliveryReport>) getWhere( LaborDeliveryReport.class, criteria );
    }

    /**
     * Returns the id
     *
     * @return the id of the report
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Returns the patient giving birth
     *
     * @return the patient
     */
    public String getPatient () {
        return patient;
    }

    /**
     * Sets the patient giving birth
     *
     * @param patient
     *            the patient to set
     */
    public void setPatient ( final String patient ) {
        this.patient = patient;
    }

    /**
     * Returns the date of the start of labor
     *
     * @return the laborDate
     */
    public ZonedDateTime getLaborDate () {
        return laborDate;
    }

    /**
     * Sets the date of the start of labor
     *
     * @param laborDate
     *            the laborDate to set
     */
    public void setLaborDate ( final ZonedDateTime laborDate ) {
        this.laborDate = laborDate;
    }

    /**
     * Returns the date the delivery happened
     *
     * @return the deliveryDate
     */
    public ZonedDateTime getDeliveryDate () {
        return deliveryDate;
    }

    /**
     * Sets the date the delivery happened
     *
     * @param deliveryDate
     *            the deliveryDate to set
     */
    public void setDeliveryDate ( final ZonedDateTime deliveryDate ) {
        this.deliveryDate = deliveryDate;
    }

    /**
     * Returns the first name of the baby
     *
     * @return the firstname
     */
    public String getFirstname () {
        return firstname;
    }

    /**
     * Sets the first name of the baby
     *
     * @param firstname
     *            the firstname to set
     */
    public void setFirstname ( final String firstname ) {
        this.firstname = firstname;
    }

    /**
     * Returns the last name of the baby
     *
     * @return the lastname
     */
    public String getLastname () {
        return lastname;
    }

    /**
     * Sets the last name of the baby
     *
     * @param lastname
     *            the lastname to set
     */
    public void setLastname ( final String lastname ) {
        this.lastname = lastname;
    }

    /**
     * Returns the weight of the baby
     *
     * @return the weight
     */
    public double getWeight () {
        return weight;
    }

    /**
     * Sets the weight of the baby
     *
     * @param weight
     *            the weight to set
     */
    public void setWeight ( final double weight ) {
        this.weight = weight;
    }

    /**
     * Returns the length of the baby
     *
     * @return the length
     */
    public double getLength () {
        return length;
    }

    /**
     * Sets the length of the baby
     *
     * @param length
     *            the length to set
     */
    public void setLength ( final double length ) {
        this.length = length;
    }

    /**
     * Returns the baby's heart rate
     *
     * @return the heartRate
     */
    public int getHeartRate () {
        return heartRate;
    }

    /**
     * Sets the baby's heart rate
     *
     * @param heartRate
     *            the heartRate to set
     */
    public void setHeartRate ( final int heartRate ) {
        this.heartRate = heartRate;
    }

    /**
     * Returns the systolic blood pressure of the baby
     *
     * @return the systolic
     */
    public int getSystolic () {
        return systolic;
    }

    /**
     * Sets the systolic blood pressure of the baby
     *
     * @param systolic
     *            the systolic to set
     */
    public void setSystolic ( final int systolic ) {
        this.systolic = systolic;
    }

    /**
     * Returns the diastolic blood pressure of the baby
     *
     * @return the diastolic
     */
    public int getDiastolic () {
        return diastolic;
    }

    /**
     * Sets the diastolic blood pressure of the baby
     *
     * @param diastolic
     *            the diastolic to set
     */
    public void setDiastolic ( final int diastolic ) {
        this.diastolic = diastolic;
    }

    /**
     * Returns the delivery method
     *
     * @return the deliverymethod
     */
    public DeliveryMethod getDeliverymethod () {
        return deliverymethod;
    }

    /**
     * Sets the delivery method
     *
     * @param deliverymethod
     *            the deliverymethod to set
     */
    public void setDeliverymethod ( final DeliveryMethod deliverymethod ) {
        this.deliverymethod = deliverymethod;
    }

    /**
     * Returns the first name of the twin
     *
     * @return the firstnameTwin
     */
    public String getFirstnameTwin () {
        return firstnameTwin;
    }

    /**
     * Sets the first name of the twin
     *
     * @param firstnameTwin
     *            the firstnameTwin to set
     */
    public void setFirstnameTwin ( final String firstnameTwin ) {
        this.firstnameTwin = firstnameTwin;
    }

    /**
     * Returns the last name of the twin
     *
     * @return the lastnameTwin
     */
    public String getLastnameTwin () {
        return lastnameTwin;
    }

    /**
     * Sets the last name of the twin
     *
     * @param lastnameTwin
     *            the lastnameTwin to set
     */
    public void setLastnameTwin ( final String lastnameTwin ) {
        this.lastnameTwin = lastnameTwin;
    }

    /**
     * Returns the weight of the twin
     *
     * @return the weightTwin
     */
    public double getWeightTwin () {
        return weightTwin;
    }

    /**
     * Sets the weight of the twin
     *
     * @param weightTwin
     *            the weightTwin to set
     */
    public void setWeightTwin ( final double weightTwin ) {
        this.weightTwin = weightTwin;
    }

    /**
     * Returns the length of the twin
     *
     * @return the lengthTwin
     */
    public double getLengthTwin () {
        return lengthTwin;
    }

    /**
     * Sets the length of the twin
     *
     * @param lengthTwin
     *            the lengthTwin to set
     */
    public void setLengthTwin ( final double lengthTwin ) {
        this.lengthTwin = lengthTwin;
    }

    /**
     * Returns the heart rate of the twin
     *
     * @return the heartRateTwin
     */
    public int getHeartRateTwin () {
        return heartRateTwin;
    }

    /**
     * Sets the heart rate of the twin
     *
     * @param heartRateTwin
     *            the heartRateTwin to set
     */
    public void setHeartRateTwin ( final int heartRateTwin ) {
        this.heartRateTwin = heartRateTwin;
    }

    /**
     * Returns the systolic blood pressure of the twin
     *
     * @return the systolicTwin
     */
    public int getSystolicTwin () {
        return systolicTwin;
    }

    /**
     * Sets the systolic blood pressure of the twin
     *
     * @param systolicTwin
     *            the systolicTwin to set
     */
    public void setSystolicTwin ( final int systolicTwin ) {
        this.systolicTwin = systolicTwin;
    }

    /**
     * Returns the diastolic blood pressure of the twin
     *
     * @return the diastolicTwin
     */
    public int getDiastolicTwin () {
        return diastolicTwin;
    }

    /**
     * Sets the diastolic blood pressure of the twin
     *
     * @param diastolicTwin
     *            the diastolicTwin to set
     */
    public void setDiastolicTwin ( final int diastolicTwin ) {
        this.diastolicTwin = diastolicTwin;
    }

    /**
     * Returns the delivery method of the twin
     *
     * @return the deliverymethodTwin
     */
    public DeliveryMethod getDeliverymethodTwin () {
        return deliverymethodTwin;
    }

    /**
     * Sets the delivery method of the twin
     *
     * @param deliverymethodTwin
     *            the deliverymethodTwin to set
     */
    public void setDeliverymethodTwin ( final DeliveryMethod deliverymethodTwin ) {
        this.deliverymethodTwin = deliverymethodTwin;
    }

    /**
     * Update current values
     *
     * @param form
     *            the form with the new values
     */
    public void updateValues ( final LaborDeliveryForm form ) {
        setPatient( form.getPatient() );
        setLaborDate( ZonedDateTime.parse( form.getLaborDate() ) );
        setDeliveryDate( ZonedDateTime.parse( form.getDeliveryDate() ) );
        setFirstname( form.getFirstname() );
        setLastname( form.getLastname() );
        setWeight( form.getWeight() );
        setLength( form.getLength() );
        setHeartRate( form.getHeartRate() );
        setSystolic( form.getSystolic() );
        setDiastolic( form.getDiastolic() );
        setDeliverymethod( form.getDeliverymethod() );
        setFirstnameTwin( form.getFirstnameTwin() );
        setLastnameTwin( form.getLastnameTwin() );
        setWeightTwin( form.getWeightTwin() );
        setLengthTwin( form.getLengthTwin() );
        setHeartRateTwin( form.getHeartRateTwin() );
        setSystolicTwin( form.getSystolicTwin() );
        setDiastolicTwin( form.getDiastolicTwin() );
        setDeliverymethodTwin( form.getDeliverymethodTwin() );

    }

    /**
     * Returns all the reports
     *
     * @return list of all the labor and delivery reports
     */
    @SuppressWarnings ( "unchecked" )
    public static List<LaborDeliveryReport> getAllReports () {
        final List<LaborDeliveryReport> visits = (List<LaborDeliveryReport>) getAll( LaborDeliveryReport.class );
        visits.sort( ( x1, x2 ) -> x1.getDeliveryDate().compareTo( x2.getDeliveryDate() ) );
        return visits;
    }

}
