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
import javax.validation.constraints.NotNull;

import org.hibernate.criterion.Criterion;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateConverter;

import com.google.gson.annotations.JsonAdapter;

import edu.ncsu.csc.itrust2.adapters.LocalDateAdapter;
import edu.ncsu.csc.itrust2.forms.hcp.ObstetricsRecordForm;
import edu.ncsu.csc.itrust2.models.enums.BloodType;
import edu.ncsu.csc.itrust2.models.enums.DeliveryMethod;

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
    private static final long serialVersionUID       = -3558282960418343804L;

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

    // Stores # of weeks the patient has been pregnant
    @NotNull
    private Integer           numWeeksPregnant;

    /**
     * The estimated due date of the ongoing pregnancy (in milliseconds)
     */
    @Basic
    // Allows the field to show up nicely in the database
    @Convert ( converter = LocalDateConverter.class )
    @JsonAdapter ( LocalDateAdapter.class )
    private LocalDate         estDueDate;

    /**
     * Flags to mark special conditions for the patient ( For the Pregnancy
     * Flags Extra Credit )
     */
    // Set automatically if the blood pressure of the patient is
    // > 140 systolic or > 90 diastolic
    // Set whenever updated with an Office Visit is recorded
    private Boolean           highBloodPressure      = false;

    // Set automatically if the patient is > 35 years old
    // Set when constructed
    private Boolean           advMaternalAge         = false;

    // Set automatically if patient has had a miscarriage before
    // Set when constructed
    private Boolean           miscarriagePotential   = false;

    // Set automatically if the fetal hear rate during any visit is
    // < 105 or > 170
    // Updated when a new Obstetrics Office Visit is recorded
    private Boolean           abnormalFetalHeartRate = false;

    // True if mother is pregnant with twins. False if not
    // Updated when a new Obstetrics Office Visit is recorded
    private Boolean           isTwins                = false;

    // Set automatically if mother has negative blood type
    // Set when constructed
    private Boolean           rhNegative             = false;

    // store the pre-pregnancy BMI of the patient
    // Updated when a new Obstetrics Office Visit is recorded
    private Double            bmi                    = -1.0;

    // String for reference of patient's recommended weight gain while pregnant
    // Updated when a new Obstetrics Office Visit is recorded
    private String            recommendedWeightGain  = null;

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
    /*
     * @SuppressWarnings ( "unchecked" ) public static List<ObstetricsRecord>
     * getByPatient ( final String patient ) { final Vector<Criterion> criteria
     * = new Vector<Criterion>(); criteria.add( eq( "patient", patient ) );
     * final List<ObstetricsRecord> list = (List<ObstetricsRecord>) getWhere(
     * ObstetricsRecord.class, criteria ); return list; }
     */

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
    public static ObstetricsRecord getByPatient ( final String patient ) {
        final Vector<Criterion> criteria = new Vector<Criterion>();
        criteria.add( eq( "patient", patient ) );

        final List<ObstetricsRecord> list = (List<ObstetricsRecord>) getWhere( ObstetricsRecord.class, criteria );

        if ( list.isEmpty() ) {
            return null;
        }

        return list.get( 0 );
    }

    /**
     * Empty constructor
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
        setLastMenstrualPeriod( LocalDate.parse( orf.getLastMenstrualPeriod() ) );
    }

    /**
     * Constructor that creates a new ObstetricsRecord using an
     * ObstetricsRecordForm to fill in data points, as well as a patient
     * username
     *
     * @param patient
     *            username of patient
     * @param orf
     *            the ObstetricsRecordForm to parse
     */
    public ObstetricsRecord ( final String patient, final ObstetricsRecordForm orf ) {

        setLastMenstrualPeriod( LocalDate.parse( orf.getLastMenstrualPeriod() ) );

        setPatient( patient );
    }

    /**
     * Called whenever an obstetrics office visit is recorded, changes any
     * necessary flags based on data from the visit
     *
     * @param oov
     *            the record from the most recent health check-up
     *
     */
    public void updateObstetricsRecord ( final ObstetricsOfficeVisit oov ) {

        setIsTwins( oov.isTwins() );

        if ( oov.getBasicHealthMetrics().getDiastolic() > 90 || oov.getBasicHealthMetrics().getSystolic() > 140 ) {
            highBloodPressure = true;
        }
        else {
            highBloodPressure = false;
        }

        if ( oov.getFetalHeartRate() < 105 || oov.getFetalHeartRate() > 170 ) {
            abnormalFetalHeartRate = true;
        }
        else {
            abnormalFetalHeartRate = false;
        }
    }

    /**
     * Sets the last menstrual period for an ongoing pregnancy
     *
     * @param date
     *            the date of the LMP
     */
    public void setLastMenstrualPeriod ( final LocalDate date ) {

        if ( date.isAfter( LocalDate.now() ) ) {
            throw new IllegalArgumentException( "Date must be before current date" );
        }

        lastMenstrualPeriod = date;

        int today = LocalDate.now().getDayOfYear();

        if ( today < date.getDayOfYear() ) {
            today += 365;
        }

        numWeeksPregnant = ( today - date.getDayOfYear() ) / 7;

        estDueDate = lastMenstrualPeriod.plusDays( 280 );
    }

    /**
     * Sets the patient for this ObstetricsRecord
     *
     * @param patient
     *            the patient to set
     */
    public void setPatient ( final String patient ) {
        this.patient = patient;

        if ( Patient.getByName( patient ) != null ) {
            // Set flags using info we can get from patient's user data
            setPatientFlags();

            // Calculate the patient's BMI and then set the recommended weight
            // gain for this term
            calculateBMI();
            setRecommendedWeightGain();
        }
    }

    /**
     * Set the flags that can be set using the patient's information
     *
     */
    public void setPatientFlags () {
        final Patient user = Patient.getByName( patient );

        if ( user == null ) {
            return;
        }

        final Integer years = LocalDate.now().getYear() - user.getDateOfBirth().getYear();

        if ( years > 35 ) {
            advMaternalAge = true;
        }
        else if ( years == 35 ) {
            final Integer month = LocalDate.now().getMonthValue() - user.getDateOfBirth().getMonthValue();
            if ( month < 0 ) {
                advMaternalAge = true;
            }
            else if ( month == 0 ) {
                final Integer day = LocalDate.now().getDayOfMonth() - user.getDateOfBirth().getDayOfMonth();
                if ( day <= 0 ) {
                    advMaternalAge = true;
                }
            }
        }

        final List<Pregnancy> pList = Pregnancy.getByPatient( patient );
        for ( final Pregnancy p : pList ) {
            if ( p.getDeliveryMethod() == DeliveryMethod.Miscarriage ) {
                miscarriagePotential = true;
                break;
            }
        }

        final BloodType userBloodType = user.getBloodType();
        if ( userBloodType == BloodType.ABNeg || userBloodType == BloodType.ANeg || userBloodType == BloodType.BNeg
                || userBloodType == BloodType.ONeg ) {
            rhNegative = true;
        }

    }

    /**
     * Calculates the Body Mass Index for the patient
     *
     */
    public void calculateBMI () {
        final List<OfficeVisit> visits = OfficeVisit.getForPatient( patient );
        if ( !visits.isEmpty() ) {
            OfficeVisit mostRecentVisit = visits.get( 0 );
            for ( final OfficeVisit vs : visits ) {
                if ( vs.getDate().isAfter( mostRecentVisit.getDate() ) ) {
                    mostRecentVisit = vs;
                }
            }

            bmi = mostRecentVisit.getBasicHealthMetrics().getWeight()
                    / Math.pow( mostRecentVisit.getBasicHealthMetrics().getHeight(), 2 );
        }
    }

    /**
     * Sets a flag telling the amount of weight the patient should put on while
     * pregnant
     *
     */
    public void setRecommendedWeightGain () {
        if ( isTwins && bmi != -1.0 ) {
            if ( bmi < 18.5 ) {
                recommendedWeightGain = "Recommended Weight Gain: 28 to 40 lbs";
            }
            else if ( bmi < 24.9 ) {
                recommendedWeightGain = "Recommended Weight Gain: 25 to 35 lbs";
            }
            else if ( bmi < 29.9 ) {
                recommendedWeightGain = "Recommended Weight Gain: 15 to 25 lbs";
            }
            else {
                recommendedWeightGain = "Recommended Weight Gain: 11 to 20 lbs";
            }
        }
        else if ( bmi != -1.0 ) {
            if ( bmi < 24.9 ) {
                recommendedWeightGain = "Recommended Weight Gain: 37 to 54 lbs";
            }
            else if ( bmi < 29.9 ) {
                recommendedWeightGain = "Recommended Weight Gain: 31 to 50 lbs";
            }
            else {
                recommendedWeightGain = "Recommended Weight Gain: 25 to 42 lbs";
            }
        }
    }

    /**
     * Sets the twins flag for this ongoing pregnancy, as well as updates the
     * recommended weight gain
     *
     * @param b
     *            True if pregnant with twins, false if not
     */
    public void setIsTwins ( final Boolean b ) {
        isTwins = b;
        setRecommendedWeightGain();
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
     * Return number of weeks patient has been pregnant
     *
     * @return number of weeks pregnant
     */
    public Integer getNumWeeksPregnant () {

        int today = LocalDate.now().getDayOfYear();

        if ( today < lastMenstrualPeriod.getDayOfYear() ) {
            today += 365;
        }

        numWeeksPregnant = ( today - lastMenstrualPeriod.getDayOfYear() ) / 7;

        return numWeeksPregnant;
    }

    /**
     * Returns T/F on if the patient has high blood pressure
     *
     * @return True if patient has high blood pressure, false if not
     */
    public Boolean hasHighBloodPressure () {
        return highBloodPressure;
    }

    /**
     * Returns T/F on if the patient has advanced maternal age ( > 35 yrs old )
     *
     * @return True if patient is > 35 years old, false if not
     */
    public Boolean hasAdvancedMaternalAge () {
        return advMaternalAge;
    }

    /**
     * Returns T/F on if the patient is at risk of a miscarriage
     *
     * @return True if patient has had a miscarriage before, false if not
     */
    public Boolean hasMiscarriagePotential () {
        return miscarriagePotential;
    }

    /**
     * Returns T/F on if the fetus's heart rate is < 105 or > 170
     *
     * @return True if fetus has abnormal heart rate
     */
    public Boolean hasAbnormalFetalHeartRate () {
        return abnormalFetalHeartRate;
    }

    /**
     * Returns T/F on if mother is pregnant w/ twins
     *
     * @return True if pregnant with twins, false if not
     */
    public Boolean isTwins () {
        return isTwins;
    }

    /**
     * Returns T/F on if mother has a negative blood type
     *
     * @return True if mother's blood type is negative (A-, B-, AB-, O-)
     */
    public Boolean isRHNegative () {
        return rhNegative;
    }

    /**
     * Returns the recommended weight gain String for the mother
     *
     * @return recommeneded weight gain
     */
    public String getRecommendedWeightGain () {
        return recommendedWeightGain;
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
     * Gets the patient for this ObstetricsRecord
     *
     * @return the patient
     */
    public String getPatient () {
        return patient;
    }

}
