package ru.pack.csps.repository;

import org.springframework.data.repository.CrudRepository;
import ru.pack.csps.entity.PaymentLogs;

public interface PaymentLogsRepository extends CrudRepository<PaymentLogs, Long> {
}
