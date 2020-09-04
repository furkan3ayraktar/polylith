# <img src="logo.png" width="50%" alt="Polylith" id="logo">
A tool used to develop Polylith based architectures.

---------

Welcome to the wonderful world of Polylith!

This implementation of Polylith works with [Clojure](https://clojure.org), which is a powerful functional language for the JVM.

Polylith is a new way of thinking around system architecture, 
that puts the developer in the driving seat and the code in the center.

Polylith is a way of organising code into reusable building blocks that are used to create systems. 
To better understand the principles and ideas behind it, we recommend you first read the Polylith 
[documentation](https://polylith.gitbook.io).

Organising code as a Polylith can be done manually, which was actually how it all began. With that said, 
there is no magic behind this way of organising the code. It's not a framework nor a library, 
just a simple yet powerful way to work with code at the system level.

The reason we built this tool is to make life easier for you as a developer by making the work more 
efficient and fun. 

Enjoy the ride!

## Migrate

The old lein-polylith tool has reached its end of lifetime. If you have any old Leiningen based projects 
that need to be migrated, follow the instructions [here](https://github.com/tengstrand/lein-polylith/blob/migrate/migrate/migrate.md)
and read about the reasons why you should migrate.

## Table of Contents

- [Installation](#installation)

## Installation

To use the Polylith tool and to get access all the features in tools.deps, follow these steps:
- Make sure [git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git) is installed.
- Install the [clj](https://clojure.org/guides/getting_started) command line tool.

Make sure that `user.email` and `user.name` is configured correctly in git, by e.g. executing:
```sh
git config --list | grep user
```
If not, check it [here](https://docs.github.com/en/github/using-git/setting-your-username-in-git).

*** verify the installation ***

The next thing we want to do is to download and install the `poly` command line tool.

*** instructions on how to install 'poly' in unix/linux and windows + verify ***

## Realworld Example

If you want to have a look at a full-blown system, go to the [RealWorld](https://github.com/furkan3ayraktar/clojure-polylith-realworld-example-app) project where you can compare it with [implementations made in other languages](https://github.com/gothinkster/realworld).

## Workspace

The workspace directory is the top-level container for all your code and everything you need to create Polylith systems.

Let’s start by creating the _example_ workspace with the top namespace _se.example_:
```sh
poly create w name:example top-ns:se.example
```

The workspace directory structure will end up like this:
```sh
example           # workspace root dir
  bases           # empty dir
  components      # empty dir
  development
    src
  environments    # empty dir
  deps.edn        # the workspace config file (tools.deps)
  logo.png        # the polylith logo
  readme.md       # documentation
```

This directory structure helps us work with, and reason about the code. 
Each top directory is responsible for its own part of a Polylith system.
A `base` exposes a public API. A `component` is responsible for a specific domain 
or part of the system. 
An `environment` specifies our deployable artifacts and what components and bases they contain.
Finally, the `development` environment is where we keep code that is part of our workflow 
but not part of any environment.

Most of the workspace settings are stored in `deps.edn`:

```clojure
{:polylith {:vcs "git"
            :top-namespace "se.example"
            :interface-ns "interface"
            :default-profile-name "default"
            :stable-since-tag-pattern "stable-*"
            :env->alias {"development" "dev"}
            :ns->lib {}}

 :aliases  {:dev {:extra-paths ["development/src"]
                  :extra-deps {org.clojure/clojure {:mvn/version "1.10.1"}
                               org.clojure/tools.deps.alpha {:mvn/version "0.8.695"}}}

            :test {:extra-paths []}

            :poly {:main-opts ["-m" "polylith.clj.core.poly_cli.poly"]
                   :extra-deps {tengstrand/polylith
                                {:git/url   "https://github.com/tengstrand/polylith.git"
                                 :sha       "69e70df8882f4d9a701ab99681a4a6870bdf052b"
                                 :deps/root "environments/cli"}}}}}
```

We will later cover what all the different settings mean and how to use them.

If you are new to tools.deps then it could be a good idea to 
[read about](https://github.com/clojure/tools.deps.alpha) the ideas behind the tool and how things 
like _aliases_ are used. If you are already familiar with tools.deps, then you know that aliases 
are used to specify what source code and libraries that should be included in an environment.

## Development

When working with a Polylith codebase, we are free to choose any editor/IDE we want, for example
Emacs/[Cider](https://github.com/clojure-emacs/cider), 
VSCode/[Calva](https://marketplace.visualstudio.com/items?itemName=betterthantomorrow.calva) or
IDEA/[Cursive](https://cursive-ide.com). Here we will use the latter.

Let's get started by creating a project. From the menu, select `File > New > Project from existing source`.
Select the `deps.edn` file, the desired version of SDK and finish the wizard.

Make sure to activate the `:dev` alias (and press the "two arrows" icon to refresh):<br>
<img src="images/dev-alias.png" width="30%" alt="Dev alias">

Let's create a REPL by clicking `Add Configuration`:<br>
<img src="images/add-configuration.png" width="20%" alt="Add configuration">

Click the `+` sign and select `Clojure REPL > Local`:<br>
<img src="images/new-local-repl.png" width="30%" alt="New local REPL">

Fill in:
- Name: REPL
- Which type of REPL to run: nREPL
- Run with Deps: (select)
- Aliases: test,dev

Now start the REPL in debug mode, by clicking the bug icon:<br>
<img src="images/repl-start.png" width="20%" alt="New local REPL">

When this turns up:
```
nREPL server started on port 53536 on host localhost - nrepl://localhost:53536
Clojure 1.10.1
```
...we are ready to go!

If we look at the `deps.edn` file again, we can see that _"development/src"_ already was added to the path:
```
 :aliases  {:dev {:extra-paths ["development/src"]
```

When we talk about the `development` environment, we mean the `src`, `test` and `resources` folders
that are added to the `:dev` alias.
Right now only the "development/src" directory is there, but every time we create a new component or base,
they will most likely be added here so that we can work with them from the `development` environment.


The "development/src" path belongs to the `dev` alias which we activated previously and also added to the REPL
by selecting the aliases "dev,test".
This means that we have configured everything that [tools.deps](https://github.com/clojure/tools.deps.alpha)
needs and that we are ready to write some Clojure code!

To do that we first need to create a namespace. We suggest that you use `dev` as a top namespace here and not 
the workspace top namespace (e.g. `se.example`).
The reason is that we don't want to mix the code we put here with production code.

One way of structuring the code here is to give every developer their own namespace under the `dev` top namespace.
Let's follow that pattern and create the namespace `dev.lisa`.

Right click on the `development/src` folder and select `New > Clojure Namespace` and type "dev.lisa":<br>
<img src="images/new-namespace.png" width="30%" alt="New local REPL">

Now let's write some code:
```clojure
(ns dev.lisa)

(+ 1 2 3)
```
If we send `(+ 1 2 3)` to the REPL we should get `6` back, and if we do,
it means that we now have a working development environment!

# Component

Now when we have a working development environment, let's continue by creating our first component:
```sh
cd example
poly create c name:user
```

<img src="images/component.png" width="20%" alt="Component">

Our workspace will now look like this:
```sh
example
  bases
  components
    user
      resources
        user
      src
        se/example/user/interface.clj
      test
        se/example/user/interface_test.clj
  development
    src
      dev/lisa.clj
  environments
  deps.edn
  logo.png
  readme.md
```

The command also printed out this message:
```
  Remember to add source directories (src/resources/test) to 'deps.edn' so they 
  will be in the environments and development profiles that they belong to.
```

This was a reminder to add resource directories to `deps.edn` so that we can work with the code in the REPL.
If we don't, then tools.deps and the development environment will not recognise our newly created component.

Let's add the component's `src`, `resources` and `test` folder to `deps.edn`:
```clojure
 :aliases  {:dev {:extra-paths ["development/src"
                                "components/user/src"
                                "components/user/resources"]
  ...
            :test {:extra-paths ["components/user/test"]}
```

We may need to refresh our IDE, by e.g. clicking this link (or the icon we used before):<br>
<img src="images/refresh-ws.png" width="40%" alt="New local REPL">

Now execute the `info` command:<br>
```sh
poly info
```
<img src="images/info-01.png" width="30%" alt="New local REPL">

This informs us that we now have one environment (`development`), one component (`user`) and
one interface (`user`) and some other information that we will explain later.

If you need to adjust the colors, then visit the ****color section*****.

Let's add the `core` namespace to `user`:<br>
<img src="images/ide-ws-01.png" width="30%" alt="New local REPL">

And change it to:
```clojure
(ns se.example.user.core)

(defn hello [name]
  (str "Hello " name "!"))
```

And update the `interface` to:
```clojure
(ns se.example.user.interface
  (:require [se.example.user.core :as core]))

(defn hello [name]
  (core/hello name))
``` 
Here we delegate the incoming call to the implementing `core` namespace,
which is the recommended way of structuring the code within a `component` by delegating from the `interface`
namespace to implementing namespaces. But something is missing. A test!

Let's edit `interface-test`:
```clojure
(ns se.example.user.interface-test
  (:require [clojure.test :refer :all]
            [se.example.user.interface :as user]))

(deftest hello--when-called-with-a-name--then-return-hello-phrase
  (is (= "Hello Lisa!"
         (user/hello "Lisa"))))
```

The test is green and we have created our first component!

## Interface

When we created the `user` component it also created the `user` interface.

<img src="images/component-interface.png" width="20%" alt="Interface">

The interface is illustrated by the light green part in the picture.
So what is an interface and what is it good for?

An interface in the Polylith world is a namespace named `interface` that often lives in one but sometimes several
components. It defines a number of `def`, `defn` and `defmacro` statements which forms the contract that 
it exposes to other components and bases.

If more than one component uses the same interface, then all these components must define the exact same set of 
`def`, `defn` and `defmacro` definitions, which the tool will help us with (more on that later).

It's true that the default name of an `interface` is "interface", but this can be changed by editing `:interface-ns` 
in `deps.edn` to something else (if you have really good reasons to do so).

To have just a single interface namespace in a component is often what we want, but it is also possible to 
divide the interface into several namespaces.
To do that we first create an `interface` package (directory) with the name `interface` at the root
and then we put the sub namaspaces in there.

You can find an example where the Polylith tool itself does that, by dividing its 
[util](https://github.com/tengstrand/polylith/tree/core/components/util/src/polylith/clj/core/util/interfc)
interface into several sub namespaces:
```sh
util
  interface
    color.clj
    exception.clj
    os.clj
    str.clj
    time.clj
```

This can be handy if you want to group the functions and not put everyone in one place.
It can also be a signal that it's time to split up the component into several components!

Code that uses an interface like this can look something like this:
```clojure
(ns dev.lisa
  (:require [se.example.util.interface.time :as time]))

(time/current-time)
```

There are several reasons why we have interfaces:
- Single point of access. Components can only be accessed through their interface, which makes them easier to use and reason about.
- Encapsulation. All the implementing namespaces can be changed without breaking the contract.
- Replacability. A component can be replaced with another component as long as they use the same interface.

## Base

A `base` is similar to a `component` except for two things:
- It doesn't have an `interface`.
- It exposes a public API to the outside world.

<img src="images/base.png" width="30%" alt="Base">

The lack of an `interface` makes bases less composable compared to components. But that is not a big problem,
because they solve another problem and that is to receive input from the outside world and to delegate 
that input to different components (or actually interfaces, because components and bases only knows about interfaces).

Let's create the `cli` base to see how it works:
```sh
poly create b name:cli
```

Our workspace should now look like this:
```sh
example
  bases
    cli
      resources
        cli
      src
        se/example/cli/api.clj
      test
        se/example/cli/api-test.clj
  components
    user
      resources
        user
      src
        se/example/user/interface.clj
      test
        se/example/user/interface_test.clj
  development
    src
      dev/lisa.clj
  environments
  deps.edn
  logo.png
  readme.md
```

We should not forget to update `deps.edn` with our newly created base:
```clojure
 :aliases  {:dev {:extra-paths ["development/src"
                                "components/user/src"
                                "components/user/resources"
                                "bases/cli/src"
                                "bases/cli/resources"]
                  :extra-deps {org.clojure/clojure {:mvn/version "1.10.1"}
                               org.clojure/tools.deps.alpha {:mvn/version "0.8.695"}}}

            :test {:extra-paths ["components/user/test"
                                 "bases/cli/test"]}
```

Now, let's add some code to the `api` namespace:
```clojure
(ns se.example.cli.api
  (:require [se.example.user.interface :as user])
  (:gen-class))

(defn -main [& args]
  (user/hello (first args)))
```

Now we want to build an artifact that we can use as a command line tool. To do that, we need to create an environment.

## Environment

There are two kinds of environments:
1. The `development` environment is used to work with the code, often by using a REPL. 
   All paths that are included in the environment are specified in `deps.edn`, like `development/src`
   and paths to the components and bases we want to work with.
2. Other environments are used to build deployable artifacts like libraries, lambda functions, REST APIs and command line tools.
   Each environment has its own directory under `environments` with a `deps.edn` file,
   that specifies all the paths to the components and bases that we want to include. 
   If it has any tests of its own, it will also have a `test` directory. 
   Optionally it could also have a `src` and `resources` directory.

Let's create an environment:
```sh
poly create e name:command-line
```
 
Our workspace should now look like this:
```sh
example
  bases
    cli
      resources
        cli
      src
        se/example/cli/api.clj
      test
        se/example/cli/api-test.clj
  components
    user
      resources
        user
      src
        se/example/user/interface.clj
      test
        se/example/user/interface_test.clj
  development
    src
      dev/lisa.clj
  environments
    command-line
      deps.edn
  deps.edn
  logo.png
  readme.md
```
 
The command also printed out this:
```sh
  It's recommended to add an alias to :env->alias in deps.edn for the command-line environment.
```

So let's do that:
```clojure
{:polylith {:vcs "git"
            ...
            :env->alias {"development" "dev"
                         "command-line" "cl"}
```

Now add `user` and `cli` to `deps.edn` in `environments/command-line`:
```clojure
{:paths ["../../components/user/src"
         "../../components/user/resources"
         "../../bases/cli/src"
         "../../bases/cli/resources"]

 :deps {org.clojure/clojure {:mvn/version "1.10.1"}
        org.clojure/tools.deps.alpha {:mvn/version "0.8.695"}}

 :aliases {:test {:extra-paths ["../../components/user/test"
                                "../../bases/cli/test"]
                  :extra-deps  {}}}}
```

Note three things here:
- We didn't add "development/src. 
- The src paths (`:paths`) and the test paths (`extra-paths`) are configured at different levels.
- All paths begin with "../../".

The reson we didn't add "development/src" is because it contains code that should only be used
from the development environment.

The reson this environment is configured differently compared to the development environment is that
it is more static in its nature with less need to switch between different configurations (aliases)
and all the paths will be included by default without the need to specify an alias.

And finally, the reason all paths begin with "../../" is that `components` and `bases` live two levels up 
compared to `environments/command-line` and not at the root as with the `development` environment.

### Colours

To make things more colourful create the `~/.polylith/config.edn` config file under your `USER-HOME` directory
with the following content:
```
{:color-mode "bright"
 :thousand-separator ","
 :empty-character "·"}
```
- The _color-mode_ can be set to either "none", "light" or "dark", depending on the colour schema you use.
  The only difference between "light" and "dark" is that they use different [codes](https://github.com/tengstrand/polylith/blob/core/components/util/src/polylith/clj/core/util/colorizer.clj) for grey.
- The _thousand-spearator_ is used to separate numbers larger then 999 like 12,345.
- The _empty-character_ can be replaced by a . (period) if your computer has problems showing it (they are used in the `deps` command).

If we run the `info` command again:
```sh
clj -A:poly info
```
<img src="images/polylith-info-bright.png" width="40%" alt="Polylith workspace">

The diagram is now shown with colours! Let's improve the readability by switching to dark mode:

```
{:color-mode "dark"
 :thousand-separator ","
 :empty-character "·"}
```
<img src="images/polylith-info.png" width="40%" alt="Polylith workspace">

That's better! 

If you want to use the same colours in your terminal, here they are:<br>
<img src="images/polylith-colors.png" width="50%" alt="Polylith colors">

If the colours (f8eeb6, bfefc5, 77bcfc, e2aeff, cccccc, 24272b, ee9b9a) looks familiar to you, it's because they are 
more or less stolen from the [Borealis](https://github.com/Misophistful/borealis-cursive-theme) colour schema!
