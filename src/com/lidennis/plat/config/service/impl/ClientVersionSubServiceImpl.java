package com.lidennis.plat.config.service.impl;
import java.util.HashMap;
import java.util.Map;

import com.lidennis.plat.common.constant.Constants;
import com.lidennis.plat.common.model.ReturnMsg;
import com.lidennis.plat.common.model.UserContext;
import com.lidennis.plat.common.service.AbstractBaseService;
import com.lidennis.plat.common.util.DataTypeUtils;
import com.lidennis.plat.common.util.StringUtils;
import com.lidennis.plat.config.model.ClientVersionMain;
import com.lidennis.plat.config.model.ClientVersionSub;
import com.lidennis.plat.config.service.IClientVersionSubService;

public class ClientVersionSubServiceImpl extends AbstractBaseService implements IClientVersionSubService
{
    @Override
    protected <T> ReturnMsg checkBySave( UserContext ucontext, final T model, Map<String, Object> exparamMap ) throws Exception
    {
        ReturnMsg returnMsg = super.checkBySave( ucontext, model, exparamMap );
        if( returnMsg.isSuccess() )
        {
            ClientVersionSub dataObj = (ClientVersionSub)model;
            if( dataObj.getItemName() == null || dataObj.getItemName().trim().equals("") )
            {
                returnMsg.setSuccess( false );
                returnMsg.setMessage( Constants.MSG_NAME_NO_EMPTY );
                returnMsg.setEnMessage( Constants.MSG_NAME_NO_EMPTY_EN );
                return returnMsg;
            }
            if( dataObj.getMainId() == null || dataObj.getMainId().longValue() <= 0 )
            {
                returnMsg.setSuccess( false );
                returnMsg.setMessage( "主版本信息不能为空." );
                returnMsg.setEnMessage( "main version information can not be empty." );
                return returnMsg;
            }
            if( dataObj.getVersionNum() == null || dataObj.getVersionNum().intValue() <= 0 )
            {
                returnMsg.setSuccess( false );
                returnMsg.setMessage( "版本号不能为空." );
                returnMsg.setEnMessage( "version number can not be empty." );
                return returnMsg;
            }
            if( StringUtils.checkIsEmpty( dataObj.getClientType() ) )
            {
                returnMsg.setSuccess( false );
                returnMsg.setMessage( "客户端类型不能为空." );
                returnMsg.setEnMessage( "client type can not be empty." );
                return returnMsg;
            }
            
            /* 在保存文件之前就已经检查过了
            Map<String,Object> pmap = new HashMap<String,Object>();
            pmap.put( "mainId", dataObj.getMainId() );
            pmap.put( "versionNum", dataObj.getVersionNum() );
            if( dataObj.getItemId() != null && dataObj.getItemId().longValue() > 0 )
                pmap.put( "itemId_notexist", dataObj.getItemId() );
            int num = baseIbatisDao.findCountByParams( ClientVersionSub.class.getSimpleName(), pmap );
            if( num > 0 )
            {
                returnMsg.setSuccess( false );
                returnMsg.setMessage( "该版本已经存在." );
                returnMsg.setEnMessage( "this version already exists." );
                return returnMsg;
            }
            */
        }
        
        return returnMsg;
    }
    
    @Override
    protected <T> ReturnMsg checkByDelete( UserContext ucontext, final T model, Map<String, Object> exparamMap ) throws Exception
    {
        ReturnMsg returnMsg = super.checkByDelete( ucontext, model, null );
        if( returnMsg.isSuccess() )
        {
            ClientVersionSub dataObj = (ClientVersionSub)model;
            if( !DataTypeUtils.checkEquals( dataObj.getEnableFlag(), Constants.BOOLEAN_VALUE_FALSE ) )
            {
                returnMsg.setSuccess( false );
                returnMsg.setMessage( "已启用的数据不能删除." );
                returnMsg.setEnMessage( "enabled data cannot be deleted." );
                return returnMsg;
            }
        }
        
        return returnMsg;
    }
    
    @SuppressWarnings("unchecked")
    protected <T> void opCallback( UserContext ucontext, String optype, final T model, Map<String, Object> exparamMap ) throws Exception
    {
        if( StringUtils.checkEquals( optype, Constants.OPERATE_TYPE_INSERT ) || StringUtils.checkEquals( optype, Constants.OPERATE_TYPE_DELETE ) )
        {
            ClientVersionSub dataObj = (ClientVersionSub)model;
            ClientVersionMain mainObj = baseIbatisDao.findById( ClientVersionMain.class.getSimpleName(), dataObj.getMainId() );
            if( mainObj != null )
            {
                Map<String,Object> pmap = new HashMap<String,Object>();
                pmap.put( "mainId", dataObj.getMainId() );
                int num = baseIbatisDao.findCountByParams( ClientVersionSub.class.getSimpleName(), pmap );
                if( !DataTypeUtils.checkEquals( mainObj.getSubCount(), num ) )
                {
                    mainObj.setSubCount( num );
                    baseIbatisDao.update( ClientVersionMain.class.getSimpleName(), mainObj );
                }
            }
        }
    }
}
