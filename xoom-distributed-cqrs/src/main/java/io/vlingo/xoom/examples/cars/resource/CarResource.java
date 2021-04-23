package io.vlingo.xoom.examples.cars.resource;

import io.vlingo.xoom.lattice.grid.Grid;
import io.vlingo.xoom.examples.cars.model.Car;
import io.vlingo.xoom.examples.cars.persistence.StorageProvider;
import io.vlingo.xoom.examples.cars.query.CarQueries;
import io.vlingo.xoom.examples.cars.query.view.CarsView;
import io.vlingo.xoom.examples.cars.resource.model.CarData;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.http.Body;
import io.vlingo.xoom.http.Header;
import io.vlingo.xoom.http.Response;
import io.vlingo.xoom.http.ResponseHeader;
import io.vlingo.xoom.http.resource.DynamicResourceHandler;
import io.vlingo.xoom.http.resource.Resource;
import io.vlingo.xoom.http.resource.ResourceBuilder;

import static io.vlingo.xoom.common.serialization.JsonSerialization.serialized;
import static io.vlingo.xoom.http.Response.Status.*;
import static io.vlingo.xoom.http.ResponseHeader.*;

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
                ResourceBuilder.patch("/api/cars/{carId}/register")
                        .param(String.class)
                        .body(String.class)
                        .handle(this::registerCar),
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

    private Completes<Response> registerCar(final String carId, final String registrationNumber) {
        return grid.actorOf(Car.class, grid.addressFactory().from(carId))
                .andThenTo(car -> car.registerWith(registrationNumber))
                .andThen(carState -> carState == null
                        ? Response.of(NotFound, "Car not found!")
                        : Response.of(Ok, serialized(carState)));
    }
}
