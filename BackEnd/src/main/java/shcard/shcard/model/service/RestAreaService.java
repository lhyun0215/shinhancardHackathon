package shcard.shcard.model.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shcard.shcard.model.domain.RestAreas;
import shcard.shcard.model.repository.RestAreaRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
//@RequiredArgsConstructor
public class RestAreaService {
    @Autowired
    RestAreaRepository restAreaRepository;

    public RestAreas findOne(int no) {
        return restAreaRepository.findOne(no);
    }
    /**
     * 휴게소 전체 조회
     */
    public List<RestAreas>findAll(){
        return restAreaRepository.findAll();
    }

}
