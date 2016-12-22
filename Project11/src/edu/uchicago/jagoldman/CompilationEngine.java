package edu.uchicago.jagoldman;

import java.io.File;

/**
 * Created by jahnaariellegoldman on 11/21/16.
 * Compiles the jack tokens from the JackTokenizer into corresponding XML code
 */
public class CompilationEngine {
    private JackTokenizer jtoken;
    private SymbolTable symboltable;
    private VMWriter vmWriter;
    private String strClassName = "";
    private String strSubRoutineName = "";

    private int nLabelIndex;

    public CompilationEngine(File inFile, File outFile) {
        jtoken = new JackTokenizer(inFile);
        symboltable = new SymbolTable();
        vmWriter = new VMWriter(outFile);
        nLabelIndex = 0;
    }

    // compiles a complete class
    public void compileClass() {
        jtoken.advance();
        jtoken.advance();

        strClassName = jtoken.identifier();
        jtoken.advance();
        compileClassVarDec();
        compileSubRoutine();

        vmWriter.close();

    }

    // compiles a static declaration or a field declaration
    public void compileClassVarDec() {
        jtoken.advance();
        while (jtoken.keyWord().equals("static") || jtoken.keyWord().equals("field")) {
            // field or static
            String strKind;
            String type;
            if (jtoken.keyWord().equals("static")) {
                strKind = "static";
            }
            // field
            else {
                strKind = "field";
            }
            jtoken.advance();
            // if for example, field Square square (Square)
            if (jtoken.tokenType().equals("IDENTIFIER")) {
                type = jtoken.identifier();
            }
            // if for example, field int square (int)
            else {
                type = jtoken.keyWord();
            }
            jtoken.advance();
            // third word of the classvardec - e.g. square in the above - field int square
            symboltable.define(jtoken.identifier(), type, strKind);
            jtoken.advance();
            // if there are multiple in 1 line - e.g. field int x, y
            while (jtoken.symbol() == ',') {
                jtoken.advance();
                symboltable.define(jtoken.identifier(), type, strKind);
                jtoken.advance();
            }
            // semicolon
            jtoken.advance();
        }

        // if reach a subroutine, go back in the arraylist to accomodate for advance in the next call
        if (jtoken.keyWord().equals("function") || jtoken.keyWord().equals("method") || jtoken.keyWord().equals("constructor")) {
            jtoken.decrementPointer();
            return;
        }


    }

    // compiles a complete method, function, or a constructor
    public void compileSubRoutine() {
        jtoken.advance();
        // once reach the end, return  - no more subroutines - base case for the recursive call
        if (jtoken.symbol() == '}' && jtoken.tokenType().equals("SYMBOL")) {
            return;
        }
        String strKeyword = "";
        // function method or constructor
        if (jtoken.keyWord().equals("function") || jtoken.keyWord().equals("method") || jtoken.keyWord().equals("constructor")) {
            strKeyword = jtoken.keyWord();
            // new subroutine - reset symbol table
            symboltable.startSubroutine();
            if (jtoken.keyWord().equals("method")) {
                symboltable.define("this", strClassName, "argument");

            }
            jtoken.advance();
        }
        String strType = "";
        // the return type - void, int, char, boolean, or identifier
        if (jtoken.tokenType().equals("KEYWORD") && jtoken.keyWord().equals("void")) {
            strType = "void";
            jtoken.advance();
        } else if (jtoken.tokenType().equals("KEYWORD") && (jtoken.keyWord().equals("int") || jtoken.keyWord().equals("boolean") || jtoken.keyWord().equals("char"))) {
            strType = jtoken.keyWord();
            jtoken.advance();
        }
        // identifier
        else {
            strType = jtoken.identifier();
            jtoken.advance();
        }
        // name of the subroutine
        if (jtoken.tokenType().equals("IDENTIFIER")) {
            strSubRoutineName = jtoken.identifier();
            jtoken.advance();
        }

        // get parameters, or lack there of
        if (jtoken.symbol() == '(') {


            compileParameterList();


        }
        jtoken.advance();
        // start subroutine body
        if (jtoken.symbol() == '{') {

            jtoken.advance();
        }
        // get all var declarations in the subroutine
        while (jtoken.keyWord().equals("var") && (jtoken.tokenType().equals("KEYWORD"))) {
            jtoken.decrementPointer();
            compileVarDec();
        }
        String strFunction = "";
        if (strClassName.length() != 0 && strSubRoutineName.length() != 0) {
            strFunction += strClassName + "." + strSubRoutineName;
        }
        vmWriter.writeFunction(strFunction, symboltable.varCount("var"));
        // need to push the first argument (this) and pop it into the pointer 0
        if (strKeyword.equals("method")) {
            vmWriter.writePush("argument", 0);
            vmWriter.writePop("pointer", 0);

        } else if (strKeyword.equals("constructor")) {
            vmWriter.writePush("constant", symboltable.varCount("field"));
            vmWriter.writeCall("Memory.alloc", 1);
            vmWriter.writePop("pointer", 0);
        }

        compileStatements();


        // recursive call
        compileSubRoutine();

    }


