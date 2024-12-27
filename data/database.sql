-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema paymybuddy
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `paymybuddy` DEFAULT CHARACTER SET utf8 ;
USE `paymybuddy` ;

-- -----------------------------------------------------
-- Table `paymybuddy`.`USER`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `paymybuddy`.`USER` (
  `USER_ID` INT NOT NULL AUTO_INCREMENT,
  `USERNAME` VARCHAR(45) NOT NULL,
  `EMAIL` VARCHAR(45) NOT NULL,
  `PASSWORD` VARCHAR(250) NOT NULL,
  PRIMARY KEY (`USER_ID`),
  UNIQUE INDEX `EMAIL_UNIQUE` (`EMAIL` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `paymybuddy`.`TRANSACTION`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `paymybuddy`.`TRANSACTION` (
  `TRANSACTION_ID` INT NOT NULL AUTO_INCREMENT,
  `DESCRIPTION` VARCHAR(45) NULL,
  `AMOUNT` DOUBLE NULL,
  `SENDER_ID` INT NOT NULL,
  `RECEIVER_ID` INT NOT NULL,
  PRIMARY KEY (`TRANSACTION_ID`),
  INDEX `fk_TRANSACTION_USER_idx` (`SENDER_ID` ASC) VISIBLE,
  INDEX `fk_TRANSACTION_USER1_idx` (`RECEIVER_ID` ASC) VISIBLE,
  CONSTRAINT `fk_TRANSACTION_USER`
    FOREIGN KEY (`SENDER_ID`)
    REFERENCES `paymybuddy`.`USER` (`USER_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_TRANSACTION_USER1`
    FOREIGN KEY (`RECEIVER_ID`)
    REFERENCES `paymybuddy`.`USER` (`USER_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `paymybuddy`.`USER_CONNECTIONS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `paymybuddy`.`USER_CONNECTIONS` (
  `USER_ID` INT NOT NULL,
  `USER_TO` INT NOT NULL,
  PRIMARY KEY (`USER_ID`, `USER_TO`),
  INDEX `fk_USER_has_USER_USER2_idx` (`USER_TO` ASC) VISIBLE,
  INDEX `fk_USER_has_USER_USER1_idx` (`USER_ID` ASC) VISIBLE,
  CONSTRAINT `fk_USER_has_USER_USER1`
    FOREIGN KEY (`USER_ID`)
    REFERENCES `paymybuddy`.`USER` (`USER_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_USER_has_USER_USER2`
    FOREIGN KEY (`USER_TO`)
    REFERENCES `paymybuddy`.`USER` (`USER_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;


insert into user (username, email, password) values ('dbuser', 'dbuser@gmail.com' , '$2y$10$.qkbukzzX21D.bqbI.B2R.tvWP90o/Y16QRWVLodw51BHft7ZWbc.');
insert into user (username, email, password) values ('test', 'test@gmail.com' , '$2y$10$.qkbukzzX21D.bqbI.B2R.tvWP90o/Y16QRWVLodw51BHft7ZWbc.');

insert into user_connections (user_id, user_to) values (1, 2);

insert into transaction (transaction_id, description, amount, sender_id, receiver_id) values (1, "test", 10, 1, 2);