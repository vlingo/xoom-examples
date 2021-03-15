package io.vlingo.developers.petclinic.infrastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import io.vlingo.developers.petclinic.model.pet.PetState;

public class PetData {
  public final String id;
  public final NameData name;
  public final long birth;
  public final long death;
  public final KindData kind;
  public final OwnerData owner;
  public final VisitData visit;

  public static PetData from(final PetState state) {
    return new PetData(state);
  }

  public static List<PetData> from(final List<PetState> states) {
    return states.stream().map(PetData::from).collect(Collectors.toList());
  }

  public static PetData from(final String id, final NameData name, final long birth, final long death,
                             final KindData kind, final OwnerData owner, final VisitData visit){
    return new PetData(id, name, birth, death, kind, owner, visit);
  }

  public static PetData empty() {
    return new PetData(PetState.identifiedBy(""));
  }

  private PetData (final PetState state) {
    this.id = state.id;
    this.birth = state.birth;
    this.death = state.death;
    this.name = NameData.of(state.name.value);
    this.kind = KindData.of(state.kind.animalTypeId);
    this.owner = OwnerData.of(state.owner.clientId);
    this.visit = VisitData.of(state.visit.start, state.visit.end, state.visit.description);
  }

  private PetData(String id, NameData name, long birth, long death, KindData kind, OwnerData owner, VisitData visit) {
    this.id = id;
    this.name = name;
    this.birth = birth;
    this.death = death;
    this.kind = kind;
    this.owner = owner;
    this.visit = visit;
  }
}
