## Introduction
The main task about this project is we want to record the hardware specification of 10 different nodes and monitor nodes resource usages such as memory
used in realtime. We collect data using shell scripts and store the data in postgreSQL database. This help the LCA team to monitor the hardware status
of each node and be able to analyze those data in order to prevent potential issues.  
  
## Architecture and design
![my image](./assets/arch.png) 
# Database  
# host_info table store the information of the hardware specifcations data of the host.  
* `id`: Unique id number corresponding to each node, is a primary key in the table and auto-incremented by PostgreSQL
* `hostname`: The fully qualified hostname of the node; assumed to be an indentifier and subject to a unique constraint
* `cpu_number`: Number of cores in the CPU
* `cpu_architecture`: The CPU's architecture
* `cpu_model`: Manufacturer and offical model designation of the CPU
* `cpu_mhz`: CPU speed
* `L2_cache`: Size of the L2 cache, measured in kB
* `total_mem`: Total amount of memory on the system, measured in kB
* `timestamp`: UTC timestamp of when node hardware specifcations were collected  
# host_usage table store server usage data of the host.  
* `timestamp`: UTC timestamp of when node resource usage information was collected
* `host_id`: The node's id, corresponds to id in the host_info table and represents a foreign key constraint
* `memory_free`: Amount of idle memory, measured in MB
* `cpu_idel`: Percentage of total CPU time spent running kernel/system code
* `cpu_kernel`: Percentage of total CPU time spent idle
* `disk_io`: Number of current disk I/O operations in progress
* `disk_available`: Amount of disk space available, measured in MB  
# File Description:  
* `ddl.sql` is used to create database and table to store the data we collect, we have two tables to store hardware infomation and usage.  
* `queries.sql` is used to write sql queries to answer business question in order to manage the cluster better.  
* `psql_docker.sh` is used to start docker server, create docker container and volumn  
* `host_info` is used to connect to the database, collect hardware infomation, and store in our psql database  
* `host_usage` is used to connect to the database, collect hardware usage, and store in our psql database  
  
## usage
1. `psql_docker.sh` takes 1 to 2 inputs, it can be either start or stop the docker.  
To start
```
start:psql_docker.sh start password
```
To stop
```
psql_docker.sh stop
```  

2. `host_info.sh` takes 5 input: psql_host, psql_port, db_name, psql_user, psql_password.  
 * we connect to `psql_host` through `psql_port` as `psql_user` with `psql_password` and store data in `db_name`.  
```
bash ./scripts/host_info.sh psql_host psql_port db_name psql_user psql_password
```  
 
3. `host_usage.sh` takes 5 input: psql_host, psql_port, db_name, psql_user, psql_password.
 * we connect to `psql_host` through `psql_port` as `psql_user` with `psql_password` and store data in `db_name`.  
```
bash ./scripts/host_usage.sh psql_host psql_port db_name psql_user psql_password
```  
 
4. `ddl.sql` usage:  
```
psql -h localhost -U postgres -w db_name -f ./.../ddl.sql
```  

5. `queries.sql` usage:  
```
psql -h localhost -U postgres -w db_name -f ./.../queries.sql
```  
## Improvement  
1.could collect more data in host_usage and host_info for monitoring.  
2.could have functions that display the data we collect.  
3.could use one scripts to run all the sub scripts and sql files.  
