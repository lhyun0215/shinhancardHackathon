package shcard.shcard.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shcard.shcard.model.domain.RestAreas;
import shcard.shcard.model.domain.RestaurantLists;
import shcard.shcard.model.repository.RestAreaRepository;
import shcard.shcard.model.repository.RestaurantRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
//@RequiredArgsConstructor
public class RestaurantService {
    @Autowired
    RestaurantRepository restaurantRepository;

    /**
     * 휴게소 전체 조회
     */
    public List<RestaurantLists>findAll(){
        return restaurantRepository.findAll();
    }

}
