#! /bin/bash

# Script usage
#./scripts/host_info.sh psql_host psql_port db_name psql_user psql_password
# Example
#./scripts/host_info.sh "localhost" 5432 "host_agent" "postgres" "mypassword"

#Setup arguments
psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
password=$5
export PGPASSWORD=$password

#validate arguments
if [ "$#" -ne 5 ]; then
    echo "Illegal number of parameters"
    echo "usage: host_info.sh psql_host psql_port db_name psql_user psql_password"
    exit 1
fi

#collecting hardware specification data
hostname=$(hostname -f)
lscpu_out=$(lscpu && vmstat --stats)
cpu_number=$(echo "$lscpu_out"  | egrep "^CPU\(s\):" | awk '{print $2}')
cpu_architecture=$(echo "$lscpu_out" | egrep "^Architecture:" | awk '{print $2}')
cpu_model=$(echo "$lscpu_out" | egrep "^Model\sname:" | awk '{$1=$2=""; print $0}')
cpu_mhz=$(echo "$lscpu_out" | egrep "^CPU\sMHz:" | awk '{print $NF}')
L2_cache=$(echo "$lscpu_out" | egrep "^L2\scache:" | awk '{print $3}' | grep -o '[0-9]\+')
total_mem=$(cat /proc/meminfo | egrep "^MemTotal:" | awk '{print $2}')
timestamp=$(echo "$(vmstat -t)" | tail -n 1 | awk '{printf("%s %s",  $(NF-1), $NF)}')

echo $timestamp

#insert the data to the psql instance.
psql -h $psql_host -U $psql_user -w $db_name -p $psql_port -c "INSERT INTO host_info (hostname, cpu_number, cpu_architecture, 
							       cpu_model, cpu_mhz, L2_cache, total_mem, timestamp)
							       VALUES ('$hostname', '$cpu_number', '$cpu_architecture', 
							       '$cpu_model', '$cpu_mhz', '$L2_cache', '$total_mem', '$timestamp');"
							   
exit 0