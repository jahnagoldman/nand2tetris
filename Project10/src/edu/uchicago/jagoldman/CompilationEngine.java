package edu.uchicago.jagoldman;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by jahnaariellegoldman on 11/21/16.
 * Compiles the jack tokens from the JackTokenizer into corresponding XML code
 */
public class CompilationEngine {
    private FileWriter fw;
    private JackTokenizer jtoken;
    private boolean bFirstRoutine;

    public CompilationEngine(File inFile, File outFile) {
        try {
            fw = new FileWriter(outFile);
            jtoken = new JackTokenizer(inFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        bFirstRoutine = true;
    }

    // compiles a complete class
    public void compileClass() {
        try {
            jtoken.advance();
            fw.write("<class>\n");
            fw.write("<keyword> class </keyword>\n");
            jtoken.advance();
            fw.write("<identifier> " + jtoken.identifier() + " </identifier>\n");
            jtoken.advance();
            fw.write("<symbol> { </symbol>\n");
            compileClassVarDec();
            compileSubRoutine();
            fw.write("<symbol> } </symbol>\n");
            fw.write("</class>\n");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // compiles a static declaration or a field declaration
    public void compileClassVarDec() {
        jtoken.advance();
        try {
            while (jtoken.keyWord().equals("static") || jtoken.keyWord().equals("field")) {
                fw.write("<classVarDec>\n");
                // field or static
                fw.write("<keyword> " + jtoken.keyWord() + " </keyword>\n");
                jtoken.advance();
                // if for example, field Square square (Square)
                if (jtoken.tokenType().equals("IDENTIFIER")) {
                    fw.write("<identifier> " + jtoken.identifier() + "</identifier>\n");
                }
                // if for example, field int square (int)
                else {
                    fw.write("<keyword> " + jtoken.keyWord() + "</keyword>\n");

                }
                jtoken.advance();
                // third word of the classvardec - e.g. square in the above - field int square
                fw.write("<identifier> " + jtoken.identifier() + "</identifier>\n");
                jtoken.advance();
                // if there are multiple in 1 line - e.g. field int x, y
                if (jtoken.symbol() == ',') {
                    fw.write("<symbol> , </symbol>\n");
                    jtoken.advance();
                    fw.write(("<identifier> " + jtoken.identifier() + "</identifier>\n"));
                    jtoken.advance();
                }
                // semicolon
                fw.write("<symbol> ; </symbol>\n");
                jtoken.advance();
                fw.write("</classVarDec>\n");
            }

            // if reach a subroutine, go back in the arraylist to accomodate for advance in the next call
            if (jtoken.keyWord().equals("function") || jtoken.keyWord().equals("method") || jtoken.keyWord().equals("constructor")) {
                jtoken.decrementPointer();
                return;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    // compiles a complete method, function, or a constructor
    public void compileSubRoutine() {
        boolean hasSubRoutines = false;

        jtoken.advance();
        try {
            // once reach the end, return  - no more subroutines - base case for the recursive call
            if (jtoken.symbol() == '}' && jtoken.tokenType().equals("SYMBOL")) {
                return;
            }
            // subroutinedec tag
            if ((bFirstRoutine) && (jtoken.keyWord().equals("function") || jtoken.keyWord().equals("method") || jtoken.keyWord().equals("constructor"))) {
                bFirstRoutine = false;
                fw.write("<subroutineDec>\n");
                hasSubRoutines = true;
            }
            // function ,e
            if (jtoken.keyWord().equals("function") || jtoken.keyWord().equals("method") || jtoken.keyWord().equals("constructor")) {
                hasSubRoutines = true;
                fw.write("<keyword> " + jtoken.keyWord() + " </keyword>\n");
                jtoken.advance();
            }
            // if there is an identifier in the subroutine statement position 2 e.g. function Square getX()
            if (jtoken.tokenType().equals("IDENTIFIER")) {
                fw.write("<identifier> " + jtoken.identifier() + " </identifier>\n");
                jtoken.advance();
            }
            // if keyword instead for subroutine statement position 2 e.g. function int getX()
            else if (jtoken.tokenType().equals("KEYWORD")) {
                fw.write("<keyword> " + jtoken.keyWord() + "</keyword>\n");
                jtoken.advance();
            }
            // name of the subroutine
            if (jtoken.tokenType().equals("IDENTIFIER")) {
                fw.write("<identifier> " + jtoken.identifier() + " </identifier>\n");
                jtoken.advance();
            }
            // get parameters, or lack there of
            if (jtoken.symbol() == '(') {
                fw.write("<symbol> ( </symbol>\n");
                fw.write("<parameterList>\n");

                compileParameterList();
                fw.write("</parameterList>\n");
                fw.write("<symbol> ) </symbol>\n");

            }
            jtoken.advance();
            // start subroutine body
            if (jtoken.symbol() == '{') {
                fw.write("<subroutineBody>\n");
                fw.write("<symbol> { </symbol>\n");
                jtoken.advance();
            }
            // get all var declarations in the subroutine
            while (jtoken.keyWord().equals("var") && (jtoken.tokenType().equals("KEYWORD"))) {
                fw.write("<varDec>\n ");
                jtoken.decrementPointer();
                compileVarDec();
                fw.write(" </varDec>\n");
            }
            fw.write("<statements>\n");
            compileStatements();
            fw.write("</statements>\n");
            fw.write("<symbol> " + jtoken.symbol() + " </symbol>\n");
            if (hasSubRoutines) {
                fw.write("</subroutineBody>\n");
                fw.write("</subroutineDec>\n");
                bFirstRoutine = true;
            }

            // recursive call
            compileSubRoutine();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    // compiles a (possibly empty) parameter list including the "()"
    public void compileParameterList() {
        jtoken.advance();
        try {
            // until reach the end - )
            while (!(jtoken.tokenType().equals("SYMBOL") && jtoken.symbol() == ')')) {
                if (jtoken.tokenType().equals("IDENTIFIER")) {
                    fw.write("<identifier> " + jtoken.identifier() + " </identifier>\n");
                    jtoken.advance();
                } else if (jtoken.tokenType().equals("KEYWORD")) {
                    fw.write("<keyword> " + jtoken.keyWord() + "</keyword>\n");
                    jtoken.advance();
                }
                // commas separate the list, if there are multiple
                else if ((jtoken.tokenType().equals("SYMBOL")) && (jtoken.symbol() == ',')) {
                    fw.write("<symbol> , </symbol>\n");
                    jtoken.advance();

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // compiles a var declaration
    public void compileVarDec() {
        jtoken.advance();
        try {

            if (jtoken.keyWord().equals("var") && (jtoken.tokenType().equals("KEYWORD"))) {
                fw.write("<keyword> var </keyword>\n");
                jtoken.advance();
            }
            // type of var, if identifier, e.g. Square or Array
            if (jtoken.tokenType().equals("IDENTIFIER")) {
                fw.write("<identifier> " + jtoken.identifier() + "</identifier>\n");
                jtoken.advance();
            }
            // type of var, if keyword, e.g. int or boolean
            else if (jtoken.tokenType().equals("KEYWORD")) {
                fw.write("<keyword> " + jtoken.keyWord() + " </keyword>\n");
                jtoken.advance();
            }
            // name of var
            if (jtoken.tokenType().equals("IDENTIFIER")) {
                fw.write("<identifier> " + jtoken.identifier() + "</identifier>\n");
                jtoken.advance();
            }
            // if there are mutliple in 1 line
            if ((jtoken.tokenType().equals("SYMBOL")) && (jtoken.symbol() == ',')) {
                fw.write("<symbol> , </symbol>\n");
                jtoken.advance();
                fw.write(("<identifier> " + jtoken.identifier() + "</identifier>\n"));
                jtoken.advance();
            }
            // end of var line
            if ((jtoken.tokenType().equals("SYMBOL")) && (jtoken.symbol() == ';')) {
                fw.write("<symbol> ; </symbol>\n");
                jtoken.advance();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // compiles a sequence of statements, not including the enclosing "{}" - do, let, if, while or return
    public void compileStatements() {
        try {
            if (jtoken.symbol() == '}' && (jtoken.tokenType().equals("SYMBOL"))) {
                return;
            } else if (jtoken.keyWord().equals("do") && (jtoken.tokenType().equals("KEYWORD"))) {
                fw.write("<doStatement>\n ");
                compileDo();
                fw.write((" </doStatement>\n"));

            } else if (jtoken.keyWord().equals("let") && (jtoken.tokenType().equals("KEYWORD"))) {
                fw.write("<letStatement>\n ");
                compileLet();
                fw.write((" </letStatement>\n"));
            } else if (jtoken.keyWord().equals("if") && (jtoken.tokenType().equals("KEYWORD"))) {
                fw.write("<ifStatement>\n ");
                compileIf();
                fw.write((" </ifStatement>\n"));
            } else if (jtoken.keyWord().equals("while") && (jtoken.tokenType().equals("KEYWORD"))) {
                fw.write("<whileStatement>\n ");
                compileWhile();
                fw.write((" </whileStatement>\n"));
            } else if (jtoken.keyWord().equals("return") && (jtoken.tokenType().equals("KEYWORD"))) {
                fw.write("<returnStatement>\n ");
                compileReturn();
                fw.write((" </returnStatement>\n"));
            }
            jtoken.advance();
            compileStatements();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // compiles a do statement
    public void compileDo() {
        try {
            if (jtoken.keyWord().equals("do")) {
                fw.write("<keyword> do </keyword>\n");
            }
            // function call
            compileCall();
            // semi colon
            jtoken.advance();
            fw.write("<symbol> " + jtoken.symbol() + " </symbol>\n");


        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    private void compileCall() {
        jtoken.advance();
        try {
            // first part
            fw.write("<identifier> " + jtoken.identifier() + "</identifier>\n");
            jtoken.advance();
            // if . - then is something like Screen.erase()
            if ((jtoken.tokenType().equals("SYMBOL")) && (jtoken.symbol() == '.')) {
                fw.write("<symbol> " + jtoken.symbol() + "</symbol>\n");
                jtoken.advance();
                fw.write("<identifier> " + jtoken.identifier() + " </identifier>\n");
                jtoken.advance();
                fw.write("<symbol> " + jtoken.symbol() + " </symbol>\n");
                // parameters in the parentheses
                fw.write("<expressionList>\n");
                compileExpressionList();
                fw.write("</expressionList>\n");
                jtoken.advance();
                fw.write("<symbol> " + jtoken.symbol() + " </symbol>\n");


            }
            // if ( then is something like erase()
            else if ((jtoken.tokenType().equals("SYMBOL")) && (jtoken.symbol() == '(')) {
                fw.write("<symbol> " + jtoken.symbol() + " </symbol>\n");
                fw.write("<expressionList>\n");
                compileExpressionList();
                fw.write("</expressionList>\n");
                // parentheses )
                jtoken.advance();
                fw.write("<symbol> " + jtoken.symbol() + " </symbol>\n");


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // compiles a let statement
    public void compileLet() {
        try {
            fw.write("<keyword>" + jtoken.keyWord() + "</keyword>\n");
            jtoken.advance();
            fw.write("<identifier> " + jtoken.identifier() + " </identifier>\n");
            jtoken.advance();
            if ((jtoken.tokenType().equals("SYMBOL")) && (jtoken.symbol() == '[')) {
                // there is an expression -- because we have x[5] for example
                fw.write("<symbol> " + jtoken.symbol() + " </symbol>\n");
                compileExpression();
                jtoken.advance();
                if ((jtoken.tokenType().equals("SYMBOL")) && ((jtoken.symbol() == ']'))) {
                    fw.write("<symbol> " + jtoken.symbol() + " </symbol>\n");
                }
                // only advance if there is an expression
                jtoken.advance();

            }

            // = sign
            fw.write("<symbol> " + jtoken.symbol() + " </symbol>\n");

            compileExpression();
            // semi colon
            fw.write("<symbol> " + jtoken.symbol() + " </symbol>\n");
            jtoken.advance();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // compiles a while statement
    public void compileWhile() {
        try {
            // while
            fw.write("<keyword>" + jtoken.keyWord() + "</keyword>\n");
            jtoken.advance();
            // (
            fw.write("<symbol>" + jtoken.symbol() + "</symbol>\n");
            // compile inside of () - expression
            compileExpression();
            // )
            jtoken.advance();
            fw.write("<symbol>" + jtoken.symbol() + "</symbol>\n");
            jtoken.advance();
            // {
            fw.write("<symbol>" + jtoken.symbol() + "</symbol>\n");
            // inside of while statement
            fw.write("<statements>\n");
            compileStatements();
            fw.write("</statements>\n");
            // }
            fw.write("<symbol>" + jtoken.symbol() + "</symbol>\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // compiles a return statement
    public void compileReturn() {
        try {
            fw.write("<keyword> return </keyword>\n");
            jtoken.advance();
            if (!((jtoken.tokenType().equals("SYMBOL") && jtoken.symbol() == ';'))) {
                jtoken.decrementPointer();
                compileExpression();
            }
            if (jtoken.tokenType().equals("SYMBOL") && jtoken.symbol() == ';') {
                fw.write("<symbol> ; </symbol>\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    // compiles an if statement, possibly with a trailing else clause
    public void compileIf() {
        try {
            fw.write("<keyword> if </keyword>\n");
            jtoken.advance();
            fw.write("<symbol> ( </symbol>\n");
            // expression within if () condition
            compileExpression();
            fw.write("<symbol> ) </symbol>\n");
            jtoken.advance();
            fw.write("<symbol> { </symbol>\n");
            jtoken.advance();
            fw.write("<statements>\n");
            // compile statements within if clause { }
            compileStatements();
            fw.write("</statements>\n");
            fw.write("<symbol> } </symbol>\n");
            jtoken.advance();
            // if there is an else clause of the if statement
            if (jtoken.tokenType().equals("KEYWORD") && jtoken.keyWord().equals("else")) {
                fw.write("<keyword> else </keyword>\n");
                jtoken.advance();
                fw.write("<symbol> { </symbol>\n");
                jtoken.advance();
                fw.write("<statements>\n");
                // compile statements within else clause
                compileStatements();
                fw.write("</statements>\n");
                fw.write("<symbol> } </symbol>\n");
            } else {
                // keep placeholder correct
                jtoken.decrementPointer();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    // compiles an expression
    public void compileExpression() {
        try {
            fw.write("<expression>\n");
            compileTerm();
            while (true) {
                jtoken.advance();
                if (jtoken.tokenType().equals("SYMBOL") && jtoken.isOperation()) {
                    // < > & = have different xml code
                    if (jtoken.symbol() == '<') {
                        fw.write("<symbol> &lt; </symbol>\n");
                    } else if (jtoken.symbol() == '>') {
                        fw.write("<symbol> &gt; </symbol>\n");
                    } else if (jtoken.symbol() == '&') {
                        fw.write("<symbol> &amp; </symbol>\n");
                    } else {
                        fw.write("<symbol> " + jtoken.symbol() + " </symbol>\n");
                    }
                    compileTerm();
                } else {
                    jtoken.decrementPointer();
                    break;
                }
            }
            fw.write("</expression>\n");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // compiles a term - if current token is an identifier, must distinguish between variable, array entry, and subroutine call
    // single look ahead token which may be "{" "(" or "." to distinguish between the three possibilities
    public void compileTerm() {
        try {
            fw.write("<term>\n");
            jtoken.advance();
            if (jtoken.tokenType().equals("IDENTIFIER")) {
                String prevIdentifier = jtoken.identifier();
                jtoken.advance();
                // for [] terms
                if (jtoken.tokenType().equals("SYMBOL") && jtoken.symbol() == '[') {
                    fw.write("<identifier> " + prevIdentifier + " </identifier>\n");
                    fw.write("<symbol> [ </symbol>\n");
                    compileExpression();
                    jtoken.advance();
                    fw.write("<symbol> ] </symbol>\n");
                }
                // for ( or . - subroutine calls
                else if (jtoken.tokenType().equals("SYMBOL") && (jtoken.symbol() == '(' || jtoken.symbol() == '.')) {
                    jtoken.decrementPointer();
                    jtoken.decrementPointer();
                    compileCall();

                } else {
                    fw.write("<identifier> " + prevIdentifier + " </identifier>\n");
                    jtoken.decrementPointer();
                }
            } else {
                // integer
                if (jtoken.tokenType().equals("INT_CONST")) {
                    fw.write("<integerConstant> " + jtoken.intVal() + " </integerConstant>\n");

                }
                // strings
                else if (jtoken.tokenType().equals("STRING_CONST")) {
                    fw.write("<stringConstant> " + jtoken.stringVal() + " </stringConstant>\n");
                }
                // this true null or false
                else if (jtoken.tokenType().equals("KEYWORD") && (jtoken.keyWord().equals("this") || jtoken.keyWord().equals("null")
                        || jtoken.keyWord().equals("false") || jtoken.keyWord().equals("true"))) {
                    fw.write("<keyword> " + jtoken.keyWord() + " </keyword>\n");
                }
                // parenthetical separation
                else if (jtoken.tokenType().equals("SYMBOL") && jtoken.symbol() == '(') {
                    fw.write("<symbol>" + jtoken.symbol() + "</symbol>\n");
                    compileExpression();
                    jtoken.advance();
                    fw.write("<symbol> " + jtoken.symbol() + "</symbol>\n");
                }
                // unary operators
                else if (jtoken.tokenType().equals("SYMBOL") && (jtoken.symbol() == '-' || jtoken.symbol() == '~')) {
                    fw.write("<symbol> " + jtoken.symbol() + "</symbol>\n");
                    // recursive call
                    compileTerm();
                }
            }
            fw.write("</term>\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // compiles (possibly empty) comma separated list of expressions
    public void compileExpressionList() {
        jtoken.advance();
        // end of list
        if (jtoken.symbol() == ')' && jtoken.tokenType().equals("SYMBOL")) {
            jtoken.decrementPointer();
        } else {
            jtoken.decrementPointer();
            compileExpression();
        }
        while (true) {
            jtoken.advance();
            if (jtoken.tokenType().equals("SYMBOL") && jtoken.symbol() == ',') {
                try {
                    fw.write("<symbol> , </symbol>\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                compileExpression();
            } else {
                jtoken.decrementPointer();
                break;
            }
        }

    }
}
