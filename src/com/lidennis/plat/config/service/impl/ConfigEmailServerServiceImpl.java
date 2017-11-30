package com.lidennis.plat.config.service.impl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.lidennis.plat.common.constant.Constants;
import com.lidennis.plat.common.model.ReturnMsg;
import com.lidennis.plat.common.model.UserContext;
import com.lidennis.plat.common.service.AbstractBaseService;
import com.lidennis.plat.common.service.ICacheService;
import com.lidennis.plat.common.util.DataTypeUtils;
import com.lidennis.plat.common.util.StringUtils;
import com.lidennis.plat.config.model.ConfigDataTemp;
import com.lidennis.plat.config.model.ConfigEmailInfo;
import com.lidennis.plat.config.model.ConfigEmailServer;
import com.lidennis.plat.system.constant.SystemConstants;
import com.lidennis.plat.system.util.EmailSendUtils;

public class ConfigEmailServerServiceImpl extends AbstractBaseService implements ICacheService
{
    private static final Logger log = Logger.getLogger( ConfigEmailServerServiceImpl.class );
    protected Map<String,Object> globalAppConfigBaseMap; 

    @Override
    public <T> ReturnMsg execExtend( UserContext ucontext, T dataObj, Map<String, Object> paramMap ) throws Exception
    {
        ReturnMsg returnMsg = new ReturnMsg( false, Constants.MSG_OPERATE_FAILED, Constants.MSG_OPERATE_FAILED_EN );
        try
        {
            if( dataObj != null && ucontext != null )
            {
                ConfigEmailServer cobj = (ConfigEmailServer)dataObj;
                if( DataTypeUtils.checkEqualsNull( ucontext.getCzId(), cobj.getCzId() ) || StringUtils.checkEquals( ucontext.getUserAcct(), Constants.COMMON_MODEL_CODE_SENIOR ) )
                {
                    ConfigEmailInfo emailInfo = null;
                    Map<String, Object> ftlmap = new HashMap<String, Object>();
                    ftlmap.put( Constants.COMMON_MODEL_CODE_CZ_ID, ucontext.getCzId() );
                    ftlmap.put( "bizCode", "email_test" );
                    ftlmap.put( Constants.COMMON_MODEL_CODE_TYPE_CODE, "email" );
                    List<ConfigDataTemp> tempList = baseIbatisDao.findByParams( ConfigDataTemp.class.getSimpleName(), ftlmap, null );
                    if( tempList != null && tempList.size() > 0 )
                        emailInfo = EmailSendUtils.buildEmailInfoByTemp( cobj, tempList.get(0), null );
                    if( emailInfo == null )
                    {
                        emailInfo = new ConfigEmailInfo();
                        emailInfo.setItemSubject( "这是一封系统测试邮件, 请勿回复" );
                        emailInfo.setItemContent( "这是一封系统测试邮件, 请勿回复" );
                    }
                    
                    if( cobj.getManagerEmail() != null && !cobj.getManagerEmail().trim().equals( "" ) )
                        emailInfo.setToEmail( cobj.getManagerEmail() );
                    else
                        emailInfo.setToEmail( cobj.getSenderMail() );
                    if( log.isDebugEnabled() )
                        log.debug( emailInfo.getToEmail() + " | " + emailInfo.getItemSubject() + " | " + emailInfo.getItemContent() );
                    returnMsg = EmailSendUtils.sendRealtime( cobj, emailInfo );
                }
                else
                {
                    returnMsg.setSuccess( false );
                    returnMsg.setMessage( Constants.MSG_OPERATE_FAILED_PREFIX + "找不到相应的配置." );
                    returnMsg.setEnMessage( Constants.MSG_OPERATE_FAILED_PREFIX_EN + "can not find the configuration" );
                }
            }
        }
        catch( Exception err )
        {
            returnMsg.setSuccess( false );
            returnMsg.setMessage( "连接出错, "+err.getMessage() );
            returnMsg.setEnMessage( "Connection error, "+err.getMessage() );
            log.error( "connectTest error, "+err.getMessage(), err );
        }
        
        return returnMsg;
    }
    
