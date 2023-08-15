package com.example.best_travel.infrastructure.abstract_services.service;

import com.example.best_travel.api.models.responses.FlyResponse;
import com.example.best_travel.domain.entities.jpa.FlyEntity;
import com.example.best_travel.domain.repositories.FlyRepository;
import com.example.best_travel.infrastructure.abstract_services.IFlyService;
import com.example.best_travel.util.SortType;
import com.example.best_travel.util.annotations.Notify;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
@AllArgsConstructor//No es compatible con la anotacion "qualifier"
public class FlyService implements IFlyService {
    private final FlyRepository flyRepository;
    private final WebClient webClient;

    //Forma para utilizar varios webClients
//    public FlyService(FlyRepository flyRepository,@Qualifier(value = "base") WebClient webClient) {
//        this.flyRepository = flyRepository;
//        this.webClient = webClient;
//    }

    @Notify
    @Override
    public Page<FlyResponse> readAll(Integer page, Integer size, SortType sortType) {
        //Iniciar como nulo por eso se crea el objeto de este tipo,
        // ya que es el tipo de objeto recibe spring
        PageRequest pageRequest = null;
        switch (sortType){
            case NONE -> pageRequest = PageRequest.of(page, size);
            case LOWER -> pageRequest = PageRequest.of(page, size, Sort.by(FIELD_BY_SORT).ascending());
            case UPPER -> pageRequest = PageRequest.of(page, size, Sort.by(FIELD_BY_SORT).descending());
        }
        return this.flyRepository.findAll(pageRequest).map(this::entityToResponse);
    }

    @Override
    public List<FlyResponse> readLessPrice(BigDecimal price) {
        return this.flyRepository.selectLessPrice(price)
                .stream()
                .map(this::entityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<FlyResponse> readBetweenPrice(BigDecimal min, BigDecimal max) {
        return this.flyRepository.selectBetweenPrice(min, max)
                .stream()
                .map(this::entityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<FlyResponse> readByOriginDestiny(String origen, String destino) {
        return this.flyRepository.selectOriginDestiny(origen, destino)
                .stream()
                .map(this::entityToResponse)
                .collect(Collectors.toList());
    }

    private FlyResponse entityToResponse(FlyEntity entity){
        FlyResponse response = new FlyResponse();
        //Copy the property values of the given source bean into the target bean.
        BeanUtils.copyProperties(entity, response);
        return response;
    }
}
