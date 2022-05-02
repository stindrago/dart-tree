[dart-method]: https://stindrago.com/blog/#
[inkscape]: https://inkscape.org
[java]: https://www.java.com
[clojure]: https://clojure.org
[leiningen]: https://leiningen.org
[releases]: https://gitlab.com/stindrago/dart-cli/-/releases

# dart-cli

![](./resources/repo/demo.png)

## Introduction

**dart-cli** is command line tool to generate a project tree from a skeleton (template). The project tree is made of files and directories.

It is built as an utility software for the [D.A.R.T method][dart-method].

The idea is to help you stay organized by providing an easy way to start a project. Whenever you start a new project the structures is more or less the same. Instead of doing this manually you can generate the project tree with **dart-cli**.

## Goals

- [x] Generate projects from skeleton.
- [x] Display help.
- [x] List skeletons.
- [ ] Manage namespaces (work, private, alter-ego-1...).
- [ ] Manage tags.
- [ ] Automatic versioning.
- [ ] Skeleton marketplace.
- [ ] GUI.

## Prerequisites

- [Java][java] 11+
- [Clojure][clojure] 1.11+
- [Leiningen][leiningen] 2.9.8+

Linux

```shell
sudo apt install openjdk-11-jre-headless clojure leiningen
```

MacOS

```shell
brew install openjdk@11
brew install clojure
brew install leiningen
```

## Quickstart

Check [prerequisites](#Prerequisites) first.

``` shell
bash < <(curl -s https://gitlab.com/stindrago/dart-cli/-/raw/main/install.sh)
```

Then you are ready to run

```shell
dt new book my-awesome-book
```

## Example

Imagine you are a graphic designer and one of your friends asks you to create a logo for his new website. Before starting to work you think about the required materials: icons, fonts, images, notes, ecc. All these files you gather must be stored in an organized fashion somewhere.

The best thing to do is to start a new project called **the-awesome-logo**, structured like this.

``` text
the-awesome-logo
├── drawing.svg
├── README.md
└── assets
    ├── input
    └── renders
```

- In **drawing.svg** build the design with [Inkscape][inkscape].
- In **RADME.md** add details about the design: tasks, ideas, the delivery process, notes, ecc.
- In **assets/input** add the images you may use as a reference, the color palet or the fonts.
- In **assets/renders** add the logo exports as `.pdf` or `.png`.

## Customization

All skeletons are stored in `~/.config/dart-cli/skel`. You can create a custom skeleton by creating a new project similar to the [example](#example).
