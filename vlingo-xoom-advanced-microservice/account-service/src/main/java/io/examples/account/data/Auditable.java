package io.examples.account.data;

import java.sql.Timestamp;

/**
 * The {@link Auditable} interface describes a contract for auditing entities and handling events that update the
 * entity query model for created and updated timestamps.
 *
 * @author Kenny Bastani
 * @see Identity
 */
public interface Auditable {

    /**
     * Get the {@link Timestamp} that describes when an auditable entity was created and saved.
     *
     * @return a {@link Timestamp}.
     */
    Timestamp getDateCreated();

    /**
     * Set the {@link Timestamp} that describes when an auditable entity is created and saved.
     *
     * @param dateCreated is the {@link Timestamp} for when an auditable entity is created and saved.
     */
    void setDateCreated(Timestamp dateCreated);

    /**
     * Get the {@link Timestamp} that describes when an auditable entity was last updated and saved.
     *
     * @return a {@link Timestamp}.
     */
    Timestamp getLastUpdated();

    /**
     * Set the {@link Timestamp} that describes when an auditable entity is updated and saved.
     *
     * @param lastUpdated is the {@link Timestamp} for when an auditable entity is updated and saved.
     */
    void setLastUpdated(Timestamp lastUpdated);
}
