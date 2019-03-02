package model;

import database.MyConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Matheus Assunção <matheus.tba@hotmail.com>
 */
public class Curso {
    private int _codigo;
    private String _descricao;
    private String _ementa;
    private static Connection connection;
    private static ResultSet rs;
    private static PreparedStatement stmt;
    
    public Curso() {
        
    }
    
    public Curso(int _codigo, String _descricao, String _ementa) {
        this._codigo = _codigo;
        this._descricao = _descricao;
        this._ementa = _ementa; 
    }
    
    public List<Curso> getData() throws ClassNotFoundException {
        List<Curso> l_curso = new ArrayList();
        Curso dao_curso;
        try {
            connection = MyConnection.getInstance();
            stmt = connection.prepareStatement("select * from curso order by codigo");
            rs = stmt.executeQuery();
            while (rs.next()) {
                dao_curso = new Curso();
                dao_curso.setCodigo(rs.getInt("codigo"));
                dao_curso.setDescricao(rs.getString("descricao"));
                dao_curso.setEmenta(rs.getString("ementa"));
                l_curso.add(dao_curso);
            }
            MyConnection.close(connection, stmt, rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return l_curso;
    }
    
    public static Curso insertOrUpdateData(Curso curso) throws SQLException, ClassNotFoundException {
        if (curso.getCodigo() == 0) {
            curso = Curso.insertData(curso);
        } else {
            curso = Curso.updateData(curso);
        }
        
        return curso;
    }
    
    public static Curso insertData(Curso curso) throws SQLException, ClassNotFoundException {
        connection = MyConnection.getInstance();
        stmt = connection.prepareStatement("insert into curso (descricao, ementa) values (?, ?);");
        stmt.setString(1, curso.getDescricao());
        stmt.setString(2, curso.getEmenta());
        stmt.executeUpdate();
        MyConnection.close(connection, stmt, null);
        return curso;
    }
    
    public static Curso updateData(Curso curso) throws SQLException, ClassNotFoundException {
        connection = MyConnection.getInstance();
        stmt = connection.prepareStatement("update curso set descricao = ?, ementa = ? where codigo = ?;");
        stmt.setString(1, curso.getDescricao());
        stmt.setString(2, curso.getEmenta());
        stmt.setInt(3, curso.getCodigo());
        stmt.executeUpdate();
        MyConnection.close(connection, stmt, null);
        return curso;
    }
    
    public static void deleteData(Curso curso) throws SQLException, ClassNotFoundException {
        connection = MyConnection.getInstance();
        stmt = connection.prepareStatement("delete from curso where codigo = ?;");
        stmt.setInt(1, curso.getCodigo());
        stmt.executeUpdate();
        MyConnection.close(connection, stmt, null);
    }
    
    public int getCodigo() {
        return _codigo;
    }

    public String getStringCodigo() {
        return Integer.toString(_codigo);
    }
    
    public void setCodigo(int _codigo) {
        this._codigo = _codigo;
    }

    public String getDescricao() {
        return _descricao;
    }

    public void setDescricao(String _descricao) {
        this._descricao = _descricao;
    }

    public String getEmenta() {
        return _ementa;
    }

    public void setEmenta(String _ementa) {
        this._ementa = _ementa;
    }

    @Override
    public String toString() {
        return _descricao;
    }
    
    
}
