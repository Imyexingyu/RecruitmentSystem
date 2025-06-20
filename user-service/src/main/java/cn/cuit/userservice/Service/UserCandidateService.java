package cn.cuit.userservice.Service;

import cn.cuit.userservice.Entity.UserCandidate;

public interface UserCandidateService {
    UserCandidate getByUserId(Long user_id);
    int save(UserCandidate candidate);
    int update(UserCandidate candidate);
}
