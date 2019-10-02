vlingo IoT Example
===================

This is a tracker application that allows users to see their way while hiking and let's other users to know when they
are in danger with a single button.

How To Run It
=============

This application depends on a PostgreSQL and a vlingo backend. You will need to create a shadowJar from the backend:

 ```sh
 $> ./gradlew backend:shadowJar
 ```
 
And run the docker-compose.yml file:

```sh
$> docker-compose -f backend/docker-compose.yml up
```

When it's done, make sure that you write your IP in the [MainActivity](app/src/main/java/com/example/vlingohike/MainActivity.kt)
so the mobile application can see your backend running in Docker.

You will need also to set up a Google Maps API Key in the [Android Manifest](app/src/main/AndroidManifest.xml):

```xml
        <meta-data android:name="com.google.android.geo.API_KEY" android:value="YOUR_API_KEY"/>
```
