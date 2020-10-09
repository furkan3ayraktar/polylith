(ns polylith.clj.core.help.info
     (:require [polylith.clj.core.help.shared :as s]
               [polylith.clj.core.help.shared :as shared]
               [polylith.clj.core.util.interface.color :as color]))

(defn help-text [cm]
  (str "  Shows workspace information.\n"
       "\n"
       "  poly info [" (s/key "ARGS" cm) "]\n"
       "    ARGS = " (s/key ":loc" cm) "       -> Shows the number of lines of code for each brick and\n"
       "                         environment.\n"
       "\n"
       "           since:" (s/key "WHEN" cm) " -> If set to " (s/key "last-stable" cm) " or if 'since' is not given, then\n"
       "                         the last stable point in time is calculated based on\n"
       "                         the latest git tag that follows the pattern 'stable-*',\n"
       "                         specified by " (s/key ":stable-tag-pattern" cm) " in ./deps.edn." "\n"
       "\n"
       "                         If set to " (s/key "previous-release" cm) " then it takes the second\n"
       "                         latest git tag that follows the pattern 'v[0-9]*',\n"
       "                         specified by " (s/key ":release-tag-pattern" cm) " in ./deps.edn.\n"
       "\n"
       "                         If no tag matched, it takes the first commit in the\n"
       "                         repository.\n"
       "\n"
       "  In addition to " (s/key ":loc" cm) ", all the arguments used by the 'test' command\n"
       "  can also be used as a way to see what tests will be executed.\n"
       "\n"
       "    stable since: " (color/grey cm "dec73ec | stable-lisa\n")
       "\n"
       "    " (color/environment "environments:" cm) " 2   " (color/interface "interfaces:" cm) " 3\n"
       "    " (color/base "bases:" cm) "        1   " (color/component "components:" cm) " 4\n"
       "\n"
       "    active profiles: " (color/profile "default" cm) "\n"
       "\n"
       "    environment   alias  source   " (color/profile "default  admin" cm) "\n"
       "    ---------------------------   --------------\n"
       "    " (color/environment "command-line  cl      ---       --      --" cm) "\n"
       "    " (color/environment "development   dev     x--       --      --" cm) "\n"
       "\n"
       "    interface  brick    " (color/environment "cl    dev  admin\n" cm)
       "    -----------------   ---   ----------\n"
       "    " (color/interface "payer" cm) "      " (color/component "payer" cm) "    " (color/environment "x--   xx-   --" cm) "\n"
       "    " (color/interface "user" cm) "       " (color/component "admin" cm) "    " (color/environment "x--   ---   xx" cm) "\n"
       "    " (color/interface "user" cm) "       " (color/component "user" cm) " *   " (color/environment "---   xx-   --" cm) "\n"
       "    " (color/interface "util" cm) "       " (color/component "util" cm) "     " (color/environment "x--   xx-   --" cm) "\n"
       "    -          " (color/base "cli" cm) "      " (color/environment "x--   xx-   --" cm) "\n"
       "\n"
       "  This example shows a sample project. Let's go through each section:\n"
       "\n"
       "  1. stable since: " (color/grey cm "dec73ec | stable-lisa\n")
       "\n"
       "     Shows the most recent commit marked as stable, or the previous build if \n"
       "     " (s/key "since:previous-release" cm) " was given, or the first commit in the repository\n"

       "     if no tag was found, followed by the tag (if found). More information\n"
       "     can be found in the 'diff' command help.\n"
       "\n"
       "  2. " (color/environment "environments:" cm) " 2   " (color/interface "interfaces:" cm) " 3\n"
       "     " (color/base "bases:" cm) "        1   " (color/component "components:" cm) " 4\n"
       "\n"
       "     Shows how many " (color/environment "environments" cm) ", " (color/base "bases" cm) ", " (color/component "components" cm) " and " (color/interface "interfaces" cm) " there are\n"
       "     in the workspace.\n"
       "\n"
       "  3. active profiles: " (color/profile "default" cm) "\n"
       "\n"
       "     Shows the names of active profiles. The profile paths are merged into the\n"
       "     " (color/environment "development" cm) " environment. A profiles is an aliase in ./deps.edn that starts\n"
       "     with a " (color/purple cm "+") ". If no profile is selected, the " (color/profile "default" cm) " profile is automatically\n"
       "     selected.\n"
       "\n"
       "     Profiles are activated by passing them in by name (prefixed with '+'), e.g.:\n"
       "       poly info +admin +onemore\n"
       "\n"
       "     To deactivate all the profiles, and stop 'default' from being merged into\n"
       "     the development environment, type:\n"
       "       poly info +\n"
       "\n"
       "  4. environment   alias  source   " (color/profile "default  admin" cm) "\n"
       "     ---------------------------   --------------\n"
       "     " (color/environment "command-line  cl      ---       --      --" cm) "\n"
       "     " (color/environment "development   dev     x--       --      --" cm) "\n"
       "\n"
       "    This table lists all environments. The 'environment' column shows the name\n"
       "    of the environments, which are the directory names under the 'environments',\n"
       "    directory except for 'development' that stores its code under the\n"
       "    'development' directory.\n"
       "\n"
       "    The 'deps.edn' config files are stored under each environment, except for\n"
       "    the development enviroment that stores it at the workspace root.\n"
       "\n"
       "    Aliases are configured in " (color/purple cm ":env-to-alias") " in ./deps.edn.\n"
       "\n"
       "    The 'source' column has three x/- flags with different meaning:\n"
       "      x--  The environment has a 'src' directory, e.g.\n"
       "           'environments/command-line/src'.\n"
       "      -x-  The environment has a 'test' directory, e.g.\n"
       "           'environments/command-line/test'\n."
       "      --x  The environment tests (its own) are marked for execution.\n"
       "\n"
       "    To show the 'resources' directory, also pass in :r or :resources, e.g.\n"
       "    'poly info :r':"
       "      x---  The environment has a 'src' directory, e.g.\n"
       "            'environments/command-line/src'.\n"
       "      -x--  The environment has a 'resources' directory, e.g.\n"
       "            'environments/command-line/resources'.\n"
       "      --x-  The environment has a 'test' directory, e.g.\n"
       "            'environments/command-line/test'\n"
       "      ---x  The environment tests (its own) are marked for execution.\n"
       "\n"
       "    The last two columns, " (s/key "default admin" cm) ", are the profiles:\n"
       "      x-  The profile contains a path to the 'src' directory, e.g.\n"
       "          'environments/command-line/src'.\n"
       "      -x  The profile contains a path to the 'test' directory, e.g.\n"
       "          'environments/command-line/test'\n."
       "\n"
       "    If also passing in :r or :resources, e.g. 'poly info +r':\n"
       "      x--  The profile contains a path to the 'src' directory, e.g.\n"
       "           'environments/command-line/src'.\n"
       "      -x-  The profile contains a path to the 'resources' directory, e.g.\n"
       "           'environments/command-line/resources'.\n"
       "      --x  The profile contains a path to the 'test' directory, e.g.\n"
       "           'environments/command-line/test'.\n"
       "\n"
       "  5. interface  brick    " (color/environment "cl    dev  admin\n" cm)
       "     -----------------   ---   ----------\n"
       "     " (color/interface "payer" cm) "      " (color/component "payer" cm) "    " (color/environment "x--   xx-   --" cm) "\n"
       "     " (color/interface "user" cm) "       " (color/component "admin" cm) "    " (color/environment "x--   ---   xx" cm) "\n"
       "     " (color/interface "user" cm) "       " (color/component "user" cm) " *   " (color/environment "---   xx-   --" cm) "\n"
       "     " (color/interface "util" cm) "       " (color/component "util" cm) "     " (color/environment "x--   xx-   --" cm) "\n"
       "     -          " (color/base "cli" cm) "      " (color/environment "x--   xx-   --" cm) "\n"
       "\n"
       "    This table lists all bricks and in which environments and profiles they are\n"
       "    added to.\n"
       "\n"
       "    The 'interface' column shows what " (color/interface "interface" cm) " the component has. The name\n"
       "    is the first namespace after the component name, e.g.:\n"
       "    " (shared/component-ns "interface" "user" cm) "\n"
       "\n"
       "    The 'brick' column shows the name of the brick. In " (color/component "green" cm) " if a component or\n"
       "    " (color/base "blue" cm) " if a base. Each component lives in a directory under the 'components'\n"
       "    directory and each base lives under the 'bases' directory. If any file for\n"
       "    a brick has changed since the last stable point in time, it will be marked\n"
       "    with an asterisk, * (" (color/component "user" cm) " in this example).\n"
       "\n"
       "    The changed files can be listed by executing 'poly diff'.\n"
       "\n"
       "    The next " (color/environment "cl" cm) " column is the " (color/environment "command-line" cm) " environment that lives under the\n"
       "    'environments' directory. Each line in this column says whether a brick is\n"
       "    included in the environment or not."
       "\n"
       "    The flags mean:\n"
       "      x--  The environment contains a path to the 'src' directory, e.g.\n"
       "           'components/user/src'.\n"
       "      -x-  The environment contains a path to the 'test' directory, e.g.\n"
       "           'components/user/test'.\n"
       "      --x  The brick is marked to be executed from this environment.\n"
       "\n"
       "    If :r or :resources is also passed in:\n"
       "      x---  The environment contains a path to the 'src' directory, e.g. \n"
       "            'components/user/src'.\n"
       "      -x--  The environment contains a path to the 'resources' directory, e.g.\n"
       "            'components/user/resources'.\n"
       "      --x-  The environment contains a path to the 'test' directory, e.g.\n"
       "            'components/user/test'\n."
       "      ---x  The brick is marked to be executed from this environment.\n"
       "\n"
       "    The next group of columns, " (color/environment "dev admin" cm) ", is the development environment with\n"
       "    its profiles. If passing in a plus with 'poly info +' then it will also show\n"
       "    the " (color/profile "default" cm) " profile. The flags for the " (color/environment "dev" cm) " environment works the same\n"
       "    as for " (color/environment "cl" cm) ".\n"
       "\n"
       "    The flags for the " (color/profile "admin" cm) " profile means:\n"
       "      x-  The profile contains a path to the 'src' directory, e.g.\n"
       "          'components/user/src'.\n"
       "      -x  The profile contains a path to the 'test' directory, e.g.\n"
       "          'components/user/test'\n"
       "\n"
       "    If :r or :resources is also passed in:\n"
       "      x--  The profile contains a path to the 'src' directory, e.g.\n"
       "           'components/user/src'.\n"
       "      -x-  The profile contains a path to the 'resources' directory, e.g.\n"
       "           'components/user/resources'.\n"
       "      --x  The profile contains a path to the 'test' directory, e.g.\n"
       "           'components/user/test'.\n"
       "\n"
       "  It's not enough that a path has been added to an environment to show an 'x',\n"
       "  the file or directory must also exist.\n"
       "\n"
       "  If any warnings or errors was found in the workspace, they will be listed at\n"
       "  the end, see the 'check' command help, for a complete list of validations.\n"
       "\n"
       "  Example:\n"
       "    poly info\n"
       "    poly info :loc\n"
       "    poly info since:release\n"
       "    poly info since:previous-release\n"
       "    poly info env:myenv\n"
       "    poly info env:myenv:another-env\n"
       "    poly info :env\n"
       "    poly info :dev\n"
       "    poly info :env :dev\n"
       "    poly info :all\n"
       "    poly info :all-bricks\n"
       "    poly info ws-dir:another-ws\n"
       "    poly info ws-file:ws.edn\n"))

(defn print-help [color-mode]
  (println (help-text color-mode)))
