/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista;

import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author JOSE
 */
public class AnchoColumna {

    private final JTable ds;
    private final int[] medidas;

    public AnchoColumna(JTable ds, int[] medidas) {
        this.ds = ds;
        this.medidas = medidas;
        medir();
    }

    private void medir() {
        int count = ds.getColumnCount();
        TableColumnModel model=ds.getColumnModel();
        for (int i = 0; i < count; i++) {
            model.getColumn(i).setPreferredWidth(medidas[i]);
        }
    }
    
    
    
    

}
