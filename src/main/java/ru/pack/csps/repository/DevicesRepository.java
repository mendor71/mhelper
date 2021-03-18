package ru.pack.csps.repository;

import org.springframework.data.repository.CrudRepository;
import ru.pack.csps.entity.Devices;

public interface DevicesRepository extends CrudRepository<Devices, Integer> {
}
