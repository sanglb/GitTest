package com.lidennis.plat.config.service.impl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.ehcache.Cache;

import org.apache.log4j.Logger;

import com.lidennis.plat.common.constant.Constants;
import com.lidennis.plat.common.model.ReturnMsg;
import com.lidennis.plat.common.model.UserContext;
import com.lidennis.plat.common.service.AbstractBaseService;
import com.lidennis.plat.common.service.ICacheService;
import com.lidennis.plat.common.service.IEncacheService;
import com.lidennis.plat.common.util.StringUtils;
import com.lidennis.plat.config.model.ConfigDictDetail;
import com.lidennis.plat.config.model.ConfigDictType;
import com.lidennis.plat.system.constant.SystemConstants;

public class ConfigDictTypeServiceImpl extends AbstractBaseService implements ICacheService
{
    private static final Logger log = Logger.getLogger( ConfigDictTypeServiceImpl.class );
    protected IEncacheService encacheService;
    
    @Override
    protected <T> ReturnMsg checkBySave( UserContext ucontext, final T model, Map<String, Object> exparamMap ) throws Exception
    {
        ReturnMsg returnMsg = super.checkBySave( ucontext, model, exparamMap );
        if( returnMsg.isSuccess() )
        {
            if( model instanceof ConfigDictType )
            {
                ConfigDictType dataObj = (ConfigDictType)model;
                if( dataObj.getCzId() == null || dataObj.getCzId().longValue() <= 0 )
                {
                    if( ucontext != null && StringUtils.checkEquals( ucontext.getUserAcct(), Constants.COMMON_MODEL_CODE_SENIOR ) )
                    {
                        dataObj.setCzId( 0L );
                    }
                    else
                    {
                        returnMsg.setSuccess( false );
                        returnMsg.setMessage( Constants.MSG_DATA_NOT_COMPLETE_INPUT );
                        returnMsg.setEnMessage( Constants.MSG_DATA_NOT_COMPLETE_INPUT_EN );
                        return returnMsg;
                    }
                }
                
                if( dataObj.getItemCode() == null || dataObj.getItemCode().trim().equals("") )
                {
                    returnMsg.setSuccess( false );
                    returnMsg.setMessage( Constants.MSG_CODE_NO_EMPTY );
                    returnMsg.setEnMessage( Constants.MSG_CODE_NO_EMPTY_EN );
                    return returnMsg;
                }
                
                Map<String,Object> pmap = new HashMap<String,Object>();
                pmap.put( Constants.COMMON_MODEL_CODE_CODE, dataObj.getItemCode() );
                pmap.put( Constants.COMMON_MODEL_CODE_CZ_ID, dataObj.getCzId() );
                if( dataObj.getItemId() != null && dataObj.getItemId().longValue() > 0 )
                    pmap.put( "itemId_notexist", dataObj.getItemId() );
                int num = baseIbatisDao.findCountByParams( ConfigDictType.class.getSimpleName(), pmap );
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
    protected <T> ReturnMsg checkByDelete( UserContext ucontext, final T model, Map<String, Object> exparamMap ) throws Exception
    {
        ReturnMsg returnMsg = super.checkByDelete( ucontext, model, exparamMap );
        if( returnMsg.isSuccess() )
        {
            if( model instanceof ConfigDictType )
            {
                ConfigDictType dataObj = (ConfigDictType)model;
                Map<String,Object> pmap1 = new HashMap<String,Object>();
                pmap1.put( "dictTypeId", dataObj.getItemId() );
                int num = baseIbatisDao.findCountByParams( ConfigDictDetail.class.getSimpleName(), pmap1 );
                if( num > 0 )
                {
                    returnMsg.setSuccess( false );
                    returnMsg.setMessage( "数据["+dataObj.getItemName()+"]已被数据字典引用, 不能删除." );
                    returnMsg.setEnMessage( "the data["+dataObj.getItemName()+"] has been referenced data dictionary, can not be deleted." );
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
        String okey = ConfigDictType.class.getSimpleName();
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
            List<ConfigDictType> dlist = baseIbatisDao.findByParams( ConfigDictType.class.getSimpleName(), pmap, null );
            if( dlist != null && dlist.size() > 0 )
            {
                for( int i=0; i<dlist.size(); i=i+1 )
                {
                    ConfigDictType dobj = dlist.get(i);
                    dataMap.put( dobj.getItemId().toString(), dobj );
                    if( dobj.getCzId() != null && dobj.getCzId().longValue() > 0 )
                    {
                        if( StringUtils.checkNotEmpty( dobj.getItemCode() ) )
                            dataMap.put( dobj.getCzId() + "__" + dobj.getItemCode(), dobj );
                    }
                    else
                    {
                        if( StringUtils.checkNotEmpty( dobj.getItemCode() ) )
                            dataMap.put( dobj.getItemCode(), dobj );
                    }
                    
                }
                
                dataMap.put( SystemConstants.CACHE_DATA_KEY_ALL_LIST, dlist );
            }
            
            if( SystemConstants.LOG_SWITCH_PROC_DEBUG_ON && SystemConstants.LOG_SWITCH_PACKAGE_INFO.length() >= 15 && SystemConstants.LOG_SWITCH_PACKAGE_INFO.indexOf( this.getClass().getPackage().getName() ) >= 0 )
                log.info( "--> load data dict to cache, total: "+dataMap.size() );
        }
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> T findCacheById( UserContext ucontext, Long itemId )
    {
        ConfigDictType dataObj = null;
        if( ucontext != null && itemId != null )
        {
            String ckey = SystemConstants.CACHE_NAME_CONFIG_DATA;
            String okey = ConfigDictType.class.getSimpleName();
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
                        dataObj = (ConfigDictType)dataMap.get( itemId.toString() );
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
        ConfigDictType dataObj = null;
        if( ucontext != null && StringUtils.checkNotEmpty( itemCode ) )
        {
            String ckey = SystemConstants.CACHE_NAME_CONFIG_DATA;
            String okey = ConfigDictType.class.getSimpleName();
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
                            dataObj = (ConfigDictType)dataMap.get( ucontext.getCzId() + "__" + itemCode );
                    }
                    if( dataObj == null )
                    {
                        if( dataMap.containsKey( itemCode ) )
                            dataObj = (ConfigDictType)dataMap.get( itemCode );
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
        String okey = ConfigDictType.class.getSimpleName();
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
                    resultList = (List<ConfigDictType>)dataMap.get( SystemConstants.CACHE_DATA_KEY_ALL_LIST );
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
