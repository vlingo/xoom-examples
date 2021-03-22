package io.vlingo.cars.resource;

import io.vlingo.actors.Grid;
import io.vlingo.cars.model.Car;
import io.vlingo.cars.persistence.StorageProvider;
import io.vlingo.cars.query.CarQueries;
import io.vlingo.cars.query.view.CarsView;
import io.vlingo.cars.resource.model.CarData;
import io.vlingo.common.Completes;
import io.vlingo.http.Body;
import io.vlingo.http.Header;
import io.vlingo.http.Response;
import io.vlingo.http.ResponseHeader;
import io.vlingo.http.resource.DynamicResourceHandler;
import io.vlingo.http.resource.Resource;
import io.vlingo.http.resource.ResourceBuilder;

import static io.vlingo.common.serialization.JsonSerialization.serialized;
import static io.vlingo.http.Response.Status.*;
import static io.vlingo.http.ResponseHeader.*;

public class CarResource extends DynamicResourceHandler {
    private static final String CarsPath = "/api/cars/%s";

    private final Grid grid;
    private final CarQueries carQueries;

    public CarResource(Grid grid) {
        super(grid.world().stage());
        this.grid = grid;
        this.carQueries = StorageProvider.instance().carQueries;
    }

    @Override
    public Resource<?> routes() {
        return ResourceBuilder.resource("Car Resource", 1,
                ResourceBuilder.post("/api/cars")
                        .body(CarData.class)
                        .handle(this::defineWith),
                ResourceBuilder.get("/api/cars")
                        .handle(this::queryCars),
                ResourceBuilder.get("/api/cars/{carId}")
                        .param(String.class)
                        .handle(this::queryCar));
    }

    private Completes<Response> defineWith(CarData data) {
        return Car.with(grid, data.type, data.model, data.registrationNumber)
                .andThenTo(state -> {
                    final String location = String.format(CarsPath, state.carId);
                    final Header.Headers<ResponseHeader> headers = Header.Headers.of(
                            of(Location, location),
                            of(ContentType, "application/json; charset=UTF-8"));
                    final String serialized = serialized(CarData.from(state));

                    return Completes.withSuccess(Response.of(Created, headers, Body.from(serialized.getBytes(), Body.Encoding.UTF8)));
                })
                .otherwise(response -> Response.of(BadRequest));
    }

    private Completes<Response> queryCar(String carId) {
        return carQueries
                .findOne(carId)
                .andThenTo(car -> car == null
                        ? Completes.withSuccess(Response.of(NotFound, serialized("Car not found!")))
                        : Completes.withSuccess(Response.of(Ok, serialized(car))))
                .otherwise(response -> Response.of(NotFound, serialized("Car not found!")))
                .recoverFrom(e -> Response.of(InternalServerError, serialized(e)));
    }

    private Completes<Response> queryCars() {
        return carQueries
                .findAll()
                .andThenTo(cars -> cars == null
                        ? Completes.withSuccess(Response.of(NotFound, serialized(CarsView.empty().all())))
                        : Completes.withSuccess(Response.of(Ok, serialized(cars.all()))))
                .otherwise(response -> Response.of(NotFound, serialized(CarsView.empty().all())))
                .recoverFrom(e -> Response.of(InternalServerError, serialized(e)));
    }
}
