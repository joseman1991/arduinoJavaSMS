/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author JOSE
 */
public class LeerXLSX {

    private final int count;

    private final List<String[]> filas;

    public LeerXLSX() {
        this.count = 1;

        filas = new ArrayList<>();
    }

    // Métodos
    /**
     * Lee un CSV que no contiene el mismo caracter que el separador en su texto
     * y sin comillas que delimiten los campos
     *
     * @param path Ruta donde está el archivo
     * @throws IOException
     */
    public void leerXLSX(String path) throws IOException {

        // Abro el .csv en buffer de lectura
        FileInputStream fis = new FileInputStream(path);
        //InputStreamReader isr = new InputStreamReader(fis, "ISO-8859-1");

        XSSFWorkbook worbook = new XSSFWorkbook(fis);

        XSSFSheet sheet = worbook.getSheetAt(0);
        //obtener todas las filas de la hoja excel
        Iterator<Row> rowIterator = sheet.iterator();

        Row row;
        rowIterator.next();
        filas.clear();

        while (rowIterator.hasNext()) {
            String[] fila = new String[count];
            int i = 0;

            row = rowIterator.next();

            //se obtiene las celdas por fila
            Iterator<Cell> cellIterator = row.cellIterator();
            Cell cell;
            //se recorre cada celda

            while (cellIterator.hasNext()) {
                cell = cellIterator.next();
                String c;
                if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    c = NumberToTextConverter.toText(cell.getNumericCellValue());
                } else {
                    c = cell.getStringCellValue();
                }
                DecimalFormat df=new DecimalFormat("0000000000");
                fila[i] = df.format(Long.parseLong(c));
                i++;
            }

            filas.add(fila);
        }
//        

        // CIerro el buffer de lectura        
    }

    public List<String[]> getFilas() {
        return filas;
    }

}
