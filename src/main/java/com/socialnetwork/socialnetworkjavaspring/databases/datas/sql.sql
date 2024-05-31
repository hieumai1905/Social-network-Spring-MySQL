-- data for users table
DELIMITER $$

DROP TEMPORARY TABLE IF EXISTS temp_users;

CREATE TEMPORARY TABLE IF NOT EXISTS temp_users
(
    user_id     VARCHAR(36) PRIMARY KEY,
    about_me    VARCHAR(1000),
    avatar      VARCHAR(255),
    country     VARCHAR(255),
    cover_photo VARCHAR(255),
    dob         DATE,
    email       VARCHAR(100),
    full_name   VARCHAR(100),
    gender      VARCHAR(255),
    password    VARCHAR(100),
    register_at DATETIME(6),
    status      VARCHAR(20),
    update_at   DATETIME(6),
    user_role   VARCHAR(255)
) $$

SET @avatars =
        '/assets/files-upload/user-1.png,/assets/files-upload/user-2.png,/assets/files-upload/user-3.png,/assets/files-upload/user-4.png,/assets/files-upload/user-5.png,/assets/files-upload/user-6.png,/assets/files-upload/user-7.png,/assets/files-upload/user-8.png,/assets/files-upload/user-9.png,/assets/files-upload/user-10.png,/assets/files-upload/user-11.png,/assets/files-upload/user-12.png,/assets/files-upload/user-13.png,/assets/files-upload/user-14.png,/assets/files-upload/user-15.png,/assets/files-upload/user-16.png,/assets/files-upload/user-17.png,/assets/files-upload/user-18.png,/assets/files-upload/user-19.png' $$
SET @covers =
        '/assets/files-upload/p-1.jpg,/assets/files-upload/p-2.jpg,/assets/files-upload/p-3.jpg,/assets/files-upload/p-4.jpg,/assets/files-upload/p-5.jpg,/assets/files-upload/p-6.jpg' $$

DROP PROCEDURE IF EXISTS insertUsers;

CREATE PROCEDURE insertUsers()
BEGIN
    DECLARE i INT DEFAULT 1;
    WHILE i <= 250
        DO
            INSERT INTO temp_users (user_id, about_me, avatar, country, cover_photo, dob, email, full_name, gender,
                                    password, register_at, status, update_at, user_role)
            VALUES (UUID(),
                    CONCAT('Thông tin của người dùng ', i),
                    SUBSTRING_INDEX(SUBSTRING_INDEX(@avatars, ',', FLOOR(RAND() * 25) + 1), ',', -1),
                    ELT(FLOOR(RAND() * 3) + 1, 'Hà Nội', 'Vĩnh Phúc', 'Bắc Ninh', 'Quảng Ninh', 'Hải Dương',
                        'Hải Phòng', 'Hưng Yên', 'Thái Bình', 'Hà Nam', 'Nam Định', 'Ninh Bình',
                        'Trung du và miền núi phía Bắc', 'Hà Giang', 'Cao Bằng', 'Bắc Kạn', 'Tuyên Quang', 'Lào Cai',
                        'Yên Bái', 'Thái Nguyên', 'Lạng Sơn', 'Bắc Giang', 'Phú Thọ', 'Điện Biên', 'Lai Châu', 'Sơn La',
                        'Hoà Bình', 'Bắc Trung Bộ và Duyên hải miền Trung', 'Thanh Hoá', 'Nghệ An', 'Hà Tĩnh',
                        'Quảng Bình', 'Quảng Trị', 'Thừa Thiên Huế', 'Đà Nẵng', 'Quảng Nam', 'Quảng Ngãi', 'Bình Định',
                        'Phú Yên', 'Khánh Hoà', 'Ninh Thuận', 'Bình Thuận', 'Tây Nguyên', 'Kon Tum', 'Gia Lai',
                        'Đắk Lắk', 'Đắk Nông', 'Lâm Đồng', 'Đông Nam Bộ', 'Bình Phước', 'Tây Ninh', 'Bình Dương',
                        'Đồng Nai', 'Bà Rịa - Vũng Tàu', 'TP.Hồ Chí Minh', 'Đồng bằng sông Cửu Long', 'Long An',
                        'Tiền Giang', 'Bến Tre', 'Trà Vinh', 'Vĩnh Long', 'Đồng Tháp', 'An Giang', 'Kiên Giang',
                        'Cần Thơ', 'Hậu Giang', 'Sóc Trăng', 'Bạc Liêu', 'Cà Mau'),
                    SUBSTRING_INDEX(SUBSTRING_INDEX(@covers, ',', FLOOR(RAND() * 6) + 1), ',', -1),
                    '2024-01-15',
                    CONCAT('user', i, '@example.com'),
                    CONCAT('User ', i),
                    ELT(FLOOR(RAND() * 3) + 1, 'MALE', 'FEMALE', 'OTHER'),
                    CONCAT('password', i),
                    '2024-01-15 05:03:29.000000',
                    ELT(FLOOR(RAND() * 4) + 1, 'ACTIVE', 'INACTIVE', 'LOCKED', 'DELETED'),
                    '2024-01-15 05:03:33.000000',
                    ELT(FLOOR(RAND() * 2) + 1, 'ROLE_USER', 'ROLE_ADMIN'));
            SET i = i + 1;
        END WHILE;
