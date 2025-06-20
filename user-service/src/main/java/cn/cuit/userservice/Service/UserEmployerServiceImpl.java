package cn.cuit.userservice.Service;

import cn.cuit.userservice.Entity.UserEmployer;
import cn.cuit.userservice.Mapper.UserEmployerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserEmployerServiceImpl implements UserEmployerService {

    @Autowired
    private UserEmployerMapper employerMapper;

    @Override
    public UserEmployer getByUserId(Long user_id) {
        return employerMapper.findByUserId(user_id);
    }

    @Override
    public int save(UserEmployer employer) {
        return employerMapper.insert(employer);
    }

    @Override
    public int update(UserEmployer employer) {
        return employerMapper.update(employer);
    }
}
