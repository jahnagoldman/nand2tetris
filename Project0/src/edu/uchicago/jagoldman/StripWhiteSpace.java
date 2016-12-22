package edu.uchicago.jagoldman;

import java.io.*;

public class StripWhiteSpace {

    public static void main(String[] args) {
        if (args.length == 0) {
            return;
        } else {
            String strFileName = args[args.length - 1];
            boolean bComments = true;
            if (args[0].equals("no-comments")) {
                bComments = false;
            }
            stripWhiteSpace(strFileName, bComments);

        }

    }


    public static void stripWhiteSpace(String fileName, boolean bComments) {

        try (FileReader fr = new FileReader(fileName)) {
            BufferedReader br = new BufferedReader(fr);
            String fileNameSplit = fileName.substring(0, fileName.length() - 3);
            FileWriter fw = new FileWriter(fileNameSplit + ".out");
            int lineNumber = 0;
            String strLine;
            while ((strLine = br.readLine()) != null) {
                // check if comments should be removed
                if (shouldRemoveComments(strLine, bComments)) {
                    // remove comments
                    int offSet = strLine.indexOf("//");
                    strLine = strLine.substring(0, offSet).trim();

                }
                // remove spaces and go to new line
                // if line is empty after removing comments, want to skip over
                if (!strLine.equals("")) {
                    strLine = strLine.trim().replaceAll("\\s", "");
                    strLine = strLine + "\n";
                    fw.write(strLine);
                }
                lineNumber++;


            }
            fr.close();
            fw.close();


        } catch (FileNotFoundException e)

        {
            e.printStackTrace();
        } catch (IOException e)

        {
            e.printStackTrace();
        }
    }

    public static boolean shouldRemoveComments(String strLine, boolean bComments) {
        boolean bRemoveComments = false;
        if (!strLine.equals("") && !bComments && strLine.indexOf("//") != -1) {
            bRemoveComments = true;
        } else {
            bRemoveComments = false;
        }

        return bRemoveComments;


    }

}

