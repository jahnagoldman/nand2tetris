package edu.uchicago.jagoldman;

/**
 * Created by jahnaariellegoldman on 11/30/16.
 */
public class Symbol {

    private String mType;
    private String mKind;
    private int mNumber;

    public Symbol(String strType, String strKind, int nNumber) {
        mType = strType;
        mKind = strKind;
        mNumber = nNumber;


    }

    public String getType() {
        return mType;
    }


    public String getKind() {
        return mKind;
    }


    public int getNumber() {
        return mNumber;
    }


}
