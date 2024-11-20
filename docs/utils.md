# Utils

If, like me, you are using GitHub Codespaces instead of IntelliJ to interact with Java, but you don't know how or if it is possible to create a project without IntelliJ, brace yourself, my dear Bilbo, we are going on an adventure!

## Preparing extensions

First, you will need to install a bundle of extensions on your IDE, which is called [`Extension Pack for Java`](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack).

## Using the correct SDK

This project was created to run in `java-17`, and the default version of the space is `java-21`. Luckily, Codespaces comes with `sdkman` installed, which will allow us to install and configure the desired SDK version.

To check the installation of sdkman, run the following command:

```bash
sdk version
```

After that, you can check all available versions for Java by using the command:

```bash
sdk list java
```

Let's install the desired version via the command:

```bash
sdk install java 17-open
```

Just like nvm, we can have a "guidance" file in our project to indicate which version should be used. So let's create a file named `.sdkmanrc` which should have the following content:

```bash
java=17-open
```

With that created, let's ask sdkman to use the information in that file to set the Java version. You can do that by running the command:

```bash
sdk env install
```

That command will check if the version is already downloaded, and if not, it will download the SDK and set it as the default.

## Creating a Project

### Via Context Menu

If you want to create the project using a more user-friendly way, you can use the `context menu options` enabled by the extensions bundle you've already installed (Extension Pack for Java). To create a new project, follow these steps:

- Right-click wherever you want and select the option `New Java Project...`
- In the context menu that appears in the middle of your screen, select `Maven`
- Select the `archetype` for your project by searching and choosing `maven-archetype-quickstart`
- Choose the version—here, we will use version `1.0`, which will later appear in your `pom.xml` file as `1.0-SNAPSHOT`
- Enter the desired name for the `group id`
- Finally, specify the `artifact id`, which will also be used as the project name

Et voilà! Your Java project is created and ready to break some pipelines out there 😁😁

### Via Command Line

In this project, we are using `maven`. Since Maven is installed in this Codespace via the bundle, we can check the installed version by running the command:

```bash
mvn -v
```

If this runs and returns the version of maven installed in your space, we can proceed to create a test project (the good old `Hello World`). Run the following command:

```bash
mvn org.apache.maven.plugins:maven-archetype-plugin:3.1.2:generate -DarchetypeArtifactId="maven-archetype-quickstart" -DarchetypeGroupId="org.apache.maven.archetypes" -DarchetypeVersion="1.0" -DgroupId="<YOUR GROUP ID>" -DartifactId="<YOUR ARTIFACT ID>" -DinteractiveMode=false
```

Yep, it is correct, the values should be that close to the `-D` flag -- I also thought the command wouldn't run.

That will produce the following folder structure:

```bash
HelloWorldJava/
├── pom.xml
└── src/
    ├── main/
    │   └── java/
    │       └── org/example/App.java
    └── test/
        └── java/
            └── org/example/AppTest.java
```

## Executing the code

To execute the code, you will need to first compile it (remember, we are on the Java side of the moon here). You can do that by running the command:

```bash
mvn compile
```

And after that, you can run the compiled code via the command:

```bash
mvn exec:java -Dexec.mainClass="org.example.App"
```

## Maven Utils

Since we are using `maven`, if we want to deploy this application, we will need to generate a package. In maven, we can do that by running a command with the same name as the action. However, best practices mention that before doing that, you must run a clean command, so your final command will look like this:

```bash
mvn clean package
```
