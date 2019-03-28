package edu.ncsu.csc.itrust2.forms.hcp;

import javax.validation.constraints.Min;

import edu.ncsu.csc.itrust2.models.persistent.ObstetricsOfficeVisit;

/**
 * Form that the doctor will fill out for creating an obstetric office visit
 *
 * @author msned
 *
 */
public class ObstetricsVisitForm extends OfficeVisitForm {

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
     * empty constructor for hibernate
     */
    public ObstetricsVisitForm () {
        // EMTPY
    }

    /**
     * Creates the form from the office visit
     *
     * @param obV
     *            an object representation of
     */
    public ObstetricsVisitForm ( final ObstetricsOfficeVisit obV ) {
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

}
