package reader;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XLSReader implements Reader {

    private String fileName;

    public XLSReader(String fileName){
        this.fileName = fileName;
    }

    @Override
    public List<XSSFRow> readData(String name) {
        try (FileInputStream input = new FileInputStream(new File("./resources/" + this.fileName));
            XSSFWorkbook workbooks = new XSSFWorkbook(input)) {
            XSSFSheet currentSheet = workbooks.getSheet(name);
            Iterator<Row> rowIterator = currentSheet.iterator();
            List<XSSFRow> data = new ArrayList<>();
            while (rowIterator.hasNext()) {
                XSSFRow row = (XSSFRow) rowIterator.next();
                data.add(row);
            }
            return data;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
