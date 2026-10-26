package library.domain;

import java.util.List;

/**
 * The members of the library, with their loans. A repository in Evans' sense: the
 * domain says what it needs from storage, in its own terms, and the infrastructure
 * provides it. A member is stored whole, loans included.
 */
public interface MemberRepository {

    List<Member> all();

    void save(Member member);
}
