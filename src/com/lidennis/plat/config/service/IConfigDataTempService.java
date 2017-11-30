package com.lidennis.plat.config.service;
import java.io.File;

import com.lidennis.plat.common.model.UserContext;
import com.lidennis.plat.common.service.ICacheService;
import com.lidennis.plat.config.model.ConfigDataTemp;

public interface IConfigDataTempService extends ICacheService
{
    File buildAndUpdateTmpTempFile( UserContext userContext, ConfigDataTemp dataObj, String dataContent );
    File buildAndUpdateTmpTempFile( UserContext userContext, ConfigDataTemp dataObj, File uploadFile, String uploadFileName );
}
