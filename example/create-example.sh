#!/usr/bin/env bash

# brew install tree

SECONDS=0

set -e
rm -rf example
rm -rf scripts

echo "### 1/13 Workspace ###"
poly create w name:example top-ns:se.example
tree example > output/workspace-tree.txt
cp example/deps.edn sections/workspace
cd example

echo "### 2/13 Development ###"
mkdir development/src/dev
cp ../sections/development/lisa.clj development/src/dev
git add development/src/dev/lisa.clj

echo "### 3/13 Component ###"
poly create c name:user
cp ../sections/component/user-core.clj components/user/src/se/example/user/core.clj
git add components/user/src/se/example/user/core.clj
cp ../sections/component/user-interface.clj components/user/src/se/example/user/interface.clj
cp ../sections/component/deps.edn .
poly info fake-sha:c91fdad > ../output/component-info.txt
cd ..
tree example > output/component-tree.txt
cd example

echo "### 4/13 Base ###"
poly create b name:cli
cd ..
tree example > output/base-tree.txt
cd example
cp ../sections/base/deps.edn .
cp ../sections/base/cli-core.clj bases/cli/src/se/example/cli/core.clj

echo "### 5/13 Project ###"
poly create p name:command-line
cd ..
tree example > output/project-tree.txt
cd example
cp ../sections/project/deps.edn .
cp ../sections/project/command-line-deps.edn projects/command-line/deps.edn

echo "### 6/13 Tools.deps ###"
cd projects/command-line
mkdir -p classes
clj -e "(compile,'se.example.cli.core)"

echo "### 7/13 Build ###"
cd ../..
mkdir scripts
cp ../../scripts/build-uberjar.sh scripts
cp ../sections/build/build-cli-uberjar.sh scripts
chmod +x scripts/build-uberjar.sh
chmod +x scripts/build-cli-uberjar.sh
git add scripts/build-uberjar.sh
git add scripts/build-cli-uberjar.sh
cp ../sections/build/command-line-deps.edn projects/command-line/deps.edn
cd scripts
./build-cli-uberjar.sh
cd ../projects/command-line/target
java -jar command-line.jar Lisa

echo "### 8/13 Git ###"
cd ../../..
poly info fake-sha:c91fdad > ../output/git-info.txt
git log > ../output/git-log.txt
poly diff > ../output/git-diff.txt
git add --all
git commit -m "Created the user and cli bricks."
git log --pretty=oneline > ../output/git-log-pretty.txt

echo "### 9/13 Tagging ###"
git tag -f stable-lisa
git log --pretty=oneline > ../output/tagging-log.txt
poly info fake-sha:e7ebe68 > ../output/tagging-info-1.txt
firstsha=`git log --pretty=oneline | tail -1 | cut -d " " -f1`
git tag v1.1.0 $firstsha
git tag v1.2.0
poly info since:release fake-sha:e7ebe68 > ../output/tagging-info-2.txt
poly info since:previous-release fake-sha:c91fdad > ../output/tagging-info-3.txt
git log --pretty=oneline > ../output/tagging-log-release.txt

echo "### 10/13 Flags ###"
poly info :r fake-sha:e7ebe68 > ../output/flags-info.txt

echo "### 11/13 Testing ###"
cp ../sections/testing/user-core.clj components/user/src/se/example/user/core.clj
poly diff > ../output/testing-diff.txt
poly info fake-sha:e7ebe68 > ../output/testing-info-1.txt
cp ../sections/testing/user-interface-test.clj components/user/test/se/example/user/interface_test.clj
set +e
poly test > ../output/testing-test-failing.txt
set -e
cp ../sections/testing/user-interface-test2.clj components/user/test/se/example/user/interface_test.clj
poly test > ../output/testing-test-ok.txt
poly info :dev fake-sha:e7ebe68 > ../output/testing-info-2.txt
poly info project:cl:dev fake-sha:e7ebe68 > ../output/testing-info-3.txt
mkdir projects/command-line/test
cp ../sections/testing/deps.edn .
cp ../sections/testing/command-line-deps.edn projects/command-line/deps.edn
mkdir projects/command-line/test/project
cp ../sections/testing/dummy_test.clj projects/command-line/test/project
git add projects/command-line/test/project/dummy_test.clj
poly info fake-sha:e7ebe68 > ../output/testing-info-4.txt
poly info :project fake-sha:e7ebe68 > ../output/testing-info-5.txt
git add --all
git commit -m "Added tests"
git tag -f stable-lisa
poly info fake-sha:e7ebe68 > ../output/testing-info-6.txt
poly info :all-bricks fake-sha:e7ebe68 > ../output/testing-info-7.txt
poly info :all-bricks :dev fake-sha:e7ebe68 > ../output/testing-info-8.txt
poly info :all fake-sha:e7ebe68 > ../output/testing-info-9.txt
poly info :all :dev fake-sha:e7ebe68 > ../output/testing-info-10.txt
poly test :all :dev > ../output/testing-test-all.txt

echo "### 12/13 Profile ###"
poly create p name:user-service
cp ../sections/profile/user-service-deps.edn projects/user-service/deps.edn
poly create b name:user-api
cp ../sections/profile/user-api-core.clj bases/user-api/src/se/example/user_api/core.clj
cp ../sections/profile/user-api-api.clj bases/user-api/src/se/example/user_api/api.clj
poly create c name:user-remote interface:user
cp ../sections/profile/user-remote-core.clj components/user-remote/src/se/example/user/core.clj
cp ../sections/profile/user-remote-interface.clj components/user-remote/src/se/example/user/interface.clj
cp ../sections/profile/deps.edn .
cp ../sections/profile/command-line-deps.edn projects/command-line/deps.edn
cd scripts
./build-cli-uberjar.sh
cd ..
cp ../sections/profile/build-user-service-uberjar.sh scripts
cd scripts
chmod +x build-user-service-uberjar.sh
./build-user-service-uberjar.sh
#cd ../projects/user-service/target
#nohup 'java -jar service.jar' &
#cd ../../command-line/target
#java -jar command-line.jar Lisa
cd ..
poly info + fake-sha:e7ebe68 > ../output/profile-info-1.txt
poly info +remote fake-sha:e7ebe68 > ../output/profile-info-2.txt
set +e
poly info +default +remote fake-sha:e7ebe68 > ../output/profile-info-3.txt
set -e
poly info :loc fake-sha:e7ebe68 > ../output/profile-info-4.txt
poly test :project fake-sha:e7ebe68 > ../output/profile-test.txt

echo "### 13/13 Configuration ###"
poly ws get:settings
poly ws get:settings:profile-to-settings:default
poly ws get:keys
poly ws get:components:keys
poly ws out:ws.edn
poly info ws-file:ws.edn fake-sha:e7ebe68
poly ws get:old-user-input:args ws-file:ws.edn > ../output/profile-ws.txt

echo "Elapsed: $((($SECONDS / 60) % 60)) min $(($SECONDS % 60)) sec"
