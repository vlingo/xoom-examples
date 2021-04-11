#!/usr/bin/env bash
# Small utility script to add master data required for the example to a schemata instance
# Requires curl and jq to be on the path, xoom-schemata runnning on localhost and
# XOOM_SCHEMATA_PORT being set.

set -e

fail () {
  MSG="${1:-failed}"
  echo  >&2 "$MSG"
  exit 1
}

[ "$XOOM_SCHEMATA_PORT" != "" ] || fail 'XOOM_SCHEMATA_PORT not set'
type curl >/dev/null 2>&1 || fail "'curl' is required but not installed."
type jq >/dev/null 2>&1 || fail "'jq' is required but not installed."


PORT=$XOOM_SCHEMATA_PORT

echo Creating organization ...
ORG=$(curl -s \
  -d '{ "organizationId": "", "name": "Vlingo", "description": "Vlingo Organization" }' \
  -H 'Content-Type: application/json' \
  -X POST http://localhost:${PORT}/api/organizations) \
  || fail
ORG_ID=$(echo $ORG  | jq -r '.organizationId')
echo Created organization ${ORG_ID}.

echo Creating unit ...
UNIT=$(curl -s \
  -d '{ "organizationId": "'${ORG_ID}'", "unitId": "", "name": "examples", "description": "Examples for vlingo/schemata" }' \
  -H 'Content-Type: application/json' \
  -X POST http://localhost:${PORT}/api/organizations/${ORG_ID}/units) \
  || fail
UNIT_ID=$(echo $UNIT  | jq -r '.unitId')
echo Created unit ${UNIT_ID}.

echo Creating context ${ORG_ID}
CONTEXT=$(curl -s \
  -d '{ "organizationId": "'${ORG_ID}'", "unitId": "'${UNIT_ID}'", "contextId": "", "namespace": "io.vlingo.examples.schemata", "description": "Bounded context for vlingo/schemata examples" }' \
  -H 'Content-Type: application/json' \
  -X POST http://localhost:${PORT}/api/organizations/${ORG_ID}/units/${UNIT_ID}/contexts) \
  || fail
CONTEXT_ID=$(echo $CONTEXT| jq -r '.contextId')
echo Created context ${CONTEXT_ID}

echo Creating schemata ...
curl -s \
  -d '{ "organizationId": "'${ORG_ID}'", "unitId": "'${UNIT_ID}'", "contextId": "'${CONTEXT_ID}'", "schemaId": "", "category": "Event", "name": "SchemaDefined", "scope": "Public", "description": "Fired whenever a new schema is defined." }' \
  -H 'Content-Type: application/json' \
  -X POST http://localhost:${PORT}/api/organizations/${ORG_ID}/units/${UNIT_ID}/contexts/${CONTEXT_ID}/schemas | jq .

curl -s \
  -d '{ "organizationId": "'${ORG_ID}'", "unitId": "'${UNIT_ID}'", "contextId": "'${CONTEXT_ID}'", "schemaId": "", "category": "Event", "name": "SchemaPublished", "scope": "Public", "description": "Fired whenever a schema version is published." }' \
  -H 'Content-Type: application/json' \
  -X POST http://localhost:${PORT}/api/organizations/${ORG_ID}/units/${UNIT_ID}/contexts/${CONTEXT_ID}/schemas | jq .

echo Created schemata.
echo Done.
