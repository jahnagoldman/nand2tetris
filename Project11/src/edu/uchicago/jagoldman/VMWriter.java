package edu.uchicago.jagoldman;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by jahnaariellegoldman on 11/30/16.
 */
public class VMWriter {
    private FileWriter fw;

    public VMWriter(File fileOut) {
        try {
            fw = new FileWriter(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // writes a VM push command
    public void writePush(String strSegment, int nIndex) {
        if (strSegment.equals("var")) {
            strSegment = "local";
        }
        if (strSegment.equals("field")) {
            strSegment = "this";
        }
        try {
            fw.write("push " + strSegment + " " + nIndex + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    // writes a VM pop command
    public void writePop(String strSegment, int nIndex) {
        if (strSegment.equals("var")) {
            strSegment = "local";
        }
        if (strSegment.equals("field")) {
            strSegment = "this";
        }
        try {
            fw.write("pop " + strSegment + " " + nIndex + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // writes a VM arithmetic command
    public void writeArithmetic(String strCommand) {
        try {
            fw.write(strCommand + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // writes a VM label command
    public void writeLabel(String strLabel) {
        try {
            fw.write("label " + strLabel + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // writes a VM goto command
    public void writeGoto(String strLabel) {
        try {
            fw.write("goto " + strLabel + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // writes a VM if-goto command
    public void writeIf(String strLabel) {
        try {
            fw.write("if-goto " + strLabel + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // writes a VM call command
    public void writeCall(String strName, int nArgs) {
        try {
            fw.write("call " + strName + " " + nArgs + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // writes a VM function command
    public void writeFunction(String strName, int nLocals) {
        try {
            fw.write("function " + strName + " " + nLocals + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // writes a VM return command
    public void writeReturn() {
        try {
            fw.write("return\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // closes the output file
    public void close() {
        try {
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
