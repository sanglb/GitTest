package com.lidennis.plat.config.web.action;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.lidennis.plat.common.constant.Constants;
import com.lidennis.plat.common.model.ReturnMsg;
import com.lidennis.plat.common.model.UserContext;
import com.lidennis.plat.common.service.ICommonService;
import com.lidennis.plat.common.util.SpringContextUtils;
import com.lidennis.plat.common.util.StringUtils;
import com.lidennis.plat.common.web.base.AbstractBaseAction;
import com.lidennis.plat.config.model.ConfigAppParam;
import com.lidennis.plat.config.model.ConfigSMSGateway;
import com.lidennis.plat.system.iface.ISyncDataService;
import com.lidennis.plat.system.service.ICommonDataUtils;
import com.lidennis.plat.system.service.IDataDetectionProcess;

public class ConfigSystemDataAction extends AbstractBaseAction
{
    private static final long serialVersionUID = 1L;
    private static final Logger log = Logger.getLogger( ConfigSystemDataAction.class );
    
    protected ICommonService   commonService;
    protected ICommonDataUtils commonDataUtils;
    protected IDataDetectionProcess dataDetectionProcess;
    protected ISyncDataService configAppParamService;
    protected ISyncDataService configGatewayMainService;
    
    protected String  cfgclfy1;             
    protected String  cfgclfy2;             
    protected String  proccode;
    protected String  cfgcd1;               
    protected String  cfgcd2;
    protected String  cfginfo1;             
    protected String  cfginfo2;             
    protected String  cfgval1;              
    protected String  cfgval2;
    protected String  cfgval3;
    protected String  cfgval4;
    protected String  cfgval5;
    protected String  cfgval6;
    protected String  cfgval7;
    protected String  cfgval8;
    protected String  cfgval9;
    protected String  cfgval10;
    protected Integer cfgflag1;             
    protected Integer cfgflag2;                
    
    public String findmain() 
    {
        UserContext ucontext = this.findUserContext();
        if( ucontext != null )
        {
            try
            {
                if( ucontext.getRoleInfo() != null && ucontext.getRoleInfo().indexOf( "admin" ) >= 0 )
                {
                    if( cfgclfy1 != null && cfgclfy1.length() > 1 && cfgclfy1.length() < 20 && cfgcd1 != null && cfgcd1.length() >= 5 && cfgcd1.length() < 50 )
                    {
                        String bcode1 = "sync_cfg_params_by_web";  
                        if( cfgclfy1.indexOf( "param" ) == 0 ) 
                        { 
                            resultList = configAppParamService.findDataByBizCode( ucontext, bcode1, "", cfgcd1, null );
                        }
                        else if( cfgclfy1.indexOf( "gw" ) == 0 ) 
                        {
                            resultList = configGatewayMainService.findDataByBizCode( ucontext, bcode1, "", cfgcd1, null );
                        }
                        else if( proccode != null && proccode.length() > 5 && proccode.length() < 50 )
                        {
                            if( ucontext.getRoleInfo().indexOf( "superadmin" ) >= 0 )
                            {
                                ISyncDataService proc1 = this.findDataProcBean( "", proccode );
                                if( proc1 != null )
                                {
                                    resultList = proc1.findDataByBizCode( ucontext, bcode1, "", cfgcd1, null );
                                }
                            }
                        }
                    }
                }
            }
            catch( Exception err ) 
            {
                log.error( "findmain error, " + err.toString() + ", clfy1: " + cfgclfy1 + ", code1: " + cfgcd1 + ", by: " + ucontext.getUserAcct() + ", " + ucontext.getUserInfo() ); 
            }
        }
        
        return SUCCESS;
    }
    
