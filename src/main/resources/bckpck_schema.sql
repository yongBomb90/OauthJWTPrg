CREATE TABLE IF NOT EXISTS  `MEMBER` (
                          `SEQ` int(11) NOT NULL AUTO_INCREMENT COMMENT '유저 번호',
                          `MEM_ID` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '유저 아이디',
                          `NAME` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '유저 이름',
                          `NICK_NAME` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '유저 별명',
                          `PWD` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '유저 비밀번호',
                          `PHN_NUM` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '유저 핸드폰번호',
                          `EMAIL` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '유저 이메일',
                          `SEX` enum('F','M','X') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'X' COMMENT '유저 성별 ( x : 미입력 )',
                          `REG_DATE` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '가입일자',
                          `UPT_DATE` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '정보 수정일자',
                          PRIMARY KEY (`SEQ`),
                          UNIQUE KEY `UNI_MEMBER_01` (`MEM_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='유저 정보 테이블'
;

CREATE TABLE IF NOT EXISTS `ORD` (
                       `ORD_NUM` varchar(12) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '주문번호',
                       `PRD_NAME` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '제품명',
                       `MEM_SEQ` int(11) NOT NULL COMMENT '유저 번호',
                       `PAY_DATE` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '결제일시',
                       `ORD_DATE` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '주문일자',
                       `REG_DATE` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일자',
                       `UPT_DATE` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일자',
                       PRIMARY KEY (`ORD_NUM`),
                       KEY `IDX_ORDER_01` (`MEM_SEQ`),
                       KEY `IDX_ORDER_02` (`ORD_DATE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='주문 정보 테이블'
;