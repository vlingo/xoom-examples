package io.vlingo.xoom.examples.petclinic.infrastructure.persistence;

import io.vlingo.xoom.examples.petclinic.model.animaltype.AnimalTypeRenamed;

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
public final class AnimalTypeRenamedAdapter implements EntryAdapter<AnimalTypeRenamed,TextEntry> {

  @Override
  public AnimalTypeRenamed fromEntry(final TextEntry entry) {
    return JsonSerialization.deserialized(entry.entryData(), entry.typed());
  }

  @Override
  public TextEntry toEntry(final AnimalTypeRenamed source, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(AnimalTypeRenamed.class, 1, serialization, metadata);
  }

  @Override
  public TextEntry toEntry(final AnimalTypeRenamed source, final String id, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(id, AnimalTypeRenamed.class, 1, serialization, metadata);
  }

  @Override
  public TextEntry toEntry(final AnimalTypeRenamed source, final int version, final String id, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(id, AnimalTypeRenamed.class, 1, serialization, version, metadata);
  }
}
