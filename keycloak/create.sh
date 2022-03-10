#
# Run the official Keycloak docker image
#
KEYCLOAK_VERSION=16.1.0
REALM_NAME=development
# KEYCLOAK DOCKER (name=keycloak) is showing some error and need to stoped
docker stop $(sudo docker ps -a | grep 'keycloak' | awk '{print $1}') && \
docker rm -f $(sudo docker ps -a | grep 'keycloak' | awk '{print $1}') && \
#docker network rm  $(sudo docker ps -a | grep 'keycloak' | awk '{print $1}') && \

docker run -p 8000:8080 -e KEYCLOAK_LOGLEVEL=DEBUG -e KEYCLOAK_USER=admin -e KEYCLOAK_PASSWORD=admin \
	--mount type=bind,src="$(pwd)"/realm-data,dst=/realm-data \
	-e JAVA_OPTS_APPEND="-Dkeycloak.migration.action=import -Dkeycloak.migration.provider=dir -Dkeycloak.migration.dir=/realm-data -Dkeycloak.migration.realmName=$REALM_NAME" \
	--name keycloak \
	quay.io/keycloak/keycloak:${KEYCLOAK_VERSION}
