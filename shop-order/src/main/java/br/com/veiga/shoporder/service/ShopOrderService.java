package br.com.veiga.shoporder.service;

import br.com.veiga.shoporder.dto.ShopOrderDTO;
import br.com.veiga.shoporder.persistence.model.ShopOrder;
import br.com.veiga.shoporder.persistence.repository.ShopOrderRepository;
import br.com.veiga.shoporder.utils.QueueUtils;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static br.com.veiga.shoporder.utils.QueueUtils.QUEUE_NAME;

@Service
public class ShopOrderService {

    private final ShopOrderRepository repository;
    private final ModelMapper mapper;
    private final RabbitTemplate rabbitTemplate;

    public ShopOrderService(ShopOrderRepository repository, ModelMapper mapper, RabbitTemplate rabbitTemplate) {
        this.repository = repository;
        this.mapper = mapper;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void create(ShopOrderDTO request) {
        ShopOrder shopOrder = repository.save(mapper.map(request, ShopOrder.class));
        rabbitTemplate.convertAndSend(QUEUE_NAME, shopOrder.getId());
    }

    public Page<ShopOrderDTO> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(shopOrder -> mapper.map(shopOrder, ShopOrderDTO.class));
    }

    @RabbitListener(queues = QUEUE_NAME)
    private void subscribe(Long id) {
        Optional<ShopOrder> shopOrder = repository.findById(id);

        if (shopOrder.isPresent()) {
            shopOrder.get().setStatus("DONE");
            repository.save(shopOrder.get());
        }
    }
}
