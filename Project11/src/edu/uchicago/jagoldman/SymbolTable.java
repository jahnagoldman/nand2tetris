package edu.uchicago.jagoldman;

import java.util.HashMap;

/**
 * Created by jahnaariellegoldman on 11/30/16.
 */
public class SymbolTable {

    // static and field variables
    private HashMap<String, Symbol> classTable;
    // arg and var
    private HashMap<String, Symbol> methodTable;
    // keeps track of indices for each symbol kind, static field arg and var
    private HashMap<String, Integer> indices;

    // creates new empty symbol table
    public SymbolTable() {
        classTable = new HashMap<>();
        methodTable = new HashMap<>();
        indices = new HashMap<>();
        indices.put("static", 0);
        indices.put("field", 0);
        indices.put("argument", 0);
        indices.put("var", 0);


    }

    // starts a new subroutine scope (i.e. resets the subroutine's symbol table)
    public void startSubroutine() {
        methodTable.clear();
        indices.put("argument", 0);
        indices.put("var", 0);


    }

    // defines a new identifier of a given name, type and kind and assigns it a running index
    // STATIC & FIELD identifiers have a class scope, while ARG and VAR identifiers have a subroutine scope
    public void define(String strName, String strType, String strKind) {
        int index = indices.get(strKind);
        Symbol symbol = new Symbol(strType, strKind, index);
        index++;
        indices.put(strKind, index);
        // if arg or var, put in the method level symbol table
        if (strKind.equals("argument") || strKind.equals("var")) {
            methodTable.put(strName, symbol);
        }
        // if static or field, put in class level symbol table
        else if (strKind.equals("static") || strKind.equals("field")) {
            classTable.put(strName, symbol);
        }

    }

    // returns the number of variables of the given kind already defined in the current scope
    // kind = STATIC, FIELD, ARG, VAR or NONE
    public int varCount(String strKind) {
        return indices.get(strKind);
    }

    // return the kind of the named identifier in the current scope, if the identifier is unknown in current scope, returns NONE
    // returns STATIC, FIELD, ARG, VAR, or NONE
    public String kindOf(String strName) {
        String kind;
        if (methodTable.containsKey(strName)) {
            kind = methodTable.get(strName).getKind();
        } else if (classTable.containsKey(strName)) {
            kind = classTable.get(strName).getKind();
        } else {
            kind = "none";
        }
        return kind;

    }

    // returns the type of the named identifier in the current scope
    public String typeOf(String strName) {
        String type;
        // if identifier is in method table
        if (methodTable.containsKey(strName)) {
            type = methodTable.get(strName).getType();

        }
        // if identifier is in class table
        else if (classTable.containsKey(strName)) {
            type = classTable.get(strName).getType();
        } else {
            type = "";
        }
        return type;

    }

    // returns the index assigned to the named identifier
    public int indexOf(String strName) {
        Symbol symbol = null;
        int index;
        if (methodTable.containsKey(strName)) {
            symbol = methodTable.get(strName);
        } else if (classTable.containsKey(strName)) {
            symbol = classTable.get(strName);
        }
        // return correct index if symbol is not null
        if (symbol == null) {
            index = -1;
        } else {
            index = symbol.getNumber();

        }
        return index;

    }
}
