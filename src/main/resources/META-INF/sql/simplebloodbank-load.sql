INSERT INTO `simplebloodbank`.`account` (`id`, `name`, `nickname`, `username`, `password`) VALUES ('1', 'Shariar', 'Shawn', 'emamis', '8288');
INSERT INTO `simplebloodbank`.`account` (`id`, `name`, `nickname`, `username`, `password`) VALUES ('2', 'Administrator', 'Admin', 'admin', 'admin');

INSERT INTO `simplebloodbank`.`person` (`id`, `first_name`, `last_name`, `phone`, `address`, `birth`) VALUES ('1', 'Shawn', 'Emami', '1234567890', 'Ottawa', '1900-01-01 12:01:01');
INSERT INTO `simplebloodbank`.`person` (`id`, `first_name`, `last_name`, `phone`, `address`, `birth`) VALUES ('2', 'Student', 'S.', '9876543210', 'Ontario', '1910-10-10 13:13:13');

INSERT INTO `simplebloodbank`.`blood_bank` (`bank_id`, `owner`, `name`, `privately_owned`, `established`, `emplyee_count`) VALUES ('1', '1', 'BloddyBank', b'1', '6666-07-13 13:07:13', '166616661');
INSERT INTO `simplebloodbank`.`blood_bank` (`bank_id`, `owner`, `name`, `privately_owned`, `established`, `emplyee_count`) VALUES ('2', null, 'Bank', b'0', '2020-01-01 12:00:00', '100');

INSERT INTO `simplebloodbank`.`blood_donation` (`donation_id`, `bank_id`, `milliliters`, `blood_group`, `rhd`, `created`) VALUES ('1', '1', '1000', 'A', '+', '2021-03-14 12:12:12');
INSERT INTO `simplebloodbank`.`blood_donation` (`donation_id`, `bank_id`, `milliliters`, `blood_group`, `rhd`, `created`) VALUES ('2', '2', '3000', 'O', '-', '2021-03-15 12:12:12');

INSERT INTO `simplebloodbank`.`donation_record` (`record_id`, `person_id`, `donation_id`, `tested`, `administrator`, `hospital`, `created`) VALUES ('1', '1', '1', b'1', 'Shawn', 'Waverly Hills Sanatorium', '1910-07-26 12:00:00');
INSERT INTO `simplebloodbank`.`donation_record` (`record_id`, `person_id`, `donation_id`, `tested`, `administrator`, `hospital`, `created`) VALUES ('2', null, null, b'0', 'Shawn', 'Waverly Hills Sanatorium', '1910-07-26 12:00:00');