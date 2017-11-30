package com.lidennis.plat.config.service.impl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.ehcache.Cache;
import net.sf.json.JSONObject;

import org.apache.http.client.HttpClient;
import org.apache.log4j.Logger;

import com.lidennis.plat.common.constant.Constants;
import com.lidennis.plat.common.model.AbstractBaseModel;
import com.lidennis.plat.common.model.OrderByModel;
import com.lidennis.plat.common.model.ReturnMsg;
import com.lidennis.plat.common.model.UserContext;
import com.lidennis.plat.common.service.AbstractBaseService;
import com.lidennis.plat.common.service.ICacheService;
import com.lidennis.plat.common.service.IEncacheService;
import com.lidennis.plat.common.util.DataOperateUtils;
import com.lidennis.plat.common.util.DataTypeUtils;
import com.lidennis.plat.common.util.DatetimeUtils;
import com.lidennis.plat.common.util.StringUtils;
import com.lidennis.plat.config.model.ConfigAppParam;
import com.lidennis.plat.config.service.IConfigAppParamService;
import com.lidennis.plat.system.constant.SystemConstants;
import com.lidennis.plat.system.iface.ISyncDataService;

public class ConfigAppParamServiceImpl extends AbstractBaseService implements IConfigAppParamService, ICacheService, ISyncDataService
{
    private static final Logger log = Logger.getLogger(ConfigAppParamServiceImpl.class);
    protected IEncacheService encacheService;
    
    
    @Override
    public ReturnMsg syncDataProc( UserContext ucontext, HttpClient httpClient, String syncType, AbstractBaseModel bizObj, Map<String, Object> dataMap ) throws Exception 
    {
        ReturnMsg rmsg = new ReturnMsg( false );
        if( ucontext != null && syncType != null && syncType.length() > 2 )
        {
            if( syncType.indexOf( "sync_cfg_params_by_web" ) == 0 )
            {
                rmsg.setMessage( Constants.MSG_OPERATE_FAILED_PREFIX + "请检查参数是否正确" );
                rmsg.setEnMessage( Constants.MSG_OPERATE_FAILED_PREFIX + "please check parameters." );
                String cfgcode1 = DataOperateUtils.findStringByMapKey( dataMap, "cfgcode1" );
                if( DataTypeUtils.checkEquals( ucontext.getManageFlag(), Constants.BOOLEAN_VALUE_TRUE ) && cfgcode1.length() < 50 && cfgcode1.indexOf( "cfgitem_" ) == 0 )
                {
                    int num1 = 0;
                    String name1  = DataOperateUtils.findStringByMapKey( dataMap, "cfginfo1" );  
                    String flag1  = DataOperateUtils.findStringByMapKey( dataMap, "cfgflag1" );  
                    Map<String,Object> pmap2 = new HashMap<String,Object>();
                    pmap2.put( Constants.COMMON_MODEL_CODE_CODE, cfgcode1 );
                    pmap2.put( Constants.COMMON_MODEL_CODE_CZ_ID, ucontext.getCzId() );
                    ConfigAppParam dobj1 = null;
                    List<ConfigAppParam> dlist2 = baseIbatisDao.findByParams( ConfigAppParam.class.getSimpleName(), 0, 1, pmap2, new OrderByModel( "ITEMID" ) );
                    if( dlist2 != null && dlist2.size() == 1 )
                    {
                        ConfigAppParam tmp2 = dlist2.get(0);
                        if( DataTypeUtils.checkEquals( tmp2.getCzId(), ucontext.getCzId() ) )
                        {
                            dobj1 = tmp2;
                            if( name1 != null && name1.length() > 1 && name1.length() < 50 )
                                dobj1.setItemName( name1 );
                            log.info( "ready to update params: " + cfgcode1 + "" );
                        }
                    }
                    
                    if( dobj1 == null ) 
                    {
                        dobj1 = new ConfigAppParam();
                        dobj1.setAppMode( "app" );
                        dobj1.setItemCode( cfgcode1 );
                        if( name1 != null && name1.length() > 1 && name1.length() < 50 )
                            dobj1.setItemName( name1 );
                        else
                            dobj1.setItemName( "配置数据 " + DatetimeUtils.convertToDateStr( System.currentTimeMillis() ) );
                        dobj1.setCzId( ucontext.getCzId() );
                        dobj1.setEnableFlag( Constants.BOOLEAN_VALUE_TRUE );
                        dobj1.setCreateTime( System.currentTimeMillis() );
                        dobj1.setCreateUser( ucontext.getUserAcct() );
                    }
                    dataMap.put( "syncTime1", DatetimeUtils.convertToDatetimeStr( System.currentTimeMillis() ) );
                    dataMap.put( "syncUser1", ucontext.getUserAcct() );
                    dobj1.setItemType( "json" );
                    dobj1.setItemValue( JSONObject.fromObject( dataMap ).toString() );
                    if( DataTypeUtils.checkStrIsNumber( flag1 ) && flag1.length() < 5 )
                        dobj1.setEnableFlag( Integer.valueOf( flag1 ) );
                    dobj1.setUpdateTime( System.currentTimeMillis() );
                    dobj1.setUpdateUser( ucontext.getUserAcct() );
                    num1 = 0;
                    if( dobj1.getItemId() == null || dobj1.getItemId().longValue() <= 0 ) 
                        num1 = baseIbatisDao.insert( ConfigAppParam.class.getSimpleName(), dobj1 );
                    else
                        num1 = baseIbatisDao.update( ConfigAppParam.class.getSimpleName(), dobj1 );
                    if( num1 > 0 && dobj1.getItemId() != null )
                    {
                        rmsg.setSuccess( true );
                        rmsg.setResultValue( dobj1.getItemId().toString() );
                    }
                }
            }
            else if( syncType.indexOf( "xxxxxx" ) >= 0 )
            {
                
            }
        }
        
        return rmsg;
    }
    
    
    @Override
    @SuppressWarnings("unchecked")
    public List findDataByBizCode( UserContext ucontext, String bizCode, String acctCode, String dataCode, Map<String,Object> paramMap )
    {
        List dlist1 = null;
        if( ucontext != null && bizCode != null && bizCode.length() > 2 )
        {
            if( bizCode.indexOf( "sync_cfg_params_by_web" ) == 0 )
            {
                dlist1 = new ArrayList<Map<String,Object>>();
                if( DataTypeUtils.checkEquals( ucontext.getManageFlag(), Constants.BOOLEAN_VALUE_TRUE ) && dataCode != null && dataCode.length() >= 5 && dataCode.length() < 50 )
                {
                    dataCode = dataCode.toLowerCase().trim();
                    Map<String,Object> pmap2 = new HashMap<String,Object>();
                    pmap2.put( Constants.COMMON_MODEL_CODE_CODE, dataCode );
                    pmap2.put( Constants.COMMON_MODEL_CODE_CZ_ID, ucontext.getCzId() );
                    List<ConfigAppParam> dlist2 = baseIbatisDao.findByParams( ConfigAppParam.class.getSimpleName(), 0, 1, pmap2, new OrderByModel( "ITEMID" ) );
                    if( dlist2 != null && dlist2.size() == 1 )
                    {
                        ConfigAppParam dobj2 = dlist2.get(0);
                        if( DataTypeUtils.checkEquals( dobj2.getCzId(), ucontext.getCzId() ) )
                        {
                            Map<String,Object> dmap2 = DataOperateUtils.convertJsonStrToMap( dobj2.getItemValue() );
                            if( dmap2 == null ) 
                            {
                                dmap2 = new HashMap<String,Object>();
                            }
                            dmap2.put( "cfginfo1", dobj2.getItemName() );
                            dmap2.put( "cfgflag1", dobj2.getEnableFlag() );
                            dmap2.put( "createTime", dobj2.getCreateTime() );
                            dlist1.add( dmap2 );
                        }
                    }
                }
            }
            else if( bizCode.indexOf( "xxxxxx" ) >= 0 )
            {
                
            }
        }
        
        return dlist1;
    }
    
