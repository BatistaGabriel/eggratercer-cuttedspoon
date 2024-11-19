# Troubleshooting

## Maven

### SDK version

If you have any sdk version erros, check your `pom.xml` file, if the `properties` sections is missing maybe adding the following might help maven to use the correct sdk version:

```maven
  <properties>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
  </properties>
```
