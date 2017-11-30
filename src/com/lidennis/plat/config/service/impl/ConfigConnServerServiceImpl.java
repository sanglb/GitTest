package com.lidennis.plat.config.service.impl;
import java.util.HashMap;
import java.util.Map;

import com.lidennis.plat.common.model.ReturnMsg;
import com.lidennis.plat.common.model.UserContext;
import com.lidennis.plat.common.service.AbstractBaseService;
import com.lidennis.plat.config.model.ConfigConnServer;
import com.lidennis.plat.config.model.ConfigCronJob;

public class ConfigConnServerServiceImpl extends AbstractBaseService
{

    @Override
    protected <T> ReturnMsg checkByDelete( UserContext userContext, final T model, Map<String, Object> exparamMap ) throws Exception
    {
        ReturnMsg returnMsg = super.checkByDelete( userContext, model, exparamMap );
        if( returnMsg.isSuccess() )
        {
            ConfigConnServer dataObj = (ConfigConnServer)model;
            Map<String,Object> params01Map = new HashMap<String,Object>();
            params01Map.put( "serverCode", dataObj.getItemCode() );
            int num = baseIbatisDao.findCountByParams( ConfigCronJob.class.getSimpleName(), params01Map );
            if( num > 0 )
            {
                returnMsg.setSuccess( false );
                returnMsg.setMessage( "数据["+dataObj.getItemName()+"]已被定时任务引用, 不能删除." );
                returnMsg.setEnMessage( "The data["+dataObj.getItemName()+"] has been referenced the timing tasks, can not be deleted." );
                return returnMsg;
            }
        }
        
        return returnMsg;
    }

}
