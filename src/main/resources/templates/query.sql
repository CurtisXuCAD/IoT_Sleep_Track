insert into voter values('chunzhix@uci.edu', '1234');
insert into candidate (candidate_name, candidate_department) values('Chunzhi Xu', 'CS');

CREATE DATABASE IF NOT EXISTS stcr_db;
USE stcr_db;

CREATE TABLE IF NOT EXISTS sleeps(
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    user_id INTEGER NOT NULL,
    sleep_time DATETIME,
    wake_time DATETIME
);

CREATE TABLE IF NOT EXISTS lights(
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    light_id INTEGER NOT NULL,
    light_on DATETIME,
    light_off DATETIME
);

CREATE TABLE IF NOT EXISTS light_equip_state(
    light_id INTEGER PRIMARY KEY,
    light_is_on BOOLEAN DEFAULT false
);

INSERT INTO sleeps(user_id, sleep_time, wake_time) VALUES(1, NOW(), null);

SELECT wake_time FROM sleeps ORDER BY id DESC LIMIT 1;

CREATE TABLE IF NOT EXISTS sleepiness_datas(
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    user_id INTEGER NOT NULL,
    track_time DATETIME,
    sleepiness integer NOT NULL
);

CREATE TABLE IF NOT EXISTS sleeptime_distrubution(
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    user_id INTEGER NOT NULL,
    track_time DATETIME,
    sleepiness integer NOT NULL
);

CREATE INDEX wake_time_index ON sleeps(wake_time);

SELECT HOUR(wake_time) as wake_hour, COUNT(*) as num FROM sleeps GROUP BY wake_hour where wake_time is not null;

SELECT SUM(TIMESTAMPDIFF(HOUR, sleep_time, wake_time)) as diff_sum, DATE(sleep_time) FROM sleeps where wake_time is not null GROUP BY DATE(sleep_time) ORDER BY DATE(sleep_time) DESC LIMIT 7;