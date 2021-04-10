package io.vlingo.xoom.examples.petclinic.infrastructure.persistence;

import io.vlingo.xoom.examples.petclinic.model.veterinarian.VeterinarianContactInformationChanged;

import io.vlingo.xoom.common.serialization.JsonSerialization;
import io.vlingo.xoom.symbio.BaseEntry.TextEntry;
import io.vlingo.xoom.symbio.EntryAdapter;
import io.vlingo.xoom.symbio.Metadata;

/**
 * See
 * <a href="https://docs.vlingo.io/xoom-lattice/entity-cqrs#entryadapter-and-entryadapterprovider">
 *   EntryAdapter and EntryAdapterProvider
 * </a>
 */
public final class VeterinarianContactInformationChangedAdapter implements EntryAdapter<VeterinarianContactInformationChanged,TextEntry> {

  @Override
  public VeterinarianContactInformationChanged fromEntry(final TextEntry entry) {
    return JsonSerialization.deserialized(entry.entryData(), entry.typed());
  }

  @Override
  public TextEntry toEntry(final VeterinarianContactInformationChanged source, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(VeterinarianContactInformationChanged.class, 1, serialization, metadata);
  }

  @Override
  public TextEntry toEntry(final VeterinarianContactInformationChanged source, final String id, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(id, VeterinarianContactInformationChanged.class, 1, serialization, metadata);
  }

  @Override
  public TextEntry toEntry(final VeterinarianContactInformationChanged source, final int version, final String id, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(id, VeterinarianContactInformationChanged.class, 1, serialization, version, metadata);
  }
}
