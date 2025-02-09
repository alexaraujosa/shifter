-- Universidade do Minho
-- Desenvolvimento de Sistemas de Software
-- Grupo: 56

-- ----------------------------------------------------
-- DATABASE SETUP
-- ----------------------------------------------------
-- DROP DATABASE `DSS`;
CREATE DATABASE `DSS`;
CREATE USER 'batman'@'localhost' IDENTIFIED WITH 'mysql_native_password' BY '88k%NryV&C2zkmQ8%8B^m8aHE!6pxG3r';
GRANT ALL PRIVILEGES ON *.* TO 'batman'@'localhost';
FLUSH PRIVILEGES;

-- ----------------------------------------------------
-- TABLE `DSS`.`Diretor`
-- ----------------------------------------------------
CREATE TABLE IF NOT EXISTS `DSS`.`Diretor` (
	`palavraPasse` VARCHAR(40) NOT NULL,
    PRIMARY KEY (`palavraPasse`)
);

-- ----------------------------------------------------
-- TABLE `DSS`.`Aluno`
-- ----------------------------------------------------
CREATE TABLE IF NOT EXISTS `DSS`.`Aluno` (
	`numMec` INT NOT NULL,
    `nome` VARCHAR(60) NOT NULL,
    `estatuto` BOOL NOT NULL,
    PRIMARY KEY (`numMec`)
);
-- CONSTRAINT `chk_estatuto` CHECK (`estatuto` BETWEEN 0 AND 1)

-- ----------------------------------------------------
-- TABLE `DSS`.`UC`
-- ----------------------------------------------------
CREATE TABLE IF NOT EXISTS `DSS`.`UC` (
	`codigo` CHAR(6) NOT NULL,
    `nome` VARCHAR(60) NOT NULL,
    `ano` TINYINT NOT NULL,
    PRIMARY KEY (`codigo`),
    CONSTRAINT `chk_ano` CHECK (`ano` BETWEEN 1 AND 3)
);

-- ----------------------------------------------------
-- TABLE `DSS`.`InscritosUC`
-- ----------------------------------------------------
CREATE TABLE IF NOT EXISTS `DSS`.`InscritosUC` (
	`codigo` CHAR(6) NOT NULL,
    `numMec` INT NOT NULL,
    PRIMARY KEY (`codigo`, `numMec`),
	CONSTRAINT `fk_Codigo`
		FOREIGN KEY (`codigo`)
        REFERENCES `DSS`.`UC` (`codigo`)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION,
	CONSTRAINT `fk_numMec`
		FOREIGN KEY (`numMec`)
        REFERENCES `DSS`.`Aluno` (`numMec`)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION
);

-- ----------------------------------------------------
-- TABLE `DSS`.`Turno`
-- ----------------------------------------------------
CREATE TABLE IF NOT EXISTS `DSS`.`Turno` (
	`id` VARCHAR(4) NOT NULL,
    `dia` TINYINT NOT NULL,
    `inicio` TIME NOT NULL,
    `fim` TIME NOT NULL,
    `tipo` TINYINT NOT NULL,
    `codigo` CHAR(6) NOT NULL,
    `capacidadeMaxima` INT NOT NULL,
    PRIMARY KEY (`id`, `codigo`),
    CONSTRAINT `chk_dia` CHECK(`dia` BETWEEN 1 AND 7),
    CONSTRAINT `chk_tipo` CHECK(`tipo` BETWEEN 1 AND 3),
    CONSTRAINT `fk_CodigoUC`
		FOREIGN KEY (`codigo`)
        REFERENCES `DSS`.`UC` (`codigo`)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION
);

-- ----------------------------------------------------
-- TABLE `DSS`.`InscritosTurnoUC`
-- ----------------------------------------------------
CREATE TABLE IF NOT EXISTS `DSS`.`InscritosTurnoUC` (
	`id` VARCHAR(4) NOT NULL,
    `codigo` CHAR(6) NOT NULL,
    `numMec` INT NOT NULL,
    PRIMARY KEY (`id`, `codigo`, `numMec`),
    CONSTRAINT `fk_CodigoUCTurno`
		FOREIGN KEY (`codigo`)
        REFERENCES `DSS`.`UC` (`codigo`)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION,
	CONSTRAINT `fk_IdTurno`
		FOREIGN KEY (`id`, `codigo`)
        REFERENCES `DSS`.`Turno` (`id`, `codigo`)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION,
	CONSTRAINT `fk_numMecUCTurno`
		FOREIGN KEY (`numMec`)
        REFERENCES `DSS`.`Aluno` (`numMec`)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION
);
