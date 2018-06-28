-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               10.2.15-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             9.4.0.5125
-- --------------------------------------------------------

-- YOU MUST ENTER IN AT LEAST ONE ROW PER TABLE TO DATABASE BEFORE IT WILL WORK

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dumping database structure for helpdesk
CREATE DATABASE IF NOT EXISTS `helpdesk` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `helpdesk`;

-- Dumping structure for table helpdesk.category
CREATE TABLE IF NOT EXISTS `category` (
  `CategoryId` int(11) NOT NULL AUTO_INCREMENT,
  `CategoryName` varchar(50) NOT NULL,
  PRIMARY KEY (`CategoryId`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- Data exporting was unselected.
-- Dumping structure for table helpdesk.filedetails
CREATE TABLE IF NOT EXISTS `filedetails` (
  `FileDetailId` int(11) NOT NULL AUTO_INCREMENT,
  `FileExtension` varchar(21000) DEFAULT NULL,
  `TicketsId` int(11) NOT NULL,
  `AddedFiles` blob DEFAULT NULL,
  PRIMARY KEY (`FileDetailId`),
  KEY `ticket_filedetails_fk` (`TicketsId`),
  CONSTRAINT `ticket_filedetails_fk` FOREIGN KEY (`TicketsId`) REFERENCES `ticket` (`TicketsId`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;

-- Data exporting was unselected.
-- Dumping structure for table helpdesk.inventory
CREATE TABLE IF NOT EXISTS `inventory` (
  `InventoryId` int(11) NOT NULL AUTO_INCREMENT,
  `ComputerName` varchar(15) NOT NULL,
  `AssetTagNumber` int(11) NOT NULL,
  `CurrentUser` varchar(50) NOT NULL,
  `BuildingLocation` varchar(50) NOT NULL,
  `LocationId` int(11) NOT NULL,
  PRIMARY KEY (`InventoryId`),
  KEY `location_inventory_fk` (`LocationId`),
  CONSTRAINT `location_inventory_fk` FOREIGN KEY (`LocationId`) REFERENCES `location` (`LocationId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=184 DEFAULT CHARSET=utf8;

-- Data exporting was unselected.
-- Dumping structure for table helpdesk.location
CREATE TABLE IF NOT EXISTS `location` (
  `LocationId` int(11) NOT NULL AUTO_INCREMENT,
  `LocationName` varchar(50) NOT NULL,
  `RegionId` int(11) NOT NULL,
  PRIMARY KEY (`LocationId`),
  KEY `region_location_fk` (`RegionId`),
  CONSTRAINT `region_location_fk` FOREIGN KEY (`RegionId`) REFERENCES `region` (`RegionId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

-- Data exporting was unselected.
-- Dumping structure for table helpdesk.priority
CREATE TABLE IF NOT EXISTS `priority` (
  `PriorityId` int(11) NOT NULL AUTO_INCREMENT,
  `PriorityName` varchar(25) NOT NULL,
  PRIMARY KEY (`PriorityId`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- Data exporting was unselected.
-- Dumping structure for table helpdesk.region
CREATE TABLE IF NOT EXISTS `region` (
  `RegionId` int(11) NOT NULL AUTO_INCREMENT,
  `RegionName` varchar(50) NOT NULL,
  PRIMARY KEY (`RegionId`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- Data exporting was unselected.
-- Dumping structure for table helpdesk.reply
CREATE TABLE IF NOT EXISTS `reply` (
  `ReplyId` int(11) NOT NULL AUTO_INCREMENT,
  `Reply` varchar(255) NOT NULL,
  `TicketsId` int(11) NOT NULL,
  PRIMARY KEY (`ReplyId`),
  KEY `reply_ticket_fk` (`TicketsId`),
  CONSTRAINT `reply_ticket_fk` FOREIGN KEY (`TicketsId`) REFERENCES `ticket` (`TicketsId`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8;

-- Data exporting was unselected.
-- Dumping structure for table helpdesk.siteadmin
CREATE TABLE IF NOT EXISTS `siteadmin` (
  `SiteAdminId` int(11) NOT NULL AUTO_INCREMENT,
  `SiteAdminName` varchar(50) NOT NULL,
  `EmailAddress` varchar(50) NOT NULL,
  `SiteRole` varchar(15) NOT NULL,
  `PhoneNumber` varchar(15) NOT NULL,
  `LocationId` int(11) NOT NULL,
  `UserName` varchar(20) NOT NULL,
  `Password` varbinary(50) NOT NULL,
  `PasswordSalt` varbinary(50) NOT NULL,
  `Flag` varchar(5) NOT NULL DEFAULT 'True',
  `Picture` longblob DEFAULT '0x89504E470D0A1A0A0000000D49484452000000E1000000E10803000000096D224800000039504C5445E4E6E7AEB4B7A9B0B3E8EAEBABB1B5C9CDCFE9EBECAFB5B8E2E4E5A8AEB2B6BBBED9DCDDB8BDC0CED1D3E0E2E3C0C5C7CCCFD1C3C7CAD4D7D8701C36500000054249444154789CED9DDB9AAB200C8585403DA156DFFF61B7D6B6336DED1E81A4049BFF6AE6AEEB0B2431C2B228044110044110044110044110044110044110044110044110044110044138200050F4EDD42D4CEDE5DF0331AB99AC53BAD6E6C2FC8772E3541D4525F4E7461BF582D68DEDF31709D06DCA5B31BAE98AAC350258F356DE4DE498B146B0EFC3F74B639DAB4698D40E7D6B1CBB0C2542E1F43E7D178D4D9F9B469876C6EFAEF19C9744183D02B8A25D56121BCF085EA2A8AAD43F7B3795BFBC55639B4918FB8000AEE83C24860BCC44621521709198FAF7FF4D8CBE4522F774034DA442A578AF5318A2D6E882615D1761F22EF4AF68CB5862852050A99A6FB601872150A9926B10A14309E1BC15D9AED3E82C738369C980114DA1E2994FAB1A4D20CFD606063C814A350C83D823869065103177E102C39D882B50D57D6A414F4087ACD08CCC8288F04CF10C33853D523BF3839E526B7A002CF2229D1958059160912A734AADEA01F445CAED21CA7786BF076353ABFA058CF8027915FD5349A1D0305208A83DE90DCDA8ADC1AF860B865145A44834BC8619964220A79A4F934A392553AC29E2337CA68A143D1B2F8534E57026B5B03B540A4D6A61778046A028FC20C75FA5C7CF3454D582CF30EA0B2A7EFCDBFB4DF8746DC7EFBCBFE0E909F7BDD35D21A327E0E34F31BE601285FAFEF706A3544A946A384D84910E433DC26BAA4FF26686D322FD82B76BC77F434AB04C792DD259E1F9E82715D04F9B706A6856B08B3EAB72BF829B6B189EFAFA82937BA88F501C43883BCB60B80B2FA0ED4476C7F6AEA09DDE63580BAFA035365C0562550C9E696605E5C605A711DB2B18F9946529FC01A267527C5E38BD235220BF8EFB85986BC0DC86336F68636E3A4FBC37E10AB4C109350F8145F842AD73B8AB7E25C4354265E41AB1D4C500E78FD43FDA0FEF2E9CF5FDE64DA06A7C1C78CAEC1C788A4B18F7C631BF00AE008C7B9CB0941E3275C22A16B3A8F14F373333E4B8407F80A26BEAFF38D29536DFF8DD00E8EDA6EDDE2C6F6CF37715BC00504D63532FC690EBC2D4755D0EDD014C137F03A753D14EDDD9DA7337B5159C0EA5EE2B800D52FF2614E06A3D7BB6C3E09AA65C691AE786D1CEABB59F576BB64AE7DFDE4FD6354AEBC57A76A310CE19479BD28D5D5BE5B62BE7B0B4DD50D6EF0BE183D4C57ED7D96C4C772F9EC18DDED5AE3DA0750E0504A0B54E874F85E7266098F83639B3BCB1F48FDD6B2C5D57311409A776D8FDB0F417A66667A00C9555B847864CED26367B126072F18BF315AD2C8BD53A3F1E9524B780D5927886E4F3C51D8FB8716897542314BB0CBB2335366D2A830CD867488EA231491C3D4669F11ADDE76739D092E5976D8DE3676B0714FE53FB488CFAE47BA913864DA937DA7DAACFF173CC47D5F8990F0CA0F8CC864A1C3E21D0DF311F11A3A80B075461EF3EF1D0B4DF5080985308581229EF614047720BCF1353D2094CBA057F43D4E104BC9BA782E63311E0D2EFC13B14125909A4387A03A9ABC433D81299457001F7631F54A6095118C4739A14F72731C013387128F41BA09D97A6F10C44C0203570F147B7C9C079606499656E609C0A4FF9C0BB038CADC838820AE3FA099541121AD1EBB4655A287E885CA78CF3E88D386702F4EF015010654D00ACF3E89598EF7B72ED479F880822642130228859ECC285606FBE0C12E94A703A0DBFA1F56902AFEFD318789110DAD864B20B5570774A6386484350EB96D1220DFCD81E95FD3109A60B50C8763AB349C8C4269772BF12F061A1ACB66190DF0B9983350D016EAE34A6AB74F81B65E6956842520DEF21E22BFE353FAF54AA027C896CADB3A2F615B87DDB8C33FE0A054110044110044110044110844FF20FD7E454E6B50216160000000049454E44AE426082',
  PRIMARY KEY (`SiteAdminId`),
  KEY `location_siteadmin_fk` (`LocationId`),
  CONSTRAINT `location_siteadmin_fk` FOREIGN KEY (`LocationId`) REFERENCES `location` (`LocationId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

-- Data exporting was unselected.
-- Dumping structure for table helpdesk.ticket
CREATE TABLE IF NOT EXISTS `ticket` (
  `TicketsId` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(50) DEFAULT NULL,
  `PhoneNumber` varchar(15) DEFAULT NULL,
  `EmailAddress` varchar(50) DEFAULT NULL,
  `AssetTagNumber` varchar(15) DEFAULT NULL,
  `SubjectTitle` varchar(30) DEFAULT NULL,
  `Description` varchar(255) DEFAULT NULL,
  `ComputerName` varchar(20) NOT NULL,
  `StatusDateChanged` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `LocationId` int(11) NOT NULL,
  `PriorityId` int(11) NOT NULL,
  `CategoryId` int(11) NOT NULL,
  `StatusId` int(11) NOT NULL,
  `SiteAdminId` int(11) NOT NULL,
  PRIMARY KEY (`TicketsId`),
  KEY `SiteAdminId` (`SiteAdminId`),
  KEY `category_ticket_fk` (`CategoryId`),
  KEY `location_ticket_fk` (`LocationId`),
  KEY `priority_ticket_fk` (`PriorityId`),
  KEY `status_ticket_fk` (`StatusId`),
  CONSTRAINT `category_ticket_fk` FOREIGN KEY (`CategoryId`) REFERENCES `category` (`CategoryId`) ON DELETE NO ACTION,
  CONSTRAINT `location_ticket_fk` FOREIGN KEY (`LocationId`) REFERENCES `location` (`LocationId`) ON DELETE NO ACTION,
  CONSTRAINT `priority_ticket_fk` FOREIGN KEY (`PriorityId`) REFERENCES `priority` (`PriorityId`) ON DELETE NO ACTION,
  CONSTRAINT `siteAdmin_ticket_fk` FOREIGN KEY (`SiteAdminId`) REFERENCES `siteadmin` (`SiteAdminId`) ON DELETE NO ACTION,
  CONSTRAINT `status_ticket_fk` FOREIGN KEY (`StatusId`) REFERENCES `ticketstatus` (`StatusId`) ON DELETE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=128 DEFAULT CHARSET=utf8;

-- Data exporting was unselected.
-- Dumping structure for table helpdesk.ticketstatus
CREATE TABLE IF NOT EXISTS `ticketstatus` (
  `StatusId` int(11) NOT NULL AUTO_INCREMENT,
  `StatusName` varchar(50) NOT NULL,
  PRIMARY KEY (`StatusId`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- Data exporting was unselected.
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;