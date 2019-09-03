package complex.com.domain;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author : hongqiangren.
 * @since: 2019/9/2 22:27
 */
@Data
public class FormDto {
    private Double threshold;
    @NotNull(message = "complexType can't be null")
    private Integer complexType;
    @Min(message = "slideWindow min is 4",value = 4)
    @Max(message = "slideWindow max is 8",value = 8)
    @NotNull(message = "slideWindow can't be null")
    private Integer slideWindow;
    private String csrfToken;
    private MultipartFile file;
}
