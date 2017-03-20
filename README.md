play2letsencrypt
============================

A one-stop shop to get your Play Framework + ReactiveMongo project to use a Let's Encrypt certificate,
and force all connections to use HTTPS.

After installing this library, the [Qualys SSL Server Test](https://www.ssllabs.com/ssltest/index.html) should give you A+ scores across the board!

### Installation

Bring in the library by adding the following to your ```build.sbt```. 

  - The release repository: 

```
   resolvers ++= Seq(
     "Millhouse Bintray"  at "http://dl.bintray.com/themillhousegroup/maven"
   )
```
  - The dependency itself: 

```
   libraryDependencies ++= Seq(
     "com.themillhousegroup" %% "play2letsencrypt" % "0.1.0"
   )

```

### Usage

Once you have __play2letsencrypt__ added to your project, you can start using it like this:

```
foo
bar
baz 
```


### Credits
Let's Encrypt

