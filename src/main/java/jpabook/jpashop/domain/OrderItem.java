package jpabook.jpashop.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "order_item")
@Getter
@Setter
public class OrderItem {
	@Id
	@GeneratedValue
	@Column(name = "order_item_id")
	private Long id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item; // 주문 상품
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	private Order order; // 주문
	private int orderPrice; // 주문 가격
	private int count; // 주문 수량

	protected OrderItem() {

		// nothing to do
	}

	// ==생성 매서드==//
	/**
	 * 상품주문 생성 매서드 1. 주문할 item을 세팅한다 2. 주문 가격을 세팅한다. 3. 주문 수량을 세팅한다. 4. 상품 재고를 차감한다.
	 * 5. 상품주문 생성을 완료한다.
	 * 
	 * @param item
	 * @param orderPrice
	 * @param count
	 * @return
	 */
	public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
		OrderItem orderItem = new OrderItem();
		orderItem.setItem(item);
		orderItem.setOrderPrice(orderPrice);
		orderItem.setCount(count);
		item.removeStock(count);
		return orderItem;
	}

	// ==비즈니스 로직==//
	/**
	 * 주문 수량을 원복하고 주문 취소
	 */
	public void cancel() {
		getItem().addStock(count);
	}

	// == 조회 로직 == //
	/*
	 * 주문 가격 * 주문 수량 값 계산
	 */
	public int getTotalPrice() {
		return this.getOrderPrice() * getCount();
	}

}
