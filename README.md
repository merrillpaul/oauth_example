# Design
* Auth server and resource (api) server are separate applications
* Auth server authenticates and should based on the principal find out the principal's nearest
mongo store to store the token information
* This is to check the token status by the APi server ( 

By default the api server will have a well known url to the mongo db token store relative to it)
For eg an API server in San Antonio , TX will have a route to the mongodb as 
mongodb://tokenstore_user:Password1!@local_mongo_store:27017/tokenstoredb
where local_mongo_store might be a localhost or a host name routed to a local IP

## AuthServer
* UserDetails in mysql
* OAuthToken Server saves token into master token store and user specific token store via JMS
* ApiServer in the workspace refers to token store with *_US store ( configured in yaml). In reality there will be multiple apiservers with respective token store servers

# Setup
* `./gradlew idea`
* Import project in intelliJ

# Mysql
For user, role and metadata

```
$ mysql -u root
create database oauth_example;
create user scott;
grant all on oauth_example.* to 'scott'@'localhost' identified by 'tiger';
grant all on oauth_example.* to 'scott'@'%' identified by 'tiger';
grant select, insert, delete, update on oauth_example.* to 'scott'@'localhost' identified by 'tiger';
grant select, insert, delete, update on oauth_example.* to 'scott'@'%' identified by 'tiger';
exit

$ mysql -u scott oauth_example -p
```

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
* repeat the above for tokenstoredb_US and tokenstoredb_FR
* Test the login with user
`$ mongo tokenstoredb -u tokenstore_user -p`
* On higher env, its recommended to use replicaSets. Research that option if needed and modify the mongo-uri in application.yml

# Starting the process
* `./gradlew --parallel runAll`
    
# Testing with browser and curl
## Auth Code flow
* Start process
This link on a browser - http://localhost:9999/uaa/oauth/authorize?response_type=code&client_id=acme&redirect_uri=http://example.com
* check token 
`curl acme:acmesecret@localhost:9999/uaa/oauth/token -d grant_type=authorization_code -d client_id=acme -d redirect_uri=http://example.com -d code=KYInMn`
* Now connect to the Resource(API) Server without any token just to see that it shows an unauthroized error
`
curl -v localhost:9998/api/
`
```
> Trying ::1...
> Connected to localhost (::1) port 9998 (#0)
> GET /api/ HTTP/1.1
> Host: localhost:9998
> User-Agent: curl/7.43.0
> Accept: */*
> 
< HTTP/1.1 401 
< Cache-Control: no-store
< Pragma: no-cache
< WWW-Authenticate: Bearer realm="oauth2-resource", error="unauthorized", error_description="Full authentication is required to access this resource"
< X-Content-Type-Options: nosniff
< X-XSS-Protection: 1; mode=block
< X-Frame-Options: DENY
< Content-Type: application/json;charset=UTF-8
< Transfer-Encoding: chunked
< Date: Tue, 10 Jan 2017 22:53:31 GMT
< 
< Connection #0 to host localhost left intact
< {"error":"unauthorized","error_description":"Full authentication is required to access this resource"}
```

* Now connect with the token you got above
`
 TOKEN=942f3afc-2a4c-45e8-b935-0b98e6c8bc9a ;
 curl -H "Authorization: Bearer $TOKEN" localhost:9998/api/foo/foo/4500 
`

## Password Flow
Allows a custom password page and using xhr we request for a token
* Invoke /oauth/token
This is how we get the token
`
curl -X POST  -u acme:acmesecret http://localhost:9999/uaa/oauth/token -H "Accept: application/json" -d "password=Password1&username=jon_us&grant_type=password&scope=read&client_id=acme"
`
Response is 
`
{"access_token":"21e5862c-d74b-4485-9fa4-79e2312a3982","token_type":"bearer","refresh_token":"8b4f0c94-8017-419a-addb-ebead4afca95","expires_in":43196,"scope":"read"}
`



## Implicit Flow
* Link on Browser -> http://localhost:9999/uaa/oauth/authorize?response_type=token&client_id=acme&redirect_uri=http://example.com
* After authentication gets redirected to http://example.com/#access_token=c77a25b5-03b3-40b4-9cbb-6d291e88f553&token_type=bearer&expires_in=43183&scope=read
* Use that token to fire an API request
`
TOKEN=93810147-ec08-4da8-818f-f62c9718fb6f; curl -H "Authorization: Bearer $TOKEN" localhost:9998/api/foo/foo/443434500 -v
`
```
>  Trying ::1...
> Connected to localhost (::1) port 9998 (#0)
> GET /api/foo/foo/443434500 HTTP/1.1
> Host: localhost:9998
> User-Agent: curl/7.43.0
> Accept: */*
> Authorization: Bearer 93810147-ec08-4da8-818f-f62c9718fb6f
> 
< HTTP/1.1 200 
< X-Content-Type-Options: nosniff
< X-XSS-Protection: 1; mode=block
< Cache-Control: no-cache, no-store, max-age=0, must-revalidate
< Pragma: no-cache
< Expires: 0
< X-Frame-Options: DENY
< Content-Type: application/json;charset=UTF-8
< Content-Length: 56
< Date: Wed, 11 Jan 2017 16:17:44 GMT
< 
< Connection #0 to host localhost left intact
{"fooName":"Foo 443434500 Name","fooId":"FID 443434500"}
```

# Checktoken
curl -u acme:acmesecret http://localhost:9999/uaa/oauth/check_token -H "Accept: application/json" -d token=$TOKEN


# Logout
* From Browser or Ajax call http://localhost:9999/uaa/oauth/logout_token?token=ca7dc0a3-2e46-4798-8218-072d7a25743c or
* TOKEN=sometoken; curl http://localhost:9999/uaa/oauth/logout_token -d token=$TOKEN

# References
* http://www.baeldung.com/rest-api-spring-oauth2-angularjs