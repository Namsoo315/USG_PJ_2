package com.usg.chat.application.port.out;

import com.usg.chat.domain.Member;

public interface MemberPersistencePort {

    void saveMember(Member member);

    String getNicknameByEmail(String email);

    Long getIdByEmail(String email);

    String getEmailById(Long id);
}
