package model;

import database.MyConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Matheus Assunção <matheus.tba@hotmail.com>
 */
public class Aluno {

    private int _codigo;
    private String _nome;
    private static Connection connection;
    private static ResultSet rs;
    private static PreparedStatement stmt;
    
    public Aluno() {
    }

    public Aluno(int _codigo, String _nome) {
        this._codigo = _codigo;
        this._nome = _nome;
    }
    
    public List<Aluno> getData() throws ClassNotFoundException {
        List<Aluno> l_aluno = new ArrayList();
        Aluno dao_aluno;
        try {
            connection = MyConnection.getInstance();
            stmt = connection.prepareStatement("select * from aluno order by codigo");
            rs = stmt.executeQuery();
            while (rs.next()) {
                dao_aluno = new Aluno();
                dao_aluno.setCodigo(rs.getInt("codigo"));
                dao_aluno.setNome(rs.getString("nome"));
                l_aluno.add(dao_aluno);
            }
            MyConnection.close(connection, stmt, rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return l_aluno;
    }

    public List<Aluno> getDataByNome(String texto) throws ClassNotFoundException {
        List<Aluno> l_aluno = new ArrayList();
        Aluno dao_aluno;
        try {
            connection = MyConnection.getInstance();
            stmt = connection.prepareStatement("select * from aluno where nome like ? order by codigo");
            stmt.setString(1, texto + '%');
            rs = stmt.executeQuery();
            while (rs.next()) {
                dao_aluno = new Aluno();
                dao_aluno.setCodigo(rs.getInt("codigo"));
                dao_aluno.setNome(rs.getString("nome"));
                l_aluno.add(dao_aluno);
            }
            MyConnection.close(connection, stmt, rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return l_aluno;
    }

    public static void insertOrUpdateData(Aluno aluno, List<CursoAluno> l_cursoAluno, List<CursoAluno> l_cursoAlunoExcluidos) throws SQLException, ClassNotFoundException {
        if (aluno.getCodigo() == 0) {
            Aluno.insertData(aluno, l_cursoAluno);
        } else {
            Aluno.updateData(aluno, l_cursoAluno, l_cursoAlunoExcluidos);
        }
    }
    
    private static void insertData(Aluno aluno, List<CursoAluno> l_cursoAluno) throws SQLException, ClassNotFoundException {
        connection = MyConnection.getInstance();
        connection.setAutoCommit(false);
        stmt = connection.prepareStatement("insert into aluno (nome) values (?);", Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, aluno.getNome());
        stmt.executeUpdate();
        if (l_cursoAluno.size() > 0) {
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int lastAlunoId = rs.getInt(1);
                
                for (int i = 0; i < l_cursoAluno.size(); i++) {
                    stmt = null;
                    stmt = connection.prepareStatement("insert into curso_aluno (codigo_aluno, codigo_curso) values (?, ?);");
                    stmt.setInt(1, lastAlunoId);
                    stmt.setInt(2, l_cursoAluno.get(i).getCodigoCurso());
                    stmt.executeUpdate();
                }
            }
        }
        connection.commit();
        MyConnection.close(connection, stmt, rs);
    }
    
    private static void updateData(Aluno aluno, List<CursoAluno> l_cursoAluno, List<CursoAluno> l_cursoAlunoExcluidos) throws SQLException, ClassNotFoundException {
        connection = MyConnection.getInstance();
        connection.setAutoCommit(false);
        stmt = connection.prepareStatement("update aluno set nome = ? where codigo = ?;");
        stmt.setString(1, aluno.getNome());
        stmt.setInt(2, aluno.getCodigo());
        stmt.executeUpdate();
        for (int i = 0; i < l_cursoAluno.size(); i++) {
            stmt = null;
            if (l_cursoAluno.get(i).getCodigo() == 0) {
                stmt = connection.prepareStatement("insert into curso_aluno (codigo_aluno, codigo_curso) values (?, ?);");
                stmt.setInt(1, aluno.getCodigo());
                stmt.setInt(2, l_cursoAluno.get(i).getCodigoCurso());   
                stmt.executeUpdate();
            }
        }
        for (int i = 0; i < l_cursoAlunoExcluidos.size(); i++) {
            stmt = null;
            stmt = connection.prepareStatement("delete from curso_aluno where codigo = ?;");
            stmt.setInt(1, l_cursoAluno.get(i).getCodigo());   
            stmt.executeUpdate();
        }
        connection.commit();
        MyConnection.close(connection, stmt, rs);
    }
    
    public static void deleteData(Aluno aluno) throws SQLException, ClassNotFoundException {
        connection = MyConnection.getInstance();
        stmt = connection.prepareStatement("delete from aluno where codigo = ?;");
        stmt.setInt(1, aluno.getCodigo());
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

    public String getNome() {
        return _nome;
    }

    public void setNome(String _nome) {
        this._nome = _nome;
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void setConnection(Connection connection) {
        Aluno.connection = connection;
    }

    public static ResultSet getRs() {
        return rs;
    }

    public static void setRs(ResultSet rs) {
        Aluno.rs = rs;
    }

    public static PreparedStatement getStmt() {
        return stmt;
    }

    public static void setStmt(PreparedStatement stmt) {
        Aluno.stmt = stmt;
    }
    
}
