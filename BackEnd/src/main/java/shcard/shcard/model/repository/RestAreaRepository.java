package shcard.shcard.model.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import shcard.shcard.model.domain.RestAreas;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RestAreaRepository {


    private final EntityManager em;


    public RestAreas findOne(int no) {
        return em.find(RestAreas.class, no);
    }

    @Nullable
    public List<RestAreas> findAll() {
        return em.createQuery("select r from RestAreas r", RestAreas.class).getResultList();
    }


}