    @SuppressWarnings("unchecked")
    protected <T> ReturnMsg checkBySave( UserContext ucontext, final T model, Map<String, Object> exparamMap ) throws Exception
    {
        ReturnMsg returnMsg = super.checkBySave( ucontext, model, exparamMap );
        if( returnMsg.isSuccess() )
        {
            
        }
        
        return returnMsg;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public void reloadCacheData()
    {
        if( globalAppConfigBaseMap != null )
        {
            /*
            Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put( "enableFlag", Constants.BOOLEAN_VALUE_TRUE );
            List<ConfigEmailServer> resultList = baseIbatisDao.findByParams( ConfigEmailServer.class.getSimpleName(), paramMap, null );
            synchronized( globalAppConfigBaseMap )
            {
                String dkey = ConfigEmailServer.class.getSimpleName();
                if( globalAppConfigBaseMap.containsKey( dkey ) )
                    globalAppConfigBaseMap.remove( dkey );
                Map<String,Object> dataMap = new ConcurrentHashMap<String,Object>();
                if( resultList != null && resultList.size() > 0 )
                {
                    for( int i=0; i<resultList.size(); i++ )
                    {
                        ConfigEmailServer dataObj = resultList.get(i);
                        if( StringUtils.checkIsEmpty( dataObj.getCzCode() 要用 getCzId() ) )
                        {
                            if( StringUtils.checkNotEmpty( dataObj.getItemCode() ) )
                                dataMap.put( dataObj.getItemCode(), dataObj );
                            dataMap.put( dataObj.getItemId().toString(), dataObj );
                        }
                        else
                        {
                            if( StringUtils.checkNotEmpty( dataObj.getItemCode() ) )
                                dataMap.put( dataObj.getCzCode()+"_"+dataObj.getItemCode(), dataObj );
                            dataMap.put( dataObj.getCzCode()+"_"+dataObj.getItemId().toString(), dataObj );
                        }
                    }
                    dataMap.put( "all_result_data_list", resultList );
                }
                
                globalAppConfigBaseMap.put( dkey, dataMap );
                if( SystemConstants.LOG_SWITCH_PROC_DEBUG_ON && SystemConstants.LOG_SWITCH_PACKAGE_INFO.length() >= 15 && SystemConstants.LOG_SWITCH_PACKAGE_INFO.indexOf( this.getClass().getPackage().getName() ) >= 0 )
                    log.info( "--> load email server config to cache, total: "+dataMap.size() );
            }
            */
        }
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> T findCacheById( UserContext ucontext, Long itemId )
    {
        ConfigEmailServer dataObj = null;
        String dkey = ConfigEmailServer.class.getSimpleName();
        if( globalAppConfigBaseMap.containsKey( dkey ) )
        {
            /*
            Map<String,Object> dataMap = (Map<String,Object>)globalAppConfigBaseMap.get( dkey );
            if( ucontext != null && itemId != null )
            {
                要用 getCzId()
                String key = ucontext.getCzCode()+"_"+itemId.toString();
                if( ucontext.getUserAcct() != null && ucontext.getUserAcct().equals(Constants.COMMON_MODEL_CODE_SENIOR) )
                {
                    if( dataMap.containsKey( itemId.toString() ) )
                    {
                        dataObj = (ConfigEmailServer)dataMap.get( itemId.toString() );
                    }
                    else
                    {
                        if( dataMap.containsKey( key ) )
                            dataObj = (ConfigEmailServer)dataMap.get( key );
                    }
                }
                else if( ucontext.getCzCode() != null && ucontext.getCzCode().equals(Constants.COMMON_CODE_SYSTEM) )
                {
                    if( dataMap.containsKey( itemId.toString() ) )
                    {
                        dataObj = (ConfigEmailServer)dataMap.get( itemId.toString() );
                    }
                    else
                    {
                        if( dataMap.containsKey( key ) )
                            dataObj = (ConfigEmailServer)dataMap.get( key );
                    }
                }
                else
                {
                    if( dataMap.containsKey( key ) )
                        dataObj = (ConfigEmailServer)dataMap.get( key );
                }
            }
            */
        }
        
        if( dataObj != null )
            return (T)dataObj;
        else
            return null;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> T findCacheByCode( UserContext ucontext, String itemCode )
    {
        ConfigEmailServer dataObj = null;
        String dkey = ConfigEmailServer.class.getSimpleName();
        if( globalAppConfigBaseMap.containsKey( dkey ) )
        {
            /*
            Map<String,Object> dataMap = (Map<String,Object>)globalAppConfigBaseMap.get( dkey );
            if( ucontext != null && itemCode != null )
            {
                  要用 getCzId()
                String key = ucontext.getCzCode()+"_"+itemCode;
                if( ucontext.getUserAcct() != null && ucontext.getUserAcct().equals(Constants.COMMON_MODEL_CODE_SENIOR) )
                {
                    if( dataMap.containsKey( itemCode ) )
                    {
                        dataObj = (ConfigEmailServer)dataMap.get( itemCode );
                    }
                    else
                    {
                        if( dataMap.containsKey( key ) )
                            dataObj = (ConfigEmailServer)dataMap.get( key );
                    }
                }
                else if( ucontext.getCzCode() != null && ucontext.getCzCode().equals(Constants.COMMON_CODE_SYSTEM) )
                {
                    if( dataMap.containsKey( itemCode ) )
                    {
                        dataObj = (ConfigEmailServer)dataMap.get( itemCode );
                    }
                    else
                    {
                        if( dataMap.containsKey( key ) )
                            dataObj = (ConfigEmailServer)dataMap.get( key );
                    }
                }
                else
                {
                    if( dataMap.containsKey( key ) )
                        dataObj = (ConfigEmailServer)dataMap.get( key );
                }
            }
            */
        }
        
        if( dataObj != null )
            return (T)dataObj;
        else
            return null;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> findCacheListByParams( UserContext ucontext, Map<String, Object> paramMap )
    {
        List resultList = new ArrayList();
        if( ucontext != null && ucontext.getCzCode() != null && !ucontext.getCzCode().equals("") )
        {
            /*
            String dkey = ConfigEmailServer.class.getSimpleName();
            if( globalAppConfigBaseMap.containsKey( dkey ) )
            {
                Map<String,Object> dataMap = (Map<String,Object>)globalAppConfigBaseMap.get( dkey );
                if( dataMap.containsKey("all_result_data_list") )
                {
                    List<ConfigEmailServer> dataList = (List<ConfigEmailServer>)dataMap.get( "all_result_data_list" );
                    for( int i=0; i<dataList.size(); i++ )
                    {
                        ConfigEmailServer dataObj = dataList.get(i);
                        要用 getCzId()
                        if( ucontext.getCzCode().equals( dataObj.getCzCode() ) )
                        {
                            resultList.add( dataObj );
                        }
                    }
                }
            }
            */
        }
        return resultList;
    }

    public void setGlobalAppConfigBaseMap(Map<String, Object> globalAppConfigBaseMap)
    {
        this.globalAppConfigBaseMap = globalAppConfigBaseMap;
    }
}
