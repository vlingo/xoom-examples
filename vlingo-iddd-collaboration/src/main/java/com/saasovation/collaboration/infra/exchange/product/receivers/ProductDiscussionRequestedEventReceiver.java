package com.saasovation.collaboration.infra.exchange.product.receivers;

import com.saasovation.collaboration.infra.exchange.product.model.ProductDiscussionRequested;
import com.saasovation.collaboration.model.Author;
import com.saasovation.collaboration.model.Creator;
import com.saasovation.collaboration.model.Moderator;
import com.saasovation.collaboration.model.Tenant;
import com.saasovation.collaboration.model.forum.Forum;
import com.saasovation.collaboration.model.forum.ForumId;
import io.vlingo.actors.Stage;
import io.vlingo.common.Tuple2;
import io.vlingo.lattice.exchange.ExchangeReceiver;

public class ProductDiscussionRequestedEventReceiver implements ExchangeReceiver<ProductDiscussionRequested> {
  private final Stage stage;

  public ProductDiscussionRequestedEventReceiver(Stage stage) {
    this.stage = stage;
  }

  @Override
  public void receive(ProductDiscussionRequested event) {

    final Forum.ForumDescription description = new Forum.ForumDescription(Creator.fromExisting(event.productOwnerId),
                                                                          Moderator.fromExisting(event.productOwnerId),
                                                                          event.name,
                                                                          event.description);

    final Tuple2<ForumId, Forum> tuple = Forum.startWith(this.stage, Tenant.fromExisting(event.tenantId), description);

    final Forum forum = tuple._2;

    forum.discussFor(Author.fromExisting(event.productOwnerId), event.name, event.ownerId);
  }
}
