# Gakusci #

The knowledge aggregator

## What is it? ##

Gakusci is a web aggregator that can be used to search for any document/information/piece of knowledge about a specific topic (research, books, music, ...).  


## How it works ##

Gakusci directly retrieves data from some platforms dedicated to a search domain.  
The application can search for documents from different domains (some of them will be dealt in the future):

- Anime
- Books
- Manga
- Music (soon)
- Images/Photos (soon)
- Research papers/articles

For now, Gakusci only interacts with services that expose a public API and do not require authentication.
Their API can be REST(-like/-ful) or based on ATOM/RSS syndication.

## Build & run ##

You simply build it with:

```
gradle build
```

You run it with:

```
gradle run
```


You can create a fat JAR like this:

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

You can build a docker image of the application with:

```
gradle jibDockerBuild
```

It uses [Jib](https://github.com/GoogleContainerTools/jib) in order to automate the creation of a docker image.

If you want to run it "in production", because you want to get your own instance, run this command:

```
docker run -it -p 80:80 --rm gakusci -config=/app/resources/application-prod.conf
```

You can also build an image tarball and load it with:

```
gradle jibBuildTar
docker load --input build/jib-image.tar
```

More info [here](https://github.com/GoogleContainerTools/jib/blob/master/docs/faq.md#can-i-build-to-a-local-docker-daemon)

## API ##

A basic endpoint:

```
/api/v1/{search_type}/?q=<query>
```
Where **searth_type** is one of :

- *papers*
- *books*
- *mangas*

Get at most N elements

```
/api/v1/{search_type}/?q=<query>&rows=N
```

You can paginate the results like this:

```
/api/v1/{search_type}/?q=<query>&start=0&rows=N
```

## Licence ##

This web application is under [CeCILL](https://cecill.info/licences/Licence_CeCILL_V2.1-en.txt) v2.1.
