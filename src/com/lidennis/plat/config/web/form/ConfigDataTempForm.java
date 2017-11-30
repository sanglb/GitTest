package com.lidennis.plat.config.web.form;
import com.lidennis.plat.config.model.ConfigDataTemp;

public class ConfigDataTempForm extends ConfigDataTemp
{
    private static final long serialVersionUID = 6674415833055873018L;

    public String toString()
    {
        StringBuffer str = new StringBuffer("");
        str.append( "Form [id=" ).append( getItemId() );
        str.append( ", code=" ).append( getItemCode() );
        str.append( ", name=" ).append( getItemName() );
        str.append( ", enabled=" ).append( getEnableFlag() );
        str.append( "]" );
        return str.toString();
    }
}
