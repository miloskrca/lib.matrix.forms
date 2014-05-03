package rs.etf.km123247m.Parser.ParserTypes;

import java.io.*;

/**
 * Created by Miloš Krsmanović.
 * 2014
 * <p/>
 * package: rs.etf.km123247m.IParser
 */
public abstract class FileParser implements IParser {

    private File file;
    private StringParser stringParser;

    public FileParser(File file) {
        this.setFile(file);
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public StringParser getStringParser() {
        return stringParser;
    }

    @Override
    public Object parseInput() throws Exception {
        this.stringParser = instantiateStringParser();

        Object object = null;
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(
                    getFile()));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line).append(" ");
            }
            getStringParser().setInputString(sb.toString());

            object = getStringParser().parseInput();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
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

        return object;
    }

    protected abstract StringParser instantiateStringParser();
}
