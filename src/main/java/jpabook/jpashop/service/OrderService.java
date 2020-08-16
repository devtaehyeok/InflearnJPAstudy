package jpabook.jpashop.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.DeliveryStatus;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderSearch;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class OrderService {
	private final MemberService memberService;
	private final ItemService itemService;
	private final OrderRepository orderRepository;

	/**
	 * 주문 회원아이디 상품아이디 주문수량
	 */
	@Transactional
	public Long order(Long memberId, Long itemId, int count) {
		Member member = memberService.findOne(memberId);
		Item item = itemService.findOne(itemId);
		Delivery delivery = new Delivery();
		delivery.setAddress(member.getAddress());
		delivery.setStatus(DeliveryStatus.READY);
		OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
		Order order = Order.createOrder(member, delivery, orderItem);
		// 주문 저장
		// Order domain에서 delivery와 OrderItem cascade 걸었음. persist 한번만 하면 됨.

		// Cascade는 언제 쓸까...? Delivery와 OrderItem은 Order만 참조함. 다른 곳도 참조하면 피하자.
		// Order만 OrderItem을 쓴다!!
		orderRepository.save(order);
		return order.getId();
	}

	/** 주문 취소 */
	@Transactional
	public void cancelOrder(Long orderId) {
		// 주문 엔티티 조회
		Order order = orderRepository.findOne(orderId);
		log.info("조회 성공");
		// 주문 취소
		order.cancel();
	}

	public List<Order> findOrders(OrderSearch orderSearch) {
		return orderRepository.findAllByString(orderSearch);

	}
}
