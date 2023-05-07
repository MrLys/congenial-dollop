# congenial-dollop
spring boot backend for the budzilla project

## Running
* Make sure you have added `export BUDZILLA_USER=<your user>` and  `export BUDZILLA_PASSWORD=<your pwd>` to your environment variables
## Budiling
* `./gradlew bootJar`
* `docker build -t budzilla:<version> .`
* `docker tag budzilla:<version> <aws ecr budzilla repo url>/budzilla:latest`
* `docker push <aws ecr budzilla repo url>/budzilla:latest`

* aws ecr get-login-password --region eu-north-1 | docker login --username AWS --password-stdin <aws ecr budzilla repo url>
