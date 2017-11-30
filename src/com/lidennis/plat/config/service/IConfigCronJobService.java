package com.lidennis.plat.config.service;
import com.lidennis.plat.common.model.ReturnMsg;
import com.lidennis.plat.common.model.UserContext;
import com.lidennis.plat.common.service.IBaseService;
import com.lidennis.plat.config.model.ConfigCronJob;

public interface IConfigCronJobService extends IBaseService
{
    ReturnMsg execJobRequest( UserContext userContext, ConfigCronJob dataObj, String operateType ) throws Exception;
}
