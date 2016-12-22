package edu.uchicago.jagoldman;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by jahnaariellegoldman onFRAME/1/16.
 * Translates VM commands into Hack assembly code
 */

public class CodeWriter {
    private FileWriter fw;
    private int mJumpNumber = 0;
    private static String fileName = "";
    private static int nLabelNum = 0;

    // opens output file/stream and gets ready to write into it
    public CodeWriter(File file) {
        try {
            fw = new FileWriter(file);
            setFileName(file);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    // informs code writer that translation of new VM file is started
    public void setFileName(File file) {
        fileName = file.getName();

    }

    public void writeComment(String strComment) {
        try {
            fw.write("//" + strComment + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // writes the assembly code that is the translation of the given arithmetic command
    public void writeArithmetic(String strCommand) {
        String strACode = null;
        if (strCommand.equals("add")) {
            strACode = new StringBuilder().append(getArithFormat1()).append("M=M+D\n").toString();

        } else if (strCommand.equals("sub")) {
            strACode = new StringBuilder().append(getArithFormat1()).append("M=M-D\n").toString();
        } else if (strCommand.equals("neg")) {
            strACode = new StringBuilder().append("D=0\n").append("@SP\n").append("A=M-1\n").append("M=D-M\n").toString();
        } else if (strCommand.equals("eq")) {
            strACode = new StringBuilder().append(getArithFormat1()).append(getArithFormat2("JNE")).toString();
            mJumpNumber++;
        } else if (strCommand.equals("gt")) {
            strACode = new StringBuilder().append(getArithFormat1()).append(getArithFormat2("JLE")).toString();
            mJumpNumber++;
        } else if (strCommand.equals("lt")) {
            strACode = new StringBuilder().append(getArithFormat1()).append(getArithFormat2("JGE")).toString();
            mJumpNumber++;
        } else if (strCommand.equals("and")) {
            strACode = new StringBuilder().append(getArithFormat1()).append("M=M&D\n").toString();
        } else if (strCommand.equals("or")) {
            strACode = new StringBuilder().append(getArithFormat1()).append("M=M|D\n").toString();
        } else if (strCommand.equals("not")) {
            strACode = new StringBuilder().append("@SP\n").append("A=M-1\n").append("M=!M\n").toString();
        }
        try {
            fw.write(strACode);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    // writes the assembly code that is the translation of the given command, where command is either C_PUSH or C_POP, given segment and index as well
    public void writePushPop(String strCommand, String strSegment, int nIndex) {
        String strAcode = "";
        if (strCommand.equals("C_PUSH")) {
            if (strSegment.equals("static")) {
                strAcode = new StringBuilder().append("@").append(fileName).append(nIndex).append("\n").append("D=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n").toString();
            } else if (strSegment.equals("this")) {
                strAcode = getPushFormat1("THIS", nIndex);
            } else if (strSegment.equals("local")) {
                strAcode = getPushFormat1("LCL", nIndex);

            } else if (strSegment.equals("argument")) {
                strAcode = getPushFormat1("ARG", nIndex);

            } else if (strSegment.equals("that")) {
                strAcode = getPushFormat1("THAT", nIndex);

            } else if (strSegment.equals("constant")) {
                strAcode = new StringBuilder().append("@").append(nIndex).append("\n").append("D=A\n").append("@SP\n").append("A=M\n").append("M=D\n").append("@SP\n").append("M=M+1\n").toString();

            } else if (strSegment.equals("pointer") && nIndex == 0) {
                strAcode = getPushFormat2("THIS");
            } else if (strSegment.equals("pointer") && nIndex == 1) {
                strAcode = getPushFormat2("THAT");
            } else if (strSegment.equals("temp")) {
                strAcode = getPushFormat1("R5", nIndex + 5);

            }

        } else if (strCommand.equals("C_POP")) {
            if (strSegment.equals("static")) {
                strAcode = new StringBuilder().append("@").append(fileName).append(nIndex).append("\nD=A\n@R13\nM=D\n@SP\nAM=M-1\nD=M\n@R13\nA=M\nM=D\n").toString();
            } else if (strSegment.equals("this")) {
                strAcode = getPopFormat1("THIS", nIndex);
            } else if (strSegment.equals("local")) {
                strAcode = getPopFormat1("LCL", nIndex);

            } else if (strSegment.equals("argument")) {
                strAcode = getPopFormat1("ARG", nIndex);

            } else if (strSegment.equals("that")) {
                strAcode = getPopFormat1("THAT", nIndex);

            } else if (strSegment.equals("constant")) {
                strAcode = new StringBuilder().append("@").append(nIndex).append("\n").append("D=A\n").append("@SP\n").append("A=M\n").append("M=D\n").append("@SP\n").append("M=M+1\n").toString();

            } else if (strSegment.equals("pointer") && nIndex == 0) {
                strAcode = getPopFormat2("THIS");
            } else if (strSegment.equals("pointer") && nIndex == 1) {
                strAcode = getPopFormat2("THAT");
            } else if (strSegment.equals("temp")) {
                strAcode = getPopFormat1("R5", nIndex + 5);

            }


        }

        try {
            fw.write(strAcode);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    // get format for arithmetic command - applies to all except for neg and not
    public String getArithFormat1() {
        return new StringBuilder().append("@SP\n").append("AM=M-1\n").append("D=M\n").append("A=A-1\n").toString();
    }

    // get 2nd part of format for arithmetic commands - only for et, gt, and lt
    public String getArithFormat2(String strJump) {
        return new StringBuilder().append("D=M-D\n").append("@FALSE").append(mJumpNumber).append("\n").append("D;").append(strJump).append("\n@SP\n").append("A=M-1\n").append("M=-1\n").append("@CONTINUE").append(mJumpNumber).append("\n0;JMP\n").append("(FALSE").append(mJumpNumber).append(")\n").append("@SP\n").append("A=M-1\n").append("M=0\n").append("(CONTINUE").append(mJumpNumber).append(")\n").toString();

    }

    // get format for pushing onto stack given the segment and index - for this, local, argument, that, and temp
    public String getPushFormat1(String strSegment, int nIndex) {
        String strACode;
        strACode = new StringBuilder().append("@").append(strSegment).append("\nD=M\n@").append(nIndex).append("\n").append("A=D+A\n").append("D=M\n").append("@SP\n").append("A=M\n").append("M=D\n").append("@SP\n").append("M=M+1\n").toString();
        return strACode;

    }

    // get format for pushing onto stack given the segment - for static & pointer
    public String getPushFormat2(String strSegment) {
        String strAcode;
        strAcode = new StringBuilder().append("@").append(strSegment).append("\nD=M\n").append("@SP\n").append("A=M\n").append("M=D\n").append("@SP\n").append("M=M+1\n").toString();
        return strAcode;
    }

    // get format for popping off of stack given the segment and index - for this, local, argument, that, and temp
    public String getPopFormat1(String strSegment, int nIndex) {
        String strAcode;
        strAcode = new StringBuilder().append("@").append(strSegment).append("\nD=M\n@").append(nIndex).append("\n").append("D=D+A\n").append("@R13\n").append("M=D\n").append("@SP\n").append("AM=M-1\n").append("D=M\n").append("@R13\n").append("A=M\n").append("M=D\n").toString();
        return strAcode;
    }

    // get format for popping off of stack given the segment - for static & pointer
    public String getPopFormat2(String strSegment) {
        String strAcode;
        strAcode = new StringBuilder().append("@").append(strSegment).append("\nD=A\n").append("@R13\n").append("M=D\n").append("@SP\n").append("AM=M-1\n").append("D=M\n").append("@R13\n").append("A=M\n").append("M=D\n").toString();
        return strAcode;

    }

    // closes output file
    public void close() {
        try {
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    // writes assembly code that effects VM initialization - bootstrap code at beginning of output file
    public void writeInit() {
        try {
            fw.write(new StringBuilder().append("@256\n").append("D=A\n").append("@SP\n").append("M=D\n").toString());
            writeCall("Sys.init", 0);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    // writes assembly code that is translation of label command
    // arbitrary string with any sequence of letters, digits, underscore, dot, and colon not beginning with digit
    public void writeLabel(String strLabel) {
        try {
            fw.write(new StringBuilder().append("(").append(strLabel).append(")\n").toString());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    // writes assembly code that is translation of goto command
    public void writeGoto(String strLabel) {
        try {
            fw.write(new StringBuilder().append("@").append(strLabel).append("\n0;JMP\n").toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // writes assembly code that is translation of if-goto command
    public void writeIf(String strLabel) {
        try {
            fw.write(new StringBuilder().append(getArithFormat1()).append("@").append(strLabel).append("\nD;JNE\n").toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // writes assembly code that is translation of call command
    public void writeCall(String strFunctionName, int nNumArgs) {
        String strLabel = "RETURN_LABEL" + nLabelNum;
        nLabelNum++;
        try {
            fw.write(new StringBuilder().append("@").append(strLabel).append("\n").append("D=A\n@SP\nA=M\nM=D\n@SP\nM=M+1\n").append(getPushFormat2("LCL")).append(getPushFormat2("ARG")).append(getPushFormat2("THIS")).append(getPushFormat2("THAT")).append("@SP\n").append("D=M\n").append("@5\n").append("D=D-A\n").append("@").append(nNumArgs).append("\n").append("D=D-A\n").append("@ARG\n").append("M=D\n").append("@SP\n").append("D=M\n").append("@LCL\n").append("M=D\n").append("@").append(strFunctionName).append("\n0;JMP\n(").append(strLabel).append(")\n").toString());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    // writes assembly code that is translation of return command
    public void writeReturn() {
        try {
            fw.write(new StringBuilder().append("@LCL\n").append("D=M\n").append("@FRAME\n").append("M=D\n").append("@5\n").append("A=D-A\n").append("D=M\n").append("@RET\n").append("M=D\n").append(getPopFormat1("ARG", 0)).append("@ARG\n").append("D=M\n").append("@SP\n").append("M=D+1\n").append("@FRAME\n").append("D=M-1\n").append("AM=D\n").append("D=M\n").append("@THAT\n").append("M=D\n").append("@FRAME\n").append("D=M-1\n").append("AM=D\n").append("D=M\n").append("@THIS\n").append("M=D\n").append("@FRAME\n").append("D=M-1\n").append("AM=D\n").append("D=M\n").append("@ARG\n").append("M=D\n").append("@FRAME\n").append("D=M-1\n").append("AM=D\n").append("D=M\n").append("@LCL\n").append("M=D\n").append("@RET\n").append("A=M\n").append("0;JMP\n").toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // writes assembly code that is translation of given function command
    public void writeFunction(String strFunctionName, int nNumLocals) {
        try {
            fw.write(new StringBuilder().append("(").append(strFunctionName).append(")\n").toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int nC = 0; nC < nNumLocals; nC++) {
            writePushPop("C_PUSH", "constant", 0);
        }


    }


}