END $$

DELIMITER ;

CALL insertUsers();

INSERT INTO users (user_id, about_me, avatar, country, cover_photo, dob, email, full_name, gender, password,
                   register_at, status, update_at, user_role)
SELECT user_id,
       about_me,
       avatar,
       country,
       cover_photo,
       dob,
       email,
       full_name,
       gender,
       password,
       register_at,
       status,
       update_at,
       user_role
FROM temp_users;

DROP TEMPORARY TABLE temp_users;


-- data for hash_tags table
DELIMITER $$

DROP PROCEDURE IF EXISTS insertHashTags$$

CREATE PROCEDURE insertHashTags()
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE hashtag_list VARCHAR(2957) DEFAULT '#travel,#food,#photography,#fitness,#love,#family,#nature,#style,#music,#motivation,#entrepreneurship,#technology,#beauty,#inspiration,#health,#sport,#art,#business,#fashion,#happiness,#lifestyle,#books,#adventure,#summer,#cute,#smile,#life,#baby,#fun,#workout,#holiday,#design,#coffee,#makeup,#instagood,#picoftheday,#beach,#party,#wedding,#home,#dog,#cat,#explore,#dream,#goals,#success,#mindfulness,#positivevibes,#yoga,#meditation,#healthy,#quote,#meme,#diy,#funny,#mood,#aesthetic,#hobby,#learn,#create,#outdoors,#hiking,#camping,#ski,#snowboard,#surfing,#diving,#cycling,#running,#fit,#gym,#bodybuilding,#nutrition,#cook,#recipe,#baking,#dessert,#wine,#cocktail,#beer,#foodie,#travelers,#wanderlust,#instatravel,#travelgram,#mytravelgram,#travelstoke,#liveauthentic,#lifeofadventure,#roamtheplanet,#ig_worldclub,#wonderful_places,#worldplaces,#traveldeeper,#travelstyle,#travelphoto,#traveldiaries,#travelista,#travelblogger,#travelpics,#traveldiary,#travelawesome,#traveladdict,#traveltheworld,#travelgirl,#travelcouple,#travellife,#travelbug,#travelingram,#travelphoto,#travelling,#travellingthroughtheworld,#travelwithme,#travelmore,#travelgram,#socialmedia,#content,#viral,#influencer,#marketing,#branding,#seo,#digital,#instagram,#twitter,#facebook,#linkedin,#pinterest,#youtube,#tiktok,#snapchat,#trends,#socialmediatips,#socialmediamarketing,#socialmediamanager,#socialmedialife,#socialmediaexpert,#socialmediainfluencer,#socialmediamatters,#socialmediastrategist,#sustainable,#ecofriendly,#green,#zerowaste,#environment,#savetheplanet,#renewableenergy,#climatechange,#sustainability,#gogreen,#ecolife,#ecofriendlytips,#nature,#organic,#recycle,#plasticfree,#cleanenergy,#environment,#mental health,#selfcare,#mentalwellness,#mindset,#healing,#wellness,#meditation,#therapy,#selflove,#spirituality,#happiness,#joy,#peace,#balance,#gratitude,#purpose,#growth,#selfdevelopment,#inspire,#motivation,#resilience,#hope,#photography,#art,#design,#portrait,#landscape,#nature,#architecture,#street,#blackandwhite,#color,#abstract,#creative,#capture,#moment,#lifestyle,#minimal,#beauty,#fashion,#makeup,#skincare,#travel,#adventure,#wanderlust,#explore,#landscape,#beach,#mountain,#city,#urban,#culture,#explore,#tourist,#vacation,#trip,#relax,#leisure,#lifestyle,#family,#friend,#couple,#baby,#kid,#child,#love,#romance,#wedding,#event,#party,#celebration,#happy,#fun,#smile,#joy,#mood,#style,#home,#interior,#decor,#diy,#craft,#handmade,#hobby,#learn,#create,#garden,#outdoor,#nature,#plant,#flower,#cooking,#baking,#recipe,#foodie,#healthy,#fitness,#workout,#gym,#sport,#wellness,#mindful,#meditation,#yoga,#motivation,#inspiration,#success,#business,#marketing,#branding,#socialmedia,#digital,#entrepreneur,#startup,#technology,#innovation,#future,#science,#environment,#sustainability,#ecofriendly,#savetheplanet,#recycle,#green,#organic,#mental health,#selfcare,#therapy,#spirituality,#happiness,#peace,#balance,#growth,#inspire,#resilience,#hope';
    DECLARE hashtag VARCHAR(255);

    WHILE i <= 500
        DO
            SET hashtag = TRIM(SUBSTR(hashtag_list, 1, INSTR(hashtag_list, ',') - 1));
            SET hashtag_list = SUBSTR(hashtag_list, INSTR(hashtag_list, ',') + 1);
            IF LENGTH(hashtag) > 0 THEN
                INSERT INTO hash_tags (hashtag)
                VALUES (hashtag)
                ON DUPLICATE KEY UPDATE hashtag = hashtag;
            END IF;

            SET i = i + 1;
        END WHILE;
