* Installing the jythonconsole into your local Maven repository
1) Download the jythonconsole from https://code.google.com/p/jythonconsole/downloads/list
2) Unfortunately, the zip file entries have a directory specified with the version number "jythonconsole-0.0.7", so we need
to unpack it and rezip it from that directory, so that the directory entry is no longer in the archive.
3) We then install the newly created archive into our local repo with the following command:
mvn install:install-file -Dfile=jythonconsole-0.0.7.zip -DgroupId=com.google.code -DartifactId=jythonconsole -Dversion=0.0.7 -Dpackaging=zip
