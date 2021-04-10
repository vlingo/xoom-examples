package io.vlingo.xoom.examples.petclinic.infrastructure.persistence;

import io.vlingo.xoom.examples.petclinic.model.pet.PetBirthRecorded;

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
public final class PetBirthRecordedAdapter implements EntryAdapter<PetBirthRecorded,TextEntry> {

  @Override
  public PetBirthRecorded fromEntry(final TextEntry entry) {
    return JsonSerialization.deserialized(entry.entryData(), entry.typed());
  }

  @Override
  public TextEntry toEntry(final PetBirthRecorded source, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(PetBirthRecorded.class, 1, serialization, metadata);
  }

  @Override
  public TextEntry toEntry(final PetBirthRecorded source, final String id, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(id, PetBirthRecorded.class, 1, serialization, metadata);
  }

  @Override
  public TextEntry toEntry(final PetBirthRecorded source, final int version, final String id, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(id, PetBirthRecorded.class, 1, serialization, version, metadata);
  }
}
