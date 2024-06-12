package com.usg.book.application.port.out;

import com.usg.book.domain.Member;

public interface MemberPersistencePort {

    void saveMember(Member member);
    String getNicknameByEmail(String email);
}
