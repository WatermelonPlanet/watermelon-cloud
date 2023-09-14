package com.watermelon.user.server.web;




import com.watermelon.common.core.util.R;
import com.watermelon.user.server.dto.SysRegisteredClientDto;
import com.watermelon.user.server.service.SysRegisteredClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
/**
 * (Oauth2RegisteredClient)表控制层
 * @author byh
 * @since 2023-09-14
 */
@RestController
@RequestMapping("/sys_registered_client")
@RequiredArgsConstructor
public class SysRegisteredClientController {


    private final SysRegisteredClientService sysRegisteredClientService;

    /**
     * 通过clientId查询单条数据
     * @param clientId 客户端id
     * @return 单条数据
     */
    @PostMapping("/get_one_by_client_id")
    public R getOneByClientId(@RequestBody String clientId) {
        return R.ok(sysRegisteredClientService.getOneByClientId(clientId));
    }


    /**
     * 通过id查询单条client数据
     * @param id id
     * @return 单条数据
     */
    @PostMapping("/get_one_by_id/{id}")
    public R getOneById(@PathVariable("id") String id) {
        return R.ok(sysRegisteredClientService.getOneById(id));
    }

    /**
     * 保存单条数据
     * @param clientDto
     * @return 保存单条数据
     */
    @PostMapping("/save_client")
    public R saveClient(@RequestBody SysRegisteredClientDto clientDto) {
        return R.ok(sysRegisteredClientService.saveClient(clientDto));
    }





}

