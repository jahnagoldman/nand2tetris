package edu.uchicago.jagoldman;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by jahnaariellegoldman on 10/26/16.
 */
public class Parser {
    private String strCurrCmd;
    private int nCmdNum = 0;
    private ArrayList<String> strInstructions = new ArrayList<>();
    private boolean bFirst = true;

    //opens input file/stream & gets ready to parse
    public Parser(String fileName) {
        try (FileReader fr = new FileReader(fileName)) {
            BufferedReader reader = new BufferedReader(fr);
            String strTemp;
            while ((strTemp = reader.readLine()) != null) {
                String strCommand = stripWhiteSpace(strTemp);
                if (!strCommand.equals("")) {
                    strInstructions.add(strCommand);
                }
            }

            fr.close();
            reader.close();


        } catch (FileNotFoundException e)

        {
            e.printStackTrace();
        } catch (IOException e)

        {
            e.printStackTrace();
        }

    }

    public int getnCmdNum() {
        return nCmdNum;
    }

    public void setnCmdNum(int nCmdNum) {
        this.nCmdNum = nCmdNum;
    }


    public void removeInstruction(int index) {
        strInstructions.remove(index);
    }

    public String getStrCurrCmd() {
        return strCurrCmd;
    }

    public String stripWhiteSpace(String strCommand) {
        String strWhiteSpace = strCommand;
        if (!strWhiteSpace.equals("") && strWhiteSpace.contains("//")) {
            // remove comments
            int nOffSet = strWhiteSpace.indexOf("//");
            strWhiteSpace = strWhiteSpace.substring(0, nOffSet).trim();
        }
        // remove spaces and go to new line, skip over empty line
        if (!strWhiteSpace.equals("")) {
            strWhiteSpace = strWhiteSpace.trim().replaceAll("\\s", "");
        }
        return strWhiteSpace;

    }


    // are there more commands in input?
    public boolean hasMoreCommands() {
        boolean hasMore = false;
        if (nCmdNum + 1 < strInstructions.size() || (nCmdNum == 0 && strInstructions.size() >= 1)) {
            hasMore = true;
        }
        return hasMore;

    }

    // reads next command from input and makes it current command, only called if hasMoreCommands is true
    public void advance() {
        if (bFirst == true) {
            strCurrCmd = strInstructions.get(nCmdNum);
            bFirst = false;
        } else {
            nCmdNum++;
            strCurrCmd = strInstructions.get(nCmdNum);
        }


    }

    public void reset() {
        strCurrCmd = strInstructions.get(0);
        nCmdNum = 0;
        bFirst = true;
    }

    // returns type of current command - A-Command for @Xxx
    // C for dest=com;jump and L_Command for @Xxx where Xxx is a symbol
    public String commandType() {
        String strType;
        if (strCurrCmd.charAt(0) == '@') {
            strType = "A_COMMAND";
        } else if (strCurrCmd.charAt(0) == '(') {
            strType = "L_COMMAND";
        } else {
            strType = "C_COMMAND";
        }
        return strType;
    }

    // returns symbol or decimal Xxx of current command @Xxx or (Xxx) - called only when commandType() is A or L command
    public String symbol() {
        return strCurrCmd.substring(1);
    }

    // returns dest mnemonic in current C-command only called when C type command
    public String dest() {
        String dest;
        if (strCurrCmd.contains("=")) {
            int nIndex = strCurrCmd.indexOf("=");
            dest = strCurrCmd.substring(0, nIndex);
        } else {
            dest = "null";
        }
        return dest;
    }

    // returns comp mnemonic in current C-command - 28 possibilities - called when command type is C
    public String comp() {
        String strComp;
        if (strCurrCmd.contains("=") && strCurrCmd.contains(";")) {
            int nEquals = strCurrCmd.indexOf("=");
            int nSemi = strCurrCmd.indexOf(";");
            strComp = strCurrCmd.substring(nEquals + 1, nSemi);
        } else if (strCurrCmd.contains("=")) {
            int nEquals = strCurrCmd.indexOf("=");
            strComp = strCurrCmd.substring(nEquals + 1);
        } else if (strCurrCmd.contains(";")) {
            int nSemi = strCurrCmd.indexOf(";");
            strComp = strCurrCmd.substring(0, nSemi);
        } else {
            return "Invalid";
        }
        return strComp;
    }

    // returns jump mnemonic in current C command 8 possiblities called only when C type
    public String jump() {
        String strJump;
        if (strCurrCmd.contains(";")) {
            int nSemi = strCurrCmd.indexOf(";");
            strJump = strCurrCmd.substring(nSemi + 1);
        } else {
            strJump = "null";
        }
        return strJump;
    }


}
