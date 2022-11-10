DELETE FROM scraper_error_log;
DELETE FROM scraper_status_log;
DELETE FROM scraper_config;

INSERT INTO scraper_config (scraper_id, configuration_name, is_active, time_zone) VALUES(100000, 'Scraper3', true, 'Europe/Warsaw');
INSERT INTO scraper_config (scraper_id, configuration_name, is_active, time_zone) VALUES(200000, 'Scraper2', true, 'Europe/Warsaw');
INSERT INTO scraper_config (scraper_id, configuration_name, is_active, time_zone) VALUES(300000, 'Scraper1', true, 'Europe/Warsaw');
INSERT INTO scraper_config (scraper_id, configuration_name, is_active, time_zone) VALUES(400000, 'Scraper4', true, 'Europe/Warsaw');
INSERT INTO scraper_config (scraper_id, configuration_name, is_active, time_zone) VALUES(500000, 'Scraper5', true, 'Europe/Warsaw');

INSERT INTO scraper_error_log (log_id, description, error_code, error_time, scraper_id) VALUES(101, 'Error 0', 'ERR_0', '2022-10-19 23:15:00.015', 100000);
INSERT INTO scraper_error_log (log_id, description, error_code, error_time, scraper_id) VALUES(102, 'Error 0', 'ERR_0', '2022-10-20 23:16:00.011', 100000);
INSERT INTO scraper_error_log (log_id, description, error_code, error_time, scraper_id) VALUES(103, 'Error 0', 'ERR_0', '2022-10-21 23:17:00.013', 100000);
INSERT INTO scraper_error_log (log_id, description, error_code, error_time, scraper_id) VALUES(104, 'Error 1', 'ERR_1', '2022-10-23 23:18:00.022', 100000);
INSERT INTO scraper_error_log (log_id, description, error_code, error_time, scraper_id) VALUES(105, 'Error 1', 'ERR_1', '2022-10-24 23:19:00.020', 100000);
INSERT INTO scraper_error_log (log_id, description, error_code, error_time, scraper_id) VALUES(106, 'Error 1', 'ERR_1', '2022-10-19 23:20:00.019', 200000);
INSERT INTO scraper_error_log (log_id, description, error_code, error_time, scraper_id) VALUES(107, 'Error 10', 'ERR_10', '2022-10-30 23:21:00.011', 200000);
INSERT INTO scraper_error_log (log_id, description, error_code, error_time, scraper_id) VALUES(108, 'Error 1', 'ERR_1', '2022-10-19 23:22:00.015', 300000);
INSERT INTO scraper_error_log (log_id, description, error_code, error_time, scraper_id) VALUES(109, 'Error 20', 'ERR_20', '2022-10-19 23:23:00.020', 300000);

INSERT INTO scraper_status_log (log_id, error_count, start_time, finish_time, scanned_event_count, scraper_id) VALUES(201, 100, '2022-11-03 20:43:44.735', '2022-11-03 20:43:58.794', 230, 100000);
INSERT INTO scraper_status_log (log_id, error_count, start_time, finish_time, scanned_event_count, scraper_id) VALUES(202, 0, '2022-11-03 20:43:44.735', '2022-11-03 20:43:58.794', 30, 100000);
INSERT INTO scraper_status_log (log_id, error_count, start_time, finish_time, scanned_event_count, scraper_id) VALUES(203, 0, '2022-11-03 20:43:44.735', '2022-11-03 20:43:58.794', 0, 100000);
INSERT INTO scraper_status_log (log_id, error_count, start_time, finish_time, scanned_event_count, scraper_id) VALUES(204, 0, '2022-11-03 20:43:44.735', '2022-11-03 20:43:58.794', 2, 200000);
INSERT INTO scraper_status_log (log_id, error_count, start_time, finish_time, scanned_event_count, scraper_id) VALUES(205, null, '2022-11-03 20:43:44.735', '2022-11-03 20:43:58.794', 3, 200000);
INSERT INTO scraper_status_log (log_id, error_count, start_time, finish_time, scanned_event_count, scraper_id) VALUES(206, 0, '2022-11-03 20:43:44.735', '2022-11-03 20:43:58.794', 310, 200000);
INSERT INTO scraper_status_log (log_id, error_count, start_time, finish_time, scanned_event_count, scraper_id) VALUES(207, 80, '2022-11-03 20:43:44.735', '2022-11-03 20:43:58.794', 0, 200000);
INSERT INTO scraper_status_log (log_id, error_count, start_time, finish_time, scanned_event_count, scraper_id) VALUES(208, 100, '2022-10-04 20:43:44.735', '2022-10-04 20:43:58.794', 230, 300000);
INSERT INTO scraper_status_log (log_id, error_count, start_time, finish_time, scanned_event_count, scraper_id) VALUES(209, 100, '2022-10-05 20:43:44.735', '2022-10-05 20:43:58.794', 230, 300000);
INSERT INTO scraper_status_log (log_id, error_count, start_time, finish_time, scanned_event_count, scraper_id) VALUES(210, 100, '2022-11-01 20:43:44.735', '2022-11-01 20:43:58.794', 230, 300000);
INSERT INTO scraper_status_log (log_id, error_count, start_time, finish_time, scanned_event_count, scraper_id) VALUES(211, 100, '2022-11-02 20:43:44.735', '2022-11-02 20:43:58.794', 230, 300000);
INSERT INTO scraper_status_log (log_id, error_count, start_time, finish_time, scanned_event_count, scraper_id) VALUES(212, 100, '2022-11-03 20:43:44.735', '2022-11-03 20:43:58.794', 230, 300000);
