Note: the documentation below was Tested with:
* Neon 3, Eclipse IDE for Committers, 32 bit, on Windows 7.
* Eclipse IDE (2019-03), 64 bit, Linux (Ubuntu-Debian). 

# Building with MAVEN:

1. Download Eclipse, one of the Java developers versions ([download Neon 3](https://www.eclipse.org/downloads/packages/eclipse-ide-eclipse-committers/neon3))). ([other versions](https://www.eclipse.org/downloads/packages/release/neon/3))

1. Either work with the command line, or download Github Desktop ([download](https://desktop.github.com/)).

1. Fork the DSMN github repository to your own account, click on "Clone or Download" (green button), and then on “Open in Desktop”.

1. In the toolbar, click on FILE/Import/Maven/"Existing Maven Projects” and click next.

1. Browse to the folder where your local installation of the repository is saved and select it. All folders and files will be shown in the main field of this page. Deselect the Launcher (in Modules folder).

![image](https://user-images.githubusercontent.com/26277832/63592643-586e8780-c5b2-11e9-9cff-87a489e997fd.png)

6. Click on finish.

7. The different folders from the github repository should now appear on the left in your workspace (under package explorer). Normally, errors will show up at this point.

8. Update the project by right click on the project folder, select Maven/"Update Project..." and click OK. If everything went well, all errors should disappear.
![image](https://user-images.githubusercontent.com/26277832/63685685-513cb900-c800-11e9-8ea1-c0cfbdaab05c.png)

9. Select the pom.xml file, and with right click select Run as/ 1. Maven build. 

![image](https://user-images.githubusercontent.com/26277832/63592958-21e53c80-c5b3-11e9-8c5c-a419724bbc8f.png)

However, if you have never build this project before in Maven with Eclipse, you can also select Run as/"Run Configurations..." and add the following info (such as the Maven goal: "clean install"). Click Apply and Run.

![image](https://user-images.githubusercontent.com/26277832/63692885-fcef0480-c812-11e9-9448-53e872696e0b.png)

10. You should see the output in the Console window (Text: BUILD SUCCESSFUL
Total time: 6 seconds). If the build fails, please check our error Q&A below***



Error 1:

*** If the build fails due to the Java version used [Error message: "Failed to execute goal org.apache.maven.plugins:maven-compiler-plugin:3.1:compile (default-compile) on project Maven"] (NOTE:  this project needs the Java Developer Kit (JDK), not the Java Runtime Environment (JRE); For more information on why to use JDK iso JRE, see: https://stackoverflow.com/questions/1906445/what-is-the-difference-between-jdk-and-jre )) please take the following steps:

1. Go to Window → Preferences, and search for Java. One option is called "Installed JREs":

![image](https://user-images.githubusercontent.com/26277832/57802623-b0e1ed80-7756-11e9-969f-e70fb82a9e70.png)

2. Click on "Add…" , then “standard VM” and select the local folder where you have a locally installed JDK version of java. Rebuild the workspace (by clicking Apply in the preference menu again.) and try to rebuild DSMN with Maven (step 8 above).

3. If the above doesn’t work, add the JDK-HOME to your classpath (JAVA_HOME). This step is different comparing [Windows](https://stackoverflow.com/questions/2619584/how-to-set-java-home-on-windows-7) to [Linux](https://access.redhat.com/documentation/en-US/JBoss_Communications_Platform/5.0/html/Platform_Installation_Guide/sect-Configuring_Java.html) (and these steps can differ between versions of Windows or Linux). 

4. If you still get JAVA_HOME error messages in Eclipse, set the JAVA_HOME within [Eclipse](https://stackoverflow.com/questions/39827802/eclipse-java-home-not-set).

5. Update the project by right click on the project folder, select Maven/"Update Project..." and click OK.

![image](https://user-images.githubusercontent.com/26277832/63685685-513cb900-c800-11e9-8ea1-c0cfbdaab05c.png)

3. Return to step 9 of the main setup. 


Error 2:
*** Build failed due to unspecified Java version in pom.xml for Maven compiler

1. In the pom.xml file, one can specify the java version which has to be used to build the project.
Change the following lines:
```
      <plugin>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.8.1</version>
        <configuration>
          <source/>
          <target/>
        </configuration>
      </plugin>
```
to:
```
      <plugin>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.8.1</version>
        <configuration>
          <source>1.8</source>
          <target>1.8</target>
        </configuration>
      </plugin>
```
This allows building the project with Java Open JDK 1.8 .

1. Update the project by right click on the project folder, select Maven/"Update Project..." and click OK.

![image](https://user-images.githubusercontent.com/26277832/63685685-513cb900-c800-11e9-8ea1-c0cfbdaab05c.png)

3. Return to step 9 of the main setup. 


Error 3:

*** If the build fails due to the Maven compiler version please take the following steps:

1. Search which Maven versions are available [here](https://search.maven.org/) . Click on the corresponding name, and in the new window click on the number next to the "latest version" column (which shows all previous version). Update to the version Eclipse mentions in the error message (or a higher version if available).
You can make this update in the pom.xml file, under the field called "artifactId". Current Maven compile version: 3.8.1 .

1. Update the project by right click on the project folder, select Maven/"Update Project..." and click OK.

![image](https://user-images.githubusercontent.com/26277832/63685685-513cb900-c800-11e9-8ea1-c0cfbdaab05c.png)

3. Return to step 9 of the main setup. 

Error 4:

*** If the build fails due to the dependency version(s) please take the following steps (we will use the Neo4j driver as an example:

1. Search which versions are available [here](https://search.maven.org/), type "org.neo4j.driver . Click on the corresponding name (artifactID column), and in the new window click on the number next to the "latest version" column (which shows all previous version, with this example: (81) ). 

![image](https://user-images.githubusercontent.com/26277832/63694794-81438680-c817-11e9-94f9-594438fe5501.png)

Update to the version Eclipse mentions in the error message (or a higher version if available).
You can make this update in the pom.xml file, under the field called "version". Current Neo4j-driver version: 1.7.5 .

2. Update the project by right click on the project folder, select Maven/"Update Project..." and click OK.

![image](https://user-images.githubusercontent.com/26277832/63685685-513cb900-c800-11e9-8ea1-c0cfbdaab05c.png)

3. Return to step 9 of the main setup.

