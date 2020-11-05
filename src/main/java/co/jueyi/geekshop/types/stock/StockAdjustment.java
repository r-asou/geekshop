package co.jueyi.geekshop.types.stock;

import co.jueyi.geekshop.types.common.Node;
import co.jueyi.geekshop.types.product.ProductVariant;
import lombok.Data;

import java.util.Date;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class StockAdjustment implements Node, StockMovement {
    private Long id;
    private Date createdAt;
    private Date updatedAt;
    private ProductVariant productVariant;
    private StockMovementType type;
    private Integer quantity;
}
