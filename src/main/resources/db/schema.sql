
-- Drop tables in correct dependency order
DROP TABLE IF EXISTS `review`;
DROP TABLE IF EXISTS `payment`;
DROP TABLE IF EXISTS `notification`;
DROP TABLE IF EXISTS `booking`;
DROP TABLE IF EXISTS `item`;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `message`;
DROP TABLE IF EXISTS `conversation`;

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

-- Conversation Table
CREATE TABLE `conversation` (
                                `id` VARCHAR(36) NOT NULL,
                                `user1_id` VARCHAR(36) NOT NULL,
                                `user2_id` VARCHAR(36) NOT NULL,
                                `booking_id` VARCHAR(36),
                                `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                PRIMARY KEY (`id`),
                                FOREIGN KEY (`user1_id`) REFERENCES `user`(`id`),
                                FOREIGN KEY (`user2_id`) REFERENCES `user`(`id`),
                                FOREIGN KEY (`booking_id`) REFERENCES `booking`(`id`),
                                UNIQUE KEY `unique_conversation` (`user1_id`, `user2_id`, `booking_id`),
                                CONSTRAINT `check_user_order` CHECK (`user1_id` < `user2_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Messages Table
CREATE TABLE `message` (
                           `id` VARCHAR(36) NOT NULL,
                           `conversation_id` VARCHAR(36) NOT NULL,
                           `sender_id` VARCHAR(36) NOT NULL,
                           `content` TEXT NOT NULL,
                           `read_status` TINYINT(1) DEFAULT 0,
                           `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           PRIMARY KEY (`id`),
                           FOREIGN KEY (`conversation_id`) REFERENCES `conversation`(`id`),
                           FOREIGN KEY (`sender_id`) REFERENCES `user`(`id`),
                           INDEX `idx_conversation` (`conversation_id`),
                           INDEX `idx_sender` (`sender_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Adding message count to users
ALTER TABLE `user`
    ADD COLUMN `unread_message_count` INT DEFAULT 0;