    // compiles a (possibly empty) parameter list including the "()"
    public void compileParameterList() {
        jtoken.advance();
        // until reach the end - )
        String type = "";
        String name = "";
        String prev;
        boolean bHasParam = false;
        while (!(jtoken.tokenType().equals("SYMBOL") && jtoken.symbol() == ')')) {
            if (jtoken.tokenType().equals("KEYWORD")) {
                bHasParam = true;
                type = jtoken.keyWord();
            } else if (jtoken.tokenType().equals("IDENTIFIER")) {
                type = jtoken.identifier();
            }
            jtoken.advance();

            if (jtoken.tokenType().equals("IDENTIFIER")) {
                name = jtoken.identifier();
            }
            jtoken.advance();
            if (jtoken.tokenType().equals("SYMBOL") && jtoken.symbol() == ',') {
                symboltable.define(name, type, "argument");
                jtoken.advance();
            }


        }
        // if has parameters add these to symbol table (if the last item in the list, no comma after)
        if (bHasParam) {
            symboltable.define(name, type, "argument");

        }


    }

    // compiles a var declaration
    public void compileVarDec() {
        jtoken.advance();
        String type = "";
        String name = "";
        if (jtoken.keyWord().equals("var") && (jtoken.tokenType().equals("KEYWORD"))) {
            jtoken.advance();
        }
        // type of var, if identifier, e.g. Square or Array
        if (jtoken.tokenType().equals("IDENTIFIER")) {
            type = jtoken.identifier();
            jtoken.advance();
        }
        // type of var, if keyword, e.g. int or boolean
        else if (jtoken.tokenType().equals("KEYWORD")) {
            type = jtoken.keyWord();
            jtoken.advance();
        }
        // name of var
        if (jtoken.tokenType().equals("IDENTIFIER")) {
            name = jtoken.identifier();
            jtoken.advance();

        }
        symboltable.define(name, type, "var");

        // if there are mutliple in 1 line
        while ((jtoken.tokenType().equals("SYMBOL")) && (jtoken.symbol() == ',')) {
            jtoken.advance();
            name = jtoken.identifier();
            symboltable.define(name, type, "var");

            jtoken.advance();
        }
        // end of var line
        if ((jtoken.tokenType().equals("SYMBOL")) && (jtoken.symbol() == ';')) {
            jtoken.advance();

        }

    }

    // compiles a sequence of statements, not including the enclosing "{}" - do, let, if, while or return
    public void compileStatements() {
        if (jtoken.symbol() == '}' && (jtoken.tokenType().equals("SYMBOL"))) {
            return;
        } else if (jtoken.keyWord().equals("do") && (jtoken.tokenType().equals("KEYWORD"))) {
            compileDo();

        } else if (jtoken.keyWord().equals("let") && (jtoken.tokenType().equals("KEYWORD"))) {
            compileLet();
        } else if (jtoken.keyWord().equals("if") && (jtoken.tokenType().equals("KEYWORD"))) {
            compileIf();
        } else if (jtoken.keyWord().equals("while") && (jtoken.tokenType().equals("KEYWORD"))) {
            compileWhile();
        } else if (jtoken.keyWord().equals("return") && (jtoken.tokenType().equals("KEYWORD"))) {
            compileReturn();
        }
        jtoken.advance();
        compileStatements();

    }

    // compiles a do statement
    public void compileDo() {
        if (jtoken.keyWord().equals("do")) {
        }
        // function call
        compileCall();
        // semi colon
        jtoken.advance();
        vmWriter.writePop("temp", 0);


    }

    private void compileCall() {
        jtoken.advance();
        // first part
        String first = jtoken.identifier();
        int nArguments = 0;
        jtoken.advance();
        // if . - then is something like Screen.erase()
        if ((jtoken.tokenType().equals("SYMBOL")) && (jtoken.symbol() == '.')) {
            String objectName = first;

            jtoken.advance();
            jtoken.advance();
            first = jtoken.identifier();
            String strType = symboltable.typeOf(objectName);
            if (strType.equals("")) {
                first = objectName + "." + first;
            } else {
                nArguments = 1;
                vmWriter.writePush(symboltable.kindOf(objectName), symboltable.indexOf(objectName));
                first = symboltable.typeOf(objectName) + "." + first;
            }

            // parameters in the parentheses
            nArguments += compileExpressionList();
            jtoken.advance();
            vmWriter.writeCall(first, nArguments);


        }
        // if ( then is something like erase()
        else if ((jtoken.tokenType().equals("SYMBOL")) && (jtoken.symbol() == '(')) {
            vmWriter.writePush("pointer", 0);

            nArguments = compileExpressionList() + 1;
            // parentheses )
            jtoken.advance();
            vmWriter.writeCall(strClassName + "." + first, nArguments);


        }
    }

