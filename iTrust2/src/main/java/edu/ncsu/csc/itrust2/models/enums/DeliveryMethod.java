package edu.ncsu.csc.itrust2.models.enums;

import com.google.gson.annotations.SerializedName;

/**
 * Enum for different methods of delivery
 *
 * @author Ben Johnson (bfjohns4)
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
    Miscarriage
}
