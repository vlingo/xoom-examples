package io.vlingo.xoom.examples.petclinic.infrastructure.persistence;

import io.vlingo.xoom.examples.petclinic.model.pet.PetDeathRecorded;

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
public final class PetDeathRecordedAdapter implements EntryAdapter<PetDeathRecorded,TextEntry> {

  @Override
  public PetDeathRecorded fromEntry(final TextEntry entry) {
    return JsonSerialization.deserialized(entry.entryData(), entry.typed());
  }

  @Override
  public TextEntry toEntry(final PetDeathRecorded source, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(PetDeathRecorded.class, 1, serialization, metadata);
  }

  @Override
  public TextEntry toEntry(final PetDeathRecorded source, final String id, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(id, PetDeathRecorded.class, 1, serialization, metadata);
  }

  @Override
  public TextEntry toEntry(final PetDeathRecorded source, final int version, final String id, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(id, PetDeathRecorded.class, 1, serialization, version, metadata);
  }
}