END$$

DELIMITER ;

CALL insertHashTags();


-- data for posts table
DELIMITER $$

DROP PROCEDURE IF EXISTS insertPosts$$

CREATE PROCEDURE insertPosts()
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE j INT DEFAULT 0;
    DECLARE post_id_var VARCHAR(36);
    DECLARE access_var VARCHAR(255);
    DECLARE create_at_var DATETIME(6);
    DECLARE content_var VARCHAR(2000);
    DECLARE post_type_var VARCHAR(255);
    DECLARE user_id_var VARCHAR(36);
    DECLARE hashtag_count INT DEFAULT 0;
    DECLARE hashtag_var VARCHAR(255);
    DECLARE hashtag_id_var BIGINT;

    WHILE i <= 1000
        DO
            -- Tạo 4 bài viết
            SELECT user_id
            INTO user_id_var
            FROM users
            ORDER BY RAND()
            LIMIT 1;

            SET access_var = ELT(FLOOR(RAND() * 3) + 1, 'PRIVATE', 'PUBLIC', 'FRIEND');
            SET create_at_var = DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY);
            SET post_type_var = ELT(FLOOR(RAND() * 3) + 1, 'POST', 'CHANGE_AVATAR', 'CHANGE_COVER');
            SET content_var = CONCAT('Nội dung bài viết ', i);
            SET post_id_var = UUID();

            SET hashtag_count = FLOOR(RAND() * 5); -- Số lượng hashtag ngẫu nhiên từ 0 đến 10

            SET j = hashtag_count;

            WHILE j > 0
                DO
                    SELECT hashtag, hashtag_id
                    INTO hashtag_var, hashtag_id_var
                    FROM hash_tags
                    ORDER BY RAND()
                    LIMIT 1;

                    SET content_var = CONCAT(content_var, ' ', hashtag_var);

                    SET j = j - 1;
                END WHILE;

            INSERT INTO posts (post_id, access, create_at, content, post_type, user_id)
            VALUES (post_id_var, access_var, create_at_var, content_var, post_type_var, user_id_var);

            -- Thêm các bản ghi vào bảng post_hashtags
            SET j = hashtag_count;
            WHILE j > 0
                DO
                    SELECT hashtag, hashtag_id
                    INTO hashtag_var, hashtag_id_var
                    FROM hash_tags
                    ORDER BY RAND()
                    LIMIT 1;

                    INSERT IGNORE INTO post_hashtags (post_id, hashtag_id)
                    VALUES (post_id_var, hashtag_id_var);

                    SET j = j - 1;
                END WHILE;

            SET i = i + 1;
        END WHILE;
