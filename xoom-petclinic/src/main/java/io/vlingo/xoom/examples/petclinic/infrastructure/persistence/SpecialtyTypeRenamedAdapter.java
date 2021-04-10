package io.vlingo.xoom.examples.petclinic.infrastructure.persistence;

import io.vlingo.xoom.examples.petclinic.model.specialtytype.SpecialtyTypeRenamed;

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
public final class SpecialtyTypeRenamedAdapter implements EntryAdapter<SpecialtyTypeRenamed,TextEntry> {

  @Override
  public SpecialtyTypeRenamed fromEntry(final TextEntry entry) {
    return JsonSerialization.deserialized(entry.entryData(), entry.typed());
  }

  @Override
  public TextEntry toEntry(final SpecialtyTypeRenamed source, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(SpecialtyTypeRenamed.class, 1, serialization, metadata);
  }

  @Override
  public TextEntry toEntry(final SpecialtyTypeRenamed source, final String id, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(id, SpecialtyTypeRenamed.class, 1, serialization, metadata);
  }

  @Override
  public TextEntry toEntry(final SpecialtyTypeRenamed source, final int version, final String id, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(id, SpecialtyTypeRenamed.class, 1, serialization, version, metadata);
  }
}
