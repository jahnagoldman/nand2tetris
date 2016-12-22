package edu.uchicago.jagoldman;

/**
 * Created by jahnaariellegoldman on 10/26/16.
 */
public class Code {

    //returns binary code of dest mnemonic
    public static String dest(String strMnemonic) {
        String strDest;
        if (strMnemonic.equals("null")) {
            strDest = "000";
        } else if (strMnemonic.equals("M")) {
            strDest = "001";
        } else if (strMnemonic.equals("D")) {
            strDest = "010";
        } else if (strMnemonic.equals("MD") || strMnemonic.equals("DM")) {
            strDest = "011";
        } else if (strMnemonic.equals("A")) {
            strDest = "100";
        } else if (strMnemonic.equals("AM") || strMnemonic.equals("MA")) {
            strDest = "101";
        } else if (strMnemonic.equals("AD") || strMnemonic.equals("DA")) {
            strDest = "110";
        } else if (strMnemonic.equals("AMD") || strMnemonic.equals("MAD") || strMnemonic.equals("ADM") ||
                strMnemonic.equals("MDA") || strMnemonic.equals("DMA") || strMnemonic.equals("DAM")) {
            strDest = "111";
        } else {
            strDest = "?";
        }
        return strDest;

    }

    //returns binary code of comp mnemonic
    public static String comp(String strMnemonic) {
        String strComp;
        if (strMnemonic.equals("0")) {
            strComp = "0101010";
        } else if (strMnemonic.equals("1")) {
            strComp = "0111111";
        } else if (strMnemonic.equals("-1")) {
            strComp = "0111010";
        } else if (strMnemonic.equals("D")) {
            strComp = "0001100";
        } else if (strMnemonic.equals("A")) {
            strComp = "0110000";
        } else if (strMnemonic.equals("!D")) {
            strComp = "0001101";
        } else if (strMnemonic.equals("!A")) {
            strComp = "0110001";
        } else if (strMnemonic.equals("-D")) {
            strComp = "0001111";
        } else if (strMnemonic.equals("-A")) {
            strComp = "0110011";
        } else if (strMnemonic.equals("D+1") || strMnemonic.equals("1+D")) {
            strComp = "0011111";
        } else if (strMnemonic.equals("A+1") || strMnemonic.equals("1+A")) {
            strComp = "0110111";
        } else if (strMnemonic.equals("D-1")) {
            strComp = "0001110";
        } else if (strMnemonic.equals("A-1")) {
            strComp = "0110010";
        } else if (strMnemonic.equals("D+A") || strMnemonic.equals("A+D")) {
            strComp = "0000010";
        } else if (strMnemonic.equals("D-A")) {
            strComp = "0010011";
        } else if (strMnemonic.equals("A-D")) {
            strComp = "0000111";
        } else if (strMnemonic.equals("D&A") || strMnemonic.equals("A&D")) {
            strComp = "0000000";
        } else if (strMnemonic.equals("D|A") || strMnemonic.equals("A|D")) {
            strComp = "0010101";
        } else if (strMnemonic.equals("M")) {
            strComp = "1110000";
        } else if (strMnemonic.equals("!M")) {
            strComp = "1110001";
        } else if (strMnemonic.equals("-M")) {
            strComp = "1110011";
        } else if (strMnemonic.equals("M+1") || strMnemonic.equals("1+M")) {
            strComp = "1110111";
        } else if (strMnemonic.equals("M-1")) {
            strComp = "1110010";
        } else if (strMnemonic.equals("D+M") || strMnemonic.equals("M+D")) {
            strComp = "1000010";
        } else if (strMnemonic.equals("D-M")) {
            strComp = "1010011";
        } else if (strMnemonic.equals("M-D")) {
            strComp = "1000111";
        } else if (strMnemonic.equals("D&M") || strMnemonic.equals("M&D")) {
            strComp = "1000000";
        } else if (strMnemonic.equals("D|M") || strMnemonic.equals("M|D")) {
            strComp = "1010101";
        } else {
            strComp = "?";
        }


        return strComp;
    }

    //returns binary code of jump mnemonic
    public static String jump(String strMnemonic) {
        String strJump;
        if (strMnemonic.equals("null")) {
            strJump = "000";
        } else if (strMnemonic.equals("JGT")) {
            strJump = "001";
        } else if (strMnemonic.equals("JEQ")) {
            strJump = "010";
        } else if (strMnemonic.equals("JGE")) {
            strJump = "011";
        } else if (strMnemonic.equals("JLT")) {
            strJump = "100";
        } else if (strMnemonic.equals("JNE")) {
            strJump = "101";
        } else if (strMnemonic.equals("JLE")) {
            strJump = "110";
        } else if (strMnemonic.equals("JMP")) {
            strJump = "111";
        } else {
            strJump = "?";
        }

        return strJump;
    }
}
