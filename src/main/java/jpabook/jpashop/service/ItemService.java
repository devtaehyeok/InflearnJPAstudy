package jpabook.jpashop.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

	private final ItemRepository itemRepository;

	@Transactional
	public void saveItem(Item item) {
		itemRepository.save(item);
	}

	public List<Item> findItems() {
		return itemRepository.findAll();
	}

	public Item findOne(Long itemId) {
		return itemRepository.findOne(itemId);
	}

	@Transactional
	public Item update(Item itemParam) { // itemParam: 파리미터로 넘어온 준영속 상태의 엔티티
		Item findItem = itemRepository.findOne(itemParam.getId()); // 같은 엔티티를 조회한다.
		findItem.setPrice(itemParam.getPrice()); // 데이터를 수정한다.
		return findItem;
	}

}
