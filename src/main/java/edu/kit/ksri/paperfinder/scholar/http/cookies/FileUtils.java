package edu.kit.ksri.paperfinder.scholar.http.cookies;

/**
 * Created by janscheurenbrand on 10.08.14.
 */

import java.io.*;

public class FileUtils {
    public static String readFile(String path) {
        File myFile = new File(path);

        FileInputStream fIn;
        try {
            if (!myFile.exists()) {
                if (myFile.getParentFile() != null) {
                    myFile.getParentFile().mkdirs();
                }
                myFile.createNewFile();
                return "";
            }
            fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));
            String aDataRow = "";
            String aBuffer = "";
            while ((aDataRow = myReader.readLine()) != null) {
                aBuffer += aDataRow;
            }
            myReader.close();
            return aBuffer;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void writeFile(String content, String path) {
        File myFile = new File(path);
        try {
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(content);
            myOutWriter.close();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
