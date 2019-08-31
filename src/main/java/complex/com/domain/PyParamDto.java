package complex.com.domain;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author : hongqiangren.
 * @since: 2019/8/25 19:52
 */
@Data
public class PyParamDto {

    private Double threshold;
    @NotNull(message = "complexType can't be null")
    private Integer complexType;
    @Min(message = "slideWindow min is 4",value = 4)
    @Max(message = "slideWindow max is 8",value = 8)
    @NotNull(message = "slideWindow can't be null")
    private Integer slideWindow;
}
