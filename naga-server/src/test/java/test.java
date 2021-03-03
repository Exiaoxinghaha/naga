import cn.hutool.core.date.DateUtil;
import hipeer.naga.entity.cluster.HdfsEntity;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

public class test {
    @Test
    public void a(){

        int a = TimeZone.getDefault().getRawOffset();
        String b = DateUtil.format(new Date(a), "yyyy-MM-dd HH:mm:ss");
        List<String> colums = Arrays.stream(FieldUtils.getAllFields(HdfsEntity.class))
                .map(Field::getName).collect(Collectors.toList());
    }
}
