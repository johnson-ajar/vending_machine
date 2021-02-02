Vending Machine Monitoring Application.
Dockerise the application to run in a container. 
1. Build a docker image
docker build -t vending/application .
2. Running the docker image.
docker run --rm -p8080:8080 -e "SPRING_PROFILES_ACTIVE=docker" vending/application
3. Write a docker-compose.yml to build the docker image and run the image