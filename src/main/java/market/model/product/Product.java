package market.model.product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "product",
        indexes = @Index(
                name = "idx_product_id",
                columnList = "product_id",
                unique = true
        ))
public class Product {
    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, name = "product_name")
    private String name;

    @Column(name = "quantity_available", nullable = false)
    private Integer quantity;

    @Column(name = "product_price", nullable = false)
    private double price;

    @Column(name = "product_category", nullable = false)
    private ProductCategory category;
}
