package com.lidennis.plat.config.service.impl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.lidennis.plat.common.constant.Constants;
import com.lidennis.plat.common.model.UserContext;
import com.lidennis.plat.common.service.AbstractBaseService;
import com.lidennis.plat.common.service.ICacheService;
import com.lidennis.plat.config.model.ConfigRptMainType;
import com.lidennis.plat.system.constant.SystemConstants;

public class ConfigRptMainTypeServiceImpl extends AbstractBaseService implements ICacheService
{
    private static final Logger log = Logger.getLogger( ConfigRptMainTypeServiceImpl.class );
    protected Map<String,Object> globalAppConfigBaseMap;
    
    @Override
    @SuppressWarnings("unchecked")
    public void reloadCacheData()
    {
        if( globalAppConfigBaseMap != null )
        {
            Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put( "enableFlag", Constants.BOOLEAN_VALUE_TRUE );
            List<ConfigRptMainType> resultList = baseIbatisDao.findByParams( ConfigRptMainType.class.getSimpleName(), paramMap, null );
            synchronized( globalAppConfigBaseMap )
            {
                if( globalAppConfigBaseMap.containsKey( ConfigRptMainType.class.getSimpleName() ) )
                    globalAppConfigBaseMap.remove( ConfigRptMainType.class.getSimpleName() );
                Map<String,Object> dataMap = new ConcurrentHashMap<String,Object>();
                if( resultList != null && resultList.size() > 0 )
                {
                    for( int i=0; i<resultList.size(); i++ )
                    {
                        ConfigRptMainType dataObj = resultList.get(i);
                        if( dataObj.getCzCode() != null && !dataObj.getCzCode().trim().equals("") )
                        {
                            if( dataObj.getItemCode() != null && !dataObj.getItemCode().trim().equals("") )
                                dataMap.put( dataObj.getCzCode()+"_"+dataObj.getItemCode(), dataObj );
                            dataMap.put( dataObj.getCzCode()+"_"+dataObj.getItemId().toString(), dataObj );
                        }
                        else
                        {
                            if( dataObj.getItemCode() != null && !dataObj.getItemCode().trim().equals("") )
                                dataMap.put( dataObj.getItemCode(), dataObj ); 
                            dataMap.put( dataObj.getItemId().toString(), dataObj );
                        }
                    }
                    dataMap.put( "all_result_data_list", resultList );
                }
                
                globalAppConfigBaseMap.put( ConfigRptMainType.class.getSimpleName(), dataMap );
                if( SystemConstants.LOG_SWITCH_PROC_DEBUG_ON && SystemConstants.LOG_SWITCH_PACKAGE_INFO.length() >= 15 && SystemConstants.LOG_SWITCH_PACKAGE_INFO.indexOf( this.getClass().getPackage().getName() ) >= 0 )
                    log.info( "--> load reprot type to cache, total: "+dataMap.size() );
            }
        }
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> T findCacheById( UserContext userContext, Long itemId )
    {
        ConfigRptMainType dataObj = null;
        if( globalAppConfigBaseMap.containsKey( ConfigRptMainType.class.getSimpleName() ) )
        {
            Map<String,Object> dataMap = (Map<String,Object>)globalAppConfigBaseMap.get( ConfigRptMainType.class.getSimpleName() );
            if( userContext != null && itemId != null )
            {
                if( dataMap.containsKey( itemId.toString() ) )
                {
                    dataObj = (ConfigRptMainType)dataMap.get( itemId.toString() );
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
    public <T> T findCacheByCode( UserContext userContext, String itemCode )
    {
        ConfigRptMainType dataObj = null;
        if( globalAppConfigBaseMap.containsKey( ConfigRptMainType.class.getSimpleName() ) )
        {
            Map<String,Object> dataMap = (Map<String,Object>)globalAppConfigBaseMap.get( ConfigRptMainType.class.getSimpleName() );
            if( userContext != null && itemCode != null )
            {
                if( dataMap.containsKey( itemCode ) )
                {
                    dataObj = (ConfigRptMainType)dataMap.get( itemCode );
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
    public <T> List<T> findCacheListByParams( UserContext userContext, Map<String, Object> paramMap )
    {
        List resultList = new ArrayList();
        if( userContext != null && userContext.getCzCode() != null && !userContext.getCzCode().equals("") )
        {
            if( globalAppConfigBaseMap.containsKey( ConfigRptMainType.class.getSimpleName() ) )
            {
                Map<String,Object> dataMap = (Map<String,Object>)globalAppConfigBaseMap.get( ConfigRptMainType.class.getSimpleName() );
                if( dataMap.containsKey("all_result_data_list") )
                {
                    resultList = (List<ConfigRptMainType>)dataMap.get( "all_result_data_list" );
                }
            }
        }
        return resultList;
    }

    public void setGlobalAppConfigBaseMap(Map<String, Object> globalAppConfigBaseMap)
    {
        this.globalAppConfigBaseMap = globalAppConfigBaseMap;
    }
}
