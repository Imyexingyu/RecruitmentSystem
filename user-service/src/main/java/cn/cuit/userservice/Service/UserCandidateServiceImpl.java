package cn.cuit.userservice.Service;

import cn.cuit.userservice.Entity.UserCandidate;
import cn.cuit.userservice.Mapper.UserCandidateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserCandidateServiceImpl implements UserCandidateService {

    @Autowired
    private UserCandidateMapper candidateMapper;

    @Override
    public UserCandidate getByUserId(Long user_id) {
        return candidateMapper.findByUserId(user_id);
    }

    @Override
    public int save(UserCandidate candidate) {
        return candidateMapper.insert(candidate);
    }

    @Override
    public int update(UserCandidate candidate) {
        return candidateMapper.update(candidate);
    }
}