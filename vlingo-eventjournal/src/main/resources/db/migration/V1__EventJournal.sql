CREATE TABLE vlingo_event_journal (
  id                 UUID PRIMARY KEY,
  event_timestamp    BIGINT       NOT NULL,
  event_data         JSONB        NOT NULL,
  event_metadata     JSONB        NOT NULL,
  event_type         VARCHAR(256) NOT NULL,
  event_type_version INTEGER      NOT NULL,
  event_stream       VARCHAR(128) NOT NULL,
  event_offset       INTEGER      NOT NULL
);

CREATE INDEX ON vlingo_event_journal (event_stream, event_offset);

CREATE INDEX ON vlingo_event_journal (event_timestamp);

CREATE TABLE vlingo_event_journal_snapshots (
  event_stream          VARCHAR(128) PRIMARY KEY,
  snapshot_type         VARCHAR(256) NOT NULL,
  snapshot_type_version INTEGER      NOT NULL,
  snapshot_data         JSONB        NOT NULL,
  snapshot_data_version INTEGER      NOT NULL,
  snapshot_metadata     JSONB        NOT NULL
);

CREATE TABLE vlingo_event_journal_offsets (
  reader_name   VARCHAR(128) PRIMARY KEY,
  reader_offset BIGINT NOT NULL
);