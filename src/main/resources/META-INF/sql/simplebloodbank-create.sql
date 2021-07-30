-- -----------------------------------------------------
-- Schema simplebloodbank
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `simplebloodbank` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `simplebloodbank` ;

-- -----------------------------------------------------
-- Table `simplebloodbank`.`person`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `simplebloodbank`.`person` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(50) NOT NULL,
  `last_name` VARCHAR(50) NOT NULL,
  `phone` VARCHAR(15) NOT NULL,
  `address` VARCHAR(100) NOT NULL,
  `birth` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `simplebloodbank`.`blood_bank`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `simplebloodbank`.`blood_bank` (
  `bank_id` INT NOT NULL AUTO_INCREMENT,
  `owner` INT NULL,
  `name` VARCHAR(100) NOT NULL,
  `privately_owned` BIT(1) NOT NULL,
  `established` DATETIME NOT NULL,
  `emplyee_count` INT NOT NULL,
  PRIMARY KEY (`bank_id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE,
  UNIQUE INDEX `bank_id_UNIQUE` (`bank_id` ASC) VISIBLE,
  INDEX `fk_blood_bank_person1_idx` (`owner` ASC) VISIBLE,
  CONSTRAINT `fk_blood_bank_person1`
    FOREIGN KEY (`owner`)
    REFERENCES `simplebloodbank`.`person` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `simplebloodbank`.`blood_donation`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `simplebloodbank`.`blood_donation` (
  `donation_id` INT NOT NULL AUTO_INCREMENT,
  `bank_id` INT NULL,
  `milliliters` INT NOT NULL,
  `blood_group` ENUM('A', 'B', 'AB', 'O') NOT NULL,
  `rhd` ENUM('+', '-') NOT NULL,
  `created` DATETIME NOT NULL,
  PRIMARY KEY (`donation_id`),
  INDEX `fk_blood_donation_blood_bank1_idx` (`bank_id` ASC) VISIBLE,
  UNIQUE INDEX `donation_id_UNIQUE` (`donation_id` ASC) VISIBLE,
  CONSTRAINT `fk_blood_donation_blood_bank1`
    FOREIGN KEY (`bank_id`)
    REFERENCES `simplebloodbank`.`blood_bank` (`bank_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `simplebloodbank`.`donation_record`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `simplebloodbank`.`donation_record` (
  `record_id` INT NOT NULL AUTO_INCREMENT,
  `person_id` INT NULL DEFAULT NULL,
  `donation_id` INT NULL DEFAULT NULL,
  `tested` BIT(1) NOT NULL,
  `administrator` VARCHAR(100) NOT NULL,
  `hospital` VARCHAR(100) NOT NULL,
  `created` DATETIME NOT NULL,
  PRIMARY KEY (`record_id`),
  UNIQUE INDEX `record_id_UNIQUE` (`record_id` ASC) VISIBLE,
  INDEX `fk_donation_record_person1_idx` (`person_id` ASC) VISIBLE,
  INDEX `fk_donation_record_blood_donation1_idx` (`donation_id` ASC) VISIBLE,
  CONSTRAINT `fk_donation_record_blood_donation1`
    FOREIGN KEY (`donation_id`)
    REFERENCES `simplebloodbank`.`blood_donation` (`donation_id`),
  CONSTRAINT `fk_donation_record_person1`
    FOREIGN KEY (`person_id`)
    REFERENCES `simplebloodbank`.`person` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `simplebloodbank`.`account`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `simplebloodbank`.`account` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `nickname` VARCHAR(45) NULL,
  `username` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE,
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE)
ENGINE = InnoDB;