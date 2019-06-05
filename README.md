# simp-csv-processor

Process CSV stream on custom TCP port

## Get sources

```
git clone git@github.com:AndreyShchagin/simp-csv-processor.git
```

## Build
 With Maven: jar will be placed in target/
 ```
 mvn clean package
 ```
 
 Without maven: jar will be in build/libs
 ```
 ./gradlew clean build
 ```
 
## Run

```
java -jar <path_to_jar>/csvParser.jar -p 9000
```

Usage will be printed on the attempt to start without parameters

## How does it work

Parser uses Map to map users with the statistics.  
Map accumulates user information and once the size reaches the threshold which is equal to batch size - the report will be written to file and the map will be cleared (that's how there are no memory leaks).  

Fork-Join pool is used in order to calculate average

## Memory consumption
 
![memory consumption](img/csvParserMem.png "Memory consumption on local environment")
