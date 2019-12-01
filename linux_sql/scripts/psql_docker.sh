# !/bin/bash
###start & stop psql instance

operation=$1
db_password=$2
export PGPASSWORD="$db_password"

case "$operation" in
	start)
		docker run --rm --name jrvs-psql -e POSTGRES_PASSWORD=$PGPASSWORD -d -v pgdata:/var/lib/postgresql/data -p 5432:5432 postgres
		psql -h localhost -U postgres
		;;
	stop)
		docker stop jrvs-psql
		;;
	*)
		echo "Usage: ./psql_docker.sh start|stop [dbpassword]"
		exit 1
esac

exit 0
	


