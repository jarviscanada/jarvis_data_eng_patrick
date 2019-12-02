--Q1
SELECT
  cpu_number,
  id as host_id,
  total_mem
FROM
  host_info
ORDER BY total_mem DESC;

--Q2
CREATE TEMP TABLE avgs AS
SELECT
  host_info.id as host_id,
  host_info.hostname as host_name,
  (host_info.total_mem * 1.0 - host_usage.memory_free * 1024.0) / host_info.total_mem * 100 AS percentage,
  host_info.total_mem,
  host_usage.timestamp AS timestamp
FROM
  host_info RIGHT OUTER JOIN host_usage ON host_info.id=host_usage.host_id;

ALTER TABLE avgs ADD COLUMN itvl timestamp;
UPDATE avgs SET itvl=date_trunc('hour', timestamp) + INTERVAL '5 min' * ROUND( date_part('minute', timestamp) / 5.0);

CREATE TEMP TABLE avg_5min AS
SELECT 
  host_id, 
  host_name, 
  total_mem, 
  AVG(percentage) over (partition by itvl) AS avg_usage
FROM 
  avgs;

SELECT host_id, host_name, total_mem, AVG(avg_usage)
FROM avg_5min
GROUP BY host_id, host_name, total_mem;