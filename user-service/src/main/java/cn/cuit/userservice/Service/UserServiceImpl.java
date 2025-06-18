package cn.cuit.userservice.Service;

import cn.cuit.userservice.Entity.UserProfile;
import cn.cuit.userservice.Mapper.UserProfileMapper;
import cn.cuit.userservice.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserProfileMapper userProfileMapper;

    @Override
    public UserProfile findProfileByUsername(String username) {
        return userProfileMapper.findByUsername(username);
    }
}