END$$

DELIMITER ;

CALL insertPosts();

-- data for relations table
DELIMITER ;
DROP TEMPORARY TABLE IF EXISTS temp_posts;

CREATE TEMPORARY TABLE temp_relations
(
    relation_id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    set_at         DATETIME(6),
    type           VARCHAR(20),
    user_id        VARCHAR(36),
    user_target_id VARCHAR(36)
);

DROP PROCEDURE IF EXISTS insertRelations;

DELIMITER $$
CREATE PROCEDURE insertRelations()
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE user1_id VARCHAR(36);
    DECLARE user2_id VARCHAR(36);
    WHILE i <= 10000
        DO
            -- Chọn ngẫu nhiên 2 user khác nhau
            SELECT user_id INTO user1_id FROM users ORDER BY RAND() LIMIT 1;
            SELECT user_id INTO user2_id FROM users WHERE user_id != user1_id ORDER BY RAND() LIMIT 1;

            -- Thêm bản ghi ngẫu nhiên theo yêu cầu
            CASE FLOOR(RAND() * 4)
                WHEN 0 THEN -- Bạn bè (3 or 4 bản ghi)
                INSERT INTO temp_relations (set_at, type, user_id, user_target_id)
                VALUES (DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY), 'FRIEND', user1_id, user2_id),
                       (DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY), 'FRIEND', user2_id, user1_id);
                IF RAND() > 0.5 THEN
                    INSERT INTO temp_relations (set_at, type, user_id, user_target_id)
                    VALUES (DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY), 'FOLLOW', user1_id, user2_id);
                END IF;
                IF RAND() > 0.5 THEN
                    INSERT INTO temp_relations (set_at, type, user_id, user_target_id)
                    VALUES (DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY), 'FOLLOW', user2_id, user1_id);
                END IF;
                WHEN 1 THEN -- Theo dõi (1 bản ghi)
                INSERT INTO temp_relations (set_at, type, user_id, user_target_id)
                VALUES (DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY), 'FOLLOW', user1_id, user2_id);
                WHEN 2 THEN -- Chặn (1 bản ghi)
                INSERT INTO temp_relations (set_at, type, user_id, user_target_id)
                VALUES (DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY), 'BLOCK', user1_id, user2_id);
                ELSE -- Yêu cầu kết bạn (1 hoặc 2 bản ghi)
                INSERT INTO temp_relations (set_at, type, user_id, user_target_id)
                VALUES (DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY), 'REQUEST', user1_id, user2_id);
                IF RAND() > 0.5 THEN
                    INSERT INTO temp_relations (set_at, type, user_id, user_target_id)
                    VALUES (DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY), 'FOLLOW', user1_id, user2_id);
                END IF;
                END CASE;
            SET i = i + 1;
        END WHILE;
END$$
DELIMITER ;

CALL insertRelations();

INSERT INTO relations (set_at, type, user_id, user_target_id)
SELECT set_at, type, user_id, user_target_id
FROM temp_relations;

DROP TEMPORARY TABLE temp_relations;

-- data for conversations table
DELIMITER ;
DROP TEMPORARY TABLE IF EXISTS temp_conversations;

