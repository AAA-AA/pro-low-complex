package complex.com.domain;

import lombok.Data;

import java.util.Date;

/**
 * @author : hongqiangren.
 * @since: 2019/8/25 16:44
 */
@Data
public class ParameterDto {
    private String name;
    private String region;
    private Date date1;
    private Boolean delivery;
    private String resource;
    private String desc;
}
