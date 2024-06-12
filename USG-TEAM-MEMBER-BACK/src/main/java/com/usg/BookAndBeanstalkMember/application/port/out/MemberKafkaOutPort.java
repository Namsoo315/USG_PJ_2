package com.usg.BookAndBeanstalkMember.application.port.out;

import com.usg.BookAndBeanstalkMember.adaptor.out.kafka.MemberPublishDTO;

public interface MemberKafkaOutPort {

    void publishMember(MemberPublishDTO memberPublishDTO);
}
