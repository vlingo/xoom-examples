CREATE TABLE vlingo_symbio_journal
(
    id                 UUID PRIMARY KEY,
    entry_timestamp    BIGINT        NOT NULL,
    entry_data         JSONB         NOT NULL,
    entry_metadata     JSONB         NOT NULL,
    entry_type         VARCHAR(256)  NOT NULL,
    entry_type_version INTEGER       NOT NULL,
    stream_name        VARCHAR(1024) NOT NULL,
    stream_version     INTEGER       NOT NULL
);

CREATE INDEX ON vlingo_symbio_journal (stream_name, stream_version);
CREATE INDEX ON vlingo_symbio_journal (entry_timestamp);

CREATE TABLE vlingo_symbio_journal_snapshots
(
    stream_name           VARCHAR(1024) PRIMARY KEY,
    snapshot_type         VARCHAR(256) NOT NULL,
    snapshot_type_version INTEGER      NOT NULL,
    snapshot_data         JSONB        NOT NULL,
    snapshot_data_version INTEGER      NOT NULL,
    snapshot_metadata     JSONB        NOT NULL
);

CREATE TABLE vlingo_symbio_journal_offsets
(
    reader_name   VARCHAR(128) PRIMARY KEY,
    reader_offset BIGINT NOT NULL
);