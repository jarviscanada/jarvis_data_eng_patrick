#! /bin/bash

# Script usage
#bash scripts/host_usage.sh psql_host psql_port db_name psql_user psql_password
# Example
#bash scripts/host_usage.sh localhost 5432 host_agent postgres password

psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
password=$5
export PGPASSWORD=$password

#collects server usage data
vmstat_out=$(vmstat --stats --unit M && vmstat --disk-sum --unit M && vmstat -wt)

timestamp=$(echo "$vmstat_out" | tail -n 1 | awk '{printf("%s %s",  $(NF-1), $NF)}')
host_name=$(hostname -f)
memory_free=$(echo "$vmstat_out" | egrep "free memory" | awk '{print$1}')
cpu_idel=$(echo "$vmstat_out" | tail -n 1 | awk '{print $(NF-2)}')
cpu_kernel=$(echo "$vmstat_out" |awk 'END {print $14}')
disk_io=$(echo "$vmstat_out" | egrep "inprogress" | awk '{print $1 * 1000}')
disk_available=$(echo "$(df -BM /)" | tail -n 1 | awk '{print substr($4, 0, length($4)-1)}')

#insert the data into the psql database.
psql -h $psql_host -U $psql_user -w $db_name -p $psql_port -c "INSERT INTO host_usage (timestamp, host_id, memory_free,
							       cpu_idle, cpu_kernel, disk_io, disk_available)
							       VALUES ('$timestamp', 
							       (SELECT id FROM host_info WHERE hostname='$host_name'),
							       '$memory_free', '$cpu_idel','$cpu_kernel', '$disk_io', 
							       '$disk_available');"
							
exit 0