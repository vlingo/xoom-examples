package io.vlingo.xoom.examples.petclinic.infrastructure.persistence;

import io.vlingo.xoom.examples.petclinic.model.specialtytype.SpecialtyTypeOffered;

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
public final class SpecialtyTypeOfferedAdapter implements EntryAdapter<SpecialtyTypeOffered,TextEntry> {

  @Override
  public SpecialtyTypeOffered fromEntry(final TextEntry entry) {
    return JsonSerialization.deserialized(entry.entryData(), entry.typed());
  }

  @Override
  public TextEntry toEntry(final SpecialtyTypeOffered source, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(SpecialtyTypeOffered.class, 1, serialization, metadata);
  }

  @Override
  public TextEntry toEntry(final SpecialtyTypeOffered source, final String id, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(id, SpecialtyTypeOffered.class, 1, serialization, metadata);
  }

  @Override
  public TextEntry toEntry(final SpecialtyTypeOffered source, final int version, final String id, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(id, SpecialtyTypeOffered.class, 1, serialization, version, metadata);
  }
}
