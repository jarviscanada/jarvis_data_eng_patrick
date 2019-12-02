# !/bin/bash
###start & stop psql instance

operation=$1
db_password=$2
export PGPASSWORD="$db_password"

# script usage 
# ./scripts/psql_docker.sh start|stop [password for database]

# check if there is too many arguments
if (($#>2));
then
	echo "Invalid Argument"
	echo "usage: psql_docker.sh start|stop [password for database]"
	exit 1
fi

# check arguments is start or stop
if [[ "$operation" == "start" && "$#" == 2 ]];
then
	# start docker_deamon
	systemctl status docker || systemctl start docker
	# check if computer is turned on
	if [[ "$(docker ps -f name=jrvs-psql | wc -l)" == 2 ]];
	then
		exit 0
	fi
	# check if the volume is created or not
	if [[ "$(docker volume ls | egrep "pgdata1$" | wc -l)" == 0 ]];
	then
		# delete the container named jrvs-psql if there is one
		if [[ "$(docker ps -a -f name=jrvs-psql | wc -l)" == 2 ]];
		then
			docker stop jrvs-psql
			docker container rm jrvs-psql
		fi
		# create the volume and container
		docker volume create pgdata
		docker run --name jrvs-psql -e POSTGRES_PASSWORD=$PGPASSWORD -d -v pgdata:/var/lib/postgresql/data -p 5432:5432 postgres
	# check if the container is created or not
	elif [[ "$(docker container ls -a | egrep "jrvs-psql$" | wc -l)" == 0 ]];
	then 
		docker run --name jrvs-psql -e POSTGRES_PASSWORD=$PGPASSWORD -d -v pgdata:/var/lib/postgresql/data -p 5432:5432 postgres
	else
		#start the container
		docker start jrvs-psql
		exit 0
	fi
# check if it is stop 
elif [[ "$operation" == "stop" && "$#" == 1 ]];
then
	#stop the container & the docker
	systemctl status docker && docker stop jrvs-psql
	systemctl status docker && systemctl stop docker
	exit 0
else
	echo "invalid arguments"
	echo "usage: psql_docker.sh start|stop [password for database]"
	exit 1
fi

exit 0