    @SuppressWarnings("unchecked")
    protected <T> ReturnMsg checkBySave( UserContext ucontext, final T model, Map<String, Object> exparamMap ) throws Exception
    {
        ReturnMsg returnMsg = super.checkBySave( ucontext, model, exparamMap );
        if( returnMsg.isSuccess() )
        {
            ConfigAppParam dataObj = (ConfigAppParam)model;
            if( StringUtils.checkEquals( dataObj.getAppMode(), Constants.COMMON_CODE_SYSTEM ) )
            {
                Map<String,Object> pmap1 = new HashMap<String,Object>();
                pmap1.put( "appMode", Constants.COMMON_CODE_SYSTEM );
                pmap1.put( "itemCode", dataObj.getItemCode() );
                if( dataObj.getItemId() != null && dataObj.getItemId().longValue() > 0 )
                    pmap1.put( "itemId_notexist", dataObj.getItemId() );
                int num = baseIbatisDao.findCountByParams( ConfigAppParam.class.getSimpleName(), pmap1 );
                if( num > 0 )
                {
                    returnMsg.setSuccess( false );
                    returnMsg.setMessage( Constants.MSG_CODE_ALREADY_EXIST );
                    returnMsg.setEnMessage( Constants.MSG_CODE_ALREADY_EXIST_EN );
                    return returnMsg;
                }
            }
        }
        
        return returnMsg;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public void reloadCacheData()
    {
        String ckey = SystemConstants.CACHE_NAME_CONFIG_DATA;
        String okey = ConfigAppParam.class.getSimpleName();
        Cache cache = encacheService.findCache( ckey );
        if( cache != null )
        {
            Object obj = encacheService.findData( cache, okey );
            Map<String,Object> dataMap = null;
            if( obj == null )
            {
                dataMap = new ConcurrentHashMap<String,Object>();
                encacheService.putData( cache, okey, dataMap );
            }
            else
            {
                dataMap = (Map<String,Object>)obj;
                dataMap.clear();
            }
            
            Map<String,Object> pmap = new HashMap<String,Object>();
            List<ConfigAppParam> dlist = baseIbatisDao.findByParams( ConfigAppParam.class.getSimpleName(), pmap, null );
            if( dlist != null && dlist.size() > 0 )
            {
                SystemConstants.LOG_SWITCH_SEARCH_DEBUG_ON     = false; 
                SystemConstants.LOG_SWITCH_SEARCH_INFO_ON      = false; 
                SystemConstants.LOG_SWITCH_MSG_DEBUG_ON        = false; 
                SystemConstants.LOG_SWITCH_MSG_INFO_ON         = false; 
                SystemConstants.LOG_SWITCH_MAIN_BIZ_DEBUG_ON   = false;
                SystemConstants.LOG_SWITCH_MAIN_BIZ_INFO_ON    = false; 
                SystemConstants.LOG_SWITCH_PROC_DEBUG_ON       = false; 
                SystemConstants.LOG_SWITCH_PROC_INFO_ON        = false; 
                Constants.LOG_SWITCH_C_PROC_DEBUG_ON           = false;
                Constants.LOG_SWITCH_C_PROC_INFO_ON            = false;
                SystemConstants.LOG_SWITCH_OTHER_DEBUG_ON      = false; 
                SystemConstants.LOG_SWITCH_OTHER_INFO_ON       = false; 
                SystemConstants.LOG_SWITCH_PACKAGE_INFO        = "";
                for( int i=0; i<dlist.size(); i=i+1 )
                {
                    ConfigAppParam dobj = dlist.get(i);
                    dataMap.put( dobj.getItemId().toString(), dobj );
                    if( StringUtils.checkNotEmpty( dobj.getItemCode() ) )
                    {
                        if( StringUtils.checkEquals( dobj.getAppMode(), Constants.COMMON_CODE_SYSTEM ) )
                        {
                            dataMap.put( dobj.getItemCode(), dobj );
                            if( dobj.getItemValue() != null && dobj.getItemValue().length() > 2 )
                            {
                                if( StringUtils.checkEquals( dobj.getItemCode(), "system_log_open_config" ) )
                                {
                                    if( dobj.getItemValue().indexOf( "search_debug" ) >= 0 )
                                    {
                                        SystemConstants.LOG_SWITCH_SEARCH_DEBUG_ON = true; 
                                        SystemConstants.LOG_SWITCH_SEARCH_INFO_ON  = true; 
                                    }
                                    if( dobj.getItemValue().indexOf( "msg_debug" ) >= 0 )
                                    {
                                        SystemConstants.LOG_SWITCH_MSG_DEBUG_ON = true; 
                                        SystemConstants.LOG_SWITCH_MSG_INFO_ON  = true; 
                                    }
                                    if( dobj.getItemValue().indexOf( "biz_debug" ) >= 0 )
                                    {
                                        SystemConstants.LOG_SWITCH_MAIN_BIZ_DEBUG_ON = true; 
                                        SystemConstants.LOG_SWITCH_MAIN_BIZ_INFO_ON  = true; 
                                    }
                                    if( dobj.getItemValue().indexOf( "proc_debug" ) >= 0 )
                                    {
                                        SystemConstants.LOG_SWITCH_PROC_DEBUG_ON = true; 
                                        SystemConstants.LOG_SWITCH_PROC_INFO_ON  = true; 
                                        Constants.LOG_SWITCH_C_PROC_DEBUG_ON     = true;
                                        Constants.LOG_SWITCH_C_PROC_INFO_ON      = true;
                                    }
                                    if( dobj.getItemValue().indexOf( "other_debug" ) >= 0 )
                                    {
                                        SystemConstants.LOG_SWITCH_OTHER_DEBUG_ON = true; 
                                        SystemConstants.LOG_SWITCH_OTHER_INFO_ON  = true; 
                                    }
                                    if( dobj.getItemValue().indexOf( "search_info" ) >= 0 )
                                        SystemConstants.LOG_SWITCH_SEARCH_INFO_ON = true; 
                                    if( dobj.getItemValue().indexOf( "msg_info" ) >= 0 )
                                        SystemConstants.LOG_SWITCH_MSG_INFO_ON = true; 
                                    if( dobj.getItemValue().indexOf( "biz_info" ) >= 0 )
                                        SystemConstants.LOG_SWITCH_MAIN_BIZ_INFO_ON = true; 
                                    if( dobj.getItemValue().indexOf( "proc_info" ) >= 0 )
                                    {
                                        SystemConstants.LOG_SWITCH_PROC_INFO_ON = true; 
                                        Constants.LOG_SWITCH_C_PROC_INFO_ON     = true;
                                    }
                                    if( dobj.getItemValue().indexOf( "other_info" ) >= 0 )
                                        SystemConstants.LOG_SWITCH_OTHER_INFO_ON = true; 
                                }
                                if( StringUtils.checkEquals( dobj.getItemCode(), "system_log_package_config" ) ) 
                                {
                                    SystemConstants.LOG_SWITCH_PACKAGE_INFO = dobj.getItemValue();
                                }
                            }
                        }
                        else if( dobj.getCzId() != null && dobj.getCzId().longValue() > 0 )
                        {
                            dataMap.put( dobj.getCzId() + "__" + dobj.getItemCode(), dobj );
                        }
                    }
                }
                
                dataMap.put( SystemConstants.CACHE_DATA_KEY_ALL_LIST, dlist );
            }
            
            if( SystemConstants.LOG_SWITCH_PROC_DEBUG_ON && SystemConstants.LOG_SWITCH_PACKAGE_INFO.length() >= 15 && SystemConstants.LOG_SWITCH_PACKAGE_INFO.indexOf( this.getClass().getPackage().getName() ) >= 0 )
                log.info( "--> load app parameter to cache, total: " + dataMap.size() );
        }
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> T findCacheById( UserContext ucontext, Long itemId )
    {
        ConfigAppParam dataObj = null;
        if( ucontext != null && itemId != null )
        {
            String ckey = SystemConstants.CACHE_NAME_CONFIG_DATA;
            String okey = ConfigAppParam.class.getSimpleName();
            Cache cache = encacheService.findCache( ckey );
            if( cache != null )
            {
                Object obj = encacheService.findData( cache, okey );
                if( obj == null )
                {
                    reloadCacheData();
                    obj = encacheService.findData( cache, okey );
                }
                if( obj != null )
                {
                    Map<String,Object> dataMap = (Map<String,Object>)obj;
                    if( dataMap.containsKey( itemId.toString() ) )
                        dataObj = (ConfigAppParam)dataMap.get( itemId.toString() );
                }
            }
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
        ConfigAppParam dataObj = null;
        if( ucontext != null && StringUtils.checkNotEmpty( itemCode ) )
        {
            String ckey = SystemConstants.CACHE_NAME_CONFIG_DATA;
            String okey = ConfigAppParam.class.getSimpleName();
            Cache cache = encacheService.findCache( ckey );
            if( cache != null )
            {
                Object obj = encacheService.findData( cache, okey );
                if( obj == null )
                {
                    reloadCacheData();
                    obj = encacheService.findData( cache, okey );
                }
                if( obj != null )
                {
                    Map<String,Object> dataMap = (Map<String,Object>)obj;
                    if( ucontext.getCzId() != null && ucontext.getCzId().longValue() > 0 )
                    {
                        if( dataMap.containsKey( ucontext.getCzId() + "__" + itemCode ) )
                            dataObj = (ConfigAppParam)dataMap.get( ucontext.getCzId() + "__" + itemCode );
                    }
                    if( dataObj == null )
                    {
                        if( dataMap.containsKey( itemCode ) )
                            dataObj = (ConfigAppParam)dataMap.get( itemCode );
                    }
                }
            }
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
        List resultList = null;
        String ckey = SystemConstants.CACHE_NAME_CONFIG_DATA;
        String okey = ConfigAppParam.class.getSimpleName();
        Cache cache = encacheService.findCache( ckey );
        if( cache != null )
        {
            Object obj = encacheService.findData( cache, okey );
            if( obj == null )
            {
                reloadCacheData();
                obj = encacheService.findData( cache, okey );
            }
            if( obj != null )
            {
                Map<String,Object> dataMap = (Map<String,Object>)obj;
                if( dataMap.containsKey( SystemConstants.CACHE_DATA_KEY_ALL_LIST ) )
                {
                    resultList = (List<ConfigAppParam>)dataMap.get( SystemConstants.CACHE_DATA_KEY_ALL_LIST );
                }
            }
        }
        
        return resultList;
    }
    
    public void setEncacheService(IEncacheService encacheService)
    {
        this.encacheService = encacheService;
    }
    
}
