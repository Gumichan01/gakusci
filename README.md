# Gakusci #

The knowledge aggregator

## What is it? ##

Gakusci is a web aggregator that can be used to search for any document/information/piece of knowledge about a specific topic (research, books, music, ...).  


## How it works ##

Gakusci directly retrieves data from some platforms dedicated to a search domain.  
For the moment, this application can only deal with research papers/articles. Some other domains will be dealt with in the future (not exhaustive):

- Anime (WIP)
- Books
- Music (WIP)
- Open Data (?)
- Images/Photos (WIP)
- Research papers/articles

Gakusci only interacts with services that expose a public API and do not require authentication.
Their API can be REST(-like/-ful) or based on ATOM/RSS syndication.

## Build & run ##

Just do this:

```
gradle run
```

You can create a fat JAR like this: 

```
gradle build
```
or
```
gradle jar
```

and launch it with:

```
java -jar build/libs/<jar_name>.jar
```

If you want to run tests:

```
gradle test
```

### Docker ###

You can build a docker images of the application with:

```
gradle jibDockerBuild
```

It uses [Jib](https://github.com/GoogleContainerTools/jib) in order to automate the creation of a docker image.

If you want to run it "in production", because you want to get your own instance, run this command: 

```
docker run -it -p 80:80 --rm gakusci -config=/app/resources/application-prod.conf
```

## Contributing ##

- [ ] TODO

## Licence ##

Thsi web application is under [CeCILL](https://cecill.info/licences/Licence_CeCILL_V2.1-en.txt) v2.1.
