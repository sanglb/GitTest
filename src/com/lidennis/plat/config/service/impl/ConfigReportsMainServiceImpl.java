package com.lidennis.plat.config.service.impl;
import java.util.HashMap;
import java.util.Map;

import com.lidennis.plat.common.constant.Constants;
import com.lidennis.plat.common.model.ReturnMsg;
import com.lidennis.plat.common.model.UserContext;
import com.lidennis.plat.common.service.AbstractLevelService;
import com.lidennis.plat.config.model.ConfigReportsItem;
import com.lidennis.plat.config.model.ConfigReportsMain;
import com.lidennis.plat.config.service.IConfigReportsMainService;

public class ConfigReportsMainServiceImpl extends AbstractLevelService implements IConfigReportsMainService
{
    @Override
    protected <T> ReturnMsg checkBySave( UserContext userContext, final T model, Map<String, Object> exparamMap ) throws Exception
    {
        ReturnMsg returnMsg = super.checkBySave( userContext, model, exparamMap );
        if( returnMsg.isSuccess() )
        {
            ConfigReportsMain dataObj = (ConfigReportsMain)model;
            if( dataObj.getAppId() == null || dataObj.getAppId().longValue() <= 0 )
            {
                returnMsg.setSuccess( false );
                returnMsg.setMessage( Constants.MSG_DATA_NOT_COMPLETE_PARAM );
                returnMsg.setEnMessage( Constants.MSG_DATA_NOT_COMPLETE_PARAM_EN );
                return returnMsg;
            }
            
            if( dataObj.getItemName() == null || dataObj.getItemName().trim().equals("") )
            {
                returnMsg.setSuccess( false );
                returnMsg.setMessage( Constants.MSG_NAME_NO_EMPTY );
                returnMsg.setEnMessage( Constants.MSG_NAME_NO_EMPTY_EN );
                return returnMsg;
            }
            else
            {
                dataObj.setItemName( dataObj.getItemName().trim() );
            }
            
            Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put( "parentId", dataObj.getParentId() );
            paramMap.put( "appId", dataObj.getAppId() );
            paramMap.put( "itemName", dataObj.getItemName() );
            if( dataObj.getItemId() != null && dataObj.getItemId().longValue() > 0 )
                paramMap.put( "itemId_notexist", dataObj.getItemId() );
            int num = this.findCountByParams( userContext, paramMap );
            if( num > 0 )
            {
                returnMsg.setSuccess( false );
                returnMsg.setMessage( Constants.MSG_NOT_REPEATE_SAME_NODE_NAME );
                returnMsg.setEnMessage( Constants.MSG_NOT_REPEATE_SAME_NODE_NAME_EN );
                return returnMsg;
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
            ConfigReportsMain dataObj = (ConfigReportsMain)model;
            if( !userContext.getCzCode().equals( dataObj.getCzCode() ) )
            {
                returnMsg.setSuccess( false );
                returnMsg.setMessage( Constants.MSG_DATA_NOT_COMPLETE_INPUT );
                returnMsg.setEnMessage( Constants.MSG_DATA_NOT_COMPLETE_INPUT_EN );
                return returnMsg;
            }
            
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put( "reportsId", dataObj.getItemId() );
            int num = baseIbatisDao.findCountByParams( ConfigReportsItem.class.getSimpleName(), paramMap );
            if( num > 0 )
            {
                returnMsg.setSuccess( false );
                returnMsg.setMessage( "该数据关联了"+num+"个子报表, 不能删除" );
                returnMsg.setEnMessage( "the data associated with "+num+" sub-reports, can not be deleted" );
                return returnMsg;
            }
        }
        
        return returnMsg;
    }
}
