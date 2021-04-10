package io.vlingo.xoom.examples.petclinic.infrastructure.persistence;

import io.vlingo.xoom.examples.petclinic.model.pet.PetOwnerChanged;

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
public final class PetOwnerChangedAdapter implements EntryAdapter<PetOwnerChanged,TextEntry> {

  @Override
  public PetOwnerChanged fromEntry(final TextEntry entry) {
    return JsonSerialization.deserialized(entry.entryData(), entry.typed());
  }

  @Override
  public TextEntry toEntry(final PetOwnerChanged source, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(PetOwnerChanged.class, 1, serialization, metadata);
  }

  @Override
  public TextEntry toEntry(final PetOwnerChanged source, final String id, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(id, PetOwnerChanged.class, 1, serialization, metadata);
  }

  @Override
  public TextEntry toEntry(final PetOwnerChanged source, final int version, final String id, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(id, PetOwnerChanged.class, 1, serialization, version, metadata);
  }
}
