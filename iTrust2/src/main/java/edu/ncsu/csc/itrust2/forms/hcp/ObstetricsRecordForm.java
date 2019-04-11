package edu.ncsu.csc.itrust2.forms.hcp;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * ObstetricsRecordForm allows for an ObstetricsRecord instance to be created
 * through front end and back end communication
 *
 * @author Ben Johnson (bfjohns4)
 *
 */
public class ObstetricsRecordForm {
    /**
     * The date as milliseconds since patient's last menstrual period
     */
    @NotEmpty
    private String lmpdate;

    /**
     * Empty constructor to make an ObstetricsRecordForm for the user to fill
     * out
     */
    public ObstetricsRecordForm () {
    }

    /**
     * Sets the date passed in as the Last Menstrual Period for this
     * ObstetricsRecordForm
     *
     * @param date
     *            the date to set as the Last Menstrual Period
     */
    public void setLastMenstrualPeriod ( String date ) {
        this.lmpdate = date;
    }

    /**
     * Returns the date of the last menstrual period
     *
     * @return the date of the last menstrual period, as a string
     */
    public String getLastMenstrualPeriod () {
        return lmpdate;
    }

}
