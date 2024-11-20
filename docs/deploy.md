# Deploy

To deploy our Lambda function to AWS, we first need to generate a `jar` file.

This file will be uploaded to AWS and will run under the hood of our function.

To generate the `jar` file, follow these steps:

- Ensure you are in the same directory as your `pom.xml` file.
- Run the following command:

```bash
mvn validate clean package
```

In this case, the `validate` command is used to trigger the `prettier-maven-plugin`. However, the other two commands—`clean` and `package`—are the most important.

The `package` command is especially critical, as it is responsible for generating the `jar` file.
