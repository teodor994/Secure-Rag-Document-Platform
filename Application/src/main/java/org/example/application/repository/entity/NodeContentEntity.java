package org.example.application.repository.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="node_content")
public class NodeContentEntity {
    @Id
    @Column(name="node_id")
    private UUID nodeId;

    @Column(name="mime_type")
    private String mimeType;

    @Column(name="size_bytes")
    private Long sizeBytes;

    @JdbcTypeCode(SqlTypes.VARBINARY)
    @Column(name = "binary_content")
    private byte[] binaryContent;

    @Column(name = "text_content")
    private String textContent;


    public UUID getNodeId() {
        return nodeId;
    }

    public void setNodeId(UUID nodeId) {
        this.nodeId = nodeId;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Long getSizeBytes() {
        return sizeBytes;
    }

    public void setSizeBytes(Long sizeBytes) {
        this.sizeBytes = sizeBytes;
    }

    public byte[] getBinaryContent() {
        return binaryContent;
    }
    
    public void setBinaryContent(byte[] binaryContent) {
        this.binaryContent = binaryContent;
    }
    public String getTextContent() {
        return textContent;
    }
    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }
}