#!/usr/bin/env bash
# Small utility script to add master data required for the example to a schemata instance
# Requires curl and jq to be on the path, vlingo-schemata runnning on localhost and
# VLINGO_SCHEMATA_PORT being set.

set -e

fail () {
  echo 'failed.'
  exit 1
}

[ "$VLINGO_SCHEMATA_PORT" != "" ] || (echo 'Please set $VLINGO_SCHEMATA_PORT.' && fail)

PORT=$VLINGO_SCHEMATA_PORT

echo Creating organization ...
ORG=$(curl -s \
  -d '{ "organizationId": "", "name": "Vlingo", "description": "Vlingo Organization" }' \
  -H 'Content-Type: application/json' \
  -X POST http://localhost:${PORT}/organizations) \
  || fail
ORG_ID=$(echo $ORG  | jq -r '.organizationId')
echo Created organization ${ORG_ID}.

echo Creating unit ...
UNIT=$(curl -s \
  -d '{ "organizationId": "'${ORG_ID}'", "unitId": "", "name": "examples", "description": "Examples for vlingo/schemata" }' \
  -H 'Content-Type: application/json' \
  -X POST http://localhost:${PORT}/organizations/${ORG_ID}/units) \
  || fail
UNIT_ID=$(echo $UNIT  | jq -r '.unitId')
echo Created unit ${UNIT_ID}.

echo Creating context ${ORG_ID}
CONTEXT=$(curl -s \
  -d '{ "organizationId": "'${ORG_ID}'", "unitId": "'${UNIT_ID}'", "contextId": "", "namespace": "io.vlingo.examples.schemata", "description": "Bounded context for vlingo/schemata examples" }' \
  -H 'Content-Type: application/json' \
  -X POST http://localhost:${PORT}/organizations/${ORG_ID}/units/${UNIT_ID}/contexts) \
  || fail
CONTEXT_ID=$(echo $CONTEXT| jq -r '.contextId')
echo Created context ${CONTEXT_ID}

echo Creating schemata ...
curl -s \
  -d '{ "organizationId": "'${ORG_ID}'", "unitId": "'${UNIT_ID}'", "contextId": "'${CONTEXT_ID}'", "schemaId": "", "category": "Event", "name": "SchemaDefined", "scope": "Public", "description": "Fired whenever a new schema is defined." }' \
  -H 'Content-Type: application/json' \
  -X POST http://localhost:${PORT}/organizations/${ORG_ID}/units/${UNIT_ID}/contexts/${CONTEXT_ID}/schemas | jq

curl -s \
  -d '{ "organizationId": "'${ORG_ID}'", "unitId": "'${UNIT_ID}'", "contextId": "'${CONTEXT_ID}'", "schemaId": "", "category": "Event", "name": "SchemaPublished", "scope": "Public", "description": "Fired whenever a schema version is published." }' \
  -H 'Content-Type: application/json' \
  -X POST http://localhost:${PORT}/organizations/${ORG_ID}/units/${UNIT_ID}/contexts/${CONTEXT_ID}/schemas | jq

echo Created schemata.
echo Done.
