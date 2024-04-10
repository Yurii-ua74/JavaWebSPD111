package step.learning.dal.dao;

import step.learning.dal.dto.PromotionDTO;

import java.util.ArrayList;
import java.util.List;

public class PromotionDAO {
    public List<PromotionDTO> getAllPromotions() {
        List<PromotionDTO> promotions = new ArrayList<>();

        promotions.add(new PromotionDTO("Товар 1", "ціна1", "знижка1"));
        promotions.add(new PromotionDTO("Товар 2", "ціна2", "знижка2"));
        promotions.add(new PromotionDTO("Товар 3", "ціна3", "знижка3"));

        return promotions;

    }
}
