package jpabook.jpashop.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // final로만 생성자 만들어줌
public class MemberService {

	private final MemberRepository memberRepository;

	// @Autowired 이거 없어도 스프링이 자동으로 Autowire해줌
//	public MemberService(MemberRepository memberRepository) {
//		this.memberRepository = memberRepository;
//	}

	// 영속성 컨텍스트는 키가 id가 됨. persist 시에 키가 있음. 디비 들가기 전에도
	@Transactional(readOnly = false)
	public Long join(Member member) {
		validateDuplicateMember(member);
		memberRepository.save(member);
		return member.getId();
	}

	public void validateDuplicateMember(Member member) {
		List<Member> findMembers = memberRepository.findByName(member.getName());
		// Exception
		if (!findMembers.isEmpty()) {
			throw new IllegalStateException("이미 존재하는 회원입니다.");
		}

	}

	@Transactional(readOnly = true)
	public List<Member> findMembers() {

		return memberRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Member findOne(Long memberId) {

		return memberRepository.findOne(memberId);
	}
}