    // compiles a let statement
    public void compileLet() {

        jtoken.advance();
        String strVariableName = jtoken.identifier();
        jtoken.advance();
        boolean bArray = false;
        if ((jtoken.tokenType().equals("SYMBOL")) && (jtoken.symbol() == '[')) {
            // there is an expression (array) -- because we have x[5] for example
            bArray = true;
            vmWriter.writePush(symboltable.kindOf(strVariableName), symboltable.indexOf(strVariableName));
            compileExpression();
            jtoken.advance();
            if ((jtoken.tokenType().equals("SYMBOL")) && ((jtoken.symbol() == ']'))) {
            }
            // add array start to number in array
            vmWriter.writeArithmetic("add");
            // only advance if there is an expression
            jtoken.advance();

        }

        // = sign

        compileExpression();
        // semi colon
        jtoken.advance();
        if (bArray) {
            // pop into temp value and into pointer to hold for that
            vmWriter.writePop("temp", 0);
            vmWriter.writePop("pointer", 1);
            // put the value into that
            vmWriter.writePush("temp", 0);
            vmWriter.writePop("that", 0);
        } else {
            // pop directly
            vmWriter.writePop(symboltable.kindOf(strVariableName), symboltable.indexOf(strVariableName));
        }
    }


    // compiles a while statement
    public void compileWhile() {
        String secondLabel = "LABEL_" + nLabelIndex++;
        String firstLabel = "LABEL_" + nLabelIndex++;
        vmWriter.writeLabel(firstLabel);
        // while
        jtoken.advance();
        // (
        // compile inside of () - expression
        compileExpression();
        // )
        jtoken.advance();
        // if not condition, go to the next label
        vmWriter.writeArithmetic("not");
        vmWriter.writeIf(secondLabel);
        jtoken.advance();
        // {
        // inside of while statement
        compileStatements();
        // }
        // if condition go to first label
        vmWriter.writeGoto(firstLabel);
        // otherwise go to next label
        vmWriter.writeLabel(secondLabel);

    }

    // compiles a return statement
    public void compileReturn() {
        jtoken.advance();
        if (!((jtoken.tokenType().equals("SYMBOL") && jtoken.symbol() == ';'))) {
            jtoken.decrementPointer();
            compileExpression();
        } else if (jtoken.tokenType().equals("SYMBOL") && jtoken.symbol() == ';') {
            vmWriter.writePush("constant", 0);
        }
        vmWriter.writeReturn();


    }

    // compiles an if statement, possibly with a trailing else clause
    public void compileIf() {
        String strLabelElse = "LABEL_" + nLabelIndex++;
        String strLabelEnd = "LABEL_" + nLabelIndex++;
        jtoken.advance();
        // expression within if () condition
        compileExpression();
        jtoken.advance();
        // if not condition got to label else
        vmWriter.writeArithmetic("not");
        vmWriter.writeIf(strLabelElse);
        jtoken.advance();
        // compile statements within if clause { }
        compileStatements();
        // after statement finishes, go to the end label
        vmWriter.writeGoto(strLabelEnd);
        vmWriter.writeLabel(strLabelElse);
        jtoken.advance();
        // if there is an else clause of the if statement
        if (jtoken.tokenType().equals("KEYWORD") && jtoken.keyWord().equals("else")) {
            jtoken.advance();
            jtoken.advance();
            // compile statements within else clause
            compileStatements();
        } else {
            // keep placeholder correct
            jtoken.decrementPointer();
        }
        vmWriter.writeLabel(strLabelEnd);


    }

