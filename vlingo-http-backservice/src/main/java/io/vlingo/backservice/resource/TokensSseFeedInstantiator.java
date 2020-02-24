package io.vlingo.backservice.resource;

import io.vlingo.http.resource.sse.SseFeed.SseFeedInstantiator;

public class TokensSseFeedInstantiator extends SseFeedInstantiator<TokensSseFeedActor> {
  private static final long serialVersionUID = 8193516785609070616L;
  protected Class<TokensSseFeedActor> feedClass;
  protected String streamName;
  protected int feedPayload;
  protected String feedDefaultId;

  @Override
  public TokensSseFeedActor instantiate() {
    return new TokensSseFeedActor(streamName, feedPayload, feedDefaultId);
  }
}
