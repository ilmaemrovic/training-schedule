package raf.microservice.components.notificationservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import raf.microservice.components.notificationservice.mapper.model.Notification;

import java.time.LocalDateTime;


@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findNotificationByUsername(String username, Pageable pageable);
    @Query(value = "SELECT * FROM notifications " +
            "WHERE " +
            "type_id = COALESCE(:typeId, type_id) " +
            "AND mail_receiver = COALESCE(:mailReceiver, mail_receiver) " +
            "AND date_sent >= COALESCE(:startDate, date_sent) " +
            "AND username = :userName " +
            "AND date_sent <= COALESCE(:endDate, date_sent)",
            nativeQuery = true)
    Page<Notification> findByFilters(@Param("userName") String username,
                                     @Param("typeId") Long type,
                                     @Param("mailReceiver") String mailReceiver,
                                     @Param("startDate") LocalDateTime dateFrom,
                                     @Param("endDate") LocalDateTime dateTo,
                                     Pageable pageable);

    @Query(value = "SELECT * FROM notifications " +
            "WHERE " +
            "type_id = COALESCE(:typeId, type_id) " +
            "AND mail_receiver = COALESCE(:mailReceiver, mail_receiver) " +
            "AND date_sent >= COALESCE(:startDate, date_sent) " +
            "AND date_sent <= COALESCE(:endDate, date_sent)",
            nativeQuery = true)
    Page<Notification> findByFiltersAll(
                                     @Param("typeId") Long type,
                                     @Param("mailReceiver") String mailReceiver,
                                     @Param("startDate") LocalDateTime dateFrom,
                                     @Param("endDate") LocalDateTime dateTo,
                                     Pageable pageable);
}
