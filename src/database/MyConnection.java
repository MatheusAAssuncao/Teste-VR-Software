package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Matheus Assunção <matheus.tba@hotmail.com>
 */
public abstract class MyConnection {
    private static Connection sInstance = null;
    private static Connection connection = null;
    
    public static Connection getInstance() {
        if (sInstance == null) 
            return sInstance = createConnection();        
        return sInstance;
    }

    private static Connection createConnection() {
        try {
            String driverName = "com.mysql.jdbc.Driver";
            Class.forName(driverName);
            if (verificaBDExistente()) {
                connection = DriverManager.getConnection("jdbc:mysql://localhost/testevrsoft", "root", "");  
            } else {
                connection = DriverManager.getConnection("jdbc:mysql://localhost/", "root", ""); 
                Statement stmt;
                stmt = connection.createStatement();
                String sql;
                int result1 = stmt.executeUpdate("CREATE DATABASE testevrsoft");
                close(connection, stmt, null);
                if (result1 != 1) {
                    JOptionPane.showMessageDialog(null, "Erro ao criar o banco de dados.");
                } else {
                    connection = DriverManager.getConnection("jdbc:mysql://localhost/testevrsoft", "root", "");
                    stmt = connection.createStatement();
                    sql = "CREATE TABLE aluno ( " +
                           "codigo int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY " +
                           ",nome varchar(50) NOT NULL) " +
                           "ENGINE=InnoDB DEFAULT CHARSET=latin1";
                    result1 = stmt.executeUpdate(sql);
                    sql = "CREATE TABLE curso ( " +
                           "codigo int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY " +
                           ",descricao varchar(50) NOT NULL " +
                           ",ementa text NOT NULL " +
                           ")ENGINE=InnoDB DEFAULT CHARSET=latin1;";
                    result1 = stmt.executeUpdate(sql);
                    sql = "CREATE TABLE curso_aluno ( " +
                           "codigo int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY " +
                           ",codigo_aluno int(11) NOT NULL " +
                           ",codigo_curso int(11) NOT NULL " +
                           ",CONSTRAINT fk_curso_aluno_aluno FOREIGN KEY (codigo_aluno) REFERENCES aluno (codigo) ON DELETE CASCADE " +
                           ",CONSTRAINT fk_curso_aluno_curso FOREIGN KEY (codigo_curso) REFERENCES curso (codigo)) " +
                           "ENGINE=InnoDB DEFAULT CHARSET=latin1;";
                    result1 = stmt.executeUpdate(sql);   
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e);
        }
        return connection;
    }
    
    public static boolean verificaBDExistente() throws ClassNotFoundException, SQLException {
        List<String> list = new ArrayList<>();
        connection = DriverManager.getConnection("jdbc:mysql://localhost", "root", "");
        DatabaseMetaData meta = connection.getMetaData();
        ResultSet rs = meta.getCatalogs();
        while (rs.next()) {
            String listofDatabases = rs.getString("TABLE_CAT");
            list.add(listofDatabases);
        }
        close(connection, null, rs);
        return list.contains("testevrsoft");
    }
    
    public static void close(Connection conn, Statement sttm, ResultSet rs) throws ClassNotFoundException, SQLException {
        try {
            if (conn != null) {
                conn.close();
            }
            if (sttm != null) {
                sttm.close();
            }
            if (rs != null) {
                rs.close();
            }
            sInstance = null;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e);
        }
    }
}
