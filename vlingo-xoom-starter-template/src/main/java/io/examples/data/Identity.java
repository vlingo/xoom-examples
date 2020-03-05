package io.examples.data;

import io.micronaut.data.annotation.DateCreated;
import io.micronaut.data.annotation.DateUpdated;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * The {@link Identity} describes an {@link Auditable} JPA entity that is mapped to a concrete class that extends
 * this implementation.
 *
 * @author Kenny Bastani
 * @see Auditable
 */
@MappedSuperclass
public class Identity implements Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @DateCreated
    private Timestamp dateCreated;

    @DateUpdated
    private Timestamp lastUpdated;

    protected String version;

    public Identity() {
        super();
        this.version = UUID.randomUUID().toString();
    }

    /**
     * Get the {@link Timestamp} that describes when an auditable entity was created and saved.
     *
     * @return a {@link Timestamp}.
     */
    @Override
    public Timestamp getDateCreated() {
        return dateCreated;
    }

    /**
     * Set the {@link Timestamp} that describes when an auditable entity is created and saved.
     *
     * @param dateCreated is the {@link Timestamp} for when an auditable entity is created and saved.
     */
    @Override
    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    /**
     * Get the {@link Timestamp} that describes when an auditable entity was last updated and saved.
     *
     * @return a {@link Timestamp}.
     */
    @Override
    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    /**
     * Set the {@link Timestamp} that describes when an auditable entity is updated and saved.
     *
     * @param lastUpdated is the {@link Timestamp} for when an auditable entity is updated and saved.
     */
    @Override
    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Identity{" +
                "id=" + id +
                ", dateCreated=" + dateCreated +
                ", lastUpdated=" + lastUpdated +
                ", version='" + version + '\'' +
                '}';
    }
}
