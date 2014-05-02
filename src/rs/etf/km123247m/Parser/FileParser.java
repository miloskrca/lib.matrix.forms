package rs.etf.km123247m.Parser;

import rs.etf.km123247m.Matrix.Matrix;

import java.io.*;
import java.net.URL;

/**
 * Created by Miloš Krsmanović.
 * 2014
 * <p/>
 * package: rs.etf.km123247m.IParser
 */
public class FileParser implements IParser {

    private String fileName;

    private StringParser stringParser;

    public FileParser() {
        this.stringParser = new StringParser();
    }
    public FileParser(String fileName) throws Exception {
        this();
        this.setFileName(fileName);
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) throws Exception {
        URL url = getClass().getResource(fileName);
        File f = new File(url.getPath());
        if(f.exists()) {
            if(!f.isDirectory()) {
                throw new Exception("File is a directory!");
            } else {
                this.fileName = fileName;
            }
        } else {
            throw new Exception("File doesn't exist!");
        }
    }

    @SuppressWarnings("InfiniteRecursion")
    @Override
    public Matrix parseMatrix() {

        Matrix matrix = null;
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(new File(
                    this.getFileName())));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line).append(" ");
            }
            this.getStringParser().setMatrixString(sb.toString());

            matrix = this.parseMatrix();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return matrix;
    }

    public StringParser getStringParser() {
        return stringParser;
    }
}
