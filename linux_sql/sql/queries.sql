SELECT 
  cpu_number,
  id as host_id,
  total_mem
FROM
  host_info
ORDER BY total_mem DESC