ALTER DATABASE openmarket CHARACTER SET = 'utf8mb4' COLLATE = 'utf8mb4_unicode_ci';

SET GLOBAL time_zone = 'Asia/Seoul';
SET time_zone = 'Asia/Seoul';

GRANT ALL PRIVILEGES ON openmarket.* TO 'openmarket'@'%';

FLUSH PRIVILEGES;