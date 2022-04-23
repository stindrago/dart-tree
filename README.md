[dart-method]: https://stindrago.com/blog/#
[inkscape]: https://inkscape.org
[babashka-install]: https://github.com/babashka/babashka#installation

# dart-cli

A command line tool for the [D.A.R.T method][dart-method] to generate a project tree from a skeleton.

When you create a new project and want to be organized usually the initial directory structures is more or less the same. Have you thought about using a template that generates all those directories and files you need to start working on the project without manually creating them? **dart-cli** is the right tool for you.

## Use Case

For example if I start a new graphic design project called **my-awesome-logo**, to stay organized I need a project structure to start with. 

``` text
my-awesome-logo
├── drawing.svg
├── README.md
└── assets
    ├── input
    └── renders
```

- In **drawing.svg** is where I build the design with [Inkscape][inkscape].
- In **RADME.md** is where I add details about my design: what I have to build, what are the needs of the client, notes, ecc.
- In **assets/input** I put the images that I use as reference or the fonts I want to use.
- In **assets/renders** I put the bitmap files, for example a PDF draft I want to send to my client for some feedback.

This is the project tree I need to start a new design project.

## Install

Install Java and [babashka][babashka-install], the Clojure compiler.

Clone the repo.

``` shell
git clone https://github.com/stindrago/dart-cli ~/Desktop/dart-cli
```


## Demo

``` shell
cd ~/Desktop/dart-cli
bb -m main.core new graphic-design my-awesome-logo
```

- **bb -m main.core** is the program call. Soon will be packaged as **dart-cli**.
- **new** is the command that invoke a creation of a new project tree.
- **graphic-design** is the name of the skeleton (template). Look in the `skel/` directory to find more project skeletons.
- **my-awesome-logo** is the name of your project. All the files will be placed inside a directory by the same name.

See `bb -m main.core --help` for details.
