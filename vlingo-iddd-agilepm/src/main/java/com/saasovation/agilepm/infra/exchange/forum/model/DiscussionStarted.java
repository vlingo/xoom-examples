package com.saasovation.agilepm.infra.exchange.forum.model;

import io.vlingo.lattice.model.DomainEvent;

public final class DiscussionStarted extends DomainEvent {
    public final String tenantId;
    public final String forumId;
    public final String discussionId;
    public final String ownerId;

    public DiscussionStarted(String tenantId, String forumId, String discussionId, String ownerId) {
        this.tenantId = tenantId;
        this.forumId = forumId;
        this.discussionId = discussionId;
        this.ownerId = ownerId;
    }
}
