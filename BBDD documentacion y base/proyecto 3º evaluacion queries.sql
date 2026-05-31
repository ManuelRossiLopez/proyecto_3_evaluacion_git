//queris database proyecto//
create DATABASE lovecode;
use lovecode;

create table usuarios (
id int auto_increment primary key,
nombre varchar(50) not null,
apellido1 varchar(50) not null,
apellido varchar(50) not null,
email varchar(120) unique not null,
contrasena varchar(50) not null,
fecha_nacimiento date
)engine=InnoDB;

create table tecnologias ( 
id int auto_increment primary key, 
nombre_tecnoligia varchar(120) not null, 
tipo varchar(120) not null 
)engine=InnoDB;	

create table usuarios_tecnologias(
id int auto_increment primary key,
id_usuarios int not null,
id_tecnologias int not null,
constraint fk_usuarios_tecnologias
foreign key (id_usuarios) references usuarios (id)
on update cascade
on delete restrict,
constraint fk_tecnologias_usuarios
foreign key (id_tecnologias) references tecnologias (id)
on update cascade
on delete restrict
)engine=InnoDB;	

CREATE TABLE likes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario_da INT NOT NULL,
    id_usuario_recibe INT NOT NULL,
    
    CONSTRAINT fk_like_usuario_da 
        FOREIGN KEY (id_usuario_da) 
        REFERENCES usuarios(id)
        ON UPDATE CASCADE 
        ON DELETE RESTRICT,
        
    CONSTRAINT fk_like_usuario_recibe 
        FOREIGN KEY (id_usuario_recibe) 
        REFERENCES usuarios(id)
        ON UPDATE CASCADE 
        ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE matches (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario1 INT NOT NULL,
    id_usuario2 INT NOT NULL,

    CONSTRAINT fk_match_usuario1
        FOREIGN KEY (id_usuario1)
        REFERENCES usuarios(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT fk_match_usuario2
        FOREIGN KEY (id_usuario2)
        REFERENCES usuarios(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE USER 'root_admin'@'localhost' IDENTIFIED BY 'admin123';
CREATE USER 'desarrollador'@'localhost' IDENTIFIED BY 'dev123';
CREATE USER 'lector'@'localhost' IDENTIFIED BY 'read123';

GRANT ALL PRIVILEGES ON tu_basedatos.* TO 'root_admin'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, ALTER, INDEX 
ON tu_basedatos.* 
TO 'desarrollador'@'localhost';
GRANT SELECT ON tu_basedatos.* TO 'lector'@'localhost';

FLUSH PRIVILEGES;


DELIMITER //

CREATE PROCEDURE conteo_matches(IN usuario_id INT)
BEGIN
    SELECT COUNT(*) AS total_matches
    FROM matches
    WHERE id_usuario1 = usuario_id 
       OR id_usuario2 = usuario_id;
END //

DELIMITER ;


DELIMITER //

CREATE PROCEDURE cargar_usuario(IN usuario_id INT)
BEGIN
    SELECT u.*, t.nombre_tecnoligia, t.tipo
    FROM usuarios u
    LEFT JOIN usuarios_tecnologias ut 
        ON u.id = ut.id_usuarios
    LEFT JOIN tecnologias t 
        ON ut.id_tecnologias = t.id
    WHERE u.id = usuario_id;
END //

DELIMITER ;


DELIMITER //

CREATE PROCEDURE borrar_usuario(IN usuario_id INT)
BEGIN
    
    DELETE FROM usuarios_tecnologias WHERE id_usuarios = usuario_id;

    
    DELETE FROM likes 
    WHERE id_usuario_da = usuario_id 
       OR id_usuario_recibe = usuario_id;

   
    DELETE FROM matches 
    WHERE id_usuario1 = usuario_id 
       OR id_usuario2 = usuario_id;

    
    DELETE FROM usuarios WHERE id = usuario_id;
END //

DELIMITER ;

DELIMITER //

CREATE TRIGGER generar_match
AFTER INSERT ON likes
FOR EACH ROW
BEGIN

    IF EXISTS (
        SELECT 1
        FROM likes
        WHERE id_usuario_da = NEW.id_usuario_recibe
          AND id_usuario_recibe = NEW.id_usuario_da
    ) THEN

        INSERT INTO matches (
            id_usuario1,
            id_usuario2
        )
        VALUES (
            NEW.id_usuario_da,
            NEW.id_usuario_recibe
        );

    END IF;

END//

DELIMITER ;