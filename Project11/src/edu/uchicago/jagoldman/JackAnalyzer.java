package edu.uchicago.jagoldman;

import java.io.File;
import java.util.ArrayList;

public class JackAnalyzer {

    public static void main(String[] args) {
        // input is the jack file
        File jackFileDir = new File(args[0]);
        ArrayList<File> files = new ArrayList<>();
        if (jackFileDir.isFile() && args[0].endsWith(".jack")) {
            files.add(jackFileDir);

        } else if (jackFileDir.isDirectory()) {
            files = getJackFiles(jackFileDir);
        }

        for (File file : files) {
            String fileOutName = file.toString().substring(0, file.toString().length() - 5) + ".vm";
            File fileOutFile = new File(fileOutName);
            // compile the files
            CompilationEngine compilationEngine = new CompilationEngine(file, fileOutFile);
            compilationEngine.compileClass();


        }


    }

    // obtain all jack files in a given directory
    public static ArrayList<File> getJackFiles(File jackFileDir) {
        File[] files = jackFileDir.listFiles();
        ArrayList<File> fResults = new ArrayList<>();
        if (files != null) for (File file : files) {
            if (file.getName().endsWith(".jack")) fResults.add(file);
        }
        return fResults;
    }

}
