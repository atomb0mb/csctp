package edu.ncsu.csc.itrust2.forms.hcp;

import org.hibernate.validator.constraints.NotEmpty;

import edu.ncsu.csc.itrust2.models.enums.DeliveryMethod;

/**
 * The form allows a Pregnancy object to be created and stored in the database.
 *
 * @author Ben Johnson (bfjohns4)
 *
 */
public class PregnancyForm {

    /**
     * Integer representation year pregnancy was conceived
     */
    @NotEmpty
    private Integer        conceptionYear;

    /**
     * Integer representation of # of weeks patient was pregnant
     */
    @NotEmpty
    private Integer        numWeeksPregnant;

    /**
     * Number of hours patient was in labor
     */
    @NotEmpty
    private Integer        numHoursInLabor;

    /**
     * Type of method baby was delivered ( 1 of 3 )
     */
    @NotEmpty
    private DeliveryMethod deliveryMethod;

    /**
     * Boolean representation for if the mother was pregnant with twins or not.
     * True = twins, False = not twins
     */
    @NotEmpty
    private Boolean        isTwins;

    /**
     * Empty constructor to make a PregnancyForm for the user to fill out
     */
    public PregnancyForm () {
    }

    /**
     * Return year of conception for pregnancy
     *
     * @return year of conception
     */
    public Integer getConceptionYear () {
        return conceptionYear;
    }

    /**
     * Set year of conception for this pregnancy
     *
     * @param conceptionYear
     *            - year of conception (as an integer)
     */
    public void setConceptionYear ( Integer conceptionYear ) {
        this.conceptionYear = conceptionYear;
    }

    /**
     * return number of weeks patient was pregnant with the pregnancy detailed
     * in this form
     *
     * @return number of weeks pregnant
     */
    public Integer getNumWeeksPregnant () {
        return numWeeksPregnant;
    }

    /**
     * Set the number of weeks the patient was pregnant
     *
     * @param numWeeksPregnant
     *            - number of weeks patient was pregnant
     */
    public void setNumWeeksPregnant ( Integer numWeeksPregnant ) {
        this.numWeeksPregnant = numWeeksPregnant;
    }

    /**
     * Return number of hours patient was in labor
     *
     * @return number of hours patient was in labor
     */
    public Integer getNumHoursInLabor () {
        return numHoursInLabor;
    }

    /**
     * Sets the number of hours the patient was in labor
     *
     * @param numHoursInLabor
     *            - number of hours patient was in labor (whole integer)
     */
    public void setNumHoursInLabor ( Integer numHoursInLabor ) {
        this.numHoursInLabor = numHoursInLabor;
    }

    /**
     * Returns delivery method of the labor that ended this pregnancy.
     *
     * @return delivery method
     */
    public DeliveryMethod getDeliveryMethod () {
        return deliveryMethod;
    }

    /**
     * Sets the delivery method of the labor that ended this pregnancy. One of
     * three options from the DeliveryMethod enumeration
     *
     * @param deliveryMethod
     *            - delivery method
     */
    public void setDeliveryMethod ( DeliveryMethod deliveryMethod ) {
        this.deliveryMethod = deliveryMethod;
    }

    /**
     * Returns if the patient was pregnant with twins or not. True - pregnant
     * with twins. False - not twins
     *
     * @return T/F on if the patient was pregnant with twins
     */
    public Boolean getIsTwins () {
        return isTwins;
    }

    /**
     * Sets if the patient was pregnant with twins or not
     *
     * @param isTwins
     *            - boolean T/F: true if pregnant with twins, false if not
     */
    public void setIsTwins ( Boolean isTwins ) {
        this.isTwins = isTwins;
    }

}
