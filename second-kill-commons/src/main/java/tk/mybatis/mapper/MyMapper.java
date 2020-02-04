package tk.mybatis.mapper;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;
/**
 * @author 佐斯特勒
 * <p>
 *  Mapper统一接口
 * </p>
 * @version v1.0.0
 * @date 2020/1/15 9:13
 * @see  MyMapper
 **/
public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
