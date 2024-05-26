package com.socialnetwork.socialnetworkjavaspring.services.relations;

import com.socialnetwork.socialnetworkjavaspring.DTOs.relations.RelationResponseObjectDTO;
import com.socialnetwork.socialnetworkjavaspring.models.Relation;
import com.socialnetwork.socialnetworkjavaspring.models.User;
import com.socialnetwork.socialnetworkjavaspring.models.enums.RelationType;
import com.socialnetwork.socialnetworkjavaspring.repositories.IRelationRepository;
import com.socialnetwork.socialnetworkjavaspring.utils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class RelationService implements IRelationService {
    @Autowired
    private IRelationRepository relationRepository;

    @Override
    public Optional<Relation> save(Relation object) {
        return Optional.of(relationRepository.save(object));
    }

    @Override
    public Optional<Relation> delete(Relation object) {
        relationRepository.delete(object);
        return Optional.of(object);
    }

    public List<Relation> findByUserIdAndUserTargetId(String userId, String userTargetId) {
        return relationRepository.findByUserUserIdAndAndUserTarget_UserId(userId, userTargetId);
    }

    @Override
    public void removeAllByUserIdAndUserTargetId(String userId, String userTargetId) {
        relationRepository.removeAllByUser_UserIdAndAndUserTarget_UserId(userId, userTargetId);
    }

    @Override
    public List<Relation> findByUserTargetIdAndType(String userId, RelationType type) {
        return relationRepository.findAllByUserTarget_UserIdAndTypeOrderBySetAtDesc(userId, type);
    }

    @Override
    public Long countMutualFriends(String userId, String userTargetId) {
        return relationRepository.countMutualFriends(userId, userTargetId);
    }

    @Override
    public List<RelationResponseObjectDTO> findRelationDTOWithMutualFriendCount(String userId, List<Relation> relations) {
        List<RelationResponseObjectDTO> relationDTOs = ConvertUtils.convertList(relations, RelationResponseObjectDTO.class);
        for (RelationResponseObjectDTO relationResponseObjectDTO : relationDTOs) {
            String userTargetId = relationResponseObjectDTO.getUser().getUserId();
            Long countMutualFriends = countMutualFriends(userId, userTargetId);
            relationResponseObjectDTO.setMutualFriendCount(countMutualFriends);
        }
        return relationDTOs;
    }

    @Override
    public Optional<Relation> findByUserIdAndUserTargetIdAndType(String userId, String userTargetId, RelationType type){
        return relationRepository.findByUserIdAndUserTargetIdAndType(userId, userTargetId, String.valueOf(type));
    }

    @Transactional
    @Override
    public void acceptFriend(User userRequest, User user) throws Exception {
        save(new Relation(RelationType.FRIEND, userRequest, user))
                .orElseThrow(() -> new Exception("Save relation failed"));
        save(new Relation(RelationType.FRIEND, user, userRequest))
                .orElseThrow(() -> new Exception("Save relation failed"));
        save(new Relation(RelationType.FOLLOW, userRequest, user))
                .orElseThrow(() -> new Exception("Save relation failed"));
        save(new Relation(RelationType.FOLLOW, user, userRequest));
    }
}
