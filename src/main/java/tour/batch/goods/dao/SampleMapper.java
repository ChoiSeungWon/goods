package tour.batch.goods.dao;

import org.springframework.stereotype.Repository;
import tour.batch.goods.model.Sample;

import java.util.List;

@Repository("sampleMapper")
public interface SampleMapper {

    List<Sample> getGoodsAddr() throws  Exception;
}
