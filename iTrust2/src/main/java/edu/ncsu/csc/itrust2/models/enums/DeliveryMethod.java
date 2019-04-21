package edu.ncsu.csc.itrust2.models.enums;

import com.google.gson.annotations.SerializedName;

/**
 * Enum for different methods of delivery
 *
 * @author Ben Johnson (bfjohns4), Chee Ng (cwng)
 *
 */
public enum DeliveryMethod {
    /**
     * Vaginal Delivery
     */
    @SerializedName ( "Vaginal Delivery" )
    VaginalDelivery,
    /**
     * Caesarean Section
     */
    @SerializedName ( "Caesarean Section" )
    CaesareanSection,
    /**
     * Miscarriage
     */
    @SerializedName ( "Miscarriage" )
    Miscarriage,

    /**
     * Vaginal Delivery for Twin
     */
    @SerializedName ( "Vaginal Delivery " )
    VaginalDeliveryTwin,
    /**
     * Caesarean Section for Twin
     */
    @SerializedName ( "Caesarean Section " )
    CaesareanSectionTwin,
    /**
     * Miscarriage for Twin
     */
    @SerializedName ( "Miscarriage " )
    MiscarriageTwin
}
