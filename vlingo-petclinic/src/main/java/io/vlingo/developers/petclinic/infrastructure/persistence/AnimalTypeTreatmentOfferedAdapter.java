package io.vlingo.developers.petclinic.infrastructure.persistence;

import io.vlingo.developers.petclinic.model.animaltype.AnimalTypeTreatmentOffered;

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
public final class AnimalTypeTreatmentOfferedAdapter implements EntryAdapter<AnimalTypeTreatmentOffered,TextEntry> {

  @Override
  public AnimalTypeTreatmentOffered fromEntry(final TextEntry entry) {
    return JsonSerialization.deserialized(entry.entryData(), entry.typed());
  }

  @Override
  public TextEntry toEntry(final AnimalTypeTreatmentOffered source, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(AnimalTypeTreatmentOffered.class, 1, serialization, metadata);
  }

  @Override
  public TextEntry toEntry(final AnimalTypeTreatmentOffered source, final String id, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(id, AnimalTypeTreatmentOffered.class, 1, serialization, metadata);
  }

  @Override
  public TextEntry toEntry(final AnimalTypeTreatmentOffered source, final int version, final String id, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(id, AnimalTypeTreatmentOffered.class, 1, serialization, version, metadata);
  }
}
