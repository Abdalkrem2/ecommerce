package com.web.ecommerce.dto.category;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {

    @Size(max = 120)
    private String name;

    @Size(max = 500)
    private String description;
}
