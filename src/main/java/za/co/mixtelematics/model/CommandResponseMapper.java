package za.co.mixtelematics.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Table(name = "CommandResponseMap")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommandResponseMapper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int MapperId;
    @Column(nullable = false,name = "MessageGuid")
    @NotEmpty(message = "Message Guid is required")
    private String messageGuid;
    @Column(nullable = false,name = "BoxNumber")
    @NotEmpty(message = "Box number is required")
    private String boxNumber;
    @Column(nullable = true,name = "Imei")
    private BigInteger imei;
    @Column(nullable = false,name = "ExpiryDateTimeUtc")
    @NotEmpty(message = "expiryDateTimeUtc is required")
    private LocalDateTime expiryDateTimeUtc;
    @Column(nullable = false,name = "RemovalDateTimeUtc")
    @NotEmpty(message = "RemovalDateTimeUtc is required")
    private LocalDateTime removalDateTimeUtc;
    @Column(nullable = false,name = "DestinationType")
    @NotEmpty(message = "DestinationType is required")
    private String destinationType;
    @Column(nullable = true,name = "Url")
    private String url;
    @Column(nullable = true,name = "NameOrPath")
    private String nameOrPath;
    @Column(nullable = true,name = "JmsType")
    private String jmsType;
    @Column(nullable = false,name = "Retry")
    @NotEmpty(message = "Retry is required")
    private int retry;
    @Column(nullable = true,name = "RetryThreshold")
    private Integer retryThreshold;
    @Column(nullable = false,name = "FileExtensionType")
    @NotEmpty(message = "FileExtensionType is required")
    private String fileExtensionType;
    @Column(nullable = true,name = "MessageType")
    private String messageType;
    @Column(nullable = true,name = "MobileMessageName")
    private String mobileMessageName;
    @Column(nullable = true,name = "ConnectionId")
    private String connectionId;
    @Column(nullable = true,name = "UserName")
    private String userName;
    @Column(nullable = true,name = "Password")
    private String password;
    @Column(nullable = true,name = "CorrelationId")
    private Long correlationId;
    @Column(nullable = false,name = "Status")
    @NotEmpty(message = "status is required")
    private String status;
    @Column(nullable = false,name = "AuditUser")
    @NotEmpty(message = "Audit User is required")
    private String auditUser;
}
