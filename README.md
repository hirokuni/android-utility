android-utility
===================

Overview
--------

Android utility for quick creation of application. When I make Android application, duplicated code are often come up. I created this repository to reduce my double work.


License
-------

Apache License, Version 2.0


How to make maven output
-------
[Goode example] <https://github.com/TakahikoKawasaki/nv-websocket-client> 

1. go to root dir
2. ./gradlew uploadArchives
3. It is successful if repository directory contain some files including pom file etc.

version
-------
* 0.0.4 : nano http server
* 0.0.3 : PCM recording with mic and async task utililty (media package and os package).

Gradle setting in client application
-------

```Gradle
repositories {
    maven { url 'https://github.com/hirokuni/android-utility/raw/master/repository' }
}
dependencies {
    compile 'jp.hkawasaki:android-util-lib:0.0.1'
}
```

Permission in client application
-------
```Permission
  <uses-permission android:name="android.permission.INTERNET" />
  <uses-permission android:name="android.permission.RECORD_AUDIO"></uses-permission>
```