CREATE TEMPORARY TABLE temp_conversations
(
    conversation_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    avatar          VARCHAR(100),
    created_at      DATETIME(6),
    name            VARCHAR(100),
    type            VARCHAR(20),
    manage_id       VARCHAR(36)
);

SET @avatars =
        '/assets/files-upload/s-1.jpg,/assets/files-upload/s-2.jpg,/assets/files-upload/s-3.jpg,/assets/files-upload/s-4.jpg,/assets/files-upload/s-5.jpg,/assets/files-upload/s-6.jpg,/assets/files-upload/s-7.jpg,/assets/files-upload/s-8.jpg,/assets/files-upload/s-9.jpg,/assets/files-upload/s-10.jpg,/assets/files-upload/s-11.jpg,/assets/files-upload/s-12.jpg';

DROP PROCEDURE IF EXISTS insertConversations;

DELIMITER $$
CREATE PROCEDURE insertConversations()
BEGIN
    DECLARE i INT DEFAULT 1;
    WHILE i <= 1000
        DO
            INSERT INTO temp_conversations (avatar, created_at, name, type, manage_id)
            VALUES (SUBSTRING_INDEX(SUBSTRING_INDEX(@avatars, ',', FLOOR(RAND() * 12) + 1), ',', -1),
                    DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY),
                    CONCAT('Cuộc trò chuyện ', i),
                    ELT(FLOOR(RAND() * 2) + 1, 'PERSONAL', 'GROUP'),
                    (SELECT user_id FROM users ORDER BY RAND() LIMIT 1));
            SET i = i + 1;
        END WHILE;
END$$
DELIMITER ;

CALL insertConversations();

INSERT INTO conversations (avatar, created_at, name, type, manage_id)
SELECT avatar, created_at, name, type, manage_id
FROM temp_conversations;

DROP TEMPORARY TABLE temp_conversations;

-- data for participants table
DELIMITER $$

DROP PROCEDURE IF EXISTS insertParticipants$$

