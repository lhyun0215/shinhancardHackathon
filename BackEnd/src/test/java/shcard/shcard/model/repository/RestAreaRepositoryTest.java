package shcard.shcard.model.repository;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import shcard.shcard.model.domain.RestAreas;
import shcard.shcard.model.service.RestAreaService;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class RestAreaRepositoryTest {

    @Autowired
    RestAreaRepository restAreaRepository;
    @Autowired
    EntityManager em;
    @Autowired
    RestAreaService restAreaService;


    @Test
    void findAll() {
      RestAreas a = restAreaService.findOne(1);

    }
}