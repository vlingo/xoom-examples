package io.vlingo.developers.petclinic.infrastructure.persistence;

import io.vlingo.developers.petclinic.model.client.ClientContactInformationChanged;

import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.symbio.BaseEntry.TextEntry;
import io.vlingo.symbio.EntryAdapter;
import io.vlingo.symbio.Metadata;

/**
 * See
 * <a href="https://docs.vlingo.io/vlingo-lattice/entity-cqrs#entryadapter-and-entryadapterprovider">
 *   EntryAdapter and EntryAdapterProvider
 * </a>
 */
public final class ClientContactInformationChangedAdapter implements EntryAdapter<ClientContactInformationChanged,TextEntry> {

  @Override
  public ClientContactInformationChanged fromEntry(final TextEntry entry) {
    return JsonSerialization.deserialized(entry.entryData(), entry.typed());
  }

  @Override
  public TextEntry toEntry(final ClientContactInformationChanged source, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(ClientContactInformationChanged.class, 1, serialization, metadata);
  }

  @Override
  public TextEntry toEntry(final ClientContactInformationChanged source, final String id, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(id, ClientContactInformationChanged.class, 1, serialization, metadata);
  }

  @Override
  public TextEntry toEntry(final ClientContactInformationChanged source, final int version, final String id, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(id, ClientContactInformationChanged.class, 1, serialization, version, metadata);
  }
}
