package com.lidennis.plat.config.web.form;
import com.lidennis.plat.config.model.ConfigConnServer;

public class ConfigConnServerForm extends ConfigConnServer
{
    private static final long serialVersionUID = -2969825642516971495L;

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
