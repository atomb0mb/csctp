package edu.ncsu.csc.itrust2.models.persistent;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Vector;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.criterion.Criterion;
import org.hibernate.validator.constraints.Length;

import edu.ncsu.csc.itrust2.forms.hcp.PregnancyForm;
import edu.ncsu.csc.itrust2.models.enums.DeliveryMethod;

/**
 * Pregnancy instances are stored alongside ObstetricsRecords to represent
 * previous pregnancies a female patient has had.
 *
 * @author Ben Johnson (bfjohns4)
 *
 */
@Entity
@Table ( name = "Pregnancy" )
public class Pregnancy extends DomainObject<Pregnancy> implements Serializable {

    /**
     * Randomly Generated ID
     */
    private static final long serialVersionUID = 7001968486373369206L;

    /**
     * The id of this Pregnancy
     */
    @Id
    @GeneratedValue ( strategy = GenerationType.AUTO )
    private Long              id;

    /**
     * The username of the patient for this Pregnancy
     */
    @Length ( max = 20 )
    private String            patient;

    // Year the pregnancy was conceived
    @NotNull
    private Integer           conceptionYear;

    // Number of weeks the patient was pregnant during this pregnancy
    @NotNull
    private Integer           numberOfWeeksPregnant;

    // Number of hours the patient was in labor at the end of this pregnancy
    @NotNull
    private Integer           numberOfHoursInLabor;

    // One of three possible methods the baby from this pregnancy was delivered
    @NotNull
    @Enumerated ( EnumType.STRING )
    private DeliveryMethod    deliveryMethod;

    // True if the mother was carrying twins, false if not
    @NotNull
    private Boolean           twins;

    /**
     * Get a specific pregnancy instance by the database ID
     *
     * @param id
     *            the database ID
     * @return the specific pregnancy with the desired ID
     */
    public static Pregnancy getById ( final Long id ) {
        try {
            return (Pregnancy) getWhere( Pregnancy.class, eqList( ID, id ) ).get( 0 );
        }
        catch ( final Exception e ) {
            return null;
        }
    }

    /**
     * Returns the Pregnancy objects for a patient based on username of the
     * patient
     *
     * @param patient
     *            the username of the patient whose ObstetricsRecord is being
     *            searched for
     * @return the ObstetricsRecord for the patient
     */
    @SuppressWarnings ( "unchecked" )
    public static List<Pregnancy> getByPatient ( final String patient ) {
        final Vector<Criterion> criteria = new Vector<Criterion>();
        criteria.add( eq( "patient", patient ) );

        final List<Pregnancy> list = (List<Pregnancy>) getWhere( Pregnancy.class, criteria );
        list.sort( new PregnancyComparator() );

        return list;
    }

    /**
     * Empty Constructor
     */
    public Pregnancy () {
    }

    /**
     * Initializes a new Pregnancy object using the data points in a
     * PregnancyForm
     *
     * @param pForm
     *            - the form to read
     */
    public Pregnancy ( final PregnancyForm pForm ) {
        setConceptionYear( pForm.getConceptionYear() );
        setNumWeeksPregnant( pForm.getNumWeeksPregnant() );
        setNumHoursLabor( pForm.getNumHoursInLabor() );
        setDeliverMethod( pForm.getDeliveryMethod() );
        setTwins( pForm.getIsTwins() );
    }

    /**
     * Sets the year of conception for this pregnancy
     *
     * @param year
     *            - year the pregnancy was conceived
     */
    public void setConceptionYear ( final Integer year ) {
        if ( year > LocalDate.now().getYear() ) {
            throw new IllegalArgumentException( "Year must be on or before current year" );
        }
        conceptionYear = year;
    }

    /**
     * Set the number of weeks the patient was pregnant
     *
     * @param nwp
     *            - integer for the number of weeks pregnant
     */
    public void setNumWeeksPregnant ( final Integer nwp ) {
        if ( nwp < 1 ) {
            throw new IllegalArgumentException( "Number of weeks pregnant must be a positive, nonzero number" );
        }
        numberOfWeeksPregnant = nwp;
    }

    /**
     * Set the number of hours the patient was in labor for this pregnancy
     *
     * @param nhl
     *            - integer for number of hours in labor
     */
    public void setNumHoursLabor ( final Integer nhl ) {
        if ( nhl < 1 ) {
            throw new IllegalArgumentException( "Number of hours in labor must be a positive, nonzero number" );
        }
        numberOfHoursInLabor = nhl;
    }

    /**
     * Marks how the baby from this pregnancy was delivered
     *
     * @param dm
     *            - one of three from the DeliveryMethod enumeration
     */
    public void setDeliverMethod ( final DeliveryMethod dm ) {
        deliveryMethod = dm;
    }

    /**
     * Marks the pregnancy for twins or not
     *
     * @param twins
     *            - TRUE if the patient was pregnant with twins, FALSE if not
     */
    public void setTwins ( final Boolean twins ) {
        this.twins = twins;
    }

    /**
     * Returns the year the baby was conceived
     *
     * @return year of conception for this pregnancy
     */
    public Integer getConceptionYear () {
        return conceptionYear;
    }

    /**
     * Returns number of weeks the patient was pregnant during this term
     *
     * @return number of weeks pregnant
     */
    public Integer getNumberOfWeeksPregnant () {
        return numberOfWeeksPregnant;
    }

    /**
     * Returns number of hours spent in labor for this pregnancy
     *
     * @return number of hours in labor
     */
    public Integer getNumberOfHoursInLabor () {
        return numberOfHoursInLabor;
    }

    /**
     * Returns the method of delivery for the baby from this pregnancy
     *
     * @return method of delivery for baby
     */
    public DeliveryMethod getDeliveryMethod () {
        return deliveryMethod;
    }

    /**
     * Returns true if this pregnancy had twins, false if not
     *
     * @return T/F if patient was pregnant with twins
     */
    public Boolean getIsTwins () {
        return twins;
    }

    /**
     * Gets the patient for this Pregnancy
     *
     * @return the patient
     */
    public String getPatient () {
        return patient;
    }

    /**
     * Sets the patient for this Pregnancy
     *
     * @param patient
     *            the patient to set
     */
    public void setPatient ( final String patient ) {
        this.patient = patient;
    }

    /**
     * Get the ID of this Pregnancy
     *
     * @return the ID of this Pregnancy
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Sets the id of this Pregnancy
     *
     * @param id
     *            the id to set
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Compares this to another Pregnancy based on conception year
     *
     * @param po
     * @return -1 if this comes before po, 1 if it comes after, 0 if same year
     */
    public int compare ( Pregnancy po ) {
        if ( getConceptionYear() < po.getConceptionYear() ) {
            return 1;
        }
        else if ( getConceptionYear() > po.getConceptionYear() ) {
            return -1;
        }

        return 0;
    }

}
