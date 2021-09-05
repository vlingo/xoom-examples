#!/bin/sh
VERSION=$1

[[ "$VERSION" != "" ]] || (echo "The version has to be passed as the first argument" && exit)

SCRIPT_PATH=$(cd -- "$(dirname "$0")/" >/dev/null 2>&1 ; pwd -P)

$SCRIPT_PATH/update-version.sh $VERSION

find . -iname README.md | xargs sed -i'.bkp' -e 's/[1-9]*.[0-9]*.[0-9]*-SNAPSHOT/'$VERSION'/g'
find . -iname README.md | xargs git add

sed -i'.bkp' -e 's/[1-9]*.[0-9]*.[0-9]*-SNAPSHOT/'$VERSION'/g' xoom-petclinic/petclinic-generation-settings.json
git add xoom-petclinic/petclinic-generation-settings.json

git commit -m "Next development version $VERSION"
git push --follow-tags
