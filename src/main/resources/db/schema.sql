
-- Drop tables in correct dependency order
DROP TABLE IF EXISTS `review`;
DROP TABLE IF EXISTS `payment`;
DROP TABLE IF EXISTS `notification`;
DROP TABLE IF EXISTS `booking`;
DROP TABLE IF EXISTS `item`;
DROP TABLE IF EXISTS `user`;

-- Users Table
CREATE TABLE `user` (
  `id` VARCHAR(36) NOT NULL,
  `first_name` VARCHAR(50),
  `last_name` VARCHAR(50),
  `email` VARCHAR(100) UNIQUE,
  `password` VARCHAR(255),
  `is_owner` TINYINT(1) DEFAULT 0,
  `is_borrower` TINYINT(1) DEFAULT 0,
  `profile_photo_url` TEXT,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Items Table
CREATE TABLE `item` (
  `id` VARCHAR(36) NOT NULL,
  `owner_id` VARCHAR(36),
  `title` VARCHAR(100),
  `description` TEXT,
  `category` VARCHAR(50),
  `price_per_day` DECIMAL(10,2),
  `location` VARCHAR(100),
  `is_active` TINYINT(1) DEFAULT 1,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`owner_id`) REFERENCES `user`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Bookings Table
CREATE TABLE `booking` (
  `id` VARCHAR(36) NOT NULL,
  `item_id` VARCHAR(36),
  `borrower_id` VARCHAR(36),
  `start_date` DATE,
  `end_date` DATE,
  `status` ENUM('pending','confirmed','cancelled','completed') DEFAULT 'pending',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`item_id`) REFERENCES `item`(`id`),
  FOREIGN KEY (`borrower_id`) REFERENCES `user`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Notifications Table
CREATE TABLE `notification` (
  `id` VARCHAR(36) NOT NULL,
  `user_id` VARCHAR(36),
  `message` TEXT,
  `type` ENUM('booking','review','reminder'),
  `read_status` TINYINT(1) DEFAULT 0,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Payments Table
CREATE TABLE `payment` (
  `id` VARCHAR(36) NOT NULL,
  `booking_id` VARCHAR(36),
  `amount` DECIMAL(10,2),
  `method` ENUM('card','paypal','bank_transfer'),
  `status` ENUM('held','released','refunded'),
  `payment_date` TIMESTAMP NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`booking_id`) REFERENCES `booking`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Reviews Table
CREATE TABLE `review` (
  `id` VARCHAR(36) NOT NULL,
  `booking_id` VARCHAR(36),
  `reviewer_id` VARCHAR(36),
  `rating` INT CHECK (`rating` BETWEEN 1 AND 5),
  `comment` TEXT,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`booking_id`) REFERENCES `booking`(`id`),
  FOREIGN KEY (`reviewer_id`) REFERENCES `user`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
