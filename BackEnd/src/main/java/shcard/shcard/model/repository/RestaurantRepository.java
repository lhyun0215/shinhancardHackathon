package shcard.shcard.model.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import shcard.shcard.model.domain.RestaurantLists;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RestaurantRepository {


    private final EntityManager em;


    @Nullable
    public List<RestaurantLists> findAll() {
        return em.createQuery("select r from RestaurantLists r", RestaurantLists.class).getResultList();
    }


}
