# Linux Cluster Monitoring Agent
This project is under development. Since this project follows the GitFlow, the final work will be merged to the master branch after Team Code Team.

![architecture](./assets/architecture.jpg)

host_info.sh: collects the host hardware info and insert it into the database. It will be run only once at the installation time.

host_usage.sh: collects the current host usage (CPU and Memory) and then insert into the database. It will be triggered by the crontab job every minute.

queries.sql: Writing sql queries to answer some business question.