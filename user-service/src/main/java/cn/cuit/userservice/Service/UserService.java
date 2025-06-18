package cn.cuit.userservice.Service;

import cn.cuit.userservice.Entity.UserProfile;

public interface UserService {
    UserProfile findProfileByUsername(String username);
}
