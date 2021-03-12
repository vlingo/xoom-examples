package io.vlingo.developers.petclinic.infrastructure.persistence;

import io.vlingo.developers.petclinic.model.veterinarian.VeterinarianSpecialtyChosen;

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
public final class VeterinarianSpecialtyChosenAdapter implements EntryAdapter<VeterinarianSpecialtyChosen,TextEntry> {

  @Override
  public VeterinarianSpecialtyChosen fromEntry(final TextEntry entry) {
    return JsonSerialization.deserialized(entry.entryData(), entry.typed());
  }

  @Override
  public TextEntry toEntry(final VeterinarianSpecialtyChosen source, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(VeterinarianSpecialtyChosen.class, 1, serialization, metadata);
  }

  @Override
  public TextEntry toEntry(final VeterinarianSpecialtyChosen source, final String id, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(id, VeterinarianSpecialtyChosen.class, 1, serialization, metadata);
  }

  @Override
  public TextEntry toEntry(final VeterinarianSpecialtyChosen source, final int version, final String id, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(id, VeterinarianSpecialtyChosen.class, 1, serialization, version, metadata);
  }
}
