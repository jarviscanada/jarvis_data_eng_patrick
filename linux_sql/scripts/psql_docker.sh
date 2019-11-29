# !/bin/bash
###start & stop psql instance

operation=$1
db_password=$2
export PGPASSWORD="$db_password"

if [ $operation = "start"]; then

  docker container start jrvs-psql
  
  sudo systemctl start docker
  if docker image ls | grep -q '^postgres'; then
    echo "postgres image found"
  else
    docker pull postgres
  fi
  
  if docker volume ls -f name=pgdata | grep -q 'pgdata'; then
    echo "docker volume pgdata already exists"
  else
    docker volume create pgdata
  fi
  docker run --name jrvs-psql -e POSTGRES_PASSWORD=$PGPASSWORD -d -v pgdata:/var/lib/postgresql/data -p 5432:5432 postgresql
  
  docker container start jrvs-psql
  
  psql -h localhost -U postgres
  
else
  docker container stop jrvs-psql
  
  docker container rm jrvs-psql
fi

exit 0
	


