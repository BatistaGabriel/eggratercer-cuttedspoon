# Setup

## Adjusting Your Editor

_All the configurations mentioned here were performed using `vscode` as the editor._

First of all, make sure to install editor plugins capable of interacting with the `.editorconfig` and `.prettierignore` files. If you are using `vscode`, you can search for `editorconfig` and `prettier`.

After installing those plugins, configure your editor by going to the `Settings` page and following these steps:

- Search for `formatter` and set the default formatter to `Prettier`.
- Search for `format on save` and enable the option to format the file on save.
- Search for `auto save` and turn this option `off`.

## Commit Standardization

This project uses [`commitizen`](https://github.com/commitizen/cz-cli) and [`husky`](https://github.com/typicode/husky) to take advantage of using [`conventional commits`](https://www.conventionalcommits.org/en/v1.0.0/) to enhance the experience around commit messages. Although both tools are more commonly used in the JS ecosystem, we can leverage their benefits here as well.

As expected, you will need `node` and `npm` installed if you would like to follow this section.

## Commitizen

### Installation

Once `node` and `npm` are installed, you can proceed to install `commitizen`. To do so, run the following command:

```bash
npm install -g commitizen
```

### Initialization

With commitizen globally installed, it is time to initialize in the project's folder. You can do that by running the following command:

```bash
commitizen init cz-conventional-changelog --save-dev --save-exact
```

### How to Commit

After all is done, anytime you want to commit something, you will need to use the following command to ensure you follow the expected `conventional commits` standards:

```bash
git cz
```

## Husky

We are going to use [`Husky`](https://github.com/typicode/husky) to lint commits using the [`git hook`](https://www.atlassian.com/git/tutorials/git-hooks) called `commit-msg`. This will ensure that only commits that respect the `conventional commits` are going to be pushed to the repo.

### Installation

As we did before, we can install husky by using npm. This time, since husky is a local dependency to manage git hooks, there is no need to install it globally, so our installation command will look like this:

```bash
npm install husky --save-dev
```

### Configuration

With husky installed, it is now time to proceed with configuring it. This command will generate the folder where all the git hook files should be for husky to be able to manage them. So you must run the following command:

```bash
npx husky init
```

Moving forward, it is time to install [`commitlint`](https://github.com/conventional-changelog/commitlint) to be used as our linter by husky when the hook is called. Run the following command:

```bash
npm install @commitlint/{config-conventional,cli} --save-dev
```

Now we will create a file named `.commitlintrc.js` that will be responsible for the necessary `commitlint` configuration. The content of this file looks like this:

```js
module.exports = { extends: ["@commitlint/config-conventional"] };
```

### Adding commit-msg hook

Finally, you can add the hook by running the following command:

```bash
printf '#!/usr/bin/env sh\nnpx --no-install commitlint --edit "$1"' > .husky/commit-msg
```
