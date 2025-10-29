package com.eventia.shipping.repository;

import com.eventia.shipping.entity.ProcessedMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedMessageRepository extends JpaRepository<ProcessedMessage,String> {
}
