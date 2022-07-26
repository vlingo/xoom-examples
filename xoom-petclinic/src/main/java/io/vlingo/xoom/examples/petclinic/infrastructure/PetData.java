package io.vlingo.xoom.examples.petclinic.infrastructure;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vlingo.xoom.examples.petclinic.model.pet.PetState;

public class PetData {
  public final String id;
  public final NameData name;
  public final DateData birth;
  public final DateData death;
  public final KindData kind;
  public final OwnerData owner;

  public static PetData from(final PetState petState) {
    final NameData name = petState.name != null ? NameData.from(petState.name) : null;
    final DateData birth = petState.birth != null ? DateData.from(petState.birth) : null;
    final DateData death = petState.death != null ? DateData.from(petState.death) : null;
    final KindData kind = petState.kind != null ? KindData.from(petState.kind) : null;
    final OwnerData owner = petState.owner != null ? OwnerData.from(petState.owner) : null;
    return from(petState.id, name, birth, death, kind, owner);
  }

  @JsonCreator
  public static PetData from(@JsonProperty("id") final String id,
                             @JsonProperty("name") final NameData name,
                             @JsonProperty("birth") final DateData birth,
                             @JsonProperty("death") final DateData death,
                             @JsonProperty("kind") final KindData kind,
                             @JsonProperty("owner") final OwnerData owner) {
    return new PetData(id, name, birth, death, kind, owner);
  }

  public static List<PetData> from(final List<PetState> states) {
    return states.stream().map(PetData::from).collect(Collectors.toList());
  }

  public static PetData empty() {
    return from(PetState.identifiedBy(""));
  }

  private PetData (final String id, final NameData name, final DateData birth, final DateData death, final KindData kind, final OwnerData owner) {
    this.id = id;
    this.name = name;
    this.birth = birth;
    this.death = death;
    this.kind = kind;
    this.owner = owner;
  }

}
