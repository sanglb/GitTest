package com.lidennis.plat.config.service;
import java.util.Map;

import com.lidennis.plat.common.model.ReturnMsg;
import com.lidennis.plat.common.model.UserContext;
import com.lidennis.plat.common.service.ICacheService;
import com.lidennis.plat.config.model.ConfigSMSGateway;

public interface IConfigGatewayMainService extends ICacheService
{
    ReturnMsg execReceive( UserContext userContext, ConfigSMSGateway smsGateway, Map<String, Object> paramMap ) throws Exception;
}
