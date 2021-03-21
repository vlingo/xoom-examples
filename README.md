# vlingo-examples

[![Gitter chat](https://badges.gitter.im/gitterHQ/gitter.png)](https://gitter.im/vlingo-platform-java/examples)
[![Build](https://github.com/vlingo/vlingo-examples/workflows/Build/badge.svg)](https://github.com/vlingo/vlingo-examples/actions?query=workflow%3ABuild)

The VLINGO XOOM examples demonstrating features and functionality available in the reactive components. See each of the submodules for specific examples.

Docs: https://docs.vlingo.io

## Building snapshots

To build examples from this repository you'll need access to VLINGO XOOM snapshot builds on
[GitHub Packages](https://github.com/vlingo/vlingo-platform/packages).

GitHub [requires authentication with a Personal Access Token](https://docs.github.com/en/packages/guides/configuring-apache-maven-for-use-with-github-packages#authenticating-with-a-personal-access-token)
to use their Maven repository.
In order to build VLINGO XOOM examples locally, you will need to configure the following in your `~/.m2/settings.xml`:

```xml
<settings>
  <servers>
    <server>
      <id>github</id>
      <username>GITHUB-USERNAME</username>
      <password>GITHUB-PERSONAL-ACCESS-TOKEN</password>
    </server>
  </servers>
</settings>
```

Replace `GITHUB-USERNAME` with your GitHub username, and `GITHUB-PERSONAL-ACCESS-TOKEN` with your Personal Access Token.
Personal Access Tokens can be created in Settings > Developer Settings > [Personal Access Tokens](https://github.com/settings/tokens) on GitHub.
Remember to create the token with `read:packages` scope.
