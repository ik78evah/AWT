# Mobile Backend as a Service
Repository for the Advanced Web Technologies Project WiSe 21/22 - Mobile Backend as a Service - Group 3

## Run
#### Backend
> `MAVEN` needs to be installed

Start the backend locally with 
```
$ cd Backend
$ mvn clean install
$ cd target
$ java -jar mBaaS-0.0.1-SNAPSHOT.jar
```

The application can then be accessed under `localhost:8080` using your local browser or `curl`.

#### Frontend
**IMPORTANT:**
The application requires the following minimum version in order to work.
* Required minimum NPM version: `8.5.4`
* Required minimum node version: `17.7.1`

Start the frontend locally with: 
```
$ cd Frontend
$ npm install
$ npm start
```
The application can then be accessed under `localhost:3000` using your local browser. 

### Demo Projects
Currently, two demo projects exist, Todo List (`Demo1`) and Shopping List (`Demo2`). They can be started from the root folder with
```
$ cd Demo1
$ npm install
$ npm start -- --port 3001
```
```
$ cd Demo2
$ npm install
$ npm start npm start -- --port 3001
```
They run on port 3001 and 3002, respectively.

If you want to use the demo backend `demo`, too, start it with
```
$ cd demo
$ mvn clean install
$ cd target
$ java -jar demo-0.0.1-SNAPSHOT.jar
```


### Postman
For testing the APIs in Postman, you need to create user first and login in order to receive a JWT token. For any other API request, the new JWT token must be replaced in the header.