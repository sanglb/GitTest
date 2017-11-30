package com.lidennis.plat.config.service.impl;
import java.util.HashMap;
import java.util.Map;

import com.lidennis.plat.common.constant.Constants;
import com.lidennis.plat.common.model.ReturnMsg;
import com.lidennis.plat.common.model.UserContext;
import com.lidennis.plat.common.service.AbstractBaseService;
import com.lidennis.plat.config.model.ConfigCronJob;
import com.lidennis.plat.config.model.ConfigProcMapping;

public class ConfigProcMappingServiceImpl extends AbstractBaseService
{
    @Override
    protected <T> ReturnMsg checkBySave( UserContext userContext, final T model, Map<String, Object> exparamMap ) throws Exception
    {
        ReturnMsg returnMsg = super.checkBySave( userContext, model, exparamMap );
        if( returnMsg.isSuccess() )
        {
            if( model instanceof ConfigProcMapping )
            {
                ConfigProcMapping dataObj = (ConfigProcMapping)model;
                if( dataObj.getItemCode() == null || dataObj.getItemCode().trim().equals("") )
                {
                    returnMsg.setSuccess( false );
                    returnMsg.setMessage( Constants.MSG_CODE_NO_EMPTY );
                    returnMsg.setEnMessage( Constants.MSG_CODE_NO_EMPTY_EN );
                    return returnMsg;
                }
                
                if( dataObj.getTypeCode() == null || dataObj.getTypeCode().trim().equals("") )
                {
                    returnMsg.setSuccess( false );
                    returnMsg.setMessage( "类型不能为空." );
                    returnMsg.setEnMessage( "the type can not be empty." );
                    return returnMsg;
                }
            }
        }
        
        return returnMsg;
    }
    
    @Override
    protected <T> ReturnMsg checkByDelete( UserContext userContext, final T model, Map<String, Object> exparamMap ) throws Exception
    {
        ReturnMsg returnMsg = super.checkByDelete( userContext, model, exparamMap );
        if( returnMsg.isSuccess() )
        {
            if( model instanceof ConfigProcMapping )
            {
                ConfigProcMapping dataObj = (ConfigProcMapping)model;
                Map<String,Object> params01Map = new HashMap<String,Object>();
                params01Map.put( "procCode", dataObj.getItemCode() );
                int num = baseIbatisDao.findCountByParams( ConfigCronJob.class.getSimpleName(), params01Map );
                if( num > 0 )
                {
                    returnMsg.setSuccess( false );
                    returnMsg.setMessage( "数据["+dataObj.getItemName()+"]已被定时任务引用, 不能删除." );
                    returnMsg.setEnMessage( "The data["+dataObj.getItemName()+"] has been referenced timing tasks, can not be deleted." );
                    return returnMsg;
                }
            }
        }
        
        return returnMsg;
    }
}
