<!-- Copyright (c) 2025 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: BSD-3-Clause -->
<!-- This file is part of https://github.com/texsydo/dorep-jekyll -->

# DoRep Jekyll

[![Project](https://mathswe-ops-services.tobiasbriones-dev.workers.dev/badge/project/texsydo)](https://tsd.math.software#dorep-jekyll)
&nbsp;
[![GitHub Repository](https://img.shields.io/static/v1?label=GITHUB&message=REPOSITORY&labelColor=555&color=0277bd&style=for-the-badge&logo=GITHUB)](https://github.com/texsydo/dorep-jekyll)

[![GitHub Project License](https://img.shields.io/github/license/texsydo/dorep-jekyll.svg?style=flat-square)](https://github.com/texsydo/dorep-jekyll/blob/main/LICENSE)

[![GitHub Release](https://mathswe-ops-services.tobiasbriones-dev.workers.dev/badge/version/github/texsydo/dorep-jekyll)](https://github.com/texsydo/dorep-jekyll/releases/latest)

Representing a textual system document as a static website generated with
Jekyll.

## Generating a Jekyll Static Website

Usage: `gradlew run --args="build {src} {dst}"`.

The program doesn't require installation or deployment, so it's just enough to
run it directly via `gradlew` since it's meant to be a script rather than an
app.

### Preferred Deployment Approach

For a more formal execution of the Kotlin script, build the distribution 
with Gradle and run the binary from the `build` directory.

- `./gradlew installDist`
- `cd build/install/dorep-for-jekyll/bin`
- `./dorep-for-jekyll build {src} {dst}`

### Testing Settings

For testing the app, use the dirs [test/src](test/src) and [test/dst](test/dst).

Usage: `./gradlew run --args="build test/src test/dst"`.

## Contact

Tobias Briones: [GitHub](https://github.com/tobiasbriones)
[LinkedIn](https://linkedin.com/in/tobiasbriones)

## About

**DoRep Jekyll**

Representing a textual system document as a static website generated with
Jekyll.

Copyright © 2023–2025 Tobias Briones. All rights reserved.

### License

This project is licensed under the
[BSD-3-Clause License](LICENSE).