    // compiles an expression
    public void compileExpression() {
        compileTerm();
        while (true) {
            jtoken.advance();
            String strCommand = "";
            if (jtoken.tokenType().equals("SYMBOL") && jtoken.isOperation()) {
                // < > & = have different xml code
                if (jtoken.symbol() == '<') {
                    compileTerm();
                    vmWriter.writeArithmetic("lt");
                } else if (jtoken.symbol() == '>') {
                    compileTerm();
                    vmWriter.writeArithmetic("gt");
                } else if (jtoken.symbol() == '&') {
                    compileTerm();
                    vmWriter.writeArithmetic("and");

                } else if (jtoken.symbol() == '+') {
                    compileTerm();
                    vmWriter.writeArithmetic("add");

                } else if (jtoken.symbol() == '-') {
                    compileTerm();
                    vmWriter.writeArithmetic("sub");

                } else if (jtoken.symbol() == '*') {
                    compileTerm();
                    vmWriter.writeCall("Math.multiply", 2);
                } else if (jtoken.symbol() == '/') {
                    compileTerm();
                    vmWriter.writeCall("Math.divide", 2);

                } else if (jtoken.symbol() == '=') {
                    compileTerm();
                    vmWriter.writeArithmetic("eq");


                } else if (jtoken.symbol() == '|') {
                    compileTerm();
                    vmWriter.writeArithmetic("or");

                }

            } else {
                jtoken.decrementPointer();
                break;
            }
        }


    }

    // compiles a term - if current token is an identifier, must distinguish between variable, array entry, and subroutine call
    // single look ahead token which may be "{" "(" or "." to distinguish between the three possibilities
    public void compileTerm() {
        jtoken.advance();
        if (jtoken.tokenType().equals("IDENTIFIER")) {
            String prevIdentifier = jtoken.identifier();
            jtoken.advance();
            // for [] terms
            if (jtoken.tokenType().equals("SYMBOL") && jtoken.symbol() == '[') {
                // push the array start
                vmWriter.writePush(symboltable.kindOf(prevIdentifier), symboltable.indexOf(prevIdentifier));
                compileExpression();
                jtoken.advance();
                // add array number to array start, pop into pointer for that, and push into that
                vmWriter.writeArithmetic("add");
                vmWriter.writePop("pointer", 1);
                vmWriter.writePush("that", 0);
            }
            // for ( or . - subroutine calls
            else if (jtoken.tokenType().equals("SYMBOL") && (jtoken.symbol() == '(' || jtoken.symbol() == '.')) {
                jtoken.decrementPointer();
                jtoken.decrementPointer();
                compileCall();

            } else {
                jtoken.decrementPointer();
                vmWriter.writePush(symboltable.kindOf(prevIdentifier), symboltable.indexOf(prevIdentifier));
            }
        } else {
            // integer
            if (jtoken.tokenType().equals("INT_CONST")) {
                vmWriter.writePush("constant", jtoken.intVal());

            }
            // strings
            else if (jtoken.tokenType().equals("STRING_CONST")) {
                String strToken = jtoken.stringVal();
                vmWriter.writePush("constant", strToken.length());
                vmWriter.writeCall("String.new", 1);
                for (int i = 0; i < strToken.length(); i++) {
                    vmWriter.writePush("constant", (int) strToken.charAt(i));
                    vmWriter.writeCall("String.appendChar", 2);
                }
            }
            // this - push this pointer
            else if (jtoken.tokenType().equals("KEYWORD") && jtoken.keyWord().equals("this")) {
                vmWriter.writePush("pointer", 0);
            }
            // false and null - 0
            else if (jtoken.tokenType().equals("KEYWORD") && (jtoken.keyWord().equals("null") || jtoken.keyWord().equals("false"))) {
                vmWriter.writePush("constant", 0);

            }
            // true - not 0
            else if (jtoken.tokenType().equals("KEYWORD") && jtoken.keyWord().equals("true")) {
                vmWriter.writePush("constant", 0);
                vmWriter.writeArithmetic("not");
            }

            // parenthetical separation
            else if (jtoken.tokenType().equals("SYMBOL") && jtoken.symbol() == '(') {
                compileExpression();
                jtoken.advance();
            }
            // unary operators
            else if (jtoken.tokenType().equals("SYMBOL") && (jtoken.symbol() == '-' || jtoken.symbol() == '~')) {
                char symbol = jtoken.symbol();
                // recursive call
                compileTerm();
                if (symbol == '-') {
                    vmWriter.writeArithmetic("neg");
                } else if (symbol == '~') {
                    vmWriter.writeArithmetic("not");
                }
            }
        }

    }

    // compiles (possibly empty) comma separated list of expressions
    public int compileExpressionList() {
        int nArguments = 0;
        jtoken.advance();
        // end of list
        if (jtoken.symbol() == ')' && jtoken.tokenType().equals("SYMBOL")) {
            jtoken.decrementPointer();
        } else {
            nArguments = 1;
            jtoken.decrementPointer();
            compileExpression();
        }
        while (true) {
            jtoken.advance();
            if (jtoken.tokenType().equals("SYMBOL") && jtoken.symbol() == ',') {
                compileExpression();
                nArguments++;
            } else {
                jtoken.decrementPointer();
                break;
            }
        }
        return nArguments;

    }
}
