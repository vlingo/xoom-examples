package com.skyharbor.airtrafficcontrol.infrastructure.persistence;

import java.util.Collection;
import io.vlingo.common.Completes;

import com.skyharbor.airtrafficcontrol.infrastructure.ControllerData;

public interface ControllerQueries {
  Completes<ControllerData> controllerOf(String id);
  Completes<Collection<ControllerData>> controllers();
}