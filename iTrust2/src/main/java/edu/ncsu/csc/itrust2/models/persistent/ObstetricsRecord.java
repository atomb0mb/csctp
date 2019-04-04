package edu.ncsu.csc.itrust2.models.persistent;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Vector;

import javax.persistence.Basic;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.criterion.Criterion;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateConverter;

import com.google.gson.annotations.JsonAdapter;

import edu.ncsu.csc.itrust2.adapters.LocalDateAdapter;
import edu.ncsu.csc.itrust2.forms.hcp.ObstetricsRecordForm;

/**
 * ObstetricsRecord stores the previous pregnancies, as well as tracks ongoing
 * ones, for female patients within iTrust2. An OBGYN user is allowed to create
 * new ObstetricsRecords, but other HCP's can view the records, and patients can
 * view their own records
 *
 * @author Ben Johnson (bfjohns4)
 *
 */
@Entity
@Table ( name = "ObstetricsRecord" )
public class ObstetricsRecord extends DomainObject<ObstetricsRecord> implements Serializable {

    /**
     * Randomly generated ID.
     */
    private static final long serialVersionUID = -3558282960418343804L;

    /**
     * The username of the patient for this ObstetricsRecord
     */
    @Length ( max = 20 )
    private String            patient;

    /**
     * The id of this ObstetricsRecord
     */
    @Id
    @GeneratedValue ( strategy = GenerationType.AUTO )
    private Long              id;

    /**
     * The date as milliseconds since the last menstrual period of the patient
     */
    @Basic
    // Allows the field to show up nicely in the database
    @Convert ( converter = LocalDateConverter.class )
    @JsonAdapter ( LocalDateAdapter.class )
    private LocalDate         lastMenstrualPeriod;

    /**
     * The estimated due date of the ongoing pregnancy (in milliseconds)
     */
    @Basic
    // Allows the field to show up nicely in the database
    @Convert ( converter = LocalDateConverter.class )
    @JsonAdapter ( LocalDateAdapter.class )
    private LocalDate         estDueDate;

    /**
     * Get a specific ObstetricsRecord by the database ID
     *
     * @param id
     *            the database ID
     * @return the specific ObstetricsRecord object with the desired ID
     */
    public static ObstetricsRecord getById ( final Long id ) {
        try {
            return (ObstetricsRecord) getWhere( ObstetricsRecord.class, eqList( ID, id ) ).get( 0 );
        }
        catch ( final Exception e ) {
            return null;
        }
    }

    /**
     * Returns the ObstetricsRecord for a patient based on username of the
     * patient
     *
     * @param patient
     *            the username of the patient whose ObstetricsRecord is being
     *            searched for
     * @return the ObstetricsRecord for the patient
     */
    @SuppressWarnings ( "unchecked" )
    public static List<ObstetricsRecord> getByPatient ( final String patient ) {
        final Vector<Criterion> criteria = new Vector<Criterion>();
        criteria.add( eq( "patient", patient ) );

        return (List<ObstetricsRecord>) getWhere( ObstetricsRecord.class, criteria );
    }

    /**
     * Empty contructor
     *
     */
    public ObstetricsRecord () {
    }

    /**
     * Constructor that creates a new ObstetricsRecord using an
     * ObstetricsRecordForm to fill in data points
     *
     * @param orf
     *            the ObstetricsRecordForm to parse
     */
    public ObstetricsRecord ( final ObstetricsRecordForm orf ) {
        // 2007-12-03 YYYY-MM-DD
        setLastMenstrualPeriod( LocalDate.parse( orf.getLastMenstrualPeriod() ) );
    }

    /**
     * Reads a given ObstetricsRecordForm and uses that to update data points
     *
     * @param orf
     *            the ObstetricsRecordForm to parse
     */
    // public void readObstetricsRecordForm ( ObstetricsRecordForm orf ) {

    // setLastMenstrualPeriod( LocalDate.parse( orf.getLastMenstrualPeriod() )
    // );
    // }

    /**
     * Sets the last menstrual period for an ongoing pregnancy
     *
     * @param date
     *            the date of the LMP
     */
    public void setLastMenstrualPeriod ( final LocalDate date ) {
        lastMenstrualPeriod = date;

        estDueDate = lastMenstrualPeriod.plusDays( 280 );
    }

    /**
     * Returns the estimated due date for the ongoing pregnancy
     *
     * @return due date
     */
    public LocalDate getEstimatedDueDate () {
        return estDueDate;
    }

    /**
     * Returns the LMP of the ongoing pregnancy
     *
     * @return LMP
     */
    public LocalDate getLastMenstrualPeriod () {
        return lastMenstrualPeriod;
    }

    /**
     * Get the ID of this ObstetricsRecord
     *
     * @return the ID of this ObstetricsRecord
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Sets the id of this ObstetricsRecord
     *
     * @param id
     *            the id to set
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Gets the patient for this ObstetricsRecord
     *
     * @return the patient
     */
    public String getPatient () {
        return patient;
    }

    /**
     * Sets the patient for this ObstetricsRecord
     *
     * @param patient
     *            the patient to set
     */
    public void setPatient ( final String patient ) {
        this.patient = patient;
    }

}