CREATE PROCEDURE insertParticipants()
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE conversation_count INT DEFAULT 0;
    DECLARE users_count INT DEFAULT 0;
    DECLARE conv_id BIGINT;
    DECLARE user_id_var VARCHAR(36);
    DECLARE other_user_id_var VARCHAR(36);
    DECLARE manager_id_var VARCHAR(36);
    DECLARE conv_type VARCHAR(20);
    DECLARE delete_at_var DATETIME(6);
    DECLARE joined_at_var DATETIME(6);
    DECLARE nickname_var VARCHAR(50);
    DECLARE status_var VARCHAR(50);

    SELECT COUNT(*) INTO conversation_count FROM conversations;
    SELECT COUNT(*) INTO users_count FROM users;

    DROP TEMPORARY TABLE IF EXISTS temp_participants;

    CREATE TEMPORARY TABLE temp_participants
    (
        conversation_id BIGINT,
        user_id         VARCHAR(36),
        delete_at       DATETIME(6),
        joined_at       DATETIME(6),
        nickname        VARCHAR(50),
        status          VARCHAR(50)
    );

    WHILE i <= 500
        DO
            -- Chọn ngẫu nhiên một conversation chưa được sử dụng
            SELECT id, type
            INTO conv_id, conv_type
            FROM (SELECT conversation_id AS id, type
                  FROM conversations
                  WHERE conversation_id NOT IN (SELECT DISTINCT conversation_id FROM temp_participants)
                  ORDER BY RAND()
                  LIMIT 1) AS temp_conversation;

            -- Chọn ngẫu nhiên một user
            SELECT user_id
            INTO user_id_var
            FROM users
            ORDER BY RAND()
            LIMIT 1;

            -- Tạo bản ghi với thông tin ngẫu nhiên
            SET delete_at_var = IF(RAND() > 0.5, DATE_ADD(NOW(), INTERVAL FLOOR(RAND() * 30) DAY), NULL);
            SET joined_at_var = DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY);
            SET nickname_var = IF(RAND() > 0.5, CONCAT('Nickname ', i),
                                  (SELECT full_name FROM users WHERE user_id = user_id_var LIMIT 1));
            SET status_var = IF(RAND() > 0.5, 'JOINED', 'LEAVED');

            INSERT INTO temp_participants (conversation_id, user_id, delete_at, joined_at, nickname, status)
            VALUES (conv_id, user_id_var, delete_at_var, joined_at_var, nickname_var, status_var);

            IF conv_type = 'PERSONAL' THEN
                SELECT user_id
                INTO other_user_id_var
                FROM temp_participants
                WHERE conversation_id = conv_id
                LIMIT 1;

                SET delete_at_var = NULL;
                SET joined_at_var = (SELECT created_at FROM conversations WHERE conversation_id = conv_id LIMIT 1);
                SET nickname_var = (SELECT full_name FROM users WHERE user_id = other_user_id_var LIMIT 1);
                SET status_var = 'JOINED';

                INSERT INTO temp_participants (conversation_id, user_id, delete_at, joined_at, nickname, status)
                VALUES (conv_id, other_user_id_var, delete_at_var, joined_at_var, nickname_var, status_var);
            END IF;

            IF conv_type = 'GROUP' THEN
                SELECT manage_id
                INTO manager_id_var
                FROM conversations
                WHERE conversation_id = conv_id;

                SET delete_at_var = NULL;
                SET joined_at_var = (SELECT created_at FROM conversations WHERE conversation_id = conv_id LIMIT 1);
                SET nickname_var = (SELECT full_name FROM users WHERE user_id = manager_id_var LIMIT 1);
                SET status_var = 'JOINED';

                INSERT INTO temp_participants (conversation_id, user_id, delete_at, joined_at, nickname, status)
                VALUES (conv_id, manager_id_var, delete_at_var, joined_at_var, nickname_var, status_var);
            END IF;

            SET i = i + 1;
        END WHILE;

    -- Thêm dữ liệu từ bảng tạm vào bảng participants
    INSERT INTO participants (conversation_id, user_id, delete_at, joined_at, nickname, status)
    SELECT conversation_id, user_id, delete_at, joined_at, nickname, status
    FROM temp_participants
    ON DUPLICATE KEY UPDATE delete_at = VALUES(delete_at),
                            joined_at = VALUES(joined_at),
                            nickname  = VALUES(nickname),
                            status    = VALUES(status);

    -- Xóa bảng tạm
    DROP TEMPORARY TABLE temp_participants;
END$$

DELIMITER ;

CALL insertParticipants();


-- data for comment table
DELIMITER $$

DROP PROCEDURE IF EXISTS insertComments$$

CREATE PROCEDURE insertComments()
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE comment_text VARCHAR(500) DEFAULT ' This is a sample comment. How are you doing today? I hope you''re having a great day! Let me know if there''s anything I can help with.';
    DECLARE comment VARCHAR(500);
    DECLARE post_id_var VARCHAR(36);
    DECLARE user_id_var VARCHAR(36);
    DECLARE comment_at DATETIME(6);
    DECLARE update_at DATETIME(6);

    WHILE i <= 2000
        DO
            SET comment = CONCAT_WS(' ', CONCAT('Comment ', i, ':'), TRIM(comment_text), '#random');

            SELECT post_id, user_id
            INTO post_id_var, user_id_var
            FROM (SELECT post_id, user_id
                  FROM posts
                  ORDER BY RAND()
                  LIMIT 1) AS t;

            SET comment_at = DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY);
            SET update_at = IF(RAND() < 0.5, DATE_SUB(comment_at, INTERVAL FLOOR(RAND() * 7) DAY), NULL);

            INSERT INTO comments (comment_at, content, update_at, post_id, user_id)
            VALUES (comment_at, comment, update_at, post_id_var, user_id_var);

            SET i = i + 1;
        END WHILE;
END$$

DELIMITER ;

CALL insertComments();

-- data for comment reply table
DELIMITER $$

DROP PROCEDURE IF EXISTS insertCommentReplies$$

