package za.co.mixtelematics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import za.co.mixtelematics.model.CommandResponseMapper;

import java.math.BigInteger;
import java.util.List;


@Repository
public interface CommandResponseMapperRepository extends JpaRepository<CommandResponseMapper,Integer> {
    List<CommandResponseMapper> getCommandResponseMapperByBoxNumber(String boxNumber);
    List<CommandResponseMapper> getCommandResponseMapperByBoxNumberAndAndAuditUser(String boxNumber, String auditUser);
    List<CommandResponseMapper> getCommandResponseMapperByBoxNumberAndMessageGuid(String boxNumber, String messageGuid);

    @Modifying
    @Transactional
    @Query(value = "SELECT *   FROM command_response_map  WHERE GETDATE() > command_response_map.removal_date_time_utc",nativeQuery = true)
    List<CommandResponseMapper>  getExpiredCommands();

    @Modifying
    @Transactional
    @Query(value = "{CALL msp_CreateMXcXSequenceNumber(?)}", nativeQuery = true)
    int getCommandResponseMapperNewSequenceNumber(String boxNumber);

    @Modifying
    @Transactional
    @Query(value = " SELECT * FROM command_response_map WHERE  command_response_map.box_number= ?1 AND command_response_map.correlation_id = ?2",nativeQuery = true)
    CommandResponseMapper getRoutingMapper(String boxNumber, long correlationId);

    @Modifying
    @Transactional
    @Query(value = " DELETE FROM command_response_map WHERE command_response_map.box_number= ?1 ",nativeQuery = true)
    void deleteCommandResponseMapperByBoxNumber(String boxNumber);

    @Override
    void deleteAll();

    @Modifying
    @Transactional
    @Query(value = " DELETE FROM command_response_map WHERE command_response_map.box_number= ?1 AND command_response_map.audit_user = ?2 ",nativeQuery = true)
    void deleteCommandResponseMapperByBoxNumberAndAuditUser(String boxNumber, String auditUser);

    @Modifying
    @Transactional
    @Query(value = " DELETE FROM command_response_map WHERE command_response_map.box_number= ?1 AND command_response_map.message_guid = ?2 ",nativeQuery = true)
    void deleteCommandResponseMapperByBoxNumberAndMessageGuid(String boxNumber,String messageGuid);

    @Modifying
    @Transactional
    @Query(value = " DELETE FROM command_response_map WHERE command_response_map.box_number= ?1 AND command_response_map.audit_user = ?2 AND command_response_map.status = 'EXPIRED' OR command_response_map.status = 'RESPONDED'",nativeQuery = true)
    void deleteMappingsRespondedOrExpired(String boxNumber, String auditUser);

    @Modifying
    @Transactional
    @Query(value = "DELETE   FROM command_response_map  WHERE GETDATE() > command_response_map.removal_date_time_utc",nativeQuery = true)
    void removeExpiredCommand();

    @Modifying
    @Transactional
    @Query(value = "UPDATE command_response_map SET command_response_map.status = 'REGISTERED', command_response_map.correlation_id = ?1 WHERE command_response_map.box_number = ?2 AND command_response_map.audit_user = ?3 AND command_response_map.status = 'RESERVED'",nativeQuery=true)
    int updateRegisteredByBoxNumber(BigInteger correlationId, String boxNumber, String auditUser);

    @Modifying
    @Transactional
    @Query(value = "UPDATE command_response_map SET command_response_map.status = 'RESPONDED' WHERE command_response_map.box_number = ?1 AND command_response_map.correlation_id = ?2 AND command_response_map.status = 'REGISTERED' OR command_response_map.status = 'EXPIRED'",nativeQuery=true)
    void updateRegisteredToResponded(String boxNumber, long correlationId);

}
