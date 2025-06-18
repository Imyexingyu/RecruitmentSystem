package cn.cuit.authservice.Feign;

import cn.cuit.authservice.Dao.UserInitDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service")
public interface RemoteUserClient {

    @PostMapping("/init/candidate")
    void initCandidate(@RequestBody UserInitDTO dto);

    @PostMapping("/init/employer")
    void initEmployer(@RequestBody UserInitDTO dto);
}