    public String save()
    {
        UserContext ucontext = this.findUserContext();
        if( ucontext != null )
        {
            returnMsg = new ReturnMsg( false );
            try
            {
                if( ucontext.getRoleInfo() != null && ucontext.getRoleInfo().indexOf( "admin" ) >= 0 )
                {
                    if( cfgclfy1 != null && cfgclfy1.length() > 1 && cfgclfy1.length() < 20 && cfgcd1 != null && cfgcd1.length() > 5 && cfgcd1.length() < 50 )
                    {
                        Map<String,Object> dmap1 = new HashMap<String,Object>();
                        dmap1.put( "cfgcode1", cfgcd1 );  
                        if( cfgval1 != null )
                            dmap1.put( "cfgval1", cfgval1 );
                        if( cfgval2 != null )
                            dmap1.put( "cfgval2", cfgval2 );
                        if( cfgval3 != null )
                            dmap1.put( "cfgval3", cfgval3 );
                        if( cfgval4 != null )
                            dmap1.put( "cfgval4", cfgval4 );
                        if( cfgval5 != null )
                            dmap1.put( "cfgval5", cfgval5 );
                        if( cfgval6 != null )
                            dmap1.put( "cfgval6", cfgval6 );
                        if( cfgval7 != null )
                            dmap1.put( "cfgval7", cfgval7 );
                        if( cfgval8 != null )
                            dmap1.put( "cfgval8", cfgval8 );
                        if( cfgval9 != null )
                            dmap1.put( "cfgval9", cfgval9 );
                        if( cfgval10 != null )
                            dmap1.put( "cfgval10", cfgval10 );
                        
                        if( dmap1.size() > 0 )
                        {
                            String bcode1 = "sync_cfg_params_by_web";  
                            dmap1.put( "cfgclfy1", cfgclfy1 );
                            if( cfgclfy2 != null && cfgclfy2.trim().length() > 1 && cfgclfy2.length() < 50 )
                                dmap1.put( "cfgclfy2", cfgclfy2 );
                            if( cfginfo1 != null && cfginfo1.trim().length() > 1 && cfginfo1.length() < 50 )
                                dmap1.put( "cfginfo1", cfginfo1 );
                            if( cfginfo2 != null && cfginfo2.trim().length() > 0 && cfginfo2.length() < 500 )
                                dmap1.put( "cfginfo2", cfginfo2 );
                            if( cfgflag1 != null )
                                dmap1.put( "cfgflag1", cfgflag1 );
                            if( cfgflag2 != null )
                                dmap1.put( "cfgflag2", cfgflag2 );
                            if( cfgclfy1.indexOf( "param" ) == 0 ) 
                            {
                                returnMsg = configAppParamService.syncDataProc( ucontext, null, bcode1, null, dmap1 );
                                if( returnMsg.isSuccess() )
                                {
                                    try {
                                        dataDetectionProcess.updateNotify( ucontext, ConfigAppParam.class.getSimpleName(), "configAppParamService" );
                                    }
                                    catch( Exception e ){
                                    }
                                }
                            }
                            else if( cfgclfy1.indexOf( "gw" ) == 0 ) 
                            {
                                returnMsg = configGatewayMainService.syncDataProc( ucontext, null, bcode1, null, dmap1 );
                                if( returnMsg.isSuccess() )
                                {
                                    try {
                                        dataDetectionProcess.updateNotify( ucontext, ConfigSMSGateway.class.getSimpleName(), "configGatewayMainService" );
                                    }
                                    catch( Exception e ){
                                    }
                                }
                            }
                            else if( proccode != null && proccode.length() > 5 && proccode.length() < 50 )  
                            {
                                if( ucontext.getRoleInfo().indexOf( "superadmin" ) >= 0 )
                                {
                                    ISyncDataService proc1 = this.findDataProcBean( "", proccode );
                                    if( proc1 != null )
                                    {
                                        returnMsg = proc1.syncDataProc( ucontext, null, bcode1, null, dmap1 );
                                    }
                                }
                            }
                        }
                    }
                }
            }
            catch( Exception err ) 
            {
                log.error( "save error, " + err.toString() + ", clfy1: " + cfgclfy1 + ", code1: " + cfgcd1 + ", by: " + ucontext.getUserAcct() + ", " + ucontext.getUserInfo() ); 
                returnMsg.setSuccess( false );
                returnMsg.setMessage( Constants.MSG_OPERATE_ERROR_PREFIX + err.getMessage() );
                returnMsg.setEnMessage( Constants.MSG_OPERATE_ERROR_PREFIX_EN + err.getMessage() );
            }
        }
        
        return SUCCESS;
    }
    
