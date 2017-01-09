# TokenStore

TokenStore is a mongoDB store.


### MongoDB
* Follow google to find out how to install mongo on your local. On OSX `$ brew install mongo` should just work.
* Create a db directory in your user directory - `$ mkdir -p ~/mongo/data/db`
* Start mongo  - `$ mongod --dbpath  ~/mongo/data/db` . 
Or follow this https://alicoding.com/how-to-start-mongodb-automatically-when-starting-your-mac-os-x/ on  OSX to get it started automatically, or other mechanisms
out there to get mongod get going ( no_hup bash_profile, launcher etc )
* Typically local mongodb runs on `localhost:27017`
* Create an admin user/password if not created 
`mongo> use admin` and then 
`db.createUser( { user: "admin", "pwd": "admin" , roles: [{role: "userAdminAnyDatabase", db: "admin"}]})`.

* Then disconnect from the mongo shell and restart mongod ( or change your service ) with `$mongod --auth --dbpath  ~/mongo/data/db`
* Now open a terminal and try to connect to your local mongo with credentials: 
`$ mongo admin -u admin -p`
enter admin password
 * Now create a 'user' user for the app: 
 `mongo> use tokenstoredb`
 `mongo> var user = {
          "user" : "tokenstore_user",
          "pwd" : "Password1!",
          roles : [
              {
                  "role" : "readWrite",
                  "db" : "tokenstoredb"
              }
          ]
        };
 `
 `mongo> db.createUser(user);`
 `exit`

* Test the login with user
`$ mongo tokenstoredb -u tokenstore_user -p`
* On higher env, its recommended to use replicaSets. Research that option if needed and modify the mongo-uri in application.yml
    
# Testing with browser and curl

* Start process
This link on a browser - http://localhost:9999/uaa/oauth/authorize?response_type=code&client_id=acme&redirect_uri=http://example.com
* check token 
`curl acme:acmesecret@localhost:9999/uaa/oauth/token -d grant_type=authorization_code -d client_id=acme -d redirect_uri=http://example.com -d code=KYInMn`