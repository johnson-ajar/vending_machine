## Vending Machine Monitoring Application
## Application Modules.
This application contain the following modules under the root folder vending_machine.
>	- vending_model
>	- vending_service
>	- vending_application
>	- vending_ui

All these modules can be build using the command `./gradlew build` under the root folder vending_machine. Individual modules cannot be build at the moment.
The module vending_application and vending_ui are spring boot application, which contain individual Dockerfile. Details regarding building and running the application is provided below.

### 1. Module(vending_model).
>This module contain the vending machine model and java api which provide the functionality to check the state of the vending machine. The state of the vending machine is defined by the following parameter.
> ##### 1.1. Vending machine coin registry.
> The machine coin registry has a default initialisation, when a machine is created and added to the machine monitoring service. The API provide the functionality to change the default machine coin registry setting.

> ##### 1.2. Vending machine user coin registry.
> The machine user coin registry is responsible for keeping account of the coins used by different user to buy the products from vending machine. The API provide the functionality to update the user coin registry whenever a user submit a payment to the machine. The machine makes a call to the monitoring service to update the user coin registry for the called machine.

> ##### 1.3. Vending Bank (Cashier)
> Each vending machine has a vending bank which is responsible for maintaining the machine and user coin registry. The coins are not transfered from the user registry when the machine registry is empty in this implementation. To give change the coins are selected by giving equal weightage to all the coin type. A weighted approach based on the availability of the coins will needs to be implemented. If a particular coin type is higher in number it will be given more weightage compared to other types. This weightage will dynamically change during each payment cycle. This can be implemented by extends AbstractMachineBank.
 
### 2. Module (vending_service)
> This module provide the definition for the rest api and swagger docs definitions. The documentation for the rest api can be obtained by using the following url `http://localhost:8080/api/v1/swagger-ui.html` when the services are run in docker. If the vending_application is run outside docker after a gradle build, run `java -jar vending_application.jar` in folder `vending_machine/modules/vending_application/build/libs`, use the following url for the rest api docs `http://localhost:7001/api/v1/swagger-ui.html`.
 
### 3. Module (vending_application)
> This module contain the implementation of the rest api and entry point to the monitoring application. This is run as a spring boot application at port 7001 outside docker. Inside docker it is run in port 8080. This can be changed in the application.yml. After running gradle build at root. Run `java -jar vending_application.jar` in folder `vending_machine/modules/vending_application/build/libs`, use the following url for the rest api docs `http://localhost:8080/api/v1/swagger-ui.html`.
Spring hateoas would be used to add related links within the response data.
 
### 4. Module (vending_ui)
> This is a separate sping boot application which run the rich web application. This can be used to interact with each machine and monitor the registries of each machine. After running gradle build at root. Run `java -jar vending_ui.jar` in folder `vending_machine/modules/vending_ui/build/libs`, if you want to run the web application outside docker. Use the following url to open the application `http://localhost:8081/vending`. This module contain two source folder src and webclient. src folder contain the java code and webclient contain the javascript web client code. The content in the webclient folder can be build and run separately without the need for spring boot using node using. Check package.json the command available. To build run `yarn build`. To run application use `yarn start`. The application can be opened using the  url `http://localhost:8081/vending/`. To compile the web application code nodejs, yarn and npm is required globally. The instruction provided below are for installing in ubuntu.

> Note: **Make sure nodejs, yarn and npm are installed globally.**

> Note: **Use the refresh button to reset the sliders to default machine/user coin registry.**

> To install nodejs from Nodesource. 
>> The script will add the Node source signing key to your system create an apt repository file, install all necessary package, and refresh apt cache.
>>>> `curl -sL https://deb.nodesource.com/setup_12.x | sudo -E bash -`

>> Once NodeSource repository is enabled. install Node.js and npm.

>>>> `sudo apt-get install nodejs`

>>>> `npm to install yarn`

>>>> `sudo npm install --force --global yarn`

## Building and Running the Application.
### 1. Build the application.
To build the two application vending_application, vending_ui, go to the root folder vending_machine and run `./gradlew build`. The build should compile and build all the modules. The vending_application provides the rest service and vending_ui provides a UI interface to test the rest api as well as the java api.
There are several ways to run this application and test it.
> - Outside Docker.
> - Inside Docker.

##### 1.1 Outside Docker.
> Application vending_application and vending_ui. Both are under the folder `vending_machine/modules`
> Go to the folder `vending_machine/modules/vending_application/build/libs` and run the command `java -jar vending_application.jar`. This should start the Rest api service. To check if its working, open the browser and enter the url `http://localhost:8080/api/v1/swagger-ui.html`. This should open a document files provide details about the REST calls provided by this service. The application is initialized with two machine which are named machine1 and machine2. These machines are initialized with a default coin registry. The user coin registry will be empty. To list all the initialized machine `http://localhost:8080/api/v1/vending/machine/all`

> Next go to the folder `vending_machine/modules/vending_ui/build/libs` and run the command `java -jar vending_ui.jar`. This should start the web application. To check if its working, open a browser tab and enter the url `http://localhost:8081/vending`. You should see a single page application. To use the web application details are provided below.

#### 1.2 Inside Docker.
> This require Docker to be available on the machine. From vending_machine root folder, run ./gradlew build. This should compile and package the application as jar. There is a `docker-compose.yml` in the root folder. Run the command `docker-compose build` to create images of the two application `vending/application and vending/ui`. After creating the images. Run the application in docker using the command `docker-compose up -d --remove-orphans`. Command `docker ps` should list these two containers `vending_machine_vending_ui and vending_machine_vending_monitor_1`. `vending/application and vending/ui`. To check everything is up and running, open the browser and enter the url `http://localhost:8080/api/v1/swagger-ui.html` and `http://localhost:8081/vending`. The first url opens the rest api swagger documentation and the second url open the single page web application.




