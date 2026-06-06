-- Seats: 4 floors x 4 seats
INSERT INTO SEATING_CHART (FLOOR_NO, SEAT_NO) VALUES
(1,'座位1'),(1,'座位2'),(1,'座位3'),(1,'座位4'),
(2,'座位1'),(2,'座位2'),(2,'座位3'),(2,'座位4'),
(3,'座位1'),(3,'座位2'),(3,'座位3'),(3,'座位4'),
(4,'座位1'),(4,'座位2'),(4,'座位3'),(4,'座位4');

-- Employees 
INSERT INTO EMPLOYEE (EMP_ID, NAME, EMAIL, FLOOR_SEAT_SEQ) VALUES
('10001', '王小明', 'ming.wang@example.com', NULL),
('10002', '李雅婷', 'yating.li@example.com', NULL),
('10003', '陳志豪', 'zhihao.chen@example.com', NULL),
('10004', '林佳蓉', 'jiarong.lin@example.com', NULL),
('10005', '張家豪', 'jiahao.zhang@example.com', NULL),
('10006', '黃詩涵', 'shihan.huang@example.com', NULL);
