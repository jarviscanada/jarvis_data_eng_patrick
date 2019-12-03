--Q1
SELECT
  cpu_number,
  id as host_id,
  total_mem
FROM host_info
ORDER BY total_mem DESC;

--Q2
CREATE TEMP TABLE avgs AS
SELECT
  host_info.id as host_id,
  host_info.hostname as host_name,
  100 * (host_info.total_mem - host_usage.memory_free * 1024) / host_info.total_mem AS percentage,
  host_info.total_mem,
  host_usage.timestamp AS timestamp,
  date_trunc('hour', host_usage.timestamp) + INTERVAL '5 min' * ROUND( date_part('minute', host_usage.timestamp) / 5.0) AS itvl
FROM host_info 
RIGHT OUTER JOIN host_usage ON host_info.id=host_usage.host_id;

CREATE TEMP TABLE avgs_5mins AS
SELECT
host_id,
host_name,
total_mem,
itvl,
CAST(AVG(percentage) AS INTEGER) AS avg_usage
FROM avgs
GROUP BY host_id, host_name, total_mem, itvl;

SELECT
host_id,
host_name,
total_mem/1024 AS total_mem,
avg_usage
FROM avgs_5mins

