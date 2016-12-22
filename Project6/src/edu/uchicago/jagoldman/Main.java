package edu.uchicago.jagoldman;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        // write your code here
        String strFileName;
        if (args.length == 0) {
            return;
        } else {
            strFileName = args[args.length - 1];
        }
        Parser parser = new Parser(strFileName);
        ArrayList<Character> chars = new ArrayList<>();
        chars.add('_');
        chars.add('.');
        chars.add('$');
        chars.add(':');

        SymbolTable symbolTable = new SymbolTable();

        while (parser.hasMoreCommands()) {
            parser.advance();
            if (parser.commandType().equals("L_COMMAND")) {
                int nLength = parser.getStrCurrCmd().length();
                int nLine = parser.getnCmdNum();
                symbolTable.addLCommandEntry(parser.symbol().substring(0, nLength - 2), nLine);
                parser.removeInstruction(nLine);
                parser.setnCmdNum(nLine--);
            }
        }
        parser.reset();
        String fileNameSplit = strFileName.substring(0, strFileName.length() - 4);
        try (FileWriter fw = new FileWriter(fileNameSplit + ".hack")) {
            while (parser.hasMoreCommands()) {
                parser.advance();
                StringBuilder sbCommand = new StringBuilder();
                String strBinary;

                if (parser.commandType().equals("A_COMMAND") && ((Character.isLetter(parser.symbol().charAt(0)) ||
                        chars.contains(parser.symbol().charAt(0))) && symbolTable.contains(parser.symbol()))) {
                    int nBinary = symbolTable.GetAddress(parser.symbol());
                    strBinary = getBinary(nBinary);
                    sbCommand.append(strBinary);

                } else if (parser.commandType().equals("A_COMMAND") && ((Character.isLetter(parser.symbol().charAt(0)) ||
                        chars.contains(parser.symbol().charAt(0)))) && !symbolTable.contains(parser.symbol())) {
                    symbolTable.addEntry(parser.symbol());
                    int nBinary = symbolTable.GetAddress(parser.symbol());
                    strBinary = getBinary(nBinary);
                    sbCommand.append(strBinary);
                } else if (parser.commandType().equals("A_COMMAND") && Character.isDigit(parser.symbol().charAt(0))) {

                    strBinary = getBinary(Integer.parseInt(parser.symbol()));
                    sbCommand.append(strBinary);
                } else if (parser.commandType().equals("C_COMMAND")) {
                    String strComp = parser.comp();
                    String strDest = parser.dest();
                    String strJump = parser.jump();
                    sbCommand.append("111");
                    sbCommand.append(Code.comp(strComp));
                    sbCommand.append(Code.dest(strDest));
                    sbCommand.append(Code.jump(strJump));
                } else {
                    sbCommand.append("error");
                }
                fw.write(sbCommand.toString() + "\n");
            }


        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public static String getBinary(int nNum) {
        String strBinary = Integer.toBinaryString(nNum);
        if (strBinary.length() != 16) {
            int nNumZeroes = 16 - strBinary.length();
            StringBuilder sbTemp = new StringBuilder();
            for (int i = 0; i < nNumZeroes; i++) {
                sbTemp.append("0");
            }
            sbTemp.append(strBinary);
            strBinary = sbTemp.toString();
        }
        return strBinary;
    }
}
