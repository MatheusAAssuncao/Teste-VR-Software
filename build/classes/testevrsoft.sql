CREATE TABLE aluno (
  codigo int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  nome varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE curso (
  codigo int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  descricao varchar(50) NOT NULL,
  ementa text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE curso_aluno (
  codigo int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  codigo_aluno int(11) NOT NULL,
  codigo_curso int(11) NOT NULL,
  CONSTRAINT fk_curso_aluno_aluno FOREIGN KEY (codigo_aluno) REFERENCES aluno (codigo) ON DELETE CASCADE,
  CONSTRAINT fk_curso_aluno_curso FOREIGN KEY (codigo_curso) REFERENCES curso (codigo)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;