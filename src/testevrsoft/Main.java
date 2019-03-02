
package testevrsoft;

import database.MyConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import javax.swing.JOptionPane;
import view.JFrameMenu;

/**
 *
 * @author Matheus Assunção <matheus.tba@hotmail.com>
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {  
        try{
            Connection con = MyConnection.getInstance();    
            JFrameMenu.getInstance().setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e);
        }
        
    }
    
}