    public String test()
    {
        UserContext ucontext = this.findUserContext();
        if( ucontext != null )
        {
            returnMsg = new ReturnMsg( false );
            try
            {
                if( ucontext.getRoleInfo() != null && ucontext.getRoleInfo().indexOf( "admin" ) >= 0 )
                {
                    if( cfgclfy1 != null && cfgclfy1.length() > 1 && cfgclfy1.length() < 20 && cfgcd1 != null && cfgcd1.length() > 5 && cfgcd1.length() < 50 )
                    {
                        
                    }
                }
            }
            catch( Exception err ) 
            {
                log.error( "test error, " + err.toString() + ", clfy1: " + cfgclfy1 + ", code1: " + cfgcd1 + ", by: " + ucontext.getUserAcct() + ", " + ucontext.getUserInfo() ); 
                returnMsg.setSuccess( false );
                returnMsg.setMessage( Constants.MSG_OPERATE_ERROR_PREFIX + err.getMessage() );
                returnMsg.setEnMessage( Constants.MSG_OPERATE_ERROR_PREFIX_EN + err.getMessage() );
            }
        }
        
        return SUCCESS;
    }
    
    private ISyncDataService findDataProcBean( String bizType, String beanName )
    {
        ISyncDataService proc1 = null;
        if( StringUtils.checkNotEmpty( beanName ) )
        {
            Object proc2 = SpringContextUtils.findBeanObj( beanName );
            if( proc2 != null && proc2 instanceof ISyncDataService )
            {
                proc1 = (ISyncDataService)proc2;
            }
            else
            {
                log.error( "findDataProcBean failed, [" + beanName + "] not exist, bizType: " + bizType );
            }
        }
        
        return proc1;
    }
    
    public void setCommonService(ICommonService commonService)
    {
        this.commonService = commonService;
    }
    public void setCommonDataUtils(ICommonDataUtils commonDataUtils)
    {
        this.commonDataUtils = commonDataUtils;
    }
    public void setDataDetectionProcess(IDataDetectionProcess dataDetectionProcess)
    {
        this.dataDetectionProcess = dataDetectionProcess;
    }
    public void setConfigGatewayMainService(ISyncDataService configGatewayMainService)
    {
        this.configGatewayMainService = configGatewayMainService;
    }
    public void setConfigAppParamService(ISyncDataService configAppParamService)
    {
        this.configAppParamService = configAppParamService;
    }
    public void setCfgclfy1(String cfgclfy1)
    {
        this.cfgclfy1 = cfgclfy1;
    }
    public void setCfgclfy2(String cfgclfy2)
    {
        this.cfgclfy2 = cfgclfy2;
    }
    public void setProccode(String proccode)
    {
        this.proccode = proccode;
    }
    public void setCfgcd1(String cfgcd1)
    {
        this.cfgcd1 = cfgcd1;
    }
    public void setCfgcd2(String cfgcd2)
    {
        this.cfgcd2 = cfgcd2;
    }
    public void setCfginfo1(String cfginfo1)
    {
        this.cfginfo1 = cfginfo1;
    }
    public void setCfginfo2(String cfginfo2)
    {
        this.cfginfo2 = cfginfo2;
    }
    public void setCfgval1(String cfgval1)
    {
        this.cfgval1 = cfgval1;
    }
    public void setCfgval2(String cfgval2)
    {
        this.cfgval2 = cfgval2;
    }
    public void setCfgval3(String cfgval3)
    {
        this.cfgval3 = cfgval3;
    }
    public void setCfgval4(String cfgval4)
    {
        this.cfgval4 = cfgval4;
    }
    public void setCfgval5(String cfgval5)
    {
        this.cfgval5 = cfgval5;
    }
    public void setCfgval6(String cfgval6)
    {
        this.cfgval6 = cfgval6;
    }
    public void setCfgval7(String cfgval7)
    {
        this.cfgval7 = cfgval7;
    }
    public void setCfgval8(String cfgval8)
    {
        this.cfgval8 = cfgval8;
    }
    public void setCfgval9(String cfgval9)
    {
        this.cfgval9 = cfgval9;
    }
    public void setCfgval10(String cfgval10)
    {
        this.cfgval10 = cfgval10;
    }
    public void setCfgflag1(Integer cfgflag1)
    {
        this.cfgflag1 = cfgflag1;
    }
    public void setCfgflag2(Integer cfgflag2)
    {
        this.cfgflag2 = cfgflag2;
    }
    
}
