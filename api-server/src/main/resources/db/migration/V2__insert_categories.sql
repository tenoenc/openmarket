-- 1. 최상위 카테고리 (Parent ID IS NULL)
INSERT INTO categories (id, name, parent_id) VALUES
(1, '전자제품', NULL),
(2, '의류', NULL),
(3, '식품', NULL),
(4, '도서', NULL);

-- 2. 하위 카테고리 - 전자제품 (Parent ID = 1)
INSERT INTO categories (id, name, parent_id) VALUES
(101, '스마트폰', 1),
(102, '노트북', 1),
(103, '스마트워치', 1),
(104, '태블릿', 1);

-- 3. 하위 카테고리 - 의류 (Parent ID = 2)
INSERT INTO categories (id, name, parent_id) VALUES
(201, '남성상의', 2),
(202, '여성상의', 2),
(203, '하의', 2);

-- 4. 하위 카테고리 - 식품 (Parent ID = 3)
INSERT INTO categories (id, name, parent_id) VALUES
(301, '신선식품', 3),
(302, '가공식품', 3),
(303, '건강기능식품', 3);