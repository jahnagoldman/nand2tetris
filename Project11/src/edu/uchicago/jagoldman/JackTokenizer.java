package edu.uchicago.jagoldman;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by jahnaariellegoldman on 11/21/16.
 * Parses the Jack code into separated tokens by their type, to then be compiled by the CompilationEngine
 */
public class JackTokenizer {
    private Scanner mScanner;
    private static ArrayList<String> keyWords;
    private static String symbols;
    private static String operations;
    private ArrayList<String> tokens;
    private String jackcode;
    private String mTokenType;
    private String mKeyWord;
    private char mSymbol;
    private String mIdentifier;
    private String mStringVal;
    private int mIntVal;
    private static ArrayList<String> libraries;
    private int pointer;
    private boolean bFirst;

    // opens input file/stream and gets ready to tokenize it
    public JackTokenizer(File file) {
        try {
            mScanner = new Scanner(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // keep all input in 1 long string - jackcode, to parse part by part in the advance method
        jackcode = "";
        while (mScanner.hasNextLine()) {
            String strLine = mScanner.nextLine();
            while (strLine.equals("") || hasComments(strLine)) {
                if (hasComments(strLine)) {
                    strLine = removeComments(strLine);
                }
                if (strLine.trim().equals("")) {
                    if (mScanner.hasNextLine()) {
                        strLine = mScanner.nextLine();
                    } else {
                        break;
                    }
                }
            }
            jackcode += strLine.trim();
        }
        // add tokens from the jackcode string into an arraylist of the tokens
        tokens = new ArrayList<String>();
        while (jackcode.length() > 0) {
            while (jackcode.charAt(0) == ' ') {
                jackcode = jackcode.substring(1);
            }
            // keyword
            for (int i = 0; i < keyWords.size(); i++) {
                if (jackcode.startsWith(keyWords.get(i).toString() + " ")) {
                    String keyword = keyWords.get(i).toString();
                    tokens.add(keyword);
                    jackcode = jackcode.substring(keyword.length());
                }

            }
            // symbol
            if (symbols.contains(jackcode.substring(0, 1))) {
                char symbol = jackcode.charAt(0);
                tokens.add(Character.toString(symbol));
                jackcode = jackcode.substring(1);
            }
            // integer constant
            else if (Character.isDigit(jackcode.charAt(0))) {
                String value = jackcode.substring(0, 1);
                jackcode = jackcode.substring(1);
                while (Character.isDigit(jackcode.charAt(0))) {
                    value += jackcode.substring(0, 1);
                    jackcode = jackcode.substring(1);

                }
                tokens.add(value);

            }
            // string constant
            else if (jackcode.substring(0, 1).equals("\"")) {
                jackcode = jackcode.substring(1);
                String strString = "\"";
                while ((jackcode.charAt(0) != '\"')) {
                    strString += jackcode.charAt(0);
                    jackcode = jackcode.substring(1);

                }
                strString = strString + "\"";
                tokens.add(strString);
                jackcode = jackcode.substring(1);

            }
            // identifier
            else if (Character.isLetter(jackcode.charAt(0)) || (jackcode.substring(0, 1).equals("_"))) {
                String strIdentifier = jackcode.substring(0, 1);
                jackcode = jackcode.substring(1);
                while ((Character.isLetter(jackcode.charAt(0))) || (jackcode.substring(0, 1).equals("_"))) {
                    strIdentifier += jackcode.substring(0, 1);
                    jackcode = jackcode.substring(1);
                }

                tokens.add(strIdentifier);

            }
            // start out with pointer at position 0
            bFirst = true;
            pointer = 0;


        }
    }


// list of keywords, symbols, and libraries for reference

    static {
        keyWords = new ArrayList<String>();
        keyWords.add("class");
        keyWords.add("constructor");
        keyWords.add("function");
        keyWords.add("method");
        keyWords.add("field");
        keyWords.add("static");
        keyWords.add("var");
        keyWords.add("int");
        keyWords.add("char");
        keyWords.add("boolean");
        keyWords.add("void");
        keyWords.add("true");
        keyWords.add("false");
        keyWords.add("null");
        keyWords.add("this");
        keyWords.add("do");
        keyWords.add("if");
        keyWords.add("else");
        keyWords.add("while");
        keyWords.add("return");
        keyWords.add("let");
        operations = "+-*/&|<>=";
        symbols = "{}()[].,;+-*/&|<>=-~";
        libraries = new ArrayList<String>();
        libraries.add("Array");
        libraries.add("Math");
        libraries.add("String");
        libraries.add("Array");
        libraries.add("Output");
        libraries.add("Screen");
        libraries.add("Keyboard");
        libraries.add("Memory");
        libraries.add("Sys");
        libraries.add("Square");
        libraries.add("SquareGame");


    }

    // do we have more tokens in the input?
    public boolean hasMoreTokens() {
        boolean hasMore = false;
        if (pointer < tokens.size() - 1) {
            hasMore = true;
        }
        return hasMore;

    }

    // gets next token from input and makes it current token, only called if hasMoreTokens() is true, initially no current token
    public void advance() {
        if (hasMoreTokens()) {
            if (!bFirst) {
                pointer++;
            }
            // if at position 0 of tokens, we do not want to increment yet
            else if (bFirst) {
                bFirst = false;
            }
            String currentItem = tokens.get(pointer);
            // assign current token type and corresponding field variable (keyword, symbol, intval, stringval, or identifier)
            // for this current token - position of where we are in the tokens array
            if (keyWords.contains(currentItem)) {
                mTokenType = "KEYWORD";
                mKeyWord = currentItem;
            } else if (symbols.contains(currentItem)) {
                mSymbol = currentItem.charAt(0);
                mTokenType = "SYMBOL";
            } else if (Character.isDigit(currentItem.charAt(0))) {
                mIntVal = Integer.parseInt(currentItem);
                mTokenType = "INT_CONST";
            } else if (currentItem.substring(0, 1).equals("\"")) {
                mTokenType = "STRING_CONST";
                mStringVal = currentItem.substring(1, currentItem.length() - 1);
            } else if ((Character.isLetter(currentItem.charAt(0))) || (currentItem.charAt(0) == '_')) {
                mTokenType = "IDENTIFIER";
                mIdentifier = currentItem;
            }
        } else {
            return;
        }


    }

    // go back 1 in the tokens array
    public void decrementPointer() {
        if (pointer > 0) {
            pointer--;
        }

    }


    // test if the line argument has comments in it
    private boolean hasComments(String strLine) {
        boolean bHasComments = false;
        if (strLine.contains("//") || strLine.contains("/*") || strLine.trim().startsWith("*")) {
            bHasComments = true;
        }
        return bHasComments;

    }

    // removes comments from a line
    private String removeComments(String strLine) {
        String strNoComments = strLine;
        if (hasComments(strLine)) {
            int offSet;
            if (strLine.trim().startsWith("*")) {
                offSet = strLine.indexOf("*");
            } else if (strLine.contains("/*")) {
                offSet = strLine.indexOf("/*");
            } else {
                offSet = strLine.indexOf("//");
            }
            strNoComments = strLine.substring(0, offSet).trim();

        }
        return strNoComments;
    }

    // returns the type of the current token - keyword, symbol, identifier, int_constant, or string_constant
    public String tokenType() {
        return mTokenType;

    }

    // returns the keyword which is the current token, should be called only when tokenType() is keyword
    public String keyWord() {
        return mKeyWord;
    }

    // returns character which is current token, should be called only when tokenType() is symbol
    public char symbol() {
        return mSymbol;
    }

    // returns identifier which is the current token - should be called only when tokenType() is identifier
    public String identifier() {
        return mIdentifier;
    }

    // returns integer value of the current token - should be called only when tokenType() is INT_CONST
    public int intVal() {
        return mIntVal;
    }

    // returns string value of current token without double quotes, should be called only when tokenType() is string_const
    public String stringVal() {
        return mStringVal;
    }

    // indicates if a symbol is an operation, i.e., =, +, -, &, |, etc.
    public boolean isOperation() {
        for (int i = 0; i < operations.length(); i++) {
            if (operations.charAt(i) == mSymbol) {
                return true;
            }
        }
        return false;
    }


}
