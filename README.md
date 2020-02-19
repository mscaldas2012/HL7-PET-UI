# Intro 

This project is a UI to test HL7-PET extracting functionality.
It uses gradle as the build tool, where it combines both client and server into a single jar

The server is a Micronaut REST API that caches HL7ParseUtils on behalf of the client

The client is a react app, single page, built with yarn .

- Check the Dockerfile to see how to build this project.

Basically, run:

 <code>gradle wrapper</code> 

 <code>./gradlew assembleServerAndClient</code>



### References
project based on https://guides.micronaut.io/micronaut-spa-react/guide/index.html
github: https://github.com/micronaut-guides/micronaut-spa-react
