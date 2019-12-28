#!/usr/bin/env bash
# Small utility script to add master data required for the example to a schemata instance
# Requires curl and jq to be on the path, vlingo-schemata runnning on localhost and
# VLINGO_SCHEMATA_PORT being set.

ORG_ID=$(curl -s -d '{ "organizationId": "", "name": "Vlingo", "description": "Vlingo Organization" }' -H 'Content-Type: application/json' -X POST http://localhost:${VLINGO_SCHEMATA_PORT}/organizations | jq -r '.organizationId')
UNIT_ID=$(curl -s -d '{ "organizationId": "'${ORG_ID}'", "unitId": "", "name": "examples", "description": "Examples for vlingo/schemata" }' -X POST -H 'Content-Type: application/json' http://localhost:${VLINGO_SCHEMATA_PORT}/organizations/${ORG_ID}/units | jq -r '.unitId')
CONTEXT_ID=$(curl -s -X POST -H 'Content-Type: application/json' -d '{ "organizationId": "'${ORG_ID}'", "unitId": "'${UNIT_ID}'", "contextId": "", "namespace": "io.vlingo.examples.schemata", "description": "Bounded context for vlingo/schemata examples" }' http://localhost:${VLINGO_SCHEMATA_PORT}/organizations/${ORG_ID}/units/${UNIT_ID}/contexts | jq -r '.contextId')
curl -s -X POST -H 'Content-Type: application/json' -d '{ "organizationId": "'${ORG_ID}'", "unitId": "'${UNIT_ID}'", "contextId": "'${CONTEXT_ID}'", "schemaId": "", "category": "Event", "name": "SchemaDefined", "scope": "Public", "description": "Fired whenever a new schema is defined." }' http://localhost:${VLINGO_SCHEMATA_PORT}/organizations/${ORG_ID}/units/${UNIT_ID}/contexts/${CONTEXT_ID}/schemas
curl -s -X POST -H 'Content-Type: application/json' -d '{ "organizationId": "'${ORG_ID}'", "unitId": "'${UNIT_ID}'", "contextId": "'${CONTEXT_ID}'", "schemaId": "", "category": "Event", "name": "SchemaPublished", "scope": "Public", "description": "Fired whenever a schema version is published." }' http://localhost:${VLINGO_SCHEMATA_PORT}/organizations/${ORG_ID}/units/${UNIT_ID}/contexts/${CONTEXT_ID}/schemas