CREATE PROCEDURE insertCommentReplies()
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE reply_text VARCHAR(500) DEFAULT ' Great comment! I agree with what you said. Let me know if you have any other thoughts.';
    DECLARE reply VARCHAR(500);
    DECLARE comment_id_var BIGINT;
    DECLARE user_id_var VARCHAR(36);
    DECLARE reply_at DATETIME(6);

    WHILE i <= 500
        DO
            SET reply = CONCAT_WS(' ', CONCAT('Reply ', i, ':'), TRIM(reply_text));

            SELECT comment_id, user_id
            INTO comment_id_var, user_id_var
            FROM (SELECT comment_id, user_id
                  FROM comments
                  ORDER BY RAND()
                  LIMIT 1) AS t;

            SET reply_at = DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 14) DAY);

            INSERT INTO comment_replies (content, reply_at, comment_id, user_id)
            VALUES (reply, reply_at, comment_id_var, user_id_var);

            SET i = i + 1;
        END WHILE;
END$$

DELIMITER ;

CALL insertCommentReplies();

-- data for likes table
DELIMITER $$

DROP PROCEDURE IF EXISTS insertLikes$$

CREATE PROCEDURE insertLikes()
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE like_type INT;
    DECLARE comment_id_var BIGINT;
    DECLARE comment_reply_id_var BIGINT;
    DECLARE post_id_var VARCHAR(36);
    DECLARE user_id_var VARCHAR(36);

    WHILE i <= 500
        DO
            SET like_type = FLOOR(RAND() * 3);

            IF like_type = 0 THEN
                SELECT comment_id, user_id
                INTO comment_id_var, user_id_var
                FROM (SELECT comment_id, user_id
                      FROM comments
                      ORDER BY RAND()
                      LIMIT 1) AS t;

                SET comment_reply_id_var = NULL;
                SET post_id_var = NULL;
            ELSEIF like_type = 1 THEN
                SELECT comment_reply_id, user_id
                INTO comment_reply_id_var, user_id_var
                FROM (SELECT comment_reply_id, user_id
                      FROM comment_replies
                      ORDER BY RAND()
                      LIMIT 1) AS t;

                SET comment_id_var = NULL;
                SET post_id_var = NULL;
            ELSE
                SELECT post_id, user_id
                INTO post_id_var, user_id_var
                FROM (SELECT post_id, user_id
                      FROM posts
                      ORDER BY RAND()
                      LIMIT 1) AS t;

                SET comment_id_var = NULL;
                SET comment_reply_id_var = NULL;
            END IF;

            INSERT IGNORE INTO likes (comment_id, comment_reply_id, post_id, user_id)
            VALUES (comment_id_var, comment_reply_id_var, post_id_var, user_id_var);

            SET i = i + 1;
        END WHILE;
END$$

DELIMITER ;

CALL insertLikes();

-- data for post_interacts table
DELIMITER $$

DROP PROCEDURE IF EXISTS insertPostInteracts$$

CREATE PROCEDURE insertPostInteracts()
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE post_id_var VARCHAR(36);
    DECLARE user_id_var VARCHAR(36);
    DECLARE type_var VARCHAR(255);
    DECLARE content_var VARCHAR(255);

    WHILE i <= 500
        DO
            SELECT post_id, user_id
            INTO post_id_var, user_id_var
            FROM (SELECT post_id, user_id
                  FROM posts
                  ORDER BY RAND()
                  LIMIT 1) AS t;

            SET type_var = CASE FLOOR(RAND() * 4)
                               WHEN 0 THEN 'SHARED'
                               WHEN 1 THEN 'SAVED'
                               WHEN 2 THEN 'REPORT'
                               ELSE 'HIDDEN'
                END;

            IF type_var = 'SHARED' THEN
                SET content_var = CONCAT('Shared content for post ', post_id_var);
                INSERT INTO post_interacts (content, interact_at, type, post_id, user_id)
                VALUES (content_var, NOW(), type_var, post_id_var, user_id_var);
            ELSE
                INSERT IGNORE INTO post_interacts (content, interact_at, type, post_id, user_id)
                VALUES (NULL, NOW(), type_var, post_id_var, user_id_var);
            END IF;

            SET i = i + 1;
        END WHILE;
