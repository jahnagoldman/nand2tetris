package edu.uchicago.jagoldman;

import java.util.HashMap;

/**
 * Created by jahnaariellegoldman on 10/26/16.
 */
public class SymbolTable {
    private HashMap<String, Integer> symbolTable;
    private int nNextSpot = 16;

    // creates new empty symbol table
    public SymbolTable() {
        symbolTable = new HashMap<>();
        symbolTable.put("SP", 0);
        symbolTable.put("LCL", 1);
        symbolTable.put("ARG", 2);
        symbolTable.put("THIS", 3);
        symbolTable.put("THAT", 4);
        for (int nC = 0; nC < 16; nC++) {
            symbolTable.put("R" + nC, nC);
        }
        symbolTable.put("SCREEN", 16384);
        symbolTable.put("KBD", 24576);

    }

    // adds pair (symbol, address) to table
    public void addLCommandEntry(String strSymbol, int nAddress) {
        symbolTable.put(strSymbol, nAddress);

    }

    public void addEntry(String strSymbol) {
        symbolTable.put(strSymbol, nNextSpot);
        nNextSpot++;
    }

    // does symbol table contain given symbol?
    public boolean contains(String symbol) {
        return (symbolTable.containsKey(symbol));
    }

    public int GetAddress(String symbol) {
        return (symbolTable.get(symbol));
    }
}
