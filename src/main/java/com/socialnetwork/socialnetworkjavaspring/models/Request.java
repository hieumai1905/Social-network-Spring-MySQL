package com.socialnetwork.socialnetworkjavaspring.models;

import com.socialnetwork.socialnetworkjavaspring.models.enums.RequestType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "requests")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id", nullable = false)
    private Long requestId;

    @Column(name = "request_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date requestAt;

    @Column(name = "email_request", nullable = false, length = 50, unique = true)
    private String emailRequest;

    @Column(name = "request_type", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private RequestType requestType;

    @Column(name = "request_code", nullable = false, length = 10)
    private String requestCode;

    public Request(String email, RequestType requestType) {
        this.emailRequest = email;
        this.requestType = requestType;
    }

    @PrePersist
    public void prePersist() {
        this.requestAt = new Date();
    }

    @PreUpdate
    public void postUpdate() {
        this.requestAt = new Date();
    }
}
