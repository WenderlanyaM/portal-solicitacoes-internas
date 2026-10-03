package info.bitsolucoes.portal.repository;

import info.bitsolucoes.portal.model.Ticket;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("SELECT t FROM Ticket t WHERE " +
            "(:categoryId IS NULL OR t.category.id = :categoryId) AND " +
            "(:status IS NULL OR t.status = :status) AND " +
            "(:title IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
            "(:startDate IS NULL OR t.creationDate >= :startDate) AND " +
            "(:endDate IS NULL OR t.creationDate <= :endDate)")
    List<Ticket> filterTickets(
            @Param("categoryId") Long categoryId,
            @Param("status") String status,
            @Param("title") String title,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Sort sort
    );

    long countByStatus(String status);
}