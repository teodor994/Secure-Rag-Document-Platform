package org.example.application.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.example.application.config.CustomUserDetails;
import org.example.application.model.ProcessingStatus;
import org.example.application.repository.NodeAccessReadRepository;
import org.example.application.repository.NodeContentRepository;
import org.example.application.repository.NodeRepository;
import org.example.application.repository.entity.*;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.attribute.UserPrincipal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class DocumentsService {
    private final NodeRepository nodeRepository;
    private final NodeContentRepository nodeContentRepository;
    private final NodeAccessReadRepository nodeAccessReadRepository;
    private final VectorStore vectorStore;

    public DocumentsService(NodeAccessReadRepository nodeAccessReadRepository,
                            NodeContentRepository nodeContentRepository,
                            NodeRepository nodeRepository, VectorStore vectorStore)
    {
        this.nodeRepository = nodeRepository;
        this.nodeContentRepository = nodeContentRepository;
        this.nodeAccessReadRepository = nodeAccessReadRepository;
        this.vectorStore = vectorStore;
    }

    // iau un nod dupa id ul lui
    public NodeEntity getNode(UUID nodeId) {
        return nodeRepository.findById(nodeId)
                .orElseThrow(() -> new EntityNotFoundException("Node not found: " + nodeId));
    }

    // caut copiii direct
    public List<NodeEntity> getChildren(UUID parentId, Pageable pageable) {
        return nodeRepository.findByParentId(parentId);
    }

    //nodurile root
    public List<NodeEntity> getRootNodes(Pageable pageable) {
        return nodeRepository.findByParentIdIsNull();
    }

    public List<NodeEntity> resolvePath(List<String> segmentNames) {
        List<NodeEntity> result = new ArrayList<>();
        UUID currentParentId = null;

        for (String segmentName : segmentNames) {
            Page<NodeEntity> candidates = (currentParentId == null)
                    ? nodeRepository.findByParentIdIsNull(Pageable.unpaged())   //intorc nepaginat
                    : nodeRepository.findByParentId(currentParentId, Pageable.unpaged());

            NodeEntity match = candidates.stream()
                    .filter(n -> n.getName().equals(segmentName))
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Nod inexistent pe segmentul: " + segmentName));

            result.add(match);
            currentParentId = match.getId();
        }

        return result;
    }

    // Both of the saves in DB must work in order to proceed
    @Transactional
    public Map<String, Object> uploadFile(MultipartFile file, String path,
                                          String parentId, String createdBy) throws IOException {
        Map<String, Object> response = new HashMap<>();
        try {
            String fileName = file.getOriginalFilename();
            String extension = file.getContentType();
            Long size = file.getSize();
            String type = "document";
            byte[] bytes = file.getBytes();
            String textContent = new String(bytes);
            UUID parentUuid = UUID.fromString(parentId);

            //unicitatea inainre de a scrie
            if(existsWithSameName(parentUuid, fileName, type)) {
                response.put("success", false);
                response.put("message", "exista deja un doc cu numele " + fileName);
                return response;
            }

            NodeEntity node = new NodeEntity();
            node.setName(fileName);
            node.setParentId(UUID.fromString(parentId));
            node.setCreatedBy(UUID.fromString(createdBy));
            node.setPath(path);
            node.setType(type);
            node.setProcessingStatus(ProcessingStatus.PENDING);

            NodeEntity savedNode = nodeRepository.save(node);

            NodeContentEntity nodeContent = new NodeContentEntity();
            nodeContent.setNodeId(savedNode.getId());
            nodeContent.setMimeType(extension);
            nodeContent.setSizeBytes(size);
            nodeContent.setBinaryContent(bytes);
            nodeContent.setTextContent(textContent);

            nodeContentRepository.save(nodeContent);
            response.put("node", savedNode);
            response.put("success", true);
        } catch (Exception ex) {
            response.put("message", ex.getMessage());
            response.put("success",  false);
        }

        return response;
    }

    //iau continutul unui nod
    public NodeContentEntity getNodeContent(UUID nodeId) {
        return nodeContentRepository.findById(nodeId)
                .orElseThrow(() -> new EntityNotFoundException("n am gasit content la " + nodeId));
    }

    private boolean existsWithSameName(UUID parentId, String name, String type) {
        return (parentId != null)
                ? nodeRepository.existsByParentIdAndNameAndType(parentId, name, type)
                : nodeRepository.existsByParentIdIsNullAndNameAndType(name, type);
    }

    public Map<String, Object> createFolder(NodeEntity nodeEntity) {
        Map<String, Object> response = new HashMap<>();

        if (existsWithSameName(nodeEntity.getParentId(), nodeEntity.getName(), nodeEntity.getType())) {
            response.put("success", false);
            response.put("message", "exista deja un doc cu numele " + nodeEntity.getName());
            return response;
        }

        try {
            NodeEntity result = nodeRepository.save(nodeEntity);
            response.put("success", true);
            response.put("result", result);
            return response;
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return response;
        }
    }
}
