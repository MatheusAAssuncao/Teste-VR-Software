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
public class CursoAluno {
    private int _codigo;
    private int _codigoAluno;
    private int _codigoCurso;
    private String _descricaoCurso;
    private static Connection connection;
    private static ResultSet rs;
    private static PreparedStatement stmt;

    public CursoAluno() {
    }

    public CursoAluno(int _codigo, int _codigoAluno, int _codigoCurso, String _descricaoCurso) {
        this._codigo = _codigo;
        this._codigoAluno = _codigoAluno;
        this._codigoCurso = _codigoCurso;
        this._descricaoCurso = _descricaoCurso;
    }

    public List<CursoAluno> getDataByAluno(int codAluno) throws ClassNotFoundException, SQLException {
        List<CursoAluno> l_cursoAluno = new ArrayList();
        CursoAluno dao_cursoAluno;
        try {
            connection = MyConnection.getInstance();
            String sql = "select "
                    + "curso_aluno.codigo"
                    + ",curso_aluno.codigo_curso"
                    + ",curso_aluno.codigo_aluno"
                    + ",curso.descricao as descricaoCurso"
                    + " from curso_aluno "
                    + " inner join curso on curso.codigo = curso_aluno.codigo_curso "
                    + " where curso_aluno.codigo_aluno = ? "
                    + "order by curso_aluno.codigo";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, codAluno);
            rs = stmt.executeQuery();
            while (rs.next()) {
                dao_cursoAluno = new CursoAluno();
                dao_cursoAluno.setCodigo(rs.getInt("codigo"));
                dao_cursoAluno.setCodigoAluno(rs.getInt("codigo_aluno"));
                dao_cursoAluno.setCodigoCurso(rs.getInt("codigo_curso"));
                dao_cursoAluno.setDescricaoCurso(rs.getString("descricaoCurso"));
                l_cursoAluno.add(dao_cursoAluno);
            }
            MyConnection.close(connection, stmt, rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return l_cursoAluno;
    }
    
    public int getCodigo() {
        return _codigo;
    }

    public void setCodigo(int _codigo) {
        this._codigo = _codigo;
    }

    public int getCodigoAluno() {
        return _codigoAluno;
    }

    public void setCodigoAluno(int _codigoAluno) {
        this._codigoAluno = _codigoAluno;
    }

    public int getCodigoCurso() {
        return _codigoCurso;
    }

    public void setCodigoCurso(int _codigoCurso) {
        this._codigoCurso = _codigoCurso;
    }
    
    public String getDescricaoCurso() {
        return _descricaoCurso;
    }

    public void setDescricaoCurso(String _descricaoCurso) {
        this._descricaoCurso = _descricaoCurso;
    }
    
    
}
