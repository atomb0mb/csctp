package edu.ncsu.csc.itrust2.models.persistent;

import java.util.Comparator;

/**
 * Comparator class used for sorting Pregnancy objects by year of conception
 *
 * @author Ben Johnson (bfjohns4)
 *
 */
public class PregnancyComparator implements Comparator<Pregnancy> {

    @Override
    public int compare ( Pregnancy p1, Pregnancy p2 ) {
        return p1.compare( p2 );
    }

}