END$$

DELIMITER ;

CALL insertPostInteracts();

-- data for user_tags table
DELIMITER $$

DROP PROCEDURE IF EXISTS insertUserTags$$

CREATE PROCEDURE insertUserTags()
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE post_id_var VARCHAR(36);
    DECLARE user_id_var VARCHAR(36);
    DECLARE user_id_tag_var VARCHAR(36);

    WHILE i <= 2000
        DO
            SELECT post_id, user_id
            INTO post_id_var, user_id_var
            FROM (SELECT post_id, user_id
                  FROM posts
                  ORDER BY RAND()
                  LIMIT 1) AS t;

            SELECT user_id
            INTO user_id_tag_var
            FROM (SELECT user_id
                  FROM users
                  WHERE user_id <> user_id_var
                  ORDER BY RAND()
                  LIMIT 1) AS t;

            INSERT IGNORE INTO user_tags (post_id, user_id)
            VALUES (post_id_var, user_id_tag_var);

            SET i = i + 1;
        END WHILE;
END$$

DELIMITER ;

CALL insertUserTags();


-- data for messages table
DELIMITER $$

DROP PROCEDURE IF EXISTS insertMessages$$

CREATE PROCEDURE insertMessages()
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE content_var VARCHAR(2000);
    DECLARE send_at_var DATETIME(6);
    DECLARE conversation_id_var BIGINT;
    DECLARE sender_id_var VARCHAR(36);

    WHILE i <= 10000
        DO
            -- Tạo 10000 tin nhắn
            SELECT conversation_id
            INTO conversation_id_var
            FROM conversations
            ORDER BY RAND()
            LIMIT 1;

            SELECT user_id
            INTO sender_id_var
            FROM users
            WHERE user_id IN (SELECT user_id
                              FROM conversations
                              WHERE conversation_id = conversation_id_var)
            ORDER BY RAND()
            LIMIT 1;

            SET content_var = CONCAT('Nội dung tin nhắn ', FLOOR(RAND() * 100000));
            SET send_at_var = DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY);

            INSERT INTO messages (content, send_at, conversation_id, sender_id)
            VALUES (content_var, send_at_var, conversation_id_var, sender_id_var);

            SET i = i + 1;
        END WHILE;
END$$

DELIMITER ;

CALL insertMessages();

-- data for medias table
DELIMITER $$

DROP PROCEDURE IF EXISTS insertMedias$$

CREATE PROCEDURE insertMedias()
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE post_id_var VARCHAR(36);
    DECLARE media_count INT DEFAULT 0;
    DECLARE type_var VARCHAR(20);
    DECLARE url_var VARCHAR(200);

    WHILE i <= 100 DO -- Tạo 100 bài post
    SELECT post_id
    INTO post_id_var
    FROM posts
    ORDER BY RAND()
    LIMIT 1;

    SET media_count = FLOOR(RAND() * 5) + 1; -- Số lượng media ngẫu nhiên từ 1 đến 5

    WHILE media_count > 0 DO
            SET type_var = ELT(FLOOR(RAND() * 2) + 1, 'IMAGE', 'VIDEO');

            IF type_var = 'IMAGE' THEN
                SET url_var = CONCAT('/assets/files-upload/t-', FLOOR(RAND() * 27) + 10, '.jpg');
            ELSEIF type_var = 'VIDEO' THEN
                SET url_var = CONCAT('/assets/files-upload/v-', FLOOR(RAND() * 3) + 1, '.mp4');
            ELSE
                SET url_var = CONCAT('/assets/files-upload/filename', i, '.', ELT(FLOOR(RAND() * 3) + 1, 'jpg', 'mp4', 'mp3'));
            END IF;

            INSERT INTO medias (type, url, post_id)
            VALUES (type_var, url_var, post_id_var);

            SET media_count = media_count - 1;
        END WHILE;

    SET i = i + 1;
        END WHILE;
END$$

DELIMITER ;

CALL insertMedias();
