package cn.cuit.userservice.Service;


import cn.cuit.userservice.Entity.UserEmployer;

public interface UserEmployerService {
    UserEmployer getByUserId(Long user_id);
    int save(UserEmployer employer);
    int update(UserEmployer employer);
}