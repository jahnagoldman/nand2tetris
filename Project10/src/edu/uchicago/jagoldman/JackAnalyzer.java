package edu.uchicago.jagoldman;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class JackAnalyzer {

    public static void main(String[] args) {
        // input is the jack file
        File jackFileDir = new File(args[0]);
        File fileOut;
        String fileNameOut = "";
        ArrayList<File> files = new ArrayList<>();
        if (jackFileDir.isFile() && args[0].endsWith(".jack")) {
            files.add(jackFileDir);
            fileNameOut = args[0].substring(0, args[0].length() - 5);

        }
        // doesnt currently work for a directory as a whole, only individual files
        else if (jackFileDir.isDirectory()) {
            files = getJackFiles(jackFileDir);
            fileNameOut = args[0];

        }
        // output xml file
        fileNameOut = fileNameOut + ".xml";

        fileOut = new File(fileNameOut);
        FileWriter fw = null;
        try {
            fw = new FileWriter(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (File file : files) {
            String fileOutName = file.toString().substring(0, file.toString().length() - 5) + ".xml";
            File fileOutFile = new File(fileOutName);
            // compile the files
            CompilationEngine compilationEngine = new CompilationEngine(file, fileOutFile);
            compilationEngine.compileClass();


        }

        try {
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
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
