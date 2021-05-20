package market.service;

import market.dto.model.alert.AlertReadDto;

import java.util.List;


public interface AlertService {
    List<AlertReadDto> findAll();
    void processAlerts();
}