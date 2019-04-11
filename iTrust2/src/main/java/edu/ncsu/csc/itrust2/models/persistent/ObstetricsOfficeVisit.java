package edu.ncsu.csc.itrust2.models.persistent;

import java.text.ParseException;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;

import edu.ncsu.csc.itrust2.forms.hcp.ObstetricsVisitForm;

/**
 * The object representation of an obstetrics office visit
 *
 * @author msned
 *
 */
@Entity
@Table ( name = "ObstetricsOfficeVisit" )
public class ObstetricsOfficeVisit extends OfficeVisit {
    /* Number of weeks the patient has been pregnant */
    private Integer numOfWeeksPreg;

    /* The fetal heart rate of baby in beats per minute */
    @Min ( 0 )
    private Integer fetalHeartRate;

    /* The fundal height in centimeters */
    @Min ( 0 )
    private Double  fundalHeight;

    /* Whether the pregnancy is for twins */
    private boolean twins;

    /* Flag for whether the placenta is low lying */
    private boolean lowLyingPlacenta;

    /**
     * empty constructor for hibernate/thymeleaf
     */
    public ObstetricsOfficeVisit () {
        // EMTPY
    }

    /**
     * Creates the object from the provided form
     *
     * @param obV
     *            the form that contains values for an office visit
     */
    public ObstetricsOfficeVisit ( final ObstetricsVisitForm obV ) throws ParseException {
        super( obV );
        setNumOfWeeksPreg( obV.getNumOfWeeksPreg() );
        setFetalHeartRate( obV.getFetalHeartRate() );
        setFundalHeight( obV.getFundalHeight() );
        setTwins( obV.isTwins() );
        setLowLyingPlacenta( obV.isLowLyingPlacenta() );

    }

    /**
     * Returns number of weeks pregnant
     *
     * @return the numOfWeeksPreg
     */
    public Integer getNumOfWeeksPreg () {
        return numOfWeeksPreg;
    }

    /**
     * Sets the time patient is pregnant
     *
     * @param numOfWeeksPreg
     *            the numOfWeeksPreg to set
     */
    public void setNumOfWeeksPreg ( final Integer numOfWeeksPreg ) {
        this.numOfWeeksPreg = numOfWeeksPreg;
    }

    /**
     * Returns the fetal heart rate
     *
     * @return the fetalHeartRate
     */
    public Integer getFetalHeartRate () {
        return fetalHeartRate;
    }

    /**
     * Sets the fetal heart rate
     *
     * @param fetalHeartRate
     *            the fetalHeartRate to set
     */
    public void setFetalHeartRate ( final Integer fetalHeartRate ) {
        this.fetalHeartRate = fetalHeartRate;
    }

    /**
     * Returns the fundal height
     *
     * @return the fundalHeight
     */
    public Double getFundalHeight () {
        return fundalHeight;
    }

    /**
     * Sets the fundal height
     *
     * @param fundalHeight
     *            the fundalHeight to set
     */
    public void setFundalHeight ( final Double fundalHeight ) {
        this.fundalHeight = fundalHeight;
    }

    /**
     * Returns whether the pregnancy is twins
     *
     * @return the twins
     */
    public boolean isTwins () {
        return twins;
    }

    /**
     * Sets whether it is twins
     *
     * @param twins
     *            the twins to set
     */
    public void setTwins ( final boolean twins ) {
        this.twins = twins;
    }

    /**
     * Returns if the placenta is low lying
     *
     * @return the lowLyingPlacenta
     */
    public boolean isLowLyingPlacenta () {
        return lowLyingPlacenta;
    }

    /**
     * Sets whether the placenta is low lying
     *
     * @param lowLyingPlacenta
     *            the lowLyingPlacenta to set
     */
    public void setLowLyingPlacenta ( final boolean lowLyingPlacenta ) {
        this.lowLyingPlacenta = lowLyingPlacenta;
    }

    /**
     * Finds an office visit by given visit ID
     *
     * @param id
     *            the id of the office visit to be returned
     * @return the specified office visit
     */
    public static ObstetricsOfficeVisit getById ( final Long id ) {
        return (ObstetricsOfficeVisit) OfficeVisit.getById( id );
    }

}
