package br.com.veiga.shoporder.persistence.repository;

import br.com.veiga.shoporder.persistence.model.ShopOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopOrderRepository extends JpaRepository<ShopOrder, Long> {
}
